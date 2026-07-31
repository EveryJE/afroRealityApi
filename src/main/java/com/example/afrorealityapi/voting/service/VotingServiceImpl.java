package com.example.afrorealityapi.voting.service;

import com.example.afrorealityapi.common.enums.ApprovalStatus;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.voting.dto.VotingDtos.CreateVotingCategoryRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.NominateOptionRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.VoteRequest;
import com.example.afrorealityapi.voting.dto.VotingDtos.VoteResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.VotingCategoryResponse;
import com.example.afrorealityapi.voting.dto.VotingDtos.VotingOptionResponse;
import com.example.afrorealityapi.voting.entity.Vote;
import com.example.afrorealityapi.voting.entity.VotingCategory;
import com.example.afrorealityapi.voting.entity.VotingOption;
import com.example.afrorealityapi.voting.repository.VoteRepository;
import com.example.afrorealityapi.voting.repository.VotingCategoryRepository;
import com.example.afrorealityapi.voting.repository.VotingOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VotingServiceImpl implements VotingService {

    private final VotingCategoryRepository categoryRepository;
    private final VotingOptionRepository optionRepository;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public VotingCategoryResponse createCategory(CreateVotingCategoryRequest request) {
        VotingCategory category = VotingCategory.builder()
                .eventId(request.getEventId())
                .name(request.getName())
                .description(request.getDescription())
                .maxVotesPerUser(request.getMaxVotesPerUser() > 0 ? request.getMaxVotesPerUser() : 1)
                .allowMultiple(request.isAllowMultiple())
                .votePrice(request.getVotePrice() != null ? request.getVotePrice() : BigDecimal.ZERO)
                .build();

        VotingCategory saved = categoryRepository.save(category);
        return mapCategoryToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VotingCategoryResponse> getCategoriesByEvent(UUID eventId) {
        return categoryRepository.findByEventIdOrderByOrderIdxAsc(eventId).stream()
                .map(this::mapCategoryToResponse)
                .toList();
    }

    @Override
    @Transactional
    public VotingOptionResponse createOption(NominateOptionRequest request) {
        VotingOption option = VotingOption.builder()
                .eventId(request.getEventId())
                .categoryId(request.getCategoryId())
                .optionText(request.getOptionText())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .nomineeCode(request.getNomineeCode())
                .status(ApprovalStatus.APPROVED)
                .build();

        VotingOption saved = optionRepository.save(option);
        return mapOptionToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VotingOptionResponse> getOptionsByCategory(UUID categoryId) {
        return optionRepository.findByCategoryIdOrderByOrderIdxAsc(categoryId).stream()
                .map(this::mapOptionToResponse)
                .toList();
    }

    @Override
    @Transactional
    public VoteResponse castVote(VoteRequest request) {
        VotingOption option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Voting option not found: " + request.getOptionId()));

        int count = request.getVoteCount() > 0 ? request.getVoteCount() : 1;

        Vote vote = Vote.builder()
                .eventId(request.getEventId())
                .optionId(request.getOptionId())
                .categoryId(request.getCategoryId())
                .voterId(request.getVoterId())
                .voteCount(count)
                .voterEmail(request.getVoterEmail())
                .voterPhone(request.getVoterPhone())
                .build();

        Vote saved = voteRepository.save(vote);

        // Increment vote count on option
        option.setVotesCount(option.getVotesCount() + count);
        optionRepository.save(option);

        return VoteResponse.builder()
                .id(saved.getId())
                .eventId(saved.getEventId())
                .optionId(saved.getOptionId())
                .voteCount(saved.getVoteCount())
                .build();
    }

    private VotingCategoryResponse mapCategoryToResponse(VotingCategory cat) {
        return VotingCategoryResponse.builder()
                .id(cat.getId())
                .eventId(cat.getEventId())
                .name(cat.getName())
                .description(cat.getDescription())
                .maxVotesPerUser(cat.getMaxVotesPerUser())
                .votePrice(cat.getVotePrice())
                .build();
    }

    private VotingOptionResponse mapOptionToResponse(VotingOption opt) {
        return VotingOptionResponse.builder()
                .id(opt.getId())
                .eventId(opt.getEventId())
                .categoryId(opt.getCategoryId())
                .optionText(opt.getOptionText())
                .description(opt.getDescription())
                .imageUrl(opt.getImageUrl())
                .nomineeCode(opt.getNomineeCode())
                .votesCount(opt.getVotesCount())
                .status(opt.getStatus())
                .build();
    }
}
