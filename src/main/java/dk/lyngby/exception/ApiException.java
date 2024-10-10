package dk.lyngby.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final int statusCode;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
