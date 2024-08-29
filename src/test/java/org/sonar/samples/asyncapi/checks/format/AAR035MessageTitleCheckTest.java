package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR035MessageTitleCheck;

public class AAR035MessageTitleCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR035";
        check = new AAR035MessageTitleCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("message-title.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR035 - MessageTitle - It is recommended to have a title per message", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
