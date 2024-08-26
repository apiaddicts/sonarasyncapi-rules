package org.sonar.samples.asyncapi.checks.examples;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.examples.AAR039MoreExamplesCheck;

public class AAR039MoreExamplesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR039";
        check = new AAR039MoreExamplesCheck();
        v2Path = getV2Path("examples");
        v3Path = getV3Path("examples");
    }

    @Test
    public void verifyInV2() {
        verifyV2("more-examples.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR039 - MoreExamples - It is recommended to have more than 2 examples of the cases that may appear", RuleType.BUG, Severity.MAJOR, tags("examples"));
    }
}
