package apiquality.sonar.asyncapi.utils;

import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

public class AsyncAPIVersionDetector {

    private AsyncAPIVersionDetector() {
    }

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
        if (rootNode == null || rootNode.isMissing()) {
            return AsyncAPIVersion.UNKNOWN;
        }

        JsonNode asyncapiNode = rootNode.get("asyncapi");
        if (asyncapiNode.isMissing() || asyncapiNode.isNull()) {
            return AsyncAPIVersion.UNKNOWN;
        }

        String version = asyncapiNode.stringValue();
        if (version == null || version.trim().isEmpty()) {
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
        return version == AsyncAPIVersion.V3 || version == AsyncAPIVersion.V31
                || version == AsyncAPIVersion.V32;
    }
}
