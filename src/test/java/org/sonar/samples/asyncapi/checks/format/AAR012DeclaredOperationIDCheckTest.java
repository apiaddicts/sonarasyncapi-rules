package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR012DeclaredOperationIDCheck;

public class AAR012DeclaredOperationIDCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR012";
        check = new AAR012DeclaredOperationIDCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("declared-operation-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR012 - DeclaredOperationID - Each operation should have a unique operator", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
