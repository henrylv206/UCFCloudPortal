package com.skycloud.management.portal.admin.productitem.dao;

import java.util.List;

import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemBO;

public interface IProductItemDAO {
	int createProductItem(TProductItemBO item,boolean fromXml) throws Exception;
	void deleteProductItem(int id) throws Exception;
	TProductItemBO getProductItemById(int id) throws Exception;
	List<TProductItemBO> listProductItem(int parentId,int release,int frontOrNot) throws Exception;
	void updateProductItem(int id,int check) throws Exception;
	void updateProductItem() throws Exception ;
	int isItemReleased(int id) throws Exception;
	int updateProductItemInfo(TProductItemBO item) throws Exception;
	TProductItemBO getProductItemByName(String key) throws Exception;
	TProductItemBO getProductItemByCode(String key) throws Exception;
	void updateProductItem(int state) throws Exception ;
	List<ProductItemXmlPO> listProductItemForExport() throws Exception;
}
