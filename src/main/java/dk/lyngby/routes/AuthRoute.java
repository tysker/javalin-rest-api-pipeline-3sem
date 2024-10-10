package dk.lyngby.routes;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.security.controller.AuthCtrlImpl;
import dk.lyngby.security.AuthDao;
import dk.lyngby.security.model.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthRoute {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final AuthDao authDao = new AuthDao(emf);
    private final AuthCtrlImpl authCtrlImpl = new AuthCtrlImpl(authDao);

    public EndpointGroup getRoutes() {

        return () -> {
            path("", () -> {
                post("/login", authCtrlImpl::login, Role.RoleName.ANYONE);
                post("/register", authCtrlImpl::register, Role.RoleName.ANYONE);
            });
        };
    }
}
