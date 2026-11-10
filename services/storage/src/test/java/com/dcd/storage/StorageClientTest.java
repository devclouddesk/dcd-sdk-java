package com.dcd.storage;

import com.dcd.storage.exception.AuthenticationException;
import com.dcd.storage.options.SignedDownloadOptions;
import com.dcd.storage.options.SignUploadOptions;
import com.dcd.storage.options.TransformationOptions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class StorageClientTest {

    @Test
    public void testBuilderPattern() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://storage.devclouddesk.com")
                .apiKey("test-api-key")
                .timeout(Duration.ofSeconds(45))
                .deliveryPrefix("cdn/assets")
                .build();

        assertNotNull(client);
        assertEquals(
                "https://storage.devclouddesk.com/cdn/assets/banner.jpg",
                client.getPublicUrl("banner.jpg")
        );
    }

    @Test
    public void testBuilderWithoutEndpointThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            StorageClient.builder().endpoint("").build();
        });
    }

    @Test
    public void testGetPublicUrlCleanSlashes() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("key")
                .build();

        assertEquals(
                "https://cdn.example.com/image/upload/docs/file.pdf",
                client.getPublicUrl("docs/file.pdf")
        );
        assertEquals(
                "https://cdn.example.com/image/upload/deep/nested/item.png",
                client.getPublicUrl("///deep/nested/item.png")
        );
    }

    @Test
    public void testUrlReturnsTransformationBuilder() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("key")
                .build();

        TransformationBuilder builder = client.url("photos/dog.jpg");
        assertNotNull(builder);
        assertEquals(
                "https://cdn.example.com/image/upload/w_300/photos/dog.jpg",
                builder.width(300).build()
        );
    }

    @Test
    public void testUrlWithDirectTransformationOptions() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("key")
                .build();

        TransformationOptions opts = TransformationOptions.builder()
                .width(400)
                .height(300)
                .crop("fill")
                .format("webp");

        String url = client.url("photos/hero.png", opts);
        assertEquals(
                "https://cdn.example.com/image/upload/w_400,h_300,c_fill,f_webp/photos/hero.webp",
                url
        );
    }

    @Test
    public void testCloudinaryCompatibleNamespacesExist() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("key")
                .build();

        assertNotNull(client.uploader);
        assertNotNull(client.api);
    }

    @Test
    public void testGetSignedUrlWithoutApiKeyThrowsAuthenticationException() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("")
                .build();

        assertThrows(AuthenticationException.class, () -> {
            client.getSignedUrl("private/file.pdf", SignedDownloadOptions.builder());
        });
    }

    @Test
    public void testCreateSignedUploadTokenWithoutApiKeyThrowsAuthenticationException() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("")
                .build();

        assertThrows(AuthenticationException.class, () -> {
            client.createSignedUploadToken(SignUploadOptions.builder().folder("uploads"));
        });
    }
}

