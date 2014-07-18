package com.skycloud.management.portal.admin.productitem.dao;

import java.util.List;

import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationBO;;

public interface IProductItemRelationDAO {
	int createProductItemRelation(ProductItemRelationBO relation) throws Exception;
//	void deleteProductItemRelation(int id) throws Exception;
	void deleteProductItemRelation(int productItemId,String currentAllProductIds,int notAll) throws Exception;
	ProductItemRelationBO getProductItemRelationById(int id) throws Exception;
	List<ProductItemRelationBO> listProductItemRelation(int itemId) throws Exception;
	int listItemProductCount(String name,int type, int state,int item) throws Exception ;
//	int isItemAduited() throws Exception;
//	ProductItemRelationBO getProductItemByName(String key) throws Exception;
//	ProductItemRelationBO getProductItemByCode(String key) throws Exception;
}
