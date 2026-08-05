package com.example.afrorealityapi.common.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

@Slf4j
@RestController
public class HealthController {

    @GetMapping({"/api/v1/health", "/api/v1/health/", "/health", "/health/", "/ping", "/ping/"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        log.info("Health check ping received — service is UP");
        Map<String, Object> data = Map.of(
                "status", "UP",
                "service", "afroRealityApi",
                "timestamp", OffsetDateTime.now()
        );
        return ResponseEntity.ok(ApiResponse.ok("Server is healthy and active", data));
    }
}

