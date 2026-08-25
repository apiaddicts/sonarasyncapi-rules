package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR062SubscribeGroupRequiredCheck;

public class AAR062SubscribeGroupRequiredCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR062";
        check = new AAR062SubscribeGroupRequiredCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
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
    public void verifyV3Valid() {
        verifyV3("valid.yaml");
    }

    @Test
    public void verifyV3Invalid() {
        verifyV3("invalid.yaml");
    }

    @Test
    public void verifyV2InvalidGroupVariants() {
        verifyV2("invalid-group-variants.yaml");
    }

    @Test
    public void verifyV3InvalidGroupVariants() {
        verifyV3("invalid-group-variants.yaml");
    }

    @Test
    public void verifyV2SkipCases() {
        verifyV2("skip-cases.yaml");
    }

    @Test
    public void verifyV3SkipCases() {
        verifyV3("skip-cases.yaml");
    }

    @Test
    public void verifyV2NoChannels() {
        verifyV2("no-channels.yaml");
    }

    @Test
    public void verifyV3NoOperations() {
        verifyV3("no-operations.yaml");
    }

    @Test
    public void verifyV2RefComponentsNoGroup() {
        verifyV2("ref-components-no-group.yaml");
    }

    @Test
    public void verifyV3RefComponentsNoGroup() {
        verifyV3("ref-components-no-group.yaml");
    }

    @Test
    public void verifyV2ComponentsNoGroupUnreferenced() {
        verifyV2("components-no-group-unreferenced.yaml");
    }

    @Test
    public void verifyV3ComponentsNoGroupUnreferenced() {
        verifyV3("components-no-group-unreferenced.yaml");
    }

    @Test
    public void verifyV2ValidGroupIdArray() {
        verifyV2("valid-groupid-array.yaml");
    }

    @Test
    public void verifyV3ValidGroupIdArray() {
        verifyV3("valid-groupid-array.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
                "AAR062 - SubscribeGroupRequired - Subscribe operations must declare the consumer group",
                RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
