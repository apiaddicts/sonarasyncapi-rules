package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR013DuplicateOperationIDCheck;

public class AAR013DuplicateOperationIDCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR013";
        check = new AAR013DuplicateOperationIDCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("duplicate-operation-id.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithDuplicateIds() {
        verifyV3("with-duplicate-ids.yaml");
    }
    @Test
    public void verifyV3WithUniqueIds() {
        verifyV3("with-unique-ids.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithDuplicateIds() {
        verifyV31("with-duplicate-ids.yaml");
    }
    @Test
    public void verifyV31WithUniqueIds() {
        verifyV31("with-unique-ids.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR013 - DuplicateOperationID - There cannot be two operations with the same operationId", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
