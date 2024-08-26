package apiquality.sonar.asyncapi.checks;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apiquality.sonar.asyncapi.checks.examples.*;
import apiquality.sonar.asyncapi.checks.format.*;
import apiquality.sonar.asyncapi.checks.operations.*;

import apiquality.sonar.asyncapi.checks.schemas.*;
import apiquality.sonar.asyncapi.checks.security.*;

public final class RulesLists {

    private RulesLists() {
    }

    public static List<Class<?>> getFormatChecks() {
        return Arrays.asList(
            AAR011DefinedLicenseCheck.class,
            AAR012DeclaredOperationIDCheck.class,
            AAR013DuplicateOperationIDCheck.class,
            AAR015UndefiendContactCheck.class,
            AAR017UndefinedUrlLicenseCheck.class,
            AAR021ProvideOpSummaryCheck.class,
            AAR022DescriptionDiffersSummaryCheck.class,
            AAR027ExtenarlDocsUrlCheck.class,
            AAR028ProtocolVersionCheck.class,
            AAR029MandatoryDescriptionCheck.class,
            AAR032NumericParameterIntegrityCheck.class,
            AAR033StringParameterIntegrityCheck.class,
            AAR034NumericFormatCheck.class,
            AAR035MessageTitleCheck.class,
            AAR036BadDescriptionCheck.class,
            AAR037BidingVersionCheck.class,
            AAR038ProtocolSeverCheck.class,
            AAR042MessageIdentifierCheck.class,
            AAR044TagServerCheck.class         
        );
    }

    public static List<Class<?>> getParametersChecks() {
        return Arrays.asList(
              );
    }            


    public static List<Class<?>> getSecurityChecks() {
        return Arrays.asList(
            AAR001MandatoryHttpsProtocolCheck.class,
            AAR008DefinedServerCheck.class,
            AAR018SecuritySchemasCheck.class,
            AAR043SecurityChannelCheck.class
        );
    }

    public static List<Class<?>> getOperationsChecks() {
        return Arrays.asList(
            AAR009DeclaredTagCheck.class,
            AAR010DocumentedTagCheck.class,
            AAR030UniqueChannelNamesCheck.class,
            AAR040DefinedChannelServersCheck.class,
            AAR041ComponetChannelServerCheck.class           
        );
    }

    public static List<Class<?>> getSchemasChecks() {
        return Arrays.asList(
            AAR019IDSchemasCheck.class,
            AAR024MessageValidationCheck.class,
            AAR026MessageSchemasCheck.class
            
            );
    }

    public static List<Class<?>> getRegexChecks() {
        return Arrays.asList(
            );
    }

    public static List<Class<?>> getExamplesChcecks() {
        return Arrays.asList(
            AAR031ExamplesCheck.class,
            AAR039MoreExamplesCheck.class           
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