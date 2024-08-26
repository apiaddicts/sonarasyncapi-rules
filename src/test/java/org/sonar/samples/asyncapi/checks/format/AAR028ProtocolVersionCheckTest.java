package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR028ProtocolVersionCheck;

public class AAR028ProtocolVersionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR028";
        check = new AAR028ProtocolVersionCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("protocol-version.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR028 - ProtocolVersion - The version of the protocol used must be specified to avoid compatibility errors", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
