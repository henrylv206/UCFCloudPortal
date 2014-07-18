package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.front.resources.entity.VirtualService;

/**
 * H3C网络规划虚服务对象持久化接口
 * 
 * @author liujijun
 */
public interface IVirtualServiceService {

	/**
	 * 创建虚服务
	 * 
	 * @param service
	 *            虚服务
	 * @throws Exception
	 */
	public void createService(VirtualService service) throws Exception;

	/**
	 * 根据设备id获取虚服务列表
	 * 
	 * @param deviceId
	 *            设备id
	 * @return 虚服务列表
	 * @throws Exception
	 */
	public List<VirtualService> getServiceListByDevice(int deviceId) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<VirtualService> getServiceListByUser(int userId) throws Exception;

	/**
	 * 根据用户id获取虚服务绑定的公网ip数量
	 * 
	 * @param deviceId
	 *            设备id
	 * @return 虚服务列表
	 * @throws Exception
	 */
	public int getPublicIPCntByVS(int userId) throws Exception;
}
