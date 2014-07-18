package com.skycloud.management.portal.admin.resmanage.dao;

import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.vo.ProductTemplateRelationBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;

public interface IProductTemplateRelationDao {
	int createProductTemplateRelation(ProductTemplateRelationBO relation) throws Exception;
	List<TTemplateVMBO> getProductTemplates(int productId);
	void deleteProductTemplateRelation(int productId) throws Exception;
//	List<TTemplateMCBO> getProductMCTemplates(int productId) ;
//	ProductTemplateRelationBO getProductTemplateRelationById(int id) throws Exception;
//	List<ProductTemplateRelationBO> listProductTemplateRelation(int id) throws Exception;
	
}
