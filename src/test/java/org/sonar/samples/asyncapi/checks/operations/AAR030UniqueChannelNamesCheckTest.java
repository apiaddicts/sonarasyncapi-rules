package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR030UniqueChannelNamesCheck;

public class AAR030UniqueChannelNamesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR030";
        check = new AAR030UniqueChannelNamesCheck();
        v2Path = getV2Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("unique-channel-names.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR030 - UniqueChannelNames - Channel names must be unique throughout the AsyncAPI specification", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
