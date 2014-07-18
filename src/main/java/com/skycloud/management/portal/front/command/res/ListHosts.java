package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class ListHosts extends QueryCommand {

	public static final String COMMAND = "listHosts";

	public static final String ID = "id";
	public static final String ZONEID = "zoneid";
	public static final String CLUSTERID = "clusterid";
	public static final String ALLOCATIONSTATE = "allocationstate";
	public static final String TYPE = "type";
	public static final String STATE = "state";

	private long id;
	private int zoneid;
	private int clusterid;
	private String allocationstate;
	private String type;
	private String hypervisor;

	private String name;
	private String ipaddress;
	private String state;
	private String zonename;
	private long memorytotal;
	private long memoryallocated;
	private long memoryused;

	public ListHosts() {
		super(COMMAND);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}


    public int getZoneid() {
    	return zoneid;
    }


    public void setZoneid(int zoneid) {
    	this.zoneid = zoneid;
    	this.setParameter(ZONEID, zoneid);
    }


    public int getClusterid() {
    	return clusterid;
    }

    public void setClusterid(int clusterid) {
    	this.clusterid = clusterid;
    	this.setParameter(CLUSTERID, clusterid);
    }

	public String getAllocationstate() {
    	return allocationstate;
    }


    public void setAllocationstate(String allocationstate) {
    	this.allocationstate = allocationstate;
    	this.setParameter(ALLOCATIONSTATE, allocationstate);
    }


    public String getType() {
    	return type;
    }


    public void setType(String type) {
    	this.type = type;
    	this.setParameter(TYPE, type);
    }


    public String getHypervisor() {
    	return hypervisor;
    }


    public void setHypervisor(String hypervisor) {
    	this.hypervisor = hypervisor;
    }


    public String getName() {
    	return name;
    }


    public void setName(String name) {
    	this.name = name;
    }


    public String getIpaddress() {
    	return ipaddress;
    }


    public void setIpaddress(String ipaddress) {
    	this.ipaddress = ipaddress;
    }


    public String getState() {
    	return state;
    }


    public void setState(String state) {
    	this.state = state;
    	this.setParameter(STATE, state);
    }


    public String getZonename() {
    	return zonename;
    }


    public void setZonename(String zonename) {
    	this.zonename = zonename;
    }


    public long getMemorytotal() {
    	return memorytotal;
    }


    public void setMemorytotal(long memorytotal) {
    	this.memorytotal = memorytotal;
    }


    public long getMemoryallocated() {
    	return memoryallocated;
    }


    public void setMemoryallocated(long memoryallocated) {
    	this.memoryallocated = memoryallocated;
    }


    public long getMemoryused() {
    	return memoryused;
    }


    public void setMemoryused(long memoryused) {
    	this.memoryused = memoryused;
    }

}
