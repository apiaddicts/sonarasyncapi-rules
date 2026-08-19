package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR056AvroSchemaFormatCheck;

public class AAR056AvroSchemaFormatCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR056";
        check = new AAR056AvroSchemaFormatCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2Valid() {
        verifyV2("valid.yaml");
    }

    @Test
    public void verifyV2WrongVersion() {
        verifyV2("wrong-version.yaml");
    }

    @Test
    public void verifyV2NonAvroSchemaFormat() {
        verifyV2("non-avro-schema-format.yaml");
    }

    @Test
    public void verifyV2WrongCase() {
        verifyV2("wrong-case.yaml");
    }

    @Test
    public void verifyV2NonStringSchemaFormat() {
        verifyV2("non-string-schema-format.yaml");
    }

    @Test
    public void verifyV2WrongFormatVariants() {
        verifyV2("wrong-format-variants.yaml");
    }

    @Test
    public void verifyV2Mixed() {
        verifyV2("mixed.yaml");
    }

    @Test
    public void verifyV2OneofNested() {
        verifyV2("oneof-nested.yaml");
    }

    @Test
    public void verifyV3Valid() {
        verifyV3("valid.yaml");
    }

    @Test
    public void verifyV3WrongVersion() {
        verifyV3("wrong-version.yaml");
    }

    @Test
    public void verifyV3NonAvroSchemaFormat() {
        verifyV3("non-avro-schema-format.yaml");
    }

    @Test
    public void verifyV3WrongCase() {
        verifyV3("wrong-case.yaml");
    }

    @Test
    public void verifyV3NonStringSchemaFormat() {
        verifyV3("non-string-schema-format.yaml");
    }

    @Test
    public void verifyV3WrongFormatVariants() {
        verifyV3("wrong-format-variants.yaml");
    }

    @Test
    public void verifyV3Mixed() {
        verifyV3("mixed.yaml");
    }

    @Test
    public void verifyV31Valid() {
        verifyV31("valid.yaml");
    }

    @Test
    public void verifyV31WrongVersion() {
        verifyV31("wrong-version.yaml");
    }

    @Test
    public void verifyV31NonAvroSchemaFormat() {
        verifyV31("non-avro-schema-format.yaml");
    }

    @Test
    public void verifyV31WrongCase() {
        verifyV31("wrong-case.yaml");
    }

    @Test
    public void verifyV31NonStringSchemaFormat() {
        verifyV31("non-string-schema-format.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR056 - AvroSchemaFormat - The schemaFormat must be application/vnd.apache.avro;version=1.9.0", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
