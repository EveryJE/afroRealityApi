package com.example.afrorealityapi.ticket.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.TicketStatus;
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
@Table(name = "ticket_types")
public class TicketType extends BaseEntity {

    @Column(name = "event_id", insertable = false, updatable = false)
    private UUID eventId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "event_id", nullable = false)
    private com.example.afrorealityapi.event.entity.Event event;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private String currency = "GHS";

    @Column(name = "quantity_total")
    private Integer quantityTotal;

    @Column(name = "quantity_sold", nullable = false)
    @Builder.Default
    private int quantitySold = 0;

    @Column(name = "sales_start")
    private OffsetDateTime salesStart;

    @Column(name = "sales_end")
    private OffsetDateTime salesEnd;

    @Column(name = "max_per_order", nullable = false)
    @Builder.Default
    private int maxPerOrder = 10;

    @Column(name = "min_per_order", nullable = false)
    @Builder.Default
    private int minPerOrder = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.AVAILABLE;

    @Column(name = "order_idx", nullable = false)
    @Builder.Default
    private int orderIdx = 0;

    private String color;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "design_variant")
    @Builder.Default
    private String designVariant = "classic";
}
