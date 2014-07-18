package com.skycloud.management.portal.webservice.databackup.po;
/**
 * 基础备份既服务命令实体类
  *<dl>
  *<dt>类名：BaseDataBackUpCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午03:31:18</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public abstract class BaseDataBackUpCommandPo {
    /**
     * 模板唯一标识
     */
	private int templateId = 0;
	/**
	 * 创建用户唯一标识
	 */
	private int createUser = 0;
    /**
     * 属性校验
     * @param basePo
     * @return
     * 创建人：  刘江宁    
     * 创建时间：2012-1-5  下午03:26:46
     */
	public abstract boolean attributeCheck ();
	
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}
	
	
}
