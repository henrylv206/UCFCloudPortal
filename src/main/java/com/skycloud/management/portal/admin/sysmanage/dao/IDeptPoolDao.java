package com.skycloud.management.portal.admin.sysmanage.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TDeptPoolBO;
import com.skycloud.management.portal.admin.sysmanage.vo.ResPoolDeptRelationVO;

/**
 * 部门与资源池对象持久化接口
  *<dl>
  *<dt>类名：IDeptPoolDao</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-12-06  上午09:55:00</dd>
  *<dd>创建人：ninghao@chinaskycloud.com</dd>
  *</dl>
 */
public interface IDeptPoolDao {
	/**
	 * 查找部门详情与资源池关系
	 * @param  deptId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  上午10:00:00
	 */
	public List<TDeptPoolBO> findDeptPoolByDeptId(final int deptId) throws SQLException ;
	
	/**
	 * 删除部门与资源池关系
	 * @param  deptId
	 * @param  delFlag 1:逻辑删除；2：物理删除
	 * @return
	 * @throws SQLException
	 * 创建人：  ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  上午10:00:00
	 */
	public int deleteDeptPoolRelation(int deptId , int delFlag) throws SQLException ;
	
	/**
	 * 保存部门与资源池关系
	 * @param  deptList
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  上午10:00:00
	 */
	public int saveDeptPoolRelation(List<TDeptPoolBO> deptList) throws SQLException ;
	
	/**
	 * 查找订单全部相关资源对应的全部资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findOrderResPoolByOrderId(final int orderId) throws SQLException ;
	
	/**
	 * 查找已分配给用户的资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findUserResPoolByUserId(final int userId) throws SQLException ;
	
}
