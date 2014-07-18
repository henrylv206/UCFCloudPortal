package com.skycloud.management.portal.admin.sysmanage.dao;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.exception.SCSException;

/**
 * 用户VLan分配接口
  *<dl>
  *<dt>类名：IUserVlanDao</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-9-22  下午23:46:27</dd>
  *<dd>创建人： 何福康</dd>
  *</dl>
 */
public interface IUserVlanDao {
	int save(TUserVlanBO userVlan)throws SCSException;
	int update(TUserVlanBO userVlan)throws SCSException;
	int delete(int id) throws SCSException;
	int deleteUserVlanByVlanId(long vlanId) throws SCSException;
	List<TUserVlanBO> findAll(TUserVlanBO userV) throws SCSException;
	TUserVlanBO findById(int id) throws SCSException;
	List<TUserVlanBO> findUserVlan(TUserVlanBO userVlan) throws SCSException;
	List<TUserVlanBO> findUserZone(TUserVlanBO userVlan) throws SCSException;

}
