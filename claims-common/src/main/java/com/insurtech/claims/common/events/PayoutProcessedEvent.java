package com.insurtech.claims.common.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PayoutProcessedEvent {
    private String eventId;
    private String payoutId;
    private String claimNumber;
    private BigDecimal payoutAmount;
    private String payeeName;
    private String paymentMethod;
    private String status;
    private LocalDateTime timestamp;

    public PayoutProcessedEvent() {}

    public PayoutProcessedEvent(String eventId, String payoutId, String claimNumber, BigDecimal payoutAmount, String payeeName, String paymentMethod, String status, LocalDateTime timestamp) {
        this.eventId = eventId;
        this.payoutId = payoutId;
        this.claimNumber = claimNumber;
        this.payoutAmount = payoutAmount;
        this.payeeName = payeeName;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.timestamp = timestamp;
    }

    public static Builder builder() { return new Builder(); }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getPayoutId() { return payoutId; }
    public void setPayoutId(String payoutId) { this.payoutId = payoutId; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public BigDecimal getPayoutAmount() { return payoutAmount; }
    public void setPayoutAmount(BigDecimal payoutAmount) { this.payoutAmount = payoutAmount; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class Builder {
        private String eventId;
        private String payoutId;
        private String claimNumber;
        private BigDecimal payoutAmount;
        private String payeeName;
        private String paymentMethod;
        private String status;
        private LocalDateTime timestamp;

        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder payoutId(String payoutId) { this.payoutId = payoutId; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder payoutAmount(BigDecimal payoutAmount) { this.payoutAmount = payoutAmount; return this; }
        public Builder payeeName(String payeeName) { this.payeeName = payeeName; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public PayoutProcessedEvent build() {
            return new PayoutProcessedEvent(eventId, payoutId, claimNumber, payoutAmount, payeeName, paymentMethod, status, timestamp);
        }
    }
}
