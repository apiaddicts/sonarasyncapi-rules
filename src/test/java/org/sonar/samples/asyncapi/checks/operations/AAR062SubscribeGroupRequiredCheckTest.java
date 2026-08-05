package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR062SubscribeGroupRequiredCheck;

public class AAR062SubscribeGroupRequiredCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR062";
        check = new AAR062SubscribeGroupRequiredCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyV2Valid() {
        verifyV2("valid.yaml");
    }

    @Test
    public void verifyV2Invalid() {
        verifyV2("invalid.yaml");
    }

    @Test
    public void verifyV3Valid() {
        verifyV3("valid.yaml");
    }

    @Test
    public void verifyV3Invalid() {
        verifyV3("invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
                "AAR062 - SubscribeGroupRequired - Subscribe operations must declare the consumer group",
                RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
