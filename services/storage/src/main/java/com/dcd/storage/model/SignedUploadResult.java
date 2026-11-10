package com.dcd.storage.model;

import java.util.Map;

public class SignedUploadResult {
    private boolean success = true;
    private String token;
    private String uploadUrl;
    private int expiresInSeconds = 300;
    private String folder;
    private Long maxBytes;
    private String access;
    private String webhookUrl;
    private Map<String, Object> rawData;

    public SignedUploadResult() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUploadUrl() { return uploadUrl; }
    public void setUploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl; }

    public int getExpiresInSeconds() { return expiresInSeconds; }
    public void setExpiresInSeconds(int expiresInSeconds) { this.expiresInSeconds = expiresInSeconds; }

    public String getFolder() { return folder; }
    public void setFolder(String folder) { this.folder = folder; }

    public Long getMaxBytes() { return maxBytes; }
    public void setMaxBytes(Long maxBytes) { this.maxBytes = maxBytes; }

    public String getAccess() { return access; }
    public void setAccess(String access) { this.access = access; }

    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }

    public Map<String, Object> getRawData() { return rawData; }
    public void setRawData(Map<String, Object> rawData) { this.rawData = rawData; }

    @Override
    public String toString() {
        return "SignedUploadResult{" +
                "success=" + success +
                ", token='" + token + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", expiresInSeconds=" + expiresInSeconds +
                '}';
    }
}

