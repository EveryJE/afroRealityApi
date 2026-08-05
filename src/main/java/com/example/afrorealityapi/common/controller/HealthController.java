package com.example.afrorealityapi.common.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping({"/api/v1/health", "/api/v1/health/", "/health", "/health/", "/ping", "/ping/"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> data = Map.of(
                "status", "UP",
                "service", "afroRealityApi",
                "timestamp", OffsetDateTime.now()
        );
        return ResponseEntity.ok(ApiResponse.ok("Server is healthy and active", data));
    }
}
