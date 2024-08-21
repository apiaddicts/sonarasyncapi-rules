package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR001MandatoryHttpsProtocolCheck;

public class AAR001MandatoryHttpsProtocolCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR001";
        check = new AAR001MandatoryHttpsProtocolCheck();
        v2Path = getV2Path("security");
    }

    @Test
    public void verifyInV2WithServer() {
        verifyV2("with-servers.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR001 - MandatoryHttpsProtocol - Https protocol is mandatory", RuleType.VULNERABILITY, Severity.CRITICAL, tags("safety"));
    }
}
