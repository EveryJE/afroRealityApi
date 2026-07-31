package com.example.afrorealityapi.payment.service;

import com.example.afrorealityapi.payment.dto.PaymentDtos.InitiatePaymentRequest;
import com.example.afrorealityapi.payment.dto.PaymentDtos.PaymentResponse;
import com.example.afrorealityapi.payment.dto.PaymentDtos.WalletResponse;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiatePayment(InitiatePaymentRequest request);
    PaymentResponse verifyPayment(String reference);
    PaymentResponse getPaymentByReference(String reference);
    WalletResponse getWalletByUserId(UUID userId);
    WalletResponse getWalletByOrganizationId(UUID organizationId);
}
