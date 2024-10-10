package dk.lyngby.util;

public class ApiProps {

    // == HIBERNATE CONFIG FILE ==
    public static final String DB_NAME = "hotel";
    public static final String DATABASE_USERNAME = "postgres";
    public static final String DATABASE_PASSWORD = "postgres";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    public static final String DATABASE_DRIVER = "org.postgresql.Driver";
    public static final String DATABASE_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
    public static final String DATABASE_TEST_CONTAINER_IMAGE = "jdbc:tc:postgresql:15.3-alpine3.18:///test-db";


    // == API CONFIG ==
    public static final int API_PORT = 7070;
    public static final String API_CONTEXT = "/api/v1";

    // == API ENDPOINTS ==
    public static final String AUTH_GROUP = "/auth";
    public static final String Hotel_GROUP = "/hotels";

    // == API SECURITY ==
    public static final String TOKEN_ISSUER = "issuer";
    public static final String TOKEN_AUDIENCE = "audience";
    public static final long TOKEN_EXPIRATION_TIME = 3600000;
    public static final String SECRET_KEY = "841D8A6C80CBA4FCAD32D5367C18C53B";

    // == API COOKIES ==
    public static final int COOKIE_MAX_AGE = 3600;

    // == API TEST ==
    public static final int TEST_PORT = 7777;
    public static final String TEST_URL = "http://localhost:" + TEST_PORT + API_CONTEXT;

    // == SQL INSERTS ==
    public static final String SQL_INSERT_HOTELS = "INSERT INTO hotel (id, hotel_name, town, category, type) VALUES " +
            "(1000, 'Sunset Resort', 'Copenhagen', 'LUXURY', 'FIVE_STAR')," +
            "(1001, 'Mountain View Lodge', 'Aarhus', 'ECONOMY', 'FOUR_STAR')," +
            "(1002, 'Seaside Inn', 'Odense', 'BUDGET', 'THREE_STAR')," +
            "(1003, 'City Central Hotel', 'Aalborg', 'BUSINESS', 'FOUR_STAR')," +
            "(1004, 'Countryside Retreat', 'Esbjerg', 'FAMILY', 'THREE_STAR')," +
            "(1005, 'Urban Stay', 'Roskilde', 'BUDGET', 'TWO_STAR')," +
            "(1006, 'Grand Plaza', 'Vejle', 'LUXURY', 'FIVE_STAR')," +
            "(1007, 'Harbor Hotel', 'Helsingør', 'ECONOMY', 'FOUR_STAR')," +
            "(1008, 'Forest Hills Resort', 'Silkeborg', 'LUXURY', 'FIVE_STAR')," +
            "(1009, 'Blue Lagoon', 'Randers', 'ECONOMY', 'THREE_STAR');";



}
