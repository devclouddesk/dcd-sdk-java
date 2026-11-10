package com.dcd.storage.model;

public class ServerStats {
    private int filesCount;
    private long totalStorageBytes;
    private int cacheVariantsCount;
    private long cacheSizeBytes;
    private long uptimeSeconds;
    private double memoryUsageMb;
    private String storageDriver = "local";

    public ServerStats() {}

    public int getFilesCount() { return filesCount; }
    public void setFilesCount(int filesCount) { this.filesCount = filesCount; }

    public long getTotalStorageBytes() { return totalStorageBytes; }
    public void setTotalStorageBytes(long totalStorageBytes) { this.totalStorageBytes = totalStorageBytes; }

    public int getCacheVariantsCount() { return cacheVariantsCount; }
    public void setCacheVariantsCount(int cacheVariantsCount) { this.cacheVariantsCount = cacheVariantsCount; }

    public long getCacheSizeBytes() { return cacheSizeBytes; }
    public void setCacheSizeBytes(long cacheSizeBytes) { this.cacheSizeBytes = cacheSizeBytes; }

    public long getUptimeSeconds() { return uptimeSeconds; }
    public void setUptimeSeconds(long uptimeSeconds) { this.uptimeSeconds = uptimeSeconds; }

    public double getMemoryUsageMb() { return memoryUsageMb; }
    public void setMemoryUsageMb(double memoryUsageMb) { this.memoryUsageMb = memoryUsageMb; }

    public String getStorageDriver() { return storageDriver; }
    public void setStorageDriver(String storageDriver) { this.storageDriver = storageDriver; }

    @Override
    public String toString() {
        return "ServerStats{" +
                "filesCount=" + filesCount +
                ", totalStorageBytes=" + totalStorageBytes +
                ", cacheVariantsCount=" + cacheVariantsCount +
                ", uptimeSeconds=" + uptimeSeconds +
                ", storageDriver='" + storageDriver + '\'' +
                '}';
    }
}

