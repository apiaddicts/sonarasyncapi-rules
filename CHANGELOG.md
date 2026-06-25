# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [2.0.2-beta-1] - 2026-06-25

### Fixed
- Bumped `asyncapi-front-end` and `asyncapi-test-tools` to `2.0.2-beta-1`.

## [2.0.1] - 2026-06-09

### Changed
- Lowered the declared Sonar Plugin API baseline (`pluginApiMinVersion`) from `13.5.0.4319` to `11.1.0.2693`, fixing the startup failure on SonarQube Server whose Plugin API was lower than the declared minimum (e.g. `13.0.0.3026`). The minimum now matches the compile-time `sonar-plugin-api` version and is decoupled from it via a dedicated `pluginApiMinVersion` property.
- Aligned `sonar-plugin-api` to `11.1.0.2693` to match the `dosonarapi-asyncapi` base plugin.
- Bumped the base plugin dependencies (`asyncapi-front-end`, `asyncapi-test-tools`) to `2.0.1`.

### Removed
- Removed unused dependencies `jackson-dataformat-yaml`, `com.networknt:json-schema-validator` and `org.json:json`. These were not referenced by any source and bundled two conflicting Jackson lines (2.x and 3.x) into the shaded plugin JAR.

### Fixed
- Moved `asyncapi-test-tools` to `test` scope (was resolving as `compile`) and dropped the unnecessary `junit-platform-console-standalone` test dependency.

## [2.0.0] - 2026-05-20

### Added
- **Major**: Upgraded core dependencies and plugins to latest stable versions.

- **AAR044AvroNamespaceCheck**: Avro record must declare a `namespace` field to avoid name collisions across services (BUG / MAJOR).
- **AAR045AvroNamespaceNamingCheck**: Avro `namespace` must follow lowercase dot notation, e.g. `org.example.company` (CODE_SMELL / MINOR).
- **AAR046AvroRecordDocCheck**: Avro record should include a `doc` description at the record level (CODE_SMELL / MINOR).
- **AAR047AvroFieldDocCheck**: Each Avro field should include a `doc` description (CODE_SMELL / INFO).
- **AAR048AvroNameNomenclatureCheck**: Avro record and field names must match `^[A-Za-z_][A-Za-z0-9_]*$` as required by the Avro specification (BUG / MAJOR).
- **AAR049AvroDefaultNullCheck**: Avro fields with a nullable union type (e.g. `["null", "string"]`) must define `default: null` to ensure backward-compatible schema evolution (BUG / MAJOR).
- Full bilingual rule documentation (English + Spanish) for all new rules: JSON metadata and HTML description files in both `l10n/asyncapi` and `l10n/es/asyncapi`.

### Changed
- **JsonNodeUtils**: Extended to automatically unwrap Avro component schema wrappers so that all rules transparently navigate `$ref` chains pointing to Avro schemas.
- **AAR019IDSchemasCheck**: Added full Avro record support — when `type: record` is detected, the check verifies that the `fields` array contains a field named `id`. Also handles v3 Multi-Format Schema Object unwrapping.
- **AAR024MessageValidationCheck**: Avro messages with `schemaFormat` present (v2 at message level, v3 inside `payload`) are now accepted as compliant, in addition to messages that declare `contentType`.
- **AAR031MessageExamplesCheck**: Added `JsonNodeUtils.resolve()` call to correctly evaluate messages referenced via `$ref`.
- **AAR035MessageTitleCheck**: Added `JsonNodeUtils.resolve()` call to correctly evaluate messages referenced via `$ref`.
- **AAR042MessageIdentifierCheck**: Added `JsonNodeUtils.resolve()` call to correctly evaluate messages referenced via `$ref`.

## [1.2.0] - 2026-04-30

### Added
- New utility class to detect AsyncAPI version (v2.x, v3.0, v3.1, v3.2) from the root node.

### Changed
- **AAR001MandatoryHttpsProtocolCheck**: Extended secure protocol list to include avro.
- **AAR008DefinedServerCheck**: Added AsyncAPI v3+ support with a dedicated error message.
- **AAR010DocumentedTagCheck**: Refactored to visit `TAG` nodes directly instead of traversing from the root `tags` array.
- **AAR019IDSchemasCheck**: Implemented check logic to verify that object schemas contain an `id` property.
- **AAR024MessageValidationCheck**: Implemented check logic to verify that messages declare a `contentType`.
- **AAR026MessageSchemasCheck**: Implemented check logic with AsyncAPI v2/v3 dual-path support to ensure messages reference component schemas via `$ref`.
- **AAR040DefinedChannelServersCheck**: Refactored to subscribe to `ROOT` instead of `CHANNEL`; iterates channels from root to validate server references.
- **AAR041ComponetChannelServerCheck**: Implemented check logic (v3+ only) to verify that `components` contains both `servers` and `channels`.
- **AAR043SecurityChannelCheck**: Implemented check logic with AsyncAPI v2/v3 dual-path support to verify security schemes are defined per channel/component.
- **AAR031MessageExamplesCheck**: Fixed subscription to `MESSAGE` node instead of `INFO`; now correctly checks for `examples` field.

## [1.1.0] - 2026-04-09

### Added
- New support for Avro schemas.

### Changed

- **AAR001MandatoryHttpsProtocolCheck**: Protocol https is mandatory.
- **AAR031MessageExamplesCheck**: All examples in message object should follow payload and headers schemas.
- **AAR037BindingVersionCheck**: You must specify the version of the binding.

## [1.0.0] - 2024-08-29

### Added

- **AAR001MandatoryHttpsProtocolCheck**: Protocol https is mandatory.
- **AAR008DefinedServerCheck**: Define 'servers' is mandatory.
- **AAR009DeclaredTagCheck**: Associate a tag to this operation.
- **AAR010DocumentedTagCheck**: Tags should be documented.
- **AAR011DefinedLicenseCheck**: License should be documented.
- **AAR012DeclaredOperationIDCheck**: Each operation should have a unique operator (Operation ID).
- **AAR013DuplicateOperationIDCheck**: There cannot be two unique operations (OperationID) that are the same.
- **AAR015UndefiendContactCheck**: API should indicate the contact in the info object.
- **AAR016ContactPropertiesCheck**: Contact should contain name, url, and email fields.
- **AAR017UndefinedUrlLicenseCheck**: The license object must have the url field.
- **AAR018SecuritySchemasCheck**: The security scheme must be among those allowed by the organization and must be complete.
- **AAR019IDSchemasCheck**: The identifier must be defined.
- **AAR021ProvideOpSummaryCheck**: Provide a summary for each operation.
- **AAR022DescriptionDiffersSummaryCheck**: Operation description must differ from its summary.
- **AAR024MessageValidationCheck**: All messages sent and received must comply with the message schema specified in the documentation.
- **AAR026MessageSchemasCheck**: Message schemas are recommended to be found in components.
- **AAR029MandatoryDescriptionCheck**: Each channel and each operation must have a description that explains its purpose and function.
- **AAR031MessageExamplesCheck**: All examples in message object should follow payload and headers schemas.
- **AAR032NumericParameterIntegrityCheck**: Numeric parameters should have minimum, maximum, or format restriction.
- **AAR033StringParameterIntegrityCheck**: String parameters should have minLength, maxLength, pattern (regular expression), or enum restriction.
- **AAR034NumericFormatCheck**: Numeric types require a valid format.
- **AAR035MessageTitleCheck**: It is recommended to have a title per message.
- **AAR036BadDescriptionCheck**: The description must begin with the first capital letter and end with a point.
- **AAR037BindingVersionCheck**: You must specify the version of the binding.
- **AAR040DefinedChannelServersCheck**: Channel server must be defined in the servers object.
- **AAR041ComponetChannelServerCheck**: It is recommended to add the servers and channels to component.
- **AAR042MessageIdentifierCheck**: It is recommended to have a unique identifier per message.
- **AAR043SecurityChannelCheck**: It is recommended to add the security scheme to be used to each channel.

