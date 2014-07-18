package com.skycloud.management.portal.front.instance.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IResourcePoolsService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ListVolumes;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.INicsDao;
import com.skycloud.management.portal.front.order.dao.IProductInstanceRefDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TVirtualServiceBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.service.IPublicIPService;
import com.skycloud.management.portal.task.vdc.util.TaskUtils;

public class InstanceServiceImpl implements IInstanceService {

  protected final Logger logger = Logger.getLogger(InstanceServiceImpl.class);

  private IInstanceInfoDao instanceInfoDao;

  private IProductInstanceRefDao instanceInfoRefDao;

  private ICommandService commandService;

  private IPublicIPService publicIpService;

  private INicsDao nicsDao;

  private IResourcePoolsService resourcePoolsService;

  /*
   * (non-Javadoc)
   *
   * @see
   * com.skycloud.management.portal.front.instance.service.IInstanceService#
   * queryInstanceCountByUser
   * (com.skycloud.management.portal.front.resources.action
   * .vo.ResourcesQueryVO)
   */
  @Override
  public int queryInstanceCountByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    return instanceInfoDao.queryInstanceCountByUser(vo, resourcePoolsId, zoneId);
  }

  @Override
  public int checkDiskBind(int instanceID, int userID) throws SCSException {
    return instanceInfoDao.checkDiskBind(instanceID, userID);
  }

  private Map<String, List<TNicsBO>> getNicsMap(TUserBO user) {
    Map<String, List<TNicsBO>> nicsMap = new HashMap<String, List<TNicsBO>>();
    try {
      List<TNicsBO> listNics = instanceInfoDao.vmVlanInfos(user);
      List<TNicsBO> list = new ArrayList<TNicsBO>();
      for (TNicsBO nics : listNics) {
        String vmid = String.valueOf(nics.getVmInstanceInfoId());
        if (nicsMap.containsKey(vmid)) {
          list = nicsMap.get(vmid);
        } else {
          list = new ArrayList<TNicsBO>();
          nicsMap.put(vmid, list);
        }
        list.add(nics);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return nicsMap;
  }

  @Override
  public List<ResourcesVO> queryInstanceInfoByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    List<ResourcesVO> instanceinfos = null;
    List<ResourcesVO> instanceinfolist = new ArrayList<ResourcesVO>();
    try {
      instanceinfos = instanceInfoDao.queryInstanceInfoByUser(vo, resourcePoolsId, zoneId);
      for (ResourcesVO infos : instanceinfos) {
        if (infos.getE_instance_id().equals("0")) {
        } else {
          if ((vo.getOperateSqlType() == 1 || vo.getOperateSqlType() == 3) && !"7".equals(infos.getState())) {// bug4615
                                                                                                              // bug4268查询虚拟机网络资源时需过滤创建失败的虚拟机
            Map<String, List<TNicsBO>> nicsMap = this.getNicsMap(vo.getUser());
            List<TNicsBO> nicsList = nicsMap.get(String.valueOf(infos.getId()));
            String ipAddress = publicIpService.getVMIPAddress(Integer.valueOf(infos.getE_instance_id()), infos.getResourcePoolsId());
            if (nicsList != null) {
              nicsList.get(0).setIp(ipAddress);
            }
            infos.setNicsBOs(nicsList);
          }
        }
        instanceinfolist.add(infos);
      }
    } catch (SCSException e) {
      throw e;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return instanceinfolist;
  }

  @Override
public List<ResourcesVO> queryVMInfoByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
	    List<ResourcesVO> instanceinfos = null;
	    List<ResourcesVO> instanceinfolist = new ArrayList<ResourcesVO>();
	    try {
	      instanceinfos = instanceInfoDao.queryInstanceInfoByUser(vo, resourcePoolsId, zoneId);
	      for (ResourcesVO infos : instanceinfos) {
	        if (infos.getE_instance_id().equals("0")) {
	        } else {
	          if ((vo.getOperateSqlType() == 1 || vo.getOperateSqlType() == 3) && !"7".equals(infos.getState())) {

	            Map<String, List<TNicsBO>> nicsMap = this.getNicsMap(vo.getUser());
	            List<TNicsBO> nicsList = nicsMap.get(String.valueOf(infos.getId()));
	          }
	        }
	        instanceinfolist.add(infos);
	      }
	    } catch (SCSException e) {
	      throw e;
	    } catch (Exception e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    return instanceinfolist;
	  }

  @Override
  public List<ResourcesVO> queryInstanceInfoByUserForNet(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    List<ResourcesVO> instanceinfos = null;
    try {
      instanceinfos = instanceInfoDao.queryInstanceInfoByUser(vo, resourcePoolsId, zoneId);

      if ((vo.getOperateSqlType() == 1 || vo.getOperateSqlType() == 3)) {
        List<TNicsBO> nics = instanceInfoDao.vmVlanInfos(vo.getUser());
        List<TNicsBO> vlan = null;
        for (ResourcesVO infos : instanceinfos) {
          // 可用的vm
          if ("2,5,6".contains(infos.getState())) {
            vlan = new ArrayList<TNicsBO>();
            for (TNicsBO tNicsBO : nics) {
              if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
                vlan.add(tNicsBO);
              }
            }
            infos.setNicsBOs(vlan);
          }
        }
      }

    } catch (SCSException e) {
      throw e;
    }
    return instanceinfos;
  }

  @Override
  public List<ResourcesVO> queryInstanceInfoExist(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    List<ResourcesVO> instanceinfos = null;
    try {
      instanceinfos = instanceInfoDao.queryInstanceInfoExist(vo, resourcePoolsId, zoneId);
    } catch (SCSException e) {
      throw e;
    }
    return instanceinfos;
  }

  @Override
  public Map<Integer, TPublicIPBO> queryBindVM() throws SCSException {
    List<TPublicIPBO> list = instanceInfoDao.queryBindVM();
    Map<Integer, TPublicIPBO> map = new HashMap<Integer, TPublicIPBO>();
    if (null != list && !list.isEmpty()) {
      for (TPublicIPBO vo : list) {
        map.put(vo.getInstanceInfoId(), vo);
      }
    }
    return map;
  }

  @Override
  public TVirtualServiceBO queryVirtualServiceByUser(ResourcesQueryVO vo) throws SCSException {
    return instanceInfoDao.queryVirtualServiceByUser(vo.getUser().getId());
  }

  @Override
  public List<ResourcesVO> queryInstanceInfoByUserId(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    List<ResourcesVO> instanceinfos = null;
    try {
      instanceinfos = instanceInfoDao.queryInstanceInfoByUserId(vo, resourcePoolsId, zoneId);
      if ((vo.getOperateSqlType() == 1 || vo.getOperateSqlType() == 3) && vo.getId() != null && !"".equals(vo.getId())) {
        List<TNicsBO> nics = instanceInfoDao.vmVlanInfos(vo.getUser());
        List<TNicsBO> vlan = null;
        for (ResourcesVO infos : instanceinfos) {
          vlan = new ArrayList<TNicsBO>();
          for (TNicsBO tNicsBO : nics) {
            if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
              vlan.add(tNicsBO);
            }
          }
          infos.setNicsBOs(vlan);
        }
      }

    } catch (SCSException e) {
      throw e;
    }
    return instanceinfos;
  }

  @Override
  public void updateTScsIntanceInfoStateById(int id, int sate) throws ServiceException {
    try {
      instanceInfoDao.updateTScsIntanceInfoStateById(id, sate);
    } catch (ServiceException e) {
      logger.error("更新IntanceInfo表状态失败[InstanceServiceImpl]方法updateTScsIntanceInfoStateById异常：", e);
      throw new ServiceException("更新IntanceInfo表状态失败[InstanceServiceImpl]方法updateTScsIntanceInfoStateById异常：" + e.getMessage());
    }
  }

  @Override
  public void rollbackIntanceInfoStateById(int id) throws ServiceException {
    try {
      instanceInfoDao.rollbackIntanceInfoStateById(id);
    } catch (ServiceException e) {
      logger.error("更新IntanceInfo表状态失败[InstanceServiceImpl]方法rollbackIntanceInfoStateById异常：", e);
      throw new ServiceException("更新IntanceInfo表状态失败[InstanceServiceImpl]方法rollbackIntanceInfoStateById异常：" + e.getMessage());
    }
  }

  @Override
  public void updateTScsIntanceInfoStateResIdById(int id, int sate, int resId) throws ServiceException {
    try {
      instanceInfoDao.updateTScsIntanceInfoStateResIdById(id, sate, resId);
    } catch (ServiceException e) {
      logger.error("[InstanceServiceImpl] method : updateTScsIntanceInfoStateResIdById Exception：", e);
      throw new ServiceException("[InstanceServiceImpl] method : updateTScsIntanceInfoStateResIdById Exception：" + e.getMessage());
    }
  }

  @Override
  public void updateTScsInstanceInfoStateAndResourceInfoById(final int id, final int state, final String resourceInfo) throws SCSException {
    try {
      instanceInfoDao.updateTScsInstanceInfoStateAndResourceInfoById(id, state, resourceInfo);
    } catch (ServiceException e) {
      logger.error("[InstanceServiceImpl] method : updateTScsInstanceInfoStateAndResourceInfoById Exception：", e);
      throw new SCSException(SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_DESC);
    }
  }

  @Override
  public List<ResourcesVO> queryVMSByUser(ResourcesQueryVO vo) throws Exception {
    List<ResourcesVO> instanceinfos = null;
    try {
      instanceinfos = instanceInfoRefDao.queryPICodeVMSByUser(vo);

      if ((vo.getOperateSqlType() == 1 || vo.getOperateSqlType() == 3)) {
        List<TNicsBO> nics = instanceInfoDao.vmVlanInfos(vo.getUser());
        List<TNicsBO> vlan = null;
        for (ResourcesVO infos : instanceinfos) {
          vlan = new ArrayList<TNicsBO>();
          for (TNicsBO tNicsBO : nics) {
            if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
              vlan.add(tNicsBO);
            }
          }
          infos.setNicsBOs(vlan);
        }
      }

    } catch (SCSException e) {
      throw new SCSException(String.format(e.getMessage()));
    }
    return instanceinfos;
  }

  @Override
  public int queryCountVMSByUser(ResourcesQueryVO vo) throws Exception {
    return instanceInfoRefDao.queryCountPICodeVMSByUser(vo.getUser().getId());
  }

  @Override
  public TInstanceInfoBO queryInstanceInfoById(int insId) throws Exception {
    TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(insId);
    return info;
  }

  @Override
  public List<ResourcesVO> queryVolumeInstanceFromElaster(List<ResourcesVO> list) throws Exception {
    try {
      for (ResourcesVO vo : list) {
        String instanceId = vo.getE_instance_id();
        String state = vo.getState();

        if (state != null && !"".equals(state) && instanceId != null && !"".equals(instanceId) && !"6".equals(state)) {
          String stateNew = "";
          ListVolumes volumeElasterList = new ListVolumes();
          volumeElasterList.setId(instanceId);
          Object response = commandService.executeAndJsonReturn(volumeElasterList, vo.getResourcePoolsId());
          JSONObject jsonRes = JSONObject.fromObject(response);
          String result = jsonRes.getString("listvolumesresponse");
          JSONObject resultObject = JSONObject.fromObject(result);

          logger.info("======start to query elaster state by" + instanceId + "" + ",state old is" + state);
          if (resultObject.isEmpty()) {
            stateNew = "-1";
            vo.setState(stateNew);
            logger.info("======end to query elaster state by" + instanceId + "" + ",state new is" + stateNew);
          }
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return list;
  }

  public IInstanceInfoDao getInstanceInfoDao() {
    return instanceInfoDao;
  }

  public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
    this.instanceInfoDao = instanceInfoDao;
  }

  public ICommandService getCommandService() {
    return commandService;
  }

  public void setCommandService(ICommandService commandService) {
    this.commandService = commandService;
  }

  public IProductInstanceRefDao getInstanceInfoRefDao() {
    return instanceInfoRefDao;
  }

  public void setInstanceInfoRefDao(IProductInstanceRefDao instanceInfoRefDao) {
    this.instanceInfoRefDao = instanceInfoRefDao;
  }

  @Override
  public int updateIntanceInfoJob(int insid, int state, String rescode) throws ServiceException {
    int index = 0;
    // TODO Auto-generated method stub
    try {
      index = instanceInfoDao.updateTScsIntanceInfoStateandRescodeById(insid, state, rescode);
    } catch (ServiceException e) {
      logger.error("更新IntanceInfo表失败[InstanceServiceImpl]方法updateTScsIntanceInfoStateandRescodeById异常：", e);
      throw new ServiceException("更新IntanceInfo表失败[InstanceServiceImpl]方法updateTScsIntanceInfoStateandRescodeById异常：" + e.getMessage());
    }
    return index;
  }

  @Override
  public void updateTScsInstanceInfo(TInstanceInfoBO infoBO) throws SCSException {
    try {
      instanceInfoDao.updateTScsInstanceInfo(infoBO);
    } catch (ServiceException e) {
      logger.error("更新IntanceInfo表失败[InstanceServiceImpl]方法updateTScsInstanceInfo异常：", e);
      throw new ServiceException("更新IntanceInfo表失败[InstanceServiceImpl]方法updateTScsInstanceInfo异常：" + e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.skycloud.management.portal.front.instance.service.IInstanceService#
   * updateTScsIntanceInfoByVMModify
   * (com.skycloud.management.portal.front.order.entity.TInstanceInfoBO)
   */
  @Override
  public int updateTScsIntanceInfoByVMModify(TInstanceInfoBO infoBO) throws SCSException {
    return instanceInfoDao.updateTScsIntanceInfoByVMModify(infoBO);
  }

  @Override
  public List<TInstanceInfoBO> queryInstanceInfoByOrderId(int orderId) throws SCSException {
    return instanceInfoDao.queryInstanceInfoByOrderId(orderId);
  }

  @Override
  public List<ResourcesVO> queryInstanceInfo4Bind(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    List<ResourcesVO> resList = instanceInfoDao.queryInstanceInfo4Bind(vo, resourcePoolsId, zoneId);
    if (resList != null && resList.size() > 0) {
      for (ResourcesVO resource : resList) {
        String targetIP = "";
        try {
          List<TNicsBO> nicsList = nicsDao.searchNicssByInstanceId(resource.getId());
          if (nicsList != null && nicsList.size() >= 1) {
            targetIP = nicsList.get(0).getIp();
          }
        } catch (SQLException e) {
          logger.error("查询块存储绑定的虚机小机物理机失败[InstanceServiceImpl]方法queryInstanceInfo4Bind异常：", e);
          throw new SCSException("查询块存储绑定的虚机小机物理机失败[InstanceServiceImpl]方法queryInstanceInfo4Bind异常：" + e.getMessage());
        }
        resource.setTargetIP(targetIP);
      }
    }
    return resList;
  }

  @Override
  public TResourcePoolsBO searchResourcePoolsById(int resourcePoolsId) throws SCSException {
    return resourcePoolsService.searchById(resourcePoolsId);
  }

  @Override
  public String searchIpSanURLByResourcePoolsId(int resourcePoolsId) throws SCSException {
    String ipsanURL = null;
    String REGEX_IPV4 = "(\\d{1,3}\\.){3}\\d{1,3}";
    Pattern pattern = Pattern.compile(REGEX_IPV4);
    TResourcePoolsBO resourcePool = this.searchResourcePoolsById(resourcePoolsId);
    String url = TaskUtils.getIpSanURL();
    ipsanURL = url;
    if (resourcePool != null) {
      String ip = resourcePool.getIp();
      Matcher matcher = pattern.matcher(url);
      if (matcher.find()) {
        ipsanURL = matcher.replaceFirst(ip);
      }
    }
    return ipsanURL;
  }

  public IPublicIPService getPublicIpService() {
    return publicIpService;
  }

  public void setPublicIpService(IPublicIPService publicIpService) {
    this.publicIpService = publicIpService;
  }

  /**
   * 根据资源实例id(例如虚机id)查询其所属的资源池id
   *
   * @author zhanghuizheng
   */
  @Override
  public int getResourcePoolIdByInstanceId(int instanceId) throws Exception {
    return this.instanceInfoDao.getResourcePoolIdByInstanceId(instanceId);
  }

  @Override
  public int queryResourcePoolIDByIRIID(int iriID) throws Exception {
    return instanceInfoDao.queryResourcePoolIDByIRIID(iriID);
  }

  @Override
public List<ResourcesVO> searchInstanceInfoByIds(List<Integer> insIds) throws SCSException {
	return instanceInfoDao.searchInstanceInfoByIds(insIds);
}

public INicsDao getNicsDao() {
    return nicsDao;
  }

  public void setNicsDao(INicsDao nicsDao) {
    this.nicsDao = nicsDao;
  }

  public IResourcePoolsService getResourcePoolsService() {
    return resourcePoolsService;
  }

  public void setResourcePoolsService(IResourcePoolsService resourcePoolsService) {
    this.resourcePoolsService = resourcePoolsService;
  }

}
