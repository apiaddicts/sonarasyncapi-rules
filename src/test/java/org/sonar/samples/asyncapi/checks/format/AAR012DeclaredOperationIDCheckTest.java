package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR012DeclaredOperationIDCheck;

public class AAR012DeclaredOperationIDCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR012";
        check = new AAR012DeclaredOperationIDCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("declared-operation-id.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithOperationId() {
        verifyV3("with-operation-id.yaml");
    }
    @Test
    public void verifyV3WithoutOperationId() {
        verifyV3("without-operation-id.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithOperationId() {
        verifyV31("with-operation-id.yaml");
    }
    @Test
    public void verifyV31WithoutOperationId() {
        verifyV31("without-operation-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR012 - DeclaredOperationID - Each operation must declare an operationId", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
