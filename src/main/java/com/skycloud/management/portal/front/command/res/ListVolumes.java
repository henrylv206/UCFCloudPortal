package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class ListVolumes extends QueryCommand{
	
	public static final String COMMAND = "listVolumes";
	
	public static final String ID = "id";
	
	public static final String VIRTUALMACHINEID = "virtualmachineid";
	
	private String id;
	private String virtualmachineid ;

	public ListVolumes(){
		 super(COMMAND);
	}

	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
		this.setParameter(ID, id);
	}



	public String getVirtualmachineid() {
		return virtualmachineid;
	}



	public void setVirtualmachineid(String virtualmachineid) {
		this.virtualmachineid = virtualmachineid;
		this.setParameter(VIRTUALMACHINEID, virtualmachineid);
	}


}
