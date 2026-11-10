package com.dcd.storage.model;

public class Pagination {
    private int page = 1;
    private int limit = 20;
    private int total = 0;
    private int totalPages = 1;
    private boolean hasNext = false;
    private boolean hasPrev = false;

    public Pagination() {}

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }

    public boolean isHasPrev() { return hasPrev; }
    public void setHasPrev(boolean hasPrev) { this.hasPrev = hasPrev; }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", limit=" + limit +
                ", total=" + total +
                ", totalPages=" + totalPages +
                ", hasNext=" + hasNext +
                ", hasPrev=" + hasPrev +
                '}';
    }
}

