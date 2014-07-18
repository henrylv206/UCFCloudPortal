package com.skycloud.management.portal.admin.sysmanage.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TRoleBO;

/**
 * 角色管理业务接口
  *<dl>
  *<dt>类名：IRoleManageService</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午10:09:37</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public interface IRoleManageService {
	int updateRole(TRoleBO role) throws SQLException;
	List<TRoleBO> findAllRole() throws SQLException;
	TRoleBO findRoleById(int roleId) throws SQLException;
	public int insertRole(TRoleBO role) throws SQLException;
	public int deleteRole(int roleID) throws SQLException;
	TRoleBO findRoleByName(String roleName) throws SQLException;
	List<TRoleBO> searchRole(QueryCriteria criteria)throws SQLException;
	public int queryUsedRole(int roleID) throws SQLException;
}
