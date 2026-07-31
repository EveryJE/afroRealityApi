package com.example.afrorealityapi.payment.service;

import com.example.afrorealityapi.common.enums.CurrencyCode;
import com.example.afrorealityapi.common.enums.FinancialStatus;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.payment.dto.PaymentDtos.InitiatePaymentRequest;
import com.example.afrorealityapi.payment.dto.PaymentDtos.PaymentResponse;
import com.example.afrorealityapi.payment.dto.PaymentDtos.WalletResponse;
import com.example.afrorealityapi.payment.entity.Payment;
import com.example.afrorealityapi.payment.entity.Wallet;
import com.example.afrorealityapi.payment.repository.PaymentRepository;
import com.example.afrorealityapi.payment.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(InitiatePaymentRequest request) {
        String reference = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .reference(reference)
                .userId(request.getUserId())
                .email(request.getEmail())
                .purpose(request.getPurpose())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : CurrencyCode.GHS)
                .provider(request.getProvider())
                .status(FinancialStatus.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(String reference) {
        Payment payment = paymentRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with reference: " + reference));

        payment.setStatus(FinancialStatus.COMPLETED);
        payment.setVerifiedAt(OffsetDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByReference(String reference) {
        Payment payment = paymentRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with reference: " + reference));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponse getWalletByUserId(UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));
        return mapWalletToResponse(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponse getWalletByOrganizationId(UUID organizationId) {
        Wallet wallet = walletRepository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for organization: " + organizationId));
        return mapWalletToResponse(wallet);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .reference(payment.getReference())
                .email(payment.getEmail())
                .purpose(payment.getPurpose())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .provider(payment.getProvider())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    private WalletResponse mapWalletToResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .organizationId(wallet.getOrganizationId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .pendingCredits(wallet.getPendingCredits())
                .pendingDebits(wallet.getPendingDebits())
                .isActive(wallet.isActive())
                .build();
    }
}
