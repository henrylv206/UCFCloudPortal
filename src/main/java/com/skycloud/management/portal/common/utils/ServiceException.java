package com.skycloud.management.portal.common.utils;

/**
 * 业务处理对象异常
 * 创建人：  刘江宁   
 * 创建时间：2011-11-22  下午05:50:28
 */
public class ServiceException extends RuntimeException
{

	public ServiceException()
	{
		super();
	}

	public ServiceException(String message)
	{
		super(message);
	}

	public ServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ServiceException(Throwable cause)
	{
		super(cause);
	}
}
