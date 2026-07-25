package com.insurtech.claims.adjuster.repository;

import com.insurtech.claims.adjuster.entity.AdjusterEntity;
import com.insurtech.claims.common.enums.LineOfBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdjusterRepository extends JpaRepository<AdjusterEntity, Long> {

    @Query("SELECT a FROM AdjusterEntity a WHERE a.lineOfBusiness = :lob AND a.active = true AND a.currentActiveClaims < a.maxClaimCapacity ORDER BY a.currentActiveClaims ASC")
    List<AdjusterEntity> findAvailableAdjustersByLob(@Param("lob") LineOfBusiness lob);
}
