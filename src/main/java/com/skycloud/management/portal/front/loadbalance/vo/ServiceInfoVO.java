package com.skycloud.management.portal.front.loadbalance.vo;

import com.skycloud.array.lb.request.vo.RserviceVo;
import com.skycloud.h3c.loadbalance.po.service.ServiceRowPO;

public class ServiceInfoVO {

	private ServiceRowPO service;

	private RserviceVo rsvo;

	private String vmId;

	public ServiceRowPO getService() {
		return service;
	}

	public void setService(ServiceRowPO service) {
		this.service = service;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public RserviceVo getRsvo() {
		return rsvo;
	}

	public void setRsvo(RserviceVo rsvo) {
		this.rsvo = rsvo;
	}

}
