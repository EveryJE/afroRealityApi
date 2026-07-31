package com.example.afrorealityapi.common.storage;

import com.example.afrorealityapi.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    /**
     * Upload or replace a file.
     *
     * @param file       the file to upload
     * @param folder     logical category: "avatars", "events", "documents", "tickets"
     * @param resourceId stable ID for the resource (e.g. userId, eventId).
     *                   When the same resourceId + folder combo is used again,
     *                   the previous file is automatically replaced — no orphans.
     *                   Omit for a one-off upload with a random key.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<StorageDtos.FileUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "documents") String folder,
            @RequestParam(value = "resourceId", required = false) String resourceId) {
        return ResponseEntity.ok(ApiResponse.ok(storageService.uploadFile(file, folder, resourceId)));
    }

    /**
     * Delete a file by its deterministic key (folder + resourceId).
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteFile(
            @RequestParam("folder") String folder,
            @RequestParam("resourceId") String resourceId) {
        storageService.deleteByKey(folder, resourceId);
        return ResponseEntity.ok(ApiResponse.ok("File deleted successfully", null));
    }

    /**
     * Delete a file by its full URL (legacy / fallback).
     */
    @DeleteMapping("/delete-by-url")
    public ResponseEntity<ApiResponse<String>> deleteByUrl(@RequestParam("fileUrl") String fileUrl) {
        storageService.deleteByUrl(fileUrl);
        return ResponseEntity.ok(ApiResponse.ok("File deleted successfully", null));
    }
}
