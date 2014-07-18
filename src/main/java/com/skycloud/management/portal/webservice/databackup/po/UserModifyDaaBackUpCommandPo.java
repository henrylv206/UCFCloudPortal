package com.skycloud.management.portal.webservice.databackup.po;
/**
 * 用户修改存储空间校验命令对象
  *<dl>
  *<dt>类名：UserApplyDaaBackUp</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午03:45:28</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserModifyDaaBackUpCommandPo extends BaseDataBackUpCommandPo {
	/**
	 * 原有模板唯一标识
	 */
	private int oldTemplateId = 0;
	@Override
	public boolean attributeCheck (){
		
		if(this.getCreateUser() == 0 || oldTemplateId == 0 || this.getTemplateId() == 0)
			return false;
		else
			return true;
	}
	public int getOldTemplateId() {
		return oldTemplateId;
	}
	public void setOldTemplateId(int oldTemplateId) {
		this.oldTemplateId = oldTemplateId;
	}
	
}
