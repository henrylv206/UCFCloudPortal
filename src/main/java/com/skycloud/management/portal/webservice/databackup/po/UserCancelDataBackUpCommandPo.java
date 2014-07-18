package com.skycloud.management.portal.webservice.databackup.po;

/**
 * 用户撤销备份服务命令对象
  *<dl>
  *<dt>类名：UserCancelDataBackUpCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午04:29:25</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserCancelDataBackUpCommandPo extends BaseDataBackUpCommandPo {
	
	@Override
	public boolean attributeCheck (){
		
		if(this.getCreateUser() == 0)
			return false;
		else
			return true;
	}
}
