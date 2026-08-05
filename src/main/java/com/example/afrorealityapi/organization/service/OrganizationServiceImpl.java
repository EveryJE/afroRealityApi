package com.example.afrorealityapi.organization.service;

import com.example.afrorealityapi.common.email.EmailService;
import com.example.afrorealityapi.common.enums.ApprovalStatus;
import com.example.afrorealityapi.common.enums.InvitationStatus;
import com.example.afrorealityapi.common.enums.OrganizationRole;
import com.example.afrorealityapi.common.exception.ApiException;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.*;
import com.example.afrorealityapi.organization.entity.MembershipRequest;
import com.example.afrorealityapi.organization.entity.Organization;
import com.example.afrorealityapi.organization.entity.OrganizationInvitation;
import com.example.afrorealityapi.organization.entity.OrganizationMember;
import com.example.afrorealityapi.organization.repository.MembershipRequestRepository;
import com.example.afrorealityapi.organization.repository.OrganizationInvitationRepository;
import com.example.afrorealityapi.organization.repository.OrganizationMemberRepository;
import com.example.afrorealityapi.organization.repository.OrganizationRepository;
import com.example.afrorealityapi.user.entity.Profile;
import com.example.afrorealityapi.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final OrganizationInvitationRepository invitationRepository;
    private final MembershipRequestRepository membershipRequestRepository;
    private final EmailService emailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    @Transactional
    public OrganizationResponse createOrganization(UUID creatorId, CreateOrganizationRequest request) {
        Organization org = Organization.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .contactEmail(request.getContactEmail())
                .phone(request.getPhone())
                .createdBy(creatorId)
                .build();

        Organization saved = organizationRepository.save(org);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(UUID orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        return mapToResponse(org);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationBySlug(String slug) {
        Organization org = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        return mapToResponse(org);
    }

    @Override
    @Transactional
    public OrganizationResponse updateOrganization(UUID orgId, UpdateOrganizationRequest request) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        if (request.getName() != null) org.setName(request.getName());
        if (request.getDescription() != null) org.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) org.setLogoUrl(request.getLogoUrl());
        if (request.getBannerUrl() != null) org.setBannerUrl(request.getBannerUrl());
        if (request.getPrimaryColor() != null) org.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) org.setSecondaryColor(request.getSecondaryColor());
        if (request.getContactEmail() != null) org.setContactEmail(request.getContactEmail());
        if (request.getPhone() != null) org.setPhone(request.getPhone());
        if (request.getWebsiteUrl() != null) org.setWebsiteUrl(request.getWebsiteUrl());

        return mapToResponse(organizationRepository.save(org));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMemberResponse> getMembers(UUID orgId) {
        return memberRepository.findByOrganizationId(orgId).stream()
                .map(this::mapMemberToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> searchOrganizations(UUID userId, String search) {
        List<Organization> results;

        if (userId != null) {
            List<Organization> userOrgs = memberRepository.findByUserId(userId).stream()
                    .map(OrganizationMember::getOrganization)
                    .toList();

            if (search != null && !search.isBlank()) {
                String lowerSearch = search.toLowerCase();
                results = userOrgs.stream()
                        .filter(o -> o.getName().toLowerCase().contains(lowerSearch)
                                || (o.getDescription() != null && o.getDescription().toLowerCase().contains(lowerSearch)))
                        .toList();
            } else {
                results = userOrgs;
            }
        } else if (search != null && !search.isBlank()) {
            results = organizationRepository.searchByNameOrDescription(search);
        } else {
            results = organizationRepository.findAll();
        }

        return results.stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public OrganizationResponse switchOrganization(UUID userId, UUID orgId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        boolean isMember = memberRepository.existsByOrganizationIdAndUserId(orgId, userId);
        if (!isMember) {
            throw new ApiException("User is not a member of this organization");
        }

        profile.setCurrentOrganizationId(orgId);
        profileRepository.save(profile);

        return mapToResponse(org);
    }

    // --- Invitation Flow ---

    @Override
    @Transactional
    public InvitationResponse inviteMember(UUID orgId, UUID inviterId, InviteMemberRequest request) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Check if inviter has admin/owner permissions
        OrganizationMember inviterMember = memberRepository.findByOrganizationIdAndUserId(orgId, inviterId)
                .orElseThrow(() -> new ApiException("Inviter is not a member of this organization"));

        if (inviterMember.getRole() != OrganizationRole.OWNER && inviterMember.getRole() != OrganizationRole.ADMIN) {
            throw new ApiException("Only organization owners and admins can send invitations");
        }

        // Check if already invited (pending)
        if (invitationRepository.existsByOrganizationIdAndEmailAndStatus(orgId, request.getEmail(), InvitationStatus.PENDING)) {
            throw new ApiException("A pending invitation already exists for this email");
        }

        Profile inviterProfile = profileRepository.findById(inviterId).orElse(null);

        String token = UUID.randomUUID().toString();
        OrganizationInvitation invitation = OrganizationInvitation.builder()
                .organization(org)
                .inviter(inviterProfile)
                .email(request.getEmail())
                .role(request.getRole() != null ? request.getRole() : OrganizationRole.MEMBER)
                .status(InvitationStatus.PENDING)
                .token(token)
                .expiresAt(OffsetDateTime.now().plusHours(48))
                .build();

        OrganizationInvitation saved = invitationRepository.save(invitation);

        // Send email notification
        String inviteLink = baseUrl + "/api/v1/organizations/invitations/accept/" + token;
        emailService.sendOrganizationInvitation(request.getEmail(), org.getName(), inviteLink);

        return mapInvitationToResponse(saved);
    }

    @Override
    @Transactional
    public OrganizationMemberResponse acceptInvitation(String token, UUID userId) {
        OrganizationInvitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired invitation token"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ApiException("Invitation has already been " + invitation.getStatus().getValue());
        }

        if (invitation.getExpiresAt() != null && invitation.getExpiresAt().isBefore(OffsetDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new ApiException("Invitation has expired");
        }

        Profile user = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

        Organization org = invitation.getOrganization();

        // Check if already a member
        if (memberRepository.existsByOrganizationIdAndUserId(org.getId(), userId)) {
            invitation.setStatus(InvitationStatus.ACCEPTED);
            invitation.setRespondedAt(OffsetDateTime.now());
            invitationRepository.save(invitation);
            throw new ApiException("User is already a member of this organization");
        }

        OrganizationMember member = OrganizationMember.builder()
                .organization(org)
                .user(user)
                .role(invitation.getRole())
                .joinedAt(OffsetDateTime.now())
                .build();

        OrganizationMember savedMember = memberRepository.save(member);

        // Mark invitation accepted
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRespondedAt(OffsetDateTime.now());
        invitationRepository.save(invitation);

        // Auto-set as current org if user doesn't have one set
        if (user.getCurrentOrganizationId() == null) {
            user.setCurrentOrganizationId(org.getId());
            profileRepository.save(user);
        }

        return mapMemberToResponse(savedMember);
    }

    @Override
    @Transactional
    public void declineInvitation(String token) {
        OrganizationInvitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired invitation token"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ApiException("Invitation has already been " + invitation.getStatus().getValue());
        }

        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRespondedAt(OffsetDateTime.now());
        invitationRepository.save(invitation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvitationResponse> getInvitations(UUID orgId) {
        return invitationRepository.findByOrganizationIdAndStatus(orgId, InvitationStatus.PENDING).stream()
                .map(this::mapInvitationToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvitationResponse> getMyInvitations(String email) {
        return invitationRepository.findByEmailAndStatus(email, InvitationStatus.PENDING).stream()
                .map(this::mapInvitationToResponse)
                .toList();
    }

    // --- Membership Request Flow ---

    @Override
    @Transactional
    public JoinRequestResponse createJoinRequest(UUID orgId, UUID userId, JoinRequestRequest request) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        if (!org.isAllowJoinRequests()) {
            throw new ApiException("This organization does not accept join requests");
        }

        Profile user = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

        if (memberRepository.existsByOrganizationIdAndUserId(orgId, userId)) {
            throw new ApiException("You are already a member of this organization");
        }

        if (membershipRequestRepository.existsByOrganizationIdAndUserIdAndStatus(orgId, userId, ApprovalStatus.PENDING)) {
            throw new ApiException("You already have a pending join request for this organization");
        }

        MembershipRequest joinRequest = MembershipRequest.builder()
                .organization(org)
                .user(user)
                .message(request != null ? request.getMessage() : null)
                .status(ApprovalStatus.PENDING)
                .build();

        MembershipRequest saved = membershipRequestRepository.save(joinRequest);
        return mapJoinRequestToResponse(saved);
    }

    @Override
    @Transactional
    public JoinRequestResponse resolveJoinRequest(UUID orgId, UUID requestId, UUID resolverId, String action) {
        MembershipRequest joinRequest = membershipRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Join request not found"));

        if (!joinRequest.getOrganizationId().equals(orgId)) {
            throw new ApiException("Request does not belong to this organization");
        }

        if (joinRequest.getStatus() != ApprovalStatus.PENDING) {
            throw new ApiException("Request has already been resolved");
        }

        // Validate resolver is owner/admin
        OrganizationMember resolverMember = memberRepository.findByOrganizationIdAndUserId(orgId, resolverId)
                .orElseThrow(() -> new ApiException("Resolver is not a member of this organization"));

        if (resolverMember.getRole() != OrganizationRole.OWNER && resolverMember.getRole() != OrganizationRole.ADMIN) {
            throw new ApiException("Only organization owners and admins can resolve join requests");
        }

        Profile resolver = profileRepository.findById(resolverId).orElse(null);

        if ("approve".equalsIgnoreCase(action)) {
            joinRequest.setStatus(ApprovalStatus.APPROVED);
            joinRequest.setResolvedAt(OffsetDateTime.now());
            joinRequest.setResolver(resolver);

            // Add user as member
            if (!memberRepository.existsByOrganizationIdAndUserId(orgId, joinRequest.getUserId())) {
                OrganizationMember member = OrganizationMember.builder()
                        .organization(joinRequest.getOrganization())
                        .user(joinRequest.getUser())
                        .role(OrganizationRole.MEMBER)
                        .joinedAt(OffsetDateTime.now())
                        .build();
                memberRepository.save(member);
            }
        } else if ("reject".equalsIgnoreCase(action)) {
            joinRequest.setStatus(ApprovalStatus.REJECTED);
            joinRequest.setResolvedAt(OffsetDateTime.now());
            joinRequest.setResolver(resolver);
        } else {
            throw new ApiException("Invalid action. Allowed values: approve, reject");
        }

        MembershipRequest saved = membershipRequestRepository.save(joinRequest);
        return mapJoinRequestToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JoinRequestResponse> getJoinRequests(UUID orgId) {
        return membershipRequestRepository.findByOrganizationIdAndStatus(orgId, ApprovalStatus.PENDING).stream()
                .map(this::mapJoinRequestToResponse)
                .toList();
    }

    // --- Helper Mappers ---

    private OrganizationResponse mapToResponse(Organization org) {
        return OrganizationResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .slug(org.getSlug())
                .description(org.getDescription())
                .logoUrl(org.getLogoUrl())
                .bannerUrl(org.getBannerUrl())
                .primaryColor(org.getPrimaryColor())
                .secondaryColor(org.getSecondaryColor())
                .contactEmail(org.getContactEmail())
                .allowJoinRequests(org.isAllowJoinRequests())
                .createdAt(org.getCreatedAt())
                .build();
    }

    private OrganizationMemberResponse mapMemberToResponse(OrganizationMember member) {
        Profile profile = profileRepository.findById(member.getUserId()).orElse(null);
        return OrganizationMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .fullName(profile != null ? profile.getFullName() : null)
                .email(profile != null ? profile.getEmail() : null)
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }

    private InvitationResponse mapInvitationToResponse(OrganizationInvitation invitation) {
        return InvitationResponse.builder()
                .id(invitation.getId())
                .organizationId(invitation.getOrganizationId())
                .organizationName(invitation.getOrganization() != null ? invitation.getOrganization().getName() : null)
                .email(invitation.getEmail())
                .role(invitation.getRole())
                .status(invitation.getStatus().getValue())
                .token(invitation.getToken())
                .expiresAt(invitation.getExpiresAt())
                .createdAt(invitation.getCreatedAt())
                .build();
    }

    private JoinRequestResponse mapJoinRequestToResponse(MembershipRequest req) {
        Profile user = req.getUser();
        return JoinRequestResponse.builder()
                .id(req.getId())
                .organizationId(req.getOrganizationId())
                .organizationName(req.getOrganization() != null ? req.getOrganization().getName() : null)
                .userId(req.getUserId())
                .userFullName(user != null ? user.getFullName() : null)
                .userEmail(user != null ? user.getEmail() : null)
                .message(req.getMessage())
                .status(req.getStatus().getValue())
                .createdAt(req.getCreatedAt())
                .resolvedAt(req.getResolvedAt())
                .build();
    }
}
