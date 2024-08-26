package org.sonar.samples.asyncapi.checks.examples;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.examples.AAR031ExamplesCheck;

public class AAR031ExamplesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR031";
        check = new AAR031ExamplesCheck();
        v2Path = getV2Path("examples");
        v3Path = getV3Path("examples");
    }

    @Test
    public void verifyInV2() {
        verifyV2("examples.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR031 - Examples - There must be examples for each channel and operation", RuleType.BUG, Severity.MAJOR, tags("examples"));
    }
}
