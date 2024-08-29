# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

