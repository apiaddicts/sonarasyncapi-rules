package apiquality.sonar.asyncapi.utils;

import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class OperationNavigator {
    public static final String CHANNELS = "channels";

    private OperationNavigator() {
    }

    @FunctionalInterface
    public interface OperationProcessor {
        void processOperation(JsonNode operation, String operationKey, JsonNode channel);
    }

    public static void processOperations(JsonNode rootNode, OperationProcessor processor) {
        if (rootNode == null || rootNode.isMissing()) {
            return;
        }

        AsyncAPIVersionDetector.AsyncAPIVersion version = AsyncAPIVersionDetector.detectVersion(rootNode);

        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            processV2Operations(rootNode, processor);
        } else if (AsyncAPIVersionDetector.isVersion3Plus(rootNode)) {
            processV3Operations(rootNode, processor);
        }
    }

    private static void processV2Operations(JsonNode root, OperationProcessor processor) {
        JsonNode channels = root.get(CHANNELS);
        if (channels.isMissing()) {
            return;
        }

        channels.elements().forEach(channel -> {
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

    private static void processV3Operations(JsonNode root, OperationProcessor processor) {
        JsonNode operations = root.get("operations");
        if (operations.isMissing()) {
            // In v3, operations might be inline in channels still for compatibility
            processV3ChannelsForInlineOperations(root, processor);
            return;
        }

        JsonNode channels = root.get(CHANNELS);
        operations.elements().forEach(operation -> {
            JsonNode channelRef = operation.get("channel");
            JsonNode channel = null;

            if (!channelRef.isMissing() && !channels.isMissing()) {
                String channelKey = channelRef.stringValue();
                channel = channels.get(channelKey);
            }

            processor.processOperation(operation, operation.key().stringValue(), channel);
        });
    }

    private static void processV3ChannelsForInlineOperations(JsonNode root, OperationProcessor processor) {
        JsonNode channels = root.get(CHANNELS);
        if (channels.isMissing()) {
            return;
        }

        channels.elements().forEach(channel -> {
            // In v3, channels may still have operations (send/receive instead of publish/subscribe)
            JsonNode send = channel.get("send");
            JsonNode receive = channel.get("receive");

            if (!send.isMissing()) {
                processor.processOperation(send, "send", channel);
            }
            if (!receive.isMissing()) {
                processor.processOperation(receive, "receive", channel);
            }
        });
    }

    public static void processChannels(JsonNode rootNode, ChannelProcessor processor) {
        if (rootNode == null || rootNode.isMissing()) {
            return;
        }

        JsonNode channels = rootNode.get(CHANNELS);
        if (channels.isMissing()) {
            return;
        }

        channels.elements().forEach(processor::processChannel);
    }

    @FunctionalInterface
    public interface ChannelProcessor {
        void processChannel(JsonNode channel);
    }
}
