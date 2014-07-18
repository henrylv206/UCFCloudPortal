package com.skycloud.management.portal.front.log.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.common.entity.PageVO;

public class TUserLogVO extends TUserLogBase {

	/**
	 * 用户操作日志实体
	 */
	private static final long serialVersionUID = 4181642385997791749L;

	private int id;

	private int userId;

	private int roleId;

	private Date createDt;

	private Date handleDt;//处理日期时间

	private String typeString;

	private String ip;

	private PageVO page;// 分页信息

	private String account;

	private String startDate;

	private String endDate;

	private String createDtString;

	private List<String> ids = new ArrayList<String>();

	public TUserLogVO() {
		super();
	}

	public TUserLogVO(int id, int userId, int roleId, String className, String methodName, String parameters, String moduleName, String functionName,
	                  Date createDt, int type, String comment, String ip, String memo) {
		super(className, methodName, parameters, moduleName, functionName, type, comment, memo);
		this.id = id;
		this.userId = userId;
		this.roleId = roleId;
		this.createDt = createDt;
		this.ip = ip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		if (createDt != null) {
			this.createDtString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(createDt);
		}
		this.createDt = createDt;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public void setCreateDtString(String createDtString) {
		this.createDtString = createDtString;
	}

	public String getCreateDtString() {
		return createDtString;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setPage(PageVO page) {
		this.page = page;
	}

	public PageVO getPage() {
		return page;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public Date getHandleDt() {
		return handleDt;
	}

	public void setHandleDt(Date handleDt) {
		this.handleDt = handleDt;
	}

}
