# AsyncAPI 3.x Migration Guide for SonarQube Rules

## Overview

This guide provides a comprehensive approach to update the SonarQube AsyncAPI rules project to support AsyncAPI 3.0, 3.1, and 3.2 while maintaining backward compatibility with 2.6.

## Key Schema Changes: 2.6 → 3.x

### 1. **Servers Structure**
- **AsyncAPI 2.6**: `servers` is a **map** (key-value pairs)
  ```yaml
  servers:
    production:
      url: wss://example.com
      protocol: ws
  ```

- **AsyncAPI 3.x**: `servers` is an **array** (list of objects)
  ```yaml
  servers:
    - host: example.com
      protocol: ws
      pathname: /ws
  ```

### 2. **Operations Object**
- **AsyncAPI 2.6**: Operations defined within `channels` with `publish`/`subscribe`
  ```yaml
  channels:
    user/signup:
      publish:
        operationId: userSignup
        message: ...
  ```

- **AsyncAPI 3.x**: Dedicated `operations` object (optional, replaces inline ops)
  ```yaml
  channels:
    user/signup:
      address: user/signup
  operations:
    UserSignup:
      action: send
      channel: user/signup
  ```

### 3. **Channels Address**
- **AsyncAPI 2.6**: Channel key implicitly defines address
- **AsyncAPI 3.x**: Explicit `address` field in each channel (optional)
  ```yaml
  channels:
    user/events:
      address: user/events
      messages:
        UserEvent:
          payload: ...
  ```

### 4. **Tags Location**
- **AsyncAPI 2.6**: Tags at root level or operation level
- **AsyncAPI 3.x**: Tags at root level, server level, operation level, or message level

### 5. **Security Schemes**
- **AsyncAPI 2.6**: `securitySchemes` in `components`
- **AsyncAPI 3.x**: Same location but may have new types (e.g., `oauth2` flow changes)

## Implementation Strategy

### Phase 1: Add Version Detection Utility

Create a utility class to detect AsyncAPI version:

```java
// File: AsyncAPIVersionDetector.java
package apiquality.sonar.asyncapi.utils;

import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class AsyncAPIVersionDetector {
    
    public enum AsyncAPIVersion {
        V2(2.6),
        V3(3.0),
        V31(3.1),
        V32(3.2),
        UNKNOWN(-1);
        
        private final double version;
        
        AsyncAPIVersion(double version) {
            this.version = version;
        }
        
        public double getVersion() {
            return version;
        }
    }
    
    public static AsyncAPIVersion detectVersion(JsonNode rootNode) {
        JsonNode asyncapiNode = rootNode.get("asyncapi");
        if (asyncapiNode.isMissing() || asyncapiNode.isNull()) {
            return AsyncAPIVersion.UNKNOWN;
        }
        
        String version = asyncapiNode.stringValue();
        if (version == null) {
            return AsyncAPIVersion.UNKNOWN;
        }
        
        if (version.startsWith("2.")) {
            return AsyncAPIVersion.V2;
        } else if (version.startsWith("3.0")) {
            return AsyncAPIVersion.V3;
        } else if (version.startsWith("3.1")) {
            return AsyncAPIVersion.V31;
        } else if (version.startsWith("3.2")) {
            return AsyncAPIVersion.V32;
        }
        
        return AsyncAPIVersion.UNKNOWN;
    }
    
    public static boolean isVersion2(JsonNode rootNode) {
        return detectVersion(rootNode) == AsyncAPIVersion.V2;
    }
    
    public static boolean isVersion3OrLater(JsonNode rootNode) {
        AsyncAPIVersion version = detectVersion(rootNode);
        return version != AsyncAPIVersion.V2 && version != AsyncAPIVersion.UNKNOWN;
    }
    
    public static boolean isVersion3Plus(JsonNode rootNode) {
        AsyncAPIVersion version = detectVersion(rootNode);
        return version == AsyncAPIVersion.V3 || version == AsyncAPIVersion.V31 || version == AsyncAPIVersion.V32;
    }
}
```

### Phase 2: Update Core Checks

#### A. Server-Related Checks (AAR001, AAR008, AAR040, AAR041)

**Example: Update AAR008DefinedServerCheck**

```java
package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = AAR008DefinedServerCheck.KEY)
public class AAR008DefinedServerCheck extends BaseCheck {

    public static final String KEY = "AAR008";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.ROOT);
    }

    @Override
    public void visitNode(JsonNode node) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(node);
        
        JsonNode serversNode = node.get("servers");
        
        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            // V2: servers should be a map
            if (serversNode.isMissing() || serversNode.isNull()) {
                addIssue(KEY, translate("AAR008.error-v2-servers"), serversNode.key());
            }
        } else if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
            // V3+: servers should be an array
            if (serversNode.isMissing() || serversNode.isNull()) {
                addIssue(KEY, translate("AAR008.error-v3-servers"), serversNode.key());
            }
        }
    }
}
```

#### B. Operation/Channel Navigation

**For version 3.x, operations moved to root level:**

```java
public class OperationNavigator {
    
    public static void processOperations(JsonNode rootNode, OperationProcessor processor) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(rootNode);
        
        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            // V2: operations within channels
            processV2Operations(rootNode, processor);
        } else if (AsyncAPIVersionDetector.isVersion3Plus(rootNode)) {
            // V3+: dedicated operations object
            processV3Operations(rootNode, processor);
        }
    }
    
    private static void processV2Operations(JsonNode root, OperationProcessor processor) {
        JsonNode channels = root.get("channels");
        if (!channels.isMissing()) {
            channels.elements().forEachRemaining(channel -> {
                JsonNode publish = channel.get("publish");
                JsonNode subscribe = channel.get("subscribe");
                
                if (!publish.isMissing()) {
                    processor.processOperation(publish, "publish", channel);
                }
                if (!subscribe.isMissing()) {
                    processor.processOperation(subscribe, "subscribe", channel);
                }
            });
        }
    }
    
    private static void processV3Operations(JsonNode root, OperationProcessor processor) {
        JsonNode operations = root.get("operations");
        if (!operations.isMissing()) {
            operations.elements().forEachRemaining(operation -> {
                // Get channel reference from operation
                JsonNode channelRef = operation.get("channel");
                JsonNode channels = root.get("channels");
                JsonNode channel = null;
                
                if (!channelRef.isMissing() && !channels.isMissing()) {
                    channel = channels.get(channelRef.stringValue());
                }
                
                processor.processOperation(operation, operation.key(), channel);
            });
        }
    }
    
    public interface OperationProcessor {
        void processOperation(JsonNode operation, String operationKey, JsonNode channel);
    }
}
```

### Phase 3: Message Access Patterns

**Handle message location differences:**

```java
public class MessageNavigator {
    
    public static JsonNode getMessageNode(JsonNode operationNode, JsonNode channelNode) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = 
            AsyncAPIVersionDetector.detectVersion(getRootNode(operationNode));
        
        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            // V2: message directly in operation
            JsonNode message = operationNode.get("message");
            if (!message.isMissing()) {
                return message;
            }
        } else if (AsyncAPIVersionDetector.isVersion3Plus(operationNode)) {
            // V3+: message in channel.messages or via $ref
            if (channelNode != null) {
                JsonNode messages = channelNode.get("messages");
                if (!messages.isMissing()) {
                    // Get first message or handle multiple
                    return messages.elements().hasNext() ? 
                        messages.elements().next() : JsonNode.MISSING;
                }
            }
        }
        
        return JsonNode.MISSING;
    }
    
    private static JsonNode getRootNode(JsonNode node) {
        // Navigate up to root - implementation depends on JsonNode API
        return node;
    }
}
```

### Phase 4: Rule-Specific Updates

#### Tags-Related Rules (AAR009, AAR010)

**Changes for 3.x:**
- Tags can be at operation level (same as V2)
- But operations are now separate objects, not nested in channels

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
        // This check works for both V2 and V3 since it operates on OPERATION nodes
        // The grammar parser handles version-specific navigation
        JsonNode tagsArray = node.at("/tags").value();
        if (tagsArray.isMissing()) {
            addIssue(CHECK_KEY, translate("AAR009.error"), node.key());
        }
    }
}
```

#### Channel Server Checks (AAR040, AAR041)

**AAR040DefinedChannelServersCheck - Needs Major Update:**

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
            detectVersion(getRootNode(node));
        
        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            // V2: servers inherited from root or channel.servers (not in spec, actually)
            // Just validate that global servers exist (AAR008 checks this)
        } else if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
            // V3+: Check for servers in channel or operation binding
            JsonNode servers = node.get("servers");
            JsonNode bindings = node.get("bindings");
            
            if ((servers.isMissing() || servers.isNull()) &&
                (bindings.isMissing() || !hasServerInBindings(bindings))) {
                addIssue(KEY, translate("AAR040.error-v3"), node.key());
            }
        }
    }
    
    private boolean hasServerInBindings(JsonNode bindings) {
        // Check for server references in protocol bindings
        return !bindings.isMissing() && 
               bindings.elements().anyMatch(binding -> 
                   !binding.get("servers").isMissing()
               );
    }
}
```

### Phase 5: Test Structure

**Create test files for v3.0, v3.1, v3.2:**

Test files should be organized as:
- `src/test/resources/checks/v3/security/AAR008/`
- `src/test/resources/checks/v31/security/AAR008/`

Example AsyncAPI 3.0 test file:

```yaml
# File: src/test/resources/checks/v3/security/AAR008/with-servers.yaml
asyncapi: 3.0.0
info:
  title: AsyncAPI Example
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: ws
    pathname: /ws
channels:
  userSignup:
    address: user/signup
    messages:
      UserSignup:
        payload:
          type: object
```

```yaml
# File: src/test/resources/checks/v3/security/AAR008/without-servers.yaml
# Noncompliant {{AAR008: Define 'servers' is mandatory}}
asyncapi: 3.0.0
info:
  title: AsyncAPI Example
  version: 1.0.0
channels:
  userSignup:
    address: user/signup
```

## Migration Checklist

- [ ] **Dependency Updates**
  - [ ] Update asyncapi-parser to version supporting 3.x (if separate)
  - [ ] Update asyncapi-front-end version in pom.xml

- [ ] **Core Utilities**
  - [ ] Implement AsyncAPIVersionDetector
  - [ ] Implement OperationNavigator
  - [ ] Implement MessageNavigator
  - [ ] Add helper utilities to BaseCheck

- [ ] **Security Checks** (AAR001, AAR008, AAR018, AAR043)
  - [ ] Update server validation logic
  - [ ] Update protocol validation (new MQTT 5.0, etc.)
  - [ ] Update security scheme validation

- [ ] **Format Checks** (AAR011, AAR012, AAR021, etc.)
  - [ ] Verify backward compatibility
  - [ ] Add version-specific error messages

- [ ] **Operations Checks** (AAR009, AAR010, AAR040, AAR041)
  - [ ] Handle new operations object structure
  - [ ] Update channel navigation logic
  - [ ] Update server binding checks

- [ ] **Schema Checks** (AAR019, AAR024, AAR026)
  - [ ] Verify message location handling
  - [ ] Update schema validation paths

- [ ] **Examples Checks** (AAR031)
  - [ ] Update to handle v3 message examples

- [ ] **Testing**
  - [ ] Create v3.0 test fixtures
  - [ ] Create v3.1 test fixtures
  - [ ] Create v3.2 test fixtures
  - [ ] Verify v2.6 backward compatibility
  - [ ] Run full test suite

- [ ] **Documentation**
  - [ ] Update rule descriptions for version differences
  - [ ] Document new features/protocols in v3.x
  - [ ] Update README

## Backward Compatibility Strategy

1. **Grammar Support**: The underlying `asyncapi-front-end` library handles parsing both versions
2. **Version Detection**: Use AsyncAPIVersionDetector at rule start
3. **Graceful Degradation**: Rules that don't apply to a version simply skip processing
4. **Error Messages**: Provide version-specific messages where applicable
5. **Testing**: Always test both v2.6 and v3.x variants

## Timeline Estimate

- **Week 1**: Utilities + Server/Channel Checks
- **Week 2**: Operations/Tags Checks + Message Navigation
- **Week 3**: Schema/Example Checks + Full Testing
- **Week 4**: Documentation + Release Prep

## References

- [AsyncAPI 3.0 Specification](https://spec.asyncapi.com/v3.0.0)
- [AsyncAPI 3.1 Specification](https://spec.asyncapi.com/v3.1.0)
- [AsyncAPI 3.2 Specification](https://spec.asyncapi.com/v3.2.0)
- [AsyncAPI 2.6 Specification](https://spec.asyncapi.com/v2.6.0)
