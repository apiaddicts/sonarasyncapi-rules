package org.sonar.samples.asyncapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.schemas.AAR055XPayloadReferencesWellFormedCheck;

public class AAR055XPayloadReferencesWellFormedCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR055";
        check = new AAR055XPayloadReferencesWellFormedCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
        v31Path = getV31Path("schemas");
    }

    @Test
    public void verifyV2Valid() {
        verifyV2("valid.yaml");
    }

    @Test
    public void verifyV2MissingRef() {
        verifyV2("missing-ref.yaml");
    }

    @Test
    public void verifyV2EmptySubject() {
        verifyV2("empty-subject.yaml");
    }

    @Test
    public void verifyV2NotArray() {
        verifyV2("not-array.yaml");
    }

    @Test
    public void verifyV2ItemNotObject() {
        verifyV2("item-not-object.yaml");
    }

    @Test
    public void verifyV2FieldWrongType() {
        verifyV2("field-wrong-type.yaml");
    }

    @Test
    public void verifyV2LenientScalarValid() {
        verifyV2("lenient-scalar-valid.yaml");
    }

    @Test
    public void verifyV2MultipleOccurrences() {
        verifyV2("multiple-occurrences.yaml");
    }

    @Test
    public void verifyV3Valid() {
        verifyV3("valid.yaml");
    }

    @Test
    public void verifyV3MissingRef() {
        verifyV3("missing-ref.yaml");
    }

    @Test
    public void verifyV3EmptySubject() {
        verifyV3("empty-subject.yaml");
    }

    @Test
    public void verifyV3NotArray() {
        verifyV3("not-array.yaml");
    }

    @Test
    public void verifyV3ItemNotObject() {
        verifyV3("item-not-object.yaml");
    }

    @Test
    public void verifyV3FieldWrongType() {
        verifyV3("field-wrong-type.yaml");
    }

    @Test
    public void verifyV3LenientScalarValid() {
        verifyV3("lenient-scalar-valid.yaml");
    }

    @Test
    public void verifyV3MultipleOccurrences() {
        verifyV3("multiple-occurrences.yaml");
    }

    @Test
    public void verifyV31Valid() {
        verifyV31("valid.yaml");
    }

    @Test
    public void verifyV31MissingRef() {
        verifyV31("missing-ref.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR055 - XPayloadReferencesWellFormed - The x-payload-references extension must be well-formed", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}
