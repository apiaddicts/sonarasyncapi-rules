#!/bin/bash

# Generate comprehensive test fixtures for AsyncAPI 3.0 and 3.1

echo "🚀 Generating comprehensive test fixtures for AsyncAPI 3.0 and 3.1"

# Function to create test file
create_test() {
    local version=$1
    local category=$2
    local rule=$3
    local filename=$4
    local content=$5

    local dir="src/test/resources/checks/$version/$category/$rule"
    mkdir -p "$dir"
    echo "$content" > "$dir/$filename"
}

# ============= SECURITY CHECKS =============
echo "📝 Creating security check tests..."

# AAR001 - Mandatory HTTPS/WSS Protocol
create_test "v3" "security" "AAR001" "with-amqps.yaml" "asyncapi: 3.0.0
info:
  title: AMQP Secure
  version: 1.0.0
servers:
  - host: broker.example.com
    protocol: amqps
    port: 5671
channels:
  messages:
    address: messages
    messages:
      Message:
        payload: {type: object}"

create_test "v3" "security" "AAR001" "with-mqtt-tls.yaml" "asyncapi: 3.0.0
info:
  title: MQTT Secure
  version: 1.0.0
servers:
  - host: mqtt.example.com
    protocol: 'mqtt+tls'
    port: 8883
channels:
  data:
    address: data
    messages:
      Data:
        payload: {type: object}"

# AAR008 - Already created but let's add more variants
create_test "v3" "security" "AAR008" "with-multiple-servers.yaml" "asyncapi: 3.0.0
info:
  title: Multi Server
  version: 1.0.0
servers:
  - host: api1.example.com
    protocol: wss
  - host: api2.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

# AAR018 - Security Schemes
create_test "v3" "security" "AAR018" "with-apikey.yaml" "asyncapi: 3.0.0
info:
  title: API Key Auth
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: https
components:
  securitySchemes:
    apiKey:
      type: apiKey
      in: header
      name: X-API-Key
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload: {type: object}"

create_test "v3" "security" "AAR018" "with-oauth2.yaml" "asyncapi: 3.0.0
info:
  title: OAuth2 Auth
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: https
components:
  securitySchemes:
    oauth2:
      type: oauth2
      flows:
        implicit:
          authorizationUrl: https://example.com/oauth/authorize
          scopes:
            write: Write access
            read: Read access
channels:
  data:
    address: data
    messages:
      Data:
        payload: {type: object}"

# AAR043 - Security Channel
create_test "v3" "security" "AAR043" "with-security.yaml" "asyncapi: 3.0.0
info:
  title: Secure Channel
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
components:
  securitySchemes:
    api_key:
      type: apiKey
      in: header
      name: X-API-Key
channels:
  secure-events:
    address: secure/events
    messages:
      Event:
        payload: {type: object}"

# ============= OPERATIONS CHECKS =============
echo "📝 Creating operations check tests..."

# AAR010 - Documented Tags
create_test "v3" "operations" "AAR010" "with-documented-tags.yaml" "asyncapi: 3.0.0
info:
  title: Documented Tags
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
tags:
  - name: users
    description: User operations
  - name: orders
    description: Order operations
channels:
  user-events:
    address: user/events
    messages:
      UserEvent:
        payload: {type: object}
operations:
  PublishUserEvent:
    action: send
    channel: user-events
    tags:
      - name: users"

# AAR040 - Defined Channel Servers
create_test "v3" "operations" "AAR040" "with-operation-servers.yaml" "asyncapi: 3.0.0
info:
  title: Operation Servers
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload: {type: object}
operations:
  SendNotification:
    action: send
    channel: notifications
    servers:
      - host: notifications.example.com
        protocol: wss"

# AAR041 - Component Channel Server
create_test "v3" "operations" "AAR041" "with-component-servers.yaml" "asyncapi: 3.0.0
info:
  title: Component Servers
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  data:
    address: data
    messages:
      Data:
        payload: {type: object}
components:
  serverBindings:
    websocket:
      - host: ws.example.com
        protocol: wss"

# ============= FORMAT CHECKS =============
echo "📝 Creating format check tests..."

# AAR011 - Defined License
create_test "v3" "format" "AAR011" "with-license.yaml" "asyncapi: 3.0.0
info:
  title: Licensed API
  version: 1.0.0
  license:
    name: Apache 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0.html
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    messages:
      Event:
        payload: {type: object}"

# AAR012 - Declared Operation ID
create_test "v3" "format" "AAR012" "with-operation-id.yaml" "asyncapi: 3.0.0
info:
  title: Operation IDs
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
    channel: messages
    operationId: sendMessage"

# AAR013 - Duplicate Operation ID
create_test "v3" "format" "AAR013" "with-unique-ids.yaml" "asyncapi: 3.0.0
info:
  title: Unique IDs
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  user-events:
    address: user/events
    messages:
      UserEvent:
        payload: {type: object}
  order-events:
    address: order/events
    messages:
      OrderEvent:
        payload: {type: object}
operations:
  PublishUserEvent:
    action: send
    channel: user-events
    operationId: publishUserEvent
  PublishOrderEvent:
    action: send
    channel: order-events
    operationId: publishOrderEvent"

# AAR021 - Provide Operation Summary
create_test "v3" "format" "AAR021" "with-summary.yaml" "asyncapi: 3.0.0
info:
  title: Operation Summaries
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  notifications:
    address: notifications
    messages:
      Notification:
        payload: {type: object}
operations:
  SendNotification:
    action: send
    channel: notifications
    summary: Send a notification to users
    operationId: sendNotification"

# AAR029 - Mandatory Description
create_test "v3" "format" "AAR029" "with-description.yaml" "asyncapi: 3.0.0
info:
  title: API
  version: 1.0.0
  description: A well-documented API
servers:
  - host: api.example.com
    protocol: wss
channels:
  events:
    address: events
    description: Event channel
    messages:
      Event:
        description: An event message
        payload: {type: object}"

# ============= SCHEMA CHECKS =============
echo "📝 Creating schema check tests..."

# AAR019 - ID Schemas
create_test "v3" "schemas" "AAR019" "with-id.yaml" "asyncapi: 3.0.0
info:
  title: Schema with ID
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

# AAR024 - Message Validation
create_test "v3" "schemas" "AAR024" "with-validation.yaml" "asyncapi: 3.0.0
info:
  title: Message Validation
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  orders:
    address: orders
    messages:
      Order:
        contentType: application/json
        payload:
          type: object
          properties:
            orderId:
              type: string
            amount:
              type: number
              minimum: 0"

# AAR026 - Message Schemas
create_test "v3" "schemas" "AAR026" "with-schema.yaml" "asyncapi: 3.0.0
info:
  title: Message Schemas
  version: 1.0.0
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
          required:
            - id
            - timestamp
          properties:
            id:
              type: string
            timestamp:
              type: string
              format: date-time"

# ============= EXAMPLES CHECKS =============
echo "📝 Creating examples check tests..."

# AAR031 - Message Examples
create_test "v3" "examples" "AAR031" "with-examples.yaml" "asyncapi: 3.0.0
info:
  title: Message Examples
  version: 1.0.0
servers:
  - host: api.example.com
    protocol: wss
channels:
  users:
    address: users
    messages:
      UserSignup:
        payload:
          type: object
          properties:
            id:
              type: string
            email:
              type: string
        examples:
          - id: '123'
            email: user@example.com"

# ============= CREATE V31 VARIANTS =============
echo "📝 Creating v3.1 variants..."

for v3_file in src/test/resources/checks/v3/**/*.yaml; do
    v31_file="${v3_file/\/v3\//\/v31\/}"
    v31_dir=$(dirname "$v31_file")
    mkdir -p "$v31_dir"

    # Copy file and replace version
    sed 's/asyncapi: 3\.0\.0/asyncapi: 3.1.0/' "$v3_file" > "$v31_file"
done

echo "✅ Test generation complete!"
echo "📊 Generated files:"
find src/test/resources/checks/v3 -name "*.yaml" | wc -l | xargs echo "   v3.0.x files:"
find src/test/resources/checks/v31 -name "*.yaml" | wc -l | xargs echo "   v3.1.x files:"
