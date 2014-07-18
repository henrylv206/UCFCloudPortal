package com.skycloud.management.portal.admin.productitem.dao;

import java.util.List;

import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationFrontBO;
import com.skycloud.management.portal.admin.resmanage.entity.Product;

public interface IProductItemRelationFrontDAO {
	int createProductItemRelationFront(ProductItemRelationFrontBO relation) throws Exception;
	void deleteProductItemRelationFront() throws Exception;
	List<Product> findFrontProduct(String key, int state, float price,
	int templateId, int type, Integer productItemId, int isDefault, int curPage, int pageSize,int frontOrNot);
	int listFrontProductCount(int productItemId, int state) throws Exception ;
	public List<Product> findItemsByProductId(int productId) ;
}
