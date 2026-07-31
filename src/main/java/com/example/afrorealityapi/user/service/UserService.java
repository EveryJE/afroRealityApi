package com.example.afrorealityapi.user.service;

import com.example.afrorealityapi.user.dto.UserDtos.ProfileResponse;
import com.example.afrorealityapi.user.dto.UserDtos.UpdateProfileRequest;

import java.util.UUID;

public interface UserService {
    ProfileResponse getProfileById(UUID userId);
    ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}
