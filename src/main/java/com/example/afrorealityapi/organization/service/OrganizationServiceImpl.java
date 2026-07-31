package com.example.afrorealityapi.organization.service;

import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.CreateOrganizationRequest;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.OrganizationMemberResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.OrganizationResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.UpdateOrganizationRequest;
import com.example.afrorealityapi.organization.entity.Organization;
import com.example.afrorealityapi.organization.entity.OrganizationMember;
import com.example.afrorealityapi.organization.repository.OrganizationMemberRepository;
import com.example.afrorealityapi.organization.repository.OrganizationRepository;
import com.example.afrorealityapi.user.entity.Profile;
import com.example.afrorealityapi.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;
    private final ProfileRepository profileRepository;

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
}
