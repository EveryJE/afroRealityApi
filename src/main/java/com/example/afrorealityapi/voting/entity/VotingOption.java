package com.example.afrorealityapi.voting.entity;

import com.example.afrorealityapi.common.enums.ApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    name = "voting_options",
    uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "nominee_code"})
)
public class VotingOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "event_id", insertable = false, updatable = false)
    private UUID eventId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "event_id", nullable = false)
    private com.example.afrorealityapi.event.entity.Event event;

    @Column(name = "category_id", insertable = false, updatable = false)
    private UUID categoryId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "category_id")
    private VotingCategory category;

    @Column(name = "nominated_by_id", insertable = false, updatable = false)
    private UUID nominatedById;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "nominated_by_id")
    private com.example.afrorealityapi.user.entity.Profile nominatedBy;

    @Column(name = "option_text", nullable = false)
    private String optionText;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "order_idx", nullable = false)
    @Builder.Default
    private int orderIdx = 0;

    @Column(name = "votes_count", nullable = false)
    @Builder.Default
    private long votesCount = 0L;

    private String email;

    @Column(name = "is_public_nomination", nullable = false)
    @Builder.Default
    private boolean isPublicNomination = false;

    @Column(name = "nominated_by_email")
    private String nominatedByEmail;

    @Column(name = "nominated_by_name")
    private String nominatedByName;

    @Column(name = "nominee_code")
    private String nomineeCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApprovalStatus status = ApprovalStatus.APPROVED;

    @Column(name = "final_image")
    private String finalImage;

    @Column(name = "deletion_code")
    private String deletionCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
