# AsyncAPI 3.x Migration Project

> Add support for AsyncAPI 3.0, 3.1, and 3.2 to SonarQube AsyncAPI rules while maintaining full backward compatibility with AsyncAPI 2.6

**Status**: ✅ Foundation Complete | 🚀 Ready for Implementation  
**Branch**: `feature/asyncapi3`  
**Version Target**: v1.2.0

---

## 📋 Quick Navigation

| Document | Purpose | Audience |
|----------|---------|----------|
| [MIGRATION_SUMMARY.md](./MIGRATION_SUMMARY.md) | Project overview, architecture, roadmap | Project managers, architects |
| [ASYNCAPI_3X_MIGRATION_GUIDE.md](./ASYNCAPI_3X_MIGRATION_GUIDE.md) | Detailed migration strategy, schema changes | Developers |
| [IMPLEMENTATION_CODE_EXAMPLES.md](./IMPLEMENTATION_CODE_EXAMPLES.md) | Code examples for all check types | Developers |
| [TEST_DATA_REFERENCE.md](./TEST_DATA_REFERENCE.md) | Test file templates and patterns | QA, developers |
| [QUICK_START_NEXT_CHECK.md](./QUICK_START_NEXT_CHECK.md) | Step-by-step guide for next implementation | Developers starting new checks |

---

## 🎯 Project Goals

✅ **Support AsyncAPI 3.0, 3.1, 3.2** - Full spec coverage  
✅ **Backward Compatible with 2.6** - No breaking changes  
✅ **Comprehensive Testing** - v2, v3.0, v3.1 variants  
✅ **Clear Documentation** - For implementation and usage  

---

## 📦 What's Been Delivered

### Utilities (✅ Complete)
```
src/main/java/apiquality/sonar/asyncapi/utils/
├── AsyncAPIVersionDetector.java      - Version detection with helpers
├── OperationNavigator.java           - Version-aware operation navigation
└── MessageNavigator.java             - Message location abstraction
```

### Sample Check Update (✅ Complete)
```
src/main/java/apiquality/sonar/asyncapi/checks/security/
└── AAR008DefinedServerCheck.java     - Updated with v3 support
```

### Test Fixtures (✅ Complete)
```
src/test/resources/checks/
├── v3/security/AAR001/
├── v3/security/AAR008/
├── v3/operations/AAR009/
├── v31/security/AAR001/
├── v31/security/AAR008/
└── v31/operations/AAR009/
```

### Documentation (✅ Complete)
- Migration guide with schema differences
- Code examples for all check types
- Test data templates
- Implementation roadmap
- Quick start guide

---

## 🚀 Getting Started

### Prerequisites
- Java 8+
- Maven 3.6+
- Git
- IDE (IntelliJ IDEA or VS Code)

### Setup
```bash
# Already on feature/asyncapi3 branch
git status

# Build project
mvn clean install

# Run tests
mvn test
```

### Verify Current State
```bash
# Check utilities are compiled
ls -la src/main/java/apiquality/sonar/asyncapi/utils/

# Verify test fixtures exist
ls -la src/test/resources/checks/v3/
ls -la src/test/resources/checks/v31/
```

---

## 📚 Understanding the Architecture

### Version Detection Pattern

All checks use the same pattern for version-aware validation:

```java
AsyncAPIVersionDetector.AsyncAPIVersion version = 
    AsyncAPIVersionDetector.detectVersion(node);

if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
    validateV2(node);
} else if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
    validateV3(node);
}
```

### Key Schema Differences

| Aspect | V2.6 | V3.x |
|--------|------|------|
| **Servers** | Map | Array |
| **Operations** | In channels | Separate object |
| **Messages** | In operations | In channels |
| **Channel ID** | Map key | `address` field |

See [ASYNCAPI_3X_MIGRATION_GUIDE.md](./ASYNCAPI_3X_MIGRATION_GUIDE.md#key-schema-changes-26--3x) for details.

---

## ✨ Implementation Guide

### Phase 1: Security Checks

**Next task**: Implement **AAR001 - Mandatory HTTPS/WSS Protocol**

```bash
# Complete step-by-step guide:
cat QUICK_START_NEXT_CHECK.md
```

**Files to modify**:
- `src/main/java/apiquality/sonar/asyncapi/checks/security/AAR001MandatoryHttpsProtocolCheck.java`
- `src/test/java/org/sonar/samples/asyncapi/checks/security/AAR001MandatoryHttpsProtocolCheckTest.java`
- Test fixtures in `src/test/resources/checks/v3/security/AAR001/` and `v31/security/AAR001/`

**Estimated time**: 1-1.5 hours

### Phase 2: Operations Checks

**Checks**: AAR009, AAR010, AAR040, AAR041

See [IMPLEMENTATION_CODE_EXAMPLES.md#operations-checks](./IMPLEMENTATION_CODE_EXAMPLES.md#operations-checks)

### Phase 3: Format & Schema Checks

**Checks**: AAR012, AAR021, AAR026, etc.

Most are already compatible - mainly need test fixtures.

### Phase 4: Full Testing

- Verify all v2 tests still pass
- Create comprehensive v3.0 and v3.1 test suites
- Performance testing

### Phase 5: Release Prep

- Update README
- Create CHANGELOG entry
- Release as v1.2.0

---

## 🧪 Testing Guide

### Run Specific Check Tests
```bash
mvn test -Dtest=AAR008DefinedServerCheckTest
```

### Run All Tests
```bash
mvn clean test
```

### Run with Coverage
```bash
mvn clean test jacoco:report
cat target/site/jacoco/index.html
```

### Test Patterns
```bash
# V2 only
verifyV2("without-servers.yaml")

# V3.0 only
verifyV3("without-servers.yaml")

# V3.1 only
verifyV31("without-servers.yaml")

# Both formats (YAML and JSON)
verify("without-servers")  # Tests .yaml and .json
```

---

## 📖 Code Example: Complete Check Update

Here's a complete example of updating a check:

```java
// Before - V2 only
@Rule(key = AAR008DefinedServerCheck.KEY)
public class AAR008DefinedServerCheck extends BaseCheck {
    @Override
    public void visitNode(JsonNode node) {
        JsonNode serversNode = node.get("servers");
        if (serversNode.isMissing() || serversNode.isNull()) {
            addIssue(KEY, translate("AAR008.error-v2-servers"), serversNode.key());
        }
    }
}

// After - V2 & V3 compatible
@Rule(key = AAR008DefinedServerCheck.KEY)
public class AAR008DefinedServerCheck extends BaseCheck {
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
}
```

---

## 🔍 Common Implementation Questions

### Q: How do I navigate operations differently in V2 vs V3?

**A**: Use `OperationNavigator`:
```java
OperationNavigator.processOperations(rootNode, (operation, key, channel) -> {
    // This handles both V2 (channels → publish/subscribe)
    // and V3 (root operations → channel reference)
    JsonNode tags = operation.get("tags");
    if (tags.isMissing()) {
        addIssue(KEY, "Operations must declare tags", operation);
    }
});
```

### Q: How do I access messages in both versions?

**A**: Use `MessageNavigator`:
```java
JsonNode message = MessageNavigator.getMessageFromOperation(operation, channel);
JsonNode payload = MessageNavigator.getMessagePayload(message);
```

### Q: Do I need to update error messages?

**A**: Recommended. Create version-specific messages:
```properties
# In errors_en.properties
AAR001.error-v2=V2-specific error message
AAR001.error-v3=V3-specific error message
```

Then use in check:
```java
if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
    addIssue(KEY, translate("AAR001.error-v2"), node);
} else {
    addIssue(KEY, translate("AAR001.error-v3"), node);
}
```

### Q: How do I test both YAML and JSON formats?

**A**: Automatically handled by BaseCheckTest:
```java
// This tests both with-servers.yaml and with-servers.json
verifyV3("with-servers");
```

### Q: What if a check doesn't need version-specific logic?

**A**: That's fine! Many checks (like tags validation) work the same way. Just ensure they handle both versions gracefully.

---

## ✅ Completion Checklist

For each check implementation:

- [ ] Version detection added
- [ ] V2 code path implemented/verified
- [ ] V3 code path implemented
- [ ] Error messages created for both versions
- [ ] V2 test file created (if not exists)
- [ ] V3.0 test file created
- [ ] V3.1 test file created
- [ ] Test class updated with new test methods
- [ ] All tests passing
- [ ] Code review completed
- [ ] Documentation updated (if needed)

---

## 🚨 Important Files Not to Modify (Yet)

- `AsyncAPICustomPlugin.java` - Entry point, no changes needed
- `AsyncAPICustomRulesDefinition.java` - Rule metadata, updated only for new rules
- `AsyncAPICustomRuleRepository.java` - Repository, no changes needed
- Existing V2 test resources - For backward compatibility

---

## 📞 Support Resources

### Documentation in This Repo
1. **[MIGRATION_SUMMARY.md](./MIGRATION_SUMMARY.md)** - Overall project status
2. **[ASYNCAPI_3X_MIGRATION_GUIDE.md](./ASYNCAPI_3X_MIGRATION_GUIDE.md)** - Detailed schema changes
3. **[IMPLEMENTATION_CODE_EXAMPLES.md](./IMPLEMENTATION_CODE_EXAMPLES.md)** - Code patterns
4. **[TEST_DATA_REFERENCE.md](./TEST_DATA_REFERENCE.md)** - Test templates
5. **[QUICK_START_NEXT_CHECK.md](./QUICK_START_NEXT_CHECK.md)** - Step-by-step guide

### Online Resources
- [AsyncAPI 3.0 Spec](https://spec.asyncapi.com/v3.0.0)
- [AsyncAPI 3.1 Spec](https://spec.asyncapi.com/v3.1.0)
- [AsyncAPI 3.2 Spec](https://spec.asyncapi.com/v3.2.0)
- [Existing Tests](./src/test/java)

---

## 📊 Progress Tracking

### Completed
- ✅ Core utilities (version detection, navigation)
- ✅ Reference check update (AAR008)
- ✅ Test fixtures for core checks
- ✅ Comprehensive documentation

### In Progress / To Do
- [ ] AAR001 - Mandatory HTTPS/WSS
- [ ] AAR018 - Security Schemes
- [ ] AAR043 - Security Channel
- [ ] AAR009 - Tags (verification)
- [ ] AAR040 - Defined Channel Servers
- [ ] AAR041 - Component Channel Server
- [ ] Remaining format & schema checks
- [ ] Full test suite
- [ ] Release preparation

---

## 🔄 Development Workflow

```bash
# 1. Create feature branch (already done)
git checkout feature/asyncapi3

# 2. Make changes to a check
vim src/main/java/apiquality/sonar/asyncapi/checks/security/AAR001*.java

# 3. Create test files
mkdir -p src/test/resources/checks/v3/security/AAR001
vim src/test/resources/checks/v3/security/AAR001/with-*.yaml

# 4. Update test class
vim src/test/java/org/sonar/samples/asyncapi/checks/security/AAR001*Test.java

# 5. Run tests
mvn test -Dtest=AAR001*Test

# 6. Commit
git add .
git commit -m "feat: add AsyncAPI 3.x support for AAR001"

# 7. Push for review
git push origin feature/asyncapi3
```

---

## 🎓 Learning Path

**New to this project?** Follow this order:

1. Read this file (you are here!)
2. Read [MIGRATION_SUMMARY.md](./MIGRATION_SUMMARY.md) for architecture
3. Review [ASYNCAPI_3X_MIGRATION_GUIDE.md](./ASYNCAPI_3X_MIGRATION_GUIDE.md) for changes
4. Read [IMPLEMENTATION_CODE_EXAMPLES.md](./IMPLEMENTATION_CODE_EXAMPLES.md)
5. Follow [QUICK_START_NEXT_CHECK.md](./QUICK_START_NEXT_CHECK.md) for first implementation
6. Reference [TEST_DATA_REFERENCE.md](./TEST_DATA_REFERENCE.md) for test files

---

## 🤝 Contributing

When implementing a new check:

1. Follow the version detection pattern shown in code examples
2. Create both V3 and V31 test fixtures
3. Ensure V2 tests still pass
4. Add clear error messages
5. Reference this README if you have questions

---

## 📝 License

This project is licensed under GNU LGPL 3. See LICENSE file for details.

---

## 🎉 Next Steps

**Ready to start?**

1. Open [QUICK_START_NEXT_CHECK.md](./QUICK_START_NEXT_CHECK.md)
2. Implement AAR001
3. Run `mvn test`
4. Create a commit
5. Rinse and repeat for other checks

**Happy coding! 🚀**

---

**Document Version**: 1.0  
**Last Updated**: 2024  
**Status**: Ready for Development
