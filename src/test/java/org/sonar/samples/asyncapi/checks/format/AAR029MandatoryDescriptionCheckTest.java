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
    }

    @Test
    public void verifyInV2() {
        verifyV2("mandatory-description.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR029 - MandatoryDescription -  Each channel and each operation must have a description that explains its purpose and function", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
