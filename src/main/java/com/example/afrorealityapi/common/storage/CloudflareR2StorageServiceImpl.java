package com.example.afrorealityapi.common.storage;

import com.example.afrorealityapi.common.exception.ApiException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class CloudflareR2StorageServiceImpl implements StorageService {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.local-upload-path:/uploads}")
    private String localUploadPath;

    @Value("${app.r2.endpoint:}")
    private String endpoint;

    @Value("${app.r2.access-key:}")
    private String accessKey;

    @Value("${app.r2.secret-key:}")
    private String secretKey;

    @Value("${app.r2.bucket-name:afroreality-uploads}")
    private String bucketName;

    @Value("${app.r2.public-url-prefix:}")
    private String publicUrlPrefix;

    // ── Constants ────────────────────────────────────────────────────────────
    
    private static final String WEBP_CONTENT_TYPE = "image/webp";
    private static final String WEBP_EXTENSION = ".webp";
    private static final String LOCAL_UPLOADS_DIR = "uploads";
    private static final long MAX_FILE_SIZE_2MB = 2L * 1024 * 1024; // 2 MB
    
    private static final Set<String> CONVERTIBLE_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/bmp", "image/tiff", "image/gif"
    );

    // ── Public Interface API ──────────────────────────────────────────────────

    @Override
    public StorageDtos.FileUploadResponse uploadFile(MultipartFile file, String folder, String resourceId) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("File to upload cannot be empty");
        }

        String targetFolder = sanitizeFolder(folder);
        validateFileSizeAndExtension(file, targetFolder);

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String contentType = file.getContentType();

        // ── WebP Conversion & Optimization ──
        boolean shouldConvert = contentType != null && CONVERTIBLE_IMAGE_TYPES.contains(contentType.toLowerCase());
        byte[] fileBytes;
        String finalExtension;
        String finalContentType;
        long finalSize;

        if (shouldConvert) {
            try {
                fileBytes = convertToWebpOptimized(file);
                finalExtension = WEBP_EXTENSION;
                finalContentType = WEBP_CONTENT_TYPE;
                finalSize = fileBytes.length;
                log.info("Converted & optimized image {} ({} bytes) → WebP ({} bytes)", 
                        originalName, file.getSize(), finalSize);
            } catch (Exception e) {
                log.warn("WebP conversion failed for {}, uploading original. Reason: {}", originalName, e.getMessage());
                fileBytes = getBytes(file);
                finalExtension = extractExtension(originalName);
                finalContentType = contentType;
                finalSize = file.getSize();
            }
        } else {
            fileBytes = getBytes(file);
            finalExtension = extractExtension(originalName);
            finalContentType = contentType;
            finalSize = file.getSize();
        }

        // ── Build Storage Key ──
        String fileName = (resourceId != null && !resourceId.isBlank())
                ? resourceId.trim() + finalExtension
                : UUID.randomUUID() + finalExtension;
        String key = targetFolder + "/" + fileName;

        // ── Upload Dispatcher ──
        if (!isR2Configured()) {
            return uploadLocally(fileBytes, originalName, targetFolder, fileName, key, finalContentType, finalSize);
        }
        return uploadToR2(fileBytes, originalName, targetFolder, fileName, key, finalContentType, finalSize);
    }

    @Override
    public void deleteByKey(String folder, String resourceId) {
        if (resourceId == null || resourceId.isBlank()) return;

        String prefix = sanitizeFolder(folder) + "/" + resourceId.trim();

        if (!isR2Configured()) {
            deleteLocalByPrefix(prefix);
            return;
        }
        deleteR2ByPrefix(prefix);
    }

    @Override
    public void deleteByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        if (!isR2Configured()) {
            deleteLocalByUrl(fileUrl);
            return;
        }
        deleteR2ByUrl(fileUrl);
    }

    // ── Internal Storage Engines ──────────────────────────────────────────────

    private StorageDtos.FileUploadResponse uploadToR2(byte[] fileBytes, String originalName, String targetFolder, 
                                                      String fileName, String key, String contentType, long size) {
        try (S3Client s3 = createS3Client()) {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3.putObject(putRequest, RequestBody.fromBytes(fileBytes));

            String fileUrl = buildPublicUrl(key);
            log.info("Successfully uploaded file to Cloudflare R2: {} (key={})", fileUrl, key);

            return StorageDtos.FileUploadResponse.builder()
                    .fileName(originalName)
                    .fileUrl(fileUrl)
                    .contentType(contentType)
                    .size(size)
                    .category(targetFolder)
                    .storageKey(key)
                    .build();
        } catch (Exception e) {
            log.error("Cloudflare R2 upload failed for key={}: {}", key, e.getMessage(), e);
            throw new ApiException("Failed to upload file to Cloudflare R2: " + e.getMessage());
        }
    }

    private StorageDtos.FileUploadResponse uploadLocally(byte[] fileBytes, String originalName, String targetFolder, 
                                                          String fileName, String key, String contentType, long size) {
        log.warn("Cloudflare R2 credentials not configured. Saving local copy to {}/{}", LOCAL_UPLOADS_DIR, key);
        try {
            Path uploadsDir = Paths.get(LOCAL_UPLOADS_DIR, targetFolder);
            if (!Files.exists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
            }
            Path targetPath = uploadsDir.resolve(fileName);
            Files.copy(new ByteArrayInputStream(fileBytes), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String localUrl = buildLocalUrl(targetFolder, fileName);
            return StorageDtos.FileUploadResponse.builder()
                    .fileName(originalName)
                    .fileUrl(localUrl)
                    .contentType(contentType)
                    .size(size)
                    .category(targetFolder)
                    .storageKey(key)
                    .build();
        } catch (Exception e) {
            log.error("Local file write failed for key={}: {}", key, e.getMessage());
            throw new ApiException("Failed to save file locally: " + e.getMessage());
        }
    }

    private void deleteR2ByPrefix(String prefix) {
        try (S3Client s3 = createS3Client()) {
            s3.listObjectsV2(b -> b.bucket(bucketName).prefix(prefix))
              .contents()
              .forEach(obj -> {
                  s3.deleteObject(DeleteObjectRequest.builder()
                          .bucket(bucketName)
                          .key(obj.key())
                          .build());
                  log.info("Deleted from R2: {}", obj.key());
              });
        } catch (Exception e) {
            log.error("Cloudflare R2 delete by prefix failed for prefix={}: {}", prefix, e.getMessage());
        }
    }

    private void deleteR2ByUrl(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            try (S3Client s3 = createS3Client()) {
                s3.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build());
                log.info("Deleted from R2: {}", key);
            }
        } catch (Exception e) {
            log.error("Cloudflare R2 delete by URL failed: {}", e.getMessage());
        }
    }

    private void deleteLocalByPrefix(String prefix) {
        try {
            Path dir = Paths.get(LOCAL_UPLOADS_DIR, prefix).getParent();
            if (dir != null && Files.exists(dir)) {
                String filePrefix = Paths.get(prefix).getFileName().toString();
                Files.list(dir)
                     .filter(p -> p.getFileName().toString().startsWith(filePrefix))
                     .forEach(p -> {
                         try {
                             Files.deleteIfExists(p);
                             log.info("Deleted local file: {}", p);
                         } catch (Exception e) {
                             log.error("Failed to delete local file {}: {}", p, e.getMessage());
                         }
                      });
            }
        } catch (Exception e) {
            log.error("Local delete by prefix failed: {}", e.getMessage());
        }
    }

    private void deleteLocalByUrl(String fileUrl) {
        if (fileUrl.contains(localUploadPath + "/")) {
            String relativePath = fileUrl.substring(fileUrl.indexOf(localUploadPath + "/") + localUploadPath.length() + 1);
            try {
                Files.deleteIfExists(Paths.get(LOCAL_UPLOADS_DIR, relativePath));
                log.info("Deleted local file: {}/{}", LOCAL_UPLOADS_DIR, relativePath);
            } catch (Exception e) {
                log.error("Failed to delete local file: {}", e.getMessage());
            }
        }
    }

    // ── High Performance WebP Compression ─────────────────────────────────────

    private byte[] convertToWebpOptimized(MultipartFile file) throws Exception {
        // Load the image in memory
        ImmutableImage image = ImmutableImage.loader().fromStream(file.getInputStream());
        
        // 1. Try Lossless conversion (Fastest & best quality)
        byte[] bytes = image.bytes(WebpWriter.DEFAULT.withLossless());
        if (bytes.length <= MAX_FILE_SIZE_2MB) {
            return bytes;
        }
        
        log.info("Lossless WebP size ({} bytes) exceeds 2MB limit. Running optimized compression loop...", bytes.length);

        // 2. Optimized progressive compression (Binary search style instead of stepping by 10)
        // High quality target (80) -> Mid target (50) -> Low target (30) to minimize write operations
        int[] qualities = {85, 65, 45, 30};
        for (int quality : qualities) {
            bytes = image.bytes(WebpWriter.DEFAULT.withQ(quality));
            if (bytes.length <= MAX_FILE_SIZE_2MB) {
                log.info("Compressed file to {} bytes using WebP quality = {}", bytes.length, quality);
                return bytes;
            }
        }
        
        log.warn("WebP compression reached minimum quality floor (30%). Storing file ({} bytes).", bytes.length);
        return bytes;
    }

    // ── Security & Validation ─────────────────────────────────────────────────

    private void validateFileSizeAndExtension(MultipartFile file, String folder) {
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        
        // Block script/executable files
        if (filename.endsWith(".exe") || filename.endsWith(".bat") || filename.endsWith(".sh") ||
            filename.endsWith(".php") || filename.endsWith(".jsp") || filename.endsWith(".html") ||
            filename.endsWith(".js") || filename.endsWith(".jar")) {
            throw new ApiException("Security risk: Uploading executable files is prohibited.");
        }

        long fileSize = file.getSize();
        long maxAllowedSize;

        switch (folder.toLowerCase()) {
            case "avatars":
                maxAllowedSize = 5L * 1024 * 1024; // 5 MB
                break;
            case "events":
                maxAllowedSize = 10L * 1024 * 1024; // 10 MB
                break;
            default:
                maxAllowedSize = 25L * 1024 * 1024; // 25 MB
                break;
        }

        if (fileSize > maxAllowedSize) {
            long maxMb = maxAllowedSize / (1024 * 1024);
            throw new ApiException("File size exceeds limit of " + maxMb + " MB for category '" + folder + "'");
        }
    }

    // ── Helper Utilities ──────────────────────────────────────────────────────

    private boolean isR2Configured() {
        return endpoint != null && !endpoint.isBlank()
                && accessKey != null && !accessKey.isBlank()
                && secretKey != null && !secretKey.isBlank();
    }

    private String sanitizeFolder(String folder) {
        return (folder != null && !folder.isBlank())
                ? folder.trim().replaceAll("^/+|/+$", "")
                : "documents";
    }

    private String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return dotIndex > 0 ? filename.substring(dotIndex) : "";
    }

    private byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new ApiException("Failed to read file bytes: " + e.getMessage());
        }
    }

    private String buildPublicUrl(String key) {
        if (publicUrlPrefix != null && !publicUrlPrefix.isBlank()) {
            return stripTrailingSlash(publicUrlPrefix) + "/" + key;
        }
        return stripTrailingSlash(endpoint) + "/" + bucketName + "/" + key;
    }

    private String buildLocalUrl(String targetFolder, String fileName) {
        return stripTrailingSlash(baseUrl) + localUploadPath + "/" + targetFolder + "/" + fileName;
    }

    private String extractKeyFromUrl(String fileUrl) {
        if (publicUrlPrefix != null && !publicUrlPrefix.isBlank() && fileUrl.startsWith(publicUrlPrefix)) {
            return fileUrl.substring(publicUrlPrefix.length()).replaceAll("^/+", "");
        }
        String marker = "/" + bucketName + "/";
        int idx = fileUrl.indexOf(marker);
        if (idx >= 0) {
            return fileUrl.substring(idx + marker.length());
        }
        return fileUrl;
    }

    private String stripTrailingSlash(String url) {
        return url != null ? url.replaceAll("/+$", "") : "";
    }

    private S3Client createS3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint.trim()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey.trim(), secretKey.trim())))
                .region(Region.US_EAST_1)
                .build();
    }
}
