package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 中软接口快照信息返回对象实体类
  *<dl>
  *<dt>类名：Snapshot</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:31:01</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class Snapshot {

	private String  id;
	
	private String desc;
	
	private String size;
	
	private String createTime;
	
	private String state;

	private String type;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
