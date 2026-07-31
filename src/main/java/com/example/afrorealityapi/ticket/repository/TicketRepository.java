package com.example.afrorealityapi.ticket.repository;

import com.example.afrorealityapi.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByOrderId(UUID orderId);
    List<Ticket> findByEventId(UUID eventId);
}
