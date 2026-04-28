package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR034NumericFormatCheck;

public class AAR034NumericFormatCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR034";
        check = new AAR034NumericFormatCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("numeric-format.yaml");
    }

    
    // ============= V3.0+ Tests =============

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR034 - NumericFormat - Numeric types requires a valid format", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
