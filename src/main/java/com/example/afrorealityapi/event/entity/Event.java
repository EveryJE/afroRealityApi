package com.example.afrorealityapi.event.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.EventStatus;
import com.example.afrorealityapi.common.enums.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "events",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "slug"})
)
public class Event extends BaseEntity {

    @Column(name = "organization_id", insertable = false, updatable = false)
    private UUID organizationId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "organization_id", nullable = false)
    private com.example.afrorealityapi.organization.entity.Organization organization;

    @Column(name = "creator_id", insertable = false, updatable = false)
    private UUID creatorId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "creator_id")
    private com.example.afrorealityapi.user.entity.Profile creator;

    @jakarta.persistence.OneToMany(mappedBy = "event", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.ticket.entity.TicketType> ticketTypes;

    @jakarta.persistence.OneToMany(mappedBy = "event", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.voting.entity.VotingCategory> votingCategories;

    @jakarta.persistence.OneToMany(mappedBy = "event", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<com.example.afrorealityapi.ticket.entity.TicketOrder> ticketOrders;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EventStatus status = EventStatus.DRAFT;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Column(nullable = false)
    @Builder.Default
    private String timezone = "Africa/Accra";

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private boolean isPublic = true;

    @Column(name = "flier_image")
    private String flierImage;

    @Column(name = "banner_image")
    private String bannerImage;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "venue_address")
    private String venueAddress;

    @Column(name = "venue_city")
    private String venueCity;

    @Column(name = "venue_country", nullable = false)
    @Builder.Default
    private String venueCountry = "Ghana";

    @Column(name = "is_virtual", nullable = false)
    @Builder.Default
    private boolean isVirtual = false;

    @Column(name = "virtual_link")
    private String virtualLink;

    @Column(name = "max_attendees")
    private Integer maxAttendees;

    @Column(name = "registration_deadline")
    private OffsetDateTime registrationDeadline;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "has_ussd", nullable = false)
    @Builder.Default
    private boolean hasUssd = false;

    @Column(name = "ussd_code", unique = true)
    private String ussdCode;
}
