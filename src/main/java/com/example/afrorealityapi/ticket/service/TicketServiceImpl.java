package com.example.afrorealityapi.ticket.service;

import com.example.afrorealityapi.common.enums.OrderStatus;
import com.example.afrorealityapi.common.enums.TicketCheckInStatus;
import com.example.afrorealityapi.common.enums.TicketStatus;
import com.example.afrorealityapi.common.exception.ApiException;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.ticket.dto.TicketDtos.CreateTicketTypeRequest;
import com.example.afrorealityapi.ticket.dto.TicketDtos.InitiateTicketOrderRequest;
import com.example.afrorealityapi.ticket.dto.TicketDtos.OrderItem;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketOrderResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketTypeResponse;
import com.example.afrorealityapi.ticket.entity.Ticket;
import com.example.afrorealityapi.ticket.entity.TicketOrder;
import com.example.afrorealityapi.ticket.entity.TicketType;
import com.example.afrorealityapi.ticket.repository.TicketOrderRepository;
import com.example.afrorealityapi.ticket.repository.TicketRepository;
import com.example.afrorealityapi.ticket.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketTypeRepository ticketTypeRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public TicketTypeResponse createTicketType(CreateTicketTypeRequest request) {
        TicketType type = TicketType.builder()
                .eventId(request.getEventId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO)
                .currency(request.getCurrency() != null ? request.getCurrency() : "GHS")
                .quantityTotal(request.getQuantityTotal())
                .salesStart(request.getSalesStart())
                .salesEnd(request.getSalesEnd())
                .maxPerOrder(request.getMaxPerOrder() > 0 ? request.getMaxPerOrder() : 10)
                .minPerOrder(request.getMinPerOrder() > 0 ? request.getMinPerOrder() : 1)
                .status(TicketStatus.AVAILABLE)
                .build();

        TicketType saved = ticketTypeRepository.save(type);
        return mapTypeToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketTypeResponse> getTicketTypesByEvent(UUID eventId) {
        return ticketTypeRepository.findByEventId(eventId).stream()
                .map(this::mapTypeToResponse)
                .toList();
    }

    @Override
    @Transactional
    public TicketOrderResponse initiateOrder(UUID buyerId, InitiateTicketOrderRequest request) {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItem item : request.getItems()) {
            TicketType type = ticketTypeRepository.findById(item.getTicketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket type not found: " + item.getTicketTypeId()));
            subtotal = subtotal.add(type.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        TicketOrder order = TicketOrder.builder()
                .eventId(request.getEventId())
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .buyerName(request.getBuyerName())
                .buyerPhone(request.getBuyerPhone())
                .buyerId(buyerId)
                .subtotal(subtotal)
                .fees(BigDecimal.ZERO)
                .status(OrderStatus.PENDING)
                .build();

        TicketOrder saved = ticketOrderRepository.save(order);
        return mapOrderToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicketByCode(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with code: " + ticketCode));
        return mapTicketToResponse(ticket);
    }

    @Override
    @Transactional
    public TicketResponse checkInTicket(String ticketCode, UUID checkedInBy) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with code: " + ticketCode));

        if (ticket.getCheckInStatus() == TicketCheckInStatus.CHECKED_IN) {
            throw new ApiException("Ticket has already been checked in at " + ticket.getCheckedInAt());
        }

        ticket.setCheckInStatus(TicketCheckInStatus.CHECKED_IN);
        ticket.setCheckedInAt(OffsetDateTime.now());
        ticket.setCheckedInBy(checkedInBy);

        Ticket saved = ticketRepository.save(ticket);
        return mapTicketToResponse(saved);
    }

    private TicketTypeResponse mapTypeToResponse(TicketType type) {
        return TicketTypeResponse.builder()
                .id(type.getId())
                .eventId(type.getEventId())
                .name(type.getName())
                .description(type.getDescription())
                .price(type.getPrice())
                .currency(type.getCurrency())
                .quantityTotal(type.getQuantityTotal())
                .quantitySold(type.getQuantitySold())
                .status(type.getStatus())
                .salesStart(type.getSalesStart())
                .salesEnd(type.getSalesEnd())
                .build();
    }

    private TicketOrderResponse mapOrderToResponse(TicketOrder order) {
        return TicketOrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .eventId(order.getEventId())
                .buyerName(order.getBuyerName())
                .subtotal(order.getSubtotal())
                .fees(order.getFees())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private TicketResponse mapTicketToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .attendeeName(ticket.getAttendeeName())
                .attendeeEmail(ticket.getAttendeeEmail())
                .checkInStatus(ticket.getCheckInStatus())
                .checkedInAt(ticket.getCheckedInAt())
                .build();
    }
}
