package dk.lyngby.routes;

import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.security.controller.ExceptionCtrl;
import dk.lyngby.util.ApiProps;
import dk.token.exceptions.TokenException;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final HotelRoute hotelRoutes = new HotelRoute();
    private final AuthRoute authRoutes = new AuthRoute();
    private final ExceptionCtrl exceptionController = new ExceptionCtrl();
    private final Logger log = LoggerFactory.getLogger(Routes.class);
    private int count = 0;

    private void requestInfoHandler(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        ctx.attribute("requestInfo", requestInfo);
    }

    public EndpointGroup getApiRoutes(Javalin app) {
        return () -> {
            app.before(this::requestInfoHandler);
            app.routes(() -> {
                path(ApiProps.AUTH_GROUP, authRoutes.getRoutes());
                path(ApiProps.Hotel_GROUP, hotelRoutes.getDogRoutes());
            });
            app.after(ctx -> log.info(" Request {} - {} was handled with status code {}", count++, ctx.attribute("requestInfo"), ctx.status()));
            app.exception(ConstraintViolationException.class, exceptionController::constraintViolationExceptionHandler);
            app.exception(ValidationException.class, exceptionController::validationExceptionHandler);
            app.exception(ApiException.class, exceptionController::apiExceptionHandler);
            app.exception(AuthorizationException.class, exceptionController::exceptionHandlerNotAuthorized);
            app.exception(TokenException.class, exceptionController::tokenExceptionHandler);
            app.exception(Exception.class, exceptionController::exceptionHandler);
        };
    }
}
