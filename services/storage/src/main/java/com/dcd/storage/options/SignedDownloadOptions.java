package com.dcd.storage.options;

public class SignedDownloadOptions {
    private Integer expiresInSeconds = 300;
    private String resourceType = "image";
    private String transform;

    public SignedDownloadOptions() {}

    public static SignedDownloadOptions builder() {
        return new SignedDownloadOptions();
    }

    public SignedDownloadOptions expiresInSeconds(int expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
        return this;
    }

    public SignedDownloadOptions resourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public SignedDownloadOptions transform(String transform) {
        this.transform = transform;
        return this;
    }

    public Integer getExpiresInSeconds() { return expiresInSeconds; }
    public String getResourceType() { return resourceType; }
    public String getTransform() { return transform; }
}

