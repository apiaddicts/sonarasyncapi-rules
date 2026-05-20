package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR048AvroNameNomenclatureCheck;

public class AAR048AvroNameNomenclatureCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR048";
        check = new AAR048AvroNameNomenclatureCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2NameNomenclatureValid() {
        verifyV2("name-nomenclature-valid.yaml");
    }

    @Test
    public void verifyV2NameNomenclatureInvalid() {
        verifyV2("name-nomenclature-invalid.yaml");
    }

    @Test
    public void verifyV3NameNomenclatureValid() {
        verifyV3("name-nomenclature-valid.yaml");
    }

    @Test
    public void verifyV3NameNomenclatureInvalid() {
        verifyV3("name-nomenclature-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR048 - AvroNameNomenclature - Avro names must follow Avro naming rules", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
