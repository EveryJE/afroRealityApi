package com.example.afrorealityapi.voting.repository;

import com.example.afrorealityapi.voting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {
    List<Vote> findByOptionId(UUID optionId);
    List<Vote> findByEventId(UUID eventId);
}
