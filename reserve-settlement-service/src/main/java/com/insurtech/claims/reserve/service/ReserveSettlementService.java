package com.insurtech.claims.reserve.service;

import com.insurtech.claims.common.events.PayoutProcessedEvent;
import com.insurtech.claims.common.events.ReserveCreatedEvent;
import com.insurtech.claims.common.events.UnderwritingAssignedEvent;
import com.insurtech.claims.reserve.dto.PayoutRequest;
import com.insurtech.claims.reserve.entity.ClaimReserveEntity;
import com.insurtech.claims.reserve.entity.PayoutLedgerEntity;
import com.insurtech.claims.reserve.repository.ClaimReserveRepository;
import com.insurtech.claims.reserve.repository.PayoutLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReserveSettlementService {

    private static final Logger log = LoggerFactory.getLogger(ReserveSettlementService.class);
    private static final String RESERVE_TOPIC = "claims.reserves.created";
    private static final String PAYOUT_TOPIC = "claims.payouts.processed";

    private final ClaimReserveRepository reserveRepository;
    private final PayoutLedgerRepository payoutLedgerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReserveSettlementService(ClaimReserveRepository reserveRepository,
                                  PayoutLedgerRepository payoutLedgerRepository,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
        this.reserveRepository = reserveRepository;
        this.payoutLedgerRepository = payoutLedgerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public ClaimReserveEntity initializeReserve(UnderwritingAssignedEvent event) {
        log.info("Initializing reserve ledger for claimNumber={}, initialReserve={}", event.getClaimNumber(), event.getInitialReserve());

        String reserveId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ClaimReserveEntity reserve = ClaimReserveEntity.builder()
                .reserveId(reserveId)
                .claimNumber(event.getClaimNumber())
                .initialReserveAmount(event.getInitialReserve())
                .currentReserveAmount(event.getInitialReserve())
                .paidAmount(BigDecimal.ZERO)
                .status("ESTABLISHED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        reserve = reserveRepository.save(reserve);

        ReserveCreatedEvent reserveEvent = ReserveCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .reserveId(reserveId)
                .claimNumber(event.getClaimNumber())
                .initialReserveAmount(event.getInitialReserve())
                .status("ESTABLISHED")
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(RESERVE_TOPIC, event.getClaimNumber(), reserveEvent);
        log.info("Published ReserveCreatedEvent for claimNumber={} to Kafka topic={}", event.getClaimNumber(), RESERVE_TOPIC);

        return reserve;
    }

    @Transactional
    public PayoutLedgerEntity processPayout(PayoutRequest request) {
        log.info("Processing payout for claimNumber={}, amount={}", request.getClaimNumber(), request.getPayoutAmount());

        ClaimReserveEntity reserve = reserveRepository.findByClaimNumber(request.getClaimNumber())
                .orElseThrow(() -> new IllegalArgumentException("No reserve found for claimNumber=" + request.getClaimNumber()));

        if (reserve.getCurrentReserveAmount().compareTo(request.getPayoutAmount()) < 0) {
            log.error("Insufficient reserve balance for claimNumber={}. Current reserve={}, requested payout={}",
                    request.getClaimNumber(), reserve.getCurrentReserveAmount(), request.getPayoutAmount());
            throw new IllegalStateException("Payout amount exceeds current reserve balance");
        }

        // Deduct from reserve and add to paid amount
        reserve.setCurrentReserveAmount(reserve.getCurrentReserveAmount().subtract(request.getPayoutAmount()));
        reserve.setPaidAmount(reserve.getPaidAmount().add(request.getPayoutAmount()));
        if (reserve.getCurrentReserveAmount().compareTo(BigDecimal.ZERO) == 0) {
            reserve.setStatus("EXHAUSTED");
        }
        reserve.setUpdatedAt(LocalDateTime.now());
        reserveRepository.save(reserve);

        String payoutId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        PayoutLedgerEntity payout = PayoutLedgerEntity.builder()
                .payoutId(payoutId)
                .claimNumber(request.getClaimNumber())
                .payoutAmount(request.getPayoutAmount())
                .payeeName(request.getPayeeName())
                .paymentMethod(request.getPaymentMethod())
                .status("PROCESSED")
                .processedAt(LocalDateTime.now())
                .build();

        payout = payoutLedgerRepository.save(payout);

        PayoutProcessedEvent event = PayoutProcessedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .payoutId(payoutId)
                .claimNumber(request.getClaimNumber())
                .payoutAmount(request.getPayoutAmount())
                .payeeName(request.getPayeeName())
                .paymentMethod(request.getPaymentMethod())
                .status("PROCESSED")
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(PAYOUT_TOPIC, request.getClaimNumber(), event);
        log.info("Published PayoutProcessedEvent for claimNumber={} payoutId={} to Kafka topic={}", request.getClaimNumber(), payoutId, PAYOUT_TOPIC);

        return payout;
    }
}
