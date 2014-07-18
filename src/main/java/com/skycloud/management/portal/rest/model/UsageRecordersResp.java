package com.skycloud.management.portal.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;
import com.skycloud.management.portal.webservice.usage.model.UsageRecorder;

/**
 * 
 * @author WangHaiDong
 * @version 2012-2-1 17:30
 *
 */
@XmlRootElement
public class UsageRecordersResp extends BaseResp {
	
	private List <UsageRecorder> usageRecorders;

	public List<UsageRecorder> getUsageRecorders() {
		return usageRecorders;
	}

	public void setUsageRecorders(List<UsageRecorder> usageRecorders) {
		this.usageRecorders = usageRecorders;
	}
}
