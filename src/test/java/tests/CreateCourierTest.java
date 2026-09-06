package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.CourierClient;
import model.Courier;
import model.CourierCredentials;
import utils.TestDataGenerator;

import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Courier createdCourier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (createdCourier != null && createdCourier.getLogin() != null && createdCourier.getPassword() != null) {
            try {
                courierId = courierClient.getCourierId(
                        new CourierCredentials(createdCourier.getLogin(), createdCourier.getPassword())
                );
                if (courierId > 0) {
                    courierClient.deleteCourier(courierId);
                }
            } catch (Exception e) {
                // Игнорируем ошибки удаления
            }
        }
    }

    @Test
    @DisplayName("Создание курьера - успешное создание")
    @Description("Проверка, что курьера можно создать и получить ok: true")
    public void createCourierSuccessTest() {
        createdCourier = TestDataGenerator.generateRandomCourier();
        Response response = courierClient.createCourier(createdCourier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера - нельзя создать двух одинаковых")
    @Description("Проверка ошибки при создании курьера с уже существующим логином")
    public void createDuplicateCourierTest() {
        createdCourier = TestDataGenerator.generateRandomCourier();
        courierClient.createCourier(createdCourier);

        Courier duplicateCourier = new Courier(
                createdCourier.getLogin(),
                "new_password",
                "New_Name"
        );

        Response response = courierClient.createCourier(duplicateCourier);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера - без обязательного поля login")
    @Description("Проверка ошибки при создании курьера без логина")
    public void createCourierWithoutLoginTest() {
        createdCourier = TestDataGenerator.generateCourierWithoutLogin();
        Response response = courierClient.createCourier(createdCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера - без обязательного поля password")
    @Description("Проверка ошибки при создании курьера без пароля")
    public void createCourierWithoutPasswordTest() {
        createdCourier = TestDataGenerator.generateCourierWithoutPassword();
        Response response = courierClient.createCourier(createdCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера - с минимальными данными")
    @Description("Проверка, что курьера можно создать только с логином и паролем")
    public void createCourierWithMinimalDataTest() {
        String login = TestDataGenerator.generateUniqueLogin();
        String password = "test_pass_123";
        createdCourier = new Courier(login, password, null);

        Response response = courierClient.createCourier(createdCourier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера - пустой запрос")
    @Description("Проверка ошибки при отправке пустого запроса")
    public void createCourierEmptyRequestTest() {
        Courier emptyCourier = new Courier();
        Response response = courierClient.createCourier(emptyCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}