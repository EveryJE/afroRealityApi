package com.example.afrorealityapi.organization.repository;

import com.example.afrorealityapi.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Organization> findByCreatedBy(UUID createdBy);

    @Query("SELECT o FROM Organization o WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(o.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Organization> searchByNameOrDescription(@Param("search") String search);
}
