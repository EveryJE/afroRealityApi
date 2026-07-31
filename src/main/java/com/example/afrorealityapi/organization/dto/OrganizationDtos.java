package com.example.afrorealityapi.organization.dto;

import com.example.afrorealityapi.common.enums.OrganizationRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

public class OrganizationDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateOrganizationRequest {
        private String name;
        private String slug;
        private String description;
        private String contactEmail;
        private String phone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateOrganizationRequest {
        private String name;
        private String description;
        private String logoUrl;
        private String bannerUrl;
        private String primaryColor;
        private String secondaryColor;
        private String tertiaryColor;
        private String contactEmail;
        private String phone;
        private String websiteUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrganizationResponse {
        private UUID id;
        private String name;
        private String slug;
        private String description;
        private String logoUrl;
        private String bannerUrl;
        private String primaryColor;
        private String secondaryColor;
        private String contactEmail;
        private boolean allowJoinRequests;
        private OffsetDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrganizationMemberResponse {
        private UUID id;
        private UUID userId;
        private String fullName;
        private String email;
        private OrganizationRole role;
        private OffsetDateTime joinedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InviteMemberRequest {
        private String email;
        private OrganizationRole role;
    }
}
