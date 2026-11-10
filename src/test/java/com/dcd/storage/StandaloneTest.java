package com.dcd.storage;

import com.dcd.storage.options.TransformationOptions;

public class StandaloneTest {
    public static void main(String[] args) {
        System.out.println("Running Java SDK validation tests (package: com.dcd.storage)...");

        // Test 1: Transformation Builder
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

        String expected = "https://storage.devclouddesk.com/image/upload/w_400,h_300,c_fill,f_webp,q_85,e_blur_5,e_gray,e_sharp/avatars/user-99.webp";
        if (!expected.equals(url)) {
            throw new AssertionError("Test 1 failed! Expected: " + expected + ", Got: " + url);
        }
        System.out.println("✓ Test 1: Full transformation pipeline passed");

        // Test 2: Original URL without transformations
        TransformationBuilder builder2 = new TransformationBuilder(
                "http://localhost:7100",
                "marketing/hero.png"
        );
        String expected2 = "http://localhost:7100/image/upload/marketing/hero.png";
        if (!expected2.equals(builder2.build())) {
            throw new AssertionError("Test 2 failed!");
        }
        System.out.println("✓ Test 2: Original URL format passed");

        // Test 3: Client URL generation with TransformationOptions
        StorageClient client = StorageClient.builder()
                .endpoint("https://cdn.example.com")
                .apiKey("test-key")
                .build();

        TransformationOptions options = TransformationOptions.builder()
                .width(200)
                .height(200)
                .crop("thumb")
                .format("png");

        String url3 = client.url("products/sneaker.jpg", options);
        String expected3 = "https://cdn.example.com/image/upload/w_200,h_200,c_thumb,f_png/products/sneaker.png";
        if (!expected3.equals(url3)) {
            throw new AssertionError("Test 3 failed! Got: " + url3);
        }
        System.out.println("✓ Test 3: Client with TransformationOptions passed");

        // Test 4: Public URL
        String pubUrl = client.getPublicUrl("docs/spec.pdf");
        if (!"https://cdn.example.com/image/upload/docs/spec.pdf".equals(pubUrl)) {
            throw new AssertionError("Test 4 failed! Got: " + pubUrl);
        }
        System.out.println("✓ Test 4: getPublicUrl passed");

        // Test 5: SimpleJson serialization & parsing
        String json = "{\"name\":\"test\",\"count\":42,\"active\":true,\"tags\":[\"a\",\"b\"]}";
        java.util.Map<String, Object> map = SimpleJson.parseObject(json);
        if (!"test".equals(map.get("name")) || !Integer.valueOf(42).equals(map.get("count")) || !Boolean.TRUE.equals(map.get("active"))) {
            throw new AssertionError("Test 5 SimpleJson failed! Got: " + map);
        }
        System.out.println("✓ Test 5: SimpleJson parsing passed");

        System.out.println("ALL JAVA SDK TESTS PASSED SUCCESSFULLY! ✅");
    }
}

