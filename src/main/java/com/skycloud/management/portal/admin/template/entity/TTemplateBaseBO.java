package com.skycloud.management.portal.admin.template.entity;

public class TTemplateBaseBO {

	private int id; //编码
	private int resourcePoolsId; //资源池ID
	private int type; //资源类型
	private String templateDesc; //虚拟机的配置 ，例如“1个CPU，1024M内存，10G磁盘，操作系统为Windows 7 Enterprise”
	private int creatorUserId; //创建人
	private String createTime; //模板创建时间，格式为“YYYY-MM-DD HH:MM:SS”
	
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
	
}
