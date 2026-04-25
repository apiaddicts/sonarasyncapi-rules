package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR019IDSchemasCheck;

public class AAR019IDSchemasCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR019";
        check = new AAR019IDSchemasCheck();
        v2Path = getV2Path("schemas");
    }

    /* 
    @Test
    public void verifyInV2() {
        verifyV2("ID-schemas.yaml");
    }
    */
    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithId() {
        verifyV3("with-id.yaml");
    }
    @Test
    public void verifyV3WithoutId() {
        verifyV3("without-id.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithId() {
        verifyV31("with-id.yaml");
    }
    @Test
    public void verifyV31WithoutId() {
        verifyV31("without-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR019 - IDSchemas - The identifier must be defined", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
