package com.skycloud.management.portal.front.instance.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.DeleteVM;
import com.skycloud.management.portal.front.instance.dao.IInstancePeriodManageDao;
import com.skycloud.management.portal.front.instance.entity.TInstancePeriodInfo;
import com.skycloud.management.portal.front.instance.entity.TServicePeriodInfo;
import com.skycloud.management.portal.front.instance.service.IInstancePeriodManageService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.instance.service.IJobInstanceInfoService;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IJobInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.service.BandWidthInstanceOperateService;
import com.skycloud.management.portal.front.resources.service.IPublicIPInstanceService;
import com.skycloud.management.portal.front.resources.service.VirtualMachineModifyService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;
import com.skycloud.management.portal.front.sg.entity.SGRule;
import com.skycloud.management.portal.front.sg.service.ISGRuleService;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

public class InstancePeriodManageServiceImpl implements IInstancePeriodManageService{
	private static Log log = LogFactory.getLog(InstancePeriodManageServiceImpl.class);

	private IInstancePeriodManageDao  instancePeriodManageDao;
	private IJobInstanceInfoDao jobInstanceDao;
	private IJobInstanceInfoService jobInstanceService;
	private VirtualMachineModifyService vmModifyService;
	private IPublicIPInstanceService publicIPService;
	private AsyncJobVDCService asyncJobVDCService;
	private BandWidthInstanceOperateService bandWidthService;
	private ISGRuleService SGRuleService;
	private IInstanceService instanceService;
	private IInstanceInfoDao instanceInfoDao;

	private IOrderDao orderDao;
	private IAuditSevice auditService;
	private IServiceInstanceService serviceInstanceService;
	//4：已回收
	private final static int STATE_RELEASE = 4;

	@Override
	public TInstancePeriodInfo findInstancePeriodById(int id) throws SQLException {
		TInstancePeriodInfo inPeriodInfo=instancePeriodManageDao.findInstancePeriodById(id);
		if(inPeriodInfo!=null){
			JSONObject jsonObject = JSONObject.fromObject(inPeriodInfo.getResourceInfo());
			if (jsonObject.containsKey("period")) {
				inPeriodInfo.setBuyPeriod(replaceYMD(jsonObject.getString("period")));
			}
			inPeriodInfo.setUnitString(getUnitName((inPeriodInfo.getUnit()+"").trim().toCharArray()[0]));
		}

		return inPeriodInfo;
	}

	@Override
	public TServicePeriodInfo findServiceInstancePeriodById(int id) throws SQLException {
		TServicePeriodInfo servicePeriodInfo=instancePeriodManageDao.findServiceInstancePeriodById(id);
		if(servicePeriodInfo!=null){
			servicePeriodInfo.setBuyPeriod(servicePeriodInfo.getPeriod()+replaceYMD(servicePeriodInfo.getUnit()));
			servicePeriodInfo.setUnitString(getUnitName((servicePeriodInfo.getUnit()+"").trim().toCharArray()[0]));
		}

		return servicePeriodInfo;
	}

	//产品续订
	@Override
	public int updateInstancePeriod(int num,String unit,String resourceInfo, Date expireDate,
			int instanceId) throws SQLException {
		int index=0;
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		if (jsonObject.containsKey("period")) {
			String period=jsonObject.getString("period");
			if(period!=null){
				char unitchar=period.trim().charAt(period.length()-1);
				int numchar=Integer.valueOf(period.trim().substring(0, period.length()-1));
//				int addNum=Math.round(getNumByUnit_D(unit.trim().charAt(0),num));
//				int allNum=Math.round(getNumByUnit_D(unitchar,numchar)+addNum);
//				long newNum=getNewUnit(unit,allNum);
				int newNum = numchar + num;
				jsonObject.remove("period");
				jsonObject.put("period", newNum+unit);
				resourceInfo=jsonObject.toString();
				//bugid=0001709 产品续订之后，已购买周期变为0，到期时间无变化
				expireDate = this.getExpireDate(num, unit, expireDate);
//				Calendar calendar=Calendar.getInstance();
//				calendar.setTime(expireDate);
//				calendar.add(Calendar.DAY_OF_YEAR, addNum);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				try {
//					expireDate = sdf.parse(sdf.format(calendar.getTime()));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
				index=instancePeriodManageDao.updateInstancePeriod(resourceInfo, expireDate, instanceId);
				if(index>0){//message 表中改状态
					instancePeriodManageDao.updateMessageState(instanceId);
				}
			}
		}
		return index;
	}

	@Override
    public int updateServicePeriod(int num,String unit,String periods, Date expireDate, int instanceId, TUserBO user) throws SQLException {
//		int index=0;

//			String period = periods;
//			if(period!=null){
//				expireDate = this.getExpireDate(num, unit, expireDate);
//				index=instancePeriodManageDao.updateServicePeriod(Integer.parseInt(period)+num, expireDate, instanceId);
//				if(index>0){
//					instancePeriodManageDao.updateMessageState(instanceId);
//				}
//			}
//			return index;
		int serviceId = instanceId;
		//获取服务实例
		TServiceInstanceBO serviceInstance =  serviceInstanceService.searchById(serviceId);

		Map<String, Object> mapRes = new HashMap<String, Object>();
		mapRes.put("periods", num);//续费周期
		mapRes.put("unit", String.valueOf(unit));//续费单位
		String resourceInfo = JsonUtil.getJsonString4JavaPOJO(mapRes);
		    int typeRenew = 4;//订单类型：4，续费
		    //获取过期时间
			TOrderBO order = new TOrderBO();
			order.setType(typeRenew);//续费申请
			order.setState(user.getRoleApproveLevel());// 申请状态与当前用户级别相等
			order.setCreatorUserId(user.getId());// 下单人ID
			order.setServiceInstanceId(serviceId);//服务ID
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
			order.setResourceInfo(resourceInfo);//续费参数json串
			order.setReason("服务续订,续费周期="+num+",续费单位="+unit);//订单产生原因
			order.setStorageSize(num);//续费周期

			int saveOrderId = 0;
            try {
            	//1.保存续订订单
	            saveOrderId = orderDao.save(order);
//	          //2.修改服务实例状态为：8:申请续订
//	            int state = 8;//申请续订
//	            serviceInstanceService.updateServiceStateByServiceId(state, serviceId);

	            List<TInstanceInfoBO>  infos = instanceInfoDao.searchInstanceInfoByServiceInstanceId(serviceId);
	            TInstanceInfoBO info = null;
	            if(infos!=null && infos.size()>0){
	            	for (TInstanceInfoBO  insInfo: infos){
	            		//3.修改续费通知邮件状态，不再催费
		            	instancePeriodManageDao.updateMessageState(insInfo.getId());
		            }
	            	info = infos.get(0);
	            	//4.自动审核续订订单,更新订单状态和最后修改时间
	            	approveOrder(user, info, saveOrderId);//自动审核
	            }
            }
            catch (SCSException e) {
	            e.printStackTrace();
	            log.error("updateServicePeriod error:" + e.getMessage());
				throw new SQLException("updateServicePeriod error:" + e.getMessage());
            }
            return saveOrderId;
	}

	private void approveOrder(TUserBO user, TInstanceInfoBO info, int saveOrderId) throws SCSException {
		if (saveOrderId > 0) {
			if (user.getRoleApproveLevel() == 4) {
				try {
					auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 4,
					                          info.getTemplateType());
				}
				catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:" + e.getMessage());
				}
			} else if (user.getRoleApproveLevel() < 4) {
				try {
					auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 4,
					                               info.getTemplateType());
				}
				catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:" + e.getMessage());
				}
			}
		}
	}

	 //获取到期日期(hefk 2012-05-10 add) //bugid=0001709 产品续订之后，已购买周期变为0，到期时间无变化
	private static Date getExpireDate(int period,String unit,Date expireDate){
		if (period<=0 || unit==null || unit.equals("")){
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expireDate);
		int day = 0;
        if (unit.equals("Y")){
        	day = calendar.get(Calendar.YEAR);
        	calendar.set(Calendar.YEAR, day + period);
        }else if (unit.equals("M")){
        	day = calendar.get(Calendar.MONTH);
        	calendar.set(Calendar.MONTH, day + period);
        }else if (unit.equals("W")){
        	day = calendar.get(Calendar.WEEK_OF_YEAR);
        	calendar.set(Calendar.WEEK_OF_YEAR, day + period);
        }else if (unit.equals("D")){
        	day = calendar.get(Calendar.DAY_OF_YEAR);
        	calendar.set(Calendar.DAY_OF_YEAR, day + period);
        }else if (unit.equals("H")){
        	day = calendar.get(Calendar.HOUR_OF_DAY);
        	calendar.set(Calendar.HOUR_OF_DAY, day + period);
        }else{
        	return null;
        }
		return calendar.getTime();
	}
	/**
	 * 产品退订
	 * @see com.skycloud.management.portal.front.instance.service.IInstancePeriodManageService#destroyExpireProduct()
	 * 创建人：   张爽
	 * 创建时间：2012-4-18  下午02:21:47
	 */
	@Override
	public int destroyExpireProduct() throws Exception {
		try {
			List<TInstanceInfoBO> instanceList=instancePeriodManageDao.findExpireProduct();
			for (TInstanceInfoBO infoBO : instanceList) {
				//下面的接口
				int type = infoBO.getTemplateType();
				switch (type) {
				case 1:
					if(infoBO.getStorageSize()<=0){
						this.insertVMAapplyDestroy(infoBO);
					}else{
						this.insertVolumeDestroy(infoBO);
					}
				case 2:
					break;
				case 3:
					break;
				case 4:// 虚拟机备份
					this.deleteBakUpInstance(infoBO);
					break;
				case 5:// 监控服务
					this.deleMonitorInstance(infoBO);
					break;
				case 6:// 负载均衡服务
					break;
				case 7:// 安全组资源服务
					this.deleteFwInstance(infoBO);
					break;
				case 8:// 带宽
					this.deleteBandWidthInstance(infoBO);
					break;
				case 9:// 公网IP
					publicIPService.writeBackForReleasePublicIP(infoBO.getId(),
							STATE_RELEASE);
					break;
				case 10:// 物理机
					break;
				case 15:// 数据云备份
					this.deleteDataBak(infoBO);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 产品过期
	 *  创建人：   CQ  //fix bug 4561
	 */
	@Override
	public int disabledExpireProduct()  {
		log.info("-------this is disabledExpireProduct----------------------");
		try{
			List<TInstanceInfoBO> instanceList=instancePeriodManageDao.findExpireProduct();	
			if(null!=instanceList&&!instanceList.isEmpty()){
				for (TInstanceInfoBO infoBO : instanceList) {
					log.info("-----------------------------infoBO:"+infoBO.getId()+"-----"+infoBO.getInstanceName());
					System.out.println(infoBO.getTemplateType()+"-------infoBO----:"+infoBO.getId()+"-----"+infoBO.getInstanceName());
					int type = infoBO.getTemplateType();
					switch (type) {
					case 1://虚拟机到期停机,状态	
						if(infoBO.geteInstanceId()>0){
							jobInstanceService.disabledVM(infoBO);
						}						
						break;
					case 2:
						if(infoBO.geteInstanceId()>0){
							jobInstanceService.disabledStorage(infoBO);
						}						
						break;
					case 3:
						break;
					case 4:// 虚拟机备份
						jobInstanceService.disabledBackup(infoBO);
						break;
					case 5:// 监控服务					
						break;
					case 6:// 负载均衡服务
						break;
					case 7:// 安全组资源服务
						break;
					case 8:// 带宽
						break;
					case 9:// 公网IP
						break;
					case 10:// 物理机
						break;
					case 15:// 数据云备份
						break;
					default:
						break;
					}
				}
			}			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	//虚拟机退订
	private String insertVMAapplyDestroy(TInstanceInfoBO vo) throws SCSException {
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(vo.getId());
	    TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
	    int index = 0;
	    if (info != null) {
	      // deleteVM
	      DeleteVM vmDelete = new DeleteVM();
	      vmDelete.setVMID(vo.getResCode());
	      String parameter = CommandCreateUtil.getJsonParameterStr(vmDelete);
          if(ConstDef.curProjectId == 2){
              AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
              asynJobVDCPO.setUser_id(vo.getUserId());
              asynJobVDCPO.setInstance_info_id(vo.getId());
              asynJobVDCPO.setOperation(OperationType.DELETE_VM);
              asynJobVDCPO.setTemplate_id(info.getTemplateId());
              asynJobVDCPO.setParameter(parameter);
              asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
              index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
          	//用户日志记录
              String para = "instanceId="+vo.getId();
              String memo = parameter;
              ConstDef.saveLogInfo(ConstDef.USERLOG_DEL, "产品管理-块存储job", "回收虚拟机", this.getClass().getName(), "insertVMAapplyDestroy()", para, "用户注销", memo);
			//用户日志结束
          }
	    }
	    return ResourcesUtil.resultTOString(index);
	  }

	//块存储退订
	private void insertVolumeDestroy(TInstanceInfoBO vo) throws Exception {
		try {
			ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
			vmModifyVO.setId(vo.getId());
			TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
			//vdc项目判断
			int project_switch = ConstDef.curProjectId;
			if(null!=info){
				if (project_switch == 2){
	            	//JOB信息封装开始
					AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
					String parameters = "";// 操作参数，调用api所需要的参数
					Map<String, Object> mapJob = new HashMap<String, Object>();
					mapJob.put("BSID", vo.getResCode());
					parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
					asynJobVDCPO.setParameter(parameters);
					asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
					asynJobVDCPO.setUser_id(vo.getUserId());
					asynJobVDCPO.setTemplate_id(info.getTemplateId());
					asynJobVDCPO.setOperation(OperationType.DELETE_EBS);
					asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
					//JOB信息封装结束

					//用户日志记录
						String para = "templateid="+info.getTemplateId()+",res_code="+info.getResourceInfo();
						String memo = parameters;
					ConstDef.saveLogInfo(1, "产品管理-块存储job", "退定job", "VolumeOperateServiceImpl.java", "insertDestroyVolume()", para, "用户注销", memo);
					//用户日志结束
				}
				//vdc项目判断结束
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}

	//虚拟机备份退订 4
	private void deleteBakUpInstance(TInstanceInfoBO vo) throws Exception{
		jobInstanceDao.deleteUserSnapshot(vo.getUserId());
		if(ConstDef.curProjectId ==2){
			instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
		}
		else if(ConstDef.curProjectId ==1){
			ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		    vmModifyVO.setId(vo.getId());
		    vmModifyVO.setApply_reason("到期自动退订");
			TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
			if(null!=info){
			//JOB信息封装开始
			AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
				String parameters = "";// 操作参数，调用api所需要的参数
				String ResourceTemplateID = ConstDef.getVDCTemplateID(vo.getTemplateType(),vo.getTemplateId());
				Map<String, Object> mapJob = new HashMap<String, Object>();
					mapJob.put("VMBackupId", ResourceTemplateID);
				parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
				asynJobVDCPO.setParameter(parameters);
				asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
				asynJobVDCPO.setUser_id(vo.getUserId());
				asynJobVDCPO.setTemplate_id(vo.getTemplateId());
				asynJobVDCPO.setInstance_info_id(vo.getId());
				asynJobVDCPO.setTemplate_res_id(ResourceTemplateID);
				asynJobVDCPO.setOrder_id(vo.getOrderId());
				asynJobVDCPO.setOperation(OperationType.DELETE_VMBAK);
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
			}
		}
	}

	//监控服务退订 5
	private void deleMonitorInstance(TInstanceInfoBO vo){
		if(ConstDef.curProjectId ==2){
			instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
		}
	}

	//负载均衡服务退订 6


	//防火墙资源退订 7
	private void deleteFwInstance(TInstanceInfoBO vo)	throws Exception {
		instanceService.updateTScsIntanceInfoStateById(vo.getId(),STATE_RELEASE);
		List<SGRule> ruleList = SGRuleService.getRuleListByInstanceId(ISGRuleService.SGID_PREFIX + String.format("%08d", vo.getId()));
		StringBuilder stIds = new StringBuilder(200);
		if(null!=ruleList&&!ruleList.isEmpty()){
			for(SGRule rule : ruleList){
				if(stIds.length()>0){
					stIds.append(",");
				}
				stIds.append(rule.getId());
			}
		}
		if(StringUtils.isNotBlank(stIds.toString())){
	        try {
	          String[] ids = stIds.toString().split(",");
	          for(String id : ids) {
	            SGRuleService.deleteRuleForDestory(Integer.parseInt(id));
	          }
	        }
	        catch(Exception e) {
	          e.printStackTrace();
	        }
	      }
	}

	//带宽退订  8
	private String deleteBandWidthInstance(TInstanceInfoBO vo) throws Exception{
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(vo.getId());
		vmModifyVO.setUserID(vo.getUserId());
		return bandWidthService.deleteBandWidthInstance(vmModifyVO);
	}

	//公网IP退订 9
	//publicIPService.writeBackForReleasePublicIP(vo.getId(),STATE_RELEASE);

	//数据云备份退订 15
	private void deleteDataBak(TInstanceInfoBO vo) throws SCSException{
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(vo.getId());
		TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
		if(null!=info){
			if (ConstDef.curProjectId ==2){
	 			AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
	 			asynJobVDCPO.setParameter(vo.getResCode());
	 			asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
	 			asynJobVDCPO.setUser_id(vo.getUserId());
	 			asynJobVDCPO.setTemplate_id(info.getTemplateId());
	 			asynJobVDCPO.setOperation(OperationType.DELETE_DATABAK);
	 			asynJobVDCPO.setInstance_info_id(info.getId());
	 			asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
	 			//用户日志记录
	            String para = "instanceId="+vo.getId();
	            ConstDef.saveLogInfo(ConstDef.USERLOG_DEL, "产品管理-数据云备份job", "回收数据云备份", this.getClass().getName(), "deleteDataBak()", para, "", "");
				//用户日志结束
	 		}
		}
	}


	private long getNewUnit(String unit,int allNumDay){
		if (unit == null || unit.equals("")) {
			return 0;
		}

		long value = 0;
		if (unit.equals("Y")) {
			value = allNumDay / 365;
		} else if (unit.equals("M")) {
			value = allNumDay / 30;
		} else if (unit.equals("W")) {
			value = allNumDay / 7;
		} else if (unit.equals("D")) {
			value = allNumDay;
		} else if (unit.equals("H")) {
			value = allNumDay * 24;
		} else {
			value = 0;
		}
		return value;
	}

	private int  getNumByUnit_D(char  unit,int beforeNum){
	      int  value = 0;
	      switch(unit){
	        case 'Y':
	        	value=beforeNum*365;
				break;
			case 'M':
			    value = beforeNum*30;
				break;
			case 'W':
				value = beforeNum*7;
				break;
			case 'H':
			    value = beforeNum/24;
				break;
			case 'D':
				value = beforeNum;
				break;
			case 'S':
			    value = 0;
				break;
			default:
			    value = 0;
	      }
	    return value;
	}


	private String  getUnitName(char  unit){
	      String  value = "";
	      switch(unit){
	        case 'Y':
				value = "年";
				break;
			case 'M':
			    value = "月";
				break;
			case 'W':
				value = "周";
				break;
			case 'H':
			    value = "小时";
				break;
			case 'D':
				value = "日";
				break;
			case 'S':
			    value = "流量";
				break;
			default:
			    value = "";
	      }
	    return value;
	}

	private String replaceYMD(String YMDStr) {
		String value = YMDStr;
		if(value==null) {
	        return "";
        }
		value = value.replace("Y", "年");
		value = value.replace("M", "月");
		value = value.replace("D", "日");
		value = value.replace("W", "周");
		value = value.replace("H", "小时");
		value = value.replace("S", "流量");
		return value;
	}

	public void setInstancePeriodManageDao(IInstancePeriodManageDao instancePeriodManageDao) {
		this.instancePeriodManageDao = instancePeriodManageDao;
	}

	public IInstancePeriodManageDao getInstancePeriodManageDao() {
		return instancePeriodManageDao;
	}

	public VirtualMachineModifyService getVmModifyService() {
		return vmModifyService;
	}

	public void setVmModifyService(VirtualMachineModifyService vmModifyService) {
		this.vmModifyService = vmModifyService;
	}

	public IPublicIPInstanceService getPublicIPService() {
		return publicIPService;
	}

	public void setPublicIPService(IPublicIPInstanceService publicIPService) {
		this.publicIPService = publicIPService;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

	public BandWidthInstanceOperateService getBandWidthService() {
		return bandWidthService;
	}

	public void setBandWidthService(BandWidthInstanceOperateService bandWidthService) {
		this.bandWidthService = bandWidthService;
	}

	public ISGRuleService getSGRuleService() {
		return SGRuleService;
	}

	public void setSGRuleService(ISGRuleService sGRuleService) {
		SGRuleService = sGRuleService;
	}

	public IInstanceService getInstanceService() {
		return instanceService;
	}

	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}


	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IJobInstanceInfoDao getJobInstanceDao() {
		return jobInstanceDao;
	}

	public void setJobInstanceDao(IJobInstanceInfoDao jobInstanceDao) {
		this.jobInstanceDao = jobInstanceDao;
	}

    public IOrderDao getOrderDao() {
    	return orderDao;
    }

    public void setOrderDao(IOrderDao orderDao) {
    	this.orderDao = orderDao;
    }

    public IAuditSevice getAuditService() {
    	return auditService;
    }

    public void setAuditService(IAuditSevice auditService) {
    	this.auditService = auditService;
    }

    public IServiceInstanceService getServiceInstanceService() {
    	return serviceInstanceService;
    }

    public void setServiceInstanceService(IServiceInstanceService serviceInstanceService) {
    	this.serviceInstanceService = serviceInstanceService;
    }

	public IJobInstanceInfoService getJobInstanceService() {
		return jobInstanceService;
	}

	public void setJobInstanceService(IJobInstanceInfoService jobInstanceService) {
		this.jobInstanceService = jobInstanceService;
	}    

}
