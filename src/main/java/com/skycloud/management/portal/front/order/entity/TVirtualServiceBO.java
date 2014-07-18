package com.skycloud.management.portal.front.order.entity;

public class TVirtualServiceBO implements java.io.Serializable {

	private String vsName;

	private String vsIp;

	private int vsPort;

	private String vsProtocol;

	private String vsMask;

	private String rgName;

	private int userId;

	private int deviceId;

	public String getVsName() {
		return vsName;
	}

	public void setVsName(String vsName) {
		this.vsName = vsName;
	}

	public String getVsIp() {
		return vsIp;
	}

	public void setVsIp(String vsIp) {
		this.vsIp = vsIp;
	}

	public String getVsMask() {
		return vsMask;
	}

	public void setVsMask(String vsMask) {
		this.vsMask = vsMask;
	}

	public String getRgName() {
		return rgName;
	}

	public void setRgName(String rgName) {
		this.rgName = rgName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getVsPort() {
		return vsPort;
	}

	public void setVsPort(int vsPort) {
		this.vsPort = vsPort;
	}

	public String getVsProtocol() {
		return vsProtocol;
	}

	public void setVsProtocol(String vsProtocol) {
		this.vsProtocol = vsProtocol;
	}

}
