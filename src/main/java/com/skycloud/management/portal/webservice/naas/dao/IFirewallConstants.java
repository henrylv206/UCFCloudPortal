package com.skycloud.management.portal.webservice.naas.dao;

/**
 * 负载均衡规则一些命令的uri
 * 
 * @author liujijun
 * @since Feb 1, 2012
 * @version 1.0
 */
public interface IFirewallConstants {

	/** 所有负载均衡规则uri */
	public static final String LIST_FWR = "?command=listLoadBalancerRules&publicipid={publicipid}&response={response}";

	/** 删除负载均衡规则uri */
	public static final String DELETE_FWR = "?command=deleteLoadBalancerRule&id={id}&response={response}";

	/** 创建负载均衡规则uri */
	public static final String CREATE_FWR = "?command=createLoadBalancerRule&name={name}&description={description}&publicipid={publicipid}&publicport={publicport}&privateport={privateport}&algorithm={algorithm}&response={response}";

	/** 将虚拟机添加到负载均衡规则中uri */
	public static final String ASSIGN_TO_FWR = "?command=assignToLoadBalancerRule&id={id}&virtualmachineids={virtualmachineids}&response={response}";

	/** 更新负载均衡规则uri */
	public static final String UPDATE_FWR = "?command=updateLoadBalancerRule&id={id}&algorithm={algorithm}&description={description}&name={name}&response={response}";

	/** 从负载均衡规则中删除指定虚拟机uri */
	public static final String REMOVE_FROM_FWR = "?command=removeFromLoadBalancerRule&id={id}&virtualmachineids={virtualmachineids}&response={response}";

	/** 负载均衡规则对应的虚拟机实例uri */
	public static final String LIST_FWR_INSTANCES = "?command=listLoadBalancerRuleInstances&id={id}&applied=true&response={response}";

	/** 错误信息封装 xml */
	public static final String ERROR_MSG_ASSIGN_CONFLICT_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"><errortext>这个防火墙规则已经绑定到虚拟机，请不要重复绑定</errortext></xml>";
	/** 错误信息封装 json */
	public static final String ERROR_MSG_ASSIGN_CONFLICT_JSON = "{\"errortext\":\"这个防火墙规则已经绑定到虚拟机，请不要重复绑定\"}";

}
