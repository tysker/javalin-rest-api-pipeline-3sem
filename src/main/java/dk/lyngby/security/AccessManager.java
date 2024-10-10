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
import io.javalin.security.RouteRole;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class AccessManager {

    public void checkRoleAccess(Context ctx) throws Exception {

        if (!ctx.routeRoles().contains(Role.RoleName.ANYONE)) {
            boolean isAuthorized = false;

            Role.RoleName[] userRoles = getUserRoles(ctx);

            for (RouteRole role : userRoles) {
                if (ctx.routeRoles().contains(role)) {
                    isAuthorized = true;
                    break;
                }
            }

            if (!isAuthorized) {
                throw new AuthorizationException(401, "You are not authorized to perform this action");
            }
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
