package com.skycloud.management.portal.front.sg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.skycloud.h3c.firewall.common.SysManageService;
import com.skycloud.h3c.firewall.customservice.elements.po.BaseCustomServiceInfo;
import com.skycloud.h3c.firewall.customservice.service.H3cCustomServices;
import com.skycloud.h3c.firewall.policy.elements.po.BaseInterCreatePolicyRowRes;
import com.skycloud.h3c.firewall.policy.elements.po.BaseInterZonePolicyRowRes;
import com.skycloud.h3c.firewall.policy.elements.po.LoginResponsePo;
import com.skycloud.h3c.firewall.policy.elements.po.MovePolicyPo;
import com.skycloud.h3c.firewall.policy.request.po.QueryVo;
import com.skycloud.h3c.firewall.policy.response.po.QueryAllInterZonePolicyResponse;
import com.skycloud.h3c.firewall.policy.response.po.Response;
import com.skycloud.h3c.firewall.policy.service.H3cLoginService;
import com.skycloud.h3c.firewall.policy.service.H3cPolicyService;
import com.skycloud.management.portal.admin.sysmanage.dao.IPublicIPDao;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.network.h3c.commons.Constants;
import com.skycloud.management.portal.front.network.h3c.dao.IH3CApiLogDao;
import com.skycloud.management.portal.front.network.h3c.dao.IHLJVirtualFirewallDao;
import com.skycloud.management.portal.front.network.h3c.entity.H3CApiLog;
import com.skycloud.management.portal.front.network.h3c.entity.HLJVirtualFirewall;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.sg.dao.IDeviceInfoDao;
import com.skycloud.management.portal.front.sg.dao.IH3CIPDao;
import com.skycloud.management.portal.front.sg.dao.ISGCustomServiceDao;
import com.skycloud.management.portal.front.sg.dao.ISGRuleDao;
import com.skycloud.management.portal.front.sg.entity.DeviceInfo;
import com.skycloud.management.portal.front.sg.entity.DeviceType;
import com.skycloud.management.portal.front.sg.entity.H3CIP;
import com.skycloud.management.portal.front.sg.entity.SGCustomService;
import com.skycloud.management.portal.front.sg.entity.SGRule;
import com.skycloud.management.portal.front.sg.service.ISGRuleService;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

/**
 * 安全组规则业务实现
 * @author jiaoyz
 */
public class SGRuleServiceImpl implements ISGRuleService {

  private ISGRuleDao SGRuleDao;

  private IH3CIPDao H3CIPDao;

  private ISGCustomServiceDao SGCustomServiceDao;

  private IDeviceInfoDao deviceInfoDao;

  private AsyncJobVDCService asyncJobVDCService;

  private IInstanceService instanceService;

  private H3cLoginService h3cLoginService;

  private H3cPolicyService h3cFirewllService;

  private H3cCustomServices h3cCustomService;

  private IHLJVirtualFirewallDao HLJVirtualFirewallDao;

  private IPublicIPDao publicIPDao;

  private IH3CApiLogDao apiLogDao;

  private SysManageService h3cSysManageService;

  @Override
  public int createRule(SGRule rule) throws Exception {
    if(rule == null) {
      throw new Exception("Paramater is missing");
    }
    if(rule.getId() > 0) {
      throw new Exception("Paramater is invalid");
    }
    int instanceId = Integer.parseInt(rule.getSgId().replace(SGID_PREFIX, ""));
    TInstanceInfoBO bo = instanceService.queryInstanceInfoById(instanceId);
    int id = 0;
    rule.setOperate(SG_RULE_OPERATE.CREATE.getValue());
    if(ConstDef.curProjectId == 2) {
      rule.setStatus(SG_RULE_STATUS.ING.getValue());
      id = SGRuleDao.createRule(rule);
      AsyncJobVDCPO job = new AsyncJobVDCPO();
      job.setUser_id(bo.getUserId());
      job.setOrder_id(bo.getOrderId());
      job.setInstance_info_id(instanceId);
      job.setFirewall_rule_id(id);
      job.setOperation(OperationType.CREATE_SGRULES);
      job.setParameter(rule.getAddSGRulesReq());
      job.setAuditstate(AuditStateVDC.NO_AUDIT);
      asyncJobVDCService.insterAsyncJobVDC(job);
    }
    else {
      HLJVirtualFirewall firewall = HLJVirtualFirewallDao.getFirewallByUserId(bo.getUserId());
      String serviceName = getCustomServiceName(firewall, rule, bo.getUserId());
      if(serviceName == null) {
        throw new Exception("Create h3c custom service failed");
      }
      addPolicy(rule, firewall, serviceName, bo.getUserId());
      rule.setStatus(SG_RULE_STATUS.ON.getValue());
      id = SGRuleDao.createRule(rule);
    }
    return id;
  }

  @Override
  public void deleteRule(int id) throws Exception {
    if(id < 1) {
      throw new Exception("Paramater is invalid");
    }
    SGRule rule = this.getRuleById(id);
    if(rule == null) {
      throw new Exception("Object not found");
    }
    if(rule.getStatus() == SG_RULE_STATUS.ING.getValue()) {
      throw new Exception("Operation not allowed");
    }
    if(rule.getStatus() == SG_RULE_STATUS.ON.getValue()) { //如果规则状态为生效，则更新状态为处理中，更新操作为删除，call job api
      rule.setOperate(SG_RULE_OPERATE.REMOVE.getValue());
      rule.setStatus(SG_RULE_STATUS.ING.getValue());
      this.updateRule(rule);
    }
    else { // 如果规则状态为失败，则根据上次操作类型区别处理
      if(rule.getOperate() == SG_RULE_OPERATE.CREATE.getValue()) { // 如果是创建失败，则直接删除记录
        SGRuleDao.deleteRule(id);
        return;
      }
      else { // 如果是删除失败，则更新状态为处理中，重新call job api
        rule.setStatus(SG_RULE_STATUS.ING.getValue());
        this.updateRule(rule);
      }
    }
    int instanceId = Integer.parseInt(rule.getSgId().replace(SGID_PREFIX, ""));
    TInstanceInfoBO bo = instanceService.queryInstanceInfoById(instanceId);
    if(ConstDef.curProjectId == 2) {
      AsyncJobVDCPO job = new AsyncJobVDCPO();
      job.setUser_id(bo.getUserId());
      job.setOrder_id(bo.getOrderId());
      job.setInstance_info_id(instanceId);
      job.setFirewall_rule_id(id);
      job.setOperation(OperationType.DELETE_SGRULES);
      job.setParameter(rule.getDeleteSGRulesReq());
      job.setAuditstate(AuditStateVDC.NO_AUDIT);
      asyncJobVDCService.insterAsyncJobVDC(job);
    }
    else {
      HLJVirtualFirewall firewall = HLJVirtualFirewallDao.getFirewallByUserId(bo.getUserId());
      delPolicy(rule, firewall, bo.getUserId());
      SGRuleDao.deleteRule(id);
    }
  }

  @Override
  public SGRule getRuleById(int id) throws Exception {
    if(id < 1) {
      throw new Exception("Paramater is invalid");
    }
    return SGRuleDao.getRuleById(id);
  }

  @Override
  public List<SGRule> getRuleList(SGRule carrier, int curPage, int pageSize) throws Exception {
    return SGRuleDao.getRuleList(carrier, curPage, pageSize);
  }
  
  @Override
  public List<SGRule> getRuleListByBindIP(SGRule carrier) throws Exception {
    return SGRuleDao.getRuleListByBindIP(carrier);
  }

  @Override
  public int getRuleListCount(SGRule carrier) throws Exception {
    return SGRuleDao.getRuleListCount(carrier);
  }

  @Override
  public void updateRule(SGRule rule) throws Exception {
    if(rule == null) {
      throw new Exception("Paramater is missing");
    }
    if(rule.getId() < 1) {
      throw new Exception("Paramater is invalid");
    }
    SGRuleDao.updateRule(rule);
  }

  @Override
  public int updateRuleJob(SGRule carrier) throws SCSException {
    return SGRuleDao.updateRuleJob(carrier);
  }

  @Override
  public List<SGRule> getRuleListByInstanceId(String id) throws Exception {
    return SGRuleDao.getRuleListByInstanceId(id);
  }

  @Override
  public void deleteRuleForDestory(int id) throws Exception {
    if(id < 1) {
      throw new Exception("Paramater is invalid");
    }
    SGRule rule = this.getRuleById(id);
    if(rule == null) {
      throw new Exception("Object not found");
    }
    int instanceId = Integer.parseInt(rule.getSgId().replace(SGID_PREFIX, ""));
    TInstanceInfoBO bo = instanceService.queryInstanceInfoById(instanceId);
    if(ConstDef.curProjectId == 2) {
      AsyncJobVDCPO job = new AsyncJobVDCPO();
      job.setUser_id(bo.getUserId());
      job.setOrder_id(bo.getOrderId());
      job.setInstance_info_id(instanceId);
      job.setFirewall_rule_id(id);
      job.setOperation(OperationType.DELETE_SGRULES);
      job.setParameter(rule.getDeleteSGRulesReq());
      job.setAuditstate(AuditStateVDC.NO_AUDIT);
      asyncJobVDCService.insterAsyncJobVDC(job);
    }
    else {
      HLJVirtualFirewall firewall = HLJVirtualFirewallDao.getFirewallByUserId(bo.getUserId());
      delPolicy(rule, firewall, bo.getUserId());
      SGRuleDao.deleteRule(id);
    }
  }

  private synchronized String getCustomServiceName(HLJVirtualFirewall firewall, SGRule rule, int userId) throws Exception {
    String serviceName = null;
    SGCustomService service = SGCustomServiceDao.getSGCustomService(firewall.getVirtualDeviceId(), firewall.getDeviceId(), rule.getProtocol(), rule.getSourcePort(), rule.getDestinationPort());
    if(service == null) {
      service = new SGCustomService();
      service.setDstPort(rule.getDestinationPort());
      service.setProtocol(rule.getProtocol());
      service.setSrcPort(rule.getSourcePort());
      service.setDeviceId(firewall.getDeviceId());
      service.setVdId(firewall.getVirtualDeviceId());
      int id = SGCustomServiceDao.createCustomService(service);
      service.setId(id);
      service.setName("portal_custom_" + id);
      SGCustomServiceDao.updateCustomService(service);
      LoginResponsePo po = null;
      try {
        DeviceInfo device = deviceInfoDao.getDeviceInfoByType(DeviceType.FIREWALL).get(0);
        if(device == null) {
          SGCustomServiceDao.deleteCustomService(id);
          throw new Exception("h3c : device not found by type " + DeviceType.FIREWALL.toString());
        }
        h3cLoginService.setUrlh3(device.getVirtualIp());
        h3cCustomService.setUrlh3(device.getVirtualIp());
        po = h3cLoginService.login(device.getUsername(), device.getPassword(), "Skycloud");
        H3CApiLog log = new H3CApiLog();
        log.setUserId(userId);
        log.setCreateDate(new Date());
        log.setOperate("Custom service : login " + device.getVirtualIp());
        if(po == null) {
          log.setResult(0);
          apiLogDao.createApiLog(log);
          SGCustomServiceDao.deleteCustomService(id);
          throw new Exception("h3c : login failed");
        }
        log.setResult(1);
        apiLogDao.createApiLog(log);
        //HLJVirtualFirewall rootWall = HLJVirtualFirewallDao.getFirewallByUniqueIndex(Constants.ID_ROOT_FIREWALL, 0);
        BaseCustomServiceInfo info = new BaseCustomServiceInfo();
        info.setObjectname(service.getName());
        if(rule.getProtocol().equalsIgnoreCase("tcp")) {
          info.setProtocol("6");
        }
        else {
          info.setProtocol("17");
        }
        //fix bug 3820
        if(rule.getDestinationPort() == 0) {
          info.setDstportbegin("0");
          info.setDstportend("65535");
        }
        else {
          info.setDstportbegin(String.valueOf(rule.getDestinationPort()));
          info.setDstportend(String.valueOf(rule.getDestinationPort()));
        }
        if(rule.getSourcePort() == 0) {
          info.setSrcportbegin("0");
          info.setSrcportend("65535");
        }
        else {
          info.setSrcportend(String.valueOf(rule.getSourcePort()));
          info.setSrcportbegin(String.valueOf(rule.getSourcePort()));
        }
        /*
        info.setOwnerid(String.valueOf(rootWall.getVirtualDeviceId())); //在ROOT上创建一份
        Response resp = h3cCustomService.createCustomService(info, po.getUid());
        log.setCreateDate(new Date());
        log.setOperate("Custom service : create " + service.getName() + " on vd " + rootWall.getVirtualDeviceId());
        if(resp != null && resp.executeIsSuccess()) {
          serviceName = service.getName();
          log.setResult(1);
          apiLogDao.createApiLog(log);
        }
        else {
          log.setResult(0);
          apiLogDao.createApiLog(log);
          SGCustomServiceDao.deleteCustomService(id);
          throw new Exception("h3c : " + resp.getErrorMsg());
        }
        */
        info.setOwnerid(String.valueOf(firewall.getVirtualDeviceId())); //在虚设备上创建一份
        Response resp = h3cCustomService.createCustomService(info, po.getUid());
        log.setCreateDate(new Date());
        log.setOperate("Custom service : create " + service.getName() + " on vd " + firewall.getVirtualDeviceId());
        if(resp != null && resp.executeIsSuccess()) {
          serviceName = service.getName();
          log.setResult(1);
          apiLogDao.createApiLog(log);
        }
        else {
          log.setResult(0);
          apiLogDao.createApiLog(log);
          SGCustomServiceDao.deleteCustomService(id);
          throw new Exception("h3c : " + resp.getErrorMsg());
        }
        saveConfig();
      }
      finally {
        if(po != null) {
          try {
            h3cLoginService.logout(po.getUid());
          }
          catch(Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    else {
      serviceName = service.getName();
    }
    return serviceName;
  }

  /*
  private H3CIP getH3CIP(SGRule rule) throws Exception {
    List<H3CIP> h3cip = null;
    if(rule.getSourceIp().contains("/")) { //流入
      h3cip = H3CIPDao.getH3CIPByPrivateIp(rule.getDestinationIp());
    }
    else { //流出
      h3cip = H3CIPDao.getH3CIPByPrivateIp(rule.getSourceIp());
    }
    if(h3cip == null || h3cip.size() == 0) {
      throw new Exception("H3C zone with this ip is not exists");
    }
    return h3cip.get(0);
  }
  */

  private void addPolicy(SGRule rule, HLJVirtualFirewall firewall, String serviceName, int userId) throws Exception {
    //HLJVirtualFirewall rootWall = HLJVirtualFirewallDao.getFirewallByUniqueIndex(Constants.ID_ROOT_FIREWALL, 0);
    String group = "";
    BaseInterCreatePolicyRowRes req = new BaseInterCreatePolicyRowRes();
    req.setAclid("65535");
    req.setAcltype("1");
    req.setEnable("1");
    req.setDescrip("");
    req.setLog("0");
    req.setTimerange("");
    if(rule.getSourceIp().contains("/")) { //流入
      group = String.format("%04x", firewall.getVirtualDeviceId()) + String.format("%04x", firewall.getUntrustZoneId()) + "-" + String.format("%04x", firewall.getVirtualDeviceId()) + String.format("%04x", firewall.getTrustZoneId());
      req.setAclgroupname(group);
      req.setDesobj(publicIPDao.getPrivateIpByPublicIp(rule.getDestinationIp()) + "/0.0.0.0");
      req.setSrcobj(rule.getSourceIp());
    }
    else { //流出
      group = String.format("%04x", firewall.getVirtualDeviceId()) + String.format("%04x", firewall.getTrustZoneId()) + "-" + String.format("%04x", firewall.getVirtualDeviceId()) + String.format("%04x", firewall.getUntrustZoneId());
      req.setAclgroupname(group);
      req.setDesobj(rule.getDestinationIp());
      req.setSrcobj(publicIPDao.getPrivateIpByPublicIp(rule.getSourceIp()) + "/0.0.0.0");
    }
    req.setObjectname(serviceName);
    req.setFilter(String.valueOf(rule.getAccess()));
    LoginResponsePo po = null;
    try {
      DeviceInfo device = deviceInfoDao.getDeviceInfoByType(DeviceType.FIREWALL).get(0);
      if(device == null) {
        throw new Exception("h3c : device not found by type " + DeviceType.FIREWALL.toString());
      }
      h3cLoginService.setUrlh3(device.getVirtualIp());
      h3cFirewllService.setUrlh3(device.getVirtualIp());
      po = h3cLoginService.login(device.getUsername(), device.getPassword(), "Skycloud");
      H3CApiLog log = new H3CApiLog();
      log.setUserId(userId);
      log.setCreateDate(new Date());
      log.setOperate("Add policy : login " + device.getVirtualIp());
      if(po == null) {
        log.setResult(0);
        apiLogDao.createApiLog(log);
        throw new Exception("h3c : login failed");
      }
      log.setResult(1);
      apiLogDao.createApiLog(log);
      Response resp = h3cFirewllService.createInterZonePolicy(req, po.getUid());
      log.setCreateDate(new Date());
      log.setOperate("Add policy : from " + req.getSrcobj() + " to " + req.getDesobj());
      if(resp == null || ! resp.executeIsSuccess()) {
        log.setResult(0);
        apiLogDao.createApiLog(log);
        throw new Exception("h3c : " + resp.getErrorMsg());
      }
      log.setResult(1);
      apiLogDao.createApiLog(log);
      //调用移动默认域间策略位置方法
      MovePolicyPo movePo = new MovePolicyPo();
      movePo.setAclgroupname(group);
      movePo.setAclid("0");
      movePo.setAcltype("1");
      movePo.setMoveto("65535");
      resp = h3cFirewllService.moveInterZonePolicy(movePo, po.getUid());
      log.setCreateDate(new Date());
      log.setOperate("Add policy : move default policy to list tail");
      if(resp == null || ! resp.executeIsSuccess()) {
        log.setResult(0);
        apiLogDao.createApiLog(log);
        throw new Exception("h3c : " + resp.getErrorMsg());
      }
      log.setResult(1);
      apiLogDao.createApiLog(log);
      saveConfig();
    }
    finally {
      if(po != null) {
        try {
          h3cLoginService.logout(po.getUid());
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void delPolicy(SGRule rule, HLJVirtualFirewall firewall, int userId) throws Exception {
    int instanceId = Integer.parseInt(rule.getSgId().replace(SGID_PREFIX, ""));
    TInstanceInfoBO bo = instanceService.queryInstanceInfoById(instanceId);
    //HLJVirtualFirewall rootWall = HLJVirtualFirewallDao.getFirewallByUniqueIndex(Constants.ID_ROOT_FIREWALL, 0);
    String groupName = "";
    String serviceName = getCustomServiceName(firewall, rule, bo.getUserId());
    String srcIp = "";
    String dstIp = "";
    String vdId = "";
    if(rule.getSourceIp().contains("/")) { //流入
      vdId = String.format("%04x", firewall.getVirtualDeviceId());
      groupName = vdId + String.format("%04x", firewall.getUntrustZoneId()) + "-" + String.format("%04x", firewall.getVirtualDeviceId()) + String.format("%04x", firewall.getTrustZoneId());
      dstIp = publicIPDao.getPrivateIpByPublicIp(rule.getDestinationIp()) + "/0.0.0.0";
      srcIp = rule.getSourceIp();
    }
    else { //流出
      vdId = String.format("%04x", firewall.getVirtualDeviceId());
      groupName = vdId + String.format("%04x", firewall.getTrustZoneId()) + "-" + String.format("%04x", firewall.getVirtualDeviceId()) + String.format("%04x", firewall.getUntrustZoneId());
      dstIp = rule.getDestinationIp();
      srcIp = publicIPDao.getPrivateIpByPublicIp(rule.getSourceIp()) + "/0.0.0.0";
    }
    LoginResponsePo po = null;
    try {
      DeviceInfo device = deviceInfoDao.getDeviceInfoByType(DeviceType.FIREWALL).get(0);
      if(device == null) {
        throw new Exception("h3c : device not found by type " + DeviceType.FIREWALL.toString());
      }
      h3cLoginService.setUrlh3(device.getVirtualIp());
      h3cFirewllService.setUrlh3(device.getVirtualIp());
      po = h3cLoginService.login(device.getUsername(), device.getPassword(), "Skycloud");
      H3CApiLog log = new H3CApiLog();
      log.setUserId(userId);
      log.setCreateDate(new Date());
      log.setOperate("Delete policy : login " + device.getVirtualIp());
      if(po == null) {
        log.setResult(0);
        apiLogDao.createApiLog(log);
        throw new Exception("h3c : login failed");
      }
      log.setResult(1);
      apiLogDao.createApiLog(log);
      QueryVo vo = new QueryVo();
      vo.setCount("20000");
      vo.setOpt("start");
      vo.setValue(vdId);
      QueryAllInterZonePolicyResponse resp = h3cFirewllService.queryAllInterZonePolicy(vo, po.getUid());
      log.setCreateDate(new Date());
      log.setOperate("Delete policy : query all inter zone policy on vd " + vdId);
      if(resp == null || ! resp.executeIsSuccess()) {
        log.setResult(0);
        apiLogDao.createApiLog(log);
        throw new Exception("h3c : " + resp.getErrorMsg());
      }
      log.setResult(1);
      apiLogDao.createApiLog(log);
      List<BaseInterZonePolicyRowRes> rows = resp.getAclTableRes().getRow();
      for(BaseInterZonePolicyRowRes row : rows) {
        if(! groupName.equals(row.getAclgroupname())) {
          continue;
        }
        if(! serviceName.equals(row.getObjectname())) {
          continue;
        }
        if(! srcIp.equals(row.getSrcobj())) {
          continue;
        }
        if(! dstIp.equals(row.getDesobj())) {
          continue;
        }
        if(rule.getAccess() != Integer.parseInt(row.getFilter())) {
          continue;
        }
        BaseInterCreatePolicyRowRes req = new BaseInterCreatePolicyRowRes();
        req.setAclgroupname(groupName);
        req.setAclid(row.getAclid());
        req.setAcltype("1");
        Response delResp = h3cFirewllService.deleteInterZonePolicy(req, po.getUid());
        log.setCreateDate(new Date());
        log.setOperate("Delete policy : delete inter zone policy(from " + row.getSrcobj() + " to " + row.getDesobj() + ")");
        if(delResp == null || ! delResp.executeIsSuccess()) {
          log.setResult(0);
          apiLogDao.createApiLog(log);
          throw new Exception("h3c : " + delResp.getErrorMsg());
        }
        log.setResult(1);
        apiLogDao.createApiLog(log);
        break;
      }
      saveConfig();
    }
    finally {
      if(po != null) {
        try {
          h3cLoginService.logout(po.getUid());
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void saveConfig() throws Exception {
    DeviceInfo device = deviceInfoDao.getDeviceInfoByType(DeviceType.FIREWALL).get(0);
    h3cLoginService.setUrlh3(device.getVirtualIp());
    h3cSysManageService.setUrlh3(device.getVirtualIp());
    LoginResponsePo po = null;
    try {
      po = h3cLoginService.login(device.getUsername(), device.getPassword(), "Skycloud");
      if(po != null) {
        h3cSysManageService.saveConfig(po.getUid());
      }
    }
    finally {
      if(po != null) {
        try {
          h3cLoginService.logout(po.getUid());
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public List<String> getPublicIpByUser(int userId) throws Exception {
    if(userId < 1) {
      throw new Exception("Paramater is invalid");
    }
    List<H3CIP> list = H3CIPDao.getPublicIpByUser(userId);
    List<String> result = null;
    if(list != null && list.size() > 0) {
      result = new ArrayList<String>();
      for(H3CIP ip : list) {
        result.add(ip.getPublicIp());
      }
    }
    return result;
  }

  @Override
  public List<String> getLocalIpByUser(int userId) throws Exception {
    if(userId < 1) {
      throw new Exception("Paramater is invalid");
    }
    List<H3CIP> list = H3CIPDao.getPublicIpByUser(userId);
    List<String> result = null;
    if(list != null && list.size() > 0) {
      result = new ArrayList<String>();
      for(H3CIP ip : list) {
        if(! result.contains(ip.getLocalIp())) {
          result.add(ip.getLocalIp());
        }
      }
    }
    return result;
  }

  public void setSGRuleDao(ISGRuleDao ruleDao) {
    SGRuleDao = ruleDao;
  }

  public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
    this.asyncJobVDCService = asyncJobVDCService;
  }

  public void setInstanceService(IInstanceService instanceService) {
    this.instanceService = instanceService;
  }

  public void setH3CIPDao(IH3CIPDao dao) {
    H3CIPDao = dao;
  }

  public void setSGCustomServiceDao(ISGCustomServiceDao customServiceDao) {
    SGCustomServiceDao = customServiceDao;
  }

  public void setH3cLoginService(H3cLoginService h3cLoginService) {
    this.h3cLoginService = h3cLoginService;
  }

  public void setH3cFirewllService(H3cPolicyService h3cFirewllService) {
    this.h3cFirewllService = h3cFirewllService;
  }

  public void setH3cCustomService(H3cCustomServices h3cCustomService) {
    this.h3cCustomService = h3cCustomService;
  }

  public void setDeviceInfoDao(IDeviceInfoDao deviceInfoDao) {
    this.deviceInfoDao = deviceInfoDao;
  }

  public void setHLJVirtualFirewallDao(IHLJVirtualFirewallDao virtualFirewallDao) {
    HLJVirtualFirewallDao = virtualFirewallDao;
  }

  public void setPublicIPDao(IPublicIPDao publicIPDao) {
    this.publicIPDao = publicIPDao;
  }

  public void setApiLogDao(IH3CApiLogDao apiLogDao) {
    this.apiLogDao = apiLogDao;
  }

  public void setH3cSysManageService(SysManageService sysManageService) {
    h3cSysManageService = sysManageService;
  }

}
