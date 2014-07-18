package com.skycloud.management.portal.front.order.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TVirtualServiceBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.enumtype.InstanceState;
import com.skycloud.management.portal.front.resources.enumtype.ServiceType;

public interface IInstanceInfoDao {

  /**
   * 添加实例记录到实例表
   * 
   * @param instanceInfo
   *          ：实例对象
   * @throws SCSException
   *           创建人：何福康 创建时间：2011-11-10 上午11:20:24
   */
  int save(TInstanceInfoBO instanceInfo) throws SCSException;

  /**
   * 根据实例Id删除IntanceInfo表记录
   * 
   * @param Id
   *          ：实例Id
   * @throws SQLException
   *           创建人：何福康 创建时间：2011-11-10 上午11:20:24
   */
  int delete(int Id) throws SQLException;

  /**
   * 修改实例表IntanceInfo记录
   * 
   * @param instanceInfo
   * @throws SCSException
   *           创建人：何福康 创建时间：2011-11-10 上午11:20:24
   */
  int update(TInstanceInfoBO instanceInfo) throws SCSException;

  int updateServiceState(int serviceID) throws SCSException;

  int updateServiceState4(int serviceID) throws SCSException;

  /**
   * 根据实例Id查询实例表记录
   * 
   * @param ID
   *          ：实例Id
   * @throws SCSException
   *           创建人：何福康 创建时间：2011-11-10 上午11:20:24
   */
  TInstanceInfoBO searchInstanceInfoByID(int ID) throws SCSException;

  TServiceInstanceBO searchServiceInstanceById(int ID) throws SCSException, SQLException;

  TServiceInstanceBO findServiceInstanceById(int ID) throws SCSException, SQLException;

  /**
   * 查询所有实例IntanceInfo表记录
   * 
   * @param 无
   * @throws SCSException
   *           创建人：何福康 创建时间：2011-11-30 上午11:20:24
   */
  List<TInstanceInfoBO> searchAllInstanceInfo() throws SCSException;

  /**
   * 查询最新插入的实例Id
   * 
   * @param 无
   * @throws SQLException
   *           创建人：何福康 创建时间：2011-11-10 上午11:20:24
   */
  int searchLastId() throws SQLException;

  /**
   * 根据Id删除IntanceInfo表记录
   * 
   * @param vo
   * @throws SCSException
   *           创建人：冯永凯 创建时间：2011-11-20 上午11:20:24
   */
  List<ResourcesVO> queryInstanceInfoByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  List<ResourcesVO> queryInstanceInfoByUserId(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  /**
   * 获取当前用户实例信息总条数
   * 
   * @param vo
   * @throws SCSException
   *           创建人：冯永凯 创建时间：2011-11-30 上午11:20:24
   */
  int queryInstanceCountByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  int checkDiskBind(int instanceID, int userID) throws SCSException;

  /**
   * 获取当前用户nics信息
   * 
   * @param user
   * @throws SCSException
   *           创建人：冯永凯 创建时间：2011-11-30 上午11:20:24
   */
  List<TNicsBO> vmVlanInfos(final TUserBO user) throws SCSException;

  /**
   * 根据Id更新IntanceInfo表对象状态
   * 
   * @param id
   *          , sate
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-11-28 上午10:47:14
   */
  public void updateTScsIntanceInfoStateById(int id, int sate) throws DataAccessException;

  /**
   * 根据Id回滚IntanceInfo表对象状态
   * 
   * @param id
   * @throws DataAccessException
   */
  public void rollbackIntanceInfoStateById(int id) throws DataAccessException;

  /**
   * 根据Id更新IntanceInfo表对象状态，回写ResId
   * 
   * @param id
   * @param sate
   * @param resId
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-12-22 下午01:17:02
   */
  public void updateTScsIntanceInfoStateResIdById(int id, int sate, int resId) throws DataAccessException;

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
   *           创建人： 甘坤彪 创建时间：2012-3-16 下午02:43:46
   */
  public void updateTScsInstanceInfoStateAndResourceInfoById(int id, int state, String resourceInfo) throws SCSException;

  /**
   * 根据订单Id删除实例表记录
   * 
   * @param orderId
   * @throws SQLException
   *           创建人：何福康 创建时间：2011-11-30 上午11:20:24
   */
  int deleteByOrderId(int orderId) throws SQLException;

  /**
   * 根据实例名和用户ID查询实例是否存在
   * 
   * @param InstanceInfo
   *          :instanceName Order:createUserId return ret_val >0 实例名称已经存在
   * @throws SQLException
   *           创建人：何福康 创建时间：2011-12-19 上午11:20:24
   */
  int searchInstanceInfoByInstanceNameAndUserId(int createUserId, String instanceName) throws SCSException;

  /**
   * 根据用户ID查询该用户申请的VM
   */
  List<TInstanceInfoBO> searchInstanceInfoByUserId(int createUserId, ServiceType serviceType, List<InstanceState> instanceState,
      int resourceDomain) throws SCSException;

  List<TInstanceInfoBO> searchInstanceAppliedByUserId(int createUserId, ServiceType serviceType, List<InstanceState> instanceState,
      int resourceDomain) throws SCSException;

  List<TInstanceInfoBO> searchInstanceAppliedByNetworkId(final long networkId, final int resourceDomain) throws SCSException;

  /**
   * 根据虚机实例ID查询弹性存储实例信息
   * 
   * @param vmInstanceId
   *          return TInstanceInfoBO 弹性存储实例信息
   * @throws SQLException
   *           创建人：何福康 创建时间：2011-12-19 上午13:33:24
   */
  List<TInstanceInfoBO> searchEBSInstanceInfosByVMInstanceId(int vmInstanceId) throws SCSException;

  /**
   * 恢复实例IntanceInfo表对象状态 修改单或删除单被删除时启用实例状态恢复 操作过程:clusterId->State
   * 
   * @param instanceInfoId
   *          :实例id
   * @throws DataAccessException
   *           创建人： 何福康 创建时间：2011-12-20 上午11:17:10
   */
  public void recoveryIntanceInfoStateById(int instanceInfoId) throws DataAccessException;

  /**
   * 根据Elaster删除的虚机同步删除状态
   * 
   * @param instanceID
   * @return
   * @throws SCSException
   */

  int updateVMState4Elaster(int instanceID) throws SCSException;

  /**
   * 查询模板(templateId)在实例中的个数
   * 
   * @param templateId
   * @return
   * @throws SCSException
   *           创建人： 何福康 创建时间：2012-2-3 下午02:30:18
   */
  // int searchInstanceCountByTemplateId(int templateId,int type) throws
  // SCSException;

  /**
   * 查询实例中的产品个数
   * 
   * @param productId
   * @return
   * @throws SCSException
   *           创建人： 何福康 创建时间：2012-2-21 下午06:42:19
   */
  int searchInstanceCountByProductId(int productId) throws SCSException;

  /**
   * 查找可用的公网IP 供带宽使用
   * 
   * @return
   * @throws SQLException
   *           创建人： 张爽 创建时间：2012-3-14 下午03:38:49
   */
  List<TInstanceInfoBO> findUsableIpInstance(int userId) throws SQLException;

  /**
   * 查找改ip是否已经开通带宽
   * 
   * @param ipAddress
   * @return
   * @throws SQLException
   *           创建人： 张爽 创建时间：2012-3-14 下午07:05:00
   */
  int findIPAddressExist(String ipAddress, int templateType, int userId) throws SQLException;

  /**
   * 供带宽申请修改和申请作废用，用于这两种订单和instance表关联方向不同，不能用searchInstanceInfoByID这个方法
   * 
   * @param instanceId
   * @return
   * @throws SQLException
   *           创建人： 张爽 创建时间：2012-3-15 下午04:41:09
   */
  TInstanceInfoBO findInstanceInfoById(int instanceId) throws SQLException;

  /**
   * 异步JOB状态回写 根据Id更新IntanceInfo表对象状态以及RESCODE
   * 
   * @param insid
   *          , state, rescode
   * @throws ServiceException
   *           创建人： 齐萌硕 创建时间：2012-03-16 下午14:24
   */
  public int updateTScsIntanceInfoStateandRescodeById(int insid, int state, String rescode) throws DataAccessException;

  /**
   * 该方法 还得讨论 资源修改功能，资源池接口成功返回后调用该方法
   * 
   * @param infoBO
   * @throws SCSException
   *           创建人： 张爽 创建时间：2012-3-16 下午05:35:36
   */
  public void updateTScsInstanceInfo(TInstanceInfoBO infoBO) throws SCSException;

  public List<String> queryLoadBalanceWithVirtualServer();

  /**
   * 虚服务
   * 
   * @param infoBO
   * @throws SCSException
   *           创建人： CQ 创建时间：2012-5-17
   */
  public TVirtualServiceBO queryVirtualServiceByUser(int userId) throws SCSException;

  /**
   * 查找改ip是否已经购买
   * 
   * @param ipAddress
   * @return
   * @throws SQLException
   *           创建人： CQ 创建时间：2012-5-20
   */
  public int findIPAddressExist(String ipAddress, int templateType) throws SQLException;

  /**
   * 查找已经被绑定的虚拟机
   * 
   * @return
   * @throws SQLException
   *           创建人： CQ 创建时间：2012-5-20
   */
  public List<TPublicIPBO> queryBindVM() throws SCSException;

  public int updateTScsIntanceInfoByVMModify(TInstanceInfoBO infoBO) throws SCSException;

  public List<TInstanceInfoBO> queryInstanceInfoByOrderId(int orderId) throws SCSException;

  /**
   * 1.2新UI用到的查询 1.3支持查询同一资源池和同一资源域下的可用的公网ip
   * 
   * @param userId
   * @return
   * @throws SQLException
   */
  public List<TInstanceInfoBO> findUsableIpInstance2(final int userId, int resourcePoolsId, int zoneId) throws SQLException;

  /**
   * 根据服务实例id查询资源实例
   * 
   * @param serviceInstanceId
   * @return
   * @throws SCSException
   */
  public List<TInstanceInfoBO> searchInstanceInfoByServiceInstanceId(int serviceInstanceId) throws SCSException;

  int[] updateVMStateMdf(List<TInstanceInfoBO> listMdfState) throws SCSException;

  /**
   * 根据资源实例id(例如虚机id)查询其所属的资源池id
   * 
   * @author zhanghuizheng
   */
  int getResourcePoolIdByInstanceId(int instanceId) throws Exception;

  /**
   * 查询可用的VM，小型机，物理机
   * 
   * @author CQ
   */
  List<ResourcesVO> queryInstanceInfoExist(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  List<ResourcesVO> queryInstanceInfo4Bind(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException;

  /**
   * 
   * @param iriID
   * @return
   * @throws Exception
   */

  int queryResourcePoolIDByIRIID(int iriID) throws Exception;
  
  public List<ResourcesVO> searchInstanceInfoByIds(List<Integer> insIds) throws SCSException;

}
