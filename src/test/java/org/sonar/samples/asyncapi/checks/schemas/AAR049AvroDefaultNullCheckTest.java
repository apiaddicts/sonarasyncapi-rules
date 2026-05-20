package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR049AvroDefaultNullCheck;

public class AAR049AvroDefaultNullCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR049";
        check = new AAR049AvroDefaultNullCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2DefaultNullValid() {
        verifyV2("default-null-valid.yaml");
    }

    @Test
    public void verifyV2DefaultNullInvalid() {
        verifyV2("default-null-invalid.yaml");
    }

    @Test
    public void verifyV3DefaultNullValid() {
        verifyV3("default-null-valid.yaml");
    }

    @Test
    public void verifyV3DefaultNullInvalid() {
        verifyV3("default-null-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR049 - AvroDefaultNull - Optional Avro fields must define default as null", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
