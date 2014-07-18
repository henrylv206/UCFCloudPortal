package com.skycloud.management.portal.front.instance.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TVirtualServiceBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public interface IInstanceService {

  public List<ResourcesVO> queryInstanceInfoByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;
  
  public List<ResourcesVO> queryVMInfoByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  public List<ResourcesVO> queryInstanceInfoByUserId(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  /**
   * 根据Id更新IntanceInfo表对象状态
   * 
   * @param id
   *          , sate
   * @throws ServiceException
   *           创建人： 刘江宁 创建时间：2011-11-28 上午10:47:14
   */
  public void updateTScsIntanceInfoStateById(int id, int sate) throws ServiceException;

  /**
   * 根据Id回滚IntanceInfo表对象状态
   * 
   * @param id
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-10-9 下午16:12:11
   */
  public void rollbackIntanceInfoStateById(int id) throws ServiceException;

  /**
   * 根据Id更新IntanceInfo表对象状态,回写ResId
   * 
   * @param id
   * @param sate
   * @param resId
   * @throws ServiceException
   *           创建人： 刘江宁 创建时间：2011-12-22 下午01:19:39
   */
  public void updateTScsIntanceInfoStateResIdById(int id, int sate, int resId) throws ServiceException;

  int queryInstanceCountByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  int checkDiskBind(int instanceID, int userID) throws SCSException;

  public TInstanceInfoBO queryInstanceInfoById(int insId) throws Exception;

  /**
   * 增加存储信息从elaster读取，完成资源elaster删除,运营管理平台显示正确
   * 
   * @author fengyk
   * @param list
   * @return
   * @throws Exception
   *           创建时间： 2012-1-13 11:11:11
   */
  List<ResourcesVO> queryVolumeInstanceFromElaster(List<ResourcesVO> list) throws Exception;

  /**
   * 异步JOB状态回写 根据Id更新IntanceInfo表对象状态以及RESCODE
   * 
   * @param insid
   *          , state, rescode
   * @throws ServiceException
   *           创建人： 齐萌硕 创建时间：2012-03-16 下午14:24
   */
  public int updateIntanceInfoJob(int insID, int state, String resCode) throws ServiceException;

  /**
   * 根据实例id，更新T_SCS_INSTANCE_INFO表对象状态，回写RESOURCE_INFO信息
   * 
   * @param id
   *          实例id
   * @param state
   *          实例状态
   * @param resourceInfo
   *          RESOURCE_INFO信息
   * @throws DataAccessException
   *           创建人： 甘坤彪 创建时间：2012-3-16 下午02:53:57
   */
  public void updateTScsInstanceInfoStateAndResourceInfoById(int id, int state, String resourceInfo) throws SCSException;

  /**
   * 该方法 还得讨论 资源修改功能，资源池接口成功返回后调用该方法
   * 
   * @param infoBO
   * @throws SCSException
   *           创建人： 张爽 创建时间：2012-3-16 下午05:35:36
   */
  public void updateTScsInstanceInfo(TInstanceInfoBO infoBO) throws SCSException;

  public List<ResourcesVO> queryInstanceInfoByUserForNet(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  public TVirtualServiceBO queryVirtualServiceByUser(ResourcesQueryVO vo) throws SCSException;

  public Map<Integer, TPublicIPBO> queryBindVM() throws SCSException;

  public List<ResourcesVO> queryVMSByUser(ResourcesQueryVO vo) throws Exception;

  public int queryCountVMSByUser(ResourcesQueryVO vo) throws Exception;

  public int updateTScsIntanceInfoByVMModify(TInstanceInfoBO infoBO) throws SCSException;

  public List<TInstanceInfoBO> queryInstanceInfoByOrderId(int orderId) throws SCSException;

  /**
   * 根据资源实例id(例如虚机id)查询其所属的资源池id
   * 
   * @param instanceId
   * @return
   * @throws Exception
   * @author zhanghuizheng
   */
  int getResourcePoolIdByInstanceId(int instanceId) throws Exception;

  /**
   * 
   * @param iriID
   * @return
   * @throws Exception
   */

  int queryResourcePoolIDByIRIID(int iriID) throws Exception;

  /**
   * 查询可用的VM，小型机，物理机
   * 
   * @author CQ
   */
  List<ResourcesVO> queryInstanceInfoExist(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  List<ResourcesVO> queryInstanceInfo4Bind(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  /**
   * 根据ID查询资源池
   * 
   * @param resourcePoolsId
   * @return
   * @throws SCSException
   */
  public TResourcePoolsBO searchResourcePoolsById(int resourcePoolsId) throws SCSException;

  public String searchIpSanURLByResourcePoolsId(int resourcePoolsId) throws SCSException;

  public List<ResourcesVO> searchInstanceInfoByIds(List<Integer> insIds) throws SCSException;
}
