package com.insurtech.claims.fnol.rules;

import com.insurtech.claims.common.enums.LineOfBusiness;
import com.insurtech.claims.common.enums.LossSeverity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DroolsRulesTest {

    private KieContainer kieContainer;

    @BeforeEach
    void setUp() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("rules/fnol-routing-rules.drl"));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieModule kieModule = kieBuilder.getKieModule();
        kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Test
    void testCatastrophicLossRule_BodilyInjury() {
        ClaimEvaluationFact fact = ClaimEvaluationFact.builder()
                .estimatedLossAmount(new BigDecimal("15000.00"))
                .injuriesReported(true)
                .lineOfBusiness(LineOfBusiness.AUTO)
                .build();

        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(fact);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        assertEquals(LossSeverity.CAT, fact.getCalculatedSeverity());
        assertEquals("URGENT", fact.getPriority());
        assertEquals(new BigDecimal("1.25"), fact.getReserveMultiplier());
    }

    @Test
    void testLowLossRule_FenderBender() {
        ClaimEvaluationFact fact = ClaimEvaluationFact.builder()
                .estimatedLossAmount(new BigDecimal("2500.00"))
                .injuriesReported(false)
                .lineOfBusiness(LineOfBusiness.AUTO)
                .build();

        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(fact);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        assertEquals(LossSeverity.LOW, fact.getCalculatedSeverity());
        assertEquals("LOW", fact.getPriority());
        assertEquals(new BigDecimal("1.00"), fact.getReserveMultiplier());
    }
}
