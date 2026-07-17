package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR052AvroNamespacePatternCheck;

public class AAR052AvroNamespacePatternCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR052";
        check = new AAR052AvroNamespacePatternCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2NamespacePatternValidApp() {
        verifyV2("namespace-pattern-valid-app.yaml");
    }

    @Test
    public void verifyV2NamespacePatternValidCommon() {
        verifyV2("namespace-pattern-valid-common.yaml");
    }

    @Test
    public void verifyV2NamespacePatternInvalid() {
        verifyV2("namespace-pattern-invalid.yaml");
    }

    @Test
    public void verifyV2NamespacePatternEmptyString() {
        verifyV2("namespace-pattern-empty-string.yaml");
    }

    @Test
    public void verifyV2NamespacePatternRef() {
        verifyV2("namespace-pattern-ref.yaml");
    }

    @Test
    public void verifyV3NamespacePatternValidApp() {
        verifyV3("namespace-pattern-valid-app.yaml");
    }

    @Test
    public void verifyV3NamespacePatternValidCommon() {
        verifyV3("namespace-pattern-valid-common.yaml");
    }

    @Test
    public void verifyV3NamespacePatternInvalid() {
        verifyV3("namespace-pattern-invalid.yaml");
    }

    @Test
    public void verifyV3NamespacePatternEmptyString() {
        verifyV3("namespace-pattern-empty-string.yaml");
    }

    @Test
    public void verifyV3NamespacePatternRef() {
        verifyV3("namespace-pattern-ref.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR052 - AvroNamespacePattern - Avro namespace must follow the corporate pattern", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("pattern", AAR052AvroNamespacePatternCheck.DEFAULT_PATTERN, RuleParamType.STRING);
    }
}
