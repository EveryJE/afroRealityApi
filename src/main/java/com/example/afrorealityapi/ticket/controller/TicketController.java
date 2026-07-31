package com.example.afrorealityapi.ticket.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.CreateTicketTypeRequest;
import com.example.afrorealityapi.ticket.dto.TicketDtos.InitiateTicketOrderRequest;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketOrderResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketResponse;
import com.example.afrorealityapi.ticket.dto.TicketDtos.TicketTypeResponse;
import com.example.afrorealityapi.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/types")
    public ResponseEntity<ApiResponse<TicketTypeResponse>> createType(
            @RequestBody CreateTicketTypeRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.createTicketType(request)));
    }

    @GetMapping("/types/event/{eventId}")
    public ResponseEntity<ApiResponse<List<TicketTypeResponse>>> getTypesByEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.getTicketTypesByEvent(eventId)));
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<TicketOrderResponse>> initiateOrder(
            @RequestParam(required = false) UUID buyerId,
            @RequestBody InitiateTicketOrderRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.initiateOrder(buyerId, request)));
    }

    @GetMapping("/code/{ticketCode}")
    public ResponseEntity<ApiResponse<TicketResponse>> getByCode(@PathVariable String ticketCode) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.getTicketByCode(ticketCode)));
    }

    @PostMapping("/check-in/{ticketCode}")
    public ResponseEntity<ApiResponse<TicketResponse>> checkIn(
            @PathVariable String ticketCode,
            @RequestParam UUID checkedInBy) {
        return ResponseEntity.ok(ApiResponse.ok(ticketService.checkInTicket(ticketCode, checkedInBy)));
    }
}
