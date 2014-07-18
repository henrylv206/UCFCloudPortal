package com.skycloud.management.portal.front.mall.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.mall.entity.TemplateTypeBO;
import com.skycloud.management.portal.front.mall.vo.ResourceTopoVO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public interface CloudServiceMallService {

	int getAllServiceCount(String key, String typeId, int userId) throws Exception;

	int getAllServiceCnt(int part, String key, String typeId, int userId) throws Exception;

	List<Product> getAllService(PageVO vo, int part, String sales, String price, String key, String typeId, int userId) throws Exception;

	int getAllServiceCount(TUserBO user, String serviceName, String typeId, String state, String start, String end) throws Exception;

	List<TServiceInstanceBO> getAllService(PageVO vo, TUserBO user, String serviceName, String typeId, String state, String start, String end)
	throws Exception;

	int getCommendServiceCount(int userId);

	List<Product> getCommendService(PageVO vo, int userId) throws Exception;

	List<TemplateTypeBO> getTemplateTypeList() throws SQLException;

	int getAllReourceCount(int serviceId) throws Exception;

	List<TInstanceInfoBO> getAllReource(PageVO vo, int serviceId, TUserBO user) throws Exception;

	List<TInstanceInfoBO> getAllReource2(PageVO vo, int serviceId, TUserBO user, int orderid) throws Exception;

	// added by zhanghuizheng
	List<Product> find(TTemplateVMBO t, Product p, int curPage, int pageSize);

	int getItemServiceCount(TTemplateVMBO t, Product p);

	List<Product> findRand();

	Product getProductById(int id) throws SCSException;

	ResourcesVO getDeviceNameById(long id, String type) throws Exception;
	/**
	 * 查询我的拓扑图数据信息 ninghao 2012-09-04
	 * 
	 * @param user
	 * @return String json format
	 */
	ResourceTopoVO findMyTopoStructData(TUserBO user) throws Exception;

	List<TServiceInstanceBO> getQuitService(TUserBO user, int serviceID) throws Exception;

	List<TServiceInstanceBO> getQuitInstance(TUserBO user, int serviceID) throws Exception;

	List<TServiceInstanceBO> checkLB2FW(int orderId) throws Exception;
}
