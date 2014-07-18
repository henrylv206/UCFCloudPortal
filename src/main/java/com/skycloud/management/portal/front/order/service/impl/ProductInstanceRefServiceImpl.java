package com.skycloud.management.portal.front.order.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.front.order.dao.IProductInstanceRefDao;
import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;
import com.skycloud.management.portal.front.order.service.IProductInstanceRefService;

public class ProductInstanceRefServiceImpl implements
		IProductInstanceRefService {

	private IProductInstanceRefDao productInstanceRefDao;
	
	@Override
	public int save(TProductInstanceInfoRefBO piRef) throws SQLException {
		return productInstanceRefDao.save(piRef);
	}

	@Override
	public int delete(int piId) throws SQLException {
		return productInstanceRefDao.delete(piId);
	}

	@Override
	public int update(TProductInstanceInfoRefBO piRef) throws SQLException {
		return productInstanceRefDao.update(piRef);
	}

	@Override
	public TProductInstanceInfoRefBO searchById(int piId) throws SQLException {
		return productInstanceRefDao.searchById(piId);
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchAll() throws SQLException {
		return productInstanceRefDao.searchAll();
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByOrderId(int orderId)
			throws SQLException {
		return productInstanceRefDao.searchByOrderId(orderId);
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByProductId(int productId)
			throws SQLException {
		return productInstanceRefDao.searchByProductId(productId);
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByInstanceId(int instanceId)
			throws SQLException {
		return productInstanceRefDao.searchByInstanceId(instanceId);
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByTemplateId(int templateId)
			throws SQLException {
		return productInstanceRefDao.searchByTemplateId(templateId);
	}
	
	@Override
    public List<TProductInstanceInfoRefBO> searchByServiceId(int serviceId) throws SQLException {
		return productInstanceRefDao.searchByServiceId(serviceId);
    }

	public IProductInstanceRefDao getProductInstanceRefDao() {
		return productInstanceRefDao;
	}

	public void setProductInstanceRefDao(
			IProductInstanceRefDao productInstanceRefDao) {
		this.productInstanceRefDao = productInstanceRefDao;
	}

	
	

}
