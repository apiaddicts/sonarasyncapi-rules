# AsyncAPI 3.x Implementation Code Examples

This document provides detailed code examples for updating existing SonarQube rules to support AsyncAPI 3.0, 3.1, and 3.2.

## Table of Contents

1. [Version Detection Pattern](#version-detection-pattern)
2. [Security Checks](#security-checks)
3. [Operations Checks](#operations-checks)
4. [Format Checks](#format-checks)
5. [Schema Checks](#schema-checks)
6. [Test Updates](#test-updates)

---

## Version Detection Pattern

### Basic Version Detection

```java
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;

AsyncAPIVersionDetector.AsyncAPIVersion version = 
    AsyncAPIVersionDetector.detectVersion(rootNode);

if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
    // Handle AsyncAPI 2.6
} else if (AsyncAPIVersionDetector.isVersion3Plus(rootNode)) {
    // Handle AsyncAPI 3.x
}
```

### In Check Classes

```java
@Override
public void visitNode(JsonNode node) {
    AsyncAPIVersionDetector.AsyncAPIVersion version = 
        AsyncAPIVersionDetector.detectVersion(node);
    
    // Rest of check logic...
}
```

---

## Security Checks

### AAR001 - Mandatory HTTPS/WSS Protocol Check

**Changes in v3.x**: Servers are now an array instead of a map.

```java
package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Rule(key = AAR001MandatoryHttpsProtocolCheck.KEY)
public class AAR001MandatoryHttpsProtocolCheck extends BaseCheck {

    public static final String KEY = "AAR001";
    private static final Set<String> SECURE_PROTOCOLS = 
        new HashSet<>(Arrays.asList("wss", "https", "amqps", "mqtt+tls", "secure-mqtt"));

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.ROOT);
    }

    @Override
    public void visitNode(JsonNode node) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(node);

        JsonNode serversNode = node.get("servers");
        if (serversNode.isMissing()) {
            return;
        }

        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            validateV2Servers(serversNode);
        } else if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
            validateV3Servers(serversNode);
        }
    }

    private void validateV2Servers(JsonNode serversNode) {
        serversNode.elements().forEach(server -> {
            JsonNode protocol = server.get("protocol");
            if (!protocol.isMissing() && !isSecureProtocol(protocol.stringValue())) {
                addIssue(KEY, translate("AAR001.error"), protocol);
            }
        });
    }

    private void validateV3Servers(JsonNode serversNode) {
        serversNode.elements().forEach(server -> {
            JsonNode protocol = server.get("protocol");
            if (!protocol.isMissing() && !isSecureProtocol(protocol.stringValue())) {
                addIssue(KEY, translate("AAR001.error"), server);
            }
        });
    }

    private boolean isSecureProtocol(String protocol) {
        return protocol != null && SECURE_PROTOCOLS.contains(protocol.toLowerCase());
    }
}
```

### AAR008 - Defined Server Check

**Already updated in the main files - see [AAR008DefinedServerCheck.java]**

```java
@Override
public void visitNode(JsonNode node) {
    AsyncAPIVersionDetector.AsyncAPIVersion version = 
        AsyncAPIVersionDetector.detectVersion(node);
    JsonNode serversNode = node.get("servers");

    if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
        if (serversNode.isMissing() || serversNode.isNull()) {
            addIssue(KEY, translate("AAR008.error-v2-servers"), serversNode.key());
        }
    } else if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
        if (serversNode.isMissing() || serversNode.isNull()) {
            addIssue(KEY, translate("AAR008.error-v3-servers"), serversNode.key());
        }
    }
}
```

### AAR018 - Security Schemes Check

**Changes in v3.x**: Security scheme types may have different structures.

```java
@Rule(key = AAR018SecuritySchemasCheck.KEY)
public class AAR018SecuritySchemasCheck extends BaseCheck {

    public static final String KEY = "AAR018";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.SECURITY_SCHEME);
    }

    @Override
    public void visitNode(JsonNode node) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(getRootNode(node));

        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            validateV2SecurityScheme(node);
        } else if (AsyncAPIVersionDetector.isVersion3Plus(getRootNode(node))) {
            validateV3SecurityScheme(node);
        }
    }

    private void validateV2SecurityScheme(JsonNode scheme) {
        JsonNode type = scheme.get("type");
        if (type.isMissing() || !isValidSecurityType(type.stringValue())) {
            addIssue(KEY, translate("AAR018.error"), type);
        }
    }

    private void validateV3SecurityScheme(JsonNode scheme) {
        JsonNode type = scheme.get("type");
        if (type.isMissing()) {
            addIssue(KEY, translate("AAR018.error"), scheme);
            return;
        }

        String secType = type.stringValue();
        if (!isValidSecurityType(secType)) {
            addIssue(KEY, translate("AAR018.error"), type);
        }

        // V3 specific: oauth2 flows structure changed
        if ("oauth2".equals(secType)) {
            validateOAuth2Flows(scheme);
        }
    }

    private void validateOAuth2Flows(JsonNode scheme) {
        // V2: flows object with implicit, password, clientCredentials, authorizationCode
        // V3: flows structure is similar but flow names may vary
        JsonNode flows = scheme.get("flows");
        if (!flows.isMissing() && flows.elements().isEmpty()) {
            addIssue(KEY, translate("AAR018.error-oauth2-flows"), flows);
        }
    }

    private boolean isValidSecurityType(String type) {
        Set<String> validTypes = new HashSet<>(Arrays.asList(
            "apiKey", "http", "oauth2", "openIdConnect"
        ));
        return validTypes.contains(type);
    }
}
```

---

## Operations Checks

### AAR009 - Declared Tag Check

**Changes in v3.x**: Operations object is at root level, separate from channels.

```java
@Rule(key = AAR009DeclaredTagCheck.CHECK_KEY)
public class AAR009DeclaredTagCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR009";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.OPERATION);
    }

    @Override
    protected void visitNode(JsonNode node) {
        // This works for both V2 and V3 since the grammar parser
        // handles version-specific navigation
        JsonNode tagsArray = node.at("/tags").value();
        if (tagsArray.isMissing()) {
            addIssue(CHECK_KEY, translate("AAR009.error"), node.key());
        }
    }
}
```

### AAR040 - Defined Channel Servers Check

**Major update for v3.x**: In v3, servers can be defined at root, channel, or operation level.

```java
@Rule(key = AAR040DefinedChannelServersCheck.KEY)
public class AAR040DefinedChannelServersCheck extends BaseCheck {

    public static final String KEY = "AAR040";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.CHANNEL);
    }

    @Override
    public void visitNode(JsonNode node) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(getRootNode(node));

        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            // In V2, channels inherit servers from root
            // Just validate that root has servers (AAR008 handles this)
            validateV2Channel(node);
        } else if (AsyncAPIVersionDetector.isVersion3Plus(getRootNode(node))) {
            validateV3Channel(node);
        }
    }

    private void validateV2Channel(JsonNode channel) {
        // V2: servers are at root level only
        JsonNode root = getRootNode(channel);
        JsonNode rootServers = root.get("servers");
        if (rootServers.isMissing()) {
            addIssue(KEY, translate("AAR040.error-no-servers"), channel);
        }
    }

    private void validateV3Channel(JsonNode channel) {
        // V3: servers can be at channel level, operation level, or inherited from root
        JsonNode servers = channel.get("servers");
        if (!servers.isMissing() && !servers.isNull()) {
            return; // Channel has servers
        }

        // Check if operations have servers
        JsonNode root = getRootNode(channel);
        JsonNode channelAddress = channel.get("address");
        if (!channelAddress.isMissing()) {
            String address = channelAddress.stringValue();
            JsonNode operations = root.get("operations");
            if (!operations.isMissing()) {
                boolean hasServerInOperation = operations.elements().stream()
                    .filter(op -> address.equals(op.get("channel").stringValue()))
                    .anyMatch(op -> !op.get("servers").isMissing());
                if (hasServerInOperation) {
                    return;
                }
            }
        }

        // Check root servers
        JsonNode rootServers = root.get("servers");
        if (rootServers.isMissing() || rootServers.isNull()) {
            addIssue(KEY, translate("AAR040.error-v3-servers"), channel);
        }
    }

    private JsonNode getRootNode(JsonNode node) {
        // Navigate to root - implementation depends on JsonNode API
        return node;
    }
}
```

---

## Format Checks

### AAR012 - Declared Operation ID Check

**Works the same for both versions** since operation IDs are still required.

```java
@Rule(key = AAR012DeclaredOperationIDCheck.KEY)
public class AAR012DeclaredOperationIDCheck extends BaseCheck {
    public static final String KEY = "AAR012";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.OPERATION);
    }

    @Override
    protected void visitNode(JsonNode node) {
        JsonNode operationId = node.get("operationId");
        if (operationId.isMissing()) {
            addIssue(KEY, translate("AAR012.error"), node.key());
        }
    }
}
```

### AAR021 - Provide Operation Summary Check

**Works the same for both versions** since operations still need summaries.

```java
@Rule(key = AAR021ProvideOpSummaryCheck.KEY)
public class AAR021ProvideOpSummaryCheck extends BaseCheck {
    public static final String KEY = "AAR021";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.OPERATION);
    }

    @Override
    protected void visitNode(JsonNode node) {
        JsonNode summary = node.get("summary");
        if (summary.isMissing()) {
            addIssue(KEY, translate("AAR021.error"), node.key());
        }
    }
}
```

---

## Schema Checks

### AAR026 - Message Schemas Check

**Changes in v3.x**: Message structure moved from operation.message to channel.messages.

```java
@Rule(key = AAR026MessageSchemasCheck.KEY)
public class AAR026MessageSchemasCheck extends BaseCheck {
    public static final String KEY = "AAR026";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.MESSAGE);
    }

    @Override
    public void visitNode(JsonNode node) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(getRootNode(node));

        JsonNode payload = MessageNavigator.getMessagePayload(node);
        if (payload == null || payload.isMissing()) {
            addIssue(KEY, translate("AAR026.error-missing-schema"), node.key());
            return;
        }

        validateSchema(payload);
    }

    private void validateSchema(JsonNode schema) {
        JsonNode type = schema.get("type");
        if (type.isMissing()) {
            addIssue(KEY, translate("AAR026.error-missing-type"), schema);
        }
    }

    private JsonNode getRootNode(JsonNode node) {
        // Implement as needed
        return node;
    }
}
```

---

## Test Updates

### Update Test Classes to Support v3.0 and v3.1

```java
package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR008DefinedServerCheck;

public class AAR008DefinedServerCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR008";
        check = new AAR008DefinedServerCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
        v31Path = getV31Path("security");
    }

    @Test
    public void verifyV2WithoutServers() {
        verifyV2("without-servers.yaml");
    }

    @Test
    public void verifyV3WithoutServers() {
        verifyV3("without-servers.yaml");
    }

    @Test
    public void verifyV3WithServers() {
        verifyV3("with-servers.yaml");
    }

    @Test
    public void verifyV31WithoutServers() {
        verifyV31("without-servers.yaml");
    }

    @Test
    public void verifyV31WithServers() {
        verifyV31("with-servers.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
            "AAR008 - DefinedServer - The servers should be defined",
            RuleType.VULNERABILITY,
            Severity.CRITICAL,
            tags("safety")
        );
    }
}
```

### Message Navigation in Tests

```java
// When testing message-related checks
JsonNode message = MessageNavigator.getMessageFromOperation(operation, channel);
JsonNode payload = MessageNavigator.getMessagePayload(message);

// For multiple messages
java.util.List<JsonNode> allMessages = MessageNavigator.getAllMessages(channel);
```

---

## Common Patterns

### Pattern 1: Version-Dependent Validation

```java
@Override
public void visitNode(JsonNode node) {
    AsyncAPIVersionDetector.AsyncAPIVersion version = 
        AsyncAPIVersionDetector.detectVersion(node);
    
    switch (version) {
        case V2:
            validateForV2(node);
            break;
        case V3:
        case V31:
        case V32:
            validateForV3Plus(node);
            break;
        default:
            // Unknown version, skip validation
            break;
    }
}
```

### Pattern 2: Using Navigator Utilities

```java
// For operations
OperationNavigator.processOperations(rootNode, (operation, key, channel) -> {
    JsonNode tags = operation.get("tags");
    if (tags.isMissing()) {
        // Report issue
    }
});

// For channels
OperationNavigator.processChannels(rootNode, channel -> {
    JsonNode address = channel.get("address");
    if (address.isMissing()) {
        // Report issue
    }
});
```

### Pattern 3: Protocol Validation

```java
private boolean isValidProtocol(String protocol, String type) {
    switch (type) {
        case "websocket":
            return "ws".equals(protocol) || "wss".equals(protocol);
        case "mqtt":
            return protocol.startsWith("mqtt");
        case "amqp":
            return "amqp".equals(protocol) || "amqps".equals(protocol);
        default:
            return false;
    }
}
```

---

## Migration Checklist for Each Check

- [ ] Identify which version-specific changes affect this check
- [ ] Create version detection logic at start of visitNode
- [ ] Implement separate validation for V2 and V3+
- [ ] Update error messages to be version-aware
- [ ] Create test files for V3.0 and V3.1
- [ ] Update test class with new test methods
- [ ] Verify backward compatibility with V2.6 tests
- [ ] Update rule description if needed

---

## Resources

- [AsyncAPI Grammar Reference](https://github.com/apiaddicts/asyncapi-soslr-yaml)
- [JsonNode API Documentation](https://github.com/apiaddicts/dosonarapi)
- [Existing Tests](../src/test/java)
