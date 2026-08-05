package com.example.afrorealityapi.organization.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.common.exception.ApiException;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.*;
import com.example.afrorealityapi.organization.service.OrganizationService;
import com.example.afrorealityapi.user.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> create(
            @RequestParam UUID creatorId,
            @RequestBody CreateOrganizationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.createOrganization(creatorId, request)));
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getById(@PathVariable UUID orgId) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.getOrganizationById(orgId)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.getOrganizationBySlug(slug)));
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> update(
            @PathVariable UUID orgId,
            @RequestBody UpdateOrganizationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.updateOrganization(orgId, request)));
    }

    @GetMapping("/{orgId}/members")
    public ResponseEntity<ApiResponse<List<OrganizationMemberResponse>>> getMembers(@PathVariable UUID orgId) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.getMembers(orgId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> searchOrganizations(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "false") boolean mine,
            @AuthenticationPrincipal Profile profile) {
        if (mine) {
            if (profile == null) {
                throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }
            userId = profile.getId();
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.searchOrganizations(userId, search)));
    }

    @PostMapping("/switch/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> switchOrganization(
            @PathVariable UUID orgId,
            @AuthenticationPrincipal Profile profile) {
        if (profile == null) {
            throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.switchOrganization(profile.getId(), orgId)));
    }

    // --- Invitation Endpoints ---

    @PostMapping("/{orgId}/invitations")
    public ResponseEntity<ApiResponse<InvitationResponse>> inviteMember(
            @PathVariable UUID orgId,
            @AuthenticationPrincipal Profile profile,
            @RequestBody InviteMemberRequest request) {
        if (profile == null) {
            throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.inviteMember(orgId, profile.getId(), request)));
    }

    @GetMapping("/{orgId}/invitations")
    public ResponseEntity<ApiResponse<List<InvitationResponse>>> getInvitations(@PathVariable UUID orgId) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.getInvitations(orgId)));
    }

    @GetMapping("/invitations/my")
    public ResponseEntity<ApiResponse<List<InvitationResponse>>> getMyInvitations(@AuthenticationPrincipal Profile profile) {
        if (profile == null) {
            throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.getMyInvitations(profile.getEmail())));
    }

    @PostMapping("/invitations/accept/{token}")
    public ResponseEntity<ApiResponse<OrganizationMemberResponse>> acceptInvitation(
            @PathVariable String token,
            @AuthenticationPrincipal Profile profile) {
        if (profile == null) {
            throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.acceptInvitation(token, profile.getId())));
    }

    @PostMapping("/invitations/decline/{token}")
    public ResponseEntity<ApiResponse<Void>> declineInvitation(@PathVariable String token) {
        organizationService.declineInvitation(token);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // --- Join Request Endpoints ---

    @PostMapping("/{orgId}/join-requests")
    public ResponseEntity<ApiResponse<JoinRequestResponse>> createJoinRequest(
            @PathVariable UUID orgId,
            @AuthenticationPrincipal Profile profile,
            @RequestBody(required = false) JoinRequestRequest request) {
        if (profile == null) {
            throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.createJoinRequest(orgId, profile.getId(), request)));
    }

    @GetMapping("/{orgId}/join-requests")
    public ResponseEntity<ApiResponse<List<JoinRequestResponse>>> getJoinRequests(@PathVariable UUID orgId) {
        return ResponseEntity.ok(ApiResponse.ok(organizationService.getJoinRequests(orgId)));
    }

    @PutMapping("/{orgId}/join-requests/{requestId}")
    public ResponseEntity<ApiResponse<JoinRequestResponse>> resolveJoinRequest(
            @PathVariable UUID orgId,
            @PathVariable UUID requestId,
            @RequestParam String action,
            @AuthenticationPrincipal Profile profile) {
        if (profile == null) {
            throw new ApiException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.ok(organizationService.resolveJoinRequest(orgId, requestId, profile.getId(), action)));
    }
}
