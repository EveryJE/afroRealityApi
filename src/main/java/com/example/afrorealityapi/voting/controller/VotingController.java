package com.example.afrorealityapi.voting.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.CreateVotingCategoryRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.NominateOptionRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.VoteRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.VoteResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.VotingCategoryResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.VotingOptionResponse;
import com.example.afrorealityapi.voting.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/voting")
@RequiredArgsConstructor
public class VotingController {

    private final VotingService votingService;

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<VotingCategoryResponse>> createCategory(
            @RequestBody CreateVotingCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(votingService.createCategory(request)));
    }

    @GetMapping("/categories/event/{eventId}")
    public ResponseEntity<ApiResponse<List<VotingCategoryResponse>>> getCategoriesByEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(ApiResponse.ok(votingService.getCategoriesByEvent(eventId)));
    }

    @PostMapping("/options")
    public ResponseEntity<ApiResponse<VotingOptionResponse>> createOption(
            @RequestBody NominateOptionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(votingService.createOption(request)));
    }

    @GetMapping("/options/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<VotingOptionResponse>>> getOptionsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(ApiResponse.ok(votingService.getOptionsByCategory(categoryId)));
    }

    @PostMapping("/vote")
    public ResponseEntity<ApiResponse<VoteResponse>> castVote(@RequestBody VoteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(votingService.castVote(request)));
    }
}
