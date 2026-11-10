package com.dcd.storage.exception;

public class StorageException extends RuntimeException {
    private final Integer statusCode;
    private final String errorCode;
    private final String rawResponseBody;

    public StorageException(String message) {
        this(message, null, null, null, null);
    }

    public StorageException(String message, Integer statusCode) {
        this(message, statusCode, null, null, null);
    }

    public StorageException(String message, Integer statusCode, String errorCode) {
        this(message, statusCode, errorCode, null, null);
    }

    public StorageException(String message, Integer statusCode, String errorCode, String rawResponseBody) {
        this(message, statusCode, errorCode, rawResponseBody, null);
    }

    public StorageException(String message, Integer statusCode, String errorCode, String rawResponseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.rawResponseBody = rawResponseBody;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getRawResponseBody() {
        return rawResponseBody;
    }
}

