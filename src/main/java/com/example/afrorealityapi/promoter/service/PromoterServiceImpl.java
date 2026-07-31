package com.example.afrorealityapi.promoter.service;

import com.example.afrorealityapi.common.enums.PromoterStatus;
import com.example.afrorealityapi.common.exception.ApiException;
import com.example.afrorealityapi.common.exception.ResourceNotFoundException;
import com.example.afrorealityapi.promoter.dto.PromoterDtos.ApplyPromoterRequest;
import com.example.afrorealityapi.promoter.dto.PromoterDtos.PromoterResponse;
import com.example.afrorealityapi.promoter.entity.Promoter;
import com.example.afrorealityapi.promoter.repository.PromoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromoterServiceImpl implements PromoterService {

    private final PromoterRepository promoterRepository;

    @Override
    @Transactional
    public PromoterResponse applyAsPromoter(ApplyPromoterRequest request) {
        if (promoterRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new ApiException("User is already registered as a promoter");
        }
        if (promoterRepository.existsByReferralCode(request.getReferralCode())) {
            throw new ApiException("Referral code is already taken: " + request.getReferralCode());
        }

        Promoter promoter = Promoter.builder()
                .userId(request.getUserId())
                .referralCode(request.getReferralCode())
                .status(PromoterStatus.PENDING)
                .bankName(request.getBankName())
                .bankAccountNumber(request.getBankAccountNumber())
                .bankAccountName(request.getBankAccountName())
                .build();

        Promoter saved = promoterRepository.save(promoter);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PromoterResponse getPromoterByUserId(UUID userId) {
        Promoter promoter = promoterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Promoter profile not found for user: " + userId));
        return mapToResponse(promoter);
    }

    @Override
    @Transactional(readOnly = true)
    public PromoterResponse getPromoterByCode(String referralCode) {
        Promoter promoter = promoterRepository.findByReferralCode(referralCode)
                .orElseThrow(() -> new ResourceNotFoundException("Promoter not found with referral code: " + referralCode));
        return mapToResponse(promoter);
    }

    private PromoterResponse mapToResponse(Promoter p) {
        return PromoterResponse.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .referralCode(p.getReferralCode())
                .status(p.getStatus())
                .commissionRate(p.getCommissionRate())
                .tier(p.getTier())
                .isGoldTier(p.isGoldTier())
                .bankName(p.getBankName())
                .bankAccountNumber(p.getBankAccountNumber())
                .build();
    }
}
