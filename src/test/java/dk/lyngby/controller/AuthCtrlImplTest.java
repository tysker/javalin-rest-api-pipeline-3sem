package dk.lyngby.controller;

import dk.lyngby.config.AppConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.util.ApiProps;
import dk.lyngby.util.LoginUtil;
import io.restassured.response.ValidatableResponse;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class AuthCtrlImplTest {

    private static final String BASE_URL = ApiProps.TEST_URL;

    @BeforeAll
    public static void setUp() {
        HibernateConfig.setTest(true);
        EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactory();
        AppConfig.startServer(ApiProps.TEST_PORT);

        LoginUtil.createTestUsers(emfTest);
    }

    @AfterAll
    static void tearDown() {
        HibernateConfig.setTest(false);
        AppConfig.stopServer();
    }

    @Test
    void login() {

        given()
                .contentType("application/json")
                .body("{\"username\": \"admin\", \"password\": \"admin123\"}")
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .statusCode(200)
                .body(
                        "username", equalTo("admin"),
                        "roles", hasItem("ADMIN"),
                        "token", notNullValue()
                );
    }

    @Test
    void register() {
        given()
                .contentType("application/json")
                .body("{\"username\": \"TestUser\", \"password\": \"test123\", \"roleList\": [\"USER\"]}")
                .when()
                .post(BASE_URL + "/auth/register")
                .then()
                .statusCode(201)
                .body(
                        "username", equalTo("TestUser"),
                        "roles", hasItem("USER"),
                        "token", notNullValue()
                );
    }

    @Test
    void logout() {
        // == test if user is logged out ==
    }

    @Test
    void refreshToken() {
        // == test if token is refreshed ==
    }
}