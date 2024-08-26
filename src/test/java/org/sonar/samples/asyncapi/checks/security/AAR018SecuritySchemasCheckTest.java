package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR018SecuritySchemasCheck;

public class AAR018SecuritySchemasCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR018";
        check = new AAR018SecuritySchemasCheck();
        v2Path = getV2Path("security");
    }
    @Test
    public void verifyInV2() {
        verifyV2("security-schemas.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR018 - SecuritySchemas - The security scheme must be among those allowed by the organization and must be complete", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }
}
