package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR046AvroRecordDocCheck;

public class AAR046AvroRecordDocCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR046";
        check = new AAR046AvroRecordDocCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2RecordDocValid() {
        verifyV2("record-doc-valid.yaml");
    }

    @Test
    public void verifyV2RecordDocInvalid() {
        verifyV2("record-doc-invalid.yaml");
    }

    @Test
    public void verifyV3RecordDocValid() {
        verifyV3("record-doc-valid.yaml");
    }

    @Test
    public void verifyV3RecordDocInvalid() {
        verifyV3("record-doc-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR046 - AvroRecordDoc - Avro record should include a doc description", RuleType.CODE_SMELL, Severity.MINOR, tags("schemas"));
    }
}
