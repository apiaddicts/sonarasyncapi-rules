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
    public void verifyV2SingleSegment() {
        verifyV2("single-segment.yaml");
    }

    @Test
    public void verifyV2NoChannels() {
        verifyV2("no-channels.yaml");
    }

    @Test
    public void verifyV2NullChannels() {
        verifyV2("null-channels.yaml");
    }

    @Test
    public void verifyV2EmptyChannels() {
        verifyV2("empty-channels.yaml");
    }

    @Test
    public void verifyV2NullChannelValue() {
        verifyV2("null-channel-value.yaml");
    }

    @Test
    public void verifyV2StackOverflow() {
        verifyV2("stack-overflow.yaml");
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
    public void verifyV3SingleSegment() {
        verifyV3("single-segment.yaml");
    }

    @Test
    public void verifyV3NoChannels() {
        verifyV3("no-channels.yaml");
    }

    @Test
    public void verifyV3NullChannels() {
        verifyV3("null-channels.yaml");
    }

    @Test
    public void verifyV3EmptyChannels() {
        verifyV3("empty-channels.yaml");
    }

    @Test
    public void verifyV3MissingAddress() {
        verifyV3("missing-address.yaml");
    }

    @Test
    public void verifyV3NullAddressMatch() {
        verifyV3("null-address-match.yaml");
    }

    @Test
    public void verifyV3NonObjectChannel() {
        verifyV3("non-object-channel.yaml");
    }

    @Test
    public void verifyV3NonStringAddress() {
        verifyV3("non-string-address.yaml");
    }

    @Test
    public void verifyV3EmptyStringAddress() {
        verifyV3("empty-string-address.yaml");
    }

    @Test
    public void verifyV3StackOverflow() {
        verifyV3("stack-overflow.yaml");
    }

    @Test
    public void verifyV31Valid() {
        verifyV31("valid.yaml");
    }

    @Test
    public void verifyV31Missing() {
        verifyV31("missing.yaml");
    }

    @Test
    public void verifyV31NoChannels() {
        verifyV31("no-channels.yaml");
    }

    @Test
    public void verifyV31NullChannels() {
        verifyV31("null-channels.yaml");
    }

    @Test
    public void verifyV31MissingAddress() {
        verifyV31("missing-address.yaml");
    }

    @Test
    public void verifyV31NullAddressMatch() {
        verifyV31("null-address-match.yaml");
    }

    @Test
    public void verifyV31SingleSegment() {
        verifyV31("single-segment.yaml");
    }

    @Test
    public void verifyV31EmptyChannels() {
        verifyV31("empty-channels.yaml");
    }

    @Test
    public void verifyV31NonObjectChannel() {
        verifyV31("non-object-channel.yaml");
    }

    @Test
    public void verifyV31NonStringAddress() {
        verifyV31("non-string-address.yaml");
    }

    @Test
    public void verifyV31EmptyStringAddress() {
        verifyV31("empty-string-address.yaml");
    }

    @Test
    public void verifyV31StackOverflow() {
        verifyV31("stack-overflow.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR057 - ErrorTopicDocumented - At least one channel must be documented as an error topic",
                RuleType.BUG, Severity.MAJOR, tags("operations"));
    }
}
