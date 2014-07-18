package com.skycloud.management.portal.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VolumeTemplate {

  private int id;

  private int resourcePoolsId;

  private int type;

  private String templateDesc;

  private int creatorUserId;

  private String createTime;

  private String code;

  private int storageSize;

  private int state;

  private int eDiskId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getResourcePoolsId() {
    return resourcePoolsId;
  }

  public void setResourcePoolsId(int resourcePoolsId) {
    this.resourcePoolsId = resourcePoolsId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getTemplateDesc() {
    return templateDesc;
  }

  public void setTemplateDesc(String templateDesc) {
    this.templateDesc = templateDesc;
  }

  public int getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(int creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getStorageSize() {
    return storageSize;
  }

  public void setStorageSize(int storageSize) {
    this.storageSize = storageSize;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getEDiskId() {
    return eDiskId;
  }

  public void setEDiskId(int diskId) {
    eDiskId = diskId;
  }
}
