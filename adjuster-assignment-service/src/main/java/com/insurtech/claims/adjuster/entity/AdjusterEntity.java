package com.insurtech.claims.adjuster.entity;

import com.insurtech.claims.common.enums.LineOfBusiness;
import jakarta.persistence.*;

@Entity
@Table(name = "adjusters")
public class AdjusterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adjuster_id", nullable = false, unique = true)
    private String adjusterId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_of_business", nullable = false)
    private LineOfBusiness lineOfBusiness;

    @Column(name = "max_claim_capacity", nullable = false)
    private Integer maxClaimCapacity;

    @Column(name = "current_active_claims", nullable = false)
    private Integer currentActiveClaims;

    @Column(name = "active", nullable = false)
    private boolean active;

    public AdjusterEntity() {}

    public AdjusterEntity(Long id, String adjusterId, String name, String email, LineOfBusiness lineOfBusiness, Integer maxClaimCapacity, Integer currentActiveClaims, boolean active) {
        this.id = id;
        this.adjusterId = adjusterId;
        this.name = name;
        this.email = email;
        this.lineOfBusiness = lineOfBusiness;
        this.maxClaimCapacity = maxClaimCapacity;
        this.currentActiveClaims = currentActiveClaims;
        this.active = active;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdjusterId() { return adjusterId; }
    public void setAdjusterId(String adjusterId) { this.adjusterId = adjusterId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LineOfBusiness getLineOfBusiness() { return lineOfBusiness; }
    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; }

    public Integer getMaxClaimCapacity() { return maxClaimCapacity; }
    public void setMaxClaimCapacity(Integer maxClaimCapacity) { this.maxClaimCapacity = maxClaimCapacity; }

    public Integer getCurrentActiveClaims() { return currentActiveClaims; }
    public void setCurrentActiveClaims(Integer currentActiveClaims) { this.currentActiveClaims = currentActiveClaims; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class Builder {
        private Long id;
        private String adjusterId;
        private String name;
        private String email;
        private LineOfBusiness lineOfBusiness;
        private Integer maxClaimCapacity;
        private Integer currentActiveClaims;
        private boolean active;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder adjusterId(String adjusterId) { this.adjusterId = adjusterId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder lineOfBusiness(LineOfBusiness lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; return this; }
        public Builder maxClaimCapacity(Integer maxClaimCapacity) { this.maxClaimCapacity = maxClaimCapacity; return this; }
        public Builder currentActiveClaims(Integer currentActiveClaims) { this.currentActiveClaims = currentActiveClaims; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public AdjusterEntity build() {
            return new AdjusterEntity(id, adjusterId, name, email, lineOfBusiness, maxClaimCapacity, currentActiveClaims, active);
        }
    }
}
