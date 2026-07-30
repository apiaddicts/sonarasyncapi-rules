package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR059AvroRecordNameCamelCaseCheck;

public class AAR059AvroRecordNameCamelCaseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR059";
        check = new AAR059AvroRecordNameCamelCaseCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2NameCamelCaseValid() {
        verifyV2("name-camel-case-valid.yaml");
    }

    @Test
    public void verifyV2NameCamelCaseInvalid() {
        verifyV2("name-camel-case-invalid.yaml");
    }

    @Test
    public void verifyV2NameCamelCaseSharedRef() {
        verifyV2("name-camel-case-shared-ref.yaml");
    }

    @Test
    public void verifyV3NameCamelCaseValid() {
        verifyV3("name-camel-case-valid.yaml");
    }

    @Test
    public void verifyV3NameCamelCaseInvalid() {
        verifyV3("name-camel-case-invalid.yaml");
    }

    @Test
    public void verifyV3NameCamelCaseSharedRef() {
        verifyV3("name-camel-case-shared-ref.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR059 - AvroRecordNameCamelCase - Avro record names must be in CamelCase",
                RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
