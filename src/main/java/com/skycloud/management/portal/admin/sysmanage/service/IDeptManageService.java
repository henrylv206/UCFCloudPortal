package com.skycloud.management.portal.admin.sysmanage.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.vo.ResPoolDeptRelationVO;
import com.skycloud.management.portal.exception.SCSException;

/**
 * 部门管理业务接口
  *<dl>
  *<dt>类名：IDeptManageService</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午10:05:28</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public interface IDeptManageService {
	
	/**
	 * 创建部门
	 * @param dept
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:20
	 */
	int createDept(TDeptBO dept) throws SQLException;
	
	/**
	 * 获得部门列表
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:24
	 */
	List<TDeptBO> getAllDept() throws SQLException;
	
	/**
	 * 获得部门详情
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:31
	 */
	TDeptBO getDeptById(int deptId) throws SQLException;
	
	/**
	 * 删除部门
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:34
	 */
	int deleteDept(int deptId) throws SQLException;
	
	/**
	 * 更新部门
	 * @param dept
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:45
	 */
	int updateDept(TDeptBO dept) throws SQLException;
	
	/**
	 * 
	 * @param deptName
	 * @param oldDeptName
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:49
	 */
	TDeptBO findDeptByName(String deptName,String oldDeptName) throws SQLException;
	
	/**
	 * 查看部门下员工列表
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:52
	 */
	List<TUserBO> findUserByDept(int deptId) throws SQLException;
	
	/**
	 * 条件查询部门
	 * @param criteria
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:05:55
	 */
	List<TDeptBO> searchDept(QueryCriteria criteria) throws SQLException ;

	
	/**
	 * 查询部门已分配资源池
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：  ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  下午05:20:00
	 */
	public List<ResPoolDeptRelationVO> findResPoolByDeptId(int deptId) throws SCSException;

	/**
	 * 查询全部资源池，并对已分配本部门的资源池做标志
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：  ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  下午05:30:00
	 */
	public List<ResPoolDeptRelationVO> findResPoolAndDeptPool(int deptId) throws SCSException;

	
	/**
	 * 查找订单全部相关资源对应的全部资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findOrderResPoolByOrderId(int orderId) throws SQLException ;
	
	/**
	 * 查找已分配给用户的资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findUserResPoolByUserId(int userId) throws SQLException ;
	
}
