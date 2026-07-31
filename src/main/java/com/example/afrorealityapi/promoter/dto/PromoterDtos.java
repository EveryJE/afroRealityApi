package com.example.afrorealityapi.promoter.dto;

import com.example.afrorealityapi.common.enums.PromoterStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

public class PromoterDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApplyPromoterRequest {
        private UUID userId;
        private String referralCode;
        private String bankName;
        private String bankAccountNumber;
        private String bankAccountName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromoterResponse {
        private UUID id;
        private UUID userId;
        private String referralCode;
        private PromoterStatus status;
        private BigDecimal commissionRate;
        private int tier;
        private boolean isGoldTier;
        private String bankName;
        private String bankAccountNumber;
    }
}
