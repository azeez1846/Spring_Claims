package com.insurtech.claims.common.dto;

import com.insurtech.claims.common.enums.CoverageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PolicyVerificationRequest {
    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotNull(message = "Loss date time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm[:ss]")
    private LocalDateTime lossDateTime;

    @NotNull(message = "Coverage type is required")
    private CoverageType coverageType;

    public PolicyVerificationRequest() {}

    public PolicyVerificationRequest(String policyNumber, LocalDateTime lossDateTime, CoverageType coverageType) {
        this.policyNumber = policyNumber;
        this.lossDateTime = lossDateTime;
        this.coverageType = coverageType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public LocalDateTime getLossDateTime() { return lossDateTime; }
    public void setLossDateTime(LocalDateTime lossDateTime) { this.lossDateTime = lossDateTime; }

    public CoverageType getCoverageType() { return coverageType; }
    public void setCoverageType(CoverageType coverageType) { this.coverageType = coverageType; }

    public static class Builder {
        private String policyNumber;
        private LocalDateTime lossDateTime;
        private CoverageType coverageType;

        public Builder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public Builder lossDateTime(LocalDateTime lossDateTime) {
            this.lossDateTime = lossDateTime;
            return this;
        }

        public Builder coverageType(CoverageType coverageType) {
            this.coverageType = coverageType;
            return this;
        }

        public PolicyVerificationRequest build() {
            return new PolicyVerificationRequest(policyNumber, lossDateTime, coverageType);
        }
    }
}
