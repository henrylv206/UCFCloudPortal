package com.skycloud.management.portal.admin.sysmanage.entity;

import java.util.Date;
import java.util.List;


public class TUserBO {
	private int id;
	private String account;
	private String pwd;

	private String dynPwd;
	private String name;
	private int deptId;
	private int roleId;
	private String email="";
	private String phone="";
	private String mobile="";
	private String fax="";
	private String position="";
	private int state=0;
	private String comment;
	private String checkCode;//checkCode
	private int isAutoApprove;
	private int creatorUserId = 1;
	private Date createDt;
	private Date lastupdateDt;
	private String creatorUserName;
	private TDeptBO dept;
	private int compId;
	private String DecPwd;
	private String compCnName;

	private String roleName;
	private String deptName;
	private String roleDescr;
	private int  roleApproveLevel;
	private List<TMenuBO> menuList;
	private List<TMenuBO> unsortedMenuList;
	private List<String> allActionURL; 
	private String role;
	
	//同步信息
	private String startLastUpdate;
	private String endLastUpdate;
	private int resPoolId;
	private String resPool;
	//	public Object get;


	public String getCompCnName() {
		return compCnName;
	}

	public String getDynPwd() {
		return dynPwd;
	}

	public void setDynPwd(String dynPwd) {
		this.dynPwd = dynPwd;
	}
	public void setCompCnName(String compCnName) {
		this.compCnName = compCnName;
	}
	public String getDecPwd() {
		return DecPwd;
	}
	public void setDecPwd(String decPwd) {
		this.DecPwd = decPwd;
	}
	public List<String> getAllActionURL() {
		return allActionURL;
	}
	public void setAllActionURL(List<String> allActionURL) {
		this.allActionURL = allActionURL;
	}
	public List<TMenuBO> getUnsortedMenuList() {
		return unsortedMenuList;
	}
	public void setUnsortedMenuList(List<TMenuBO> unsortedMenuList) {
		this.unsortedMenuList = unsortedMenuList;
	}
	public List<TMenuBO> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<TMenuBO> menuList) {
		this.menuList = menuList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public int getIsAutoApprove() {
		return isAutoApprove;
	}
	public void setIsAutoApprove(int isAutoApprove) {
		this.isAutoApprove = isAutoApprove;
	}
	public int getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	public Date getLastupdateDt() {
		return lastupdateDt;
	}
	public void setLastupdateDt(Date lastupdateDt) {
		this.lastupdateDt = lastupdateDt;
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
	public int getRoleApproveLevel() {
		return roleApproveLevel;
	}
	public void setRoleApproveLevel(int roleApproveLevel) {
		this.roleApproveLevel = roleApproveLevel;
	}
	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}
	public String getCreatorUserName() {
		return creatorUserName;
	}
	public void setDept(TDeptBO dept) {
		this.dept = dept;
	}
	public TDeptBO getDept() {
		return dept;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
	}
	public int getCompId() {
		return compId;
	}
	public void setCompId(int compId) {
		this.compId = compId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getStartLastUpdate() {
		return startLastUpdate;
	}

	public void setStartLastUpdate(String startLastUpdate) {
		this.startLastUpdate = startLastUpdate;
	}

	public String getEndLastUpdate() {
		return endLastUpdate;
	}

	public void setEndLastUpdate(String endLastUpdate) {
		this.endLastUpdate = endLastUpdate;
	}

	public int getResPoolId() {
		return resPoolId;
	}

	public void setResPoolId(int resPoolId) {
		this.resPoolId = resPoolId;
	}

	public String getResPool() {
		return resPool;
	}

	public void setResPool(String resPool) {
		this.resPool = resPool;
	}
	public boolean equals(Object destination)  {
        boolean retVal = false;
        if(destination != null && destination.getClass().equals(this.getClass())) {
            TUserBO bean = (TUserBO)destination;            
            if(bean.getAccount()!=null && bean.getAccount().equals(this.getAccount())) {
                retVal = true;
            }        
        }    
        return retVal;    
	}

}
