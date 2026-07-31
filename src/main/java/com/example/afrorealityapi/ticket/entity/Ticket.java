package com.example.afrorealityapi.ticket.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.TicketCheckInStatus;
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
@Table(name = "tickets")
public class Ticket extends BaseEntity {

    @Column(name = "order_id", insertable = false, updatable = false)
    private UUID orderId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", nullable = false)
    private TicketOrder order;

    @Column(name = "event_id", insertable = false, updatable = false)
    private UUID eventId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "event_id", nullable = false)
    private com.example.afrorealityapi.event.entity.Event event;

    @Column(name = "ticket_type_id", insertable = false, updatable = false)
    private UUID ticketTypeId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(name = "ticket_code", nullable = false, unique = true)
    private String ticketCode;

    @Column(name = "attendee_name")
    private String attendeeName;

    @Column(name = "attendee_email")
    private String attendeeEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_status", nullable = false)
    @Builder.Default
    private TicketCheckInStatus checkInStatus = TicketCheckInStatus.NOT_CHECKED_IN;

    @Column(name = "checked_in_at")
    private OffsetDateTime checkedInAt;

    @Column(name = "checked_in_by")
    private UUID checkedInBy;

    @Column(name = "sms_sent", nullable = false)
    @Builder.Default
    private boolean smsSent = false;

    @Column(name = "whatsapp_sent", nullable = false)
    @Builder.Default
    private boolean whatsappSent = false;
}
