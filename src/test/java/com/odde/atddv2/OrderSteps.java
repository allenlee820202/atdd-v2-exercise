package com.odde.atddv2;

import com.odde.atddv2.entity.Order;
import com.odde.atddv2.page.OrderPage;
import com.odde.atddv2.page.WelcomePage;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
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
}
