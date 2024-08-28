package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR037BindingVersionCheck;

public class AAR037BindingVersionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR037";
        check = new AAR037BindingVersionCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("binding-version.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR037 - BindingVersion - You must specify the version of the binding", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
