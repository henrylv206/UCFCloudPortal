package com.skycloud.management.portal.webservice.databackup.jdbc;

import org.springframework.dao.DataAccessException;


/**
 * DAO操作的异常
 * 
 * @author 刘江宁
 */
public class DaoException extends DataAccessException
{
	public DaoException(String message)
	{
		super(message);
	}

	public DaoException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public DaoException(Throwable cause)
	{
		super(cause.getMessage(),cause);
	}
	
}
