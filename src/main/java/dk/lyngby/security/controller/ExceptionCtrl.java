package dk.lyngby.security.controller;

import dk.lyngby.exception.ApiException;
import dk.lyngby.exception.AuthorizationException;
import dk.lyngby.exception.ValidationMessage;
import dk.lyngby.model.Message;
import dk.token.exceptions.TokenException;
import io.javalin.http.Context;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExceptionCtrl {

    private final Logger log = LoggerFactory.getLogger(ExceptionCtrl.class);

    public void exceptionHandlerNotAuthorized(AuthorizationException e, Context ctx) {
        ctx.status(e.getStatusCode());
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }

    public void tokenExceptionHandler(TokenException e, Context context) {
        context.status(400);
        context.json(new Message(400, e.getMessage()));
    }

    public void apiExceptionHandler(ApiException e, Context ctx) {
        log.error("{} {}", ctx.res().getStatus(), e.getMessage());
        ctx.status(e.getStatusCode());
        ctx.json(new Message(e.getStatusCode(), e.getMessage()));
    }

    public void exceptionHandler(Exception e, Context ctx) {
        log.error("{} {}", ctx.res().getStatus(), e.getMessage());
        ctx.status(500);
        ctx.json(new Message(500, e.getMessage()));
    }

    public void constraintViolationExceptionHandler(ConstraintViolationException e, Context ctx) {
        ctx.status(500);
        ctx.json(new Message(e.getErrorCode(), e.getSQLException().getMessage()));
    }

    public void validationExceptionHandler(ValidationException e, Context ctx) {
        Map<String, List<ValidationError<Object>>> errors = e.getErrors();
        List<ValidationError<Object>> errorList = new ArrayList<>();
        int statusCode = 0;

        if (errors.containsKey("REQUEST_BODY")) {
            errorList = errors.get("REQUEST_BODY");
            statusCode = 400;
        }

        if (errors.containsKey("id")) {
            errorList = errors.get("id");
            statusCode = 404;
        }

        String message = errorList.get(0).getMessage();
        Map<String, Object> args = errorList.get(0).getArgs();
        Object value = errorList.get(0).getValue();

        ctx.status(statusCode);
        ctx.json(new ValidationMessage(message, args, value));
    }
}
