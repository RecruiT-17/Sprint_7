package tests;

import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private CourierClient courierClient;
    private Courier createdCourier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        createdCourier = TestDataGenerator.generateRandomCourier();
        courierClient.createCourier(createdCourier);
        courierId = courierClient.getCourierId(
                new CourierCredentials(createdCourier.getLogin(), createdCourier.getPassword())
        );
    }

    @After
    public void tearDown() {
        if (courierId > 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Логин курьера - успешная авторизация")
    @Description("Проверка успешной авторизации и получения id")
    public void loginCourierSuccessTest() {
        CourierCredentials credentials = new CourierCredentials(
                createdCourier.getLogin(),
                createdCourier.getPassword()
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин курьера - неправильный логин")
    @Description("Проверка ошибки при авторизации с неправильным логином")
    public void loginCourierWrongLoginTest() {
        CourierCredentials credentials = new CourierCredentials(
                "wrong_login",
                createdCourier.getPassword()
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера - неправильный пароль")
    @Description("Проверка ошибки при авторизации с неправильным паролем")
    public void loginCourierWrongPasswordTest() {
        CourierCredentials credentials = new CourierCredentials(
                createdCourier.getLogin(),
                "wrong_password"
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера - без логина")
    @Description("Проверка ошибки при авторизации без логина")
    public void loginCourierWithoutLoginTest() {
        CourierCredentials credentials = new CourierCredentials(null, createdCourier.getPassword());

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера - без пароля")
    @Description("Проверка ошибки при авторизации без пароля")
    public void loginCourierWithoutPasswordTest() {
        CourierCredentials credentials = new CourierCredentials(createdCourier.getLogin(), null);

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(400)  // ← Ожидает 400, получает 504 → тест падает
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера - несуществующий пользователь")
    @Description("Проверка ошибки при авторизации несуществующего пользователя")
    public void loginCourierNonExistentTest() {
        CourierCredentials credentials = new CourierCredentials(
                "nonexistent_user",
                "nonexistent_password"
        );

        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}