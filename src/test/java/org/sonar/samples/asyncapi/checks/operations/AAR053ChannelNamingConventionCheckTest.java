package org.sonar.samples.asyncapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.operations.AAR053ChannelNamingConventionCheck;

public class AAR053ChannelNamingConventionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR053";
        check = new AAR053ChannelNamingConventionCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
        v31Path = getV31Path("operations");
    }

    @Test
    public void verifyV2ChannelNamingValid() {
        verifyV2("channel-naming-valid.yaml");
    }

    @Test
    public void verifyV2ChannelNamingValidWithVersion() {
        verifyV2("channel-naming-valid-with-version.yaml");
    }

    @Test
    public void verifyV2ChannelNamingInvalidUnderscore() {
        verifyV2("channel-naming-invalid-underscore.yaml");
    }

    @Test
    public void verifyV2ChannelNamingInvalidSegmentCount() {
        verifyV2("channel-naming-invalid-segment-count.yaml");
    }

    @Test
    public void verifyV2ChannelNamingInvalidHyphen() {
        verifyV2("channel-naming-invalid-hyphen.yaml");
    }

    @Test
    public void verifyV3ChannelNamingValid() {
        verifyV3("channel-naming-valid.yaml");
    }

    @Test
    public void verifyV3ChannelNamingInvalid() {
        verifyV3("channel-naming-invalid.yaml");
    }

    @Test
    public void verifyV3ChannelNamingNoAddressValid() {
        verifyV3("channel-naming-no-address-valid.yaml");
    }

    @Test
    public void verifyV3ChannelNamingNoAddressInvalid() {
        verifyV3("channel-naming-no-address-invalid.yaml");
    }

    @Test
    public void verifyV3ChannelNamingNullAddressSkipped() {
        verifyV3("channel-naming-null-address.yaml");
    }

    @Test
    public void verifyV31ChannelNamingValid() {
        verifyV31("channel-naming-valid.yaml");
    }

    @Test
    public void verifyV31ChannelNamingInvalid() {
        verifyV31("channel-naming-invalid.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("AAR053 - ChannelNamingConvention - Channel name must follow the Kafka topic naming convention", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("pattern", AAR053ChannelNamingConventionCheck.DEFAULT_PATTERN, RuleParamType.STRING);
    }
}
