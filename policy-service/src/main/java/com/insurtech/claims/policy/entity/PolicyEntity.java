package com.insurtech.claims.policy.entity;

import com.insurtech.claims.common.enums.LineOfBusiness;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @Column(name = "policy_holder_name", nullable = false)
    private String policyHolderName;

    @Column(name = "policy_holder_email", nullable = false)
    private String policyHolderEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_of_business", nullable = false)
    private LineOfBusiness lineOfBusiness;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "effective_date", nullable = false)
    private LocalDateTime effectiveDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PolicyCoverageEntity> coverages = new ArrayList<>();

    public PolicyEntity() {}

    public PolicyEntity(Long id, String policyNumber, String policyHolderName, String policyHolderEmail, LineOfBusiness lineOfBusiness, String status, LocalDateTime effectiveDate, LocalDateTime expirationDate, List<PolicyCoverageEntity> coverages) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.policyHolderName = policyHolderName;
        this.policyHolderEmail = policyHolderEmail;
        this.lineOfBusiness = lineOfBusiness;
        this.status = status;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        if (coverages != null) {
            this.coverages = coverages;
        }
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getPolicyHolderName() { return policyHolderName; }
    public void setPolicyHolderName(String policyHolderName) { this.policyHolderName = policyHolderName; }

    public String getPolicyHolderEmail() { return policyHolderEmail; }
    public void setPolicyHolderEmail(String policyHolderEmail) { this.policyHolderEmail = policyHolderEmail; }

    public LineOfBusiness getLineOfBusiness() { return lineOfBusiness; }
    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDateTime effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    public List<PolicyCoverageEntity> getCoverages() { return coverages; }
    public void setCoverages(List<PolicyCoverageEntity> coverages) { this.coverages = coverages; }

    public static class Builder {
        private Long id;
        private String policyNumber;
        private String policyHolderName;
        private String policyHolderEmail;
        private LineOfBusiness lineOfBusiness;
        private String status;
        private LocalDateTime effectiveDate;
        private LocalDateTime expirationDate;
        private List<PolicyCoverageEntity> coverages = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder policyNumber(String policyNumber) { this.policyNumber = policyNumber; return this; }
        public Builder policyHolderName(String policyHolderName) { this.policyHolderName = policyHolderName; return this; }
        public Builder policyHolderEmail(String policyHolderEmail) { this.policyHolderEmail = policyHolderEmail; return this; }
        public Builder lineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder effectiveDate(LocalDateTime effectiveDate) { this.effectiveDate = effectiveDate; return this; }
        public Builder expirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; return this; }
        public Builder coverages(List<PolicyCoverageEntity> coverages) { this.coverages = coverages; return this; }

        public PolicyEntity build() {
            return new PolicyEntity(id, policyNumber, policyHolderName, policyHolderEmail, lineOfBusiness, status, effectiveDate, expirationDate, coverages);
        }
    }
}
