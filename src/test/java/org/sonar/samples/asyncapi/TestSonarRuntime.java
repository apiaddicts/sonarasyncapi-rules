package org.sonar.samples.asyncapi;

import org.sonar.api.SonarEdition;
import org.sonar.api.SonarProduct;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.utils.Version;

public class TestSonarRuntime implements SonarRuntime {

    private static final TestSonarRuntime INSTANCE = new TestSonarRuntime();

    public static SonarRuntime create() {
        return INSTANCE;
    }

    @Override
    public Version getApiVersion() {
        return Version.create(9, 4);
    }

    @Override
    public SonarProduct getProduct() {
        return SonarProduct.SONARQUBE;
    }

    @Override
    public SonarQubeSide getSonarQubeSide() {
        return SonarQubeSide.SCANNER;
    }

    @Override
    public SonarEdition getEdition() {
        return SonarEdition.COMMUNITY;
    }
}
