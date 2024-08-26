package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR037BidingVersionCheck;

public class AAR037BidingVersionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR037";
        check = new AAR037BidingVersionCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("biding-version.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR037 - BidingVersion - You must specify the version of the biding", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
