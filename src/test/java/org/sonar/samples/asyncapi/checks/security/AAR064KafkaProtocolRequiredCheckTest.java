package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR064KafkaProtocolRequiredCheck;

public class AAR064KafkaProtocolRequiredCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR064";
        check = new AAR064KafkaProtocolRequiredCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
        v31Path = getV31Path("security");
    }

    @Test
    public void verifyV2Valid() {
        verifyV2("valid.yaml");
    }

    @Test
    public void verifyV2Invalid() {
        verifyV2("invalid.yaml");
    }

    @Test
    public void verifyV2InvalidArrayProtocol() {
        verifyV2("invalid-array-protocol.yaml");
    }

    @Test
    public void verifyV3Valid() {
        verifyV3("valid.yaml");
    }

    @Test
    public void verifyV3Invalid() {
        verifyV3("invalid.yaml");
    }

    @Test
    public void verifyV3InvalidArray() {
        verifyV3("invalid-array.yaml");
    }

    @Test
    public void verifyV31Invalid() {
        verifyV31("invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
            "AAR064 - KafkaProtocolRequired - The server protocol must be kafka or kafka-ssl",
            RuleType.VULNERABILITY, Severity.CRITICAL, tags("safety"));
    }
}
