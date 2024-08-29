package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR017UndefinedUrlLicenseCheck;

public class AAR017UndefiendUrlLinceseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR017";
        check = new AAR017UndefinedUrlLicenseCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("undefined-url-license.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR017 - UndefinedUrlLicense - The license object must have the url field", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
