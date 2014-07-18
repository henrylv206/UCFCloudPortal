package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class ListVirtualMachines extends QueryCommand{

	
	public static final String COMMAND = "listVirtualMachines";

	
	public static final String ID = "id";
	public static final String ZONEID = "zoneid";
	public static final String NETWORKID = "networkid";
	public static final String NAME = "name";
	private long id;
	private int zoneId;
	private long networkId;
	private String name;
	
	public ListVirtualMachines() {
		super(COMMAND);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}
	
	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONEID, zoneId);
	}

	public long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(long networkId) {
		this.networkId = networkId;
		this.setParameter(NETWORKID, networkId);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}
}
