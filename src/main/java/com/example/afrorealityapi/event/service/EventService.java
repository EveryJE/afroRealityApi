package com.example.afrorealityapi.event.service;

import com.example.afrorealityapi.event.dto.EventDtos.CreateEventRequest;
import com.example.afrorealityapi.event.dto.EventDtos.EventResponse;
import com.example.afrorealityapi.event.dto.EventDtos.UpdateEventRequest;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventResponse createEvent(UUID creatorId, CreateEventRequest request);
    EventResponse getEventById(UUID eventId);
    EventResponse getEventBySlug(UUID organizationId, String slug);
    List<EventResponse> getEventsByOrganization(UUID organizationId);
    List<EventResponse> getPublicEvents();
    EventResponse updateEvent(UUID eventId, UpdateEventRequest request);
    EventResponse publishEvent(UUID eventId);
    EventResponse cancelEvent(UUID eventId);
    void deleteEvent(UUID eventId);
}
