package com.skycloud.management.portal.webservice.naas.dao;

import java.util.List;

import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.entity.LoadBalancerTemplate;

/**
 * 负载均衡模板DAO层
 * 
 * @author jijun
 * 
 */
public interface ILoadBalancerTemplateDao {

	/**
	 * 根据负载均衡模板编号返回负载均衡模板详细信息
	 * 
	 * @param id
	 * @return
	 */
	public LoadBalancerTemplate getLoadBalancerTemplateById(int id) throws ObjectNotFoundException;

	/**
	 * 返回所有负载均衡模板数据
	 * 
	 * @return
	 */
	public List<LoadBalancerTemplate> listLoadBalancerTemplate();

}
