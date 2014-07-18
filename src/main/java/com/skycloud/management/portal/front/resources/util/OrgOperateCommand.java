package com.skycloud.management.portal.front.resources.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.command.res.CreateTemplate;
import com.skycloud.management.portal.front.command.res.DeleteTemplate;
import com.skycloud.management.portal.front.command.res.DeployVirtualMachine;
import com.skycloud.management.portal.front.command.res.DestroyVirtualMachine;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.VolumeResumeVO;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;

public class OrgOperateCommand {

  /**
   * 组织创建模板ajinfo
   * 
   * @param volumeResumeVO
   * @return
   */
  public AsyncJobInfo orgCreateTemplate(VolumeResumeVO volumeResumeVO) {

    AsyncJobInfo ajInfo = new AsyncJobInfo();
    ajInfo.setAPPLY_ID(0);
    ajInfo.setINSTANCE_INFO_ID(volumeResumeVO.getInstanceId());
    ajInfo.setJOBSTATE(1);
    Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    Map<String, Object> subMap = new HashMap<String, Object>();
    subMap.put(CreateTemplate.SNAPSHOTID, volumeResumeVO.getSnapshotId());
    subMap.put(CreateTemplate.NAME, ResourcesUtil.getresName());
    subMap.put(CreateTemplate.DISPLAYTEXT, ResourcesUtil.getresName());
    subMap.put(CreateTemplate.OSTYPEID, volumeResumeVO.getOsTypeId());
    map.put(CreateTemplate.COMMAND, subMap);
    String parameter = JsonUtil.getJsonString4JavaPOJO(map);
    ajInfo.setOPERATION(CreateTemplate.COMMAND);
    ajInfo.setPARAMETER(parameter);

    return ajInfo;

  }

  /**
   * 组织删除模板ajinfo
   * 
   * @param volumeResumeVO
   * @return
   */
  public AsyncJobInfo orgDeleteTemplate(VolumeResumeVO volumeResumeVO) {

    AsyncJobInfo ajInfo = new AsyncJobInfo();
    ajInfo.setAPPLY_ID(0);
    ajInfo.setINSTANCE_INFO_ID(volumeResumeVO.getInstanceId());
    ajInfo.setJOBSTATE(1);
    Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    Map<String, Object> subMap = new HashMap<String, Object>();
    subMap.put(DeleteTemplate.ID, "");
    map.put(DeleteTemplate.COMMAND, subMap);
    String parameter = JsonUtil.getJsonString4JavaPOJO(map);
    ajInfo.setOPERATION(DeleteTemplate.COMMAND);
    ajInfo.setPARAMETER(parameter);

    return ajInfo;

  }

  // 组织部署虚拟机命令ajInfo
  public List<AsyncJobInfo> orgDeployVirturlMachines(TInstanceInfoBO instanceInfoBO, String account, int intDomainId) {

    List<AsyncJobInfo> ajInfos = new ArrayList<AsyncJobInfo>();
    AsyncJobInfo ajInfo = new AsyncJobInfo();

    List<Long> networkIds = new ArrayList<Long>();
    List<TNicsBO> nics = instanceInfoBO.getNicsBOs();

    List<Map<String, Integer>> ipLists = new ArrayList<Map<String, Integer>>();
    //bugid 0005232
    if (nics != null && nics.size() > 0) {
      for (TNicsBO nic : nics) {
        if (nic.getVmInstanceInfoId() == instanceInfoBO.getId()) {
          networkIds.add(nic.geteVlanId());
          Map<String, Integer> ips = new HashMap<String, Integer>();
          ips.put(nic.getIp(), Integer.valueOf(String.valueOf(nic.geteVlanId())));
          ipLists.add(ips);
        }
      }
    }
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> subMap = new HashMap<String, Object>();
    subMap.put(DeployVirtualMachine.SERVICE_OFFERING_ID, instanceInfoBO.geteServiceId());
    subMap.put(DeployVirtualMachine.ZONE_ID, instanceInfoBO.getZoneId());

    subMap.put(DeployVirtualMachine.ACCOUNT, account);

    subMap.put(DeployVirtualMachine.TEMPLATE_ID, instanceInfoBO.getTemplateId());

    subMap.put(DeployVirtualMachine.DOMAIN_ID, String.valueOf(intDomainId));

    subMap.put(DeployVirtualMachine.NETWORK_IDS, networkIds);
    subMap.put(DeployVirtualMachine.DISPLAY_NAME, instanceInfoBO.getInstanceName());

    // 通过0判断是否ip通过dhcp生成
    if (ipLists != null && ipLists.size() > 0) {
      if (ipLists.get(0).containsKey("0")) {
        subMap.put("isCheck", "1");
        map.put("IP", "");
      } else {
        map.put("IP", ipLists);
      }
    }

    map.put(DeployVirtualMachine.COMMAND, subMap);

    String parameter = JsonUtil.getJsonString4JavaPOJO(map);
    ajInfo.setINSTANCE_INFO_ID(instanceInfoBO.getId());
    ajInfo.setOPERATION(DeployVirtualMachine.COMMAND);
    ajInfo.setPARAMETER(parameter);
    ajInfo.setJOBSTATE(1);
    ajInfo.setAPPLY_ID(0);
    ajInfos.add(ajInfo);

    if (map.containsValue("")) {
    } else {
      if ("0".equals(ConstDef.getDeployVmByNewAPI())) {// bug 0004359
        ajInfos.addAll(this.orgRebootCommand(nics, instanceInfoBO));
      }
    }
    return ajInfos;
  }

  /**
   * 组织销毁虚拟机ajInfo信息
   * 
   * @param instanceInfoBO
   * @return
   */
  public AsyncJobInfo orgDestroyVirturlMachines(TInstanceInfoBO instanceInfoBO) {
    AsyncJobInfo ajInfo = new AsyncJobInfo();

    DestroyVirtualMachine dvm = new DestroyVirtualMachine();
    dvm.setId((int) instanceInfoBO.geteInstanceId());
    String parameter = CommandCreateUtil.getJsonParameterStr(dvm);
    String operation = dvm.getCOMMAND();
    ajInfo.setINSTANCE_INFO_ID(instanceInfoBO.getId());
    ajInfo.setOPERATION(operation);
    ajInfo.setPARAMETER(parameter);
    ajInfo.setJOBSTATE(1);
    ajInfo.setAPPLY_ID(0);
    return ajInfo;
  }

  /**
   * 组织更改iri表状态ajInfo信息
   * 
   * @param instanceInfoBO
   * @return
   */
  public AsyncJobInfo orgupdateIriRecordState(TInstanceInfoBO instanceInfoBO) {
    AsyncJobInfo ajInfo = new AsyncJobInfo();

    String operation = "updateIriRecordStateIsUninstall"; // 更改iri状态 为vm快照恢复
    String parameter = "update iri state to Uninstall when resume vmvolume "; // 更改iri状态
    // 为vm快照恢复
    ajInfo.setINSTANCE_INFO_ID(instanceInfoBO.getId());
    ajInfo.setOPERATION(operation);
    ajInfo.setPARAMETER(parameter);
    ajInfo.setJOBSTATE(1);
    ajInfo.setAPPLY_ID(0);
    return ajInfo;
  }

  /**
   * 组织reboot命令
   * 
   * @param networkIds
   * @param info
   * @return List<AsyncJobInfo>
   */
  protected List<AsyncJobInfo> orgRebootCommand(List<TNicsBO> nics, TInstanceInfoBO instanceInfoBO) {
    List<AsyncJobInfo> ajInfos = new ArrayList<AsyncJobInfo>();
    if (nics != null && nics.size() > 0) {

      for (@SuppressWarnings("unused")
      TNicsBO nic : nics) {
        AsyncJobInfo ajInfo1 = new AsyncJobInfo();
        Map<String, Map<String, Object>> map1 = new HashMap<String, Map<String, Object>>();
        Map<String, Object> subMap1 = new HashMap<String, Object>();
        subMap1.put("id", "");
        map1.put("rebootRouter", subMap1);
        String parameter1 = JsonUtil.getJsonString4JavaPOJO(map1);
        ajInfo1.setINSTANCE_INFO_ID(instanceInfoBO.getId());
        ajInfo1.setOPERATION("rebootRouter");
        ajInfo1.setPARAMETER(parameter1);
        ajInfo1.setJOBSTATE(1);
        ajInfo1.setAPPLY_ID(0);
        ajInfos.add(ajInfo1);
      }
      AsyncJobInfo ajInfo = new AsyncJobInfo();
      Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
      Map<String, Object> subMap = new HashMap<String, Object>();
      subMap.put("id", "");
      map.put("rebootVirtualMachine", subMap);
      String parameter = JsonUtil.getJsonString4JavaPOJO(map);
      ajInfo.setINSTANCE_INFO_ID(instanceInfoBO.getId());
      ajInfo.setOPERATION("rebootVirtualMachine");
      ajInfo.setPARAMETER(parameter);
      ajInfo.setJOBSTATE(1);
      ajInfo.setAPPLY_ID(0);
      ajInfos.add(ajInfo);
    }
    return ajInfos;

  }

}
