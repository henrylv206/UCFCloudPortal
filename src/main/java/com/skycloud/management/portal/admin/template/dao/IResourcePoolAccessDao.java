package com.skycloud.management.portal.admin.template.dao;

public interface IResourcePoolAccessDao {
	public void getServiceOfferingFromResourcePool(String resourcePoolId);
	public void getDiskOfferingFromResourcePool(String resourcePoolId );
	public void getOSTemplateFromResourcePool(String resourcePoolId);
}
