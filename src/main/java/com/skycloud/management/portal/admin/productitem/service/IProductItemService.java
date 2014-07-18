package com.skycloud.management.portal.admin.productitem.service;

import java.util.List;

import com.skycloud.management.portal.admin.menu.entity.MenuBO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationBO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemBO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemFrontBO;
import com.skycloud.management.portal.admin.resmanage.entity.Product;

public interface IProductItemService {
	int createProductItem(TProductItemBO item, boolean fromXml)
			throws Exception;

	void deleteProductItem(int id) throws Exception;

	TProductItemBO getProductItemById(int id) throws Exception;

	List<TProductItemBO> listProductItem(int parentId, int release,
			int frontOrNot) throws Exception;

	void updateProductItem(String releaseItemIds, String unreleaseItemIds)
			throws Exception;

	int isItemReleased(int id) throws Exception;

	int updateProductItemInfo(TProductItemBO item) throws Exception;

	int getTreeLevel(int subId, int level) throws Exception;

	TProductItemBO getProductItemByName(String key) throws Exception;

	TProductItemBO getProductItemByCode(String key) throws Exception;

	void insertXmlProductItems(List<ProductItemXmlPO> list) throws Exception;

	void updateProductItem(int state) throws Exception;

	List<ProductItemXmlPO> listProductItemForExport() throws Exception;

	TProductItemFrontBO getProductItemFrontById(int id) throws Exception;

	List<TProductItemFrontBO> listProductItemFront(int parentId)
			throws Exception;

	int isItemAduited() throws Exception;

	void updateProductItemRelation(int productItemId, String ids,
			String currentAllProductIds) throws Exception;

	List<ProductItemRelationBO> listProductItemRelation(int itemId)
			throws Exception;

	List<Product> findProductFront(String key, float price, int type,
			Integer productItemId, int isDefault, int curPage, int pageSize,int frontOrNot);
	int listFrontProductCount(int productItemId, int state) throws Exception ;
	int listItemProductCount(String name,int type, int state,int item) throws Exception ;
	String listMenu(int addMutilVM) throws Exception;
	List<Product> findItemsByProductId(int productId) ;
	void deleteItemExceptRoot(int id) throws Exception ;
	List<MenuBO> listMenu2() throws Exception ;
}
