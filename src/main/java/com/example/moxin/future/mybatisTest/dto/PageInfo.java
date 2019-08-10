package com.example.moxin.future.mybatisTest.dto;

import java.io.Serializable;

public class PageInfo implements Serializable {
    private static final long SerialVersionUID = 1L;

    private int totalNumber;//总页数
    private int currentPage; //当前页的位置
    private int totalPage;//总页数
    private int pageSize=3;//页面大小
    private int startIndex;//检索的起始位置
    private int totalSelect;//检索的总数目

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
        this.count();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalSelect() {
        return totalSelect;
    }

    public void setTotalSelect(int totalSelect) {
        this.totalSelect = totalSelect;
    }

    private void count(){
        int totalPageTemp = this.totalNumber/this.pageSize;
        int plus = (this.totalNumber%this.pageSize)==0?0:1;
        totalPageTemp = totalPageTemp+plus;
        if(totalPageTemp<=0){
            totalPageTemp=1;
        }
        this.totalPage=totalPageTemp;//总页数

        if(this.totalPage<this.currentPage){
            this.currentPage=this.totalPage;
        }

        if(this.currentPage<1){
            this.currentPage=1;
        }

        this.startIndex =(this.currentPage-1)*this.pageSize;
        this.totalSelect = this.pageSize;
    }
}
