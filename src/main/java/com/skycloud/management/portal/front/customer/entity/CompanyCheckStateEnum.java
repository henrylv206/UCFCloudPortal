package com.skycloud.management.portal.front.customer.entity;



public class CompanyCheckStateEnum  {
	
	
	/**
	 * 1：待初审（默认）
	 */
	public static final int WAITING_FOR_1ST_CHECK = 1;
	/**
	 * 2：待复审
	 */
	public static final int WAITING_FOR_2ND_CHECK = 2;
	/**
	 * 3：失败
	 */
	public static final int FAILURE = 3;
	/**
	 * 4：成功
	 */
	public static final int SUCCESS = 4;
	/**
	 * 5：挂起
	 */	
	public static final int PAUSE = 5;
	/**
	 * 6：待激活
	 */	
	public static final int WAITING_ACTIVATE= 6;
}
