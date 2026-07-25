package com.insurtech.claims.fnol.rules;

import com.insurtech.claims.common.enums.LineOfBusiness;
import com.insurtech.claims.common.enums.LossSeverity;

import java.math.BigDecimal;

public class ClaimEvaluationFact {
    private BigDecimal estimatedLossAmount;
    private boolean injuriesReported;
    private LineOfBusiness lineOfBusiness;

    private LossSeverity calculatedSeverity;
    private String priority;
    private BigDecimal reserveMultiplier;

    public ClaimEvaluationFact() {}

    public ClaimEvaluationFact(BigDecimal estimatedLossAmount, boolean injuriesReported, LineOfBusiness lineOfBusiness, LossSeverity calculatedSeverity, String priority, BigDecimal reserveMultiplier) {
        this.estimatedLossAmount = estimatedLossAmount;
        this.injuriesReported = injuriesReported;
        this.lineOfBusiness = lineOfBusiness;
        this.calculatedSeverity = calculatedSeverity;
        this.priority = priority;
        this.reserveMultiplier = reserveMultiplier;
    }

    public static Builder builder() { return new Builder(); }

    public BigDecimal getEstimatedLossAmount() { return estimatedLossAmount; }
    public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) { this.estimatedLossAmount = estimatedLossAmount; }

    public boolean isInjuriesReported() { return injuriesReported; }
    public void setInjuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; }

    public LineOfBusiness getLineOfBusiness() { return lineOfBusiness; }
    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; }

    public LossSeverity getCalculatedSeverity() { return calculatedSeverity; }
    public void setCalculatedSeverity(LossSeverity calculatedSeverity) { this.calculatedSeverity = calculatedSeverity; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public BigDecimal getReserveMultiplier() { return reserveMultiplier; }
    public void setReserveMultiplier(BigDecimal reserveMultiplier) { this.reserveMultiplier = reserveMultiplier; }

    public static class Builder {
        private BigDecimal estimatedLossAmount;
        private boolean injuriesReported;
        private LineOfBusiness lineOfBusiness;
        private LossSeverity calculatedSeverity;
        private String priority;
        private BigDecimal reserveMultiplier;

        public Builder estimatedLossAmount(BigDecimal estimatedLossAmount) { this.estimatedLossAmount = estimatedLossAmount; return this; }
        public Builder injuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; return this; }
        public Builder lineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; return this; }
        public Builder calculatedSeverity(LossSeverity calculatedSeverity) { this.calculatedSeverity = calculatedSeverity; return this; }
        public Builder priority(String priority) { this.priority = priority; return this; }
        public Builder reserveMultiplier(BigDecimal reserveMultiplier) { this.reserveMultiplier = reserveMultiplier; return this; }

        public ClaimEvaluationFact build() {
            return new ClaimEvaluationFact(estimatedLossAmount, injuriesReported, lineOfBusiness, calculatedSeverity, priority, reserveMultiplier);
        }
    }
}
