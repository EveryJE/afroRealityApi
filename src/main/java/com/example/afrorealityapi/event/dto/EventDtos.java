package com.example.afrorealityapi.event.dto;

import com.example.afrorealityapi.common.enums.EventStatus;
import com.example.afrorealityapi.common.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

public class EventDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateEventRequest {
        private UUID organizationId;
        private String title;
        private String description;
        private String slug;
        private EventType type;
        private OffsetDateTime startDate;
        private OffsetDateTime endDate;
        private String venueName;
        private String venueCity;
        private boolean isVirtual;
        private String virtualLink;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateEventRequest {
        private String title;
        private String description;
        private EventType type;
        private EventStatus status;
        private OffsetDateTime startDate;
        private OffsetDateTime endDate;
        private String timezone;
        private Boolean isPublic;
        private String flierImage;
        private String bannerImage;
        private String venueName;
        private String venueAddress;
        private String venueCity;
        private String venueCountry;
        private Boolean isVirtual;
        private String virtualLink;
        private Integer maxAttendees;
        private OffsetDateTime registrationDeadline;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventResponse {
        private UUID id;
        private UUID organizationId;
        private String title;
        private String description;
        private String slug;
        private EventType type;
        private EventStatus status;
        private OffsetDateTime startDate;
        private OffsetDateTime endDate;
        private boolean isPublic;
        private String venueName;
        private String venueCity;
        private boolean isVirtual;
        private String ussdCode;
        private OffsetDateTime createdAt;
    }
}
