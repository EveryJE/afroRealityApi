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

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void sendMagicLink(RequestMagicLinkRequest request);
    AuthResponse loginWithMagicLink(String token);
    AuthResponse verifyEmail(VerifyEmailRequest request);
    void resendVerificationEmail(ResendVerificationRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    AuthResponse processOAuth2Login(OAuth2LoginRequest request);
}
