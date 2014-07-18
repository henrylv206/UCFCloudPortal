package com.skycloud.management.portal.front.order.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;

public interface IServiceInstanceService {

	int save(TServiceInstanceBO serviceInstance) throws SQLException;

	int delete(int id) throws SQLException;

	int update(TServiceInstanceBO serviceInstance) throws SQLException;

	TServiceInstanceBO searchById(int id) throws SQLException;

	List<TServiceInstanceBO> searchAll() throws SQLException;

	List<TServiceInstanceBO> searchByOrderId(int orderId, int orderType) throws SQLException;
	List<TServiceInstanceBO> searchByOrderId(PageVO vo,int orderId, int orderType) throws SQLException;

	int checkServiceInstanceStateById(int serviceId) throws SQLException;

	/**
	 * 根据资源实例Id修改所在的服务实例状态
	 * @param state
	 * @param instanceInfoId
	 * @return
	 * @throws SCSException
	 */
		int updateServiceStateByInstanceInfoId(int state,int instanceInfoId) throws SCSException;

		/**
		 * 根据订单Id修改服务实例状态
		 * @param state
		 * @ param orderId
		 * @return
		 * @throws SCSException
		 */
			int updateServiceStateByOrderId(int state,int orderId) throws SCSException;

			/**
			 * 根据订单Id修改服务实例状态
			 * @param state
			 * @ param orderId
			 * @return
			 * @throws SCSException
			 */
				int updateServiceStateByOrder(int state,TOrderBO order) throws SCSException;

				/**
				 * 根据服务实例Id修改服务实例状态
				 * @param serviceId
				 * @param state
				 * @return
				 * @throws SCSException
				 */
				int updateServiceStateByServiceId(int state,int serviceId) throws SCSException;

				/**
				 * 根据服务实例Id回滚服务实例状态
				 * @param state
				 * @param serviceId
				 * @return
				 * @throws SCSException
				 */
				int rollbackServiceStateByServiceId(int serviceId) throws SCSException;

				/**
				 * 根据资源实例ID查找服务实例
				 * @param instanceInfoid
				 * @return
				 * @throws SQLException
				 */
				TServiceInstanceBO searchServiceInstanceByInstanceInfoId(int instanceInfoid) throws SCSException;

				int countDetailByOrderId(int orderId,	int orderType) throws SQLException;
				/**
				 * 取消订单实例的修改
				 * @param orderId
				 * @return
				 * @throws SQLException
				 */
				int cancelUpdateServiceInstanceByOrderId(int orderId) throws SQLException;

}
