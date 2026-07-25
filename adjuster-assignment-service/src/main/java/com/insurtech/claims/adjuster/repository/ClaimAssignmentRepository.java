package com.insurtech.claims.adjuster.repository;

import com.insurtech.claims.adjuster.entity.ClaimAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaimAssignmentRepository extends JpaRepository<ClaimAssignmentEntity, Long> {
    Optional<ClaimAssignmentEntity> findByClaimNumber(String claimNumber);
}
