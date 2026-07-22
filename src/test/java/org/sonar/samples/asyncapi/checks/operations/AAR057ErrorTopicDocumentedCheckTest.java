package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR057ErrorTopicDocumentedCheck;

public class AAR057ErrorTopicDocumentedCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR057";
        check = new AAR057ErrorTopicDocumentedCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
        v31Path = getV31Path("operations");
    }

    @Test
    public void verifyV2Valid() {
        verifyV2("valid.yaml");
    }

    @Test
    public void verifyV2Missing() {
        verifyV2("missing.yaml");
    }

    @Test
    public void verifyV3Valid() {
        verifyV3("valid.yaml");
    }

    @Test
    public void verifyV3Missing() {
        verifyV3("missing.yaml");
    }

    @Test
    public void verifyV31Valid() {
        verifyV31("valid.yaml");
    }

    @Test
    public void verifyV31Missing() {
        verifyV31("missing.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR057 - ErrorTopicDocumented - At least one channel must be documented as an error topic",
                RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
