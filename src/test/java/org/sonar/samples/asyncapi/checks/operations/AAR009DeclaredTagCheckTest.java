package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR009DeclaredTagCheck;

public class AAR009DeclaredTagCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR009";
        check = new AAR009DeclaredTagCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("declared-tag.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR009 - DeclaredTag - Each operation should have a tag.", RuleType.BUG, Severity.BLOCKER, tags("operations"));
    }
}
