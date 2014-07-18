/**
 * 2012-1-31 上午10:49:58 $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.StorageMonitorVO;
import com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao;
import com.skycloud.management.portal.front.resources.service.StorageMonitorService;

/**
 * @author shixq
 * @version $Revision$ 上午10:49:58
 */
public class StorageMonitorServiceImpl implements StorageMonitorService {

	private VirtualMachineListDao vmListDao;

	private String diskRateNA(String diskRate) {
		if (diskRate == null || "".equals(diskRate)) {
			return "N/A";
		}
		return diskRate;
	}

	private String diskRate(JSONArray harddiskKbs, String instanceId) throws SCSException {
		for (int i = 0; i < harddiskKbs.size(); i++) {
			JSONObject jsonObject = harddiskKbs.getJSONObject(i);
			String diskID = jsonObject.getString("hdId");
			if (diskID != null && diskID.equals(instanceId)) {
				return jsonObject.getString("rate");
			}
		}
		return null;
	}

	private StorageMonitorVO updateResultJson(JSONObject result, String instanceId) throws SCSException {
		StorageMonitorVO vo = new StorageMonitorVO();

		JSONArray harddiskKbsReadArray = null;
		JSONArray harddiskKbsWriteArray = null;
		String harddiskKbsRead = null;
		String harddiskKbsWrite = null;
		if (result != null) {
			harddiskKbsReadArray = result.getJSONArray("harddiskKbsRead");
			harddiskKbsWriteArray = result.getJSONArray("harddiskKbsWrite");
		}
		harddiskKbsRead = diskRate(harddiskKbsReadArray, instanceId);
		harddiskKbsWrite = diskRate(harddiskKbsWriteArray, instanceId);
		vo.setDatadiskkbsread(diskRateNA(harddiskKbsRead));
		vo.setDatadiskkbswrite(diskRateNA(harddiskKbsWrite));
		vo.setSpaceused("N/A");
		return vo;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.front.resources.service.StorageMonitorService
	 * #storageMonitorInfo4Elaster(java.lang.String)
	 */
	@Override
	public StorageMonitorVO storageMonitorInfo4Elaster(ResourcesQueryVO queryVO, int resourcePoolsID) throws SCSException {
		StorageMonitorVO vo = new StorageMonitorVO();
		JSONObject resultObject = vmListDao.instanceInfoList4Elaster(queryVO.getId(), resourcePoolsID);
		if (resultObject.isEmpty()) {
			return vo;
		}
		try {
			if (resultObject.containsKey("virtualmachine")) {
				JSONObject result = resultObject.getJSONArray("virtualmachine").getJSONObject(0);
				vo = updateResultJson(result, queryVO.getStorageID());
			}
		}
		catch (Exception e) {

		}
		return vo;
	}

	public VirtualMachineListDao getVmListDao() {
		return vmListDao;
	}

	public void setVmListDao(VirtualMachineListDao vmListDao) {
		this.vmListDao = vmListDao;
	}

}
