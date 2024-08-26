package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR036BadDescriptionCheck;

public class AAR036BadDescriptionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR036";
        check = new AAR036BadDescriptionCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("bad-description.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR036 - BadDescription - The description must begin with the first capital letter and end with point", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
