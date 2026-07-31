package com.example.afrorealityapi.event.repository;

import com.example.afrorealityapi.common.enums.EventStatus;
import com.example.afrorealityapi.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findByOrganizationIdAndSlug(UUID organizationId, String slug);
    Optional<Event> findByUssdCode(String ussdCode);
    List<Event> findByOrganizationId(UUID organizationId);
    List<Event> findByStatusAndIsPublicTrue(EventStatus status);
}
