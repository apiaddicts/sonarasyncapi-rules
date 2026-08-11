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
    public void verifyV2NamespacePatternMissing() {
        verifyV2("namespace-pattern-missing.yaml");
    }

    @Test
    public void verifyV2NamespacePatternTwoSegments() {
        verifyV2("namespace-pattern-two-segments.yaml");
    }

    @Test
    public void verifyV2NamespacePatternFourSegments() {
        verifyV2("namespace-pattern-four-segments.yaml");
    }

    @Test
    public void verifyV2NamespacePatternUppercase() {
        verifyV2("namespace-pattern-uppercase.yaml");
    }

    @Test
    public void verifyV2NamespaceEnumInvalid() {
        verifyV2("namespace-enum-invalid.yaml");
    }

    @Test
    public void verifyV2NamespaceEnumValid() {
        verifyV2("namespace-enum-valid.yaml");
    }

    @Test
    public void verifyV2NamespaceFixedInvalid() {
        verifyV2("namespace-fixed-invalid.yaml");
    }

    @Test
    public void verifyV2NamespaceFixedValid() {
        verifyV2("namespace-fixed-valid.yaml");
    }

    @Test
    public void verifyV2NamespaceNull() {
        verifyV2("namespace-null.yaml");
    }

    @Test
    public void verifyV2NamespaceNonString() {
        verifyV2("namespace-non-string.yaml");
    }

    @Test
    public void verifyV2NamespaceSpecialChars() {
        verifyV2("namespace-special-chars.yaml");
    }

    @Test
    public void verifyV2NamespaceCommonMisspelled() {
        verifyV2("namespace-common-misspelled.yaml");
    }

    @Test
    public void verifyV2NamespaceMultiRecord() {
        verifyV2("namespace-multi-record.yaml");
    }

    @Test
    public void verifyV2NamespaceCustomPattern() {
        check = customPatternCheck();
        verifyV2("namespace-custom-pattern.yaml");
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

    @Test
    public void verifyV3NamespacePatternMissing() {
        verifyV3("namespace-pattern-missing.yaml");
    }

    @Test
    public void verifyV3NamespacePatternTwoSegments() {
        verifyV3("namespace-pattern-two-segments.yaml");
    }

    @Test
    public void verifyV3NamespacePatternFourSegments() {
        verifyV3("namespace-pattern-four-segments.yaml");
    }

    @Test
    public void verifyV3NamespacePatternUppercase() {
        verifyV3("namespace-pattern-uppercase.yaml");
    }

    @Test
    public void verifyV3NamespaceEnumInvalid() {
        verifyV3("namespace-enum-invalid.yaml");
    }

    @Test
    public void verifyV3NamespaceEnumValid() {
        verifyV3("namespace-enum-valid.yaml");
    }

    @Test
    public void verifyV3NamespaceFixedInvalid() {
        verifyV3("namespace-fixed-invalid.yaml");
    }

    @Test
    public void verifyV3NamespaceFixedValid() {
        verifyV3("namespace-fixed-valid.yaml");
    }

    @Test
    public void verifyV3NamespaceNull() {
        verifyV3("namespace-null.yaml");
    }

    @Test
    public void verifyV3NamespaceNonString() {
        verifyV3("namespace-non-string.yaml");
    }

    @Test
    public void verifyV3NamespaceSpecialChars() {
        verifyV3("namespace-special-chars.yaml");
    }

    @Test
    public void verifyV3NamespaceCommonMisspelled() {
        verifyV3("namespace-common-misspelled.yaml");
    }

    @Test
    public void verifyV3NamespaceMultiRecord() {
        verifyV3("namespace-multi-record.yaml");
    }

    @Test
    public void verifyV3NamespaceCustomPattern() {
        check = customPatternCheck();
        verifyV3("namespace-custom-pattern.yaml");
    }

    // A check configured with a custom `pattern` RuleProperty, used to prove the override
    // is applied instead of the default (the fixture namespace is valid under the default).
    private AAR052AvroNamespacePatternCheck customPatternCheck() {
        AAR052AvroNamespacePatternCheck customCheck = new AAR052AvroNamespacePatternCheck();
        customCheck.patternStr = "^custom\\.[a-z]+$";
        return customCheck;
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
