package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR020DefineIDCheck;

public class AAR020DefineIDCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR020";
        check = new AAR020DefineIDCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("define-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR020 - DefineID - It must conform to the URI format , according to RFC3986", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
