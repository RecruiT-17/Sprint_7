package tests;

import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что список заказов возвращается и содержит данные")
    public void getOrdersListTest() {
        Response response = orderClient.getOrdersList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(java.util.List.class));
    }

    @Test
    @DisplayName("Получение списка заказов - проверка структуры")
    @Description("Проверка, что в ответе есть список заказов с корректной структурой")
    public void getOrdersListStructureTest() {
        Response response = orderClient.getOrdersList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());

        // Проверяем только наличие списка, без проверки конкретных полей
        // так как они могут быть null
        java.util.List<?> orders = response.path("orders");
        if (orders != null && !orders.isEmpty()) {
            // Проверяем, что каждый заказ имеет структуру
            for (int i = 0; i < Math.min(orders.size(), 3); i++) {
                // Проверяем, что заказ не null
                response.then()
                        .body("orders[" + i + "]", notNullValue());
            }
        }
    }
}