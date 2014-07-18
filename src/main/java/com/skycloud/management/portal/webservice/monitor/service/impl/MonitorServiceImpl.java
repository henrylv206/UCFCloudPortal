/**
 * 2012-1-18 下午03:52:51 $Id:shixq
 */
package com.skycloud.management.portal.webservice.monitor.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.MiniComputerMonitorVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.StorageMonitorVO;
import com.skycloud.management.portal.front.resources.action.vo.VirtualMachineMonitorVO;
import com.skycloud.management.portal.front.resources.dao.HCMonitorDao;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao;
import com.skycloud.management.portal.front.resources.service.HCMonitorService;
import com.skycloud.management.portal.front.resources.service.StorageMonitorService;
import com.skycloud.management.portal.front.resources.service.VirtualMachineListService;
import com.skycloud.management.portal.rest.model.BWMonitorInfoReq;
import com.skycloud.management.portal.rest.model.BWMonitorInfoResp;
import com.skycloud.management.portal.rest.model.FirewallMonitorInfoReq;
import com.skycloud.management.portal.rest.model.FirewallMonitorInfoResp;
import com.skycloud.management.portal.rest.model.LBMonitorInfoReq;
import com.skycloud.management.portal.rest.model.LBMonitorInfoResp;
import com.skycloud.management.portal.rest.model.MCMonitorInfoReq;
import com.skycloud.management.portal.rest.model.MCMonitorInfoResp;
import com.skycloud.management.portal.rest.model.PubllcNetworkIPMonitorInfoReq;
import com.skycloud.management.portal.rest.model.PubllcNetworkIPMonitorInfoResp;
import com.skycloud.management.portal.rest.model.VMMonitorInfoReq;
import com.skycloud.management.portal.rest.model.VMMonitorInfoResp;
import com.skycloud.management.portal.rest.model.VolumeMonitorInfoReq;
import com.skycloud.management.portal.rest.model.VolumeMonitorInfoResp;
import com.skycloud.management.portal.webservice.monitor.service.MonitorService;
import com.skycloud.management.portal.webservice.util.WebServiceUtil;

/**
 * @author shixq
 * @version $Revision$ 下午03:52:51
 */
public class MonitorServiceImpl implements MonitorService {

	private static Log log = LogFactory.getLog(MonitorServiceImpl.class);

	private VirtualMachineListDao vmListDao;

	private HCMonitorDao hcMonitorDao;

	private ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao;

	private VirtualMachineListService vmListService;

	private HCMonitorService hcMonitorService;

	private StorageMonitorService storageMonitorService;

	/**
	 * 检查用户是否有访问监控服务条件
	 * 
	 * @param userID
	 * @return
	 * @throws SCSException
	 */

	private boolean checkHaveMonitor(int userID, String monitorType) throws SCSException {
		ResourcesQueryVO vo = new ResourcesQueryVO();
		TUserBO user = new TUserBO();
		user.setId(userID);
		vo.setUser(user);
		vo.setOperateSqlType(5);
		vo.setMonitorType(monitorType);
		int count = resouceServiceInstanceOperateDao.queryResouceServiceInstanceInfoCount(vo);
		if (count != 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryVMMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.VMMonitorInfoReq)
	 */
	@Override
	public VMMonitorInfoResp queryVMMonitorInfoService(VMMonitorInfoReq req) throws SCSException {

		boolean flag = false;
		VMMonitorInfoResp resp = new VMMonitorInfoResp();

		String instanceId = req.getVmID();
		String userID = req.getUserID();

		if (WebServiceUtil.isNotNullString(instanceId)) {
			return resp;
		}
		if (WebServiceUtil.isNotNullString(userID)) {
			return resp;
		}

		flag = checkHaveMonitor(Integer.valueOf(userID), "vm");

		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}
		// 经过与张慧征确认，此处方法没有action调用。
		VirtualMachineMonitorVO vmVO = vmListService.instanceStatisticsInfoList4Elaster(instanceId, 1);

		if (vmVO == null) {
			return resp;
		}
		try {
			resp.setCpuused(vmVO.getCpuused());// CPU利用率
			resp.setMemused(vmVO.getMemused());// 内存利用率
			resp.setNetworkkbsread(vmVO.getNetworkkbsread());// 网络读取
			resp.setNetworkkbswrite(vmVO.getNetworkkbswrite());// 网络写
			resp.setRootdiskkbsread(vmVO.getRootdiskkbsread());
			resp.setRootdiskkbswrite(vmVO.getRootdiskkbswrite());
		}
		catch (Exception e) {
			log.error(e);
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryMCMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.MCMonitorInfoReq)
	 */
	@Override
	public MCMonitorInfoResp queryMCMonitorInfoService(MCMonitorInfoReq req) throws SCSException {
		String instanceId = req.getMcID();
		String userID = req.getUserID();
		boolean flag = false;
		MCMonitorInfoResp resp = new MCMonitorInfoResp();

		if (WebServiceUtil.isNotNullString(instanceId)) {
			return resp;
		}
		if (WebServiceUtil.isNotNullString(userID)) {
			return resp;
		}

		flag = checkHaveMonitor(Integer.valueOf(userID), "mc");
		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}

		MiniComputerMonitorVO resultVO = hcMonitorService.getMonitorInfo(Integer.valueOf(instanceId));
		if (resultVO == null) {
			return resp;
		}
		try {
			resp.setCpuused(resultVO.getCpuused());// CPU利用率
			resp.setMemused(resultVO.getMemused());// 内存利用率
			resp.setNetworkkbsread(resultVO.getNetworkkbsread());// 网络读取
			resp.setNetworkkbswrite(resultVO.getNetworkkbswrite());// 网络写
			resp.setRootdiskkbsread(resultVO.getRootdiskkbsread());
			resp.setRootdiskkbswrite(resultVO.getRootdiskkbswrite());
		}
		catch (Exception e) {
			log.error(e);
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryVolumeMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.VolumeMonitorInfoReq)
	 */
	@Override
	public VolumeMonitorInfoResp queryVolumeMonitorInfoService(VolumeMonitorInfoReq req) throws SCSException {
		String userID = req.getUserID();
		String volumeid = req.getVolumeID();
		String vmID = req.getVmID();
		boolean flag = false;
		VolumeMonitorInfoResp resp = new VolumeMonitorInfoResp();

		flag = checkHaveMonitor(Integer.valueOf(userID), "vl");
		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}
		TUserBO user = new TUserBO();
		user.setId(Integer.valueOf(userID));
		ResourcesQueryVO vo = new ResourcesQueryVO();
		vo.setUser(user);
		vo.setStorageID(volumeid);
		vo.setId(vmID);

		StorageMonitorVO storageVO = storageMonitorService.storageMonitorInfo4Elaster(vo, req.getResourcePoolsId());
		if (storageVO == null) {
			return resp;
		}
		try {
			resp.setSpaceused(storageVO.getSpaceused());
			resp.setDatadiskkbsread(storageVO.getDatadiskkbsread());
			resp.setDatadiskkbswrite(storageVO.getDatadiskkbswrite());
		}
		catch (Exception e) {
			log.error(e);
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryLBMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.LBMonitorInfoReq)
	 */
	@Override
	public LBMonitorInfoResp queryLBMonitorInfoService(LBMonitorInfoReq req) throws SCSException {
		String userID = req.getUserID();
		boolean flag = false;
		LBMonitorInfoResp resp = new LBMonitorInfoResp();

		flag = checkHaveMonitor(Integer.valueOf(userID), "lb");
		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryFirewallMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.FirewallMonitorInfoReq)
	 */
	@Override
	public FirewallMonitorInfoResp queryFirewallMonitorInfoService(FirewallMonitorInfoReq req) throws SCSException {
		String userID = req.getUserID();
		boolean flag = false;
		FirewallMonitorInfoResp resp = new FirewallMonitorInfoResp();

		flag = checkHaveMonitor(Integer.valueOf(userID), "fw");
		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryPubllcNetworkIPMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.PubllcNetworkIPMonitorInfoReq)
	 */
	@Override
	public PubllcNetworkIPMonitorInfoResp queryPubllcNetworkIPMonitorInfoService(PubllcNetworkIPMonitorInfoReq req) throws SCSException {
		String userID = req.getUserID();
		boolean flag = false;
		PubllcNetworkIPMonitorInfoResp resp = new PubllcNetworkIPMonitorInfoResp();

		flag = checkHaveMonitor(Integer.valueOf(userID), "pnip");
		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.webservice.monitor.service.MonitorService
	 * #queryBWMonitorInfoService
	 * (com.skycloud.management.portal.rest.model.BWMonitorInfoReq)
	 */
	@Override
	public BWMonitorInfoResp queryBWMonitorInfoService(BWMonitorInfoReq req) throws SCSException {
		String userID = req.getUserID();
		boolean flag = false;
		BWMonitorInfoResp resp = new BWMonitorInfoResp();

		flag = checkHaveMonitor(Integer.valueOf(userID), "bw");
		if (!flag) {
			resp.setResCode("000000");
			return resp;
		}
		return resp;
	}

	public VirtualMachineListDao getVmListDao() {
		return vmListDao;
	}

	public void setVmListDao(VirtualMachineListDao vmListDao) {
		this.vmListDao = vmListDao;
	}

	public HCMonitorDao getHcMonitorDao() {
		return hcMonitorDao;
	}

	public void setHcMonitorDao(HCMonitorDao hcMonitorDao) {
		this.hcMonitorDao = hcMonitorDao;
	}

	public ResouceServiceInstanceOperateDao getResouceServiceInstanceOperateDao() {
		return resouceServiceInstanceOperateDao;
	}

	public void setResouceServiceInstanceOperateDao(ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao) {
		this.resouceServiceInstanceOperateDao = resouceServiceInstanceOperateDao;
	}

	public VirtualMachineListService getVmListService() {
		return vmListService;
	}

	public void setVmListService(VirtualMachineListService vmListService) {
		this.vmListService = vmListService;
	}

	public HCMonitorService getHcMonitorService() {
		return hcMonitorService;
	}

	public void setHcMonitorService(HCMonitorService hcMonitorService) {
		this.hcMonitorService = hcMonitorService;
	}

	public StorageMonitorService getStorageMonitorService() {
		return storageMonitorService;
	}

	public void setStorageMonitorService(StorageMonitorService storageMonitorService) {
		this.storageMonitorService = storageMonitorService;
	}

}
