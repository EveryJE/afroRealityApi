package com.example.afrorealityapi.ticket.repository;

import com.example.afrorealityapi.ticket.entity.TicketOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketOrderRepository extends JpaRepository<TicketOrder, UUID> {
    Optional<TicketOrder> findByOrderNumber(String orderNumber);
    List<TicketOrder> findByEventId(UUID eventId);
    List<TicketOrder> findByBuyerId(UUID buyerId);
}
