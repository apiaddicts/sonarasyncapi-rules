package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR013DuplicateOperationIDCheck;

public class AAR013DuplicateOperationIDCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR013";
        check = new AAR013DuplicateOperationIDCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("duplicate-operation-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR013 - DuplicateOperationID - There cannot be two unique operations that are the same", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
