package com.example.afrorealityapi.promoter.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.CommissionStatus;
import com.example.afrorealityapi.common.enums.CommissionType;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "commissions")
public class Commission extends BaseEntity {

    @Column(name = "promoter_id", insertable = false, updatable = false)
    private UUID promoterId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "promoter_id", nullable = false)
    private Promoter promoter;

    @Column(name = "referral_id", insertable = false, updatable = false)
    private UUID referralId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "referral_id")
    private Referral referral;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CommissionStatus status = CommissionStatus.PENDING;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    @Builder.Default
    private String currency = "GHS";

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(name = "base_amount", precision = 15, scale = 2)
    private BigDecimal baseAmount;

    @Column(name = "commission_rate", precision = 5, scale = 2)
    private BigDecimal commissionRate;

    private String description;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Column(name = "payment_reference")
    private String paymentReference;
}
