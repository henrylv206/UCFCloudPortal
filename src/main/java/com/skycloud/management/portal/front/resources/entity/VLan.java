package com.skycloud.management.portal.front.resources.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * H3C网络规划VLan对象
 * 
 * @author liujj
 */
@XmlRootElement
public class VLan {

	private int id; // vlan id

	private String type; // vlan类型，例如fw1_sw2、sw2_lb2、gateway等

	private String srcIp; // vlan上端ip

	private String dstIp; // vlan对端ip

	private String srcInterface; // vlan上端虚接口

	private String dstInterface; // vlan对端虚接口

	private int vdId; // vlan所在虚设备id

	private int zoneId; // vlan对应安全域id

	private String zoneName; // vlan对应安全域名称

	private String series; // 标识同一业务应用

	private int preId; // 链路上游vlan，为0表示为顶端

	private int nxtId; // 链路下游vlan，为0表示为末端

	private int srcDeviceId; // 上游设备id

	private int dstDeviceId; // 下游设备id

	private String vdName; //虚设备名称

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public String getDstIp() {
		return dstIp;
	}

	public String getSrcInterface() {
		return srcInterface;
	}

	public String getDstInterface() {
		return dstInterface;
	}

	public int getVdId() {
		return vdId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public String getSeries() {
		return series;
	}

	public int getPreId() {
		return preId;
	}

	public int getNxtId() {
		return nxtId;
	}

	public int getSrcDeviceId() {
		return srcDeviceId;
	}

	public int getDstDeviceId() {
		return dstDeviceId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public void setSrcInterface(String srcInterface) {
		this.srcInterface = srcInterface;
	}

	public void setDstInterface(String dstInterface) {
		this.dstInterface = dstInterface;
	}

	public void setVdId(int vdId) {
		this.vdId = vdId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public void setPreId(int preId) {
		this.preId = preId;
	}

	public void setNxtId(int nxtId) {
		this.nxtId = nxtId;
	}

	public void setSrcDeviceId(int srcDeviceId) {
		this.srcDeviceId = srcDeviceId;
	}

	public void setDstDeviceId(int dstDeviceId) {
		this.dstDeviceId = dstDeviceId;
	}

  public String getVdName() {
    return vdName;
  }

  public void setVdName(String vdName) {
    this.vdName = vdName;
  }

}
