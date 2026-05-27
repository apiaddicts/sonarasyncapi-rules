package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR035MessageTitleCheck;

public class AAR035MessageTitleCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR035";
        check = new AAR035MessageTitleCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("message-title.yaml");
    }

    // ============= V2 Avro Tests =============

    @Test
    public void verifyV2WithAvroTitle() {
        verifyV2("message-title-avro-compliant.yaml");
    }

    @Test
    public void verifyV2WithoutAvroTitle() {
        verifyV2("message-title-avro.yaml");
    }

    // ============= V3.0+ Tests =============

    // --- V3 Avro Tests ---
    @Test
    public void verifyV3WithAvroTitle() {
        verifyV3("with-avro-title.yaml");
    }

    @Test
    public void verifyV3WithoutAvroTitle() {
        verifyV3("without-avro-title.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR035 - MessageTitle - It is recommended to have a title per message", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
