package com.skycloud.management.portal.front.order.dao;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.action.vo.VMResourcesVO;

public interface IJobInstanceInfoDao {
	/**
	 *按用户查询实例，不包括虚拟机和块存储
	 * 
	 * */
	public List<ResourcesVO> queryInstanceByUser(TUserBO user) throws SCSException;
	/**
	 *按订单查询实例，不包括虚拟机和块存储
	 * 
	 * */
	public List<ResourcesVO> queryInstanceByOrder(TOrderBO order) throws SCSException;
	
	/**
	 *按用户查询虚拟机，有状态限制，
	 * 
	 * */
	public List<ResourcesVO> queryVMForUpdateByUser(TUserBO user) throws SCSException;
	
	/**
	 *按用户查询实例，仅包括虚拟机和块存储
	 * 
	 * */
	public List<VMResourcesVO> queryVMByUser(TUserBO user) throws SCSException;
	
	/**
	 *按订单查询实例，仅包括虚拟机和块存储
	 * 
	 * */
	public List<VMResourcesVO> queryVMByOrder(TOrderBO order) throws SCSException;
	
	/**
	 *删除用户的快照
	 * 
	 * */
	public int deleteUserSnapshot (int createUserId)throws SCSException;
	/**
	 *查询用户
	 * 
	 * */
	public List<TUserBO> queryUserByState(int state) throws SCSException;
	/**
	 * 根据存储id查询绑定的虚拟机
	 * */
	public Iri queryIriByDiskId(int diskId)throws SCSException;
	/**
	 * 根据虚拟机id查询绑定的公网IP
	 * */
	public TPublicIPBO queryPublicIPByVMId(int vmId)throws SCSException;
	/**
	 * 根据用户id查询所有资源
	 * */
	public List<ResourcesVO> queryAllInstanceByUser(TUserBO user) throws SCSException;
}
