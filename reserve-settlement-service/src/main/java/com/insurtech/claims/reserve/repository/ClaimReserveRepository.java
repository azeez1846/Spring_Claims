package com.insurtech.claims.reserve.repository;

import com.insurtech.claims.reserve.entity.ClaimReserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaimReserveRepository extends JpaRepository<ClaimReserveEntity, Long> {
    Optional<ClaimReserveEntity> findByClaimNumber(String claimNumber);
}
