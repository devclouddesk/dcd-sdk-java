package com.dcd.storage;

import com.dcd.storage.options.TransformationOptions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransformationBuilderTest {

    @Test
    public void testTransformationBuilderFullPipeline() {
        TransformationBuilder builder = new TransformationBuilder(
                "https://storage.devclouddesk.com",
                "avatars/user-99.jpg"
        );

        String url = builder
                .width(400)
                .height(300)
                .crop("fill")
                .format("webp")
                .quality(85)
                .blur(5)
                .grayscale(true)
                .sharpen(true)
                .build();

        assertEquals(
                "https://storage.devclouddesk.com/image/upload/w_400,h_300,c_fill,f_webp,q_85,e_blur_5,e_gray,e_sharp/avatars/user-99.webp",
                url
        );
    }

    @Test
    public void testTransformationBuilderOriginal() {
        TransformationBuilder builder = new TransformationBuilder(
                "http://localhost:7100",
                "marketing/hero.png"
        );

        assertEquals(
                "http://localhost:7100/image/upload/marketing/hero.png",
                builder.build()
        );
    }

    @Test
    public void testTransformationWithOptions() {
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("test-key")
                .build();

        TransformationOptions options = TransformationOptions.builder()
                .width(200)
                .height(200)
                .crop("thumb")
                .format("png");

        String url = client.url("products/sneaker.jpg", options);

        assertEquals(
                "https://cdn.example.com/image/upload/w_200,h_200,c_thumb,f_png/products/sneaker.png",
                url
        );
    }

    @Test
    public void testPublicUrl() {
        StorageClient client = new StorageClient("https://storage.example.com", "dcd_key");
        assertEquals(
                "https://storage.example.com/image/upload/docs/spec.pdf",
                client.getPublicUrl("docs/spec.pdf")
        );
    }
}

