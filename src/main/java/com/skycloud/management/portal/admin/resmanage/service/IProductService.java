package com.skycloud.management.portal.admin.resmanage.service;

import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.entity.ProuctChargeUnit;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;

public interface IProductService {

	public int insertProduct(Product product) throws Exception;

	public void deleteProduct(int id, int type) throws Exception;

	public void updateProduct(Product product) throws Exception;

	public void updateProduct2(Product product) throws Exception;

	public List<Product> findAllProduct();

	public List<Product> findProduct(String key, int state, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize);
	public List<Product> findProductForOpt(TTemplateVMBO template, Product product, int curPage, int pageSize);
	public List<Product> findProductForItem(String key, int state, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) ;
	/**
	 * 前台产品列表调用接口
	 * 
	 * @param key
	 *            名称关键字
	 * @param price
	 *            产品价格, 0：表示所有价格
	 * @param type
	 *            产品类型，0：表示所有类型
	 * @param productItemId
	 *            服务目录ID; 0:表示所有服务目录
	 * @param isDefault
	 *            1:首页推荐，2：内页显示
	 * @param curPage
	 *            当前页 ， -1：表示所有数据
	 * @param pageSize
	 *            分页页大小， -1：表示所有数据
	 * @return 创建人： 何福康 创建时间：2012-2-24 下午01:40:04
	 */
	public List<Product> findProductFront(String key, float price, int type, Integer productItemId, int isDefault, int curPage, int pageSize);

	/**
	 * 前台默认首页显示的产品接口,最多3条数据
	 * 
	 * @param type
	 *            类型
	 * @return 创建人： 何福康 创建时间：2012-2-24 下午02:36:29
	 */

	public List<Product> findProductVM(int type, int page, int pagesize);

	public List<Product> findProductFrontDefault(int userId, int type);

	public List<Product> findProductByCatalog(int type);

	public List<Product> findProductFrontDefaultAll(int type);

	// to fix bug [2833]
	public List<Product> findProductFrontDefaultAllTJ(int type);

	public List<Product> findProductById(int id);

	public List<Product> findProductFrontNew();

	public List<Product> findProductCommend();

	public Product getProduct(int id);

	public List<Product> findAduit(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize);

	public List<Product> findRelease(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize);

	// public void updateProduct(final int productItemId,final String ids,String
	// currentAllProductIds) throws Exception;
	public int listProductCount(String name, int type, int state) throws Exception;

	public int getProductCountByName(String name) throws Exception;

	public int getProductCountByCode(String code) throws Exception;

	public int listItemProductCount(String name, int type, int state, int item) throws Exception;

	public int getInstanceCountByProductIdAndUserId(int productId, int userId) throws Exception;

	public int getInstanceCountByProductId(int productId) throws Exception;

	public int checkProductRename(String name) throws Exception;

	int updateState(int productId, int state,int templateId) throws Exception;

	int updateState(int productId, int state, int operateType,int templateId) throws Exception;

	int checkProductRename(String name, int type) throws Exception;

	int checkVMSProductRename(String name) throws Exception;

	List<Product> findRelativeProduct(int state, int type);
	
	/**
	 * 增加模板和服务
	 * @param template
	 * @param product
	 * @return
	 * @throws Exception
	 * @author zhanghuizheng
	 */
	int insertProductAndVMTemplate(int type,Map<String, String> map) throws Exception ;
	
	void updateProductAndVMTemplate(int type,Map<String, String> map) throws Exception ;

	List<Product> find(TTemplateVMBO template, Product product, int curPage, int pageSize) ;
	
	int getFindCount(TTemplateVMBO template, Product product) ;
	List<Product> findByType(String searchKey,int type, int curPage, int pageSize) ;
	
	/**
	 * 取得产品的多个定价信息
	 * 何军辉 20130310
	 * @param productId
	 * @return
	 */
	List<ProuctChargeUnit> getProductChargeUnitList(int productId);
	/**
	 *  增加多计价单位的支持
	 * 何军辉 20130313
	 * @param productId
	 * @return
	 */
	public void addProductChargeUnit(int id, String priceList);
}
