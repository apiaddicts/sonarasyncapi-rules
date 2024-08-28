package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR016ContactPropertiesCheck;

public class AAR016ContactPropertiesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR016";
        check = new AAR016ContactPropertiesCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("contact-properties.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR016 - ContactProperties - Contact should contain name, url and email fields", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
