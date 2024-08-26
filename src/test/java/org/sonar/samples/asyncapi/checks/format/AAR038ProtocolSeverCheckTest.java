package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR038ProtocolSeverCheck;

public class AAR038ProtocolSeverCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR038";
        check = new AAR038ProtocolSeverCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("protocol-server.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR038 - ProtocolServer - It is mandatory to define the server protocol", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
