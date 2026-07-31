package com.example.afrorealityapi.common.email;

public interface EmailService {
    void sendVerificationOtp(String toEmail, String code);
    void sendMagicLink(String toEmail, String magicLink);
    void sendPasswordResetOtp(String toEmail, String code);
}
