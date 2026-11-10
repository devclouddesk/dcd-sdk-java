package com.dcd.storage;

import com.dcd.storage.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    @Test
    public void testStorageExceptionBase() {
        StorageException ex = new StorageException("Something went wrong", 500, "INTERNAL_ERROR", "{\"error\":\"fail\"}");

        assertTrue(ex instanceof RuntimeException);
        assertEquals("Something went wrong", ex.getMessage());
        assertEquals(500, ex.getStatusCode());
        assertEquals("INTERNAL_ERROR", ex.getErrorCode());
        assertEquals("{\"error\":\"fail\"}", ex.getRawResponseBody());
    }

    @Test
    public void testAuthenticationException() {
        AuthenticationException ex = new AuthenticationException("Bad key", 401, "{\"error\":\"invalid_key\"}");

        assertTrue(ex instanceof StorageException);
        assertEquals("Bad key", ex.getMessage());
        assertEquals(401, ex.getStatusCode());
        assertEquals("AUTH_ERROR", ex.getErrorCode());
        assertEquals("{\"error\":\"invalid_key\"}", ex.getRawResponseBody());
    }

    @Test
    public void testNotFoundException() {
        NotFoundException ex = new NotFoundException("Image not found", 404, "{\"error\":\"not_found\"}");

        assertTrue(ex instanceof StorageException);
        assertEquals("Image not found", ex.getMessage());
        assertEquals(404, ex.getStatusCode());
        assertEquals("NOT_FOUND", ex.getErrorCode());
    }

    @Test
    public void testPayloadTooLargeException() {
        PayloadTooLargeException ex = new PayloadTooLargeException("Payload exceeds 50MB", 413, "{\"error\":\"too_large\"}");

        assertTrue(ex instanceof StorageException);
        assertEquals("Payload exceeds 50MB", ex.getMessage());
        assertEquals(413, ex.getStatusCode());
        assertEquals("PAYLOAD_TOO_LARGE", ex.getErrorCode());
    }

    @Test
    public void testNetworkTimeoutException() {
        NetworkTimeoutException ex = new NetworkTimeoutException("Request timed out", 408, "{\"error\":\"timeout\"}");

        assertTrue(ex instanceof StorageException);
        assertEquals("Request timed out", ex.getMessage());
        assertEquals(408, ex.getStatusCode());
        assertEquals("REQUEST_TIMEOUT", ex.getErrorCode());
    }

    @Test
    public void testUploadException() {
        UploadException ex = new UploadException("Upload broken", 502, "{\"error\":\"bad_gateway\"}");

        assertTrue(ex instanceof StorageException);
        assertEquals("Upload broken", ex.getMessage());
        assertEquals(502, ex.getStatusCode());
        assertEquals("UPLOAD_ERROR", ex.getErrorCode());
    }
}

