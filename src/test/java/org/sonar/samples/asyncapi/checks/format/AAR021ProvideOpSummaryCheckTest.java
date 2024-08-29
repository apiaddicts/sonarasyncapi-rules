package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR021ProvideOpSummaryCheck;

public class AAR021ProvideOpSummaryCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR021";
        check = new AAR021ProvideOpSummaryCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("provide-op-summary.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR021 - ProvideOpSummary - Provide a summary for each operation", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
