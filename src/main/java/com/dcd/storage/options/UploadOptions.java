package com.dcd.storage.options;

import java.util.ArrayList;
import java.util.List;

public class UploadOptions {
    private String folder;
    private String publicId;
    private String filename;
    private Boolean overwrite;
    private Boolean uniqueFilename;
    private String access = "public"; // "public" or "private"
    private String webhookUrl;
    private String token;
    private List<String> tags = new ArrayList<>();
    private Integer timeoutMs;

    public UploadOptions() {}

    public static UploadOptions builder() {
        return new UploadOptions();
    }

    public UploadOptions folder(String folder) {
        this.folder = folder;
        return this;
    }

    public UploadOptions publicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public UploadOptions filename(String filename) {
        this.filename = filename;
        return this;
    }

    public UploadOptions overwrite(boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public UploadOptions uniqueFilename(boolean uniqueFilename) {
        this.uniqueFilename = uniqueFilename;
        return this;
    }

    public UploadOptions access(String access) {
        this.access = access;
        return this;
    }

    public UploadOptions webhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        return this;
    }

    public UploadOptions token(String token) {
        this.token = token;
        return this;
    }

    public UploadOptions tags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public UploadOptions timeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
        return this;
    }

    public String getFolder() { return folder; }
    public String getPublicId() { return publicId; }
    public String getFilename() { return filename; }
    public Boolean getOverwrite() { return overwrite; }
    public Boolean getUniqueFilename() { return uniqueFilename; }
    public String getAccess() { return access; }
    public String getWebhookUrl() { return webhookUrl; }
    public String getToken() { return token; }
    public List<String> getTags() { return tags; }
    public Integer getTimeoutMs() { return timeoutMs; }
}

