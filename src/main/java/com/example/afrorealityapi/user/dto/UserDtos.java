package com.example.afrorealityapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class UserDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileResponse {
        private UUID id;
        private String email;
        private String username;
        private String fullName;
        private String avatarUrl;
        private String phone;
        private boolean whatsappOptIn;
        private String pricingPlan;
        private BigDecimal communicationCredits;
        private boolean isVerifiedPartner;
        private boolean onboardingCompleted;
        private int onboardingStep;
        private OffsetDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateProfileRequest {
        private String username;
        private String fullName;
        private String avatarUrl;
        private String phone;
        private Boolean whatsappOptIn;
    }
}
