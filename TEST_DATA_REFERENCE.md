# AsyncAPI Test Data Reference Guide

This guide provides template test files for AsyncAPI 3.0, 3.1, and 3.2 that demonstrate common patterns for testing SonarQube rules.

## Directory Structure

```
src/test/resources/checks/
├── v2/
│   ├── security/AAR001/
│   ├── security/AAR008/
│   ├── operations/AAR009/
│   ├── format/AAR012/
│   └── ... (other checks)
├── v3/          # NEW: AsyncAPI 3.0.x
│   ├── security/AAR001/
│   ├── security/AAR008/
│   ├── operations/AAR009/
│   └── ... (mirror v2 structure)
└── v31/         # NEW: AsyncAPI 3.1.x
    ├── security/AAR001/
    ├── security/AAR008/
    ├── operations/AAR009/
    └── ... (mirror v2 structure)
```

## Template: Basic AsyncAPI Document

### AsyncAPI 2.6 Template

```yaml
asyncapi: '2.6.0'
info:
  title: Basic Example
  version: '1.0.0'
servers:
  production:
    url: wss://api.example.com
    protocol: wss
channels:
  user/signup:
    publish:
      operationId: userSignup
      tags:
        - name: users
      message:
        payload:
          type: object
          properties:
            userId:
              type: string
```

### AsyncAPI 3.0.x Template

```yaml
asyncapi: 3.0.0
info:
  title: Basic Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
    pathname: /ws
channels:
  user-signup:
    address: user/signup
    messages:
      UserSignup:
        payload:
          type: object
          properties:
            userId:
              type: string
operations:
  UserSignup:
    action: send
    channel: user-signup
    tags:
      - name: users
```

### AsyncAPI 3.1.x Template

Same as 3.0.x but with version number change:

```yaml
asyncapi: 3.1.0
# Rest is identical to 3.0.0
```

---

## Test File Patterns by Check

### Security Checks

#### AAR001 - Mandatory HTTPS/WSS Protocol

**Non-compliant (v3):** Insecure protocol

```yaml
# File: src/test/resources/checks/v3/security/AAR001/with-insecure-protocol.yaml
# Noncompliant {{AAR001: Servers should use secure protocols}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: ws
    pathname: /ws
channels:
  chat:
    address: chat/messages
    messages:
      Message:
        payload:
          type: object
```

**Compliant (v3):** Secure protocol

```yaml
# File: src/test/resources/checks/v3/security/AAR001/with-secure-protocol.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
    pathname: /ws
channels:
  chat:
    address: chat/messages
    messages:
      Message:
        payload:
          type: object
```

#### AAR008 - Defined Servers

**Non-compliant (v3):** Missing servers

```yaml
# File: src/test/resources/checks/v3/security/AAR008/without-servers.yaml
# Noncompliant {{AAR008: Define 'servers' is mandatory}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
channels:
  notifications:
    address: events/notifications
    messages:
      Notification:
        payload:
          type: object
```

**Compliant (v3):** With servers

```yaml
# File: src/test/resources/checks/v3/security/AAR008/with-servers.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  notifications:
    address: events/notifications
    messages:
      Notification:
        payload:
          type: object
```

#### AAR018 - Security Schemes

**Non-compliant (v3):** Invalid security type

```yaml
# File: src/test/resources/checks/v3/security/AAR018/invalid-security-type.yaml
# Noncompliant {{AAR018: Invalid security scheme type}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
components:
  securitySchemes:
    apiKey:
      type: invalidType
      in: header
channels:
  events:
    address: events
    messages:
      Event:
        payload:
          type: object
```

**Compliant (v3):** Valid security schemes

```yaml
# File: src/test/resources/checks/v3/security/AAR018/valid-security-schemes.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
components:
  securitySchemes:
    apiKey:
      type: apiKey
      in: header
      name: X-API-Key
    oauth:
      type: oauth2
      flows:
        implicit:
          authorizationUrl: https://example.com/oauth/authorize
          scopes:
            write: Write access
channels:
  events:
    address: events
    messages:
      Event:
        payload:
          type: object
```

---

### Operations Checks

#### AAR009 - Declared Tags

**Non-compliant (v3):** Missing tags on operation

```yaml
# File: src/test/resources/checks/v3/operations/AAR009/without-tags.yaml
# Noncompliant {{AAR009: Operations should declare tags}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  user-events:
    address: user/events
    messages:
      UserEvent:
        payload:
          type: object
operations:
  PublishUserEvent:
    action: send
    channel: user-events
```

**Compliant (v3):** Tags declared

```yaml
# File: src/test/resources/checks/v3/operations/AAR009/with-tags.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  user-events:
    address: user/events
    messages:
      UserEvent:
        payload:
          type: object
operations:
  PublishUserEvent:
    action: send
    channel: user-events
    tags:
      - name: users
```

#### AAR012 - Declared Operation ID

**Non-compliant (v3):** Missing operationId

```yaml
# File: src/test/resources/checks/v3/operations/AAR012/without-operation-id.yaml
# Noncompliant {{AAR012: Operations should declare operationId}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  orders:
    address: orders/created
    messages:
      OrderCreated:
        payload:
          type: object
operations:
  PublishOrderCreated:
    action: send
    channel: orders
```

**Compliant (v3):** operationId provided

```yaml
# File: src/test/resources/checks/v3/operations/AAR012/with-operation-id.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  orders:
    address: orders/created
    messages:
      OrderCreated:
        payload:
          type: object
operations:
  PublishOrderCreated:
    action: send
    channel: orders
    operationId: publishOrderCreated
```

#### AAR040 - Defined Channel Servers

**Non-compliant (v3):** No servers defined at any level

```yaml
# File: src/test/resources/checks/v3/operations/AAR040/without-servers.yaml
# Noncompliant {{AAR040: Servers should be defined for channels}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload:
          type: object
operations:
  ReceiveNotification:
    action: receive
    channel: notifications
```

**Compliant (v3):** Servers at root level

```yaml
# File: src/test/resources/checks/v3/operations/AAR040/with-root-servers.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload:
          type: object
operations:
  ReceiveNotification:
    action: receive
    channel: notifications
```

**Alternative compliant (v3):** Servers at channel level

```yaml
# File: src/test/resources/checks/v3/operations/AAR040/with-channel-servers.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
channels:
  notifications:
    address: notifications
    servers:
      - host: api.example.com
        protocol: wss
    messages:
      Notification:
        payload:
          type: object
operations:
  ReceiveNotification:
    action: receive
    channel: notifications
```

---

### Format Checks

#### AAR021 - Provide Operation Summary

**Non-compliant (v3):** Missing summary

```yaml
# File: src/test/resources/checks/v3/format/AAR021/without-summary.yaml
# Noncompliant {{AAR021: Operations should provide a summary}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  payments:
    address: payments/processed
    messages:
      PaymentProcessed:
        payload:
          type: object
operations:
  PublishPaymentProcessed:
    action: send
    channel: payments
    operationId: publishPaymentProcessed
```

**Compliant (v3):** With summary

```yaml
# File: src/test/resources/checks/v3/format/AAR021/with-summary.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  payments:
    address: payments/processed
    messages:
      PaymentProcessed:
        payload:
          type: object
operations:
  PublishPaymentProcessed:
    action: send
    channel: payments
    summary: Publish payment processed event
    operationId: publishPaymentProcessed
```

---

### Schema Checks

#### AAR026 - Message Schemas

**Non-compliant (v3):** Missing payload

```yaml
# File: src/test/resources/checks/v3/schemas/AAR026/without-schema.yaml
# Noncompliant {{AAR026: Messages should define schemas}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        contentType: application/json
```

**Compliant (v3):** With payload

```yaml
# File: src/test/resources/checks/v3/schemas/AAR026/with-schema.yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        contentType: application/json
        payload:
          type: object
          properties:
            id:
              type: string
            timestamp:
              type: string
              format: date-time
```

---

## Key Differences for Test File Creation

| Aspect | AsyncAPI 2.6 | AsyncAPI 3.0+ |
|--------|-------------|---------------|
| Version line | `asyncapi: '2.6.0'` | `asyncapi: 3.0.0` |
| Servers structure | Map with server keys | Array of server objects |
| Channel location | Top-level channels object | Top-level channels object |
| Channel address | Implicit (from key) | Explicit `address` field |
| Operations location | Within each channel (publish/subscribe) | Top-level `operations` object |
| Operation actions | publish, subscribe | send, receive |
| Message location | In operation (operation.message) | In channel (channel.messages) |
| Message reference | Direct | Can be by name or ref |
| Tags location | Root or operation level | Root, operation, server, or message level |

---

## Best Practices for Test Files

1. **Use YAML for readability**: Always prefer `.yaml` over `.json` for test files
2. **Mark non-compliant cases**: Use `# Noncompliant {{RULE_ID: message}}` comment
3. **Minimal documents**: Keep test files as simple as possible
4. **One issue per file**: Each non-compliant file should trigger exactly one issue
5. **Version consistency**: Test the same scenario for v2, v3.0, and v3.1
6. **Realistic payloads**: Use realistic (though minimal) payload structures
7. **Protocol testing**: Always use `wss` for secure protocol tests

---

## Naming Conventions

```
# Compliant test file
with-servers.yaml
with-tags.yaml
with-summary.yaml

# Non-compliant test files
without-servers.yaml
without-tags.yaml
with-insecure-protocol.yaml
invalid-security-type.yaml
```

---

## Creating JSON Variants

For each YAML test file, also create a JSON equivalent:

```json
// File: src/test/resources/checks/v3/security/AAR008/without-servers.json
{
  "asyncapi": "3.0.0",
  "info": {
    "title": "Swagger Petstore",
    "version": "1.0.0"
  },
  "channels": {
    "user-signup": {
      "address": "user/signup",
      "messages": {
        "UserSignedUp": {
          "payload": {
            "type": "object"
          }
        }
      }
    }
  }
}
```

## Testing Both YAML and JSON

The `BaseCheckTest` class automatically tests both `.yaml` and `.json` formats:

```java
protected void verify(String file, boolean isV2, boolean isV3, boolean isV31) {
    // Tests both file.yaml and file.json
    if (filePath.contains(".")) {
        // Test specified format
    } else {
        // Tests both .yaml and .json
    }
}
```
