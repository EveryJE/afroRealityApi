package com.example.afrorealityapi.voting.service;

import com.example.afrorealityapi.voting.dto.VotingDtos.CreateVotingCategoryRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.NominateOptionRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.VoteRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.VoteResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.VotingCategoryResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.VotingOptionResponse;

import java.util.List;
import java.util.UUID;

public interface VotingService {
    VotingCategoryResponse createCategory(CreateVotingCategoryRequest request);
    List<VotingCategoryResponse> getCategoriesByEvent(UUID eventId);
    VotingOptionResponse createOption(NominateOptionRequest request);
    List<VotingOptionResponse> getOptionsByCategory(UUID categoryId);
    VoteResponse castVote(VoteRequest request);
}
