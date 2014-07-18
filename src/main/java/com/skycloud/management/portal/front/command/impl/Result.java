package com.skycloud.management.portal.front.command.impl;

import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.front.command.IResult;

public class Result implements IResult{
	
	private Object result;
	private Type type;
	
	@SuppressWarnings({"rawtypes" })
	public Result(Object obj){
		if(obj instanceof Exception)
			type = Type.ERROR;
		else if(obj instanceof Map){
			//errorcode,errortext
			Map map = (Map)obj;
			if(map.get(ERRORTEXT) != null)
				type = Type.STATUS_ERROR;
			else
				type = Type.MAP;
		}else if(obj instanceof List)
			type = Type.LIST;
		else
			type = Type.UNIQUE;
		result = obj;
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append(this.getClass().getName())
			.append("(type: ").append(type)
			.append(", result: ").append(result)
			.append(")");
		return s.toString();
	}
	
	@SuppressWarnings("unchecked")
	public <X> X getResult(){
		return (X)this.result;
	}
	
	public boolean isUnique(){
		return type == Type.UNIQUE;
	}
	
	public boolean isError(){
		return type == Type.ERROR;
	}
	
	public boolean isList(){
		return type == Type.LIST;
	}
	
	public boolean isMap(){
		return type == Type.MAP;
	}
	
	public boolean isStatusError(){
		return type == Type.STATUS_ERROR;
	}
	
	public String getErrorText() {
		if(!isStatusError())
			return null;
		Map<String, Object> map = this.getResult();
		return String.valueOf(map.get(ERRORTEXT));
	}
	
	public static enum Type{
		UNIQUE, LIST, MAP, ERROR, STATUS_ERROR
	}
}