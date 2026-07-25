package com.insurtech.claims.reserve.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PayoutRequest {

    @NotBlank(message = "Claim number is required")
    private String claimNumber;

    @NotNull(message = "Payout amount is required")
    @Positive(message = "Payout amount must be positive")
    private BigDecimal payoutAmount;

    @NotBlank(message = "Payee name is required")
    private String payeeName;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    public PayoutRequest() {}

    public PayoutRequest(String claimNumber, BigDecimal payoutAmount, String payeeName, String paymentMethod) {
        this.claimNumber = claimNumber;
        this.payoutAmount = payoutAmount;
        this.payeeName = payeeName;
        this.paymentMethod = paymentMethod;
    }

    public static Builder builder() { return new Builder(); }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public BigDecimal getPayoutAmount() { return payoutAmount; }
    public void setPayoutAmount(BigDecimal payoutAmount) { this.payoutAmount = payoutAmount; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public static class Builder {
        private String claimNumber;
        private BigDecimal payoutAmount;
        private String payeeName;
        private String paymentMethod;

        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder payoutAmount(BigDecimal payoutAmount) { this.payoutAmount = payoutAmount; return this; }
        public Builder payeeName(String payeeName) { this.payeeName = payeeName; return this; }
        public Builder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }

        public PayoutRequest build() {
            return new PayoutRequest(claimNumber, payoutAmount, payeeName, paymentMethod);
        }
    }
}
