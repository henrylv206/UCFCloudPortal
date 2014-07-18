package com.skycloud.management.update.portal.admin.resmanage.entity;

import java.util.HashMap;

public enum ResourceTypeEnum {
    PhysicalHost(0, "物理机", "个"), 
    Storage(1, "块存储", "MB"), 
    ObjectStorage(2, "对象存储", "MB"), 
    PublicNetworkIP(3, "公网IP", "个"), 
    SecurityGroup(4, "安全组", "个");
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int id;
    String unit;
    String name;
    ResourceTypeEnum(int id, String name, String unit) {
        this.id = id;
        this.unit = unit;
        this.name = name;
    }
    
    private static HashMap<Integer, ResourceTypeEnum> es;
    public static ResourceTypeEnum fromId(int id) {
        if (es == null) {
            es = new HashMap<Integer, ResourceTypeEnum>();
            es.put(PhysicalHost.id, PhysicalHost);
            es.put(Storage.id, Storage);
            es.put(ObjectStorage.id, ObjectStorage);
            es.put(PublicNetworkIP.id, PublicNetworkIP);
            es.put(SecurityGroup.id, SecurityGroup);
        }
        if (es.containsKey(id)) {
            return es.get(id);
        }
        return null;
    }
}
