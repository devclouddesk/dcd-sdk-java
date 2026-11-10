package com.dcd.storage;

import com.dcd.storage.options.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class OptionsTest {

    @Test
    public void testFileListOptions() {
        FileListOptions opts = FileListOptions.builder()
                .folder("uploads/images")
                .page(3)
                .limit(50);

        assertEquals("uploads/images", opts.getFolder());
        assertEquals(3, opts.getPage());
        assertEquals(50, opts.getLimit());
    }

    @Test
    public void testSignUploadOptions() {
        SignUploadOptions opts = SignUploadOptions.builder()
                .folder("kyc-docs")
                .maxBytes(5242880L)
                .expiresInSeconds(600)
                .webhookUrl("https://example.com/callback")
                .access("private");

        assertEquals("kyc-docs", opts.getFolder());
        assertEquals(5242880L, opts.getMaxBytes());
        assertEquals(600, opts.getExpiresInSeconds());
        assertEquals("https://example.com/callback", opts.getWebhookUrl());
        assertEquals("private", opts.getAccess());
    }

    @Test
    public void testSignedDownloadOptions() {
        SignedDownloadOptions opts = SignedDownloadOptions.builder()
                .expiresInSeconds(1200)
                .resourceType("raw")
                .transform("w_500");

        assertEquals(1200, opts.getExpiresInSeconds());
        assertEquals("raw", opts.getResourceType());
        assertEquals("w_500", opts.getTransform());
    }

    @Test
    public void testTransformationOptions() {
        TransformationOptions opts = TransformationOptions.builder()
                .resize(800, 600, "fill")
                .format("webp")
                .quality(85)
                .blur(5)
                .grayscale(true)
                .sharpen(true)
                .rotation("90");

        assertEquals(800, opts.getWidth());
        assertEquals(600, opts.getHeight());
        assertEquals("fill", opts.getCrop());
        assertEquals("webp", opts.getFormat());
        assertEquals("85", opts.getQuality());
        assertEquals(5, opts.getBlur());
        assertEquals(Boolean.TRUE, opts.getGrayscale());
        assertEquals(Boolean.TRUE, opts.getSharpen());
        assertEquals("90", opts.getRotation());
    }

    @Test
    public void testUploadOptions() {
        UploadOptions opts = UploadOptions.builder()
                .folder("avatars")
                .publicId("custom-avatar")
                .filename("user.png")
                .overwrite(true)
                .uniqueFilename(false)
                .access("private")
                .token("tok_123")
                .webhookUrl("https://hook.com")
                .tags(Arrays.asList("user", "profile"))
                .timeoutMs(15000);

        assertEquals("avatars", opts.getFolder());
        assertEquals("custom-avatar", opts.getPublicId());
        assertEquals("user.png", opts.getFilename());
        assertEquals(Boolean.TRUE, opts.getOverwrite());
        assertEquals(Boolean.FALSE, opts.getUniqueFilename());
        assertEquals("private", opts.getAccess());
        assertEquals("tok_123", opts.getToken());
        assertEquals("https://hook.com", opts.getWebhookUrl());
        assertEquals(2, opts.getTags().size());
        assertEquals(15000, opts.getTimeoutMs());
    }
}

