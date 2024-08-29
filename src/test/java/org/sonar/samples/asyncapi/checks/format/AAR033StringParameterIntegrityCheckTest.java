package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR033StringParameterIntegrityCheck;

public class AAR033StringParameterIntegrityCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR033";
        check = new AAR033StringParameterIntegrityCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("string-parameter-integrity.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR033 - StringParameterIntegrity - String parameters should have minLength, maxLength, pattern (regular expression), or enum restriction", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
