package com.insurtech.claims.reserve.listener;

import com.insurtech.claims.common.events.UnderwritingAssignedEvent;
import com.insurtech.claims.reserve.service.ReserveSettlementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AssignmentEventListener {

    private static final Logger log = LoggerFactory.getLogger(AssignmentEventListener.class);

    private final ReserveSettlementService reserveSettlementService;

    public AssignmentEventListener(ReserveSettlementService reserveSettlementService) {
        this.reserveSettlementService = reserveSettlementService;
    }

    @KafkaListener(topics = "claims.fnol.assigned", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUnderwritingAssignedEvent(UnderwritingAssignedEvent event) {
        log.info("Received UnderwritingAssignedEvent from Kafka: claimNumber={}", event.getClaimNumber());
        try {
            reserveSettlementService.initializeReserve(event);
        } catch (Exception e) {
            log.error("Failed to initialize reserve for claimNumber={}", event.getClaimNumber(), e);
        }
    }
}
