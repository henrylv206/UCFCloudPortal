package com.skycloud.management.portal.webservice.databackup.po;
/**
 * 用户快照恢复虚拟机
  *<dl>
  *<dt>类名：UserResumeVirtualMachineCommandPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-4  下午03:54:58</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class UserResumeVirtualMachineCommandPo extends BaseDataBackUpCommandPo {
	/**
	 * 快照唯一标识
	 */
	private int snapshotId = 0 ;
	/**
	 * 虚拟机唯一标识
	 */
	private int virtualMachineId = 0;
	/**
	 * 恢复类型
	 */
	private int type = -1;
	
	@Override
	public boolean attributeCheck (){
		
		if(this.getCreateUser() != 0 || snapshotId != 0 || type != -1){
			if(type == 1 || type == 3){
				if(virtualMachineId == 0){
					return false;
				}
				return true;
			}
			return true;
		}else
			return false;
	}
	public int getSnapshotId() {
		return snapshotId;
	}
	public void setSnapshotId(int snapshotId) {
		this.snapshotId = snapshotId;
	}
	public int getVirtualMachineId() {
		return virtualMachineId;
	}
	public void setVirtualMachineId(int virtualMachineId) {
		this.virtualMachineId = virtualMachineId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
