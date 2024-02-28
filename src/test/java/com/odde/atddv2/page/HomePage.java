package com.odde.atddv2.page;

import com.odde.atddv2.TestSteps;

public class HomePage {
    private final Browser browser = new Browser();

    public void open() {
        browser.getWebDriver().get("http://host.docker.internal:10081");
    }

    public void login(String userName, String password) {
        browser.inputByPlaceholder("用户名", userName);
        browser.inputByPlaceholder("密码", password);
        browser.clickByText("登录");
    }

    public void shouldContainText(String text, TestSteps testSteps) {
        browser.shouldContainText(text, testSteps);
    }

    public void quitWebDriver() {
        browser.quitWebDriver();
    }
}
