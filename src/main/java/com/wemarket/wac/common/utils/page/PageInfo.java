package com.wemarket.wac.common.utils.page;

import org.springframework.util.Assert;

public class PageInfo {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String MAP_KEY = "page";

    protected int pageSize = DEFAULT_PAGE_SIZE;
    protected int currentPage = 0;
    // protected int prePage;
    // protected int nextPage;
    protected int totalPage = -1;
    protected int totalCount = -1;

    /**
     * 获取pageSize。
     *
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置pageSize。
     *
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        Assert.isTrue(pageSize >= 1, "page size must be larger than 1");
        this.pageSize = pageSize;
    }

    /**
     * 返回currentPage。
     *
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 设置currentPage。
     *
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        Assert.isTrue(pageSize >= 1, "current page must be larger than 1");
        this.currentPage = currentPage;
    }

    /**
     * 返回totalPage。
     *
     * @return the totalPage
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 设置 totalPage。
     *
     * @param totalPage the totalPage to set
     */
    public void setTotalPage(int totalPage) {
        Assert.isTrue(pageSize >= 0, "total page must be larger than or equals to 0");
        this.totalPage = totalPage;
    }

    /**
     * 获取totalCount。
     *
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 设置totalCount。
     *
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        Assert.isTrue(pageSize >= 0, "total count must be larger than or equals to 0");
        this.totalCount = totalCount;
    }

    public PageInfo() {
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * 构造函数。
     *
     * @param currentPage 当前页
     * @param pageSize 页数
     */
    public PageInfo(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        Assert.isTrue(pageSize >= 1, "page size must be larger than 1");
        this.pageSize = pageSize;

    }
}
