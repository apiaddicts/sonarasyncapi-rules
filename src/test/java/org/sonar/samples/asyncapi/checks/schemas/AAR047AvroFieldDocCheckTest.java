package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR047AvroFieldDocCheck;

public class AAR047AvroFieldDocCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR047";
        check = new AAR047AvroFieldDocCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2FieldDocValid() {
        verifyV2("field-doc-valid.yaml");
    }

    @Test
    public void verifyV2FieldDocInvalid() {
        verifyV2("field-doc-invalid.yaml");
    }

    @Test
    public void verifyV3FieldDocValid() {
        verifyV3("field-doc-valid.yaml");
    }

    @Test
    public void verifyV3FieldDocInvalid() {
        verifyV3("field-doc-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR047 - AvroFieldDoc - Avro fields should include a doc description", RuleType.CODE_SMELL, Severity.MINOR, tags("schemas"));
    }
}
