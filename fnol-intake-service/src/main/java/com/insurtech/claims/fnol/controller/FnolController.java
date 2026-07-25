package com.insurtech.claims.fnol.controller;

import com.insurtech.claims.common.dto.FnolClaimRequest;
import com.insurtech.claims.common.events.ClaimSubmittedEvent;
import com.insurtech.claims.fnol.service.FnolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fnol")
public class FnolController {

    private final FnolService fnolService;

    public FnolController(FnolService fnolService) {
        this.fnolService = fnolService;
    }

    @PostMapping("/claims")
    public ResponseEntity<ClaimSubmittedEvent> submitFnolClaim(@Valid @RequestBody FnolClaimRequest request) {
        ClaimSubmittedEvent event = fnolService.processFnolClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
}
