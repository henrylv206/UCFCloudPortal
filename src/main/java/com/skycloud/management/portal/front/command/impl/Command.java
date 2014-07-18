package com.skycloud.management.portal.front.command.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.front.command.ICommand;

public class Command implements ICommand{
	
	private String commandName;
	private Map<String, Object> parameters;
	public Command(String name){
		this.commandName = name;
		this.parameters = new HashMap<String, Object>();
	}
	public void setParameter(String key, Object value){
		this.parameters.put(key, value);
	}
	public Object getParameter(String key){
		return this.parameters.get(key);
	}
	public Map<String,Object> getParameters(){
		return this.parameters;
	}
	public String getCommandName(){
		return commandName;
	}
	
	/**
	 * 返回key=value的键值对,包含command,不包含apiKey
	 * @param map
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<String> getKeyValues(){
		Map<String, Object> map = this.parameters;
		List<String> params = new ArrayList<String>();
		params.add("command="+commandName);
		Iterator<String> it = map.keySet().iterator();
		String key = null;
		Object obj = null;
		try {
			while(it.hasNext()){
				key = it.next();
				obj = map.get(key);
				if(obj instanceof Collection)
					this.addParameter(params, key, (Collection)obj);
				else if(obj instanceof Object[])
					this.addParameter(params, key, (Object[])obj);
				else
					this.addParameter(params, key, obj);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return params;
	}
	
	/**
	 * 
	 * @param keyValues
	 * @return
	 */
	public String getUriParams(){
		List<String> keyValues = getKeyValues();
		StringBuilder sortedUri = new StringBuilder(50);
		boolean first = true;
		for (String param : keyValues) {
			if (first) {
				sortedUri.append(param);
				first = false;
			} else {
				sortedUri.append("&").append(param);
			}
		}
		return sortedUri.toString();
	}
	
	protected void addParameter(List<String> params, String key, Object[] arr) throws UnsupportedEncodingException{
		Object obj1 = null; 
		int index = 0;
		for(Object obj: arr){
			if(index == 0){
				obj1 =obj;
			}else{
				obj1 = obj1.toString().concat(","+obj);
			}
			index++;
		}
		params.add(key+"="+obj1.toString());
	}
	
	protected void addParameter(List<String> params, String key, Collection<Object> col) throws UnsupportedEncodingException{
		Object obj1 = null; 
		int index = 0;
		for(Object obj: col){
			if(index == 0){
				obj1 =obj;
			}else{
				obj1 = obj1.toString().concat(","+obj);
			}
			index++;
		}
		params.add(key+"="+obj1.toString());
//			addParameter(params, key, obj1);
	}
	
	protected void addParameter(List<String> params, String key, Object obj) throws UnsupportedEncodingException{
		params.add(key+"="+URLEncoder.encode(obj.toString(), ENCODING));
	}
}
