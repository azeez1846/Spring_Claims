package com.insurtech.claims.policy.service;

import com.insurtech.claims.common.dto.PolicyVerificationRequest;
import com.insurtech.claims.common.dto.PolicyVerificationResponse;
import com.insurtech.claims.policy.entity.PolicyCoverageEntity;
import com.insurtech.claims.policy.entity.PolicyEntity;
import com.insurtech.claims.policy.repository.PolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PolicyService {

    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Cacheable(value = "policies", key = "#policyNumber")
    public Optional<PolicyEntity> getPolicyByNumber(String policyNumber) {
        log.info("Fetching policy from database for policyNumber={}", policyNumber);
        return policyRepository.findByPolicyNumber(policyNumber);
    }

    public PolicyVerificationResponse verifyPolicy(PolicyVerificationRequest request) {
        log.info("Verifying policy number={} for lossDateTime={}", request.getPolicyNumber(), request.getLossDateTime());

        Optional<PolicyEntity> policyOpt = getPolicyByNumber(request.getPolicyNumber());

        if (policyOpt.isEmpty()) {
            return PolicyVerificationResponse.builder()
                    .policyNumber(request.getPolicyNumber())
                    .valid(false)
                    .rejectionReason("Policy not found in registry")
                    .build();
        }

        PolicyEntity policy = policyOpt.get();

        if (!"ACTIVE".equalsIgnoreCase(policy.getStatus())) {
            return PolicyVerificationResponse.builder()
                    .policyNumber(policy.getPolicyNumber())
                    .policyHolderName(policy.getPolicyHolderName())
                    .policyHolderEmail(policy.getPolicyHolderEmail())
                    .valid(false)
                    .rejectionReason("Policy status is " + policy.getStatus())
                    .build();
        }

        LocalDateTime lossDate = request.getLossDateTime();
        if (lossDate.isBefore(policy.getEffectiveDate()) || lossDate.isAfter(policy.getExpirationDate())) {
            return PolicyVerificationResponse.builder()
                    .policyNumber(policy.getPolicyNumber())
                    .policyHolderName(policy.getPolicyHolderName())
                    .policyHolderEmail(policy.getPolicyHolderEmail())
                    .valid(false)
                    .rejectionReason("Loss date " + lossDate + " is outside policy term (" + policy.getEffectiveDate() + " to " + policy.getExpirationDate() + ")")
                    .build();
        }

        Optional<PolicyCoverageEntity> coverageOpt = policy.getCoverages().stream()
                .filter(c -> c.getCoverageType() == request.getCoverageType() && c.isActive())
                .findFirst();

        if (coverageOpt.isEmpty()) {
            return PolicyVerificationResponse.builder()
                    .policyNumber(policy.getPolicyNumber())
                    .policyHolderName(policy.getPolicyHolderName())
                    .policyHolderEmail(policy.getPolicyHolderEmail())
                    .valid(false)
                    .rejectionReason("Coverage type " + request.getCoverageType() + " is not active on this policy")
                    .build();
        }

        PolicyCoverageEntity coverage = coverageOpt.get();

        return PolicyVerificationResponse.builder()
                .policyNumber(policy.getPolicyNumber())
                .policyHolderName(policy.getPolicyHolderName())
                .policyHolderEmail(policy.getPolicyHolderEmail())
                .valid(true)
                .checkedCoverage(coverage.getCoverageType())
                .coverageLimit(coverage.getCoverageLimit())
                .deductibleAmount(coverage.getDeductibleAmount())
                .build();
    }
}
