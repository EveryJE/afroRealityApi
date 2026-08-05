package com.example.afrorealityapi.common.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BrevoApiEmailServiceImpl implements EmailService {

    @Value("${app.brevo.api-key:}")
    private String apiKey;

    @Value("${app.brevo.sender-email:kgyan19lf@gmail.com}")
    private String senderEmail;

    @Value("${app.brevo.sender-name:AfroReality}")
    private String senderName;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendVerificationOtp(String toEmail, String code) {
        String subject = "Your AfroReality Verification Code";
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                + "<h2 style='color: #4F46E5;'>Welcome to AfroReality!</h2>"
                + "<p>Your 6-digit verification code is:</p>"
                + "<h1 style='letter-spacing: 4px; color: #111827; background: #F3F4F6; padding: 10px 20px; display: inline-block; border-radius: 8px;'>" + code + "</h1>"
                + "<p>This code will expire in 15 minutes.</p>"
                + "</div>";

        sendEmail(toEmail, subject, htmlContent);
    }

    @Override
    public void sendMagicLink(String toEmail, String magicLink) {
        log.info("============== DEV MAGIC LINK GENERATED ==============");
        log.info("TO: {}", toEmail);
        log.info("MAGIC LINK URL: {}", magicLink);
        log.info("======================================================");

        String subject = "Your AfroReality Magic Login Link";
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                + "<h2 style='color: #4F46E5;'>AfroReality Magic Login</h2>"
                + "<p>Click the link below to log in instantly without a password:</p>"
                + "<p><a href='" + magicLink + "'>" + magicLink + "</a></p>"
                + "<p style='margin-top: 20px; color: #6B7280;'>If you did not request this link, please ignore this email.</p>"
                + "</div>";

        sendEmail(toEmail, subject, htmlContent);
    }

    @Override
    public void sendPasswordResetOtp(String toEmail, String code) {
        String subject = "AfroReality Password Reset Code";
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                + "<h2 style='color: #DC2626;'>Password Reset Request</h2>"
                + "<p>Your 6-digit password reset code is:</p>"
                + "<h1 style='letter-spacing: 4px; color: #111827; background: #F3F4F6; padding: 10px 20px; display: inline-block; border-radius: 8px;'>" + code + "</h1>"
                + "<p>This code will expire in 15 minutes.</p>"
                + "</div>";

        sendEmail(toEmail, subject, htmlContent);
    }

    @Override
    public void sendOrganizationInvitation(String toEmail, String orgName, String inviteLink) {
        String subject = "You've been invited to join " + orgName + " on AfroReality";
        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                + "<h2 style='color: #4F46E5;'>Organization Invitation</h2>"
                + "<p>You have been invited to join <strong>" + orgName + "</strong> on AfroReality.</p>"
                + "<p>Click the link below to accept the invitation:</p>"
                + "<p><a href='" + inviteLink + "' style='background: #4F46E5; color: #fff; padding: 10px 20px; text-decoration: none; border-radius: 6px; display: inline-block;'>Accept Invitation</a></p>"
                + "<p style='margin-top: 15px; font-size: 12px; color: #6B7280;'>Or copy this link: " + inviteLink + "</p>"
                + "<p style='margin-top: 20px; color: #6B7280;'>This invitation will expire in 48 hours.</p>"
                + "</div>";

        sendEmail(toEmail, subject, htmlContent);
    }

    private void sendEmail(String toEmail, String subject, String htmlContent) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("BREVO_API_KEY is missing. Logging email content to console.");
            logConsoleFallback(toEmail, subject, htmlContent);
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey.trim());
            headers.set("accept", "application/json");

            Map<String, Object> body = Map.of(
                    "sender", Map.of("email", senderEmail, "name", senderName),
                    "to", List.of(Map.of("email", toEmail)),
                    "subject", subject,
                    "htmlContent", htmlContent
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(BREVO_API_URL, request, String.class);
            log.info("Email successfully sent via Brevo HTTP API to {}", toEmail);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Failed to send email via Brevo HTTP API to {}: HTTP {} - Body: {}", toEmail, e.getStatusCode(), e.getResponseBodyAsString());
            logConsoleFallback(toEmail, subject, htmlContent);
        } catch (Exception e) {
            log.error("Failed to send email via Brevo HTTP API to {}: {}", toEmail, e.getMessage());
            logConsoleFallback(toEmail, subject, htmlContent);
        }
    }

    private void logConsoleFallback(String toEmail, String subject, String htmlContent) {
        log.info("============== DEV EMAIL FALLBACK ==============");
        log.info("TO: {}", toEmail);
        log.info("SUBJECT: {}", subject);
        log.info("CONTENT: {}", htmlContent.replaceAll("<[^>]*>", " "));
        log.info("================================================");
    }
}
