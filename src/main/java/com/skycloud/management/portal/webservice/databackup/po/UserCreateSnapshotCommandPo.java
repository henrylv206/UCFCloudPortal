package com.skycloud.management.portal.webservice.databackup.po;

/**
 * 用户创建快照命令实体类
  *<dl>
  *<dt>类名：UserCreateSnapshotCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午03:51:20</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserCreateSnapshotCommandPo extends BaseDataBackUpCommandPo {
    /**
     * 虚拟机上块存储实例唯一标识
     */
	private int intanceInfoId = 0;
	/**
	 * 快照实例描述
	 */
	private String description;
	/**
	 * 快照类型
	 */
	private int type = 0;
	
	@Override
	public boolean attributeCheck (){
		
		if(this.getCreateUser() == 0 ||intanceInfoId ==0||this.getTemplateId() == 0 || type == 0)
			return false;
		else
			return true;
	}

	public int getIntanceInfoId() {
		return intanceInfoId;
	}

	public void setIntanceInfoId(int intanceInfoId) {
		this.intanceInfoId = intanceInfoId;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
