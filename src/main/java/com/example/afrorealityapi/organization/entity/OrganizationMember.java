package com.example.afrorealityapi.organization.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.OrganizationRole;
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
    name = "organization_members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "user_id"})
)
public class OrganizationMember extends BaseEntity {

    @Column(name = "organization_id", insertable = false, updatable = false)
    private UUID organizationId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "user_id", nullable = false)
    private com.example.afrorealityapi.user.entity.Profile user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrganizationRole role = OrganizationRole.MEMBER;

    @Column(name = "joined_at", nullable = false)
    @Builder.Default
    private OffsetDateTime joinedAt = OffsetDateTime.now();
}
