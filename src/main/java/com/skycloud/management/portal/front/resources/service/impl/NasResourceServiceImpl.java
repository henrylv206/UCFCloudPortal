package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IResourcePoolsService;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.INicsDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalVlanIP;
import com.skycloud.management.portal.front.resources.service.NasResourceService;
import com.skycloud.management.portal.front.resources.service.PhysicalMachinesService;

public class NasResourceServiceImpl implements NasResourceService {

	private final Log logger = LogFactory.getLog(NasResourceServiceImpl.class);

	private ResouceServiceInstanceOperateDao resourceServiceInstanceOperateDao;

	private ICommandService commandService;

	private INicsDao nicsDao;

	private PhysicalMachinesService pmService;

	private IInstanceInfoDao instanceInfoDao;

	private IOrderDao orderDao;

	private IAuditSevice auditService;

	private IResourcePoolsService resourcePoolsService;

	private String port;

	private String servername;


	public NasResourceServiceImpl() {
	  this.port =  ConfigManager.getInstance().getString("nas.port");
    this.servername =  ConfigManager.getInstance().getString("nas.servername");
  }
	@Override
	public TTemplateVMBO creatSpecalIpSanTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
		// TODO Auto-generated method stub
		template.setCode(null);// 模板编码
		// 特殊模板 1：用户定义的特殊模板
		template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);
		template.setCpufrequency(0);
		template.setCpuNum(0);
		template.setMemorySize(0);
		vminfo.setDisknumber(0);
		template.setStorageSize(vminfo.getStorageSize());
		template.setVethAdaptorNum(0);
		if (user != null) {
			template.setCreatorUserId(user.getId());
		}
		template.setZoneId(vminfo.getZoneId());
		template.setResourcePoolsId(vminfo.getPoolId());
		template.setType(vminfo.getTemplateType());
		template.setOperType(1);
		Timestamp ts = new Timestamp(new Date().getTime());
		template.setCreateTime(ts.toString());
		template.setMeasureMode("Duration");// Duration：按时长计量
		template.setState(ConstDef.STATE_TWO);//可用状态modify by hfk 13-1-22
		template.seteOsId(0);
		// 根据物理机创建模板,操作系统信息组织要创建服务信息
		product.setName("IpSan");
		product.setCreateDate(ts);
		product.setType(template.getType());
		product.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
		product.setDescription("弹性块存储自动创建服务");
		product.setSpecification(product.getDescription());
		product.setQuotaNum(1);// 待确定 TODO
		product.setPrice(0f);
		product.setUnit(vminfo.getUnit());
		product.setOperateType(1);
		product.setIsDefault(0);// 待确定 TODO
		return template;
	}

	@Override
	public TTemplateVMBO creatSpecalNasTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
		// TODO Auto-generated method stub
		template.setCode(null);// 模板编码
		// 特殊模板 1：用户定义的特殊模板
		template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);
		template.setCpufrequency(0);
		template.setCpuNum(0);
		template.setMemorySize(0);
		vminfo.setDisknumber(0);
		template.setStorageSize(vminfo.getStorageSize());
		template.setVethAdaptorNum(0);
		if (user != null) {
			template.setCreatorUserId(user.getId());
		}
		template.setZoneId(vminfo.getZoneId());
		template.setResourcePoolsId(vminfo.getPoolId());
		template.setType(vminfo.getTemplateType());
		template.setOperType(1);
		Timestamp ts = new Timestamp(new Date().getTime());
		template.setCreateTime(ts.toString());
		template.setMeasureMode("Duration");// Duration：按时长计量
		template.setState(1);
		template.seteOsId(0);
		// 根据物理机创建模板,操作系统信息组织要创建服务信息
		product.setName("Nasstorage");
		product.setCreateDate(ts);
		product.setType(template.getType());
		product.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
		product.setDescription("NAS存储自动创建服务");
		product.setSpecification(product.getDescription());
		product.setQuotaNum(1);// 待确定 TODO
		product.setPrice(0f);
		product.setUnit(vminfo.getUnit());
		product.setOperateType(1);
		product.setIsDefault(0);// 待确定 TODO
		return template;
	}

	@Override
	public TTemplateVMBO creatSpecalObjectStorageTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
		// TODO Auto-generated method stub
		template.setCode(null);// 模板编码
		// 特殊模板 1：用户定义的特殊模板
		template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);
		template.setCpufrequency(0);
		template.setCpuNum(0);
		template.setMemorySize(0);
		vminfo.setDisknumber(0);
		template.setStorageSize(vminfo.getStorageSize());
		template.setVethAdaptorNum(0);
		if (user != null) {
			template.setCreatorUserId(user.getId());
		}
		template.setZoneId(vminfo.getZoneId());
		template.setResourcePoolsId(vminfo.getPoolId());
		template.setType(vminfo.getTemplateType());
		template.setOperType(1);
		Timestamp ts = new Timestamp(new Date().getTime());
		template.setCreateTime(ts.toString());
		template.setMeasureMode("Duration");// Duration：按时长计量
		template.setState(ConstDef.STATE_TWO);//可用状态modify by hfk 13-1-22
		template.seteOsId(0);
		// 根据物理机创建模板,操作系统信息组织要创建服务信息
		product.setName("IpSan");
		product.setCreateDate(ts);
		product.setType(template.getType());
		product.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
		product.setDescription("弹性块存储自动创建服务");
		product.setSpecification(product.getDescription());
		product.setQuotaNum(1);// 待确定 TODO
		product.setPrice(0f);
		product.setUnit(vminfo.getUnit());
		product.setOperateType(1);
		product.setIsDefault(0);// 待确定 TODO
		return template;
	}

	@Override
	public List<ResourcesVO> queryResouceInstanceInfoIncludeMc(ResourcesQueryVO rqvo) throws Exception {
		List<ResourcesVO> resourceInfos = resourceServiceInstanceOperateDao.queryResouceInstanceInfoIncludeMc(rqvo);
		resourceInfos = this.addIpInfotoResourceInstances(resourceInfos);
		return resourceInfos;
	}



	@Override
	public ResponseEntity<String> accessToNasResource(TInstanceInfoBO infobo,String instanceIp) throws Exception {
		try {
			RestTemplate rt = new RestTemplate();
			TResourcePoolsBO resourcePool = resourcePoolsService.searchById(infobo.getResourcePoolsId());
			String url = "http://" + resourcePool.getIp() +":"+port+"/"+servername+"/nasmgr/dir/{dirId}/access";
			HttpHeaders requestHeaders = new HttpHeaders();
			String resourceInfo = infobo.getResourceInfo();
			String account = JSONObject.fromObject(resourceInfo).getString("account");
			requestHeaders.set("NAS-OPERATOR", account);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.set("accessName", "ip");
			map.set("accessValue", instanceIp);
			HttpEntity<String> entity = new HttpEntity(map, requestHeaders);
			ResponseEntity<String> resp = rt.postForEntity(url, entity, String.class, infobo.getResCode());

			return resp;
		} catch (Exception e) {
			logger.error("access nas error : "+e.getMessage());
			throw new Exception("access nas error : " + e.getMessage());
		}

	}

	public TServiceInstanceBO updateServiceInfoState(int serviceID) throws SCSException {
		int index = 0;
		int id = serviceID;
		TServiceInstanceBO info = null;
		if (id != 0) {
			try {
				info = instanceInfoDao.findServiceInstanceById(serviceID);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			if (info.getState() == 7) {// 不可用的退订时，直接作废
				index = instanceInfoDao.updateServiceState4(serviceID);
			} else if (info.getState() == 6) {
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

	@Override
	public void insertDestroyNAS(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {

		this.insertDestoryNAS(instanceId, user, reason, serviceID);
	}

	public void insertDestoryNAS(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);
		updateServiceInfoState(serviceID);
		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			if (info.getState() == 7) {// bug 0003965
				info.setState(4);// 退订不可用的资源，直接作废，不生成订单
			} else {
				info.setState(3);
				TOrderBO order = new TOrderBO();
				order.setType(ConstDef.ORDER_DEL_TYPE);
				order.setState(1); // 申请状态
				order.setCreatorUserId(user.getId()); // 下单人ID
				// order.setInstanceInfoId(instanceId);
				order.setServiceInstanceId(serviceID);
				order.setZoneId(info.getZoneId());
				order.setClusterId(info.getState()); // clusterId中保存原instance状态
				order.setOrderApproveLevelState(user.getRoleApproveLevel());
				order.setState(user.getRoleApproveLevel());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
				order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
				// 日期+6位随机数
				order.setReason(reason);
				int saveOrderId = orderDao.save(order);
				if (saveOrderId > 0) {
					if (user.getRoleApproveLevel() == 4) {
						try {
							auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
							                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					} else if (user.getRoleApproveLevel() < 4) {
						try {
							auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
							                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					}
				}
			}
			instanceInfoDao.update(info);
		}
	}

	@Override
	public String getNasResourceByDirId(TInstanceInfoBO infobo) throws Exception {
		try {
			RestTemplate rt = new RestTemplate();
			TResourcePoolsBO resourcePool = resourcePoolsService.searchById(infobo.getResourcePoolsId());
			String getUrl = "http://" + resourcePool.getIp()  +":"+port+"/"+servername+"/nasmgr/dir/{dirId}/access";
			
			if(StringUtils.isBlank(infobo.getResCode())){
        logger.error("resCode is null");
        return null;
      }
			HttpEntity<String> resp  =rt.getForEntity(getUrl, String.class, infobo.getResCode());

			if(resp!=null){
				JSONObject jsonObject = JSONObject.fromObject(resp.getBody());
				return jsonObject.toString();
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("get nas error : "+e.getMessage());
			throw new Exception("get nas error : " + e.getMessage());
		}

	}



	@Override
  public String getNasResourceDirInfo(TInstanceInfoBO infobo) throws Exception {
	  try {
      RestTemplate rt = new RestTemplate();
      TResourcePoolsBO resourcePool = resourcePoolsService.searchById(infobo.getResourcePoolsId());
      String getUrl = "http://" + resourcePool.getIp()  +":"+port+"/"+servername+"/nasmgr/dir/{dirId}";

      if(StringUtils.isBlank(infobo.getResCode())){
        logger.error("resCode is null");
        return null;
      }
      HttpEntity<String> resp  =rt.getForEntity(getUrl, String.class, infobo.getResCode());

      if(resp!=null){
        JSONObject jsonObject = JSONObject.fromObject(resp.getBody());
        return jsonObject.toString();
      }else{
        return null;
      }
    } catch (Exception e) {
      logger.error("get nas error : "+e.getMessage());
      throw new Exception("get nas error : " + e.getMessage());
    }

  }
  @Override
	public void deleteaccessFromNasResource(TInstanceInfoBO infobo,String dirId) throws Exception {
		try {
			RestTemplate rt = new RestTemplate();
			HttpHeaders requestHeaders = new HttpHeaders();
			String resourceInfo = infobo.getResourceInfo();
	    String account =  JSONObject.fromObject(resourceInfo).getString("account");
		  requestHeaders.set("NAS-OPERATOR", account);
		  HttpEntity entity = new HttpEntity(requestHeaders);
			TResourcePoolsBO resourcePool = resourcePoolsService.searchById(infobo.getResourcePoolsId());
			String deleteUrl = "http://" + resourcePool.getIp() +":"+port+"/"+servername+"/nasmgr/dir/{dirId}/access/{accessId}";
			Map<String,String> map = new HashMap<String,String>();
			map.put("dirId", infobo.getResCode());
			map.put("accessId", dirId);
			ResponseEntity<Object> response = rt.exchange(deleteUrl, HttpMethod.DELETE, entity, null, map);
		} catch (Exception e) {
			logger.error("delete access nas error : "+e.getMessage());
			throw new Exception("delete access nas error : " + e.getMessage());
		}

	}



	private List<ResourcesVO> addIpInfotoResourceInstances(List<ResourcesVO> resources) throws Exception {

		for (ResourcesVO resource : resources) {
			// vm
			if (StringUtils.equals(resource.getTemplate_type(), "1")) {
				ListVirtualMachines elasterVmList = new ListVirtualMachines();
				elasterVmList.setId(Long.parseLong(resource.getE_instance_id()));
				logger.info("find vm ip address by vm id {}" + resource.getE_instance_id());
				Object response = commandService.executeAndJsonReturn(elasterVmList, resource.getResourcePoolsId());
				JSONObject jsonRes = JSONObject.fromObject(response);
				String result = jsonRes.getString("listvirtualmachinesresponse");
				JSONObject resultObject = JSONObject.fromObject(result);
				if (resultObject.containsKey("virtualmachine")) {
					JSONObject resultO = resultObject.getJSONArray("virtualmachine").getJSONObject(0);
					JSONArray nicList = resultO.getJSONArray("nic");
					List<TNicsBO> nicsBOs = new ArrayList<TNicsBO>();
					for (int i = 0; i < nicList.size(); i++) {
						JSONObject nicresult = nicList.getJSONObject(i);
						TNicsBO nic = new TNicsBO();
						nic.setVlan(nicresult.getString("networkid"));
						nic.setIp(nicresult.getString("ipaddress"));
						nicsBOs.add(nic);
					}
					resource.setNicsBOs(nicsBOs);
				}
				// mc
			} else if (StringUtils.equals(resource.getTemplate_type(), "3")) {
				List<TNicsBO> nics = nicsDao.searchNicssByInstanceId(resource.getId());
				resource.setNicsBOs(nics);
				// pm
			} else if (StringUtils.equals(resource.getTemplate_type(), "10")) {
				ListPhysicalVlanIP listVO = new ListPhysicalVlanIP();
				listVO.setId(Long.parseLong(resource.getE_instance_id()));
				List<ListPhysicalVlanIP> list = pmService.findPhysicalHostVlanIpByREST(listVO, resource.getResourcePoolsId());
				List<TNicsBO> nicsBOs = new ArrayList<TNicsBO>();
				for(int i = 0; i < list.size(); i++){
					TNicsBO nic = new TNicsBO();
					nic.setIp(list.get(i).getIp());
					nic.setVlan(String.valueOf(list.get(i).getVlanid()));
					nicsBOs.add(nic);
				}
				resource.setNicsBOs(nicsBOs);
			}
		}
		return resources;
	}

	public ResouceServiceInstanceOperateDao getResourceServiceInstanceOperateDao() {
		return resourceServiceInstanceOperateDao;
	}

	public void setResourceServiceInstanceOperateDao(ResouceServiceInstanceOperateDao resourceServiceInstanceOperateDao) {
		this.resourceServiceInstanceOperateDao = resourceServiceInstanceOperateDao;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public INicsDao getNicsDao() {
		return nicsDao;
	}

	public void setNicsDao(INicsDao nicsDao) {
		this.nicsDao = nicsDao;
	}

	public PhysicalMachinesService getPmService() {
		return pmService;
	}

	public void setPmService(PhysicalMachinesService pmService) {
		this.pmService = pmService;
	}



	public IResourcePoolsService getResourcePoolsService() {
		return resourcePoolsService;
	}



	public void setResourcePoolsService(IResourcePoolsService resourcePoolsService) {
		this.resourcePoolsService = resourcePoolsService;
	}



	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}



	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
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
  public String getPort() {
    return port;
  }
  public void setPort(String port) {
    this.port = port;
  }
  public String getServername() {
    return servername;
  }
  public void setServername(String servername) {
    this.servername = servername;
  }

}
