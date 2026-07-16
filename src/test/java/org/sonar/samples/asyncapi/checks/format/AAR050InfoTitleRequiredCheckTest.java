package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR050InfoTitleRequiredCheck;

public class AAR050InfoTitleRequiredCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR050";
        check = new AAR050InfoTitleRequiredCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyV2WithTitle() {
        verifyV2("with-title.yaml");
    }

    @Test
    public void verifyV2EmptyTitle() {
        verifyV2("empty-title.yaml");
    }

    @Test
    public void verifyV2WithoutTitle() {
        verifyV2("without-title.yaml");
    }

    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithTitle() {
        verifyV3("with-title.yaml");
    }

    @Test
    public void verifyV3EmptyTitle() {
        verifyV3("empty-title.yaml");
    }

    @Test
    public void verifyV3WithoutTitle() {
        verifyV3("without-title.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithTitle() {
        verifyV31("with-title.yaml");
    }

    @Test
    public void verifyV31EmptyTitle() {
        verifyV31("empty-title.yaml");
    }

    @Test
    public void verifyV31WithoutTitle() {
        verifyV31("without-title.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR050 - InfoTitleRequired - The info.title field must exist and not be empty", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
