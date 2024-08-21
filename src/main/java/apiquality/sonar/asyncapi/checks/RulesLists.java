package apiquality.sonar.asyncapi.checks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apiquality.sonar.asyncapi.checks.apim.wso2.*;
import apiquality.sonar.asyncapi.checks.examples.*;
import apiquality.sonar.asyncapi.checks.format.*;
import apiquality.sonar.asyncapi.checks.operations.*;
import apiquality.sonar.asyncapi.checks.owasp.*;
import apiquality.sonar.asyncapi.checks.parameters.*;
import apiquality.sonar.asyncapi.checks.regex.*;
import apiquality.sonar.asyncapi.checks.schemas.*;
import apiquality.sonar.asyncapi.checks.security.*;

public final class RulesLists {

    private RulesLists() {
    }

    public static List<Class<?>> getFormatChecks() {
        return Arrays.asList(
            
        );
    }

    public static List<Class<?>> getParametersChecks() {
        return Arrays.asList(
              );
    }            


    public static List<Class<?>> getSecurityChecks() {
        return Arrays.asList(
            AAR001MandatoryHttpsProtocolCheck.class,
            AAR008DefinedServerCheck.class
        );
    }

    public static List<Class<?>> getOperationsChecks() {
        return Arrays.asList(
            
        );
    }

    public static List<Class<?>> getSchemasChecks() {
        return Arrays.asList(
            
            );
    }

    public static List<Class<?>> getRegexChecks() {
        return Arrays.asList(
            );
    }

    public static List<Class<?>> getExamplesChcecks() {
        return Arrays.asList(
            
        );
    }

    public static List<Class<?>> getOWASPChecks() {
        return Arrays.asList(
            
        );
    }
    public static List<Class<?>> getWSO2Checks() {
        return Arrays.asList(
            
        );
    }

    public static List<Class<?>> getAllChecks() {
        List<Class<?>> allChecks = new LinkedList<>();
        allChecks.addAll(getFormatChecks());
        allChecks.addAll(getParametersChecks());
        allChecks.addAll(getSchemasChecks());
        allChecks.addAll(getExamplesChcecks());
        allChecks.addAll(getOWASPChecks());
        allChecks.addAll(getSecurityChecks());
        allChecks.addAll(getOperationsChecks());
        allChecks.addAll(getWSO2Checks());
        allChecks.addAll(getRegexChecks());
        return allChecks;
    }
}