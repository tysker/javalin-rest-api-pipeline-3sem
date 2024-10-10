package dk.lyngby.routes;

import dk.lyngby.util.ApiProps;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final HotelRoute hotelRoutes = new HotelRoute();
    private final AuthRoute authRoutes = new AuthRoute();

    public EndpointGroup getApiRoutes() {
        return () -> {
            path(ApiProps.AUTH_GROUP, authRoutes.getRoutes());
            path(ApiProps.Hotel_GROUP, hotelRoutes.getDogRoutes());
        };
    }
}
