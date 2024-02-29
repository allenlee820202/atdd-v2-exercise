package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.page.OrderPage;
import com.odde.atddv2.page.WelcomePage;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
import okhttp3.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class OrderSteps {

    @Autowired
    private WelcomePage welcomePage;

    @Autowired
    private Browser browser;

    @Autowired
    private OrderPage orderPage;

    @Autowired
    private OrderRepo orderRepo;

    private String token;

    private Response orderListResponse;

    @SneakyThrows
    @Before(order = 999999)
    public void login() {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(new User().setUserName("j").setPassword("j")));
        Request request = new Request.Builder().url("http://localhost:10081/users/login").post(requestBody).build();
        token = okHttpClient.newCall(request).execute().header("Token");
        System.out.println(token);
    }

    @SneakyThrows
    @那么("显示如下订单")
    public void 显示如下订单(DataTable table) {
        table.asList().forEach(browser::shouldHaveText);
    }

    @当("用如下数据录入订单:")
    public void 用如下数据录入订单(DataTable table) {
        welcomePage.goToOrders();
        orderPage.addOrder(table.asMaps().get(0));
    }

    @假如("存在如下订单:")
    public void 存在如下订单(DataTable table) {
        var map = table.transpose().asMap(String.class, String.class);
        var order = new Order()
                .setCode((String) map.get("code"))
                .setProductName((String) map.get("productName"))
                .setTotal(BigDecimal.valueOf(Long.parseLong((String)map.get("total"))))
                .setRecipientName((String) map.get("recipientName"))
                .setStatus(Order.OrderStatus.valueOf((String) map.get("status")));
        orderRepo.save(order);
    }

    @SneakyThrows
    @当("API查询订单时")
    public void api查询订单时() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://localhost:10081/api/orders").header("Token", token).get().build();
        orderListResponse = okHttpClient.newCall(request).execute();
        System.out.print(orderListResponse);
    }

    @SneakyThrows
    @那么("返回如下订单")
    public void 返回如下订单(String json) {
        JSONAssert.assertEquals(json, orderListResponse.body().string(), true);
    }
}
