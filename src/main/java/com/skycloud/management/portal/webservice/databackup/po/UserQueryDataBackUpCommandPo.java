package com.skycloud.management.portal.webservice.databackup.po;
/**
 * 用户查询备份空间实体类
  *<dl>
  *<dt>类名：UserQueryDataBackUpCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-5  下午04:52:11</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserQueryDataBackUpCommandPo extends BaseDataBackUpCommandPo {
	@Override
	public boolean attributeCheck (){
		
		if(this.getCreateUser() == 0 || this.getTemplateId() == 0)
			return false;
		else
			return true;
	}
}
