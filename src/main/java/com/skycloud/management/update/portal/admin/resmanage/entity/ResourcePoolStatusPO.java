package com.skycloud.management.update.portal.admin.resmanage.entity;

import java.util.Date;

public class ResourcePoolStatusPO {
    private Integer id;
    private Integer type;
    private Integer total;
    private Float used;
    private Integer surplus;
    private Date modifyDate;
    private String description;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public Float getUsed() {
        return used;
    }
    public void setUsed(Float used) {
        this.used = used;
    }
    public Integer getSurplus() {
        return surplus;
    }
    public void setSurplus(Integer surplus) {
        this.surplus = surplus;
    }
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
