package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.page.HomePage;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.java.After;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
import okhttp3.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class TestSteps {
    private final HomePage homePage = new HomePage();

    private WebDriver webDriver = null;

    @Autowired
    UserRepo userRepo;
    private Response response;

    @当("测试环境")
    public void 测试环境() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='登录']"))).isNotEmpty();
    }

    @那么("打印Token")
    public void 打印_token() {
        System.out.println("response.header(\"token\") = " + response.header("token"));
    }

    @SneakyThrows
    @那么("打印百度为您找到的相关结果数")
    public void 打印百度为您找到的相关结果数() {
        TimeUnit.SECONDS.sleep(2);
        String text = getWebDriver().findElement(xpath("//*[@id='container']/div[2]/div/div[2]/span")).getText();
        System.out.println("text = " + text);
    }

    @假如("存在用户名为{string}和密码为{string}的用户")
    public void 存在用户名为和密码为的用户(String userName, String password) {
        userRepo.deleteAll();
        userRepo.save(new User().setUserName(userName).setPassword(password));
    }

    @SneakyThrows
    @当("通过API以用户名为{string}和密码为{string}登录时")
    public void 通过api以用户名为和密码为登录时(String userName, String password) {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(new User().setUserName(userName).setPassword(password)));
        Request request = new Request.Builder().url("http://localhost:10081/users/login").post(requestBody).build();
        response = okHttpClient.newCall(request).execute();
    }

    @SneakyThrows
    @当("在百度搜索关键字{string}")
    public void 在百度搜索关键字(String keyword) {
        getWebDriver().get("http://www.baidu.com");
        getWebDriver().findElement(By.xpath("//*[@id='kw']")).sendKeys(keyword);
        getWebDriver().findElement(By.xpath("//*[@id='su']")).click();
    }

    @当("以用户名为{string}和密码为{string}登录时")
    public void 以用户名为和密码为登录时(String userName, String password) {
        homePage.open();
        homePage.login(userName, password, this);
    }

    @那么("{string}登录成功")
    public void 登录成功(String userName) {
        homePage.shouldContainText(("Welcome " + userName), this);
    }

    @那么("登录失败的错误信息是{string}")
    public void 登录失败的错误信息是(String message) {
        homePage.shouldContainText(message, this);
    }

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @After
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
        homePage.quitWebDriver();
    }

    public WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }
}
