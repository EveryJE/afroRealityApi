package com.example.afrorealityapi.organization.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import com.example.afrorealityapi.common.enums.InvitationStatus;
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
    name = "organization_invitations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "email", "status"})
)
public class OrganizationInvitation extends BaseEntity {

    @Column(name = "organization_id", insertable = false, updatable = false)
    private UUID organizationId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "inviter_id", insertable = false, updatable = false)
    private UUID inviterId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "inviter_id")
    private com.example.afrorealityapi.user.entity.Profile inviter;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrganizationRole role = OrganizationRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private InvitationStatus status = InvitationStatus.PENDING;

    @Column(unique = true)
    private String token;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "responded_at")
    private OffsetDateTime respondedAt;
}
