/**
 * 2012-2-1  下午01:50:10  $Id:shixq
 */
package com.skycloud.management.portal.front.task.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao;

/**
 * @author shixq
 * @version $Revision$ 下午01:50:10
 */
public class TaskSyncElasterDelVMState {

  private static Log log = LogFactory.getLog(TaskSyncElasterDelVMState.class);
  private final static String algorithm = ConfigManager.getInstance().getString("elaster.poll.algorithm");
  private VirtualMachineListDao vmListDao;
  private IInstanceInfoDao instanceInfoDao;
  private ISysParametersService parametersService;
  private IResourcePoolsDao resourcePoolsDao;

  private int[] updateVMStateMdf(List<TInstanceInfoBO> listMdfState) throws SCSException {
    return instanceInfoDao.updateVMStateMdf(listMdfState);
  }

  /**
   * 同步后台VM状态
   */
  public void updateVMState4Elaster() {
    List<TInstanceInfoBO> listMdfState = new ArrayList<TInstanceInfoBO>();
    Integer projectId = parametersService.getCurProjectId();
    try {
      if (projectId != null && projectId > -1 && projectId == ConstDef.ProjectId.SkyFormOpt.getCode()) {
        List<TResourcePoolsBO> resourcePoolsList = resourcePoolsDao.searchAllPools();
        if (resourcePoolsList != null && resourcePoolsList.size() > 0) {
          for (TResourcePoolsBO resourcePoolsBO : resourcePoolsList) {
            int resourcePoolsID = resourcePoolsBO.getId();
            if (resourcePoolsID > 0) {
              List<ResourcesVO> vmInstances4SkyCloud;
              try {
                vmInstances4SkyCloud = vmListDao.queryVMList(resourcePoolsID);
                if (algorithm != null && !"".equals(algorithm) && algorithm.equals("true")) {
                  JSONObject resultObject = null;
                  resultObject = vmListDao.instanceInfoList4Elaster(resourcePoolsID);
                  if (resultObject != null && !resultObject.isEmpty()) {
                    JSONArray resultArray = (JSONArray) resultObject.getJSONArray("virtualmachine");
                    Map<String, String> vmInstances4Elaster = new HashMap<String, String>();
                    for (int index = 0; index < resultArray.size(); index++) {
                      JSONObject result = resultArray.getJSONObject(index);
                      String state = String.valueOf(result.get("state"));
                      String id = String.valueOf(result.get("id"));
                      vmInstances4Elaster.put(id, state);
                    }
                    for (ResourcesVO vo : vmInstances4SkyCloud) {
                      String elasterVMID = vo.getE_instance_id();
                      int instanceID = vo.getId();
                      String stateInstance = vo.getState();
                      String stateElaster = vmInstances4Elaster.get(elasterVMID);
                      TInstanceInfoBO instance = new TInstanceInfoBO();
                      instance.setId(instanceID);
                      if (StringUtils.isBlank(stateElaster) && StringUtils.isNotBlank(stateInstance) && !stateInstance.equals("4")) {
                        instance.setState(4);
                        listMdfState.add(instance);
                        log.info("update instance VM delete State for Elaster,instance is " + instanceID + ",elasterVMID is " + elasterVMID
                            + ",stateInstance is " + stateInstance + ",stateElaster is " + stateElaster);
                      } else if (StringUtils.isNotBlank(stateElaster) && StringUtils.isNotBlank(stateInstance)
                          && stateElaster.equals("Running") && !stateInstance.equals("2")) {
                        instance.setState(2);
                        listMdfState.add(instance);
                        log.info("update instance VM running State for Elaster,instance is " + instanceID + ",elasterVMID is "
                            + elasterVMID + ",stateInstance is " + stateInstance + ",stateElaster is " + stateElaster);
                      } else if (StringUtils.isNotBlank(stateElaster) && StringUtils.isNotBlank(stateInstance)
                          && stateElaster.equals("Stopped") && !stateInstance.equals("5")) {
                        instance.setState(5);
                        listMdfState.add(instance);
                        log.info("update instance VM powoff State for Elaster,instance is " + instanceID + ",elasterVMID is " + elasterVMID
                            + ",stateInstance is " + stateInstance + ",stateElaster is " + stateElaster);
                      }
                    }
                  }
                  int[] index = updateVMStateMdf(listMdfState);
                  log.info("async job update index=" + index.length);
                } else if (algorithm != null && !equals(algorithm) && algorithm.equals("false")) {

                } else {

                }
              } catch (SCSException e) {
                log.error(e);
              }

            }
          }
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public VirtualMachineListDao getVmListDao() {
    return vmListDao;
  }

  public void setVmListDao(VirtualMachineListDao vmListDao) {
    this.vmListDao = vmListDao;
  }

  public IInstanceInfoDao getInstanceInfoDao() {
    return instanceInfoDao;
  }

  public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
    this.instanceInfoDao = instanceInfoDao;
  }

  public void setParametersService(ISysParametersService parametersService) {
    this.parametersService = parametersService;
  }

  public IResourcePoolsDao getResourcePoolsDao() {
    return resourcePoolsDao;
  }

  public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
    this.resourcePoolsDao = resourcePoolsDao;
  }

}
