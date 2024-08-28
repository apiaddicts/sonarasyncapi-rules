package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR041ComponetChannelServerCheck;

public class AAR041ComponetChannelServerCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR041";
        check = new AAR041ComponetChannelServerCheck();
        v2Path = getV2Path("operations");
    }
    /*
    @Test
    public void verifyInV2() {
        verifyV2("componet-channel-server.yaml");
    } */

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR041 - ComponetChannelServer - It is recommend to add the servers and channels to component", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
