package com.skycloud.management.portal.front.mall.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.mall.entity.TemplateTypeBO;
import com.skycloud.management.portal.front.mall.vo.RelationVO;
import com.skycloud.management.portal.front.mall.vo.ResourceVO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public interface CloudServiceMallDao {

	int getAllServiceCount(String key, String typeId, int userId) throws Exception;

	List<Product> getAllService(PageVO vo, int start, int end, String sales, String price, String key, String typeId, int userId) throws Exception;

	int getAllServiceCount(TUserBO user, String serviceName, String typeId, String state, String start, String end) throws Exception;

	List<TServiceInstanceBO> getAllService(PageVO vo, TUserBO user, String serviceName, String typeId, String state, String start, String end)
	throws Exception;

	List<TServiceInstanceBO> getQuitService(TUserBO user, int serviceID) throws Exception;

	List<TServiceInstanceBO> getQuitInstance(TUserBO user, int serviceID) throws Exception;

	List<TServiceInstanceBO> checkLB2FW(int orderId) throws Exception;

	ResourcesVO getVolumeVO(long userid, long id) throws Exception;

	int getCommendServiceCount(int userId);

	List<Product> getCommendService(PageVO vo, int userId) throws Exception;

	List<TemplateTypeBO> getTemplateTypeList() throws SQLException;

	ResourcesVO getDeviceNameById(long eid, String type) throws Exception;
	int getAllReourceCount(int serviceId) throws Exception;

	List<TInstanceInfoBO> getAllReource(PageVO vo, int serviceId) throws Exception;

	// added by zhanghuizheng
	List<Product> find(TTemplateVMBO t, Product p, int curPage, int pageSize);

	int getItemServiceCount(TTemplateVMBO t, Product p);

	List<Product> findRand();

	/**
	 * 查询拓扑图资源信息 ninghao 2012-09-05
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<ResourceVO> getMyTopoReourceVO(TUserBO user, String[] type) throws Exception;

	/**
	 * 查询拓扑图资源关系信息 ninghao 2012-09-05
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<RelationVO> getMyTopoRelationVO(TUserBO user, String[] type) throws Exception;

	public List<TServiceInstanceBO> getAllServiceOfOrder(String ids, int orderType)
	throws Exception;

}
