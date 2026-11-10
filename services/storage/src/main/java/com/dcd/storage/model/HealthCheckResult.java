package com.dcd.storage.model;

public class HealthCheckResult {
    private String status = "unknown";
    private Long uptime;
    private Object timestamp;
    private String version;

    public HealthCheckResult() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getUptime() { return uptime; }
    public void setUptime(Long uptime) { this.uptime = uptime; }

    public Object getTimestamp() { return timestamp; }
    public void setTimestamp(Object timestamp) { this.timestamp = timestamp; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public boolean isHealthy() {
        return "ok".equalsIgnoreCase(status) || "healthy".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "HealthCheckResult{" +
                "status='" + status + '\'' +
                ", version='" + version + '\'' +
                ", uptime=" + uptime +
                '}';
    }
}

