package com.skycloud.management.update.portal.admin.productitem.entity;

public class QueryProductItemsDisplay {
    private int id;
    private String path;
    private String name;
    private String description;
    private String specification;
    private String priceUnitStr;
    private String serviceTypeStr;
    
    public String getPriceUnitStr() {
        return priceUnitStr;
    }
    public void setPriceUnitStr(String priceUnitStr) {
        this.priceUnitStr = priceUnitStr;
    }
    public String getServiceTypeStr() {
        return serviceTypeStr;
    }
    public void setServiceTypeStr(String serviceTypeStr) {
        this.serviceTypeStr = serviceTypeStr;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSpecification() {
        return specification;
    }
    public void setSpecification(String specification) {
        this.specification = specification;
    }
}
