package com.insurtech.claims.adjuster.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_assignments")
public class ClaimAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_number", nullable = false, unique = true)
    private String claimNumber;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "adjuster_id", nullable = false)
    private String adjusterId;

    @Column(name = "assignment_reason")
    private String assignmentReason;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public ClaimAssignmentEntity() {}

    public ClaimAssignmentEntity(Long id, String claimNumber, String policyNumber, String adjusterId, String assignmentReason, LocalDateTime assignedAt) {
        this.id = id;
        this.claimNumber = claimNumber;
        this.policyNumber = policyNumber;
        this.adjusterId = adjusterId;
        this.assignmentReason = assignmentReason;
        this.assignedAt = assignedAt;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getAdjusterId() { return adjusterId; }
    public void setAdjusterId(String adjusterId) { this.adjusterId = adjusterId; }

    public String getAssignmentReason() { return assignmentReason; }
    public void setAssignmentReason(String assignmentReason) { this.assignmentReason = assignmentReason; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public static class Builder {
        private Long id;
        private String claimNumber;
        private String policyNumber;
        private String adjusterId;
        private String assignmentReason;
        private LocalDateTime assignedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder claimNumber(String claimNumber) { this.claimNumber = claimNumber; return this; }
        public Builder policyNumber(String policyNumber) { this.policyNumber = policyNumber; return this; }
        public Builder adjusterId(String adjusterId) { this.adjusterId = adjusterId; return this; }
        public Builder assignmentReason(String assignmentReason) { this.assignmentReason = assignmentReason; return this; }
        public Builder assignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; return this; }

        public ClaimAssignmentEntity build() {
            return new ClaimAssignmentEntity(id, claimNumber, policyNumber, adjusterId, assignmentReason, assignedAt);
        }
    }
}
