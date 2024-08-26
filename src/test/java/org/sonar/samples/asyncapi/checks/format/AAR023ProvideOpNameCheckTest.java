package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR023ProvideOpNameCheck;

public class AAR023ProvideOpNameCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR023";
        check = new AAR023ProvideOpNameCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("provide-op-name.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR023 - ProvideOpName - Provide a summary for each operation", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
