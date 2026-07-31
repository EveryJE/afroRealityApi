package com.example.afrorealityapi.user.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.user.dto.UserDtos.ProfileResponse;
import com.example.afrorealityapi.user.dto.UserDtos.UpdateProfileRequest;
import com.example.afrorealityapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getProfileById(userId)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @PathVariable UUID userId,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateProfile(userId, request)));
    }
}
