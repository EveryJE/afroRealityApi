package com.example.afrorealityapi.ticket.dto;

import com.example.afrorealityapi.common.enums.OrderStatus;
import com.example.afrorealityapi.common.enums.TicketCheckInStatus;
import com.example.afrorealityapi.common.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class TicketDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateTicketTypeRequest {
        private UUID eventId;
        private String name;
        private String description;
        private BigDecimal price;
        private String currency;
        private Integer quantityTotal;
        private OffsetDateTime salesStart;
        private OffsetDateTime salesEnd;
        private int maxPerOrder;
        private int minPerOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketTypeResponse {
        private UUID id;
        private UUID eventId;
        private String name;
        private String description;
        private BigDecimal price;
        private String currency;
        private Integer quantityTotal;
        private int quantitySold;
        private TicketStatus status;
        private OffsetDateTime salesStart;
        private OffsetDateTime salesEnd;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InitiateTicketOrderRequest {
        private UUID eventId;
        private String buyerName;
        private String buyerPhone;
        private List<OrderItem> items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        private UUID ticketTypeId;
        private int quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketOrderResponse {
        private UUID id;
        private String orderNumber;
        private UUID eventId;
        private String buyerName;
        private BigDecimal subtotal;
        private BigDecimal fees;
        private OrderStatus status;
        private OffsetDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketResponse {
        private UUID id;
        private String ticketCode;
        private String attendeeName;
        private String attendeeEmail;
        private TicketCheckInStatus checkInStatus;
        private OffsetDateTime checkedInAt;
    }
}
