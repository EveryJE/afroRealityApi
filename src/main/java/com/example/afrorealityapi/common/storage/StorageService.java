package com.example.afrorealityapi.common.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * Upload a file with a deterministic storage key.
     * If a file already exists at this key, it is automatically replaced (no orphans).
     *
     * @param file       the multipart file to upload
     * @param folder     logical folder / category (e.g. "avatars", "events", "documents")
     * @param resourceId a stable identifier for the resource (e.g. userId, eventId)
     *                   — ensures the same resource always maps to the same storage key.
     *                   Pass null for a one-off upload that gets a random key.
     * @return upload response with the public URL
     */
    StorageDtos.FileUploadResponse uploadFile(MultipartFile file, String folder, String resourceId);

    /**
     * Delete a file by its storage key (folder + resourceId).
     *
     * @param folder     the folder the file lives in
     * @param resourceId the resource identifier used when uploading
     */
    void deleteByKey(String folder, String resourceId);

    /**
     * Delete a file by its full public URL (legacy / fallback).
     */
    void deleteByUrl(String fileUrl);
}
