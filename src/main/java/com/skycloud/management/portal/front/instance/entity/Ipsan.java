package com.skycloud.management.portal.front.instance.entity;

import java.util.Date;

public class Ipsan {
	//to fix bug [4011]
	private String bsid;
	private Date dt_created;
	private Date dt_updated;
	private String template_id;
	private String host_ip;
	private String vol_path;
	private String lun_path;
	private String protocol;
	private String provider;
	private String vm_id;
	private String initiator;

	public String getBsid() {
		return bsid;
	}

	public void setBsid(String bsid) {
		this.bsid = bsid;
	}

	public Date getDt_created() {
		return dt_created;
	}

	public void setDt_created(Date dtCreated) {
		dt_created = dtCreated;
	}

	public Date getDt_updated() {
		return dt_updated;
	}

	public void setDt_updated(Date dtUpdated) {
		dt_updated = dtUpdated;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String templateId) {
		template_id = templateId;
	}

	public String getHost_ip() {
		return host_ip;
	}

	public void setHost_ip(String hostIp) {
		host_ip = hostIp;
	}

	public String getVol_path() {
		return vol_path;
	}

	public void setVol_path(String volPath) {
		vol_path = volPath;
	}

	public String getLun_path() {
		return lun_path;
	}

	public void setLun_path(String lunPath) {
		lun_path = lunPath;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getVm_id() {
		return vm_id;
	}

	public void setVm_id(String vmId) {
		vm_id = vmId;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

}
