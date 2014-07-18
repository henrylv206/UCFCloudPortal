package com.skycloud.management.portal.webservice.naas.service;

import java.util.List;

import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.entity.FirewallTemplate;

/**
 * 
 * @author jijun
 * 
 */
public interface IFirewallTemplateService {
	/**
	 * 根据带宽模板编号返回带宽模板详细信息
	 * 
	 * @param id
	 * @return
	 */
	public FirewallTemplate getFirewallTemplateById(int id) throws ObjectNotFoundException;

	/**
	 * @param curPage
	 *            :当前页，从1开始
	 * @param pageSize
	 *            ：每页大小
	 * @return 列表数据
	 */
	public List<FirewallTemplate> listFirewallTemplate();

}
