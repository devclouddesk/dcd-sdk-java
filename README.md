# ☕ DCD Storage Client for Java

[![Maven Central](https://img.shields.io/maven-central/v/com.devclouddesk/dcd-storage-client.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.devclouddesk%20a:dcd-storage-client)
[![Software License](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat-square)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-%3E%3D11-blue.svg?style=flat-square)](https://openjdk.org)

Official Java client library for [DevCloudDesk (DCD) Storage Buckets](https://devclouddesk.com). Supports ultra-fast media uploads, dynamic on-the-fly image transformations, cryptographically signed URLs, client-side upload tokens, and asset management.

Built for **Java 11+** with **zero required external dependencies** using the standard Java `java.net.http.HttpClient` library. Compatible with Spring Boot, Quarkus, Micronaut, Android (API 26+), and vanilla Java applications.

---

## 📦 Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.devclouddesk</groupId>
    <artifactId>dcd-storage-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.devclouddesk:dcd-storage-client:1.0.0'
```

---

## 🚀 Quickstart

### 1. Initialize Client

```java
import com.dcd.storage.StorageClient;

// Initialize via Builder or constructor
// Auto-detects from DCD_STORAGE_ENDPOINT and DCD_STORAGE_API_KEY environment variables if omitted
StorageClient client = StorageClient.builder()
    .endpoint("https://storage.yourdomain.com") // or "http://192.168.1.50:7100"
    .apiKey("dcd_sk_live_...")
    .build();
```

---

### 2. Uploading Files

```java
import com.dcd.storage.model.AssetResult;
import com.dcd.storage.options.UploadOptions;
import java.io.File;

// Upload from local File
File file = new File("/path/to/banner.png");
AssetResult asset = client.upload(file, UploadOptions.builder()
    .folder("marketing")
    .overwrite(true));

System.out.println("Public ID: " + asset.getPublicId());
System.out.println("CDN URL:   " + asset.getUrl());

// Upload raw bytes
byte[] data = "...".getBytes();
AssetResult rawAsset = client.upload(data, "avatar.png", UploadOptions.builder()
    .folder("avatars")
    .access("public"));
```

---

### 3. Dynamic On-the-Fly Image CDN URLs

Generate CDN transformation URLs without server re-encoding:

```java
// Fluent Builder Style
String avatarUrl = client.url("avatars/user-42.jpg")
    .resize(300, 300, "fill")
    .format("webp")
    .quality(85)
    .blur(2)
    .build();

// Direct Options Style
String thumbUrl = client.url("products/shoe.jpg", TransformationOptions.builder()
    .width(400)
    .height(400)
    .crop("thumb")
    .format("avif")
    .quality(80));

// Fast direct public URL without transformations
String publicUrl = client.getPublicUrl("docs/whitepaper.pdf");
```

---

### 4. Signed Download URLs for Private Files

```java
import com.dcd.storage.options.SignedDownloadOptions;

// 5-minute temporary link for KYC documents or private invoices
String signedUrl = client.getSignedUrl("invoices/inv_2026.pdf", SignedDownloadOptions.builder()
    .expiresInSeconds(300)
    .resourceType("raw"));
```

---

### 5. Client-Side Upload Tokens (Browser / Mobile)

Safely allow frontend browser uploads directly to the VPS storage container without exposing secret API keys:

```java
import com.dcd.storage.model.SignedUploadResult;
import com.dcd.storage.options.SignUploadOptions;

// In your Spring Boot controller:
SignedUploadResult tokenInfo = client.createSignedUploadToken(SignUploadOptions.builder()
    .folder("user-uploads")
    .maxBytes(10 * 1024 * 1024) // 10MB
    .expiresInSeconds(600));

// Return tokenInfo.getToken() and tokenInfo.getUploadUrl() to client
```

---

### 6. Asset Management & Cloudinary-Compatible Namespaces

```java
import com.dcd.storage.model.FileListResult;
import com.dcd.storage.options.FileListOptions;

// Get asset metadata
AssetResult asset = client.getResource("avatars/user-42.jpg");

// List files with pagination
FileListResult result = client.listFiles(FileListOptions.builder()
    .folder("avatars")
    .limit(25)
    .page(1));

for (AssetResult f : result.getFiles()) {
    System.out.printf("%s (%d bytes)%n", f.getPublicId(), f.getBytes());
}

// Delete asset
client.delete("avatars/old-avatar.jpg");

// Cloudinary-compatible syntax:
client.uploader.upload(new File("image.png"), UploadOptions.builder().folder("test"));
client.uploader.destroy("test/image");
client.api.usage();
```

---

## 📄 License

MIT License. See [LICENSE](LICENSE) for details.

