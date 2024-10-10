package dk.lyngby.security.controller;

import dk.token.exceptions.TokenException;
import io.javalin.http.Context;

import java.io.IOException;

public interface AuthCtrl {
    void login(Context ctx) throws IOException, TokenException;
    void register(Context ctx) throws IOException, TokenException;
}
