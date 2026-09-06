package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import config.Config;  // ← правильный импорт
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()  // ← убрали точку с запятой!
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(Config.BASE_URL + Config.ORDER_PATH);  // ← Config с большой буквы
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()  // ← убрали точку с запятой!
                .when()
                .get(Config.BASE_URL + Config.ORDER_PATH);  // ← Config с большой буквы
    }
}