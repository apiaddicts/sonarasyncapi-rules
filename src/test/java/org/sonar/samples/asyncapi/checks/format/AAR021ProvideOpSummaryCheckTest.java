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

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithSummary() {
        verifyV3("with-summary.yaml");
    }
    @Test
    public void verifyV3WithoutSummary() {
        verifyV3("without-summary.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithSummary() {
        verifyV31("with-summary.yaml");
    }
    @Test
    public void verifyV31WithoutSummary() {
        verifyV31("without-summary.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR021 - ProvideOpSummary - Provide a summary for each operation", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
