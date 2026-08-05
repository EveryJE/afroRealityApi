package com.example.afrorealityapi.user.service;

import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.user.dto.UserDtos.ProfileResponse;
import com.example.afrorealityapi.user.dto.UserDtos.UpdateProfileRequest;
import com.example.afrorealityapi.user.entity.Profile;
import com.example.afrorealityapi.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfileById(UUID userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

        if (request.getUsername() != null) profile.setUsername(request.getUsername());
        if (request.getFullName() != null) profile.setFullName(request.getFullName());
        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());
        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getWhatsappOptIn() != null) profile.setWhatsappOptIn(request.getWhatsappOptIn());

        Profile saved = profileRepository.save(profile);
        return mapToResponse(saved);
    }

    private ProfileResponse mapToResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .fullName(profile.getFullName())
                .avatarUrl(profile.getAvatarUrl())
                .phone(profile.getPhone())
                .whatsappOptIn(profile.isWhatsappOptIn())
                .pricingPlan(profile.getPricingPlan())
                .communicationCredits(profile.getCommunicationCredits())
                .isVerifiedPartner(profile.isVerifiedPartner())
                .onboardingCompleted(profile.isOnboardingCompleted())
                .onboardingStep(profile.getOnboardingStep())
                .currentOrganizationId(profile.getCurrentOrganizationId())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}
