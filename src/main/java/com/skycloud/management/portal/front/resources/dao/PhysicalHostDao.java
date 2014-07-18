package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.front.resources.action.vo.PhysicalHostVO;

public interface PhysicalHostDao {

	public PhysicalHostVO findById(Long id);

	/**
	 * 根据条件查询物理机信息列表
	 * ninghao@chinaskycloud.com
	 * 2012-12-03
	 * @param pmHostVO
	 * @return List
	 */
	public List<PhysicalHostVO> findPhysicalHostByOption(PhysicalHostVO pmHostVO) throws Exception;
	
}
