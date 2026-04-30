package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR001MandatoryHttpsProtocolCheck;

public class AAR001MandatoryHttpsProtocolCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR001";
        check = new AAR001MandatoryHttpsProtocolCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
        v31Path = getV31Path("security");
    }

    @Test
    public void verifyInV2WithServer() {
        verifyV2("with-servers.yaml");
    }

    @Test
    public void verifyInV2WithHttps() {
        verifyV2("with-https.yaml");
    }

    @Test
    public void verifyInV2WithKafka() {
        verifyV2("with-kafka.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithAmqps() {
        verifyV3("with-amqps.yaml");
    }
    @Test
    public void verifyV3WithHttpProtocol() {
        verifyV3("with-http-protocol.yaml");
    }
    @Test
    public void verifyV3WithHttps() {
        verifyV3("with-https.yaml");
    }
    @Test
    public void verifyV3WithMqttInsecure() {
        verifyV3("with-mqtt-insecure.yaml");
    }
    @Test
    public void verifyV3WithMqttTls() {
        verifyV3("with-mqtt-tls.yaml");
    }
    @Test
    public void verifyV3WithWssProtocol() {
        verifyV3("with-wss-protocol.yaml");
    }
    @Test
    public void verifyV3WithoutSecureProtocol() {
        verifyV3("without-secure-protocol.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithAmqps() {
        verifyV31("with-amqps.yaml");
    }
    @Test
    public void verifyV31WithHttpProtocol() {
        verifyV31("with-http-protocol.yaml");
    }
    @Test
    public void verifyV31WithHttps() {
        verifyV31("with-https.yaml");
    }
    @Test
    public void verifyV31WithMqttInsecure() {
        verifyV31("with-mqtt-insecure.yaml");
    }
    @Test
    public void verifyV31WithMqttTls() {
        verifyV31("with-mqtt-tls.yaml");
    }
    @Test
    public void verifyV31WithWssProtocol() {
        verifyV31("with-wss-protocol.yaml");
    }
    @Test
    public void verifyV31WithoutSecureProtocol() {
        verifyV31("without-secure-protocol.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR001 - MandatoryHttpsProtocol - Https protocol is mandatory", RuleType.VULNERABILITY, Severity.CRITICAL, tags("safety"));
    }
}
