package apiquality.sonar.asyncapi.utils;

import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class MessageNavigator {

    private MessageNavigator() {
    }

    public static JsonNode getMessageFromOperation(JsonNode operationNode, JsonNode channelNode) {
        if (operationNode == null || operationNode.isMissing()) {
            return null;
        }

        // Try V2 path: message directly in operation
        JsonNode message = operationNode.get("message");
        if (!message.isMissing()) {
            return message;
        }

        // Try V3 path: messages in channel
        if (channelNode != null && !channelNode.isMissing()) {
            JsonNode messages = channelNode.get("messages");
            if (!messages.isMissing()) {
                // Get first message if it's an array
                java.util.List<JsonNode> messagesList = messages.elements();
                if (!messagesList.isEmpty()) {
                    return messagesList.get(0);
                }
            }
        }

        return null;
    }

    public static java.util.List<JsonNode> getAllMessages(JsonNode channelNode) {
        if (channelNode == null || channelNode.isMissing()) {
            return java.util.Collections.emptyList();
        }

        JsonNode messages = channelNode.get("messages");
        if (!messages.isMissing()) {
            return messages.elements();
        }

        return java.util.Collections.emptyList();
    }

    public static JsonNode getMessagePayload(JsonNode messageNode) {
        if (messageNode == null || messageNode.isMissing()) {
            return null;
        }

        // Try V2/V3 path: payload
        JsonNode payload = messageNode.get("payload");
        if (!payload.isMissing()) {
            return payload;
        }

        // Try V2 path: schema (deprecated but may exist)
        JsonNode schema = messageNode.get("schema");
        if (!schema.isMissing()) {
            return schema;
        }

        return null;
    }

    public static JsonNode getMessageContentType(JsonNode messageNode) {
        if (messageNode == null || messageNode.isMissing()) {
            return null;
        }

        return messageNode.get("contentType");
    }
}
