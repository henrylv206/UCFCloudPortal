package com.skycloud.management.portal.admin.productitem.dao;

import java.util.List;

import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemFrontBO;

public interface IProductItemFrontDAO {
	int createProductItemFront(TProductItemFrontBO item) throws Exception;
	void deleteProductItemFront(int id) throws Exception;
	TProductItemFrontBO getProductItemFrontById(int id) throws Exception;
	List<TProductItemFrontBO> listProductItemFront(int parentId) throws Exception;
	int isItemAduited() throws Exception;
	TProductItemFrontBO getProductItemByName(String key) throws Exception;
	TProductItemFrontBO getProductItemByCode(String key) throws Exception;
}
