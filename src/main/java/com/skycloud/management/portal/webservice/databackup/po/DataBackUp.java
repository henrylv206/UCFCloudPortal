package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 中软接口备份空间相信信息返回对象类
  *<dl>
  *<dt>类名：DataBackUp</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:26:05</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DataBackUp {

	private int total;
	
	private int used;

	

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}
	
	
}
