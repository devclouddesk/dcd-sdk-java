package com.dcd.storage.exception;

public class AuthenticationException extends StorageException {
    public AuthenticationException(String message) {
        super(message, 401, "AUTH_ERROR");
    }

    public AuthenticationException(String message, Integer statusCode) {
        super(message, statusCode, "AUTH_ERROR");
    }

    public AuthenticationException(String message, Integer statusCode, String rawResponseBody) {
        super(message, statusCode, "AUTH_ERROR", rawResponseBody);
    }
}

