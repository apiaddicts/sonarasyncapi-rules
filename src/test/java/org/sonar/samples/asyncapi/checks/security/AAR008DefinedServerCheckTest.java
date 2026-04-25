package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR008DefinedServerCheck;

public class AAR008DefinedServerCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR008";
        check = new AAR008DefinedServerCheck();
        v2Path = getV2Path("security");
    }
    @Test
    public void verifyInV2WithoutServers() {
        verifyV2("without-servers.yaml");
    }

    
    // ============= V3.0+ Tests =============

    // --- V3 Tests ---
    @Test
    public void verifyV3WithMultipleServers() {
        verifyV3("with-multiple-servers.yaml");
    }
    @Test
    public void verifyV3WithServers() {
        verifyV3("with-servers.yaml");
    }
    @Test
    public void verifyV3WithoutServers() {
        verifyV3("without-servers.yaml");
    }

    // --- V31 Tests ---
    @Test
    public void verifyV31WithMultipleServers() {
        verifyV31("with-multiple-servers.yaml");
    }
    @Test
    public void verifyV31WithServers() {
        verifyV31("with-servers.yaml");
    }
    @Test
    public void verifyV31WithoutServers() {
        verifyV31("without-servers.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR008 - DefinedServer - The servers should be defined", RuleType.VULNERABILITY, Severity.CRITICAL, tags("safety"));
    }
}
