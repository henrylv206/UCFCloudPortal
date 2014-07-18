package com.skycloud.management.portal.front.order.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;

public interface IServiceInstanceDao {

	int save(TServiceInstanceBO serviceInstance) throws SQLException;

	int delete(int id) throws SQLException;

	int update(TServiceInstanceBO serviceInstance) throws SQLException;

	TServiceInstanceBO searchById(int id) throws SQLException;

	List<TServiceInstanceBO> searchAll() throws SQLException;

	List<TServiceInstanceBO> searchByOrderId(int orderId, int orderType) throws SQLException;
	List<TServiceInstanceBO> searchByOrderId(PageVO vo,int orderId, int orderType) throws SQLException;

	/**
	 * 根据资源实例Id修改所在的服务实例状态
	 * @param instanceInfoId
	 * @param state
	 * @return
	 * @throws SCSException
	 */
		int updateServiceStateByInstanceInfoId(int state,int instanceInfoId) throws SCSException;

		/**
		 * 根据订单Id修改服务实例状态
		 * @param state
		 * @ param orderId
		 * @param orderType
		 * @return
		 * @throws SCSException
		 */
			int updateServiceStateByOrderId(int state,int orderId) throws SCSException;

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

			int countDetailByOrderId(int orderId,
					int orderType) throws SQLException;

}
