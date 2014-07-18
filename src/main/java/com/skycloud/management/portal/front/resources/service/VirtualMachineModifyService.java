/**
 * 2011-11-28  下午03:09:13  $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.VolumeResumeVO;

/**
 * @author shixq
 * @version $Revision$ $Date$
 */
public interface VirtualMachineModifyService {
	/**
	 * 虚拟机开机
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMStart(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 虚拟机重启
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMReboot(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 虚拟机停止
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMStop(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 虚拟机暂停
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMPause(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 对象存储变更
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertOMModify(ResourcesModifyVO vmModifyVO, TUserBO user)
	throws SCSException;

	String insertDISKModify(ResourcesModifyVO vmModifyVO, TUserBO user)
	throws SCSException;

	/**
	 * 虚拟机变更
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMModify(ResourcesModifyVO vmModifyVO, TUserBO user)
	throws SCSException;

	/**
	 * 虚拟机恢复
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMRestore(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 虚拟机挂载光盘
	 * 
	 * @return
	 */
	String insertVMMountIOS(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 查询社区光盘
	 * 
	 * @return
	 */
	String listCommunityIso(int resourcePoolsId) throws SCSException;

	/**
	 * 虚拟机卸载光盘
	 * 
	 * @return
	 */
	String insertDetachIso(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 虚拟机卷快照
	 * 
	 * @return
	 */
	String insertVMVolumeSnapshot(ResourcesModifyVO vmModifyVO, int resourcePoolsId)
	throws SCSException;

	/**
	 * 虚拟机快照恢复
	 * 
	 * @return
	 */
	String insertVMSnapshotResume(VolumeResumeVO volumeResumeVO, TUserBO user,
			String res_code, int resourcePoolsId) throws SCSException;

	/**
	 * 虚拟机申请修改
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMApplyUpdate(ResourcesModifyVO vo, TUserBO user)
	throws SCSException;

	/**
	 * 虚拟机销毁
	 * 
	 * @return
	 * @throws SCSException
	 */
	String insertVMAapplyDestroy(ResourcesModifyVO vo, TUserBO user)
	throws SCSException;

	String insertVMDestroy(ResourcesModifyVO vo, TUserBO user, int serviceID)
	throws SCSException;

	String insertVMSDestroy(ResourcesModifyVO vo, TUserBO user, int serviceID)
	throws SCSException;

	/**
	 * 获取虚机快照信息
	 * 
	 * @param vmModifyVO
	 * @return
	 * @throws SCSException
	 */
	List<VolumeResumeVO> queryVMRootSnapshotById(ResourcesModifyVO vmModifyVO, int resourcePoolsId)
	throws SCSException;

	/**
	 * 虚拟机快照删除
	 * 
	 * @return
	 */
	String insertVMSnapshotDelete(VolumeResumeVO volumeResumeVO, int userId,
			String res_code) throws SCSException;

	public TInstanceInfoBO updateInstanceInfoState(ResourcesModifyVO vmModifyVO)
	throws SCSException;

	public TTemplateVMBO creatSpecalVMTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;

	public TTemplateVMBO creatSpecalVdiskTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;
	public TTemplateVMBO creatSpecalBKTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;

}
