package com.skycloud.management.portal.front.order.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;

public interface IProductInstanceRefService {
	int save(TProductInstanceInfoRefBO piRef) throws SQLException;
	
	int delete(int piId) throws SQLException;
	
	int update(TProductInstanceInfoRefBO piRef) throws SQLException;

	TProductInstanceInfoRefBO searchById(int piId) throws SQLException;

	List<TProductInstanceInfoRefBO> searchAll() throws SQLException;
	
	List<TProductInstanceInfoRefBO> searchByOrderId(int orderId) throws SQLException;
	
	List<TProductInstanceInfoRefBO> searchByProductId(int productId) throws SQLException;
	
	List<TProductInstanceInfoRefBO> searchByInstanceId(int instanceId) throws SQLException;
	
	List<TProductInstanceInfoRefBO> searchByTemplateId(int templateId) throws SQLException;
	
	List<TProductInstanceInfoRefBO> searchByServiceId(int serviceId) throws SQLException;

}
