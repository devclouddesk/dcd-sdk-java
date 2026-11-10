package com.dcd.storage.exception;

public class UploadException extends StorageException {
    public UploadException(String message) {
        super(message, null, "UPLOAD_ERROR");
    }

    public UploadException(String message, Integer statusCode) {
        super(message, statusCode, "UPLOAD_ERROR");
    }

    public UploadException(String message, Integer statusCode, String rawResponseBody) {
        super(message, statusCode, "UPLOAD_ERROR", rawResponseBody);
    }
}

