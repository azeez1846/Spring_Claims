package com.insurtech.claims.reserve.controller;

import com.insurtech.claims.reserve.dto.PayoutRequest;
import com.insurtech.claims.reserve.entity.ClaimReserveEntity;
import com.insurtech.claims.reserve.entity.PayoutLedgerEntity;
import com.insurtech.claims.reserve.repository.ClaimReserveRepository;
import com.insurtech.claims.reserve.repository.PayoutLedgerRepository;
import com.insurtech.claims.reserve.service.ReserveSettlementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reserves")
public class ReserveController {

    private final ClaimReserveRepository reserveRepository;
    private final PayoutLedgerRepository payoutLedgerRepository;
    private final ReserveSettlementService reserveSettlementService;

    public ReserveController(ClaimReserveRepository reserveRepository,
                             PayoutLedgerRepository payoutLedgerRepository,
                             ReserveSettlementService reserveSettlementService) {
        this.reserveRepository = reserveRepository;
        this.payoutLedgerRepository = payoutLedgerRepository;
        this.reserveSettlementService = reserveSettlementService;
    }

    @GetMapping("/{claimNumber}")
    public ResponseEntity<ClaimReserveEntity> getReserve(@PathVariable("claimNumber") String claimNumber) {
        return reserveRepository.findByClaimNumber(claimNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{claimNumber}/payouts")
    public ResponseEntity<List<PayoutLedgerEntity>> getPayouts(@PathVariable("claimNumber") String claimNumber) {
        return ResponseEntity.ok(payoutLedgerRepository.findByClaimNumber(claimNumber));
    }

    @PostMapping("/payouts")
    public ResponseEntity<PayoutLedgerEntity> executePayout(@Valid @RequestBody PayoutRequest request) {
        PayoutLedgerEntity payout = reserveSettlementService.processPayout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payout);
    }
}
