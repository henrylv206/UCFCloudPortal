package com.skycloud.management.portal.front.order.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.entity.TOrderHistoryBO;

public interface IOrderHistoryService {
	
		int save(TOrderHistoryBO orderHistory) throws SQLException;
		
		int delete(int id) throws SQLException;
		
		int update(TOrderHistoryBO orderHistory) throws SQLException;

		TOrderHistoryBO searchById(int id) throws SQLException;

		List<TOrderHistoryBO> searchAll() throws SQLException;
		
		List<TOrderHistoryBO> searchByOrderId(int orderId) throws SQLException;
		
		List<TOrderHistoryBO> searchByProductId(int productId) throws SQLException;
		
		List<TOrderHistoryBO> searchByInstanceId(int instanceId) throws SQLException;
		
		List<TOrderHistoryBO> searchByTemplateId(int templateId) throws SQLException;
		
		int saveByOrder(TOrderBO order) throws SQLException;

}
