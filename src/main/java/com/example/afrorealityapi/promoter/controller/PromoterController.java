package com.example.afrorealityapi.promoter.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.promoter.dto.PromoterDtos.ApplyPromoterRequest;
import com.example.afrorealityapi.promoter.dto.PromoterDtos.PromoterResponse;
import com.example.afrorealityapi.promoter.service.PromoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promoters")
@RequiredArgsConstructor
public class PromoterController {

    private final PromoterService promoterService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<PromoterResponse>> apply(@RequestBody ApplyPromoterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(promoterService.applyAsPromoter(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PromoterResponse>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(promoterService.getPromoterByUserId(userId)));
    }

    @GetMapping("/code/{referralCode}")
    public ResponseEntity<ApiResponse<PromoterResponse>> getByCode(@PathVariable String referralCode) {
        return ResponseEntity.ok(ApiResponse.ok(promoterService.getPromoterByCode(referralCode)));
    }
}
