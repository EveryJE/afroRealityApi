package com.example.afrorealityapi.payment.controller;

import com.example.afrorealityapi.common.dto.ApiResponse;
import com.example.afrorealityapi.payment.dto.PaymentDtos.InitiatePaymentRequest;
import com.example.afrorealityapi.payment.dto.PaymentDtos.PaymentResponse;
import com.example.afrorealityapi.payment.dto.PaymentDtos.WalletResponse;
import com.example.afrorealityapi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(@RequestBody InitiatePaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.initiatePayment(request)));
    }

    @PostMapping("/verify/{reference}")
    public ResponseEntity<ApiResponse<PaymentResponse>> verifyPayment(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.verifyPayment(reference)));
    }

    @GetMapping("/{reference}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByReference(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getPaymentByReference(reference)));
    }

    @GetMapping("/wallets/user/{userId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getWalletByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getWalletByUserId(userId)));
    }

    @GetMapping("/wallets/organization/{organizationId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getWalletByOrg(@PathVariable UUID organizationId) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getWalletByOrganizationId(organizationId)));
    }
}
