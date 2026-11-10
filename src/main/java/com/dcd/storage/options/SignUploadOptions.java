package com.dcd.storage.options;

public class SignUploadOptions {
    private String folder;
    private Long maxBytes;
    private Integer expiresInSeconds = 300;
    private String webhookUrl;
    private String access = "public";

    public SignUploadOptions() {}

    public static SignUploadOptions builder() {
        return new SignUploadOptions();
    }

    public SignUploadOptions folder(String folder) {
        this.folder = folder;
        return this;
    }

    public SignUploadOptions maxBytes(long maxBytes) {
        this.maxBytes = maxBytes;
        return this;
    }

    public SignUploadOptions expiresInSeconds(int expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
        return this;
    }

    public SignUploadOptions webhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        return this;
    }

    public SignUploadOptions access(String access) {
        this.access = access;
        return this;
    }

    public String getFolder() { return folder; }
    public Long getMaxBytes() { return maxBytes; }
    public Integer getExpiresInSeconds() { return expiresInSeconds; }
    public String getWebhookUrl() { return webhookUrl; }
    public String getAccess() { return access; }
}

