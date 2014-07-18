package com.skycloud.management.update.portal.admin.resmanage.entity;

public class ResourcePoolStatusDisplay {
    private String resourcePool;
    public String getResourcePool() {
        return resourcePool;
    }
    public void setResourcePool(String resourcePool) {
        this.resourcePool = resourcePool;
    }
    private String type;
    private String total;
    private String used;
    private String surplus;
    private String modifyDate;
    private String description;
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public String getUsed() {
        return used;
    }
    public void setUsed(String used) {
        this.used = used;
    }
    public String getSurplus() {
        return surplus;
    }
    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }
    public String getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
