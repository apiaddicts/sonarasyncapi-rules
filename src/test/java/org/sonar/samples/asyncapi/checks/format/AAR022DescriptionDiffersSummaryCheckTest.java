package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR022DescriptionDiffersSummaryCheck;

public class AAR022DescriptionDiffersSummaryCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR022";
        check = new AAR022DescriptionDiffersSummaryCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("description-differs-summary.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR022 - DescriptionDiffersSummary - Operation description must differ from its summary", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
