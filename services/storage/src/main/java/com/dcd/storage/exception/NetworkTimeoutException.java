package com.dcd.storage.exception;

public class NetworkTimeoutException extends StorageException {
    public NetworkTimeoutException(String message) {
        super(message, 408, "REQUEST_TIMEOUT");
    }

    public NetworkTimeoutException(String message, Integer statusCode) {
        super(message, statusCode, "REQUEST_TIMEOUT");
    }

    public NetworkTimeoutException(String message, Integer statusCode, String rawResponseBody) {
        super(message, statusCode, "REQUEST_TIMEOUT", rawResponseBody);
    }
}

