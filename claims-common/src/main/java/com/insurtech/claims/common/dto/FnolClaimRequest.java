package com.insurtech.claims.common.dto;

import com.insurtech.claims.common.enums.CoverageType;
import com.insurtech.claims.common.enums.LineOfBusiness;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FnolClaimRequest {
    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotNull(message = "Line of business is required")
    private LineOfBusiness lineOfBusiness;

    @NotNull(message = "Coverage type is required")
    private CoverageType coverageType;

    @NotNull(message = "Loss date time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm[:ss]")
    private LocalDateTime lossDateTime;

    @NotBlank(message = "Loss location is required")
    private String lossLocation;

    @NotBlank(message = "Loss description is required")
    private String lossDescription;

    @Positive(message = "Estimated loss amount must be positive")
    private BigDecimal estimatedLossAmount;

    private boolean injuriesReported;
    private boolean policeReportFiled;
    private String vehicleVin;

    public FnolClaimRequest() {}

    public FnolClaimRequest(String policyNumber, LineOfBusiness lineOfBusiness, CoverageType coverageType, LocalDateTime lossDateTime, String lossLocation, String lossDescription, BigDecimal estimatedLossAmount, boolean injuriesReported, boolean policeReportFiled, String vehicleVin) {
        this.policyNumber = policyNumber;
        this.lineOfBusiness = lineOfBusiness;
        this.coverageType = coverageType;
        this.lossDateTime = lossDateTime;
        this.lossLocation = lossLocation;
        this.lossDescription = lossDescription;
        this.estimatedLossAmount = estimatedLossAmount;
        this.injuriesReported = injuriesReported;
        this.policeReportFiled = policeReportFiled;
        this.vehicleVin = vehicleVin;
    }

    public static Builder builder() { return new Builder(); }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public LineOfBusiness getLineOfBusiness() { return lineOfBusiness; }
    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; }

    public CoverageType getCoverageType() { return coverageType; }
    public void setCoverageType(CoverageType coverageType) { this.coverageType = coverageType; }

    public LocalDateTime getLossDateTime() { return lossDateTime; }
    public void setLossDateTime(LocalDateTime lossDateTime) { this.lossDateTime = lossDateTime; }

    public String getLossLocation() { return lossLocation; }
    public void setLossLocation(String lossLocation) { this.lossLocation = lossLocation; }

    public String getLossDescription() { return lossDescription; }
    public void setLossDescription(String lossDescription) { this.lossDescription = lossDescription; }

    public BigDecimal getEstimatedLossAmount() { return estimatedLossAmount; }
    public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) { this.estimatedLossAmount = estimatedLossAmount; }

    public boolean isInjuriesReported() { return injuriesReported; }
    public void setInjuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; }

    public boolean isPoliceReportFiled() { return policeReportFiled; }
    public void setPoliceReportFiled(boolean policeReportFiled) { this.policeReportFiled = policeReportFiled; }

    public String getVehicleVin() { return vehicleVin; }
    public void setVehicleVin(String vehicleVin) { this.vehicleVin = vehicleVin; }

    public static class Builder {
        private String policyNumber;
        private LineOfBusiness lineOfBusiness;
        private CoverageType coverageType;
        private LocalDateTime lossDateTime;
        private String lossLocation;
        private String lossDescription;
        private BigDecimal estimatedLossAmount;
        private boolean injuriesReported;
        private boolean policeReportFiled;
        private String vehicleVin;

        public Builder policyNumber(String policyNumber) { this.policyNumber = policyNumber; return this; }
        public Builder lineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; return this; }
        public Builder coverageType(CoverageType coverageType) { this.coverageType = coverageType; return this; }
        public Builder lossDateTime(LocalDateTime lossDateTime) { this.lossDateTime = lossDateTime; return this; }
        public Builder lossLocation(String lossLocation) { this.lossLocation = lossLocation; return this; }
        public Builder lossDescription(String lossDescription) { this.lossDescription = lossDescription; return this; }
        public Builder estimatedLossAmount(BigDecimal estimatedLossAmount) { this.estimatedLossAmount = estimatedLossAmount; return this; }
        public Builder injuriesReported(boolean injuriesReported) { this.injuriesReported = injuriesReported; return this; }
        public Builder policeReportFiled(boolean policeReportFiled) { this.policeReportFiled = policeReportFiled; return this; }
        public Builder vehicleVin(String vehicleVin) { this.vehicleVin = vehicleVin; return this; }

        public FnolClaimRequest build() {
            return new FnolClaimRequest(policyNumber, lineOfBusiness, coverageType, lossDateTime, lossLocation, lossDescription, estimatedLossAmount, injuriesReported, policeReportFiled, vehicleVin);
        }
    }
}
