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
    public void verifyV2InvalidWrongCase() {
        verifyV2("invalid-wrong-case.yaml");
    }

    @Test
    public void verifyV2InvalidWhitespace() {
        verifyV2("invalid-whitespace.yaml");
    }

    @Test
    public void verifyV2InvalidNumeric() {
        verifyV2("invalid-numeric.yaml");
    }

    @Test
    public void verifyV2InvalidObjectProtocol() {
        verifyV2("invalid-object-protocol.yaml");
    }

    @Test
    public void verifyV2InvalidEmptyObjectArrayProtocol() {
        verifyV2("invalid-empty-object-array-protocol.yaml");
    }

    @Test
    public void verifyV2InvalidMixed() {
        verifyV2("invalid-mixed.yaml");
    }

    @Test
    public void verifyV2SkipCases() {
        verifyV2("skip-cases.yaml");
    }

    @Test
    public void verifyV2NoServers() {
        verifyV2("no-servers.yaml");
    }

    @Test
    public void verifyV2NullServers() {
        verifyV2("null-servers.yaml");
    }

    @Test
    public void verifyV2EmptyServers() {
        verifyV2("empty-servers.yaml");
    }

    @Test
    public void verifyV2RefServer() {
        verifyV2("ref-server.yaml");
    }

    @Test
    public void verifyV2RefServerValid() {
        verifyV2("ref-server-valid.yaml");
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
    public void verifyV3InvalidArrayMixed() {
        verifyV3("invalid-array-mixed.yaml");
    }

    @Test
    public void verifyV3InvalidNonstring() {
        verifyV3("invalid-nonstring.yaml");
    }

    @Test
    public void verifyV3InvalidEmptyAndWhitespace() {
        verifyV3("invalid-empty-and-whitespace.yaml");
    }

    @Test
    public void verifyV3InvalidWrongCase() {
        verifyV3("invalid-wrong-case.yaml");
    }

    @Test
    public void verifyV3InvalidObjectAndArrayProtocol() {
        verifyV3("invalid-object-and-array-protocol.yaml");
    }

    @Test
    public void verifyV3SkipCases() {
        verifyV3("skip-cases.yaml");
    }

    @Test
    public void verifyV3NoServers() {
        verifyV3("no-servers.yaml");
    }

    @Test
    public void verifyV3NullServers() {
        verifyV3("null-servers.yaml");
    }

    @Test
    public void verifyV3EmptyServersObject() {
        verifyV3("empty-servers-object.yaml");
    }

    @Test
    public void verifyV3EmptyServersArray() {
        verifyV3("empty-servers-array.yaml");
    }

    @Test
    public void verifyV3RefServer() {
        verifyV3("ref-server.yaml");
    }

    @Test
    public void verifyV3RefServerValid() {
        verifyV3("ref-server-valid.yaml");
    }

    @Test
    public void verifyV31Valid() {
        verifyV31("valid.yaml");
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
