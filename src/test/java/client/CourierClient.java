package client;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(Config.BASE_URL + Config.COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post(Config.BASE_URL + Config.LOGIN_PATH);
    }

    @Step("Удаление курьера по id: {id}")
    public void deleteCourier(int id) {
        given()
                .when()
                .delete(Config.BASE_URL + Config.COURIER_PATH + "/" + id);
    }

    @Step("Получение id курьера")
    public int getCourierId(CourierCredentials credentials) {
        return loginCourier(credentials)
                .then()
                .extract()
                .path("id");
    }
}