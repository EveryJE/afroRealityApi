package com.example.afrorealityapi.payment.repository;

import com.example.afrorealityapi.payment.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUserId(UUID userId);
    Optional<Wallet> findByOrganizationId(UUID organizationId);
}
