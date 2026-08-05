package com.example.afrorealityapi.organization.service;

import com.example.afrorealityapi.organization.dto.OrganizationDtos.*;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    OrganizationResponse createOrganization(UUID creatorId, CreateOrganizationRequest request);
    OrganizationResponse getOrganizationById(UUID orgId);
    OrganizationResponse getOrganizationBySlug(String slug);
    OrganizationResponse updateOrganization(UUID orgId, UpdateOrganizationRequest request);
    List<OrganizationMemberResponse> getMembers(UUID orgId);
    List<OrganizationResponse> searchOrganizations(UUID userId, String search);
    OrganizationResponse switchOrganization(UUID userId, UUID orgId);

    // Invitation flow
    InvitationResponse inviteMember(UUID orgId, UUID inviterId, InviteMemberRequest request);
    OrganizationMemberResponse acceptInvitation(String token, UUID userId);
    void declineInvitation(String token);
    List<InvitationResponse> getInvitations(UUID orgId);
    List<InvitationResponse> getMyInvitations(String email);

    // Membership request flow
    JoinRequestResponse createJoinRequest(UUID orgId, UUID userId, JoinRequestRequest request);
    JoinRequestResponse resolveJoinRequest(UUID orgId, UUID requestId, UUID resolverId, String action);
    List<JoinRequestResponse> getJoinRequests(UUID orgId);
}
