package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR045AvroNamespaceNamingCheck;

public class AAR045AvroNamespaceNamingCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR045";
        check = new AAR045AvroNamespaceNamingCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2NamespaceNamingValid() {
        verifyV2("namespace-naming-valid.yaml");
    }

    @Test
    public void verifyV2NamespaceNamingInvalid() {
        verifyV2("namespace-naming-invalid.yaml");
    }

    @Test
    public void verifyV3NamespaceNamingValid() {
        verifyV3("namespace-naming-valid.yaml");
    }

    @Test
    public void verifyV3NamespaceNamingInvalid() {
        verifyV3("namespace-naming-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR045 - AvroNamespaceNaming - Avro namespace must follow lowercase dot notation", RuleType.CODE_SMELL, Severity.MINOR, tags("schemas"));
    }
}
