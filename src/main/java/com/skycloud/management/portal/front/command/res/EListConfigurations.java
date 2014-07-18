package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

//to fix bug:0001910
/**
 * 列出Elaster的全局变量配置参数值
 * Lists listConfigurations.
 * @author hefk
 */
public class EListConfigurations extends QueryCommand {

	public static final String COMMAND = "listConfigurations";
	public static final String NAME = "name";

	private String category;
	private String name;
	private String value;
	private String description;
	public EListConfigurations(){
		super(COMMAND);
	}
	public EListConfigurations(String name, String category, 
			String value, String description) {
		super(COMMAND);
		this.category = category;
		this.value = value;
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
}
