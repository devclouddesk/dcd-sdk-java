package com.dcd.storage.exception;

public class PayloadTooLargeException extends StorageException {
    public PayloadTooLargeException(String message) {
        super(message, 413, "PAYLOAD_TOO_LARGE");
    }

    public PayloadTooLargeException(String message, Integer statusCode) {
        super(message, statusCode, "PAYLOAD_TOO_LARGE");
    }

    public PayloadTooLargeException(String message, Integer statusCode, String rawResponseBody) {
        super(message, statusCode, "PAYLOAD_TOO_LARGE", rawResponseBody);
    }
}

