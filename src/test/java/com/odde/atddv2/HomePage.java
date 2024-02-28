package com.odde.atddv2;

public class HomePage {
    private final TestSteps testSteps;

    public HomePage(TestSteps testSteps) {
        this.testSteps = testSteps;
    }

    void open() {
        testSteps.getWebDriver().get("http://host.docker.internal:10081");
    }
}