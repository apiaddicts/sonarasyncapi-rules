package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR060ContentTypeAvroCheck;

public class AAR060ContentTypeAvroCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR060";
        check = new AAR060ContentTypeAvroCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
    }

    @Test
    public void verifyV2ContentTypeValid() {
        verifyV2("content-type-valid.yaml");
    }

    @Test
    public void verifyV2ContentTypeInvalid() {
        verifyV2("content-type-invalid.yaml");
    }

    @Test
    public void verifyV3ContentTypeValid() {
        verifyV3("content-type-valid.yaml");
    }

    @Test
    public void verifyV3ContentTypeInvalid() {
        verifyV3("content-type-invalid.yaml");
    }

    @Test
    public void verifyV2ContentTypeFormatVariants() {
        verifyV2("content-type-format-variants.yaml");
    }

    @Test
    public void verifyV3ContentTypeFormatVariants() {
        verifyV3("content-type-format-variants.yaml");
    }

    @Test
    public void verifyV2ContentTypeNull() {
        verifyV2("content-type-null.yaml");
    }

    @Test
    public void verifyV3ContentTypeNull() {
        verifyV3("content-type-null.yaml");
    }

    @Test
    public void verifyV2ContentTypeNonString() {
        verifyV2("content-type-non-string.yaml");
    }

    @Test
    public void verifyV3ContentTypeNonString() {
        verifyV3("content-type-non-string.yaml");
    }

    @Test
    public void verifyV2ContentTypeNoDefault() {
        verifyV2("content-type-no-default.yaml");
    }

    @Test
    public void verifyV3ContentTypeNoDefault() {
        verifyV3("content-type-no-default.yaml");
    }

    @Test
    public void verifyV2ContentTypeLengthBoundary() {
        verifyV2("content-type-length-boundary.yaml");
    }

    @Test
    public void verifyV3ContentTypeLengthBoundary() {
        verifyV3("content-type-length-boundary.yaml");
    }

    @Test
    public void verifyV3ContentTypeOneOf() {
        verifyV3("content-type-oneof.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR060 - ContentTypeAvro - The message contentType must be application/*+avro",
                RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
