package com.insurtech.claims.policy.service;

import com.insurtech.claims.common.dto.PolicyVerificationRequest;
import com.insurtech.claims.common.dto.PolicyVerificationResponse;
import com.insurtech.claims.common.enums.CoverageType;
import com.insurtech.claims.common.enums.LineOfBusiness;
import com.insurtech.claims.policy.entity.PolicyCoverageEntity;
import com.insurtech.claims.policy.entity.PolicyEntity;
import com.insurtech.claims.policy.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PolicyServiceTest {

    private PolicyRepository policyRepository;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyRepository = Mockito.mock(PolicyRepository.class);
        policyService = new PolicyService(policyRepository);
    }

    @Test
    void testVerifyPolicy_Success() {
        PolicyEntity policy = PolicyEntity.builder()
                .policyNumber("POL-1001")
                .policyHolderName("Jane Doe")
                .policyHolderEmail("jane@example.com")
                .lineOfBusiness(LineOfBusiness.AUTO)
                .status("ACTIVE")
                .effectiveDate(LocalDateTime.now().minusMonths(6))
                .expirationDate(LocalDateTime.now().plusMonths(6))
                .build();

        PolicyCoverageEntity coverage = PolicyCoverageEntity.builder()
                .policy(policy)
                .coverageType(CoverageType.COLLISION)
                .coverageLimit(new BigDecimal("50000.00"))
                .deductibleAmount(new BigDecimal("500.00"))
                .active(true)
                .build();

        policy.setCoverages(List.of(coverage));

        when(policyRepository.findByPolicyNumber("POL-1001")).thenReturn(Optional.of(policy));

        PolicyVerificationRequest request = PolicyVerificationRequest.builder()
                .policyNumber("POL-1001")
                .lossDateTime(LocalDateTime.now())
                .coverageType(CoverageType.COLLISION)
                .build();

        PolicyVerificationResponse response = policyService.verifyPolicy(request);

        assertTrue(response.isValid());
        assertEquals("POL-1001", response.getPolicyNumber());
        assertEquals(new BigDecimal("500.00"), response.getDeductibleAmount());
        assertEquals(new BigDecimal("50000.00"), response.getCoverageLimit());
    }

    @Test
    void testVerifyPolicy_ExpiredPolicy() {
        PolicyEntity policy = PolicyEntity.builder()
                .policyNumber("POL-EXPIRED")
                .policyHolderName("John Smith")
                .policyHolderEmail("john@example.com")
                .lineOfBusiness(LineOfBusiness.AUTO)
                .status("EXPAIRED")
                .effectiveDate(LocalDateTime.now().minusYears(2))
                .expirationDate(LocalDateTime.now().minusYears(1))
                .build();

        when(policyRepository.findByPolicyNumber("POL-EXPIRED")).thenReturn(Optional.of(policy));

        PolicyVerificationRequest request = PolicyVerificationRequest.builder()
                .policyNumber("POL-EXPIRED")
                .lossDateTime(LocalDateTime.now())
                .coverageType(CoverageType.COLLISION)
                .build();

        PolicyVerificationResponse response = policyService.verifyPolicy(request);

        assertFalse(response.isValid());
        assertNotNull(response.getRejectionReason());
    }
}
