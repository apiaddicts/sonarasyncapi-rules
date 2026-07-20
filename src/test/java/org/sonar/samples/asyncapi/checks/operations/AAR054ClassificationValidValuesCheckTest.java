package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR054ClassificationValidValuesCheck;

public class AAR054ClassificationValidValuesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR054";
        check = new AAR054ClassificationValidValuesCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
        v31Path = getV31Path("operations");
    }

    @Test
    public void verifyV2ClassificationValidCdc() {
        verifyV2("classification-valid-cdc.yaml");
    }

    @Test
    public void verifyV2ClassificationValidCmd() {
        verifyV2("classification-valid-cmd.yaml");
    }

    @Test
    public void verifyV2ClassificationValidSys() {
        verifyV2("classification-valid-sys.yaml");
    }

    @Test
    public void verifyV2ClassificationInvalidValue() {
        verifyV2("classification-invalid-value.yaml");
    }

    @Test
    public void verifyV2ClassificationMissingSegment() {
        verifyV2("classification-missing-segment.yaml");
    }

    @Test
    public void verifyV2ClassificationInvalidNumericKey() {
        verifyV2("classification-invalid-numeric-key.yaml");
    }

    @Test
    public void verifyV3ClassificationValid() {
        verifyV3("classification-valid.yaml");
    }

    @Test
    public void verifyV3ClassificationInvalid() {
        verifyV3("classification-invalid.yaml");
    }

    @Test
    public void verifyV3ClassificationInvalidUnquotedNumericAddress() {
        verifyV3("classification-invalid-unquoted-numeric-address.yaml");
    }

    @Test
    public void verifyV3ClassificationInvalidUnquotedBooleanAddress() {
        verifyV3("classification-invalid-unquoted-boolean-address.yaml");
    }

    @Test
    public void verifyV3ClassificationNoAddressValid() {
        verifyV3("classification-no-address-valid.yaml");
    }

    @Test
    public void verifyV3ClassificationNoAddressInvalid() {
        verifyV3("classification-no-address-invalid.yaml");
    }

    @Test
    public void verifyV3ClassificationNullAddressSkipped() {
        verifyV3("classification-null-address.yaml");
    }

    @Test
    public void verifyV31ClassificationValid() {
        verifyV31("classification-valid.yaml");
    }

    @Test
    public void verifyV31ClassificationInvalid() {
        verifyV31("classification-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR054 - ClassificationValidValues - Channel classification must be cdc, cmd or sys", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("validValues", AAR054ClassificationValidValuesCheck.DEFAULT_VALID_VALUES, RuleParamType.STRING);
    }
}
