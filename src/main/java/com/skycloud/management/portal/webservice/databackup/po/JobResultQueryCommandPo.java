package com.skycloud.management.portal.webservice.databackup.po;

import org.apache.commons.lang.StringUtils;

/**
 * 异步任务结果查询命令对象类
  *<dl>
  *<dt>类名：JobResultQueryCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-5  下午05:31:29</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class JobResultQueryCommandPo {

	private String jobId = "";
	
	private int createUser = 0;
    /**
     * 属性值校验
     * @return
     * 创建人：  刘江宁    
     * 创建时间：2012-1-5  下午05:32:12
     */
	public boolean attributeCheck (){
		if(StringUtils.isBlank(jobId) || createUser == 0){
			return false;
		}else{
			return true;
		}
	}
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}
	
}
