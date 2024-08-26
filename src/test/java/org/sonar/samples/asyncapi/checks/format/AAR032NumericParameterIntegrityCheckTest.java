package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR032NumericParameterIntegrityCheck;

public class AAR032NumericParameterIntegrityCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR032";
        check = new AAR032NumericParameterIntegrityCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("numeric-parameter-integrity.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR032 - NumericParameterIntegrity - Numeric parameters should have minimum, maximum, or format restriction", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
