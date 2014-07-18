package com.skycloud.management.portal.admin.productitem.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import com.skycloud.management.portal.admin.menu.entity.MenuBO;
import com.skycloud.management.portal.admin.productitem.entity.ListProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationBO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationFrontBO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemBO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemFrontBO;
import com.skycloud.management.portal.admin.productitem.service.IProductItemService;
import com.skycloud.management.portal.admin.productitem.action.ProductItemAction;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemDAO;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemFrontDAO;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemRelationDAO;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemRelationFrontDAO;
import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.menu.dao.IMenuDAO;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.thoughtworks.xstream.XStream;

public class ProductItemServiceImpl implements IProductItemService {
	private Logger logger = Logger.getLogger(ProductItemServiceImpl.class);
	IProductItemDAO productItemDAO;
	IProductDao productDao;
	IProductItemFrontDAO productItemFrontDAO;
	IProductItemRelationDAO productItemRelationDAO;
	IProductItemRelationFrontDAO productItemRelationFrontDAO;
	IMenuDAO menuDAO;

	@Override
	public int createProductItem(TProductItemBO item, boolean fromXml)
			throws Exception {
		return this.productItemDAO.createProductItem(item, fromXml);
	}

	@Override
	public void deleteProductItem(int id) throws Exception {
		this.removeProductItem(id);
		//把树的状态设置为待审批状态
		this.productItemDAO.updateProductItem(3);
	}
	
	public void removeProductItem(int id) throws Exception {
		//查询其下的子节点
		List<TProductItemBO> subItems = this.productItemDAO.listProductItem(id, 0, 0);
		if(null != subItems && subItems.size()>0){
			for(TProductItemBO subItem : subItems){
				int _id = subItem.getId();
				this.removeProductItem(_id);
			}
		}
		
		TProductItemBO item = this.getProductItemById(id);
		if (null != item) {
			int pid = item.getParentId();
			//把节点所关联的资源放到其父节点上
			this.productDao.updateOldbyNew(id, pid);
		}
		//删除节点
		this.productItemDAO.deleteProductItem(id);
		//删除后台关联的资源
		//this.productItemRelationDAO.deleteProductItemRelation(id, null,-1);
		
	}
	
	public void deleteItemExceptRoot(int id) throws Exception {
		this.productItemDAO.deleteProductItem(id);
	}

	@Override
	public TProductItemBO getProductItemById(int id) throws Exception {
		TProductItemBO item = this.productItemDAO.getProductItemById(id);
		return item;
	}

	@Override
	public List<TProductItemBO> listProductItem(int parentId, int release,
			int frontOrNot) throws Exception {
		List<TProductItemBO> list = this.productItemDAO.listProductItem(
				parentId, release, frontOrNot);
		return list;
	}

	public IProductItemDAO getProductItemDAO() {
		return productItemDAO;
	}

	public void setProductItemDAO(IProductItemDAO productItemDAO) {
		this.productItemDAO = productItemDAO;
	}

	public IProductItemRelationDAO getProductItemRelationDAO() {
		return productItemRelationDAO;
	}

	public void setProductItemRelationDAO(
			IProductItemRelationDAO productItemRelationDAO) {
		this.productItemRelationDAO = productItemRelationDAO;
	}

	public IProductItemRelationFrontDAO getProductItemRelationFrontDAO() {
		return productItemRelationFrontDAO;
	}

	public void setProductItemRelationFrontDAO(
			IProductItemRelationFrontDAO productItemRelationFrontDAO) {
		this.productItemRelationFrontDAO = productItemRelationFrontDAO;
	}

	public IMenuDAO getMenuDAO() {
		return menuDAO;
	}

	public void setMenuDAO(IMenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	@Override
	public void updateProductItem(String releaseItemIds, String unreleaseItemIds)
			throws Exception {
		this.productItemDAO.updateProductItem();
		if (null != releaseItemIds && !releaseItemIds.equals("")) {
			String[] arrItems = releaseItemIds.split(",");
			for (int i = 0; i < arrItems.length; i++) {
				if (null != arrItems[i] && !arrItems[i].equals("")) {
					this.productItemDAO.updateProductItem(
							Integer.parseInt(arrItems[i]), 1);
				}
			}
		}
		if (null != unreleaseItemIds && !unreleaseItemIds.equals("")) {
			String[] arrunItems = unreleaseItemIds.split(",");
			for (int j = 0; j < arrunItems.length; j++) {
				if (null != arrunItems[j] && !arrunItems[j].equals("")) {
					this.productItemDAO.updateProductItem(
							Integer.parseInt(arrunItems[j]), -1);
				}
			}
		}
		this.productItemDAO.updateProductItem(3);
	}

	@Override
	public int isItemReleased(int id) throws Exception {
		return this.productItemDAO.isItemReleased(id);
	}

	public IProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public int updateProductItemInfo(TProductItemBO item) throws Exception {
		item.setLevel(3);
		return this.productItemDAO.updateProductItemInfo(item);
	}

	public int getTreeLevel(int subId, int level) throws Exception {
		// try {
		TProductItemBO item = this.productItemDAO.getProductItemById(subId);
		if (null != item) {
			int parentId = item.getParentId();
			if (parentId != -1) {
				level++;
				level = getTreeLevel(parentId, level);
			}
		} else {
			throw new Exception("Parent node is not exist");

		}
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return level;
	}

	@Override
	public TProductItemBO getProductItemByName(String key) throws Exception {
		return this.productItemDAO.getProductItemByName(key);
	}

	@Override
	public TProductItemBO getProductItemByCode(String key) throws Exception {
		return this.productItemDAO.getProductItemByCode(key);
	}

	public void insertXmlProductItems(List<ProductItemXmlPO> list)
			throws Exception {
		if (null != list && list.size() > 0) {
			this.deleteItemExceptRoot(-2);
			logger.error("delete all nodes excepiton root!!!!!!!!!!!!!!");
			for (ProductItemXmlPO item : list) {
				//to fix bug:3260
				if(item.getId()>18){
					int level = this.getTreeLevel(item.getParentId(), 1);
					//to fix bug:3260,to fix bug:3746
					if(level == 3){
						TProductItemBO productItem = new TProductItemBO();
						productItem.setId(item.getId());
						productItem.setCode(item.getCode());
						productItem.setName(item.getName());
						// 默认从1级开始增加节点
						productItem.setLevel(level);
						productItem.setParentId(item.getParentId());
						productItem.setState(-1);
						productItem.setNodeType(0);
						productItem.setReleaseOrNot(0);
						this.createProductItem(productItem, true);
						logger.error("create item "+productItem.getId());
					}else{
						throw new Exception("Can only create the third level service item");
					}
				}
			}
		}
		this.productItemDAO.updateProductItem();
		logger.error("update item ");
	}

	@Override
	public void updateProductItem(int state) throws Exception {
		// 更新服务目录的根节点的状态
		this.productItemDAO.updateProductItem(state);
		if (state == 2) {
			// 把T_SCS_PRODUCT_ITEM_FRONT表中的数据删除
			this.productItemFrontDAO.deleteProductItemFront(-2);
			// 如果审核通过则把审核过的目录同步到T_SCS_PRODUCT_ITEM_FRONT表中
			List<TProductItemBO> list = this.productItemDAO.listProductItem(-2,
					1, 1);
			if (null != list && list.size() > 0) {
				for (TProductItemBO itm : list) {
					TProductItemFrontBO _item = new TProductItemFrontBO();
					_item.setCode(itm.getCode());
					_item.setId(itm.getId());
					_item.setLevel(itm.getLevel());
					_item.setName(itm.getName());
					_item.setNodeType(itm.getNodeType());
					_item.setParentId(itm.getParentId());
					_item.setState(itm.getState());
					this.productItemFrontDAO.createProductItemFront(_item);
				}
			}
			//把T_SCS_PRODUCT_ITEM_RELATION_FRONT表中的数据删除
			this.productItemRelationFrontDAO.deleteProductItemRelationFront();
			//如果审核通过则把审核过的目录和产品的对应关系同步到T_SCS_PRODUCT_ITEM_RELATION_FRONT表中
			List<ProductItemRelationBO> relationlist = productItemRelationDAO.listProductItemRelation(-1);
			if(null != relationlist && relationlist.size()>0){
				for(ProductItemRelationBO relation : relationlist){
					ProductItemRelationFrontBO front = new ProductItemRelationFrontBO();
					front.setId(relation.getId());
					front.setProductId(relation.getProductId());
					front.setProductItemId(relation.getProductItemId());
					front.setState(relation.getState());
					this.productItemRelationFrontDAO.createProductItemRelationFront(front);
				}
			}
		}

	}

	@Override
	public List<ProductItemXmlPO> listProductItemForExport() throws Exception {
		return this.productItemDAO.listProductItemForExport();
	}

	public IProductItemFrontDAO getProductItemFrontDAO() {
		return productItemFrontDAO;
	}

	public void setProductItemFrontDAO(IProductItemFrontDAO productItemFrontDAO) {
		this.productItemFrontDAO = productItemFrontDAO;
	}

	@Override
	public TProductItemFrontBO getProductItemFrontById(int id) throws Exception {
		return this.productItemFrontDAO.getProductItemFrontById(id);
	}

	@Override
	public List<TProductItemFrontBO> listProductItemFront(int parentId)
			throws Exception {
		return this.productItemFrontDAO.listProductItemFront(parentId);
	}

	@Override
	public int isItemAduited() throws Exception {
		return this.productItemFrontDAO.isItemAduited();
	}

	@Override
	public void updateProductItemRelation(int productItemId, String ids,
			String currentAllProductIds) throws Exception {
		this.productItemRelationDAO.deleteProductItemRelation(productItemId, currentAllProductIds,1);

		if (null != ids && !ids.equals("")) {
			String[] arrids = ids.split(",");
			if(null != arrids && arrids.length>0){
				for (int i = 0; i < arrids.length; i++) {
					ProductItemRelationBO relation = new ProductItemRelationBO();
					relation.setProductId(Integer.parseInt(arrids[i]));
					relation.setProductItemId(productItemId);
					relation.setState(-1);
					this.productItemRelationDAO.createProductItemRelation(relation);
					/*
					 * To Fix Bug Id:[1527] start
					 */
//					this.productItemRelationDAO.update(productItemId,
//							Integer.parseInt(arrids[i]));
					// 添加用户操作日志
//					String parameters = "productItemId=" + productItemId;
//					String _memo = "要绑定的资源的id值为:" + arrids[i];
//					String _desc = "把资源添加到服务目录";
//					ConstDef.saveLogInfo(3, "服务目录定义", "资源绑定",
//							"ProductItemServiceImpl", "updateProductItemRelation", parameters,
//							_desc, _memo);
					/*
					 * To Fix Bug Id:[1527] end
					 */
				}
				
			}
		}
	}

	@Override
	public List<ProductItemRelationBO> listProductItemRelation(int itemId)
			throws Exception {
		return this.productItemRelationDAO.listProductItemRelation(itemId);
	}

	@Override
	public List<Product> findProductFront(String key, float price,
			int type, Integer productItemId, int isDefault,int curPage,
			int pageSize,int frontOrNot) {
		return this.productItemRelationFrontDAO.findFrontProduct(key, 3, price, 0, type, productItemId, isDefault, curPage, pageSize,frontOrNot);
	}

	@Override
	public int listFrontProductCount(int productItemId, int state)
			throws Exception {
		return this.productItemRelationFrontDAO.listFrontProductCount(productItemId, state);
	}

	@Override
	public int listItemProductCount(String name, int type, int state, int item)
			throws Exception {
		return this.productItemRelationDAO.listItemProductCount(name, type, state, item);
	}

	@Override
	public String listMenu(int addMutilVM) throws Exception {
		List<MenuBO> menulist = this.menuDAO.listMenu();
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		if(null != menulist && menulist.size()>0){
			for(MenuBO _menu : menulist){
    			Map<String, String> optionMap = new HashMap<String, String>();
            	optionMap.put("text", String.valueOf(_menu.getMenuDesc()));
            	optionMap.put("value", String.valueOf(ConstDef.RESOURCE_TYPE_MENU_MAP.get(_menu.getMenuCode())));
            	reList.add(optionMap);
			}
		}
		//to fix bug:2382
		if(addMutilVM == 1){
			//加上多虚机服务类型
			Map<String, String> optionMap = new HashMap<String, String>();
	    	optionMap.put("text", "多实例");
	    	optionMap.put("value", "50");
	    	reList.add(optionMap);				
		}
		return JSONArray.fromObject(reList).toString();
	}
	
	public List<MenuBO> listMenu2() throws Exception {
		List<MenuBO> menulist = this.menuDAO.listMenu();
		return menulist;
	}
	
	
	public List<Product> findItemsByProductId(int productId) {
		return this.productItemRelationFrontDAO.findItemsByProductId(productId);
	}

	
}
