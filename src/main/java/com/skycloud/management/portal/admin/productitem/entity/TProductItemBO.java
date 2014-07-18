package com.skycloud.management.portal.admin.productitem.entity;

public class TProductItemBO {
	private int id;
	private String code; // 类别编号
	private String name; // 类别名称
	private int level; // 类别等级
	private int state; // 状态
	private int parentId; // 父目录ID
	private int nodeType;  //节点类型 0-目录 1-
	private int releaseOrNot;
	private int checkForRelease;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getReleaseOrNot() {
		return releaseOrNot;
	}

	public void setReleaseOrNot(int releaseOrNot) {
		this.releaseOrNot = releaseOrNot;
	}

	public int getCheckForRelease() {
		return checkForRelease;
	}

	public void setCheckForRelease(int checkForRelease) {
		this.checkForRelease = checkForRelease;
	}

}
