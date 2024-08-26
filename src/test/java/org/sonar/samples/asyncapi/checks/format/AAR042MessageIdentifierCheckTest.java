package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR042MessageIdentifierCheck;

public class AAR042MessageIdentifierCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR042";
        check = new AAR042MessageIdentifierCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("message-identifier.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR042 - MessageIdentifier - It is recommended to have a unique identifier per message", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
