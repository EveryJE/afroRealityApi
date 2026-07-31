package com.example.afrorealityapi.promoter.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.ReferralStatus;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "referrals")
public class Referral extends BaseEntity {

    @Column(name = "promoter_id", insertable = false, updatable = false)
    private UUID promoterId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "promoter_id", nullable = false)
    private Promoter promoter;

    @Column(name = "referred_user_id", insertable = false, updatable = false)
    private UUID referredUserId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "referred_user_id")
    private com.example.afrorealityapi.user.entity.Profile referredUser;

    @Column(name = "referred_email")
    private String referredEmail;

    @Column(name = "referral_code_used", nullable = false)
    private String referralCodeUsed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReferralStatus status = ReferralStatus.PENDING;

    private String source;

    private String campaign;

    @Column(name = "landing_page")
    private String landingPage;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "converted_at")
    private OffsetDateTime convertedAt;

    @Column(name = "first_purchase_at")
    private OffsetDateTime firstPurchaseAt;
}
