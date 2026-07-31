package com.example.afrorealityapi.promoter.repository;

import com.example.afrorealityapi.promoter.entity.Promoter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromoterRepository extends JpaRepository<Promoter, UUID> {
    Optional<Promoter> findByUserId(UUID userId);
    Optional<Promoter> findByReferralCode(String referralCode);
    boolean existsByReferralCode(String referralCode);
}
