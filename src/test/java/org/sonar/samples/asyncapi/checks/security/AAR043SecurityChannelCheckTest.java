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
        v3Path = getV3Path("security");
        v31Path = getV31Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("security-channel.yaml");
    }

    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithSecurity() {
        verifyV3("with-security.yaml");
    }
    @Test
    public void verifyV3WithoutSecurity() {
        verifyV3("without-security.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithSecurity() {
        verifyV31("with-security.yaml");
    }
    @Test
    public void verifyV31WithoutSecurity() {
        verifyV31("without-security.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR043 - SecurityChannel - It is recommended to add the security scheme to be used to each channel", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }
}
