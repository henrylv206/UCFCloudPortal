package com.skycloud.management.portal.webservice.databackup.po;
/**
 * 中软接口模板返回对象属性定义类
  *<dl>
  *<dt>类名：DBTemplate</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:24:26</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DBTemplate {

	private int id;
	
	private String name = "";
	
	private int size;
	
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	
}
