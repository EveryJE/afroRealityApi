package com.example.afrorealityapi.user.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "profiles")
@EqualsAndHashCode(callSuper = true)
public class Profile extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "onboarding_completed", nullable = false)
    @Builder.Default
    private boolean onboardingCompleted = false;

    @Column(name = "onboarding_step", nullable = false)
    @Builder.Default
    private int onboardingStep = 0;

    @Column(name = "referred_by", insertable = false, updatable = false)
    private UUID referredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_by")
    private com.example.afrorealityapi.promoter.entity.Promoter referrer;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private com.example.afrorealityapi.promoter.entity.Promoter promoter;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.event.entity.Event> createdEvents;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.organization.entity.Organization> createdOrganizations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.organization.entity.OrganizationMember> organizationMemberships;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.payment.entity.Payment> payments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.payment.entity.Wallet> wallets;

    @Column(unique = true)
    private String phone;

    @Column(name = "whatsapp_opt_in", nullable = false)
    @Builder.Default
    private boolean whatsappOptIn = false;

    @Column(name = "pricing_plan", nullable = false)
    @Builder.Default
    private String pricingPlan = "essential";

    @Column(name = "communication_credits", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal communicationCredits = BigDecimal.ZERO;

    @Column(name = "is_verified_partner", nullable = false)
    @Builder.Default
    private boolean isVerifiedPartner = false;

    @Column(name = "current_organization_id")
    private UUID currentOrganizationId;
}
