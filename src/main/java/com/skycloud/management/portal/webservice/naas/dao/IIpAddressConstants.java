package com.skycloud.management.portal.webservice.naas.dao;

public interface IIpAddressConstants {
	/**显示所有IP地址*/
	public static final String LIST_IP_ADDRESSES = "?command=listPublicIpAddresses&response={response}";
	/**显示指定IP详情*/
	public static final String LIST_IP_ADDRESS = "?command=listPublicIpAddresses&ipaddress={ipaddress}&response={response}";
	/**显示IP对应的具体信息*/
	public static final String LIST_IP_ADDRESS_BY_ID = "?command=listPublicIpAddresses&id={id}&response={response}";
	/**获取新IP*/
	public static final String ASSOCIATE_IP_ADDRESS = "?command=associateIpAddress&zoneid={zoneid}&account={account}&domainId={domainId}&response={response}";

}
