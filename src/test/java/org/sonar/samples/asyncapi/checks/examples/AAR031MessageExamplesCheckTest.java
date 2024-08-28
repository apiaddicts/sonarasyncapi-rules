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
    }

    /* 
    @Test
    public void verifyInV2() {
        verifyV2("message-examples.yaml");
    }*/

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR031 - MessageExamples - All examples in message object should follow payload and headers schemas", RuleType.BUG, Severity.MAJOR, tags("examples"));
    }
}
