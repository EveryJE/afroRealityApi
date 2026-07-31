package com.example.afrorealityapi.payment.repository;

import com.example.afrorealityapi.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByReference(String reference);
    Optional<Payment> findByProviderReference(String providerReference);
    List<Payment> findByUserId(UUID userId);
}
