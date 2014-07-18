package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class ListSnapshots extends QueryCommand{

	public static final String COMMAND = "listSnapshots";
	
	public static final String VOLUMEID = "volumeid";
	
	public static final String ID = "id";
	
	private String volumeid ;
	
	private int id;

	public ListSnapshots(){
		 super(COMMAND);
	}

	public String getVolumeid() {
		return volumeid;
	}

	public void setVolumeid(String volumeid) {
		this.volumeid = volumeid;
		this.setParameter(VOLUMEID, volumeid);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.setParameter(ID, id);
	}
	
}
