package dk.lyngby.config;

import dk.lyngby.security.AccessManager;
import dk.lyngby.security.model.User;
import dk.lyngby.routes.Routes;
import dk.lyngby.util.ApiProps;
import dk.token.model.ClaimBuilder;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
    private static final AccessManager amc = new AccessManager();
    private static final Routes routes = new Routes();
    private static Javalin app;

    private static void configuration(JavalinConfig config) {
        // == server ==
        config.routing.contextPath = "/api/v1";// base path for all routes
        config.http.defaultContentType = "application/json"; // default content type for requests

        // == plugin ==
        config.plugins.register(new RouteOverviewPlugin("/routes")); // enables route overview at /routes
        config.plugins.enableDevLogging(); // enables development logging

        // == routes ==
        config.accessManager(amc::accessManagerHandler);
    }

    // == security ==
    private static void corsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }

    public static ClaimBuilder getClaimBuilder(User user, String roles) throws IOException {
        return ClaimBuilder.builder()
                .issuer(ApiProps.TOKEN_ISSUER)
                .audience(ApiProps.TOKEN_AUDIENCE)
                .claimSet(Map.of("username", user.getUsername(), "roles", roles))
                .expirationTime(ApiProps.TOKEN_EXPIRATION_TIME)
                .issueTime(3600000L)
                .build();
    }

    public static String getProperty(String propName) throws IOException {
        try (InputStream is = HibernateConfig.class.getClassLoader().getResourceAsStream("properties-from-pom.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            return prop.getProperty(propName);
        } catch (IOException ex) {
            log.error("Could not read property from pom file. Build Maven!");
            throw new IOException("Could not read property from pom file. Build Maven!");
        }
    }

    public static void startServer(int port) {
        app = Javalin.create(AppConfig::configuration);
        app.updateConfig(AppConfig::configuration);
        app.before(AppConfig::corsHeaders);
        app.options("/*", AppConfig::corsHeaders);
        app.routes(routes.getApiRoutes(app));
        app.start(port);
        log.info("Server started on port: {}", port);
    }

    public static void stopServer() {
        app.stop();
    }
}