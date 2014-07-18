package com.skycloud.management.portal.front.resources.rest;


public class PhysicalStopHost extends RestfulPath{

	public static final String PATH_STOP_HOST  = "/rest/phyhost/stopHost";//关机
	
	public static final String ID = "id";
	public static final String RESULT = "result";
	
	private long id;//物理主机ID
	private String result;//执行结果
	
	public PhysicalStopHost() {
		super(PATH_STOP_HOST);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
		this.setParameter(RESULT, id);
	}
	
}
