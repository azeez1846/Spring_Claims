package com.insurtech.claims.common.dto;

import com.insurtech.claims.common.enums.CoverageType;

import java.math.BigDecimal;

public class PolicyVerificationResponse {
    private String policyNumber;
    private boolean valid;
    private String policyHolderName;
    private String policyHolderEmail;
    private CoverageType checkedCoverage;
    private BigDecimal coverageLimit;
    private BigDecimal deductibleAmount;
    private String rejectionReason;

    public PolicyVerificationResponse() {}

    public PolicyVerificationResponse(String policyNumber, boolean valid, String policyHolderName, String policyHolderEmail, CoverageType checkedCoverage, BigDecimal coverageLimit, BigDecimal deductibleAmount, String rejectionReason) {
        this.policyNumber = policyNumber;
        this.valid = valid;
        this.policyHolderName = policyHolderName;
        this.policyHolderEmail = policyHolderEmail;
        this.checkedCoverage = checkedCoverage;
        this.coverageLimit = coverageLimit;
        this.deductibleAmount = deductibleAmount;
        this.rejectionReason = rejectionReason;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getPolicyHolderName() { return policyHolderName; }
    public void setPolicyHolderName(String policyHolderName) { this.policyHolderName = policyHolderName; }

    public String getPolicyHolderEmail() { return policyHolderEmail; }
    public void setPolicyHolderEmail(String policyHolderEmail) { this.policyHolderEmail = policyHolderEmail; }

    public CoverageType getCheckedCoverage() { return checkedCoverage; }
    public void setCheckedCoverage(CoverageType checkedCoverage) { this.checkedCoverage = checkedCoverage; }

    public BigDecimal getCoverageLimit() { return coverageLimit; }
    public void setCoverageLimit(BigDecimal coverageLimit) { this.coverageLimit = coverageLimit; }

    public BigDecimal getDeductibleAmount() { return deductibleAmount; }
    public void setDeductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public static class Builder {
        private String policyNumber;
        private boolean valid;
        private String policyHolderName;
        private String policyHolderEmail;
        private CoverageType checkedCoverage;
        private BigDecimal coverageLimit;
        private BigDecimal deductibleAmount;
        private String rejectionReason;

        public Builder policyNumber(String policyNumber) { this.policyNumber = policyNumber; return this; }
        public Builder valid(boolean valid) { this.valid = valid; return this; }
        public Builder policyHolderName(String policyHolderName) { this.policyHolderName = policyHolderName; return this; }
        public Builder policyHolderEmail(String policyHolderEmail) { this.policyHolderEmail = policyHolderEmail; return this; }
        public Builder checkedCoverage(CoverageType checkedCoverage) { this.checkedCoverage = checkedCoverage; return this; }
        public Builder coverageLimit(BigDecimal coverageLimit) { this.coverageLimit = coverageLimit; return this; }
        public Builder deductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; return this; }
        public Builder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }

        public PolicyVerificationResponse build() {
            return new PolicyVerificationResponse(policyNumber, valid, policyHolderName, policyHolderEmail, checkedCoverage, coverageLimit, deductibleAmount, rejectionReason);
        }
    }
}
