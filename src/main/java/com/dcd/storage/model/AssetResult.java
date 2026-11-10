package com.dcd.storage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetResult {
    private String id;
    private String publicId;
    private String folder;
    private Integer version;
    private String filename;
    private String originalFilename;
    private String format;
    private String mimeType;
    private String resourceType;
    private Integer width;
    private Integer height;
    private long bytes;
    private String etag;
    private List<String> tags = new ArrayList<>();
    private String access;
    private String url;
    private String secureUrl;
    private String rawUrl;
    private Object createdAt;
    private Object updatedAt;
    private Map<String, Object> rawData;

    public AssetResult() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public String getFolder() { return folder; }
    public void setFolder(String folder) { this.folder = folder; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public long getBytes() { return bytes; }
    public void setBytes(long bytes) { this.bytes = bytes; }

    public String getEtag() { return etag; }
    public void setEtag(String etag) { this.etag = etag; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getAccess() { return access; }
    public void setAccess(String access) { this.access = access; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getSecureUrl() { return secureUrl; }
    public void setSecureUrl(String secureUrl) { this.secureUrl = secureUrl; }

    public String getRawUrl() { return rawUrl; }
    public void setRawUrl(String rawUrl) { this.rawUrl = rawUrl; }

    public Object getCreatedAt() { return createdAt; }
    public void setCreatedAt(Object createdAt) { this.createdAt = createdAt; }

    public Object getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Object updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, Object> getRawData() { return rawData; }
    public void setRawData(Map<String, Object> rawData) { this.rawData = rawData; }

    @Override
    public String toString() {
        return "AssetResult{" +
                "publicId='" + publicId + '\'' +
                ", url='" + url + '\'' +
                ", bytes=" + bytes +
                ", format='" + format + '\'' +
                '}';
    }
}

