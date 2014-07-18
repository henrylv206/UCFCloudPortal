package com.skycloud.management.portal.webservice.naas.service;

/**
 * 防火墙访问层，目前仅支持端口转发功能
 * 
 * @author liujijun
 * @since Jan 30, 2012
 * @version 1.0
 */
public interface IFirewallService {

	/**
	 * 根据公网IP编号显示防火墙规则
	 * 
	 * @param publicIpId
	 * @return
	 */
	public String listFirewallRules(int publicIpId);

	/**
	 * <b>异步操作</b>创建防火墙规则
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
	 * @param vmId
	 *            :虚机ID
	 * @return
	 */
	public String createFirewallRule(String name, String description, String publicIpId,
			int publicPort, int privatePort);

	/**
	 * <b>异步操作</b>删除防火墙规则 ，返回jobid
	 * 
	 * @param id
	 * @return
	 */
	public String deleteFirewallRule(int id);

	/**
	 * <b>异步操作</b>更新防火墙规则
	 * 
	 * @return
	 */
	public String updateFirewallRule(int id, String name, String description);

	/**
	 * <b>异步操作</b>分配防火墙到具体的虚拟机
	 * 
	 * @param id
	 * @param vmId
	 *            :端口转向的虚机ID
	 * @return
	 */
	public String assignToFirewallRule(int id, int vmId);

	
	/**
	 * <b>异步操作</b>删除某规则对应的虚机编号
	 * 
	 * @param id
	 * @param vmIds
	 * @return
	 */
	public String removeFromFirewallRule(int id, int vmId);

	
	
	/**
	 * 返回值类型,xml 或者json
	 * 
	 * @param dataType
	 */
	public void setDataType(String dataType);

}
