package dk.lyngby.controller;

import dk.lyngby.config.AppConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.util.ApiProps;
import dk.lyngby.util.LoginUtil;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class HotelCtrlImplTest {

    private static final String BASE_URL = ApiProps.TEST_URL;
    private static String adminToken;
    private static String userToken;

    @BeforeAll
    public static void setUp() {
        HibernateConfig.setTest(true);
        EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactory();
        AppConfig.startServer(ApiProps.TEST_PORT);
        LoginUtil.createTestUsers(emfTest);
        adminToken = LoginUtil.getAdminToken();
        userToken = LoginUtil.getUserToken();
    }

    @BeforeEach
    void setUpEach() {
        EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactory();
        try (var em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery(ApiProps.SQL_INSERT_HOTELS).executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDown() {
        HibernateConfig.setTest(false);
        AppConfig.stopServer();
    }

    @Test
    @DisplayName("Get Hotel By Id with user token")
    void getHotelByIdWithAuthToken() {

        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/hotels/1000")
                .then()
                .statusCode(200)
                .body(
                        "hotelName", is(equalTo("Sunset Resort")),
                        "town", is(equalTo("Copenhagen")),
                        "hotelCategory", is(equalTo("LUXURY")),
                        "hotelType", is(equalTo("FIVE_STAR"))
                );
    }

    @Test
    @DisplayName("Get Hotel By Id without user token")
    void getHotelByIdWithoutAuthToken() {

        given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/hotels/1")
                .then()
                .statusCode(400)
                .body("message", is(equalTo("No Token provided")));
    }

    @Test
    void getAllHotels() {

        given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + "/hotels")
                .then()
                .statusCode(200)
                .body("size()", is(equalTo(10)));
    }

    @Test
    @DisplayName("Create Hotel Success")
    void createHotelSuccess() {

        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body("{\"hotelName\":\"TEST\",\"town\":\"TEST\",\"hotelCategory\":\"LUXURY\",\"hotelType\":\"FIVE_STAR\"}")
                .when()
                .post(BASE_URL + "/hotels")
                .then()
                .statusCode(201)
                .body("message", equalTo("Hotel created"));

    }

    @Test
    @DisplayName("Create Hotel Fail: Hotel json mismatch")
    void createHotelJsonMismatch() {

        given()
                .contentType("application/json")
                .body("{\"NOT_EXISTING_HOTEL_PROPERTY\":\"TEST\",\"town\":\"TEST\",\"hotelCategory\":\"FIVE_STAR\",\"hotelType\":\"LUXURY\"}")
                .when()
                .post(BASE_URL + "/hotels")
                .then()
                .statusCode(400);
    }

    @Test
    void updateHotel() {

        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body("{\"hotelName\":\"UPDATE\",\"town\":\"UPDATE\",\"hotelCategory\":\"LUXURY\",\"hotelType\":\"FIVE_STAR\"}")
                .when()
                .put(BASE_URL + "/hotels/1002")
                .then()
                .statusCode(200)
                .body("message", equalTo("Hotel updated"));
    }

    @Test
    void deleteHotel() {

        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .when()
                .delete(BASE_URL + "/hotels/1003")
                .then()
                .statusCode(204);
    }
}