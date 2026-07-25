package com.insurtech.claims.common.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UnderwritingAssignedEvent {
    private String eventId;
    private String claimNumber;
    private String policyNumber;
    private String adjusterId;
    private String adjusterName;
    private String adjusterEmail;
    private String assignmentReason;
    private BigDecimal initialReserve;
    private LocalDateTime timestamp;

    public UnderwritingAssignedEvent() {}

    public UnderwritingAssignedEvent(String eventId, String claimNumber, String policyNumber, String adjusterId, String adjusterName, String adjusterEmail, String assignmentReason, BigDecimal initialReserve, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.claimNumber = claimNumber;
        this.policyNumber = policyNumber;
        this.adjusterId = adjusterId;
        this.adjusterName = adjusterName;
        this.adjusterEmail = adjusterEmail;
        this.assignmentReason = assignmentReason;
        this.initialReserve = initialReserve;
        this.timestamp = timestamp;
    }

    public static Builder builder() { return new Builder(); }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getAdjusterId() { return adjusterId; }
    public void setAdjusterId(String adjusterId) { this.adjusterId = adjusterId; }

    public String getAdjusterName() { return adjusterName; }
    public void setAdjusterName(String adjusterName) { this.adjusterName = adjusterName; }

    public String getAdjusterEmail() { return adjusterEmail; }
    public void setAdjusterEmail(String adjusterEmail) { this.adjusterEmail = adjusterEmail; }

    public String getAssignmentReason() { return assignmentReason; }
    public void setAssignmentReason(String assignmentReason) { this.assignmentReason = assignmentReason; }

    public BigDecimal getInitialReserve() { return initialReserve; }
    public void setInitialReserve(BigDecimal initialReserve) { this.initialReserve = initialReserve; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class Builder {
        private String eventId;
        private String claimNumber;
        private String policyNumber;
        private String adjusterId;
        private String adjusterName;
        private String adjusterEmail;
        private String assignmentReason;
        private BigDecimal initialReserve;
        private LocalDateTime timestamp;

        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder policyNumber(String policyNumber) { this.policyNumber = policyNumber; return this; }
        public Builder adjusterId(String adjusterId) { this.adjusterId = adjusterId; return this; }
        public Builder adjusterName(String adjusterName) { this.adjusterName = adjusterName; return this; }
        public Builder adjusterEmail(String adjusterEmail) { this.adjusterEmail = adjusterEmail; return this; }
        public Builder assignmentReason(String assignmentReason) { this.assignmentReason = assignmentReason; return this; }
        public Builder initialReserve(BigDecimal initialReserve) { this.initialReserve = initialReserve; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public UnderwritingAssignedEvent build() {
            return new UnderwritingAssignedEvent(eventId, claimNumber, policyNumber, adjusterId, adjusterName, adjusterEmail, assignmentReason, initialReserve, timestamp);
        }
    }
}
