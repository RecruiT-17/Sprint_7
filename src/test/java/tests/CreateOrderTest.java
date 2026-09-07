package tests;

import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.TestDataGenerator;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final Order order;
    private final String testDescription;
    private final OrderClient orderClient;

    public CreateOrderTest(Order order, String testDescription) {
        this.order = order;
        this.testDescription = testDescription;
        this.orderClient = new OrderClient();
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {TestDataGenerator.generateOrderWithColor("BLACK"), "Заказ с цветом BLACK"},
                {TestDataGenerator.generateOrderWithColor("GREY"), "Заказ с цветом GREY"},
                {TestDataGenerator.generateOrderWithColors(Arrays.asList("BLACK", "GREY")), "Заказ с обоими цветами"},
                {TestDataGenerator.generateOrderWithoutColor(), "Заказ без указания цвета"}
        });
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Проверка создания заказа с разными вариантами цвета")
    public void createOrderTest() {
        Response response = orderClient.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue())
                .body("track", instanceOf(Integer.class));

        System.out.println("Response: " + response.asString());
    }
}