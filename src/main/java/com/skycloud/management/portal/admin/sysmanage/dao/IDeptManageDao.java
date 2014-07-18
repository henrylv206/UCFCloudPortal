package com.skycloud.management.portal.admin.sysmanage.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;

/**
 * 部门对象持久化接口
  *<dl>
  *<dt>类名：IDeptManageDao</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午09:55:14</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public interface IDeptManageDao {
	/**
	 * 部门列表显示
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:00:11
	 */
	List<TDeptBO> deptAll() throws SQLException ;
	/**
	 * 查找部门详情
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:00:44
	 */
	TDeptBO findDeptById(int deptId) throws SQLException ;
	
	/**
	 * 更新部门
	 * @param dept
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:01:08
	 */
	int updateDept(TDeptBO dept) throws SQLException ;
	
	/**
	 * 删除部门
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:01:23
	 */
	int deleteDept(int deptId) throws SQLException ;
	
	/**
	 * 保存部门
	 * @param dept
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:01:34
	 */
	int saveDept(TDeptBO dept) throws SQLException ;
	
	/**
	 * 通过部门名称查找部门
	 * @param deptName
	 * @param oldDeptName
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:01:45
	 */
	TDeptBO findDeptByName(String deptName,String oldDeptName) throws SQLException;
	
	/**
	 * 查找部门下员工
	 * @param deptId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:02:14
	 */
	List<TUserBO> findUserByDept(int deptId) throws SQLException;
	
	/**
	 * 条件查询部门
	 * @param criteria
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:02:18
	 */
	List<TDeptBO> searchDept(QueryCriteria criteria) throws SQLException ;
}
