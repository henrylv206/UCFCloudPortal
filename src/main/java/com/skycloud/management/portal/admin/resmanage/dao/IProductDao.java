package com.skycloud.management.portal.admin.resmanage.dao;

import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;

public interface IProductDao {

	public int save(Product product);

	public void delete(int id);

	public void update(Product product);

	public List<Product> findAll();

	public List<Product> find(int userId, String key, int state, float price, int templateId, int type, Integer productItemId, int isDefault,
	        int curPage, int pageSize);
	public List<Product> findForOpt(TTemplateVMBO template, Product product,
			int curPage, int pageSize);
	public List<Product> findForItem(int userId, String key, int state, float price, int templateId, int type, Integer productItemId, int isDefault,
	        int curPage, int pageSize) ;

	public List<Product> findNew();

	public List<Product> findById(int id);

	public List<Product> findCommend();

	public List<Product> findBuyByCatalog(int state, int type, int curPage, int pageSize);

	public Product get(int id);

	public int getProductCountByName(String name) throws Exception;

	public List<Product> findAduit(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize);

	public List<Product> findRelease(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize);

	public void update(final int productItemId, final int id) throws Exception;

	public int listProductCount(String name, int type, int state) throws Exception;

	public void update(final int productItemId, String currentAllProductIds) throws Exception;

	public void updateOldbyNew(final int oldItem, final int newItem) throws Exception;

	public int listItemProductCount(String name, int type, int state, int item) throws Exception;

	public int getProductCountByCode(final String code) throws Exception;

	public int getInstanceCountByProductIdAndUserId(int productId, int userId) throws Exception;

	public int checkProductRename(String name) throws Exception;

	int updateState(int productId, int state) throws Exception;

	int updateState(int productId, int state, int operateType) throws Exception;

	int checkProductRename(String name, int type) throws Exception;

	int checkVMSProductRename(String name) throws Exception;

	List<Product> findRelativeProduct(int state, int type);
	
	List<Product> find(TTemplateVMBO template, Product product, int curPage, int pageSize) ;
	
	int getFindCount(TTemplateVMBO template, Product product) ;
	List<Product> findByType(String searchKey,int type, int curPage, int pageSize);
}
