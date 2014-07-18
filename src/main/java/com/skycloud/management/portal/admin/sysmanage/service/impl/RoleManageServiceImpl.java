package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.dao.IRoleManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TRoleBO;
import com.skycloud.management.portal.admin.sysmanage.service.IRoleManageService;

/**
 * 角色管理业务实现
  *<dl>
  *<dt>类名：RoleManageServiceImpl</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午10:09:00</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public class RoleManageServiceImpl implements IRoleManageService {
	private IRoleManageDao roleManageDao;

	public IRoleManageDao getRoleManageDao() {
		return roleManageDao;
	}

	public void setRoleManageDao(IRoleManageDao roleManageDao) {
		this.roleManageDao = roleManageDao;
	}

	@Override
	public int updateRole(TRoleBO role) throws SQLException {
		return roleManageDao.updateRole(role);	
	}
	@Override
	public int insertRole(TRoleBO role) throws SQLException {
		return roleManageDao.insertRole(role);
	}
	@Override
	public int deleteRole(int roleId) throws SQLException {
		return roleManageDao.deleteRole(roleId);		
	}

	@Override
	public TRoleBO findRoleById(int roleId) throws SQLException {
		return roleManageDao.findRoleById(roleId);
	}
	
	@Override
	public TRoleBO findRoleByName(String roleName) throws SQLException {
		return roleManageDao.findRoleByName(roleName);
	}


	@Override
	public List<TRoleBO> findAllRole() throws SQLException {
		return roleManageDao.findAllRole();
	}
	@Override
	public List<TRoleBO> searchRole(QueryCriteria criteria) throws SQLException {
		return roleManageDao.searchRole(criteria);
	}

	@Override
	public int queryUsedRole(int roleID) throws SQLException {
		return roleManageDao.queryUsedRole(roleID);
	}
}
