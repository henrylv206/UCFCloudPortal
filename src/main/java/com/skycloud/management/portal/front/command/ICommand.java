package com.skycloud.management.portal.front.command;

import java.util.List;
import java.util.Map;

public interface ICommand {
	public static final String ENCODING = "UTF-8";
	
	public void setParameter(String key, Object value);
	
	public Object getParameter(String key);
	
	public Map<String,Object> getParameters();
	
	public String getCommandName();
	
	public List<String> getKeyValues();
	
	public String getUriParams();
}
