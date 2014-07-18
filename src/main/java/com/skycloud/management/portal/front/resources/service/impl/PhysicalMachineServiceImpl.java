package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.BeanFactoryUtil;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.instance.service.IRestfulService;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.PhyHardwareVO;
import com.skycloud.management.portal.front.resources.action.vo.PhysicalHostVO;
import com.skycloud.management.portal.front.resources.action.vo.PhysicalOsTypeVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.PhysicalHostDao;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.enumtype.HostPowerState;
import com.skycloud.management.portal.front.resources.rest.CheckPhysicalHost;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalHost;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalHostHardware;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalOS;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalVlan;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalVlanIP;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalZone;
import com.skycloud.management.portal.front.resources.rest.PhysicalDeleteHost;
import com.skycloud.management.portal.front.resources.rest.PhysicalGetHostPowerState;
import com.skycloud.management.portal.front.resources.rest.PhysicalResetHost;
import com.skycloud.management.portal.front.resources.rest.PhysicalStartHost;
import com.skycloud.management.portal.front.resources.rest.PhysicalStopHost;
import com.skycloud.management.portal.front.resources.service.PhysicalMachinesService;

public class PhysicalMachineServiceImpl implements PhysicalMachinesService {

	private static Log log = LogFactory.getLog(PhysicalMachineServiceImpl.class);

	private ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao;

	//	private VirtualMachineModifyService  vmModifyService;

	@Override
	public int queryPMListCount(ResourcesQueryVO rqvo) throws SCSException {
		rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_PM);
		return resouceServiceInstanceOperateDao
		.queryResouceServiceInstanceInfoCount(rqvo);
	}

	@Override
	public List<ResourcesVO> queryPMInstanceList(ResourcesQueryVO rqvo)
	throws SCSException {
		List<ResourcesVO> resultList = new ArrayList<ResourcesVO>();
		try {
			rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_PM);
			resultList = resouceServiceInstanceOperateDao
			.queryResouceServiceInstanceInfo(rqvo);
		} catch (SCSException e) {
			throw new SCSException(e.getMessage());
		}
		return resultList;
	}

	public ResouceServiceInstanceOperateDao getResouceServiceInstanceOperateDao() {
		return resouceServiceInstanceOperateDao;
	}

	public void setResouceServiceInstanceOperateDao(
			ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao) {
		this.resouceServiceInstanceOperateDao = resouceServiceInstanceOperateDao;
	}

	/**
	 * 重构物理机原来资源服务操作处理逻辑
	 * ninghao 2012-08-30
	 */
	private final static Logger logger = LoggerFactory.getLogger(PhysicalMachinesService.class);

	private static Runtime runTime = Runtime.getRuntime();
	//物理机注入HOST物理主机信息DAO
	private PhysicalHostDao physicalHostDao;
	//订单DAO
	private IOrderDao orderDao;
	private IInstanceInfoDao instanceInfoDao;

	public PhysicalHostDao getPhysicalHostDao() {
		return physicalHostDao;
	}

	public void setPhysicalHostDao(PhysicalHostDao physicalHostDao) {
		this.physicalHostDao = physicalHostDao;
	}
	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	/**
	 * 开机
	 * CMD命令方式
	 * @param pmVO
	 * @return boolean
	 */
	//	public String startPhysicalMachineByCMD(ResourcesModifyVO pmVO) throws SCSException {
	//		if(pmVO == null || pmVO.geteInstanceId() <= 0){
	//			logger.error("Start host Error! host is null or host eInstanceId <= 0 ");
	//			return "执行开机操作失败：参数资源实例ID不正确！";
	//		}
	//		//根据资源实例查询物理机Host信息
	//		Long id = new Long(pmVO.geteInstanceId());
	//		PhysicalHostVO host = physicalHostDao.findById(id);
	//		//效验物理机Host信息：执行命令参数是否已配置完整
	//		String checkMSG = this.checkHostInfo(host);
	//		if(checkMSG != null){
	//			return "执行开机操作失败：" + checkMSG;
	//		}
	//		//判断开关机状态
	//		Constants.STATUS_COMMONS state = this.getPhysicalMachinePowerStatusByCMD(host);
	//		if(state == Constants.STATUS_COMMONS.ON)
	//		{
	//			return "未执行开机操作：该机器已是开机状态，无需再次执行开机操作。";
	//		}
	//
	//		//host信息正确无误，允许执行开机命令
	//		String cmd = "ipmitool -H "+host.getIpmiip()
	//					+" -I lanplus -U "+host.getIpmiusername()
	//					+" -P "+host.getIpmipassword()
	//					+"  power on";
	//		try{//执行开机命令
	//			runTime.exec(cmd);
	//			return "操作成功!";
	//		}catch (Exception e) {
	//			//执行开机命令失败
	//			logger.error("Start host Error! "+e.getMessage());
	//			return "执行开机操作失败：" + e.getMessage();
	//		}
	//	}

	/**
	 * 关机
	 * CMD命令方式
	 * @param bo
	 * @return boolean
	 */
	//	public String stopPhysicalMachineByCMD(ResourcesModifyVO pmVO) throws SCSException {
	//		if(pmVO == null || pmVO.geteInstanceId() <= 0){
	//			logger.error("Start host Error! host is null or host eInstanceId <= 0 ");
	//			return "执行关机操作失败：参数资源实例ID不正确！";
	//		}
	//		//根据资源实例查询物理机Host信息
	//		Long id = new Long(pmVO.geteInstanceId());
	//		PhysicalHostVO host = physicalHostDao.findById(id);
	//		//效验物理机Host信息：执行命令参数是否已配置完整
	//		String checkMSG = this.checkHostInfo(host);
	//		if(checkMSG != null){
	//			return "执行关机操作失败：" + checkMSG;
	//		}
	//		//判断开关机状态
	//		Constants.STATUS_COMMONS state = this.getPhysicalMachinePowerStatusByCMD(host);
	//		if(state == Constants.STATUS_COMMONS.OFF)
	//		{
	//			return "未执行关机操作：该机器已是关机状态，无需再次执行关机操作。";
	//		}
	//		//host信息正确无误，允许执行关机命令
	//		String cmd = "ipmitool -H "+host.getIpmiip()
	//					+" -I lanplus -U "+host.getIpmiusername()
	//					+" -P "+host.getIpmipassword()
	//					+"  power off";
	//		try{//执行关机命令
	//			logger.debug(cmd);
	//			runTime.exec(cmd);
	//			return "操作成功!";
	//		}catch (Exception e) {
	//			logger.error("Stop host Error! "+e.getMessage());
	//			return "执行关机操作失败：" + e.getMessage();
	//		}
	//	}

	/**
	 * 重启
	 * CMD命令方式
	 * @param pmVO
	 * @return boolean
	 */
	//	public String resetPhysicalMachineByCMD(ResourcesModifyVO pmVO) throws SCSException {
	//		if(pmVO == null || pmVO.geteInstanceId() <= 0){
	//			logger.error("Start host Error! host is null or host eInstanceId <= 0 ");
	//			return "执行重启操作失败：参数资源实例ID不正确！";
	//		}
	//		//根据资源实例查询物理机Host信息
	//		Long id = new Long(pmVO.geteInstanceId());
	//		PhysicalHostVO host = physicalHostDao.findById(id);
	//		//效验物理机Host信息：执行命令参数是否已配置完整
	//		String checkMSG = this.checkHostInfo(host);
	//		if(checkMSG != null){
	//			return "执行重启操作失败：" + checkMSG;
	//		}
	//		//host信息正确无误，允许执行重启命令
	//		Constants.STATUS_COMMONS state = this.getPhysicalMachinePowerStatusByCMD(host);
	//		String cmd = "ipmitool -H "+host.getIpmiip()
	//					+" -I lanplus -U "+host.getIpmiusername()
	//					+" -P "+host.getIpmipassword()
	//					+"  power reset";
	//		//只判断开关机状态
	//		if(state == Constants.STATUS_COMMONS.OFF){
	//			//原状态为关机，执行开机操作
	//			cmd = "ipmitool -H "+host.getIpmiip()
	//				 +" -I lanplus -U "+host.getIpmiusername()
	//				 +" -P "+host.getIpmipassword()
	//				 +"  power on";
	//		}
	//		else if(state == Constants.STATUS_COMMONS.ON){
	//			//原状态为开机，执行重启操作
	//			cmd = "ipmitool -H "+host.getIpmiip()
	//				 +" -I lanplus -U "+host.getIpmiusername()
	//				 +" -P "+host.getIpmipassword()
	//				 +"  power reset";
	//		}
	//		else{//应该没有其他状态
	//			return "执行重启操作失败：物理机状态异常，请检查物理机是否故障！" ;
	//		}
	//		try{//执行重启命令
	//			logger.debug(cmd);
	//			runTime.exec(cmd);
	//			return "操作成功!";
	//		}catch (Exception e) {
	//			logger.error("Reset host Error! "+e.getMessage());
	//			return "执行重启操作失败：" + e.getMessage();
	//		}
	//	}

	/**
	 * 查询物理机当前状态
	 * CMD命令方式
	 * @param host
	 * @return Constants.STATUS_COMMONS
	 */
	//	public Constants.STATUS_COMMONS getPhysicalMachinePowerStatusByCMD(PhysicalHostVO host) throws SCSException {
	//		Constants.STATUS_COMMONS state = Constants.STATUS_COMMONS.IGNORE;
	//		String cmd = "ipmitool -H "+host.getIpmiip()+" -I lanplus -U "+host.getIpmiusername()+" -P "+host.getIpmipassword()+"  power status";
	//		try{
	//			logger.debug(cmd);
	//			Process proc = runTime.exec(cmd);
	//			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	//			String line;
	//			if ((line = br.readLine()) != null) {
	//				if(line.indexOf("on") > 0){
	//					state = Constants.STATUS_COMMONS.ON;
	//				}
	//				if(line.indexOf("off") > 0){
	//					state = Constants.STATUS_COMMONS.OFF;
	//				}
	//			}
	//			br.close();
	//			return state;
	//		}catch (Exception e) {
	//			logger.error("Get host power state error! "+e.getMessage());
	//			state =  Constants.STATUS_COMMONS.IGNORE;//状态判断异常错误
	//			return state;
	//		}
	//	}

	/**
	 * 效验物理机Host(主机)信息：执行命令参数是否已配置完整
	 * @param host
	 * @return
	 */
	//	private String checkHostInfo(PhysicalHostVO host){
	//
	//		//判断host是否为空
	//		if(host == null || host.getId() == null){
	//			logger.error("Start host Error! host is null or host id is null . ");
	//			return "物理机主机配置信息为空或者不存在！";
	//		}
	//		//判断Ipmiip是否为空
	//		if(host.getIpmiip() == null){
	//			logger.error("Start host Error! host 's Ipmiip is null . ");
	//			return "物理机主机配置信息中IPMI的IP地址为空或者不存在！";
	//		}
	//		//判断Iipmiusername是否为空
	//		if(host.getIpmiusername() == null){
	//			logger.error("Start host Error! host 's Iipmiusername is null . ");
	//			return "物理机主机配置信息中IPMI的用户为空或者不存在！";
	//		}
	//		//判断Ipmipassword是否为空
	//		if(host.getIpmipassword() == null){
	//			logger.error("Start host Error! host 's Ipmipassword is null . ");
	//			return "物理机主机配置信息中IPMI的密码为空或者不存在！";
	//		}
	//
	//		return null;
	//	}

	/**
	 * 物理机退订申请
	 * @param vo
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public TInstanceInfoBO applyPhyDestroy(ResourcesModifyVO vo, TUserBO user) throws SCSException
	{
		TInstanceInfoBO info = updateInstanceInfoState3(vo);

		//服务实例的申请退订状态处理
		this.updateServiceInfoState(vo.getIsoId());

		//保存退订订单
		if (info != null) {
			TOrderBO order = new TOrderBO();
			order.setType(3);// 作废申请
			order.setState(1);// 申请状态
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			order.setCreatorUserId(user.getId());// 下单人ID
			//order.setInstanceInfoId(vo.getId());
			order.setServiceInstanceId(vo.getIsoId());
			order.setZoneId(info.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			// 生成 订单编码 ： 日期+6位随机数
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			order.setOrderCode(sdf.format(new java.util.Date())
			                   + (int) ((Math.random() * 9 + 1) * 100000));
			order.setReason(vo.getApply_reason());//申请退订原因
			int saveOrderId = orderDao.save(order);

			if (saveOrderId > 0) {
				info.setOrderId(saveOrderId);
			}
			//根据用户审批级别，判断并进入自动审批
			//fix bug 5074 物理机退订，不生成退订单，原因为：未判断是否自动审批
	        if (user.getRoleApproveLevel() == 4) {
	            try {
	            	IAuditSevice auditService = (IAuditSevice) BeanFactoryUtil.getBean("auditService");
	            	auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 3,
	                  info.getTemplateType());
	            } catch (SQLException e) {
	              log.error("approveOrder error:" + e.getMessage());
	              throw new SCSException("approveOrder error:" + e.getMessage());
	            }
	          } else if (user.getRoleApproveLevel() < 4) {
	            try {
	            	IAuditSevice auditService = (IAuditSevice) BeanFactoryUtil.getBean("auditService");
	            	auditService.isAutoApproveUser(user.getId()
	            			, saveOrderId
	            			, user.getRoleApproveLevel() + 1
	            			, 0
	            			, "自动审批"
	            			, user.getEmail()
	            			, 3
	            			,info.getTemplateType()
	            		);
	            } catch (SQLException e) {
	              log.error("approveOrder error:" + e.getMessage());
	              throw new SCSException("approveOrder error:" + e.getMessage());
	            }
	          }
		}
		return info;
	}

	//	public TInstanceInfoBO updateInstanceInfoState(int id)
	//			throws SCSException {
	//		int index = 0;
	//		TInstanceInfoBO info = null;
	//		if (id != 0) {
	//			info = instanceInfoDao.searchInstanceInfoByID(id);
	//			if (info.getState() == 2) {//2：可用状态
	//				info.setClusterId(info.getState());
	//				info.setState(3);//3：正在处理状态
	//				index = instanceInfoDao.update(info);
	//				if (index == 0) {
	//					return null;
	//				}
	//			} else {
	//				return null;
	//			}
	//		}
	//		return info;
	//	}

	public TInstanceInfoBO updateInstanceInfoState3(ResourcesModifyVO vmModifyVO)
	throws SCSException {
		int index = 0;
		int id = vmModifyVO.getId();
		TInstanceInfoBO info = null;
		if (id != 0) {
			info = instanceInfoDao.searchInstanceInfoByID(id);
			if (info.getState() == 6) {
				return null;
			} else {
				info.setClusterId(info.getState());
				info.setState(3);
				index = instanceInfoDao.update(info);
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	}

	//	private TInstanceInfoBO updateInstanceInfoState(ResourcesModifyVO vmModifyVO)
	//			throws Exception {
	//		int index = 0;
	//		int id = vmModifyVO.getId();
	//		TInstanceInfoBO info = null;
	//		if (id != 0) {
	//			info = instanceInfoDao.searchInstanceInfoByID(id);
	//			if (info.getState() == 6) {
	//				return null;
	//			} else {
	//				info.setClusterId(info.getState());
	//				info.setState(6);
	//				index = instanceInfoDao.update(info);
	//				if (index == 0) {
	//					return null;
	//				}
	//			}
	//		}
	//		return info;
	//	}

	public TServiceInstanceBO updateServiceInfoState(int serviceID) throws SCSException {
		int index = 0;
		int id = serviceID;
		TServiceInstanceBO info = null;
		if (id != 0) {
			try {
				info = instanceInfoDao.findServiceInstanceById(serviceID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (info.getState() == 6) {
				return null;
			} else {
				index = instanceInfoDao.updateServiceState(serviceID);
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	}



	/**=================== 物理机，多资源池 ，操作处理 ninghao 2012-12-10 =======================*/

	private IRestfulService restfulService;

	public IRestfulService getRestfulService() {
		return restfulService;
	}

	public void setRestfulService(IRestfulService restfulService) {
		this.restfulService = restfulService;
	}

	/**
	 * 查询可用的物理机
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return List
	 */
	@Override
	public List<PhysicalHostVO> findPhysicalHostByREST(ListPhysicalHost listVO , int resourcePoolsId) throws SCSException
	{
		List<PhysicalHostVO> host_list = new ArrayList<PhysicalHostVO>();
		try {
			logger.info(" findPhysicalHostByREST ", listVO.toString());
			Object response = restfulService.executeAndJsonReturn(listVO, resourcePoolsId);

			if(response != null){
				//				if(response.toString().indexOf("Error") >= 0 || response.toString().indexOf("html") >= 0){
				//					return host_list;
				//				}
				//			JSONObject jsonRes = JSONObject.fromObject(response);
				JSONArray hostArray = JSONArray.fromObject(response);
				//			String result = jsonRes.getString("hosts");
				//			JSONObject resultObject = JSONObject.fromObject(result);
				//			if (resultObject.containsKey("host")) {
				//				JSONArray hostArray = resultObject.getJSONArray("host");
				//将字符串转为对象BO
				PhysicalHostVO[] hostVOs = (PhysicalHostVO[])JSONArray.toArray(hostArray, PhysicalHostVO.class);
				//将数组转为List，并赋部门id值
				if(hostVOs != null && hostVOs.length > 0){
					for(int i=0;i<hostVOs.length;i++){
						host_list.add(hostVOs[i]);
					}
				}
				//			} else {
				//				logger.error("没有找到相对应的物理机信息！");
				//				throw new Exception("没有找到相对应的物理机信息！");
				//			}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error:" + e.getMessage());
		}

		return host_list;
	}

	/**
	 * 查询可用的物理机规格信息
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	@Override
	public List<PhyHardwareVO> findPhysicalHostHardwaresByREST(ListPhysicalHostHardware listVO , int resourcePoolsId)
	throws SCSException{

		List<PhyHardwareVO> hardware_list = new ArrayList<PhyHardwareVO>();
		try {
			logger.info(" findPhysicalHostHardwaresByREST ", listVO.toString());
			Object response = restfulService.executeAndJsonReturn(listVO, resourcePoolsId);

			if(response != null){
				JSONArray hardwareArray = JSONArray.fromObject(response);
				//将字符串转为对象BO
				PhyHardwareVO[] hardwareVOs = (PhyHardwareVO[])JSONArray.toArray(hardwareArray, PhyHardwareVO.class);
				//将数组转为List，并赋部门id值
				if(hardwareVOs != null && hardwareVOs.length > 0){
					for(int i=0;i<hardwareVOs.length;i++){
						hardware_list.add(hardwareVOs[i]);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error:" + e.getMessage());
		}

		return hardware_list;
	}


	/**
	 * 查询物理机的操作系统osType
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return List
	 */
	@Override
	public List<PhysicalOsTypeVO> findPhysicalHostOsTypeByREST(ListPhysicalOS listOSVO , int resourcePoolsId)
	throws SCSException
	{
		List<PhysicalOsTypeVO> os_list = new ArrayList<PhysicalOsTypeVO>();
		try {
			logger.info(" findPhysicalHostOsTypeByREST ", listOSVO.toString());
			Object response = restfulService.executeAndJsonReturn(listOSVO, resourcePoolsId);
			if(response != null){
				if(response.toString().indexOf("Error") >= 0 || response.toString().indexOf("html") >= 0){
					return os_list;
				}
				JSONArray hostArray = JSONArray.fromObject(response);
				//			JSONObject jsonRes = JSONObject.fromObject(response);
				//			String result = jsonRes.getString("oSTypes");
				//			JSONObject resultObject = JSONObject.fromObject(result);
				//			if (resultObject.containsKey("osType")) {
				//				JSONArray hostArray = resultObject.getJSONArray("osType");
				//将字符串转为对象BO
				PhysicalOsTypeVO[] hostVOs = (PhysicalOsTypeVO[])JSONArray.toArray(hostArray, PhysicalOsTypeVO.class);
				//将数组转为List，并赋部门id值
				if(hostVOs != null && hostVOs.length > 0){
					for(int i=0;i<hostVOs.length;i++){
						os_list.add(hostVOs[i]);
					}
				}
				//			} else {
				//				logger.error("没有找到相对应的物理机操作系统信息！");
				//				throw new Exception("没有找到相对应的物理机操作系统信息！");
				//			}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error:" + e.getMessage());
		}

		return os_list;
	}

	/**
	 * 查询物理机的资源域
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	@Override
	public List<ListPhysicalZone> findPhysicalHostZoneByREST(ListPhysicalZone listVO , int resourcePoolsId)
	throws SCSException{
		List<ListPhysicalZone> zone_list = new ArrayList<ListPhysicalZone>();
		try {
			logger.info(" findPhysicalHostZoneByREST ", listVO.toString());
			Object response = restfulService.executeAndJsonReturn(listVO, resourcePoolsId);

			if(response != null){
				if(response.toString().indexOf("Error") >= 0 || response.toString().indexOf("html") >= 0){
					return zone_list;
				}
				JSONArray hostArray = JSONArray.fromObject(response);
				//将字符串转为对象BO
				ListPhysicalZone[] resultArray = (ListPhysicalZone[])JSONArray.toArray(hostArray, ListPhysicalZone.class);
				//将数组转为List，并赋部门id值
				if(resultArray != null && resultArray.length > 0){
					for(int i=0;i<resultArray.length;i++){
						zone_list.add(resultArray[i]);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error:" + e.getMessage());
		}

		return zone_list;
	}

	/**
	 * 查询资源域下的VLAN信息
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	@Override
	public List<ListPhysicalVlan> findPhysicalHostVlanByREST(ListPhysicalVlan listVO , int resourcePoolsId)
	throws SCSException{
		List<ListPhysicalVlan> vlan_list = new ArrayList<ListPhysicalVlan>();
		try {
			logger.info(" findPhysicalHostVlanByREST ", listVO.toString());
			Object response = restfulService.executeAndJsonReturn(listVO, resourcePoolsId);
			if(response != null){
				if(response.toString().indexOf("Error") >= 0 || response.toString().indexOf("html") >= 0){
					return vlan_list;
				}
				JSONArray hostArray = JSONArray.fromObject(response);
				//将字符串转为对象BO
				ListPhysicalVlan[] resultArray = (ListPhysicalVlan[])JSONArray.toArray(hostArray, ListPhysicalVlan.class);
				//将数组转为List，并赋部门id值
				if(resultArray != null && resultArray.length > 0){
					for(int i=0;i<resultArray.length;i++){
						vlan_list.add(resultArray[i]);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error:" + e.getMessage());
		}

		return vlan_list;
	}

	/**
	 * 查询资源域下的VLAN下的IP地址信息
	 * REST命令方式
	 * @param listVO
	 * @param resourcePoolsId
	 * @return String
	 */
	@Override
	public List<ListPhysicalVlanIP> findPhysicalHostVlanIpByREST(ListPhysicalVlanIP listVO , int resourcePoolsId)
	throws SCSException{
		List<ListPhysicalVlanIP> ip_list = new ArrayList<ListPhysicalVlanIP>();
		try {
			logger.info(" findPhysicalHostVlanIpByREST ", listVO.toString());
			Object response = restfulService.executeAndJsonReturn(listVO, resourcePoolsId);
			if(response != null){
				if(response.toString().indexOf("Error") >= 0 || response.toString().indexOf("html") >= 0){
					return ip_list;
				}
				JSONArray hostArray = JSONArray.fromObject(response);
				//将字符串转为对象BO
				ListPhysicalVlanIP[] resultArray = (ListPhysicalVlanIP[])JSONArray.toArray(hostArray, ListPhysicalVlanIP.class);
				//将数组转为List，并赋部门id值
				if(resultArray != null && resultArray.length > 0){
					for(int i=0;i<resultArray.length;i++){
						ip_list.add(resultArray[i]);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error:" + e.getMessage());
		}

		return ip_list;
	}

	/**
	 * 特殊模板申请创建申请模板
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	@Override
	public TTemplateVMBO creatSpecalPhysicalTemplate(TVmInfo vminfo,TUserBO user,TTemplateVMBO template,Product product) throws SCSException
	{
//		long hostId =0;
		int resourcePoolsId=vminfo.getPoolId();

		if(template == null) {
			template = new TTemplateVMBO();
		}

		//根据物理机ID，资源池ID，获取物理机的规格信息
//		ListPhysicalHost listVO = new ListPhysicalHost();
//		listVO.setId(hostId);
//		//		listVO.setState("Active");
//		PhysicalHostVO host = null;
//		List<PhysicalHostVO> hostlist = findPhysicalHostByREST(listVO, resourcePoolsId);
//		if(hostlist !=null && hostlist.size() == 1){
//			host = hostlist.get(0);
//		}else{
//			//			throw new SCSException("物理机已被占用，不能申请！");
//		}
//
//		//根据操作系统ID，资源池ID，获取OS信息
		ListPhysicalOS listOSVO = new ListPhysicalOS();
		int osId = vminfo.getOsId();
		if(osId > 0){
			listOSVO.setId(osId);
		}
		List<PhysicalOsTypeVO> oslist = findPhysicalHostOsTypeByREST(listOSVO, resourcePoolsId);
		PhysicalOsTypeVO osVO = null;
		if(oslist !=null && oslist.size() == 1){
			osVO = oslist.get(0);
		}

		//根据物理机,操作系统信息组织要创建模板信息
//		if(host != null && osVO != null){
		if(vminfo != null && template != null && product != null){
			template.setCode(null);//模板编码
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);//特殊模板 1：管理员定义的模板，2：用户定义的特殊模板
//			template.setCpufrequency(host.getCpuspeed());
//			template.setCpuNum(host.getCpunumber().intValue());
//			template.setMemorySize(host.getMemory().intValue());
//			template.setStorageSize((host.getDisknumber().intValue()*host.getDisksize().intValue()));
//			template.setVethAdaptorNum(1);//网卡个数
			template.setCpuNum(vminfo.getCpuNum());
			template.setCpufrequency(vminfo.getCpufrequency());
			template.setMemorySize(vminfo.getMemorySize());
			template.setStorageSize(vminfo.getDisknumber()*vminfo.getStorageSize());
			template.setVethAdaptorNum(vminfo.getVethAdaptorNum());//网卡个数
//			vminfo.setDisknumber(host.getDisknumber().intValue());
//			vminfo.setStorageSize(host.getDisksize().intValue());
			if(user != null){
				template.setCreatorUserId(user.getId());
			}
//			if(host.getZoneid() != null){
//				template.setZoneId(host.getZoneid().intValue());
//			}else if(vminfo.getZoneId() > 0){
//				template.setZoneId(vminfo.getZoneId());
//			}
			if(vminfo.getZoneId() > 0){
				template.setZoneId(vminfo.getZoneId());
			}
			if(vminfo.getOsId() > 0){
				template.seteOsId(vminfo.getOsId());
			}
			template.setResourcePoolsId(resourcePoolsId);
			template.setType(vminfo.getTemplateType());
			template.setOperType(1);
			Timestamp ts = new Timestamp(new Date().getTime());
			template.setCreateTime(ts.toString());
			template.setMeasureMode("Duration");//Duration：按时长计量
			template.setOsDesc(osVO.getName());
			//to fix bug:6737 物理机模板创建及可用，无需后台job
			template.setState(ConstDef.STATE_TWO);//hfk 13-2-6
//			template.seteOsId(vminfo.getOsId());
			//根据物理机创建模板,操作系统信息组织要创建服务信息
			product.setName("PHY");
			product.setCreateDate(ts);
			product.setType(template.getType());
			product.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
			product.setDescription("物理机自动创建服务");
			product.setSpecification(product.getDescription());
			product.setQuotaNum(1);//待确定 TODO
			product.setPrice(0f);
			product.setUnit(vminfo.getUnit());
			product.setOperateType(1);
			product.setIsDefault(0);//是否首页推荐，0否，1是
		}

		return template;
	}

	/**
	 * 开机
	 * CMD命令方式
	 * @param pmVO
	 * @return String
	 */
	@Override
	public String startPhysicalHost(int infoId , int hostId) throws SCSException
	{
		try {
			logger.info(" startPhysicalHost id = {} ", hostId);
			//查询资源池
			int resourcePoolsId = this.instanceInfoDao.getResourcePoolIdByInstanceId(infoId);
			if(resourcePoolsId <= 0){
				throw new SCSException("error","物理机资源实例ID="+infoId+"的资源池不存在！");
			}
			HostPowerState state = this.getPhysicalHostPowerStatus(infoId, hostId);
			//只判断开关机状态
			if(state == HostPowerState.OFF){
				//组装参数
				PhysicalStartHost requestVO = new PhysicalStartHost();
				requestVO.setId(hostId);
				//执行RESTFUL接口
				Object response = restfulService.executeAndJsonReturn(requestVO, resourcePoolsId);
				if(response != null){
					JSONObject jsonObject = JSONObject.fromObject(response);

					PhysicalHostVO resultHost = (PhysicalHostVO)JSONObject.toBean(jsonObject, PhysicalHostVO.class);

					//					if(response.toString().indexOf("errorcode") >= 0 && response.toString().indexOf("errordesc") >= 0){
					if(resultHost== null || "false".equals(resultHost.getReqstate())){
						throw new SCSException("error","物理机启动失败！");//资源实例【ID="+infoId+"】的，错误编号为
					}else{
						//						PhysicalStartHost resultHost = (PhysicalStartHost)JSONObject.toBean(jsonObject, PhysicalStartHost.class);
						if(resultHost != null){
							if(hostId == resultHost.getId()){
								return "操作成功!";
							}else{
								return "启动失败!";
							}
						}
					}
				}
			}
			else if(state == HostPowerState.ON){
				return "物理机已是运行状态！" ;
			}else{
				return "不能获取物理机状态，请联系管理员！" ;
			}


			//			JSONObject jsonRes = JSONObject.fromObject(response);
			//			String result = jsonRes.getString("host");
			//			JSONObject resultObject = JSONObject.fromObject(result);
			//			if (resultObject.containsKey("id")) {
			//				String resultId = resultObject.getString("id");
			//				String resultStr = resultObject.getString("result");
			//			} else {
			//				logger.error("启动物理机出错，请检查物理机是否正常运行！");
			//				throw new Exception("启动物理机出错，请检查物理机是否正常运行！");
			//			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error","error:" + e.getMessage());
		}
		return "操作成功!";
	}

	/**
	 * 关机
	 * CMD命令方式
	 * @param bo
	 * @return String
	 */
	@Override
	public String stopPhysicalHost(int infoId , int hostId) throws SCSException
	{
		try {
			logger.info(" stopPhysicalHost id = {} ", hostId);
			//查询资源池
			int resourcePoolsId = this.instanceInfoDao.getResourcePoolIdByInstanceId(infoId);
			if(resourcePoolsId <= 0){
				throw new SCSException("error","物理机资源实例ID="+infoId+"的资源池不存在！");
			}
			HostPowerState state = this.getPhysicalHostPowerStatus(infoId, hostId);
			//只判断开关机状态
			if(state == HostPowerState.OFF){
				return "物理机已是停止状态！" ;
			}
			else if(state == HostPowerState.ON){
				//组装参数
				PhysicalStopHost requestVO = new PhysicalStopHost();
				requestVO.setId(hostId);
				//执行RESTFUL接口
				Object response = restfulService.executeAndJsonReturn(requestVO, resourcePoolsId);
				if(response != null){
					JSONObject jsonObject = JSONObject.fromObject(response);

					PhysicalHostVO resultHost = (PhysicalHostVO)JSONObject.toBean(jsonObject, PhysicalHostVO.class);

					//					if(response.toString().indexOf("errorcode") >= 0 && response.toString().indexOf("errordesc") >= 0){
					if(resultHost== null || "false".equals(resultHost.getReqstate())){
						throw new SCSException("error","物理机停止失败！");//资源实例【ID="+infoId+"】的，错误编号为
					}
				}
			}
			else{//应该没有其他状态
				return "执行重启操作失败：物理机状态异常，请检查物理机是否故障！" ;
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error","error:" + e.getMessage());
		}
		return "操作成功!";
	}

	/**
	 * 重启
	 * CMD命令方式
	 * @param pmVO
	 * @return String
	 */
	@Override
	public String resetPhysicalHost(int infoId , int hostId) throws SCSException
	{
		try {
			logger.info(" resetPhysicalHost id = {} ", hostId);
			//查询资源池
			int resourcePoolsId = this.instanceInfoDao.getResourcePoolIdByInstanceId(infoId);
			if(resourcePoolsId <= 0){
				throw new SCSException("error","物理机资源实例ID="+infoId+"的资源池不存在！");
			}
			HostPowerState state = this.getPhysicalHostPowerStatus(infoId, hostId);
			//只判断开关机状态
			if(state == HostPowerState.NONE){//得不到状态
				throw new SCSException("error","物理机状态获取错误！");
			}
			else if(state == HostPowerState.OFF){//关机，启动
				this.startPhysicalHost(infoId, hostId);
			}
			else if(state == HostPowerState.ON){//运行，重启
				//组装参数
				PhysicalResetHost requestVO = new PhysicalResetHost();
				requestVO.setId(hostId);
				//执行RESTFUL接口
				Object response = restfulService.executeAndJsonReturn(requestVO, resourcePoolsId);
				if(response != null){
					JSONObject jsonObject = JSONObject.fromObject(response);

					PhysicalHostVO resultHost = (PhysicalHostVO)JSONObject.toBean(jsonObject, PhysicalHostVO.class);

					//					if(response.toString().indexOf("errorcode") >= 0 && response.toString().indexOf("errordesc") >= 0){
					if(resultHost== null || "false".equals(resultHost.getReqstate())){
						throw new SCSException("error","物理机重启失败！");//资源实例【ID="+infoId+"】的，错误编号为
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error","error:" + e.getMessage());
		}
		return "操作成功!";
	}

	/**
	 * 查询物理机当前状态
	 * CMD命令方式
	 * @param bo
	 * @return
	 */
	@Override
	public HostPowerState getPhysicalHostPowerStatus(int infoId , int hostId) throws SCSException
	{
		HostPowerState state = HostPowerState.NONE;
		try {
			logger.info(" getPhysicalHostPowerStatus id = {} ", hostId);
			//查询资源池
			int resourcePoolsId = this.instanceInfoDao.getResourcePoolIdByInstanceId(infoId);
			if(resourcePoolsId <= 0){
				throw new SCSException("error","物理机资源实例ID="+infoId+"的资源池不存在！");
			}
			//组装参数
			PhysicalGetHostPowerState requestVO = new PhysicalGetHostPowerState();
			requestVO.setId(hostId);
			//执行RESTFUL接口
			Object response = restfulService.executeAndJsonReturn(requestVO, resourcePoolsId);
			if(response != null){
				JSONObject jsonObject = JSONObject.fromObject(response);

				PhysicalHostVO resultHost = (PhysicalHostVO)JSONObject.toBean(jsonObject, PhysicalHostVO.class);

				//				if(response.toString().indexOf("errorcode") >= 0 && response.toString().indexOf("errordesc") >= 0){
				if(resultHost== null || "false".equals(resultHost.getReqstate())){
					state = HostPowerState.NONE;
					//					throw new SCSException("error","物理机状态查询失败！");//资源实例【ID="+infoId+"】的，错误编号为
				}else{
					if(HostPowerState.ON.getName().equals(resultHost.getPowerstate())){
						state = HostPowerState.ON;
					} else if (HostPowerState.OFF.getName().equals(resultHost.getPowerstate())){
						state = HostPowerState.OFF;
					}else{
						state = HostPowerState.NONE;
					}
				}
			}
		}
		catch (Exception e) {
			state = HostPowerState.NONE;
			logger.error("error:" + e.getMessage());
			throw new SCSException("error","error:" + e.getMessage());
		}
		return state;
	}


	/**
	 * 物理机退订申请
	 * @param vo
	 * @param user
	 * @return
	 * @throws SCSException
	 * @throws Exception
	 */
	@Override
	public String destroyPhysicalHost(int infoId , int hostId) throws SCSException
	{
		try {
			logger.info(" getPhysicalHostPowerStatus id = {} ", hostId);
			//查询资源池
			int resourcePoolsId = this.instanceInfoDao.getResourcePoolIdByInstanceId(infoId);
			if(resourcePoolsId <= 0){
				throw new SCSException("物理机资源实例ID="+infoId+"的资源池不存在！");
			}
			//组装参数
			PhysicalDeleteHost requestVO = new PhysicalDeleteHost();
			requestVO.setId(hostId);
			//执行RESTFUL接口
			Object response = restfulService.executeAndJsonReturn(requestVO, resourcePoolsId);
			if(response != null){
				if(response.toString().indexOf("errorcode") >= 0 && response.toString().indexOf("errordesc") >= 0){
					throw new SCSException("error","物理机退订失败！");//资源实例【ID="+infoId+"】的，错误编号为
				}
			}
		}
		catch (Exception e) {
			logger.error("error:" + e.getMessage());
			throw new SCSException("error","error:" + e.getMessage());
		}
		return "操作成功!";
	}

	@Override
    public int CheckPhysicalHost(CheckPhysicalHost checkPhysicalHost, Integer resourcePoolId)
            throws Exception {
		int total = 0;
		try{
			//执行RESTFUL接口
			Object response = restfulService.executeAndJsonReturn(checkPhysicalHost, resourcePoolId);
			if(response != null){
				if(response.toString().indexOf("Error") >= 0 || response.toString().indexOf("html") >= 0){
					return -1;
				}
				JSONObject jsonObject = JSONObject.fromObject(response);
				CheckPhysicalHost resultHost = (CheckPhysicalHost)JSONObject.toBean(jsonObject, CheckPhysicalHost.class);
	            //调用接口成功
				if (resultHost != null && "true".equals(resultHost.getReqstate())){
					total = resultHost.getTotal();
				}else{
					total = -1;
				}
			}
		}
		catch (Exception e) {
			total = -2;
			logger.error("error:" + e.getMessage());
			throw new SCSException("error","error:" + e.getMessage());
		}
		return total;
    }

	

}
