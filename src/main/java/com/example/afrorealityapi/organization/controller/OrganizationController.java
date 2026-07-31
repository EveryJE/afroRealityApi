package com.example.afrorealityapi.organization.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.CreateOrganizationRequest;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.OrganizationMemberResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.OrganizationResponse;
import com.example.afrorealityapi.organization.dto.OrganizationDtos.UpdateOrganizationRequest;
import com.example.afrorealityapi.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
