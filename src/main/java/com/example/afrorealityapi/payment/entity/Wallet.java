package com.example.afrorealityapi.payment.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.CurrencyCode;
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
@Table(name = "wallets")
public class Wallet extends BaseEntity {

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "user_id")
    private com.example.afrorealityapi.user.entity.Profile user;

    @Column(name = "organization_id", insertable = false, updatable = false)
    private UUID organizationId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "organization_id")
    private com.example.afrorealityapi.organization.entity.Organization organization;

    @jakarta.persistence.OneToMany(mappedBy = "wallet", cascade = jakarta.persistence.CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<Transaction> transactions;

    @jakarta.persistence.OneToMany(mappedBy = "wallet", cascade = jakarta.persistence.CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<Payout> payouts;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CurrencyCode currency = CurrencyCode.GHS;

    @Column(name = "pending_credits", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal pendingCredits = BigDecimal.ZERO;

    @Column(name = "pending_debits", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal pendingDebits = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    private boolean isLocked = false;

    @Column(name = "lock_reason")
    private String lockReason;

    @Column(name = "last_transaction_at")
    private OffsetDateTime lastTransactionAt;
}
