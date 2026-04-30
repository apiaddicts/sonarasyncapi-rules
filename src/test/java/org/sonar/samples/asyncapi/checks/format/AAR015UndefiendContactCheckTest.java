package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR015UndefiendContactCheck;

public class AAR015UndefiendContactCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR015";
        check = new AAR015UndefiendContactCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
        v31Path = getV31Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("undefined-contact.yaml");
    }

    
    // ============= V3.0+ Tests =============

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR015 - UndefinedContact - Api should indicate the contact in the info object", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
