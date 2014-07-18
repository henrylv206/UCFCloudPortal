package com.skycloud.management.portal.front.resources.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * H3C网络规划虚服务对象
 * 
 * @author jiaoyz
 */
@XmlRootElement
public class VirtualService {

	private String vsName; // 虚服务名称

	private String vsIp; // 虚服务ip

	private String vsMask; // 虚服务ip掩码

	private String vsPort;// 端口

	private String vsProtocol;// 协议

	private String rgName; // 对应实服务组名称

	// private int deviceId; // 所在设备id

	private int userId; // 用户id
	
	private int vsId;
	
	private int vpnId;

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

	public String getVsPort() {
		return vsPort;
	}

	public void setVsPort(String vsPort) {
		this.vsPort = vsPort;
	}

	public String getVsProtocol() {
		return vsProtocol;
	}

	public void setVsProtocol(String vsProtocol) {
		this.vsProtocol = vsProtocol;
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

  public int getVsId() {
    return vsId;
  }

  public void setVsId(int vsId) {
    this.vsId = vsId;
  }

  public int getVpnId() {
    return vpnId;
  }

  public void setVpnId(int vpnId) {
    this.vpnId = vpnId;
  }

	// public int getDeviceId() {
	// return deviceId;
	// }
	//
	// public void setDeviceId(int deviceId) {
	// this.deviceId = deviceId;
	// }
}
