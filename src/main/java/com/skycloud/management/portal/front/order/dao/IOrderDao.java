package com.skycloud.management.portal.front.order.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.webservice.usage.model.orderVO;

public interface IOrderDao {

	int save(TOrderBO order) throws SCSException;

	int delete(int orderId) throws SQLException;

	int update(TOrderBO order) throws SQLException;

	TOrderBO searchOrderById(int orderId) throws SQLException;

	List<TOrderBO> searchAllOrder() throws SQLException;

	int searchLastId() throws SQLException;

	orderVO searchVMOrderByElInstanceId(String ElInstaceId) throws SQLException;

	orderVO searchLBOrderByElInstanceId(String ElInstaceId) throws SQLException;

	List<TOrderBO> searchOrders(PageVO vo, TUserBO user, QueryCriteria query) throws SQLException;
	List<TOrderBO> searchOrders2(PageVO vo, TUserBO user, QueryCriteria query) throws SQLException;

	int searchOrdersAmount(TUserBO user, QueryCriteria query) throws SQLException;

	List<TInstanceInfoBO> findInstanceByOrderId(int orderId, int orderType) throws SQLException;

	List<TInstanceInfoBO> findService2InstanceByOrderId(final int orderId, int orderType) throws SQLException;

	List<TOrderBO> searchOrdersByInstanceId(int instanceId) throws SQLException;

	TUserBO getAuditorByOrderId(int orderId) throws SQLException;
}
