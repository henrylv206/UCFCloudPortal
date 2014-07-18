package com.skycloud.management.portal.webservice.databackup.po;
/**
 * 用户删除快照命令实体对象
  *<dl>
  *<dt>类名：UserDeleteSnapshotCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午04:25:34</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserDeleteSnapshotCommandPo extends BaseDataBackUpCommandPo {
	/**
	 * 快照唯一标识
	 */
	private int snapshotId = 0;

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
