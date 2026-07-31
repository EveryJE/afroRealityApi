package com.example.afrorealityapi.user.repository;

import com.example.afrorealityapi.user.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
