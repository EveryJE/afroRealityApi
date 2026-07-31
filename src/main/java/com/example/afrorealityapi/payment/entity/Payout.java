package com.example.afrorealityapi.payment.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.CurrencyCode;
import com.example.afrorealityapi.common.enums.FinancialStatus;
import com.example.afrorealityapi.common.enums.PaymentProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payouts")
public class Payout extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(name = "wallet_id", insertable = false, updatable = false)
    private UUID walletId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "approved_by", insertable = false, updatable = false)
    private UUID approvedBy;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "approved_by")
    private com.example.afrorealityapi.user.entity.Profile approver;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CurrencyCode currency = CurrencyCode.GHS;

    @Column(name = "fee_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal feeAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FinancialStatus status = FinancialStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;

    @Column(name = "provider_reference")
    private String providerReference;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "provider_response")
    private Map<String, Object> providerResponse;

    private String description;

    private String notes;

    @Column(name = "requires_approval", nullable = false)
    @Builder.Default
    private boolean requiresApproval = false;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "failed_at")
    private OffsetDateTime failedAt;
}
