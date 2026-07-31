package com.example.afrorealityapi.auth.repository;

import com.example.afrorealityapi.auth.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(String email, String purpose);
    Optional<VerificationToken> findByEmailAndCodeAndPurposeAndUsedFalse(String email, String code, String purpose);
}
