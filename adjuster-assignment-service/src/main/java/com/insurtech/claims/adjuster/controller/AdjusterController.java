package com.insurtech.claims.adjuster.controller;

import com.insurtech.claims.adjuster.entity.AdjusterEntity;
import com.insurtech.claims.adjuster.entity.ClaimAssignmentEntity;
import com.insurtech.claims.adjuster.repository.AdjusterRepository;
import com.insurtech.claims.adjuster.repository.ClaimAssignmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adjusters")
public class AdjusterController {

    private final AdjusterRepository adjusterRepository;
    private final ClaimAssignmentRepository claimAssignmentRepository;

    public AdjusterController(AdjusterRepository adjusterRepository, ClaimAssignmentRepository claimAssignmentRepository) {
        this.adjusterRepository = adjusterRepository;
        this.claimAssignmentRepository = claimAssignmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<AdjusterEntity>> getAllAdjusters() {
        return ResponseEntity.ok(adjusterRepository.findAll());
    }

    @GetMapping("/assignments/{claimNumber}")
    public ResponseEntity<ClaimAssignmentEntity> getAssignmentForClaim(@PathVariable("claimNumber") String claimNumber) {
        return claimAssignmentRepository.findByClaimNumber(claimNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
