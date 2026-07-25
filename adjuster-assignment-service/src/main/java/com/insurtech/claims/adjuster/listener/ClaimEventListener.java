package com.insurtech.claims.adjuster.listener;

import com.insurtech.claims.adjuster.service.AdjusterAssignmentService;
import com.insurtech.claims.common.events.ClaimSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventListener {

    private static final Logger log = LoggerFactory.getLogger(ClaimEventListener.class);

    private final AdjusterAssignmentService assignmentService;

    public ClaimEventListener(AdjusterAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @KafkaListener(topics = "claims.fnol.submitted", groupId = "${spring.kafka.consumer.group-id}")
    public void handleClaimSubmittedEvent(ClaimSubmittedEvent event) {
        log.info("Received ClaimSubmittedEvent from Kafka: claimNumber={}", event.getClaimNumber());
        try {
            assignmentService.assignClaimToAdjuster(event);
        } catch (Exception e) {
            log.error("Failed to assign adjuster for claimNumber={}", event.getClaimNumber(), e);
        }
    }
}
