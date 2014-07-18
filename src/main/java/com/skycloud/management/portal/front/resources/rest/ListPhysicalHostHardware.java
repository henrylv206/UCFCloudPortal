package com.skycloud.management.portal.front.resources.rest;


public class ListPhysicalHostHardware extends RestfulPath{
	
	public static final String PATH = "/rest/phyhost/listHardware";
	
	public static final String VALUE = "value";
	public static final String TEXT  = "text";
	public static final String CPUNUMBER = "cpunumber";
	public static final String CPUSPEED  = "cpuspeed";
	public static final String MEMORY = "memory";
	public static final String DISKNUMBER = "disknumber";
	public static final String DISKSIZE = "disksize";
	public static final String BUSINESS = "business";
	public static final String MANAGEMENT = "management";
	public static final String STORE = "store";
	public static final String ZONEID = "zoneid";
	
	private long value;//物理规格序号
	private String text;//物理机规格
	private long cpunumber; //<cpunumber>8</cpunumber>
	private long cpuspeed;  //<cpuspeed>2.4</cpuspeed>
	private long memory;    //<memory>12</memory>
	private long disknumber;//<disknumber>4</disknumber>
	private long disksize;  //<disksize>4</disksize>
	
	private String business;  //生产网 <business>true</business>
	private String management;//管理网 <management>false</management>
	private String store;     //存储网 <store>true</store>
	private Long zoneId;//资源域
    
    
	public ListPhysicalHostHardware() {
	    super(PATH);
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
		this.setParameter(VALUE, value);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.setParameter(TEXT, text);
	}

	public long getCpunumber() {
		return cpunumber;
	}

	public void setCpunumber(long cpunumber) {
		this.cpunumber = cpunumber;
		this.setParameter(CPUNUMBER, cpunumber);
	}

	public long getCpuspeed() {
		return cpuspeed;
	}

	public void setCpuspeed(long cpuspeed) {
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

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
		this.setParameter(BUSINESS, business);
	}

	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
		this.setParameter(MANAGEMENT, management);
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
		this.setParameter(STORE, store);
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONEID, zoneId);
	}
	
}
