package com.dcd.storage.example;

import com.dcd.storage.StorageClient;
import com.dcd.storage.options.TransformationOptions;

public class Quickstart {
    public static void main(String[] args) {
        String endpoint = System.getenv("DCD_STORAGE_ENDPOINT") != null
                ? System.getenv("DCD_STORAGE_ENDPOINT")
                : "http://127.0.0.1:7100";
        String apiKey = System.getenv("DCD_STORAGE_API_KEY") != null
                ? System.getenv("DCD_STORAGE_API_KEY")
                : "dcd_sk_sample_key";

        StorageClient client = StorageClient.builder()
                .endpoint(endpoint)
                .apiKey(apiKey)
                .build();

        System.out.println("--- DCD Java Storage Client Quickstart ---");

        // 1. Generate transformed image URL via fluent builder
        String cdnUrl = client.url("avatars/user-42.jpg")
                .resize(400, 400, "fill")
                .format("webp")
                .quality(85)
                .build();
        System.out.println("1. Transformed CDN URL: " + cdnUrl);

        // 2. Direct public URL without transformations
        String publicUrl = client.getPublicUrl("docs/guide.pdf");
        System.out.println("2. Direct Public URL: " + publicUrl);

        // 3. Transformation via options object
        TransformationOptions options = TransformationOptions.builder()
                .width(200)
                .height(200)
                .crop("thumb")
                .format("png");
        String thumbUrl = client.url("products/item.jpg", options);
        System.out.println("3. Thumbnail URL: " + thumbUrl);

        System.out.println("Quickstart completed successfully.");
    }
}

