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

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR060 - ContentTypeAvro - The message contentType must be application/*+avro",
                RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
