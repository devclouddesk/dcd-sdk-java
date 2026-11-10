package com.dcd.storage.exception;

public class NotFoundException extends StorageException {
    public NotFoundException(String message) {
        super(message, 404, "NOT_FOUND");
    }

    public NotFoundException(String message, Integer statusCode) {
        super(message, statusCode, "NOT_FOUND");
    }

    public NotFoundException(String message, Integer statusCode, String rawResponseBody) {
        super(message, statusCode, "NOT_FOUND", rawResponseBody);
    }
}

