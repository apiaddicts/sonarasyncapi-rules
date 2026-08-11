package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR051OperationIdCamelCaseCheck;

public class AAR051OperationIdCamelCaseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR051";
        check = new AAR051OperationIdCamelCaseCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyV2ValidOperationId() {
        verifyV2("valid-operation-id.yaml");
    }

    @Test
    public void verifyV2InvalidOperationId() {
        verifyV2("invalid-operation-id.yaml");
    }

    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3ValidOperationId() {
        verifyV3("valid-operation-id.yaml");
    }

    @Test
    public void verifyV3InvalidOperationId() {
        verifyV3("invalid-operation-id.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31ValidOperationId() {
        verifyV31("valid-operation-id.yaml");
    }

    @Test
    public void verifyV31InvalidOperationId() {
        verifyV31("invalid-operation-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR051 - OperationIdCamelCase - The operationId must be present and follow camelCase naming convention", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
