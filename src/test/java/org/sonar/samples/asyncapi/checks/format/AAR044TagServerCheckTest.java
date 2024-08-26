package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR044TagServerCheck;

public class AAR044TagServerCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR044";
        check = new AAR044TagServerCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("tag-server.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR044 - TagServer - It is recommended use tags of server", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
