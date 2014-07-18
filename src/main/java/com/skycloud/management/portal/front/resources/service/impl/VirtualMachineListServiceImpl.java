/**
 * 2011-12-13 下午03:11:30 $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ListVolumes;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.action.vo.VirtualMachineMonitorVO;
import com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao;
import com.skycloud.management.portal.front.resources.enumtype.VMStateType;
import com.skycloud.management.portal.front.resources.service.VirtualMachineListService;

/**
 * @author shixq
 * @version $Revision$ 下午03:11:30
 */
public class VirtualMachineListServiceImpl implements VirtualMachineListService {

	private static Log log = LogFactory.getLog(VirtualMachineListServiceImpl.class);

	private VirtualMachineListDao vmListDao;

	private ICommandService commandService;

	private VirtualMachineMonitorVO updateResultJson(JSONObject result, int resourcePoolsId) throws SCSException {
		VirtualMachineMonitorVO vmVO = new VirtualMachineMonitorVO();

		JSONArray harddiskKbsReadArray = null;
		JSONArray harddiskKbsWriteArray = null;
		String cpunumber = null;
		String cpuused = null;
		String memused = null;
		String networkkbsread = null;
		String networkkbswrite = null;
		String harddiskKbsRead = null;
		String harddiskKbsWrite = null;
		String memory = null;
		String memoryInternalFree = null;
		String hypervisor = null;
		if (result != null) {
			cpunumber = result.getString("cpunumber");
			cpuused = result.getString("cpuused");
			networkkbsread = result.getString("networkkbsread");
			networkkbswrite = result.getString("networkkbswrite");
			harddiskKbsReadArray = result.getJSONArray("harddiskKbsRead");
			harddiskKbsWriteArray = result.getJSONArray("harddiskKbsWrite");
			memory = result.getString("memory");
			memoryInternalFree = result.getString("memoryInternalFree");
			hypervisor = result.getString("hypervisor");
		}
		harddiskKbsRead = rootDisk(harddiskKbsReadArray, resourcePoolsId);
		harddiskKbsWrite = rootDisk(harddiskKbsWriteArray, resourcePoolsId);

		if (memory != null && !"".equals(memory) && memoryInternalFree != null && !"".equals(memoryInternalFree)) {
			BigDecimal momoryB = new BigDecimal(memory);
			BigDecimal memoryInternalFreeB = new BigDecimal(memoryInternalFree);
			memused = momoryB.divide(memoryInternalFreeB, 2, BigDecimal.ROUND_UP).toString() + "%";
		}

		vmVO.setCpunumber(cpunumber);// 总的CPU
		vmVO.setCpuused(cpuused);// CPU使用率
		vmVO.setNetworkkbsread(networkkbsread);// 网络读取
		vmVO.setNetworkkbswrite(networkkbswrite);// 网络写
		vmVO.setRootdiskkbsread(harddiskKbsRead);
		vmVO.setRootdiskkbswrite(harddiskKbsWrite);
		vmVO.setMemused(memused);
		vmVO.setHypervisor(hypervisor);
		return vmVO;
	}

	private String rootDisk(JSONArray harddiskKbs, int resourcePoolsId) throws SCSException {
		for (int i = 0; i < harddiskKbs.size(); i++) {
			JSONObject jsonObject = harddiskKbs.getJSONObject(i);
			String diskName = jsonObject.getString("name");
			if (diskName != null && !"".equals(diskName) && diskName.indexOf("ROOT") != -1) {
				String diskID = jsonObject.getString("hdId");
				ListVolumes volumeElasterList = new ListVolumes();
				volumeElasterList.setId(diskID);
				Object response = null;
				try {
					response = commandService.executeAndJsonReturn(volumeElasterList, resourcePoolsId);
					JSONObject jsonRes = JSONObject.fromObject(response);
					String resultListvolumesresponse = jsonRes.getString("listvolumesresponse");
					JSONObject resultObjectVolumesresponse = JSONObject.fromObject(resultListvolumesresponse);
					if (resultObjectVolumesresponse.isEmpty()) {
						return null;
					}
					String resultListVolume = resultObjectVolumesresponse.getString("volume");
					JSONArray resultObjectVolume = JSONArray.fromObject(resultListVolume);
					if (resultObjectVolume.isEmpty()) {
						return null;
					}
					String diskType = resultObjectVolume.getJSONObject(0).getString("type");
					if (diskType != null && !"".equals(diskType) && diskType.equals("ROOT")) {
						String harddisk = jsonObject.getString("rate");
						return harddisk;
					}
				}
				catch (Exception e) {
					throw new SCSException(e.getMessage());
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.skycloud.management.portal.front.resources.service.
	 * VirtualMachineListService
	 * #instanceStatisticsInfoList4Elaster(java.util.List)
	 */
	@Override
	public VirtualMachineMonitorVO instanceStatisticsInfoList4Elaster(String instanceId, int resourcePoolsId) throws SCSException {
		VirtualMachineMonitorVO vmVO = new VirtualMachineMonitorVO();
		JSONObject resultObject = vmListDao.instanceInfoList4Elaster(instanceId, resourcePoolsId);
		if (resultObject.isEmpty()) {
			return vmVO;
		}
		try {
			if (resultObject.containsKey("virtualmachine")) {
				JSONObject result = resultObject.getJSONArray("virtualmachine").getJSONObject(0);
				vmVO = updateResultJson(result, resourcePoolsId);
			}
		}
		catch (Exception e) {

		}
		return vmVO;
	}

	/*
	 * (non-Javadoc)
	 * @see com.skycloud.management.portal.front.resources.service.
	 * VirtualMachineListService#instanceInfoList4Elaster(java.util.List)
	 */
	@Override
	public List<ResourcesVO> instanceInfoList4Elaster(List<ResourcesVO> list) throws SCSException {
		try {
			for (ResourcesVO vo : list) {
				String instanceId = vo.getE_instance_id();
				int resourcePoolsId = vo.getResourcePoolsId();
				String state = vo.getState();

				if (state != null && !"".equals(state) && instanceId != null && !"".equals(instanceId) && !"6".equals(state)) {
					String stateNew = "";
					JSONObject resultObject = vmListDao.instanceInfoList4Elaster(instanceId, resourcePoolsId);
					if (resultObject == null || resultObject.isEmpty()) {//fix bug 7661 后台查询调用Elaster的API时，返回数据增加非空判断
						stateNew = "-1";
					}
					else if (resultObject.containsKey("virtualmachine")) {
						JSONObject resultO = resultObject.getJSONArray("virtualmachine").getJSONObject(0);
						stateNew = resultO.getString("state");
						// 2011.01.04添加从elaster读取ip地址
						JSONArray nicArray = resultO.getJSONArray("nic");
						//fix bug 7599 多实例，增加判断资源池是否返回网卡信息。
						if (vo.getNicsBOs() != null && nicArray != null 
								&& !"".equals(nicArray.toString()) 
								&& !"[]".equals(nicArray.toString())
								&& nicArray.size() == vo.getNicsBOs().size()//fix bug 7605 资源池返回网卡个数与本地一致时执行替换IP操作。
							) 
						{
							for (int i = 0; i < vo.getNicsBOs().size(); i++) {
								if (nicArray.getJSONObject(i).getLong("networkid") == vo.getNicsBOs().get(i).geteVlanId()) {
									String ip = nicArray.getJSONObject(i).getString("ipaddress");
									vo.getNicsBOs().get(i).setIp(ip);
								}
							}
						}else{
							vo.setNicsBOs(null);
						}

						// 添加是否添加光盘属性
						if (resultO.containsKey("isoid")) {
							vo.setIsois("yes");
						}
					}
					log.info("======start to query elaster state by" + instanceId + "" + ",state old is" + stateNew);
					if (stateNew != null && !"".equals(stateNew)) {
						try {
							if ("7".equals(state)) {
								stateNew = "7";
							} else {
								stateNew = VMStateType.valueOf(stateNew).getValue();
							}

						}
						catch (Exception e) {
						}
					}
					log.info("======end to query elaster state by" + instanceId + "" + ",state new is" + stateNew);
					vo.setState(stateNew);
				}
			}
		}
		catch (SCSException e) {
			throw e;
		}

		return list;
	}

	@Override
	public List<ResourcesVO> instanceInfoList4ElasterDefault(List<ResourcesVO> list) throws SCSException {
		try {
			for (ResourcesVO vo : list) {
				String instanceId = vo.getE_instance_id();
				int resourcePoolsId = vo.getResourcePoolsId();
				String state = vo.getState();

				if (state != null && !"".equals(state) && instanceId != null && !"".equals(instanceId) && !"6".equals(state)) {
					String stateNew = "";
					JSONObject resultObject = vmListDao.instanceInfoList4Elaster(instanceId, resourcePoolsId);
					if (resultObject.isEmpty()) {
						stateNew = "-1";
					}
					if (resultObject.containsKey("virtualmachine")) {
						JSONObject resultO = resultObject.getJSONArray("virtualmachine").getJSONObject(0);
						stateNew = resultO.getString("state");
						// 2011.01.04添加从elaster读取ip地址
						JSONArray nicArray = resultO.getJSONArray("nic");
						if (vo.getNicsBOs() != null) {
							for (int i = 0; i < vo.getNicsBOs().size(); i++) {
								// 取<isdefault>true</isdefault>的网卡 fix bug 2704
								for (int j = 0; j < nicArray.size(); j++) {
									JSONObject nic = nicArray.getJSONObject(j);
									String isdefault = nic.getString("isdefault");
									if ("true".equals(isdefault) && nic.getLong("networkid") == vo.getNicsBOs().get(i).geteVlanId()) {
										String ip = nic.getString("ipaddress");
										vo.getNicsBOs().get(i).setIp(ip);
										break;
									}
								}
							}
						}

						// 添加是否添加光盘属性
						if (resultO.containsKey("isoid")) {
							vo.setIsois("yes");
						}
					}
					log.info("======start to query elaster state by" + instanceId + "" + ",state old is" + stateNew);
					if (stateNew != null && !"".equals(stateNew)) {
						try {
							if ("7".equals(state)) {
								stateNew = "7";
							} else {
								stateNew = VMStateType.valueOf(stateNew).getValue();
							}

						}
						catch (Exception e) {
						}
					}
					log.info("======end to query elaster state by" + instanceId + "" + ",state new is" + stateNew);
					vo.setState(stateNew);
				}
			}
		}
		catch (SCSException e) {
			throw e;
		}

		return list;
	}

	public VirtualMachineListDao getVmListDao() {
		return vmListDao;
	}

	public void setVmListDao(VirtualMachineListDao vmListDao) {
		this.vmListDao = vmListDao;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

}
