package com.skycloud.management.portal.webservice.naas.dao;

/**
 * 本类对应的是Elaster里的PublicIP,而非传统意义上的外网IP
 * 
 * @author liujijun
 * @since Feb 4, 2012
 * @version 1.0
 */
public interface IIpAddressDao {
	/**
	 * 
	 * @param ipAddress
	 * @return
	 */
	public String listPublicIpAddress(String ipAddress);

	/**
	 * 显示可用的IP地址
	 * 
	 * @return
	 */
	public String listAvailableIpAddresses();

	/**
	 * 检查IP地址是否可用
	 * 
	 * @param ipAddress
	 * @return
	 */
	public boolean isAvailableIpAddress(String ipAddress);

	/**
	 * 获取新的IP地址
	 * 
	 * @param zoneId
	 * @param account 
	 * @param domainId 
	 * @return
	 */
	public String associateIpAddress(int zoneId, int domainId, String account);
}
