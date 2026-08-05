package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR063AsyncAPIVersionAllowedCheck;

public class AAR063AsyncAPIVersionAllowedCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR063";
        check = new AAR063AsyncAPIVersionAllowedCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyV2Allowed() {
        verifyV2("allowed.yaml");
    }

    @Test
    public void verifyV2NotAllowed() {
        verifyV2("not-allowed.yaml");
    }

    @Test
    public void verifyV2EmptyVersion() {
        verifyV2("empty-version.yaml");
    }

    @Test
    public void verifyV3NotAllowed() {
        verifyV3("not-allowed.yaml");
    }

    @Test
    public void verifyV31NotAllowed() {
        verifyV31("not-allowed.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
            "AAR063 - AsyncAPIVersionAllowed - The asyncapi version must be one of the versions allowed by the organization",
            RuleType.BUG, Severity.MAJOR, tags("format"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("allowedVersions", "2.6.0", RuleParamType.STRING);
    }
}
