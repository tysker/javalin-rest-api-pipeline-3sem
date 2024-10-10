package dk.lyngby.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.lyngby.config.AppConfig;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.security.dto.TokenDto;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.security.model.Role;
import dk.lyngby.security.model.User;
import dk.lyngby.util.ApiProps;
import dk.token.TokenFactory;
import dk.token.exceptions.TokenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AccessManager {

    public void accessManagerHandler(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {
        System.out.println(permittedRoles);
        String path = ctx.path();
        boolean isAuthorized = false;

        if (path.equals("/api/v1/routes") || permittedRoles.contains(Role.RoleName.ANYONE) || Objects.equals(ctx.method().toString(), "OPTIONS")) {
            handler.handle(ctx);
            return;
        } else {
            RouteRole[] roles = getUserRoles(ctx);
            for (RouteRole role : roles) {
                if (permittedRoles.contains(role)) {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (isAuthorized) {
            handler.handle(ctx);
        } else {
            throw new AuthorizationException(401, "You are not authorized to perform this action");
        }
    }

    private Role.RoleName[] getUserRoles(Context ctx) throws TokenException {

        AuthDao authDao = new AuthDao(HibernateConfig.getEntityManagerFactory());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String token;

        try {
            token = Objects.requireNonNull(ctx.header("Authorization")).split(" ")[1];
        } catch (Exception e) {
            throw new TokenException("No Token provided", e);
        }

        try {
            String usernameFromToken = TokenFactory.getUsernameFromToken(token);
            User user = authDao.getUser(usernameFromToken);

            String roles = user.getRoleList().stream().map(role -> role.getRoleName().toString()).collect(Collectors.joining(", ", "[", "]"));

            String claim = TokenFactory.verifyToken(token, ApiProps.SECRET_KEY, AppConfig.getClaimBuilder(user, roles));
            return gson.fromJson(claim, TokenDto.class).roles();
        } catch (Exception e) {
            throw new TokenException("Token is not valid", e);
        }

    }

}
