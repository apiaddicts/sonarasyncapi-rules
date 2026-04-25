package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR010DocumentedTagCheck;

public class AAR010DocumentedTagCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR010";
        check = new AAR010DocumentedTagCheck();
        v2Path = getV2Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("documented-tag.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithDocumentedTags() {
        verifyV3("with-documented-tags.yaml");
    }
    @Test
    public void verifyV3WithoutDocumentedTags() {
        verifyV3("without-documented-tags.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithDocumentedTags() {
        verifyV31("with-documented-tags.yaml");
    }
    @Test
    public void verifyV31WithoutDocumentedTags() {
        verifyV31("without-documented-tags.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR010 - DocumentedTag - Tags should be documented", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
