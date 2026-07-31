package com.example.afrorealityapi.ticket.service;

import com.example.afrorealityapi.ticket.dto.TicketDtos.CreateTicketTypeRequest;
import com.example.afrorealityapi.ticket.dto.TicketDtos.InitiateTicketOrderRequest;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketOrderResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketTypeResponse;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    TicketTypeResponse createTicketType(CreateTicketTypeRequest request);
    List<TicketTypeResponse> getTicketTypesByEvent(UUID eventId);
    TicketOrderResponse initiateOrder(UUID buyerId, InitiateTicketOrderRequest request);
    TicketResponse getTicketByCode(String ticketCode);
    TicketResponse checkInTicket(String ticketCode, UUID checkedInBy);
}
