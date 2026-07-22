package org.sonar.samples.asyncapi.tools;

import apiquality.sonar.asyncapi.I18nContext;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCheck;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;
import org.sonar.check.RuleProperty;
import org.sonar.samples.asyncapi.ExtendedAsyncApiCheckVerifier;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Standalone runner used only to cross-check Sonar's issue output against Spectral's for the
 * same fixture file, mirroring apiaddicts.sonar.openapi.tools.IssueDumper from the OpenAPI side.
 * Not part of the plugin itself — lives in test sources because it depends on
 * ExtendedAsyncApiCheckVerifier.
 *
 * Usage: java -cp <classpath> org.sonar.samples.asyncapi.tools.IssueDumper
 *          <fully.qualified.CheckClassName> <filePath> <v2|v3|v31> [propertyKey=value ...]
 *
 * Prints a single-line JSON array to stdout: [{"line":N,"message":"..."}, ...]
 * 
 * Spectral: fnm use 22, npm test
 * Maven: mvn test
 */
public final class IssueDumper {

    private IssueDumper() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: IssueDumper <CheckClassName> <filePath> <v2|v3|v31> [key=value ...]");
            System.exit(2);
            return;
        }

        I18nContext.setLang("en");

        String className = args[0];
        String filePath = args[1];
        String version = args[2];

        AsyncApiCheck check = (AsyncApiCheck) Class.forName(className).getDeclaredConstructor().newInstance();

        for (int i = 3; i < args.length; i++) {
            int eq = args[i].indexOf('=');
            if (eq < 0) {
                throw new IllegalArgumentException("Invalid property override (expected key=value): " + args[i]);
            }
            setRuleProperty(check, args[i].substring(0, eq), args[i].substring(eq + 1));
        }

        boolean isV2 = "v2".equals(version);
        boolean isV3 = "v3".equals(version);
        boolean isV31 = "v31".equals(version);

        List<PreciseIssue> issues = ExtendedAsyncApiCheckVerifier.scanFileForIssues(
                new File(filePath), check, isV2, isV3, isV31);

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < issues.size(); i++) {
            PreciseIssue issue = issues.get(i);
            if (i > 0) json.append(',');
            json.append("{\"line\":").append(issue.primaryLocation().startLine())
                    .append(",\"message\":").append(jsonString(issue.primaryLocation().message()))
                    .append('}');
        }
        json.append(']');
        System.out.println(json);
    }

    private static void setRuleProperty(Object check, String key, String value) throws NoSuchFieldException {
        Class<?> c = check.getClass();
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                RuleProperty rp = field.getAnnotation(RuleProperty.class);
                if (rp != null && rp.key().equals(key)) {
                    field.setAccessible(true);
                    try {
                        if (field.getType() == Integer.class || field.getType() == int.class) {
                            field.set(check, Integer.valueOf(value));
                        } else {
                            field.set(check, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Cannot set property " + key, e);
                    }
                    return;
                }
            }
            c = c.getSuperclass();
        }
        throw new NoSuchFieldException("No @RuleProperty found for key: " + key + " on " + check.getClass());
    }

    private static String jsonString(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.append('"').toString();
    }
}
