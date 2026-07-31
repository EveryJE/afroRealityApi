package com.example.afrorealityapi.payment.dto;

import com.example.afrorealityapi.common.enums.CurrencyCode;
import com.example.afrorealityapi.common.enums.FinancialStatus;
import com.example.afrorealityapi.common.enums.PaymentProvider;
import com.example.afrorealityapi.common.enums.PaymentPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PaymentDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InitiatePaymentRequest {
        private UUID userId;
        private String email;
        private PaymentPurpose purpose;
        private BigDecimal amount;
        private CurrencyCode currency;
        private PaymentProvider provider;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentResponse {
        private UUID id;
        private String reference;
        private String email;
        private PaymentPurpose purpose;
        private BigDecimal amount;
        private CurrencyCode currency;
        private PaymentProvider provider;
        private FinancialStatus status;
        private String authorizationUrl;
        private OffsetDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WalletResponse {
        private UUID id;
        private UUID userId;
        private UUID organizationId;
        private BigDecimal balance;
        private CurrencyCode currency;
        private BigDecimal pendingCredits;
        private BigDecimal pendingDebits;
        private boolean isActive;
    }
}
