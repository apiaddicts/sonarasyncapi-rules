#!/bin/bash

# Create non-compliant test cases for comprehensive coverage

echo "🚀 Creating non-compliant test fixtures..."

create_noncompliant() {
    local version=$1
    local category=$2
    local rule=$3
    local filename=$4
    local content=$5

    local dir="src/test/resources/checks/$version/$category/$rule"
    mkdir -p "$dir"
    echo "$content" > "$dir/$filename"
}

# ============= SECURITY CHECKS NON-COMPLIANT =============
echo "📝 Creating non-compliant security tests..."

# AAR001 - Insecure protocols
create_noncompliant "v3" "security" "AAR001" "without-secure-protocol.yaml" "# Noncompliant {{AAR001: Insecure protocol}}
asyncapi: 3.0.0
info:
  title: Insecure API
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: ws
    pathname: /api
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

create_noncompliant "v3" "security" "AAR001" "with-mqtt-insecure.yaml" "# Noncompliant {{AAR001: Insecure protocol}}
asyncapi: 3.0.0
info:
  title: MQTT Insecure
  version: 1.0.0
servers:
  - host: mqtt.example.com
    protocol: mqtt
    port: 1883
channels:
  data:
    address: data
    messages:
      Data:
        payload: {type: object}"

# AAR008 - Already have without-servers

# AAR018 - Invalid security scheme
create_noncompliant "v3" "security" "AAR018" "invalid-security-type.yaml" "# Noncompliant {{AAR018: Invalid security type}}
asyncapi: 3.0.0
info:
  title: Invalid Security
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
components:
  securitySchemes:
    badAuth:
      type: invalid-type
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

# AAR043 - Without security
create_noncompliant "v3" "security" "AAR043" "without-security.yaml" "# Noncompliant {{AAR043: Channel requires security}}
asyncapi: 3.0.0
info:
  title: No Security
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

# ============= OPERATIONS CHECKS NON-COMPLIANT =============
echo "📝 Creating non-compliant operations tests..."

# AAR009 - Already have without-tags

# AAR010 - Undocumented tags
create_noncompliant "v3" "operations" "AAR010" "without-documented-tags.yaml" "# Noncompliant {{AAR010: Tags should be documented}}
asyncapi: 3.0.0
info:
  title: Undocumented Tags
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}
operations:
  PublishEvent:
    action: send
    channel: events
    tags:
      - name: undocumented"

# AAR040 - Without servers
create_noncompliant "v3" "operations" "AAR040" "without-servers.yaml" "# Noncompliant {{AAR040: Servers required}}
asyncapi: 3.0.0
info:
  title: No Servers
  version: 1.0.0
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}
operations:
  SendEvent:
    action: send
    channel: events"

# ============= FORMAT CHECKS NON-COMPLIANT =============
echo "📝 Creating non-compliant format tests..."

# AAR011 - Without license
create_noncompliant "v3" "format" "AAR011" "without-license.yaml" "# Noncompliant {{AAR011: License required}}
asyncapi: 3.0.0
info:
  title: No License
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

# AAR012 - Without operation ID
create_noncompliant "v3" "format" "AAR012" "without-operation-id.yaml" "# Noncompliant {{AAR012: Operation ID required}}
asyncapi: 3.0.0
info:
  title: No Operation IDs
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  messages:
    address: messages
    messages:
      Message:
        payload: {type: object}
operations:
  SendMessage:
    action: send
    channel: messages"

# AAR013 - Duplicate operation IDs
create_noncompliant "v3" "format" "AAR013" "with-duplicate-ids.yaml" "# Noncompliant {{AAR013: Duplicate operation IDs}}
asyncapi: 3.0.0
info:
  title: Duplicate IDs
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events1:
    address: events1
    messages:
      Event:
        payload: {type: object}
  events2:
    address: events2
    messages:
      Event:
        payload: {type: object}
operations:
  PublishEvent:
    action: send
    channel: events1
    operationId: publishEvent
  PublishEvent2:
    action: send
    channel: events2
    operationId: publishEvent"

# AAR021 - Without summary
create_noncompliant "v3" "format" "AAR021" "without-summary.yaml" "# Noncompliant {{AAR021: Summary required}}
asyncapi: 3.0.0
info:
  title: No Summaries
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}
operations:
  SendEvent:
    action: send
    channel: events"

# AAR029 - Without description
create_noncompliant "v3" "format" "AAR029" "without-description.yaml" "# Noncompliant {{AAR029: Description required}}
asyncapi: 3.0.0
info:
  title: No Description
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

# ============= SCHEMA CHECKS NON-COMPLIANT =============
echo "📝 Creating non-compliant schema tests..."

# AAR019 - Without ID
create_noncompliant "v3" "schemas" "AAR019" "without-id.yaml" "# Noncompliant {{AAR019: ID field required}}
asyncapi: 3.0.0
info:
  title: No ID
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  users:
    address: users
    messages:
      User:
        payload:
          type: object
          properties:
            name:
              type: string"

# AAR024 - Without validation
create_noncompliant "v3" "schemas" "AAR024" "without-validation.yaml" "# Noncompliant {{AAR024: Message validation required}}
asyncapi: 3.0.0
info:
  title: No Validation
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  orders:
    address: orders
    messages:
      Order:
        payload:
          type: object"

# AAR026 - Without schema
create_noncompliant "v3" "schemas" "AAR026" "without-schema.yaml" "# Noncompliant {{AAR026: Schema required}}
asyncapi: 3.0.0
info:
  title: No Schema
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        contentType: application/json"

# ============= EXAMPLES CHECKS NON-COMPLIANT =============
echo "📝 Creating non-compliant examples tests..."

# AAR031 - Without examples
create_noncompliant "v3" "examples" "AAR031" "without-examples.yaml" "# Noncompliant {{AAR031: Examples required}}
asyncapi: 3.0.0
info:
  title: No Examples
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  users:
    address: users
    messages:
      User:
        payload:
          type: object
          properties:
            id:
              type: string
            name:
              type: string"

# ============= CREATE V31 VARIANTS =============
echo "📝 Creating v3.1 variants of non-compliant tests..."

find src/test/resources/checks/v3 -name "*.yaml" | while read file; do
    if grep -q "Noncompliant" "$file"; then
        v31_file="${file/v3\//v31\/}"
        v31_dir=$(dirname "$v31_file")
        mkdir -p "$v31_dir"
        sed 's/asyncapi: 3\.0\.0/asyncapi: 3.1.0/' "$file" > "$v31_file"
    fi
done

echo "✅ Non-compliant test generation complete!"
echo ""
echo "📊 Final test coverage:"
echo "V2.6: $(find src/test/resources/checks/v2 -name '*.yaml' | wc -l) files"
echo "V3.0: $(find src/test/resources/checks/v3 -name '*.yaml' | wc -l) files"
echo "V3.1: $(find src/test/resources/checks/v31 -name '*.yaml' | wc -l) files"
