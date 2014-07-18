package com.skycloud.management.portal.front.order.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.front.order.entity.TOrderHistoryBO;
import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;

public interface IOrderHistoryDao {
	int save(TOrderHistoryBO orderHistory) throws SQLException;
	
	int delete(int id) throws SQLException;
	
	int update(TOrderHistoryBO orderHistory) throws SQLException;

	TOrderHistoryBO searchById(int id) throws SQLException;

	List<TOrderHistoryBO> searchAll() throws SQLException;
	
	List<TOrderHistoryBO> searchByOrderId(int orderId) throws SQLException;
	
	List<TOrderHistoryBO> searchByProductId(int productId) throws SQLException;
	
	List<TOrderHistoryBO> searchByInstanceId(int instanceId) throws SQLException;
	
	List<TOrderHistoryBO> searchByTemplateId(int templateId) throws SQLException;
	

}
