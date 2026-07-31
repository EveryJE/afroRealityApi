package com.example.afrorealityapi.voting.repository;

import com.example.afrorealityapi.voting.entity.VotingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VotingCategoryRepository extends JpaRepository<VotingCategory, UUID> {
    List<VotingCategory> findByEventIdOrderByOrderIdxAsc(UUID eventId);
}
