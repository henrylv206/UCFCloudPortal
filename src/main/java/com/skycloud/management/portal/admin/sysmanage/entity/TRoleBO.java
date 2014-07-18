package com.skycloud.management.portal.admin.sysmanage.entity;

import java.util.Date;
import java.util.List;

public class TRoleBO {
	private int roleId;        //角色ID
	private String roleName;   //角色名称
	private String roleDescr;  //角色描述
	private int creatorUserId; //创建人
	private Date createTime;   //创建时间
	private Date lastupdateDt;//修改时间
	private int roleApproveLevel;   //审批级别，审批时使用。比如：订单由普通用户（级别为1）创建，提交到级别为2的高级用户审批，以此类推
	private List<TMenuBO> menuList;
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescr() {
		return roleDescr;
	}
	public void setRoleDescr(String roleDescr) {
		this.roleDescr = roleDescr;
	}
	public int getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getRoleApproveLevel() {
		return roleApproveLevel;
	}
	public void setRoleApproveLevel(int roleApproveLevel) {
		this.roleApproveLevel = roleApproveLevel;
	}
	
	public List<TMenuBO> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<TMenuBO> menuList) {
		this.menuList = menuList;
	}
	public void setLastupdateDt(Date lastupdateDt) {
		this.lastupdateDt = lastupdateDt;
	}
	public Date getLastupdateDt() {
		return lastupdateDt;
	}
}
