package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR024MessageValidationCheck;

public class AAR024MessageValidationCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR024";
        check = new AAR024MessageValidationCheck();
        v2Path = getV2Path("schemas");
    }
    /* 
    @Test
    public void verifyInV2() {
        verifyV2("message-validation.yaml");
    }
    */
    @Override
    public void verifyRule() {
        assertRuleProperties("AAR024 - MessageValidation - All messages sent and received must comply with the message schema specified in the documentation", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
