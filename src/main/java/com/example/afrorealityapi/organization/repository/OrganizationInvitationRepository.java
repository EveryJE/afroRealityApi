package com.example.afrorealityapi.organization.repository;

import com.example.afrorealityapi.common.enums.InvitationStatus;
import com.example.afrorealityapi.organization.entity.OrganizationInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationInvitationRepository extends JpaRepository<OrganizationInvitation, UUID> {
    Optional<OrganizationInvitation> findByToken(String token);
    List<OrganizationInvitation> findByOrganizationIdAndStatus(UUID organizationId, InvitationStatus status);
    List<OrganizationInvitation> findByEmailAndStatus(String email, InvitationStatus status);
    boolean existsByOrganizationIdAndEmailAndStatus(UUID organizationId, String email, InvitationStatus status);
}
