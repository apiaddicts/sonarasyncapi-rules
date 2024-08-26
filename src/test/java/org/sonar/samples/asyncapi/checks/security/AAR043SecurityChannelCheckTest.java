package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR043SecurityChannelCheck;

public class AAR043SecurityChannelCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR043";
        check = new AAR043SecurityChannelCheck();
        v2Path = getV2Path("security");
    }
    @Test
    public void verifyInV2() {
        verifyV2("security-channel.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR043 - SecurityChannel - It is recommended to add the security scheme to be used to each channel", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }
}
