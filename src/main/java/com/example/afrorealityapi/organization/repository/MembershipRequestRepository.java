package com.example.afrorealityapi.organization.repository;

import com.example.afrorealityapi.common.enums.ApprovalStatus;
import com.example.afrorealityapi.organization.entity.MembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, UUID> {
    List<MembershipRequest> findByOrganizationIdAndStatus(UUID organizationId, ApprovalStatus status);
    List<MembershipRequest> findByUserIdAndStatus(UUID userId, ApprovalStatus status);
    boolean existsByOrganizationIdAndUserIdAndStatus(UUID organizationId, UUID userId, ApprovalStatus status);
}
