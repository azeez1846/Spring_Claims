package com.insurtech.claims.common.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReserveCreatedEvent {
    private String eventId;
    private String reserveId;
    private String claimNumber;
    private BigDecimal initialReserveAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal maxCoverageLimit;
    private String status;
    private LocalDateTime timestamp;

    public ReserveCreatedEvent() {}

    public ReserveCreatedEvent(String eventId, String reserveId, String claimNumber, BigDecimal initialReserveAmount, BigDecimal deductibleAmount, BigDecimal maxCoverageLimit, String status, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.reserveId = reserveId;
        this.claimNumber = claimNumber;
        this.initialReserveAmount = initialReserveAmount;
        this.deductibleAmount = deductibleAmount;
        this.maxCoverageLimit = maxCoverageLimit;
        this.status = status;
        this.timestamp = timestamp;
    }

    public static Builder builder() { return new Builder(); }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getReserveId() { return reserveId; }
    public void setReserveId(String reserveId) { this.reserveId = reserveId; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public BigDecimal getInitialReserveAmount() { return initialReserveAmount; }
    public void setInitialReserveAmount(BigDecimal initialReserveAmount) { this.initialReserveAmount = initialReserveAmount; }

    public BigDecimal getDeductibleAmount() { return deductibleAmount; }
    public void setDeductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; }

    public BigDecimal getMaxCoverageLimit() { return maxCoverageLimit; }
    public void setMaxCoverageLimit(BigDecimal maxCoverageLimit) { this.maxCoverageLimit = maxCoverageLimit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class Builder {
        private String eventId;
        private String reserveId;
        private String claimNumber;
        private BigDecimal initialReserveAmount;
        private BigDecimal deductibleAmount;
        private BigDecimal maxCoverageLimit;
        private String status;
        private LocalDateTime timestamp;

        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder reserveId(String reserveId) { this.reserveId = reserveId; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder initialReserveAmount(BigDecimal initialReserveAmount) { this.initialReserveAmount = initialReserveAmount; return this; }
        public Builder deductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; return this; }
        public Builder maxCoverageLimit(BigDecimal maxCoverageLimit) { this.maxCoverageLimit = maxCoverageLimit; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ReserveCreatedEvent build() {
            return new ReserveCreatedEvent(eventId, reserveId, claimNumber, initialReserveAmount, deductibleAmount, maxCoverageLimit, status, timestamp);
        }
    }
}
