package com.example.afrorealityapi.payment.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.CurrencyCode;
import com.example.afrorealityapi.common.enums.FinancialStatus;
import com.example.afrorealityapi.common.enums.PaymentProvider;
import com.example.afrorealityapi.common.enums.PaymentPurpose;
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
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "user_id")
    private com.example.afrorealityapi.user.entity.Profile user;

    @jakarta.persistence.OneToMany(mappedBy = "payment", fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.ticket.entity.TicketOrder> ticketOrders;

    @jakarta.persistence.OneToMany(mappedBy = "payment", fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.voting.entity.Vote> votes;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentPurpose purpose;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CurrencyCode currency = CurrencyCode.GHS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Column(name = "provider_reference")
    private String providerReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FinancialStatus status = FinancialStatus.PENDING;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "provider_response")
    private Map<String, Object> providerResponse;

    @Column(name = "paystack_transaction_id")
    private String paystackTransactionId;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;
}
