package com.example.afrorealityapi.organization.service;

import com.example.afrorealityapi.organization.dto.OrganizationDtos.CreateOrganizationRequest;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.OrganizationMemberResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.OrganizationResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.UpdateOrganizationRequest;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    OrganizationResponse createOrganization(UUID creatorId, CreateOrganizationRequest request);
    OrganizationResponse getOrganizationById(UUID orgId);
    OrganizationResponse getOrganizationBySlug(String slug);
    OrganizationResponse updateOrganization(UUID orgId, UpdateOrganizationRequest request);
    List<OrganizationMemberResponse> getMembers(UUID orgId);
}
