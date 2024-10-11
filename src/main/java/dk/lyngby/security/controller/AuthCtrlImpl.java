package dk.lyngby.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.lyngby.config.AppConfig;
import dk.lyngby.security.AuthDao;
import dk.lyngby.security.dto.LoginDto;
import dk.lyngby.security.dto.RegisterDto;
import dk.lyngby.security.model.Role;
import dk.lyngby.security.model.User;
import dk.lyngby.util.ApiProps;
import dk.token.TokenFactory;
import dk.token.exceptions.TokenException;
import dk.token.model.ClaimBuilder;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class AuthCtrlImpl implements AuthCtrl {

    private final AuthDao authDao;

    public AuthCtrlImpl(AuthDao _authDao) {
        this.authDao = _authDao;
    }

    @Override
    public void login(@NotNull Context ctx) throws IOException, TokenException {
        LoginDto loginDto = ctx.bodyAsClass(LoginDto.class);
        User user = authDao.verifyUser(loginDto.username(), loginDto.password());

        // == create an array of roles ==
        List<Role.RoleName> roleList = user.getRoleList().stream().map(Role::getRoleName).toList().stream().toList();

        // == create token ==
        ClaimBuilder claimBuilder = AppConfig.getClaimBuilder(user, roleList.toString());
        String token = TokenFactory.createToken(claimBuilder, ApiProps.TOKEN_SECRET_KEY);

        ctx.status(200);
        ctx.json(createResponseObject(user.getUsername(), roleList, token));
    }

    @Override
    public void register(@NotNull Context ctx) throws IOException, TokenException {
        // == validate user ==
        RegisterDto registerDto = validateUser(ctx);
        // == check if user exists ==
        authDao.checkUser(registerDto.username());
        // == check if role exists ==
        authDao.checkRoles(registerDto.roleList());
        // == register user ==
        User user = authDao.registerUser(registerDto.username(), registerDto.password(), registerDto.roleList());
        // == create an array of roles ==
        List<Role.RoleName> roleList = registerDto.roleList().stream().map(Role.RoleName::valueOf).toList().stream().toList();
        // == create token ==
        ClaimBuilder claimBuilder = AppConfig.getClaimBuilder(new User(registerDto.username(), registerDto.password()), roleList.toString());
        String token = TokenFactory.createToken(claimBuilder, ApiProps.TOKEN_SECRET_KEY);

        ctx.status(201);
        ctx.cookie("token", token);
        ctx.json(createResponseObject(registerDto.username(), roleList, token));
    }

    private ObjectNode createResponseObject(String userName, List<Role.RoleName> roles, String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode respondNode = mapper.createObjectNode();
        respondNode.put("username", userName);
        respondNode.putPOJO("roles", roles);
        respondNode.put("token", token);
        return respondNode;
    }


    private RegisterDto validateUser(Context ctx) {
        return ctx.bodyValidator(RegisterDto.class)
                .check(user -> user.username() != null && user.password() != null, "Username and password must be set")
                .check(user -> user.username().length() >= 5, "Username must be at least 5 characters")
                .check(user -> user.password().length() >= 5, "Password must be at least 5 characters")
                .check(user -> !user.roleList().isEmpty(), "User must have at least one role")
                .get();
    }
}
