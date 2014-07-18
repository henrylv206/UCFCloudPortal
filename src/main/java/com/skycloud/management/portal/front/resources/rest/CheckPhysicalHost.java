package com.skycloud.management.portal.front.resources.rest;


public class CheckPhysicalHost extends RestfulPath{
	
	public static final String PATH = "/rest/phyhost/checkHost";

	public static final String CPUNUMBER = "cpunumber";
	public static final String CPUSPEED  = "cpuspeed";
	public static final String MEMORY = "memory";
	public static final String DISKNUMBER = "disknumber";
	public static final String DISKSIZE = "disksize";
	public static final String ZONEID = "zoneid";
	
	private long cpunumber; //<cpunumber>8</cpunumber>
	private double cpuspeed;  //<cpuspeed>2.4</cpuspeed>
	private long memory;    //<memory>12</memory>
	private long disknumber;//<disknumber>4</disknumber>
	private long disksize;  //<disksize>4</disksize>
	
	private Long zoneId;//资源域
	
	private String reqstate;//<reqstate>true</reqstate>状态：true 请求成功，false 请求失败
	private int total;//<total>1</total>
    
	public CheckPhysicalHost() {
	    super(PATH);
	}



	public long getCpunumber() {
		return cpunumber;
	}

	public void setCpunumber(long cpunumber) {
		this.cpunumber = cpunumber;
		this.setParameter(CPUNUMBER, cpunumber);
	}

	public double getCpuspeed() {
		return cpuspeed;
	}

	public void setCpuspeed(double cpuspeed) {
		this.cpuspeed = cpuspeed;
		this.setParameter(CPUSPEED, cpuspeed);
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
		this.setParameter(MEMORY, memory);
	}

	public long getDisknumber() {
		return disknumber;
	}

	public void setDisknumber(long disknumber) {
		this.disknumber = disknumber;
		this.setParameter(DISKNUMBER, disknumber);
	}

	public long getDisksize() {
		return disksize;
	}

	public void setDisksize(long disksize) {
		this.disksize = disksize;
		this.setParameter(DISKSIZE, disksize);
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONEID, zoneId);
	}

    public String getReqstate() {
    	return reqstate;
    }

    public void setReqstate(String reqstate) {
    	this.reqstate = reqstate;
    }

    public int getTotal() {
    	return total;
    }
	
    public void setTotal(int total) {
    	this.total = total;
    }
	
	
	
}
