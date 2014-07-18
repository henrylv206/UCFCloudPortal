package com.skycloud.management.portal.front.resources.rest;


public class ListPhysicalOS extends RestfulPath{

	
	public static final String PATH = "/rest/ostype/listOS";

	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String STATE = "state";
	
	private long id;//操作系统类型ID
	private String name;//操作系统类型名称
	private String desc;//操作系统描述
	private String state;//操作系统状态

	public ListPhysicalOS() {
	    super(PATH);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
		this.setParameter(DESC, desc);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.setParameter(STATE, state);
	}
	
}
