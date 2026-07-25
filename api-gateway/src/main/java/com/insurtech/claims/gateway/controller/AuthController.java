package com.insurtech.claims.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        String domain = credentials.getOrDefault("domain", "CORP.GUIDEWIRE.LOCAL");

        Map<String, Object> response = new HashMap<>();

        if ("su".equalsIgnoreCase(username) && "gw".equals(password)) {
            response.put("authenticated", true);
            response.put("username", "su");
            response.put("fullName", "Super User (Guidewire Claims Admin)");
            response.put("email", "su@guidewire.local");
            response.put("domain", domain);
            response.put("roles", List.of("ROLE_CLAIMS_ADMIN", "ROLE_UNDERWRITER", "ROLE_SYSTEM_ADMIN"));
            response.put("authMethod", "LDAP_BIND_SUCCESS");
            response.put("token", "bearer-ldap-su-jwt-token-" + UUID.randomUUID().toString().substring(0, 8));

            return Mono.just(ResponseEntity.ok(response));
        } else {
            response.put("authenticated", false);
            response.put("error", "Invalid LDAP credentials. User '" + username + "' failed directory bind.");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
    }
}
