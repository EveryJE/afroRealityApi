package com.example.afrorealityapi.promoter.service;

import com.example.afrorealityapi.promoter.dto.PromoterDtos.ApplyPromoterRequest;
import com.example.afrorealityapi.promoter.dto.PromoterDtos.PromoterResponse;

import java.util.UUID;

public interface PromoterService {
    PromoterResponse applyAsPromoter(ApplyPromoterRequest request);
    PromoterResponse getPromoterByUserId(UUID userId);
    PromoterResponse getPromoterByCode(String referralCode);
}
