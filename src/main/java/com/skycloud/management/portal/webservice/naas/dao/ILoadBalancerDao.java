package com.skycloud.management.portal.webservice.naas.dao;

/**
 * 负载均衡具体信息
 * 
 * @author liujijun
 * @since Jan 31, 2012
 * @version 1.0
 */
public interface ILoadBalancerDao {

	/**
	 * 根据公网IP编号显示负载均衡规则
	 * 
	 * @param publicIpId
	 * @return
	 */
	public String listLoadBalancerRules(int publicIpId);

	/**
	 * <b>异步操作</b>创建负载均衡规则
	 * 
	 * @param name
	 * @param publicIpId
	 *            :公网IP编号
	 * @param publicPort
	 *            :公网IP端口
	 * @param privatePort
	 *            ：虚机端口
	 * @param description
	 *            :描述
	 * @param algorithm
	 *            :算法
	 * @return
	 */
	public String createLoadBalancerRule(String name, String description, String publicIpId,
			int publicPort, int privatePort, String algorithm);

	/**
	 * <b>异步操作</b>删除负载均衡规则 ，返回jobid
	 * 
	 * @param id
	 * @return
	 */
	public String deleteLoadBalancerRule(int id);

	/**
	 * <b>异步操作</b>分配负载均衡到具体的虚拟机
	 * 
	 * @param id
	 * @param vmIds
	 *            :不同虚机id以英文逗号分割,例如(1,2,3)
	 * @return
	 */
	public String assignToLoadBalancerRule(int id, String vmIds);

	/**
	 * <b>异步操作</b>更新负载均衡规则
	 * 
	 * @return
	 */
	public String updateLoadBalancerRule(int id, String name, String algorithm,
			String description);

	/**
	 * <b>异步操作</b>删除某规则对应的虚机编号
	 * 
	 * @param id
	 * @param vmIds
	 * @return
	 */
	public String removeFromLoadBalancerRule(int id, String vmIds);

	/**
	 * 显示应用规则的虚机实例
	 * 
	 * @param id
	 * @return
	 */
	public String listLoadBalancerRuleInstances(int id);

	
	/**
	 * 返回值类型,xml 或者json
	 * 
	 * @param dataType
	 */
	public void setDataType(String dataType);

	
}
