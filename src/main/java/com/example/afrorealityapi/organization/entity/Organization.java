package com.example.afrorealityapi.organization.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "organizations")
public class Organization extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "primary_color", nullable = false)
    @Builder.Default
    private String primaryColor = "#02a605ff";

    @Column(name = "secondary_color", nullable = false)
    @Builder.Default
    private String secondaryColor = "#ffe100ff";

    @Column(name = "tertiary_color", nullable = false)
    @Builder.Default
    private String tertiaryColor = "#dc2626";

    @Column(name = "favicon_url")
    private String faviconUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "contact_email")
    private String contactEmail;

    private String phone;

    @Column(name = "paystack_account_name")
    private String paystackAccountName;

    @Column(name = "paystack_account_number")
    private String paystackAccountNumber;

    @Column(name = "paystack_bank_code")
    private String paystackBankCode;

    @Column(name = "subaccount_code")
    private String subaccountCode;

    @Column(name = "allow_join_requests", nullable = false)
    @Builder.Default
    private boolean allowJoinRequests = false;

    @Column(name = "auto_payout", nullable = false)
    @Builder.Default
    private boolean autoPayout = true;

    @Column(name = "created_by", insertable = false, updatable = false)
    private UUID createdBy;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "created_by")
    private com.example.afrorealityapi.user.entity.Profile creator;

    @jakarta.persistence.OneToMany(mappedBy = "organization", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.event.entity.Event> events;

    @jakarta.persistence.OneToMany(mappedBy = "organization", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<OrganizationMember> members;

    @jakarta.persistence.OneToMany(mappedBy = "organization", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<OrganizationInvitation> invitations;

    @jakarta.persistence.OneToMany(mappedBy = "organization", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<MembershipRequest> requests;

    @jakarta.persistence.OneToMany(mappedBy = "organization", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.payment.entity.Wallet> wallets;
}
