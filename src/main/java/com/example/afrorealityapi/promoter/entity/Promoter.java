package com.example.afrorealityapi.promoter.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.PromoterStatus;
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
@Table(name = "promoters")
public class Promoter extends BaseEntity {

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @jakarta.persistence.OneToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "user_id", nullable = false)
    private com.example.afrorealityapi.user.entity.Profile user;

    @Column(name = "referral_code", nullable = false, unique = true)
    private String referralCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PromoterStatus status = PromoterStatus.PENDING;

    @Column(name = "commission_rate", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal commissionRate = new BigDecimal("5.00");

    @Column(nullable = false)
    @Builder.Default
    private int tier = 1;

    @Column(name = "is_gold_tier", nullable = false)
    @Builder.Default
    private boolean isGoldTier = false;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_account_name")
    private String bankAccountName;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;
}
