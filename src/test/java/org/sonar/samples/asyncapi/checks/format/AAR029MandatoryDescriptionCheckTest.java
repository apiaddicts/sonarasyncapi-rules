package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR029MandatoryDescriptionCheck;

public class AAR029MandatoryDescriptionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR029";
        check = new AAR029MandatoryDescriptionCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("mandatory-description.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithDescription() {
        verifyV3("with-description.yaml");
    }
    @Test
    public void verifyV3WithoutDescription() {
        verifyV3("without-description.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithDescription() {
        verifyV31("with-description.yaml");
    }
    @Test
    public void verifyV31WithoutDescription() {
        verifyV31("without-description.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR029 - MandatoryDescription - Each channel and each operation must have a description that explains its purpose and function", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
