package com.insurtech.claims.policy.repository;

import com.insurtech.claims.policy.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {
    Optional<PolicyEntity> findByPolicyNumber(String policyNumber);
}
