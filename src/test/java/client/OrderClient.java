package client;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(Config.BASE_URL + Config.ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .when()
                .get(Config.BASE_URL + Config.ORDER_PATH);
    }
}