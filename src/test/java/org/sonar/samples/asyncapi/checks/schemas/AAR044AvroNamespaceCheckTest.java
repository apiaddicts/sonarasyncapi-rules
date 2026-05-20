package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR044AvroNamespaceCheck;

public class AAR044AvroNamespaceCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR044";
        check = new AAR044AvroNamespaceCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2NamespaceValid() {
        verifyV2("namespace-required-valid.yaml");
    }

    @Test
    public void verifyV2NamespaceInvalid() {
        verifyV2("namespace-required-invalid.yaml");
    }

    @Test
    public void verifyV3NamespaceValid() {
        verifyV3("namespace-required-valid.yaml");
    }

    @Test
    public void verifyV3NamespaceInvalid() {
        verifyV3("namespace-required-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR044 - AvroNamespace - Avro record must define a namespace", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
