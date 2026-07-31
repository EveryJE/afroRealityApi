package com.example.afrorealityapi.voting.repository;

import com.example.afrorealityapi.voting.entity.VotingOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotingOptionRepository extends JpaRepository<VotingOption, UUID> {
    Optional<VotingOption> findByEventIdAndNomineeCode(UUID eventId, String nomineeCode);
    List<VotingOption> findByCategoryIdOrderByOrderIdxAsc(UUID categoryId);
}
