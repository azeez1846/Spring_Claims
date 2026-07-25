package com.insurtech.claims.reserve.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_reserves")
public class ClaimReserveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reserve_id", nullable = false, unique = true)
    private String reserveId;

    @Column(name = "claim_number", nullable = false, unique = true)
    private String claimNumber;

    @Column(name = "initial_reserve_amount", nullable = false)
    private BigDecimal initialReserveAmount;

    @Column(name = "current_reserve_amount", nullable = false)
    private BigDecimal currentReserveAmount;

    @Column(name = "paid_amount", nullable = false)
    private BigDecimal paidAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ClaimReserveEntity() {}

    public ClaimReserveEntity(Long id, String reserveId, String claimNumber, BigDecimal initialReserveAmount, BigDecimal currentReserveAmount, BigDecimal paidAmount, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.reserveId = reserveId;
        this.claimNumber = claimNumber;
        this.initialReserveAmount = initialReserveAmount;
        this.currentReserveAmount = currentReserveAmount;
        this.paidAmount = paidAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReserveId() { return reserveId; }
    public void setReserveId(String reserveId) { this.reserveId = reserveId; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public BigDecimal getInitialReserveAmount() { return initialReserveAmount; }
    public void setInitialReserveAmount(BigDecimal initialReserveAmount) { this.initialReserveAmount = initialReserveAmount; }

    public BigDecimal getCurrentReserveAmount() { return currentReserveAmount; }
    public void setCurrentReserveAmount(BigDecimal currentReserveAmount) { this.currentReserveAmount = currentReserveAmount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class Builder {
        private Long id;
        private String reserveId;
        private String claimNumber;
        private BigDecimal initialReserveAmount;
        private BigDecimal currentReserveAmount;
        private BigDecimal paidAmount;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder reserveId(String reserveId) { this.reserveId = reserveId; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder initialReserveAmount(BigDecimal initialReserveAmount) { this.initialReserveAmount = initialReserveAmount; return this; }
        public Builder currentReserveAmount(BigDecimal currentReserveAmount) { this.currentReserveAmount = currentReserveAmount; return this; }
        public Builder paidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ClaimReserveEntity build() {
            return new ClaimReserveEntity(id, reserveId, claimNumber, initialReserveAmount, currentReserveAmount, paidAmount, status, createdAt, updatedAt);
        }
    }
}
