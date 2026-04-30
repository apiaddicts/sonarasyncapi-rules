# Quick Start: Implementing the Next Check

This guide walks you through implementing **AAR001 - Mandatory HTTPS/WSS Protocol Check** as your next task. Follow this exact pattern for other checks.

## Step 1: Understand the Check Requirements

**Rule**: AAR001 - Mandatory HTTPS/WSS Protocol  
**What it validates**: All servers must use secure protocols (wss, https, amqps, mqtt+tls)  
**Location**: `src/main/java/apiquality/sonar/asyncapi/checks/security/AAR001MandatoryHttpsProtocolCheck.java`

## Step 2: Review the Reference Implementation

See `IMPLEMENTATION_CODE_EXAMPLES.md` section "AAR001 - Mandatory HTTPS/WSS Protocol Check" for the complete code.

Key points:
- Define secure protocols set: `wss`, `https`, `amqps`, `mqtt+tls`, etc.
- For V2: Servers are a map - iterate and check `protocol` field
- For V3: Servers are an array - iterate and check `protocol` field
- Subscribe to `AsyncApiGrammar.ROOT` kind

## Step 3: Update the Check Implementation

```bash
# Open the file
open src/main/java/apiquality/sonar/asyncapi/checks/security/AAR001MandatoryHttpsProtocolCheck.java
```

Copy the implementation from IMPLEMENTATION_CODE_EXAMPLES.md and paste it in (replacing the old code if it exists).

**Key changes from original**:
1. Add `import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;`
2. Add version detection in `visitNode` method
3. Create `validateV2Servers()` method for v2 logic
4. Create `validateV3Servers()` method for v3 logic
5. Add private helper `isSecureProtocol()` method

## Step 4: Update Error Messages

Edit `src/main/resources/messages/errors_en.properties`:

```properties
AAR001.error=Server must use secure protocol (wss, https, amqps, mqtt+tls)
AAR001.error-v3=Server must use secure protocol in AsyncAPI 3.x (wss, https, amqps, mqtt+tls)
```

## Step 5: Create Test Files for V3.0

Create directory: `src/test/resources/checks/v3/security/AAR001/`

### File 1: with-insecure-protocol.yaml

```yaml
# Noncompliant {{AAR001: Server must use secure protocol}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: ws
    pathname: /ws
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload:
          type: object
```

### File 2: with-secure-protocol.yaml

```yaml
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
    pathname: /ws
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload:
          type: object
```

### File 3: with-multiple-servers-mixed.yaml

```yaml
# Noncompliant {{AAR001: Server must use secure protocol}}
asyncapi: 3.0.0
info:
  title: Example
  version: '1.0.0'
servers:
  - host: api.example.com
    protocol: wss
  - host: api-dev.example.com
    protocol: ws
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload:
          type: object
```

## Step 6: Create Test Files for V3.1

Create directory: `src/test/resources/checks/v31/security/AAR001/`

Copy the same files from v3 but update the version number:

```yaml
asyncapi: 3.1.0
# ... rest is identical
```

Files needed:
- `with-insecure-protocol.yaml`
- `with-secure-protocol.yaml`
- `with-multiple-servers-mixed.yaml`

## Step 7: Update the Test Class

Edit: `src/test/java/org/sonar/samples/asyncapi/checks/security/AAR001MandatoryHttpsProtocolCheckTest.java`

```java
package org.sonar.samples.asyncapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.asyncapi.BaseCheckTest;

import apiquality.sonar.asyncapi.checks.security.AAR001MandatoryHttpsProtocolCheck;

public class AAR001MandatoryHttpsProtocolCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "AAR001";
        check = new AAR001MandatoryHttpsProtocolCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
        v31Path = getV31Path("security");
    }

    @Test
    public void verifyV2WithInsecureProtocol() {
        verifyV2("with-insecure-protocol.yaml");
    }

    @Test
    public void verifyV2WithSecureProtocol() {
        verifyV2("with-secure-protocol.yaml");
    }

    @Test
    public void verifyV3WithInsecureProtocol() {
        verifyV3("with-insecure-protocol.yaml");
    }

    @Test
    public void verifyV3WithSecureProtocol() {
        verifyV3("with-secure-protocol.yaml");
    }

    @Test
    public void verifyV3WithMultipleServersMixed() {
        verifyV3("with-multiple-servers-mixed.yaml");
    }

    @Test
    public void verifyV31WithInsecureProtocol() {
        verifyV31("with-insecure-protocol.yaml");
    }

    @Test
    public void verifyV31WithSecureProtocol() {
        verifyV31("with-secure-protocol.yaml");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties(
            "AAR001 - MandatoryHttpsProtocol - Servers should use secure protocols",
            RuleType.VULNERABILITY,
            Severity.CRITICAL,
            tags("safety")
        );
    }
}
```

## Step 8: Run Tests

```bash
# Run only AAR001 tests
mvn test -Dtest=AAR001MandatoryHttpsProtocolCheckTest

# Run all tests to ensure no regression
mvn clean test

# With coverage report
mvn clean test jacoco:report
```

## Step 9: Verify Backward Compatibility

Check that existing V2 tests still pass:
```bash
mvn test -Dtest=AAR001MandatoryHttpsProtocolCheckTest#verifyV2*
```

## Step 10: Code Review Checklist

Before considering the check complete:

- [ ] V2 test cases pass ✓
- [ ] V3.0 test cases pass ✓
- [ ] V3.1 test cases pass ✓
- [ ] Version detection logic is correct ✓
- [ ] Both secure and insecure cases are tested ✓
- [ ] Edge cases are covered (multiple servers, etc.) ✓
- [ ] Error messages are clear and version-appropriate ✓
- [ ] Code follows existing patterns in BaseCheck ✓
- [ ] No null pointer exceptions with missing fields ✓
- [ ] Performance is acceptable ✓

## Common Issues & Solutions

### Issue 1: "OPERATION kind not found in V3"
**Solution**: Make sure you're subscribing to ROOT, not OPERATION. Use `AsyncApiGrammar.ROOT`.

### Issue 2: Test fails with "Cannot find test file"
**Solution**: Verify directory structure is correct:
```
src/test/resources/checks/v3/security/AAR001/
src/test/resources/checks/v31/security/AAR001/
```

### Issue 3: Null Pointer Exception on protocol.stringValue()
**Solution**: Add null check before calling stringValue():
```java
String protocol = protocolNode.stringValue();
if (protocol != null && !isSecureProtocol(protocol)) {
    // Report issue
}
```

### Issue 4: Version detection always returns UNKNOWN
**Solution**: Make sure the root node has the `asyncapi` field:
```yaml
asyncapi: 3.0.0  # Must be first!
info:
  title: Example
```

## Next Check After This

Once AAR001 is complete, follow the same pattern for:
1. **AAR018** - Security Schemes Check (similar structure, different validation)
2. **AAR040** - Defined Channel Servers Check (more complex, see IMPLEMENTATION_CODE_EXAMPLES.md)
3. **AAR043** - Security Channel Check

## Files Modified Summary

```
Modified/Created:
- src/main/java/.../checks/security/AAR001MandatoryHttpsProtocolCheck.java
- src/test/java/.../checks/security/AAR001MandatoryHttpsProtocolCheckTest.java
- src/test/resources/checks/v3/security/AAR001/
- src/test/resources/checks/v31/security/AAR001/
- src/main/resources/messages/errors_en.properties

Total files: ~8-10 (YAML/JSON pairs)
```

## Time Estimate

- Code modification: 15-30 minutes
- Test file creation: 15-20 minutes
- Testing & debugging: 30-45 minutes
- **Total: 1-1.5 hours**

## Resources

- **Code Template**: IMPLEMENTATION_CODE_EXAMPLES.md (search "AAR001")
- **Test Templates**: TEST_DATA_REFERENCE.md (search "AAR001")
- **Error Messages**: Check existing entries in errors_en.properties
- **Existing Check**: View AAR008DefinedServerCheck.java for pattern reference

---

**Ready to start?** Open your IDE and follow the steps above!
