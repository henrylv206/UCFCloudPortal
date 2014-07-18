package com.skycloud.management.portal.admin.customer.entity;

public class CTBRI_Domain {
	private int id;
	private String name;
	private String level;
	private String hasChild;
	private int parentDomainId;
	private String parentDomainName;

	private int page;
	private int pageSize;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public int getParentDomainId() {
		return parentDomainId;
	}

	public void setParentDomainId(int parentDomainId) {
		this.parentDomainId = parentDomainId;
	}

	public String getParentDomainName() {
		return parentDomainName;
	}

	public void setParentDomainName(String parentDomainName) {
		this.parentDomainName = parentDomainName;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
