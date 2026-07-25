package com.insurtech.claims.common.events;

import com.insurtech.claims.common.enums.CoverageType;
import com.insurtech.claims.common.enums.LineOfBusiness;
import com.insurtech.claims.common.enums.LossSeverity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClaimSubmittedEvent {
    private String eventId;
    private String claimNumber;
    private String policyNumber;
    private LineOfBusiness lineOfBusiness;
    private CoverageType coverageType;
    private LossSeverity lossSeverity;
    private BigDecimal estimatedLossAmount;
    private BigDecimal calculatedReserve;
    private BigDecimal deductibleAmount;
    private String lossLocation;
    private String lossDescription;
    private boolean injuriesReported;
    private boolean policeReportFiled;
    private String priority;
    private LocalDateTime timestamp;

    public ClaimSubmittedEvent() {}

    public ClaimSubmittedEvent(String eventId, String claimNumber, String policyNumber, LineOfBusiness lineOfBusiness, CoverageType coverageType, LossSeverity lossSeverity, BigDecimal estimatedLossAmount, BigDecimal calculatedReserve, BigDecimal deductibleAmount, String lossLocation, String lossDescription, boolean injuriesReported, boolean policeReportFiled, String priority, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.claimNumber = claimNumber;
        this.policyNumber = policyNumber;
        this.lineOfBusiness = lineOfBusiness;
        this.coverageType = coverageType;
        this.lossSeverity = lossSeverity;
        this.estimatedLossAmount = estimatedLossAmount;
        this.calculatedReserve = calculatedReserve;
        this.deductibleAmount = deductibleAmount;
        this.lossLocation = lossLocation;
        this.lossDescription = lossDescription;
        this.injuriesReported = injuriesReported;
        this.policeReportFiled = policeReportFiled;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    public static Builder builder() { return new Builder(); }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public LineOfBusiness getLineOfBusiness() { return lineOfBusiness; }
    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; }

    public CoverageType getCoverageType() { return coverageType; }
    public void setCoverageType(CoverageType coverageType) { this.coverageType = coverageType; }

    public LossSeverity getLossSeverity() { return lossSeverity; }
    public void setLossSeverity(LossSeverity lossSeverity) { this.lossSeverity = lossSeverity; }

    public BigDecimal getEstimatedLossAmount() { return estimatedLossAmount; }
    public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) { this.estimatedLossAmount = estimatedLossAmount; }

    public BigDecimal getCalculatedReserve() { return calculatedReserve; }
    public void setCalculatedReserve(BigDecimal calculatedReserve) { this.calculatedReserve = calculatedReserve; }

    public BigDecimal getDeductibleAmount() { return deductibleAmount; }
    public void setDeductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; }

    public String getLossLocation() { return lossLocation; }
    public void setLossLocation(String lossLocation) { this.lossLocation = lossLocation; }

    public String getLossDescription() { return lossDescription; }
    public void setLossDescription(String lossDescription) { this.lossDescription = lossDescription; }

    public boolean isInjuriesReported() { return injuriesReported; }
    public void setInjuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; }

    public boolean isPoliceReportFiled() { return policeReportFiled; }
    public void setPoliceReportFiled(boolean policeReportFiled) { this.policeReportFiled = policeReportFiled; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class Builder {
        private String eventId;
        private String claimNumber;
        private String policyNumber;
        private LineOfBusiness lineOfBusiness;
        private CoverageType coverageType;
        private LossSeverity lossSeverity;
        private BigDecimal estimatedLossAmount;
        private BigDecimal calculatedReserve;
        private BigDecimal deductibleAmount;
        private String lossLocation;
        private String lossDescription;
        private boolean injuriesReported;
        private boolean policeReportFiled;
        private String priority;
        private LocalDateTime timestamp;

        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder policyNumber(String policyNumber) { this.policyNumber = policyNumber; return this; }
        public Builder lineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; return this; }
        public Builder coverageType(CoverageType coverageType) { this.coverageType = coverageType; return this; }
        public Builder lossSeverity(LossSeverity lossSeverity) { this.lossSeverity = lossSeverity; return this; }
        public Builder estimatedLossAmount(BigDecimal estimatedLossAmount) { this.estimatedLossAmount = estimatedLossAmount; return this; }
        public Builder calculatedReserve(BigDecimal calculatedReserve) { this.calculatedReserve = calculatedReserve; return this; }
        public Builder deductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; return this; }
        public Builder lossLocation(String lossLocation) { this.lossLocation = lossLocation; return this; }
        public Builder lossDescription(String lossDescription) { this.lossDescription = lossDescription; return this; }
        public Builder injuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; return this; }
        public Builder policeReportFiled(boolean policeReportFiled) { this.policeReportFiled = policeReportFiled; return this; }
        public Builder priority(String priority) { this.priority = priority; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ClaimSubmittedEvent build() {
            return new ClaimSubmittedEvent(eventId, claimNumber, policyNumber, lineOfBusiness, coverageType, lossSeverity, estimatedLossAmount, calculatedReserve, deductibleAmount, lossLocation, lossDescription, injuriesReported, policeReportFiled, priority, timestamp);
        }
    }
}
