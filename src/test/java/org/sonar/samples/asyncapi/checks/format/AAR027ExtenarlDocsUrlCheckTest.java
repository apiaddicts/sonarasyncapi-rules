package org.sonar.samples.asyncapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.format.AAR027ExtenarlDocsUrlCheck;

public class AAR027ExtenarlDocsUrlCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR027";
        check = new AAR027ExtenarlDocsUrlCheck();
        v2Path = getV2Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("external-docs-url.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR027 - ExternalDocsUrl - ExternalDocs must contain a url", RuleType.BUG, Severity.MAJOR, tags("format"));
    }
}
