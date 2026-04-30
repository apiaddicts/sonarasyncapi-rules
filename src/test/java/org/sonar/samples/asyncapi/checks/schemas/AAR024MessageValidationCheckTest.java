package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR024MessageValidationCheck;

public class AAR024MessageValidationCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR024";
        check = new AAR024MessageValidationCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyInV2WithValidation() {
        verifyV2("with-validation.yaml");
    }

    @Test
    public void verifyInV2WithoutValidation() {
        verifyV2("without-validation.yaml");
    }
    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithValidation() {
        verifyV3("with-validation.yaml");
    }
    @Test
    public void verifyV3WithoutValidation() {
        verifyV3("without-validation.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithValidation() {
        verifyV31("with-validation.yaml");
    }
    @Test
    public void verifyV31WithoutValidation() {
        verifyV31("without-validation.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR024 - MessageValidation - All messages sent and received must comply with the message schema specified in the documentation", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
