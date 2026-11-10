package com.dcd.storage;

import com.dcd.storage.exception.*;
import com.dcd.storage.model.*;
import com.dcd.storage.options.*;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

/**
 * Official Java Client SDK for DevCloudDesk Storage Engine.
 * Built for Java 11+ with zero required external dependencies.
 */
public class StorageClient {
    private final String endpoint;
    private final String apiKey;
    private final Duration timeout;
    private final String deliveryPrefix;
    private final HttpClient httpClient;

    public final UploaderNamespace uploader;
    public final ApiNamespace api;

    public static class Builder {
        private String endpoint;
        private String apiKey;
        private Duration timeout = Duration.ofSeconds(30);
        private String deliveryPrefix = "image/upload";

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder timeoutSeconds(int seconds) {
            this.timeout = Duration.ofSeconds(seconds);
            return this;
        }

        public Builder deliveryPrefix(String deliveryPrefix) {
            this.deliveryPrefix = deliveryPrefix;
            return this;
        }

        public StorageClient build() {
            return new StorageClient(endpoint, apiKey, timeout, deliveryPrefix);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public StorageClient(String endpoint, String apiKey) {
        this(endpoint, apiKey, Duration.ofSeconds(30), "image/upload");
    }

    public StorageClient(String endpoint, String apiKey, Duration timeout, String deliveryPrefix) {
        String envEndpoint = System.getenv("DCD_STORAGE_ENDPOINT");
        String envApiKey = System.getenv("DCD_STORAGE_API_KEY");
        if (envApiKey == null || envApiKey.isEmpty()) {
            envApiKey = System.getenv("DCD_API_KEY");
        }

        String resolvedEndpoint = endpoint != null && !endpoint.isEmpty() ? endpoint : envEndpoint;
        if (resolvedEndpoint == null || resolvedEndpoint.isEmpty()) {
            throw new IllegalArgumentException(
                    "StorageClient: endpoint is required (pass in constructor or set DCD_STORAGE_ENDPOINT environment variable)"
            );
        }

        this.endpoint = resolvedEndpoint.replaceAll("/+$", "");
        this.apiKey = apiKey != null ? apiKey.trim() : (envApiKey != null ? envApiKey.trim() : "");
        this.timeout = timeout != null ? timeout : Duration.ofSeconds(30);
        this.deliveryPrefix = deliveryPrefix != null ? deliveryPrefix.replaceAll("^/+|/+$", "") : "image/upload";

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(this.timeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        this.uploader = new UploaderNamespace();
        this.api = new ApiNamespace();
    }

    public class UploaderNamespace {
        public AssetResult upload(File file, UploadOptions options) {
            return StorageClient.this.upload(file, options);
        }

        public AssetResult upload(Path path, UploadOptions options) {
            return StorageClient.this.upload(path, options);
        }

        public boolean destroy(String publicId) {
            return StorageClient.this.delete(publicId);
        }

        public boolean delete(String publicId) {
            return StorageClient.this.delete(publicId);
        }
    }

    public class ApiNamespace {
        public AssetResult resource(String publicId) {
            return StorageClient.this.getResource(publicId);
        }

        public FileListResult resources(FileListOptions options) {
            return StorageClient.this.listFiles(options);
        }

        public AssetResult update(String publicId, String access) {
            return StorageClient.this.updateAccess(publicId, access);
        }

        public ServerStats usage() {
            return StorageClient.this.getStats();
        }
    }

    /**
     * Directly returns the public CDN URL for an asset without transformation boilerplate.
     */
    public String getPublicUrl(String publicId) {
        String clean = publicId != null ? publicId.replaceAll("^/+", "") : "";
        return endpoint + "/" + deliveryPrefix + "/" + clean;
    }

    /**
     * Generates a time-limited cryptographically signed URL to access private assets
     */
    public String getSignedUrl(String publicId, SignedDownloadOptions options) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new AuthenticationException("API key is required to generate signed download URLs.");
        }

        String clean = publicId != null ? publicId.replaceAll("^/+", "") : "";
        String signUrl = endpoint + "/v1/download/sign";

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("publicId", clean);
        payload.put("expiresInSeconds", options != null && options.getExpiresInSeconds() != null ? options.getExpiresInSeconds() : 300);
        payload.put("resourceType", options != null && options.getResourceType() != null ? options.getResourceType() : "image");
        if (options != null && options.getTransform() != null) {
            payload.put("transform", options.getTransform());
        }

        Map<String, Object> response = executeJsonRequest("POST", signUrl, payload, true);
        Object url = response.get("signedUrl");
        if (url == null) url = response.get("signed_url");
        return url != null ? String.valueOf(url) : "";
    }

    /**
     * Returns a fluent transformation builder for this asset.
     */
    public TransformationBuilder url(String publicId) {
        return new TransformationBuilder(endpoint, publicId, new TransformationOptions(), deliveryPrefix);
    }

    /**
     * Generates a transformed URL string immediately from the given options.
     */
    public String url(String publicId, TransformationOptions options) {
        return new TransformationBuilder(endpoint, publicId, options, deliveryPrefix).build();
    }

    /**
     * Directly uploads a local File.
     */
    public AssetResult upload(File file, UploadOptions options) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file);
        }
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String filename = options != null && options.getFilename() != null ? options.getFilename() : file.getName();
            return upload(bytes, filename, options);
        } catch (IOException e) {
            throw new UploadException("Failed to read file for upload: " + e.getMessage(), 0, null);
        }
    }

    public AssetResult upload(Path path, UploadOptions options) {
        if (path == null || !Files.exists(path)) {
            throw new IllegalArgumentException("Path does not exist: " + path);
        }
        try {
            byte[] bytes = Files.readAllBytes(path);
            String filename = options != null && options.getFilename() != null ? options.getFilename() : path.getFileName().toString();
            return upload(bytes, filename, options);
        } catch (IOException e) {
            throw new UploadException("Failed to read path for upload: " + e.getMessage(), 0, null);
        }
    }

    public AssetResult upload(byte[] fileBytes, String filename, UploadOptions options) {
        String token = options != null ? options.getToken() : null;
        if ((apiKey == null || apiKey.isEmpty()) && (token == null || token.isEmpty())) {
            throw new AuthenticationException("Either apiKey or a signed upload token must be provided to upload.");
        }

        String uploadUrl = endpoint + "/v1/upload";
        String boundary = "----DCDStorageClientBoundary" + System.currentTimeMillis();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (options != null) {
                if (options.getFolder() != null) writeFormField(baos, boundary, "folder", options.getFolder());
                if (options.getPublicId() != null) writeFormField(baos, boundary, "publicId", options.getPublicId());
                if (options.getOverwrite() != null) writeFormField(baos, boundary, "overwrite", String.valueOf(options.getOverwrite()));
                if (options.getUniqueFilename() != null) writeFormField(baos, boundary, "uniqueFilename", String.valueOf(options.getUniqueFilename()));
                if (options.getAccess() != null) writeFormField(baos, boundary, "access", options.getAccess());
                if (options.getWebhookUrl() != null) writeFormField(baos, boundary, "webhookUrl", options.getWebhookUrl());
            }

            writeBinaryField(baos, boundary, "file", filename != null ? filename : "upload.bin", fileBytes);
            baos.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
            byte[] multipartBody = baos.toByteArray();

            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(uploadUrl))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .timeout(timeout)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody));

            if (token != null && !token.isEmpty()) {
                reqBuilder.header("X-Upload-Token", token);
            } else {
                reqBuilder.header("Authorization", "Bearer " + apiKey);
            }

            HttpResponse<String> resp = sendRequest(reqBuilder.build());
            Map<String, Object> respMap = SimpleJson.parseObject(resp.body());

            Object assetObj = respMap.get("asset");
            if (assetObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> assetMap = (Map<String, Object>) assetObj;
                return mapToAssetResult(assetMap);
            }
            return mapToAssetResult(respMap);
        } catch (IOException e) {
            throw new UploadException("Error preparing multipart upload: " + e.getMessage(), 0, null);
        }
    }

    public AssetResult uploadWithToken(File file, String token, UploadOptions options) {
        UploadOptions merged = options != null ? options : new UploadOptions();
        merged.token(token);
        return upload(file, merged);
    }

    public AssetResult uploadWithToken(byte[] bytes, String filename, String token, UploadOptions options) {
        UploadOptions merged = options != null ? options : new UploadOptions();
        merged.token(token);
        return upload(bytes, filename, merged);
    }

    public boolean delete(String publicId) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new AuthenticationException("API key is required to delete assets.");
        }

        String clean = publicId != null ? publicId.replaceAll("^/+", "") : "";
        String deleteUrl = endpoint + "/v1/admin/files/" + clean;

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(deleteUrl))
                .header("Authorization", "Bearer " + apiKey)
                .timeout(timeout)
                .DELETE()
                .build();

        sendRequest(req);
        return true;
    }

    public boolean destroy(String publicId) {
        return delete(publicId);
    }

    @SuppressWarnings("unchecked")
    public AssetResult getResource(String publicId) {
        String clean = publicId != null ? publicId.replaceAll("^/+", "") : "";
        String url = endpoint + "/v1/admin/files/" + clean;

        Map<String, Object> resp = executeJsonRequest("GET", url, null, true);
        Object assetData = resp.get("asset");
        if (assetData instanceof Map) {
            return mapToAssetResult((Map<String, Object>) assetData);
        }
        return mapToAssetResult(resp);
    }

    public AssetResult getFile(String publicId) {
        return getResource(publicId);
    }

    @SuppressWarnings("unchecked")
    public FileListResult listFiles(FileListOptions options) {
        StringBuilder urlBuilder = new StringBuilder(endpoint).append("/v1/admin/files?");
        if (options != null) {
            if (options.getFolder() != null) urlBuilder.append("folder=").append(URLEncoder.encode(options.getFolder(), StandardCharsets.UTF_8)).append("&");
            if (options.getPage() != null) urlBuilder.append("page=").append(options.getPage()).append("&");
            if (options.getLimit() != null) urlBuilder.append("limit=").append(options.getLimit()).append("&");
        }

        Map<String, Object> resp = executeJsonRequest("GET", urlBuilder.toString(), null, true);
        FileListResult result = new FileListResult();
        result.setSuccess(Boolean.TRUE.equals(resp.get("success")));

        Object dataObj = resp.get("data");
        Map<String, Object> dataMap = dataObj instanceof Map ? (Map<String, Object>) dataObj : resp;

        Object filesObj = dataMap.get("files");
        if (filesObj instanceof List) {
            List<AssetResult> list = new ArrayList<>();
            for (Object f : (List<Object>) filesObj) {
                if (f instanceof Map) {
                    list.add(mapToAssetResult((Map<String, Object>) f));
                }
            }
            result.setFiles(list);
        }

        Object pagObj = dataMap.get("pagination");
        if (pagObj instanceof Map) {
            Map<String, Object> pagMap = (Map<String, Object>) pagObj;
            Pagination p = new Pagination();
            if (pagMap.get("page") instanceof Number) p.setPage(((Number) pagMap.get("page")).intValue());
            if (pagMap.get("limit") instanceof Number) p.setLimit(((Number) pagMap.get("limit")).intValue());
            if (pagMap.get("total") instanceof Number) p.setTotal(((Number) pagMap.get("total")).intValue());
            if (pagMap.get("totalPages") instanceof Number) p.setTotalPages(((Number) pagMap.get("totalPages")).intValue());
            if (pagMap.get("hasNext") instanceof Boolean) p.setHasNext((Boolean) pagMap.get("hasNext"));
            if (pagMap.get("hasPrev") instanceof Boolean) p.setHasPrev((Boolean) pagMap.get("hasPrev"));
            result.setPagination(p);
        }

        return result;
    }

    public FileListResult list(FileListOptions options) {
        return listFiles(options);
    }

    public SignedUploadResult createSignedUploadToken(SignUploadOptions options) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new AuthenticationException("API key is required on backend to generate signed upload tokens.");
        }

        String signUrl = endpoint + "/v1/upload/sign";
        Map<String, Object> payload = new LinkedHashMap<>();
        if (options != null) {
            if (options.getFolder() != null) payload.put("folder", options.getFolder());
            if (options.getMaxBytes() != null) payload.put("maxBytes", options.getMaxBytes());
            if (options.getExpiresInSeconds() != null) payload.put("expiresInSeconds", options.getExpiresInSeconds());
            if (options.getWebhookUrl() != null) payload.put("webhookUrl", options.getWebhookUrl());
            if (options.getAccess() != null) payload.put("access", options.getAccess());
        }

        Map<String, Object> resp = executeJsonRequest("POST", signUrl, payload, true);
        SignedUploadResult res = new SignedUploadResult();
        res.setSuccess(Boolean.TRUE.equals(resp.get("success")));
        res.setToken((String) resp.get("token"));
        res.setUploadUrl((String) resp.get("uploadUrl"));
        if (resp.get("expiresInSeconds") instanceof Number) {
            res.setExpiresInSeconds(((Number) resp.get("expiresInSeconds")).intValue());
        }
        res.setFolder((String) resp.get("folder"));
        if (resp.get("maxBytes") instanceof Number) {
            res.setMaxBytes(((Number) resp.get("maxBytes")).longValue());
        }
        res.setAccess((String) resp.get("access"));
        res.setWebhookUrl((String) resp.get("webhookUrl"));
        res.setRawData(resp);
        return res;
    }

    public SignedUploadResult createUploadToken(SignUploadOptions options) {
        return createSignedUploadToken(options);
    }

    @SuppressWarnings("unchecked")
    public AssetResult updateAccess(String publicId, String access) {
        String clean = publicId != null ? publicId.replaceAll("^/+", "") : "";
        String url = endpoint + "/v1/admin/files/" + clean + "/access";

        Map<String, Object> payload = Collections.singletonMap("access", access != null ? access : "public");
        Map<String, Object> resp = executeJsonRequest("PATCH", url, payload, true);
        Object assetData = resp.get("asset");
        if (assetData instanceof Map) {
            return mapToAssetResult((Map<String, Object>) assetData);
        }
        return mapToAssetResult(resp);
    }

    public boolean purgeCache(String publicId) {
        String clean = publicId != null ? publicId.replaceAll("^/+", "") : "";
        String url = endpoint + "/v1/admin/cache/" + clean;
        executeJsonRequest("DELETE", url, null, true);
        return true;
    }

    public ServerStats getStats() {
        String url = endpoint + "/v1/admin/stats";
        Map<String, Object> resp = executeJsonRequest("GET", url, null, true);

        ServerStats stats = new ServerStats();
        if (resp.get("filesCount") instanceof Number) stats.setFilesCount(((Number) resp.get("filesCount")).intValue());
        if (resp.get("totalStorageBytes") instanceof Number) stats.setTotalStorageBytes(((Number) resp.get("totalStorageBytes")).longValue());
        if (resp.get("cacheVariantsCount") instanceof Number) stats.setCacheVariantsCount(((Number) resp.get("cacheVariantsCount")).intValue());
        if (resp.get("cacheSizeBytes") instanceof Number) stats.setCacheSizeBytes(((Number) resp.get("cacheSizeBytes")).longValue());
        if (resp.get("uptimeSeconds") instanceof Number) stats.setUptimeSeconds(((Number) resp.get("uptimeSeconds")).longValue());
        if (resp.get("memoryUsageMb") instanceof Number) stats.setMemoryUsageMb(((Number) resp.get("memoryUsageMb")).doubleValue());
        if (resp.get("storageDriver") != null) stats.setStorageDriver(String.valueOf(resp.get("storageDriver")));
        return stats;
    }

    public HealthCheckResult healthCheck() {
        String url = endpoint + "/health";
        Map<String, Object> resp = executeJsonRequest("GET", url, null, false);

        HealthCheckResult result = new HealthCheckResult();
        if (resp.get("status") != null) result.setStatus(String.valueOf(resp.get("status")));
        if (resp.get("uptime") instanceof Number) result.setUptime(((Number) resp.get("uptime")).longValue());
        result.setTimestamp(resp.get("timestamp"));
        if (resp.get("version") != null) result.setVersion(String.valueOf(resp.get("version")));
        return result;
    }

    @SuppressWarnings("unchecked")
    private AssetResult mapToAssetResult(Map<String, Object> map) {
        AssetResult res = new AssetResult();
        res.setId((String) (map.get("id") != null ? map.get("id") : map.get("asset_id")));
        res.setPublicId((String) (map.get("publicId") != null ? map.get("publicId") : map.get("public_id")));
        res.setFolder((String) map.get("folder"));
        if (map.get("version") instanceof Number) res.setVersion(((Number) map.get("version")).intValue());
        res.setFilename((String) map.get("filename"));
        res.setOriginalFilename((String) (map.get("originalFilename") != null ? map.get("originalFilename") : map.get("original_filename")));
        res.setFormat((String) map.get("format"));
        res.setMimeType((String) (map.get("mimeType") != null ? map.get("mimeType") : map.get("mime_type")));
        res.setResourceType((String) (map.get("resourceType") != null ? map.get("resourceType") : map.get("resource_type")));
        if (map.get("width") instanceof Number) res.setWidth(((Number) map.get("width")).intValue());
        if (map.get("height") instanceof Number) res.setHeight(((Number) map.get("height")).intValue());
        if (map.get("bytes") instanceof Number) res.setBytes(((Number) map.get("bytes")).longValue());
        res.setEtag((String) map.get("etag"));
        if (map.get("tags") instanceof List) res.setTags((List<String>) map.get("tags"));
        res.setAccess((String) (map.get("access") != null ? map.get("access") : "public"));
        res.setUrl((String) map.get("url"));
        res.setSecureUrl((String) (map.get("secureUrl") != null ? map.get("secureUrl") : (map.get("secure_url") != null ? map.get("secure_url") : map.get("url"))));
        res.setRawUrl((String) (map.get("rawUrl") != null ? map.get("rawUrl") : map.get("raw_url")));
        res.setCreatedAt(map.get("createdAt") != null ? map.get("createdAt") : map.get("created_at"));
        res.setUpdatedAt(map.get("updatedAt") != null ? map.get("updatedAt") : map.get("updated_at"));
        res.setRawData(map);
        return res;
    }

    private Map<String, Object> executeJsonRequest(String method, String url, Object body, boolean requireAuth) {
        HttpRequest.Builder b = HttpRequest.newBuilder().uri(URI.create(url)).timeout(timeout);

        if (requireAuth) {
            if (apiKey == null || apiKey.isEmpty()) {
                throw new AuthenticationException("API key required for " + method + " " + url);
            }
            b.header("Authorization", "Bearer " + apiKey);
        }

        if (body != null) {
            b.header("Content-Type", "application/json");
            String json = SimpleJson.serialize(body);
            b.method(method, HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));
        } else {
            b.method(method, HttpRequest.BodyPublishers.noBody());
        }

        HttpResponse<String> resp = sendRequest(b.build());
        return SimpleJson.parseObject(resp.body());
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                return response;
            }

            String body = response.body();
            String errorMsg = "HTTP error " + status;
            try {
                Map<String, Object> json = SimpleJson.parseObject(body);
                if (json.get("error") != null) errorMsg = String.valueOf(json.get("error"));
                else if (json.get("message") != null) errorMsg = String.valueOf(json.get("message"));
            } catch (Exception ignored) {}

            switch (status) {
                case 401: throw new AuthenticationException(errorMsg, 401, body);
                case 404: throw new NotFoundException(errorMsg, 404, body);
                case 413: throw new PayloadTooLargeException(errorMsg, 413, body);
                case 408: case 504: throw new NetworkTimeoutException(errorMsg, status, body);
                default: throw new StorageException(errorMsg, status, "STORAGE_ERROR", body);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StorageException("Storage request interrupted: " + e.getMessage(), 0, "INTERRUPTED", null, e);
        } catch (IOException e) {
            throw new StorageException("I/O error communicating with Storage cluster: " + e.getMessage(), 0, "IO_ERROR", null, e);
        }
    }

    private static void writeFormField(ByteArrayOutputStream baos, String boundary, String name, String value) throws IOException {
        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(value.getBytes(StandardCharsets.UTF_8));
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    private static void writeBinaryField(ByteArrayOutputStream baos, String boundary, String name, String filename, byte[] data) throws IOException {
        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write("Content-Type: application/octet-stream\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        baos.write(data);
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
}

