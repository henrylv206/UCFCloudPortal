package com.skycloud.management.portal.front.command;

public interface IResult {
	public static final String ERRORCODE = "errorcode";
	public static final String ERRORTEXT = "errortext";
	public <X> X getResult();
	boolean isUnique();
	boolean isError();
	boolean isList();
	boolean isMap();
	boolean isStatusError();
	String getErrorText();
}
