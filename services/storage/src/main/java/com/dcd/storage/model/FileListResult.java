package com.dcd.storage.model;

import java.util.ArrayList;
import java.util.List;

public class FileListResult {
    private boolean success = true;
    private List<AssetResult> files = new ArrayList<>();
    private Pagination pagination;

    public FileListResult() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<AssetResult> getFiles() { return files; }
    public void setFiles(List<AssetResult> files) { this.files = files; }

    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination pagination) { this.pagination = pagination; }

    @Override
    public String toString() {
        return "FileListResult{" +
                "success=" + success +
                ", filesCount=" + (files != null ? files.size() : 0) +
                ", pagination=" + pagination +
                '}';
    }
}

