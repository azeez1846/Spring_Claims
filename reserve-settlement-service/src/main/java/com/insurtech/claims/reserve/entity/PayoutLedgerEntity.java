package com.insurtech.claims.reserve.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payout_ledger")
public class PayoutLedgerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payout_id", nullable = false, unique = true)
    private String payoutId;

    @Column(name = "claim_number", nullable = false)
    private String claimNumber;

    @Column(name = "payout_amount", nullable = false)
    private BigDecimal payoutAmount;

    @Column(name = "payee_name", nullable = false)
    private String payeeName;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    public PayoutLedgerEntity() {}

    public PayoutLedgerEntity(Long id, String payoutId, String claimNumber, BigDecimal payoutAmount, String payeeName, String paymentMethod, String status, LocalDateTime processedAt) {
        this.id = id;
        this.payoutId = payoutId;
        this.claimNumber = claimNumber;
        this.payoutAmount = payoutAmount;
        this.payeeName = payeeName;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.processedAt = processedAt;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public static class Builder {
        private Long id;
        private String payoutId;
        private String claimNumber;
        private BigDecimal payoutAmount;
        private String payeeName;
        private String paymentMethod;
        private String status;
        private LocalDateTime processedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder payoutId(String payoutId) { this.payoutId = payoutId; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder payoutAmount(BigDecimal payoutAmount) { this.payoutAmount = payoutAmount; return this; }
        public Builder payeeName(String payeeName) { this.payeeName = payeeName; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder processedAt(LocalDateTime processedAt) { this.processedAt = processedAt; return this; }

        public PayoutLedgerEntity build() {
            return new PayoutLedgerEntity(id, payoutId, claimNumber, payoutAmount, payeeName, paymentMethod, status, processedAt);
        }
    }
}
