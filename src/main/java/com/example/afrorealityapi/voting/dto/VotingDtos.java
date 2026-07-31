package com.example.afrorealityapi.voting.dto;

import com.example.afrorealityapi.common.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

public class VotingDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateVotingCategoryRequest {
        private UUID eventId;
        private String name;
        private String description;
        private int maxVotesPerUser;
        private boolean allowMultiple;
        private BigDecimal votePrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VotingCategoryResponse {
        private UUID id;
        private UUID eventId;
        private String name;
        private String description;
        private int maxVotesPerUser;
        private BigDecimal votePrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NominateOptionRequest {
        private UUID eventId;
        private UUID categoryId;
        private String optionText;
        private String description;
        private String imageUrl;
        private String nomineeCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VotingOptionResponse {
        private UUID id;
        private UUID eventId;
        private UUID categoryId;
        private String optionText;
        private String description;
        private String imageUrl;
        private String nomineeCode;
        private long votesCount;
        private ApprovalStatus status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VoteRequest {
        private UUID eventId;
        private UUID optionId;
        private UUID categoryId;
        private UUID voterId;
        private int voteCount;
        private String voterEmail;
        private String voterPhone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VoteResponse {
        private UUID id;
        private UUID eventId;
        private UUID optionId;
        private int voteCount;
    }
}
