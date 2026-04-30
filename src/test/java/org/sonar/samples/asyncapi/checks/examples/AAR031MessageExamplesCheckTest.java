package org.sonar.samples.asyncapi.checks.examples;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.examples.AAR031MessageExamplesCheck;

public class AAR031MessageExamplesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR031";
        check = new AAR031MessageExamplesCheck();
        v2Path = getV2Path("examples");
        v3Path = getV3Path("examples");
        v31Path = getV31Path("examples");
    }

    @Test
    public void verifyInV2() {
        verifyV2("message-examples.yaml");
    }

    @Test
    public void verifyInV2Compliant() {
        verifyV2("message-examples-compliant.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithExamples() {
        verifyV3("with-examples.yaml");
    }
    @Test
    public void verifyV3WithoutExamples() {
        verifyV3("without-examples.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithExamples() {
        verifyV31("with-examples.yaml");
    }
    @Test
    public void verifyV31WithoutExamples() {
        verifyV31("without-examples.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR031 - MessageExamples - All examples in message object should follow payload and headers schemas", RuleType.BUG, Severity.MAJOR, tags("examples"));
    }
}
