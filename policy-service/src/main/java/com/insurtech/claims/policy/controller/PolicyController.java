package com.insurtech.claims.policy.controller;

import com.insurtech.claims.common.dto.PolicyVerificationRequest;
import com.insurtech.claims.common.dto.PolicyVerificationResponse;
import com.insurtech.claims.policy.entity.PolicyEntity;
import com.insurtech.claims.policy.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<PolicyEntity> getPolicy(@PathVariable("policyNumber") String policyNumber) {
        return policyService.getPolicyByNumber(policyNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/verify")
    public ResponseEntity<PolicyVerificationResponse> verifyPolicy(@Valid @RequestBody PolicyVerificationRequest request) {
        PolicyVerificationResponse response = policyService.verifyPolicy(request);
        return ResponseEntity.ok(response);
    }
}
