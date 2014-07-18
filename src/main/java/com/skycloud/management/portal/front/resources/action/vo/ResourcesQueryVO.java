/**
 * 2011-11-30 下午02:36:07 $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;

/**
 * @author shixq
 * @version $Revision$ 下午02:36:07
 */
public class ResourcesQueryVO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 4236033798689716893L;

	private String id;// 虚拟机ID

	private String name;

	private int resourcePoolsId;

	private int zoneId;

	private int special;

	private int operateSqlType;// 4是备份服务，5是监控服务

	private String monitorType;// 数据库中监控服务类型

	private String storageID;// 块存储ID

	private TUserBO user;

	private PageVO page;

	public PageVO getPage() {
		return page;
	}

	public void setPage(PageVO page) {
		this.page = page;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOperateSqlType() {
		return operateSqlType;
	}

	public void setOperateSqlType(int operateSqlType) {
		this.operateSqlType = operateSqlType;
	}

	public TUserBO getUser() {
		return user;
	}

	public void setUser(TUserBO user) {
		this.user = user;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public String getStorageID() {
		return storageID;
	}

	public void setStorageID(String storageID) {
		this.storageID = storageID;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

}
