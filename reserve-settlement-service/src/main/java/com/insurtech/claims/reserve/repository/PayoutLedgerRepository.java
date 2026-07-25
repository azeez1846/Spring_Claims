package com.insurtech.claims.reserve.repository;

import com.insurtech.claims.reserve.entity.PayoutLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutLedgerRepository extends JpaRepository<PayoutLedgerEntity, Long> {
    List<PayoutLedgerEntity> findByClaimNumber(String claimNumber);
}
