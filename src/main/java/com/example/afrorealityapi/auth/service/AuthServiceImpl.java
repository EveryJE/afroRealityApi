package com.example.afrorealityapi.auth.service;

import com.example.afrorealityapi.auth.dto.AuthDtos.AuthResponse;
import com.example.afrorealityapi.auth.dto.AuthDtos.ForgotPasswordRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.LoginRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.OAuth2LoginRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.RegisterRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.RequestMagicLinkRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.ResendVerificationRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.ResetPasswordRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.VerifyEmailRequest;
import com.example.afrorealityapi.auth.entity.VerificationToken;
import com.example.afrorealityapi.auth.repository.VerificationTokenRepository;
import com.example.afrorealityapi.common.email.EmailService;
import com.example.afrorealityapi.common.exception.ApiException;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.common.security.JwtService;
import com.example.afrorealityapi.user.entity.Profile;
import com.example.afrorealityapi.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ProfileRepository profileRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @org.springframework.beans.factory.annotation.Value("${app.frontend-url:http://localhost:3000}")
    private String defaultFrontendUrl;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (profileRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email is already registered: " + request.getEmail());
        }
        if (request.getUsername() != null && profileRepository.existsByUsername(request.getUsername())) {
            throw new ApiException("Username is already taken: " + request.getUsername());
        }

        Profile profile = Profile.builder()
                .email(request.getEmail())
                .passwordHash(request.getPassword() != null && !request.getPassword().isBlank()
                        ? passwordEncoder.encode(request.getPassword()) : null)
                .username(request.getUsername() != null ? request.getUsername() : generateFallbackUsername(request.getEmail()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .onboardingCompleted(false)
                .onboardingStep(1)
                .pricingPlan("essential")
                .isVerifiedPartner(false)
                .build();

        Profile saved = profileRepository.save(profile);

        // Generate 6-digit OTP code and send email
        sendVerificationOtpInternal(saved.getEmail());

        String jwt = jwtService.generateToken(saved.getId(), saved.getEmail());

        return AuthResponse.builder()
                .userId(saved.getId())
                .email(saved.getEmail())
                .username(saved.getUsername())
                .fullName(saved.getFullName())
                .token(jwt)
                .isEmailVerified(saved.isVerifiedPartner())
                .message("Registration successful! 6-digit verification code sent to your email.")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (profile.getPasswordHash() != null) {
            if (request.getPassword() == null || !passwordEncoder.matches(request.getPassword(), profile.getPasswordHash())) {
                throw new ApiException("Invalid credentials");
            }
        } else {
            throw new ApiException("Password not set for account. Please log in using Social Login or request a Magic Link.");
        }

        String jwt = jwtService.generateToken(profile.getId(), profile.getEmail());

        return AuthResponse.builder()
                .userId(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .fullName(profile.getFullName())
                .token(jwt)
                .isEmailVerified(profile.isVerifiedPartner())
                .message("Login successful")
                .build();
    }

    @Override
    public void sendMagicLink(RequestMagicLinkRequest request) {
        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        String magicToken = jwtService.generateVerificationToken(profile.getEmail());
        String baseUrl = (request.getRedirectUrl() != null && !request.getRedirectUrl().isBlank())
                ? request.getRedirectUrl().replaceAll("/+$", "")
                : defaultFrontendUrl.replaceAll("/+$", "") + "/auth/magic-login";

        String magicLink = baseUrl.contains("?")
                ? baseUrl + "&token=" + magicToken
                : baseUrl + "?token=" + magicToken;

        emailService.sendMagicLink(profile.getEmail(), magicLink);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse loginWithMagicLink(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new ApiException("Invalid or expired magic link");
        }

        String email = jwtService.extractEmail(token);
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        String jwt = jwtService.generateToken(profile.getId(), profile.getEmail());

        return AuthResponse.builder()
                .userId(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .fullName(profile.getFullName())
                .token(jwt)
                .isEmailVerified(true)
                .message("Logged in successfully via Magic Link")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse verifyEmail(VerifyEmailRequest request) {
        VerificationToken token = verificationTokenRepository
                .findByEmailAndCodeAndPurposeAndUsedFalse(request.getEmail(), request.getCode(), "EMAIL_VERIFICATION")
                .orElseThrow(() -> new ApiException("Invalid verification code for email: " + request.getEmail()));

        if (token.isExpired()) {
            throw new ApiException("Verification code has expired. Please request a new code.");
        }

        token.setUsed(true);
        verificationTokenRepository.save(token);

        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        profile.setVerifiedPartner(true);
        profileRepository.save(profile);

        String jwt = jwtService.generateToken(profile.getId(), profile.getEmail());

        return AuthResponse.builder()
                .userId(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .fullName(profile.getFullName())
                .token(jwt)
                .isEmailVerified(true)
                .message("Email verified successfully!")
                .build();
    }

    @Override
    @Transactional
    public void resendVerificationEmail(ResendVerificationRequest request) {
        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        if (profile.isVerifiedPartner()) {
            throw new ApiException("Email is already verified");
        }

        sendVerificationOtpInternal(profile.getEmail());
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        String code = String.format("%06d", new SecureRandom().nextInt(1000000));

        VerificationToken token = VerificationToken.builder()
                .email(profile.getEmail())
                .code(code)
                .purpose("PASSWORD_RESET")
                .used(false)
                .expiresAt(OffsetDateTime.now().plusMinutes(15))
                .build();

        verificationTokenRepository.save(token);

        emailService.sendPasswordResetOtp(profile.getEmail(), code);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        VerificationToken token = verificationTokenRepository
                .findByEmailAndCodeAndPurposeAndUsedFalse(request.getEmail(), request.getCode(), "PASSWORD_RESET")
                .orElseThrow(() -> new ApiException("Invalid or expired password reset code"));

        if (token.isExpired()) {
            throw new ApiException("Password reset code has expired");
        }

        token.setUsed(true);
        verificationTokenRepository.save(token);

        Profile profile = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        profile.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public AuthResponse processOAuth2Login(OAuth2LoginRequest request) {
        Optional<Profile> existingProfile = profileRepository.findByEmail(request.getEmail());

        Profile profile;
        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
            if (request.getAvatarUrl() != null && profile.getAvatarUrl() == null) {
                profile.setAvatarUrl(request.getAvatarUrl());
                profileRepository.save(profile);
            }
        } else {
            profile = Profile.builder()
                    .email(request.getEmail())
                    .username(generateFallbackUsername(request.getEmail()))
                    .fullName(request.getFullName())
                    .avatarUrl(request.getAvatarUrl())
                    .isVerifiedPartner(true)
                    .onboardingCompleted(true)
                    .pricingPlan("essential")
                    .build();
            profile = profileRepository.save(profile);
        }

        String jwt = jwtService.generateToken(profile.getId(), profile.getEmail());

        return AuthResponse.builder()
                .userId(profile.getId())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .fullName(profile.getFullName())
                .token(jwt)
                .isEmailVerified(true)
                .message("OAuth2 (" + request.getProvider() + ") login successful")
                .build();
    }

    private void sendVerificationOtpInternal(String email) {
        String code = String.format("%06d", new SecureRandom().nextInt(1000000));

        VerificationToken token = VerificationToken.builder()
                .email(email)
                .code(code)
                .purpose("EMAIL_VERIFICATION")
                .used(false)
                .expiresAt(OffsetDateTime.now().plusMinutes(15))
                .build();

        verificationTokenRepository.save(token);

        emailService.sendVerificationOtp(email, code);
    }

    private String generateFallbackUsername(String email) {
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
        return base + "_" + UUID.randomUUID().toString().substring(0, 4);
    }
}
