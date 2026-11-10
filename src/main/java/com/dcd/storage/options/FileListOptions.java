package com.dcd.storage.options;

public class FileListOptions {
    private String folder;
    private Integer page = 1;
    private Integer limit = 20;

    public FileListOptions() {}

    public static FileListOptions builder() {
        return new FileListOptions();
    }

    public FileListOptions folder(String folder) {
        this.folder = folder;
        return this;
    }

    public FileListOptions page(int page) {
        this.page = page;
        return this;
    }

    public FileListOptions limit(int limit) {
        this.limit = limit;
        return this;
    }

    public String getFolder() { return folder; }
    public Integer getPage() { return page; }
    public Integer getLimit() { return limit; }
}

