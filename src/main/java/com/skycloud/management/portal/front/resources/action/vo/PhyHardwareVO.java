package com.skycloud.management.portal.front.resources.action.vo;


public class PhyHardwareVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4381446693761955588L;
	
	private long value;//物理规格序号
	private String text;//物理机规格
	private Long cpunumber; //<cpunumber>8</cpunumber>
	private Float cpuspeed; //<cpuspeed>2.4</cpuspeed>
	private Long memory;    //<memory>12</memory>
	private Long disknumber;//<disknumber>4</disknumber>
	private Long disksize;  //<disksize>4</disksize>
	
	private Boolean business;  //生产网 <business>true</business>
	private Boolean management;//管理网 <management>false</management>
	private Boolean store;     //存储网 <store>true</store>
    

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getCpunumber() {
		return cpunumber;
	}

	public void setCpunumber(long cpunumber) {
		this.cpunumber = cpunumber;
	}

	public Float getCpuspeed() {
		return cpuspeed;
	}

	public void setCpuspeed(Float cpuspeed) {
		this.cpuspeed = cpuspeed;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public long getDisknumber() {
		return disknumber;
	}

	public void setDisknumber(long disknumber) {
		this.disknumber = disknumber;
	}

	public long getDisksize() {
		return disksize;
	}

	public void setDisksize(long disksize) {
		this.disksize = disksize;
	}

	public Boolean getBusiness() {
		return business;
	}

	public void setBusiness(Boolean business) {
		this.business = business;
	}

	public Boolean getManagement() {
		return management;
	}

	public void setManagement(Boolean management) {
		this.management = management;
	}

	public Boolean getStore() {
		return store;
	}

	public void setStore(Boolean store) {
		this.store = store;
	}
	
}
