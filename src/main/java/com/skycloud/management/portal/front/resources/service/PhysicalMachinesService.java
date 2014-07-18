package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.PhyHardwareVO;
import com.skycloud.management.portal.front.resources.action.vo.PhysicalHostVO;
import com.skycloud.management.portal.front.resources.action.vo.PhysicalOsTypeVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.enumtype.HostPowerState;
import com.skycloud.management.portal.front.resources.rest.CheckPhysicalHost;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalHost;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalHostHardware;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalOS;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalVlan;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalVlanIP;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalZone;

public interface PhysicalMachinesService {

	/**
	 * 查看用户物理机服务总数
	 * 
	 * @param rqvo
	 * @return
	 * @throws SCSException
	 * 创建人： 冯永凯 
	 * 创建时间：2012-2-9 下午02:24:25
	 */
	int queryPMListCount(ResourcesQueryVO rqvo) throws SCSException;

	/**
	 * 查看物理机服务列表信息
	 * 
	 * @param rqvo
	 * @return
	 * @throws SCSException
	 * 创建人： 冯永凯 
	 * 创建时间：2012-2-9 下午02:25:02
	 */
	List<ResourcesVO> queryPMInstanceList(ResourcesQueryVO rqvo) throws SCSException;

	/**
	 * 插入物理机删除申请
	 * 
	 * @param vo
	 * @param user
	 * @return
	 * @throws SCSException
	 * 创建人： 冯永凯 
	 * 创建时间：2012-2-9 下午02:33:30
	 */
//	String insertPMApplyDestroy(ResourcesModifyVO vo, TUserBO user) throws SCSException;
	
	/**
	 * 插入物理机开机命令
	 * @param vmModifyVO
	 * @return
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-2-9  下午02:34:59
	 */
//	String insertPMStart(ResourcesModifyVO vmModifyVO) throws SCSException;

	/**
	 * 插入物理机重启命令
	 * @param vmModifyVO
	 * @return
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-2-9  下午02:36:34
	 */
//	String insertPMReboot(ResourcesModifyVO vmModifyVO) throws SCSException;
	
	/**
	 * 插入物理机关机命令
	 * @param vmModifyVO
	 * @return
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-2-9  下午02:36:39
	 */
//	String insertPMStop(ResourcesModifyVO vmModifyVO) throws SCSException;
	

	
	
	
	
	/**=================== 重构物理机原来资源服务操作处理逻辑  ninghao 2012-08-30 =======================*/

	/**
	 * 开机
	 * CMD命令方式
	 * @param pmVO
	 * @return String
	 */
//	public String startPhysicalMachineByCMD(ResourcesModifyVO pmVO) throws SCSException;

	/**
	 * 关机
	 * CMD命令方式
	 * @param bo
	 * @return String
	 */
//	public String stopPhysicalMachineByCMD(ResourcesModifyVO pmVO) throws SCSException;

	/**
	 * 重启
	 * CMD命令方式
	 * @param pmVO
	 * @return String
	 */
//	public String resetPhysicalMachineByCMD(ResourcesModifyVO pmVO) throws SCSException;

	/**
	 * 查询物理机当前状态
	 * CMD命令方式
	 * @param bo
	 * @return
	 */
//	public Constants.STATUS_COMMONS getPhysicalMachinePowerStatusByCMD(PhysicalHostVO bo) throws SCSException;


	/**
	 * 物理机退订申请
	 * @param vo
	 * @param user
	 * @return
	 * @throws SCSException
	 * @throws Exception 
	 */
	public TInstanceInfoBO applyPhyDestroy(ResourcesModifyVO vo, TUserBO user) throws SCSException;

	
	
	
	/**=================== 物理机，多资源池 Restful接口，操作处理 ninghao 2012-12-10 =======================*/

	/**
	 * 查询可用的物理机
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	public List<PhysicalHostVO> findPhysicalHostByREST(ListPhysicalHost listVO , int resourcePoolsId) 
	throws SCSException;

	/**
	 * 查询可用的物理机规格信息
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	public List<PhyHardwareVO> findPhysicalHostHardwaresByREST(ListPhysicalHostHardware listVO , int resourcePoolsId) 
	throws SCSException;

	/**
	 * 查询物理机的操作系统osType
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	public List<PhysicalOsTypeVO> findPhysicalHostOsTypeByREST(ListPhysicalOS listOSVO , int resourcePoolsId) 
	throws SCSException;

	/**
	 * 查询物理机的资源域
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	public List<ListPhysicalZone> findPhysicalHostZoneByREST(ListPhysicalZone listVO , int resourcePoolsId) 
	throws SCSException;

	/**
	 * 查询资源域下的VLAN信息
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	public List<ListPhysicalVlan> findPhysicalHostVlanByREST(ListPhysicalVlan listVO , int resourcePoolsId) 
	throws SCSException;

	/**
	 * 查询资源域下的VLAN下的IP地址信息
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	public List<ListPhysicalVlanIP> findPhysicalHostVlanIpByREST(ListPhysicalVlanIP listVO , int resourcePoolsId) 
	throws SCSException;

	/**
	 * 特殊模板申请创建申请模板
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	public TTemplateVMBO creatSpecalPhysicalTemplate(TVmInfo vminfo,TUserBO user,TTemplateVMBO template,Product product) throws SCSException;

	/**
	 * 开机
	 * CMD命令方式
	 * @param pmVO
	 * @return String
	 */
	public String startPhysicalHost(int infoId , int hostId) throws SCSException;

	/**
	 * 关机
	 * CMD命令方式
	 * @param bo
	 * @return String
	 */
	public String stopPhysicalHost(int infoId , int hostId) throws SCSException;

	/**
	 * 重启
	 * CMD命令方式
	 * @param pmVO
	 * @return String
	 */
	public String resetPhysicalHost(int infoId , int hostId) throws SCSException;

	/**
	 * 查询物理机当前状态
	 * CMD命令方式
	 * @param bo
	 * @return
	 */
	public HostPowerState getPhysicalHostPowerStatus(int infoId , int hostId) throws SCSException;


	/**
	 * 物理机退订申请
	 * @param vo
	 * @param user
	 * @return
	 * @throws SCSException
	 * @throws Exception 
	 */
	public String destroyPhysicalHost(int infoId , int hostId) throws SCSException;

	/**
	 * 检查物理机是否可用
	 * @param checkPhysicalHost
	 * @param resourcePoolId
	 * @return 返回值 >0 可用物理机数，=0 资源不足  <0 调用接口失败
	 * @throws Exception
	 */
	public int CheckPhysicalHost(CheckPhysicalHost checkPhysicalHost,Integer resourcePoolId) throws Exception ;

	
}
