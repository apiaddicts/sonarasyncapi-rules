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
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyInV2WithValidation() {
        verifyV2("with-validation.yaml");
    }

    @Test
    public void verifyInV2WithoutValidation() {
        verifyV2("without-validation.yaml");
    }

    @Test
    public void verifyInV2OneOfWithoutValidation() {
        verifyV2("oneof-without-validation.yaml");
    }

    @Test
    public void verifyInV2NonAvroSchemaFormat() {
        verifyV2("non-avro-schemaformat.yaml");
    }

    @Test
    public void verifyInV2NullContentType() {
        verifyV2("null-content-type.yaml");
    }

    @Test
    public void verifyInV2ComponentsMessage() {
        verifyV2("components-message.yaml");
    }

    // ============= V2 Avro Tests =============

    @Test
    public void verifyV2WithAvroSchemaFormat() {
        verifyV2("with-avro-schema-format.yaml");
    }

    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithValidation() {
        verifyV3("with-validation.yaml");
    }
    @Test
    public void verifyV3WithoutValidation() {
        verifyV3("without-validation.yaml");
    }

    @Test
    public void verifyV3NonAvroSchemaFormat() {
        verifyV3("non-avro-schemaformat.yaml");
    }

    @Test
    public void verifyV3MessageOneOf() {
        verifyV3("message-oneof.yaml");
    }

    // --- V3 Avro Tests ---
    @Test
    public void verifyV3WithAvroSchemaFormat() {
        verifyV3("with-avro-schema-format.yaml");
    }

    @Test
    public void verifyV3AvroMessageLevel() {
        verifyV3("avro-message-level.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithValidation() {
        verifyV31("with-validation.yaml");
    }
    @Test
    public void verifyV31WithoutValidation() {
        verifyV31("without-validation.yaml");
    }

    // --- V31 Avro Tests ---
    @Test
    public void verifyV31WithAvroSchemaFormat() {
        verifyV31("with-avro-schema-format.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR024 - MessageValidation - Each message must declare a contentType unless it is an Avro message", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
