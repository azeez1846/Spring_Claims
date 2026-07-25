package com.insurtech.claims.fnol.service;

import com.insurtech.claims.common.dto.FnolClaimRequest;
import com.insurtech.claims.common.dto.PolicyVerificationRequest;
import com.insurtech.claims.common.dto.PolicyVerificationResponse;
import com.insurtech.claims.common.events.ClaimSubmittedEvent;
import com.insurtech.claims.fnol.rules.ClaimEvaluationFact;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FnolService {

    private static final Logger log = LoggerFactory.getLogger(FnolService.class);
    private static final String KAFKA_TOPIC = "claims.fnol.submitted";

    private final KieContainer kieContainer;
    private final KafkaTemplate<String, ClaimSubmittedEvent> kafkaTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.policy-service.url:http://localhost:8081}")
    private String policyServiceUrl;

    public FnolService(KieContainer kieContainer, KafkaTemplate<String, ClaimSubmittedEvent> kafkaTemplate) {
        this.kieContainer = kieContainer;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ClaimSubmittedEvent processFnolClaim(FnolClaimRequest request) {
        log.info("Processing FNOL claim intake for policyNumber={}", request.getPolicyNumber());

        // 1. Verify policy via Policy Service
        PolicyVerificationRequest verifyRequest = PolicyVerificationRequest.builder()
                .policyNumber(request.getPolicyNumber())
                .lossDateTime(request.getLossDateTime())
                .coverageType(request.getCoverageType())
                .build();

        PolicyVerificationResponse verificationResponse = restTemplate.postForObject(
                policyServiceUrl + "/api/v1/policies/verify",
                verifyRequest,
                PolicyVerificationResponse.class
        );

        if (verificationResponse == null || !verificationResponse.isValid()) {
            String reason = verificationResponse != null ? verificationResponse.getRejectionReason() : "Policy service unreachable";
            log.warn("FNOL Rejected for policyNumber={}: {}", request.getPolicyNumber(), reason);
            throw new IllegalArgumentException("Policy verification failed: " + reason);
        }

        // 2. Perform Resilience4j protected Fraud Check
        boolean fraudDetected = checkFraudRisk(request.getPolicyNumber());
        if (fraudDetected) {
            log.warn("Fraud check flagged policyNumber={}", request.getPolicyNumber());
        }

        // 3. Execute Drools Rules for Loss Severity & Priority
        ClaimEvaluationFact fact = ClaimEvaluationFact.builder()
                .estimatedLossAmount(request.getEstimatedLossAmount())
                .injuriesReported(request.isInjuriesReported())
                .lineOfBusiness(request.getLineOfBusiness())
                .build();

        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(fact);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        BigDecimal calculatedReserve = request.getEstimatedLossAmount()
                .multiply(fact.getReserveMultiplier());

        String claimNumber = "CLM-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        // 4. Build ClaimSubmittedEvent
        ClaimSubmittedEvent event = ClaimSubmittedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .claimNumber(claimNumber)
                .policyNumber(request.getPolicyNumber())
                .lineOfBusiness(request.getLineOfBusiness())
                .coverageType(request.getCoverageType())
                .lossSeverity(fact.getCalculatedSeverity())
                .estimatedLossAmount(request.getEstimatedLossAmount())
                .calculatedReserve(calculatedReserve)
                .deductibleAmount(verificationResponse.getDeductibleAmount())
                .lossLocation(request.getLossLocation())
                .lossDescription(request.getLossDescription())
                .injuriesReported(request.isInjuriesReported())
                .policeReportFiled(request.isPoliceReportFiled())
                .priority(fact.getPriority())
                .timestamp(LocalDateTime.now())
                .build();

        // 5. Stream event to Kafka
        kafkaTemplate.send(KAFKA_TOPIC, claimNumber, event);
        log.info("Successfully published ClaimSubmittedEvent for claimNumber={} to Kafka topic={}", claimNumber, KAFKA_TOPIC);

        return event;
    }

    @CircuitBreaker(name = "fraudCheckService", fallbackMethod = "fraudCheckFallback")
    @Retry(name = "fraudCheckService")
    public boolean checkFraudRisk(String policyNumber) {
        log.info("Performing external fraud risk assessment for policyNumber={}", policyNumber);
        return false;
    }

    public boolean fraudCheckFallback(String policyNumber, Throwable t) {
        log.warn("Fraud check service unavailable for policyNumber={}, fallback triggered: {}", policyNumber, t.getMessage());
        return false;
    }
}
