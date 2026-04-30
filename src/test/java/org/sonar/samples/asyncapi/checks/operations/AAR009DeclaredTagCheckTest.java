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
        v31Path = getV31Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("declared-tag.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithTags() {
        verifyV3("with-tags.yaml");
    }
    @Test
    public void verifyV3WithoutTags() {
        verifyV3("without-tags.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithTags() {
        verifyV31("with-tags.yaml");
    }
    @Test
    public void verifyV31WithoutTags() {
        verifyV31("without-tags.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR009 - DeclaredTag - Each operation should have a tag.", RuleType.BUG, Severity.BLOCKER, tags("operations"));
    }
}
