package com.example.afrorealityapi.common.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StorageDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileUploadResponse {
        private String fileName;
        private String fileUrl;
        private String contentType;
        private long size;
        private String category;    // "events", "avatars", "documents", "tickets"
        private String storageKey;  // deterministic key for future updates/deletes
    }
}
