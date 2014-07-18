package com.skycloud.tezz.model;

public class ToptenInfo {
	private String hostName;
	private double fw;
	public ToptenInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ToptenInfo(String hostName, double fw) {
		super();
		this.hostName = hostName;
		this.fw = fw;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public double getFw() {
		return fw;
	}
	public void setFw(double fw) {
		this.fw = fw;
	}
	
	
}
