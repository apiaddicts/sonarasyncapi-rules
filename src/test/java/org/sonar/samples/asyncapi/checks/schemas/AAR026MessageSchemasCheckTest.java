package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR026MessageSchemasCheck;

public class AAR026MessageSchemasCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR026";
        check = new AAR026MessageSchemasCheck();
        v2Path = getV2Path("schemas");
    }

    /* 
    @Test
    public void verifyInV2() {
        verifyV2("message-schemas.yaml");
    }
    */
    @Override
    public void verifyRule() {
        assertRuleProperties("AAR026 - MessageSchemas - Message schemas are recommended to be found in components", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
