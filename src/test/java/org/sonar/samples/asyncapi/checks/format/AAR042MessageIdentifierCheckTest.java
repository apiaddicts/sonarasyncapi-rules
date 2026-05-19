package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR042MessageIdentifierCheck;

public class AAR042MessageIdentifierCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR042";
        check = new AAR042MessageIdentifierCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("message-id.yaml");
    }

    // ============= V2 Avro Tests =============

    @Test
    public void verifyV2WithAvroMessageId() {
        verifyV2("message-id-avro-compliant.yaml");
    }

    @Test
    public void verifyV2WithoutAvroMessageId() {
        verifyV2("message-id-avro.yaml");
    }

    // ============= V3.0+ Tests =============

    // --- V3 Avro Tests ---
    @Test
    public void verifyV3WithAvroMessageId() {
        verifyV3("with-avro-message-id.yaml");
    }

    @Test
    public void verifyV3WithoutAvroMessageId() {
        verifyV3("without-avro-message-id.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR042 - MessageIdentifier - It is recommended to have a unique identifier per message", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
