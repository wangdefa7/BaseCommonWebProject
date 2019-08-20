package com.web.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 前端参数通用基础类
 * @author [wdf]
 * @version [版本号, 2019-08-19]
 * @since [XX查询/后台]
 */
public class CommonEntity {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int pageSize;//页面大小
    private int pageNum;//页面位置（第几页）
    private String startDate;//开始时间
    private String endDate;//结束时间
    private String value;//参数
    private Object object;

    public int getPageSize() {
        return pageSize == 0 ? 10 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageSize(Object pageSize) {
        this.pageSize = pageSize == null ? 10 : Integer.valueOf(String.valueOf(pageSize));
    }

    public int getPageNum() {
        return pageNum = (pageNum - 1) * pageSize;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageNum(Object pageNum) {
        this.pageNum = pageNum == null ? 10 : Integer.valueOf(String.valueOf(pageNum));
    }

    public String getStartDate() {
        return startDate == null ? dateFormat.format(new Date(Long.MIN_VALUE)) : startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? "" :startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate == null ? "" : String.valueOf(startDate);
    }

    public String getEndDate() {
        return endDate == null ? dateFormat.format(new Date()) :endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? "" : endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate == null ? "" : String.valueOf(endDate);
    }

    public String getValue() {
        return value = value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value =  value == null ? "" : value;
    }

    public void setValue(Object value) {
        this.value =  value == null ? "" : String.valueOf(value);
    }

    public Object getObject() {
        return object == null ? "" : object;
    }

    public void setObject(Object object) {
        this.object = object == null ? "" : object;
    }
}
