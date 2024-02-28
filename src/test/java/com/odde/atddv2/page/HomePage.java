package com.odde.atddv2.page;

import com.odde.atddv2.TestSteps;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.openqa.selenium.By.xpath;

public class HomePage {
    private WebDriver webDriver = null;

    public void open() {
        getWebDriver().get("http://host.docker.internal:10081");
    }

    public void login(String userName, String password, TestSteps testSteps) {
        inputByPlaceholder("用户名", userName);
        inputByPlaceholder("密码", password);
        clickByText("登录");
    }

    private void clickByText(String text) {
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[text()='" + text + "']")), Objects::nonNull).click();
    }

    private void inputByPlaceholder(String placeholder, String text) {
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[@placeholder='" + placeholder + "']")), Objects::nonNull).sendKeys(text);
    }

    public void shouldContainText(String text, TestSteps testSteps) {
        await().ignoreExceptions().untilAsserted(() -> assertThat(getWebDriver().findElements(xpath("//*[text()='" + text + "']"))).isNotEmpty());
    }

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }
}
