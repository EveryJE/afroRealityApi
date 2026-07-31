package com.example.afrorealityapi.payment.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.CurrencyCode;
import com.example.afrorealityapi.common.enums.TransactionCategory;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fee_configurations")
public class FeeConfiguration extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "fee_type", nullable = false)
    private String feeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_category")
    private TransactionCategory transactionCategory;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(name = "fixed_amount", precision = 15, scale = 2)
    private BigDecimal fixedAmount;

    @Column(name = "min_fee", precision = 15, scale = 2)
    private BigDecimal minFee;

    @Column(name = "max_fee", precision = 15, scale = 2)
    private BigDecimal maxFee;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> tiers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CurrencyCode currency = CurrencyCode.GHS;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    private String description;

    @Column(name = "effective_from", nullable = false)
    @Builder.Default
    private OffsetDateTime effectiveFrom = OffsetDateTime.now();

    @Column(name = "effective_to")
    private OffsetDateTime effectiveTo;
}
