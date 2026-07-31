package com.example.afrorealityapi.event.service;

import com.example.afrorealityapi.common.enums.EventStatus;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.event.dto.EventDtos.CreateEventRequest;
import com.example.afrorealityapi.event.dto.EventDtos.EventResponse;
import com.example.afrorealityapi.event.dto.EventDtos.UpdateEventRequest;
import com.example.afrorealityapi.event.entity.Event;
import com.example.afrorealityapi.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventResponse createEvent(UUID creatorId, CreateEventRequest request) {
        Event event = Event.builder()
                .organizationId(request.getOrganizationId())
                .creatorId(creatorId)
                .title(request.getTitle())
                .description(request.getDescription())
                .slug(request.getSlug())
                .type(request.getType())
                .status(EventStatus.DRAFT)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .venueName(request.getVenueName())
                .venueCity(request.getVenueCity())
                .isVirtual(request.isVirtual())
                .virtualLink(request.getVirtualLink())
                .build();

        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        return mapToResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventBySlug(UUID organizationId, String slug) {
        Event event = eventRepository.findByOrganizationIdAndSlug(organizationId, slug)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with slug: " + slug));
        return mapToResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByOrganization(UUID organizationId) {
        return eventRepository.findByOrganizationId(organizationId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getPublicEvents() {
        return eventRepository.findByStatusAndIsPublicTrue(EventStatus.PUBLISHED).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public EventResponse updateEvent(UUID eventId, UpdateEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getType() != null) event.setType(request.getType());
        if (request.getStatus() != null) event.setStatus(request.getStatus());
        if (request.getStartDate() != null) event.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) event.setEndDate(request.getEndDate());
        if (request.getTimezone() != null) event.setTimezone(request.getTimezone());
        if (request.getIsPublic() != null) event.setPublic(request.getIsPublic());
        if (request.getFlierImage() != null) event.setFlierImage(request.getFlierImage());
        if (request.getBannerImage() != null) event.setBannerImage(request.getBannerImage());
        if (request.getVenueName() != null) event.setVenueName(request.getVenueName());
        if (request.getVenueAddress() != null) event.setVenueAddress(request.getVenueAddress());
        if (request.getVenueCity() != null) event.setVenueCity(request.getVenueCity());
        if (request.getVenueCountry() != null) event.setVenueCountry(request.getVenueCountry());
        if (request.getIsVirtual() != null) event.setVirtual(request.getIsVirtual());
        if (request.getVirtualLink() != null) event.setVirtualLink(request.getVirtualLink());
        if (request.getMaxAttendees() != null) event.setMaxAttendees(request.getMaxAttendees());
        if (request.getRegistrationDeadline() != null) event.setRegistrationDeadline(request.getRegistrationDeadline());

        return mapToResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResponse publishEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        event.setStatus(EventStatus.PUBLISHED);
        event.setPublishedAt(java.time.OffsetDateTime.now());
        return mapToResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResponse cancelEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));
        event.setStatus(EventStatus.CANCELLED);
        return mapToResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event not found: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .organizationId(event.getOrganizationId())
                .title(event.getTitle())
                .description(event.getDescription())
                .slug(event.getSlug())
                .type(event.getType())
                .status(event.getStatus())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .isPublic(event.isPublic())
                .venueName(event.getVenueName())
                .venueCity(event.getVenueCity())
                .isVirtual(event.isVirtual())
                .ussdCode(event.getUssdCode())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
