package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR040DefinedChannelServersCheck;

public class AAR040DefinedChannelServersCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR040";
        check = new AAR040DefinedChannelServersCheck();
        v2Path = getV2Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("defined-channels-server.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR040 - DefinedChannelServers - Channel server must be defined in the servers object", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
