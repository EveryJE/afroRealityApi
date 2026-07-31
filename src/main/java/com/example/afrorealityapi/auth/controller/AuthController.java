package com.example.afrorealityapi.auth.controller;

import com.example.afrorealityapi.auth.dto.AuthDtos.AuthResponse;
import com.example.afrorealityapi.auth.dto.AuthDtos.ForgotPasswordRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.LoginRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.OAuth2LoginRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.RegisterRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.RequestMagicLinkRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.ResendVerificationRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.ResetPasswordRequest;
import com.example.afrorealityapi.auth.dto.AuthDtos.VerifyEmailRequest;
import com.example.afrorealityapi.auth.service.AuthService;
import com.example.afrorealityapi.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(request)));
    }

    @PostMapping("/magic-link/request")
    public ResponseEntity<ApiResponse<String>> sendMagicLink(@RequestBody RequestMagicLinkRequest request) {
        authService.sendMagicLink(request);
        return ResponseEntity.ok(ApiResponse.ok("Magic login link dispatched to email", null));
    }

    @GetMapping("/magic-link/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithMagicLink(@RequestParam String token) {
        return ResponseEntity.ok(ApiResponse.ok(authService.loginWithMagicLink(token)));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyEmail(@RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.verifyEmail(request)));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerification(@RequestBody ResendVerificationRequest request) {
        authService.resendVerificationEmail(request);
        return ResponseEntity.ok(ApiResponse.ok("Verification email sent successfully", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.ok("Password reset OTP code sent to your email", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.ok("Password reset successfully", null));
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<ApiResponse<AuthResponse>> oauth2Login(@RequestBody OAuth2LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.processOAuth2Login(request)));
    }
}
