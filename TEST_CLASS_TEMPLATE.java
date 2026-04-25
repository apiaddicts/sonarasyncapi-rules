// Template for comprehensive test class with V2.6, V3.0, V3.1, V3.2 support

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
        // Optional: v32Path if you need 3.2 specific tests
    }

    // ============= V2.6 Tests =============
    @Test
    public void verifyV2WithHttpsProtocol() {
        verifyV2("with-https.yaml");
    }

    @Test
    public void verifyV2WithHttpsProtocol_json() {
        verifyV2("with-https.json");
    }

    // Add more V2 tests...

    // ============= V3.0 Tests =============
    @Test
    public void verifyV3WithHttpsProtocol() {
        verifyV3("with-https.yaml");
    }

    @Test
    public void verifyV3WithAmqpsProtocol() {
        verifyV3("with-amqps.yaml");
    }

    @Test
    public void verifyV3WithMqttTls() {
        verifyV3("with-mqtt-tls.yaml");
    }

    @Test
    public void verifyV3WithInsecureProtocol() {
        verifyV3("without-secure-protocol.yaml");
    }

    @Test
    public void verifyV3WithMqttInsecure() {
        verifyV3("with-mqtt-insecure.yaml");
    }

    // ============= V3.1 Tests =============
    @Test
    public void verifyV31WithHttpsProtocol() {
        verifyV31("with-https.yaml");
    }

    @Test
    public void verifyV31WithAmqpsProtocol() {
        verifyV31("with-amqps.yaml");
    }

    @Test
    public void verifyV31WithMqttTls() {
        verifyV31("with-mqtt-tls.yaml");
    }

    @Test
    public void verifyV31WithInsecureProtocol() {
        verifyV31("without-secure-protocol.yaml");
    }

    @Test
    public void verifyV31WithMqttInsecure() {
        verifyV31("with-mqtt-insecure.yaml");
    }

    // ============= Rule Definition Test =============
    @Override
    public void verifyRule() {
        assertRuleProperties(
            "AAR001 - MandatoryHttpsProtocol - Servers should use secure protocols",
            RuleType.VULNERABILITY,
            Severity.CRITICAL,
            tags("safety")
        );
    }
}
