package com.skycloud.management.portal.webservice.databackup.po;

/**
 * 用户查询快照详细信息命令对象
  *<dl>
  *<dt>类名：UserQuerySnapshotCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午04:45:32</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserQuerySnapshotCommandPo extends BaseDataBackUpCommandPo {
	/**
	 * 快照唯一标识
	 */
	private int snapshotId;
	
	@Override
	public boolean attributeCheck (){
		
		if(this.getCreateUser() == 0 || snapshotId == 0)
			return false;
		else
			return true;
	}
	
	public int getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(int snapshotId) {
		this.snapshotId = snapshotId;
	}
	
}
