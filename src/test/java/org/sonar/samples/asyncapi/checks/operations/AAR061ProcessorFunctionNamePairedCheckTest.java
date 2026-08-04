package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR061ProcessorFunctionNamePairedCheck;

public class AAR061ProcessorFunctionNamePairedCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR061";
        check = new AAR061ProcessorFunctionNamePairedCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyV2Paired() {
        verifyV2("paired-valid.yaml");
    }

    @Test
    public void verifyV2Unpaired() {
        verifyV2("unpaired-invalid.yaml");
    }

    @Test
    public void verifyV3Paired() {
        verifyV3("paired-valid.yaml");
    }

    @Test
    public void verifyV3Unpaired() {
        verifyV3("unpaired-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
                "AAR061 - ProcessorFunctionNamePaired - Paired processor operations must share x-scs-function-name",
                RuleType.BUG, Severity.MINOR, tags("operations"));
    }
}
