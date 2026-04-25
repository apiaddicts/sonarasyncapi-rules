package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR011DefinedLicenseCheck;

public class AAR011DefinedLicenseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR011";
        check = new AAR011DefinedLicenseCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("defined-license.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithLicense() {
        verifyV3("with-license.yaml");
    }
    @Test
    public void verifyV3WithoutLicense() {
        verifyV3("without-license.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithLicense() {
        verifyV31("with-license.yaml");
    }
    @Test
    public void verifyV31WithoutLicense() {
        verifyV31("without-license.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR011 - DefinedLicense - License should be documented", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
