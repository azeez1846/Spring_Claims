package com.insurtech.claims.policy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.insurtech.claims.common.enums.CoverageType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "policy_coverages")
public class PolicyCoverageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    @JsonIgnore
    private PolicyEntity policy;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type", nullable = false)
    private CoverageType coverageType;

    @Column(name = "coverage_limit", nullable = false)
    private BigDecimal coverageLimit;

    @Column(name = "deductible_amount", nullable = false)
    private BigDecimal deductibleAmount;

    @Column(name = "active", nullable = false)
    private boolean active;

    public PolicyCoverageEntity() {}

    public PolicyCoverageEntity(Long id, PolicyEntity policy, CoverageType coverageType, BigDecimal coverageLimit, BigDecimal deductibleAmount, boolean active) {
        this.id = id;
        this.policy = policy;
        this.coverageType = coverageType;
        this.coverageLimit = coverageLimit;
        this.deductibleAmount = deductibleAmount;
        this.active = active;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PolicyEntity getPolicy() { return policy; }
    public void setPolicy(PolicyEntity policy) { this.policy = policy; }

    public CoverageType getCoverageType() { return coverageType; }
    public void setCoverageType(CoverageType coverageType) { this.coverageType = coverageType; }

    public BigDecimal getCoverageLimit() { return coverageLimit; }
    public void setCoverageLimit(BigDecimal coverageLimit) { this.coverageLimit = coverageLimit; }

    public BigDecimal getDeductibleAmount() { return deductibleAmount; }
    public void setDeductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class Builder {
        private Long id;
        private PolicyEntity policy;
        private CoverageType coverageType;
        private BigDecimal coverageLimit;
        private BigDecimal deductibleAmount;
        private boolean active;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder policy(PolicyEntity policy) { this.policy = policy; return this; }
        public Builder coverageType(CoverageType coverageType) { this.coverageType = coverageType; return this; }
        public Builder coverageLimit(BigDecimal coverageLimit) { this.coverageLimit = coverageLimit; return this; }
        public Builder deductibleAmount(BigDecimal deductibleAmount) { this.deductibleAmount = deductibleAmount; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public PolicyCoverageEntity build() {
            return new PolicyCoverageEntity(id, policy, coverageType, coverageLimit, deductibleAmount, active);
        }
    }
}
