package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.front.resources.entity.VLan;

/**
 * H3C网络规划VLan对象持久化接口
 * 
 * @author liujijun
 */
public interface IVLanService {

	/**
	 * 创建vlan
	 * 
	 * @param vlan
	 *            vlan对象
	 * @throws Exception
	 */
	public void createVLan(VLan vlan) throws Exception;

	/**
	 * 更新vlan
	 * 
	 * @param vlan
	 *            vlan对象
	 * @throws Exception
	 */
	public void updateVLan(VLan vlan) throws Exception;

	/**
	 * 根据id获取vlan对象
	 * 
	 * @param id
	 *            vlan id
	 * @return vlan对象
	 * @throws Exception
	 */
	public VLan getVLanById(int id) throws Exception;

	/**
	 * 获取vlan列表
	 * 
	 * @param series
	 *            系列标识
	 * @return vlan列表
	 * @throws Exception
	 */
	public List<VLan> getVLanList(String series) throws Exception;

	/**
	 * 获取指定类型vlan的一个空闲对象
	 * 
	 * @param type
	 *            vlan类型
	 * @return vlan对象
	 * @throws Exception
	 */
	public VLan getIdleVLanByType(String type) throws Exception;

	/**
	 * 根据当前vlan的vdId获取配对vlan
	 * 
	 * @param vlan
	 *            当前vlan
	 * @return 配对vlan
	 * @throws Exception
	 */
	public VLan getPairVLan(VLan vlan) throws Exception;

	/**
	 * 根据上游设备编号取得对应vlan列表
	 * 
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public List<VLan> getVLanList(int srcDeviceId) throws Exception;

	/**
	 * 编辑vlan
	 * 
	 * @param vlan
	 */
	public void editVLan(VLan vlan) throws Exception;

	/**
	 * 删除VLAN
	 * @param vlanId
	 * @throws Exception
	 */
	public void removeVLan(int vlanId) throws Exception;

}
