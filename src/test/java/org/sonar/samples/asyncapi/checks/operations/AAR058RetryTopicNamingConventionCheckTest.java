package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR058RetryTopicNamingConventionCheck;

public class AAR058RetryTopicNamingConventionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR058";
        check = new AAR058RetryTopicNamingConventionCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
        v31Path = getV31Path("operations");
    }

    @Test
    public void verifyV2RetryValid() {
        verifyV2("retry-valid.yaml");
    }

    @Test
    public void verifyV2RetryInvalidNonNumeric() {
        verifyV2("retry-invalid-non-numeric.yaml");
    }

    @Test
    public void verifyV2RetryInvalidUnderscore() {
        verifyV2("retry-invalid-underscore.yaml");
    }

    @Test
    public void verifyV2RetryInvalidExtraSegment() {
        verifyV2("retry-invalid-extra-segment.yaml");
    }

    @Test
    public void verifyV2RetryInvalidTooFewSegments() {
        verifyV2("retry-invalid-too-few-segments.yaml");
    }

    @Test
    public void verifyV2RetryInvalidMissingNumber() {
        verifyV2("retry-invalid-missing-number.yaml");
    }

    @Test
    public void verifyV2NotARetryChannel() {
        verifyV2("retry-not-a-retry-channel.yaml");
    }

    @Test
    public void verifyV3RetryValid() {
        verifyV3("retry-valid.yaml");
    }

    @Test
    public void verifyV3RetryInvalidNonNumeric() {
        verifyV3("retry-invalid-non-numeric.yaml");
    }

    @Test
    public void verifyV3RetryInvalidUnderscore() {
        verifyV3("retry-invalid-underscore.yaml");
    }

    @Test
    public void verifyV3RetryInvalidExtraSegment() {
        verifyV3("retry-invalid-extra-segment.yaml");
    }

    @Test
    public void verifyV3RetryInvalidTooFewSegments() {
        verifyV3("retry-invalid-too-few-segments.yaml");
    }

    @Test
    public void verifyV3RetryInvalidMissingNumber() {
        verifyV3("retry-invalid-missing-number.yaml");
    }

    @Test
    public void verifyV3NotARetryChannel() {
        verifyV3("retry-not-a-retry-channel.yaml");
    }

    @Test
    public void verifyV3RetryNullAddressSkipped() {
        verifyV3("retry-null-address.yaml");
    }

    @Test
    public void verifyV31RetryValid() {
        verifyV31("retry-valid.yaml");
    }

    @Test
    public void verifyV31RetryInvalid() {
        verifyV31("retry-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR058 - RetryTopicNamingConvention - Retry channels must follow the retry-topic naming convention",
                RuleType.BUG, Severity.MINOR, tags("operations"));
    }
}
