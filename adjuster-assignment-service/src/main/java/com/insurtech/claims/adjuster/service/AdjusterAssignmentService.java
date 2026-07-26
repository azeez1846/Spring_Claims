package com.insurtech.claims.adjuster.service;

import com.insurtech.claims.adjuster.entity.AdjusterEntity;
import com.insurtech.claims.adjuster.entity.ClaimAssignmentEntity;
import com.insurtech.claims.adjuster.repository.AdjusterRepository;
import com.insurtech.claims.adjuster.repository.ClaimAssignmentRepository;
import com.insurtech.claims.common.events.ClaimSubmittedEvent;
import com.insurtech.claims.common.events.UnderwritingAssignedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AdjusterAssignmentService {

    private static final Logger log = LoggerFactory.getLogger(AdjusterAssignmentService.class);
    private static final String ASSIGNED_TOPIC = "claims.fnol.assigned";

    private final AdjusterRepository adjusterRepository;
    private final ClaimAssignmentRepository claimAssignmentRepository;
    private final KafkaTemplate<String, UnderwritingAssignedEvent> kafkaTemplate;

    public AdjusterAssignmentService(AdjusterRepository adjusterRepository,
                                     ClaimAssignmentRepository claimAssignmentRepository,
                                     KafkaTemplate<String, UnderwritingAssignedEvent> kafkaTemplate) {
        this.adjusterRepository = adjusterRepository;
        this.claimAssignmentRepository = claimAssignmentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @SuppressWarnings("null")
    public UnderwritingAssignedEvent assignClaimToAdjuster(ClaimSubmittedEvent event) {
        log.info("Assigning adjuster for claimNumber={}, lineOfBusiness={}, severity={}",
                event.getClaimNumber(), event.getLineOfBusiness(), event.getLossSeverity());

        List<AdjusterEntity> availableAdjusters = adjusterRepository.findAvailableAdjustersByLob(event.getLineOfBusiness());

        AdjusterEntity assignedAdjuster;
        if (availableAdjusters.isEmpty()) {
            log.warn("No specific LOB adjuster available for {}. Falling back to default system adjuster.", event.getLineOfBusiness());
            assignedAdjuster = adjusterRepository.findAll().stream()
                    .filter(a -> a.getCurrentActiveClaims() < a.getMaxClaimCapacity())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No available claim adjusters in capacity!"));
        } else {
            assignedAdjuster = availableAdjusters.get(0);
        }

        // Increment active workload
        assignedAdjuster.setCurrentActiveClaims(assignedAdjuster.getCurrentActiveClaims() + 1);
        adjusterRepository.save(assignedAdjuster);

        String reason = "Auto-assigned based on LOB=" + event.getLineOfBusiness() + " and lowest current workload (" + assignedAdjuster.getCurrentActiveClaims() + ")";

        ClaimAssignmentEntity assignment = ClaimAssignmentEntity.builder()
                .claimNumber(event.getClaimNumber())
                .policyNumber(event.getPolicyNumber())
                .adjusterId(assignedAdjuster.getAdjusterId())
                .assignmentReason(reason)
                .assignedAt(LocalDateTime.now())
                .build();

        claimAssignmentRepository.save(assignment);

        UnderwritingAssignedEvent assignedEvent = UnderwritingAssignedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .claimNumber(event.getClaimNumber())
                .policyNumber(event.getPolicyNumber())
                .adjusterId(assignedAdjuster.getAdjusterId())
                .adjusterName(assignedAdjuster.getName())
                .adjusterEmail(assignedAdjuster.getEmail())
                .assignmentReason(reason)
                .initialReserve(event.getCalculatedReserve())
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(ASSIGNED_TOPIC, event.getClaimNumber(), assignedEvent);
        log.info("Published UnderwritingAssignedEvent for claimNumber={} assigned to adjusterId={}", event.getClaimNumber(), assignedAdjuster.getAdjusterId());

        return assignedEvent;
    }
}
