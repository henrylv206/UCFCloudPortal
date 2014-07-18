package com.skycloud.management.portal.admin.sysmanage.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ENetwork;

/**
 * VLAN管理业务接口
  *<dl>
  *<dt>类名：IUserVlanService</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-10-15  上午16:00:00</dd>
  *<dd>创建人： 何福康</dd>
  *</dl>
 */
public interface IUserVlanService {

	int save(TUserVlanBO userVlan)throws SCSException;
	int update(TUserVlanBO userVlan)throws SCSException;
	int delete(int id) throws Exception;
	int deleteUserVlanByVlanId(long vlanId) throws SCSException;
	List<TUserVlanBO> findAll(TUserVlanBO userV) throws SCSException;
	TUserVlanBO findById(int id) throws SCSException;
	List<TUserVlanBO> findUserVlan(TUserVlanBO userVlan) throws SCSException;
	List<TUserVlanBO> findUserZone(TUserVlanBO userVlan) throws SCSException;
	int searchNicssCountByvlanId(int vlanId) throws SQLException;
	public List<ENetwork> listNetworksIpRangeByZoneId(int zoneId,boolean isdefault,int resourcePoolsId) throws Exception ;
	public int checkbindVpnInstance(int userId,int vlanId) throws Exception ;
	public int checkMenuPublicNetworkIp()  throws Exception ;
	/*
	 * 列出资源池所有记录ResourcePools
	 * 参数  无
	 * return List<TResourcePoolsBO>对象
	 * 生成时间：2011-12-22 14:58
	 */
	public List<TResourcePoolsBO> listAllResourcePools() throws SQLException ;

	/*
	 * 列出ResourcePools
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return String对象
	 */
	public String getResourcePoolNameById(int resourcePoolsId) throws SQLException;
	/**
	 * 根据网络类型对应配置参数的tag标签过滤网卡信息
	 * @param networkList
	 * @param networkType
	 * @return
	 * @throws Exception
	 */
	public List<ENetwork> listNetworksByTypeTag(List<ENetwork> networkList,int networkType) throws Exception ;

}
