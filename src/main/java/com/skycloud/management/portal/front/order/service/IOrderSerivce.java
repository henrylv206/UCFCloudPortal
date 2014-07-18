package com.skycloud.management.portal.front.order.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;

public interface IOrderSerivce {

	/**
	 * 保存新订单
	 *
	 * @param order
	 *            return falg -1失败，0成功
	 */
	public int saveOrder(TOrderBO order) throws Exception;

	/**
	 * 保存实例
	 *
	 * @param instanceInfo
	 *            return falg -1失败，0成功
	 */
	public int saveInstanceInfo(TInstanceInfoBO instanceInfo) throws Exception;

	/**
	 * 保存实例订单
	 *
	 * @param TOrderBO
	 *            ,TInstanceInfoBO,TNicsBO return falg -1失败，0成功
	 */
	public int insertOrder(TOrderBO order, List<TInstanceInfoBO> instanceInfo) throws Exception;

	/**
	 * 获取最新插入的一条订单记录
	 *
	 * @return TOrderBO
	 */
	public int selectLastOrderId() throws Exception;

	/**
	 * 获取最新插入的一条实例记录
	 *
	 * @return TInstanceInfoBO
	 */
	public int selectLastInstanceInfoId() throws Exception;

	/**
	 * 保存实例的网络信息(vlan,ipaddress)
	 *
	 * @param nics
	 * @return flag > 0成功， -1失败
	 */
	public int saveNics(TNicsBO nics) throws Exception;

	/**
	 * 删除新订单实例的网络信息(vlan,ipaddress)
	 *
	 * @param nics
	 * @return flag > 0成功， -1失败
	 */
	public int deleteNewOrderByid(int orderId, int orderType, int instanceInfoId) throws Exception;

	public int deleteOrderByid(TOrderBO order) throws Exception;

	/**
	 * 获取订单记录
	 *
	 * @param orderId
	 * @return TOrderBO
	 */
	public TOrderBO selectOrderByOrderId(int orderId) throws Exception;

	/**
	 * 查询当前用户的实例名称是否重名
	 *
	 * @param instanceName
	 *            ：实例名称,createUserId：用户Id
	 * @return int > 0 重名
	 */
	public int searchInstanceInfoByInstanceNameAndUserId(int createUserId, String instanceName) throws SCSException;

	/**
	 * 根据虚机实例ID查询弹性存储实例信息
	 *
	 * @param vmInstanceId
	 *            return TInstanceInfoBO 弹性存储实例信息
	 * @throws SQLException
	 *             创建人：何福康 创建时间：2011-12-19 上午13:33:24
	 */
	public List<TInstanceInfoBO> searchEBSInstanceInfosByVMInstanceId(int vmInstanceId) throws SCSException;

	/**
	 * 根据参数配置表的类型查询参数对应的值
	 *
	 * @param type
	 *            类型 return int 参数值value
	 * @throws SQLException
	 *             创建人：何福康 创建时间：2012-01-04 下午16:14:00
	 */
	public int searchParametersValueByType(String type) throws SCSException;

	List<TOrderBO> searchOrders(PageVO vo, TUserBO user, QueryCriteria query) throws Exception;

	List<TOrderBO> searchOrderServices(PageVO vo, TUserBO user, QueryCriteria query) throws Exception;
	List<TOrderBO> searchOrderServices2(PageVO vo, TUserBO user, QueryCriteria query, int orderType) throws Exception;

	int searchOrdersAmount(TUserBO user, QueryCriteria query) throws SQLException;

	List<TOrderBO> searchOrdersByInstanceId(int instanceId) throws SQLException;


	/**
	 * 修改订单
	 * @author ninghao@chinaskycloud.com 2012-12-19
	 * @param vminfos
	 * @return
	 * @throws Exception
	 */
	public int updateOrder(List<TVmInfo> vminfos,TUserBO user) throws SCSException ;
}
