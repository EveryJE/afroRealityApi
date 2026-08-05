package com.example.afrorealityapi.event.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.event.dto.EventDtos.CreateEventRequest;
import com.example.afrorealityapi.event.dto.EventDtos.EventResponse;
import com.example.afrorealityapi.event.dto.EventDtos.UpdateEventRequest;
import com.example.afrorealityapi.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @RequestParam UUID creatorId,
            @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.createEvent(creatorId, request)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.getEventById(eventId)));
    }

    @GetMapping("/org/{organizationId}/slug/{slug}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventBySlug(
            @PathVariable UUID organizationId,
            @PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.getEventBySlug(organizationId, slug)));
    }

    @GetMapping({"/public", "/public/"})
    public ResponseEntity<ApiResponse<List<EventResponse>>> getPublicEvents() {
        return ResponseEntity.ok(ApiResponse.ok(eventService.getPublicEvents()));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByOrganization(
            @PathVariable UUID organizationId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.getEventsByOrganization(organizationId)));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable UUID eventId,
            @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.updateEvent(eventId, request)));
    }

    @PostMapping("/{eventId}/publish")
    public ResponseEntity<ApiResponse<EventResponse>> publishEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.publishEvent(eventId)));
    }

    @PostMapping("/{eventId}/cancel")
    public ResponseEntity<ApiResponse<EventResponse>> cancelEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.cancelEvent(eventId)));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(ApiResponse.ok("Event deleted successfully", null));
    }
}
