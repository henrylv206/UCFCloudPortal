package com.skycloud.management.portal.admin.sysmanage.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;

/**
 * 用户对象持久化接口
  *<dl>
  *<dt>类名：IUserManageDao</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午09:56:50</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public interface IUserManageDao {
	/**
	 * 用户列表显示
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:57:46
	 */
	List<TUserBO> userALL() throws SQLException ;
	
	/**
	 * 保存用户
	 * @param user
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:58:02
	 */
	int saveUser(TUserBO user)throws SQLException;
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:58:15
	 */
	int udpateUser(TUserBO user)throws SQLException;
	
	/**
	 * 删除用户
	 * @param userId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:58:28
	 */
	int deleteUser(Integer userId)throws SQLException;
	
	/**
	 * 查找用户详情
	 * @param userId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:58:38
	 */
	TUserBO  findUserById(int userId) throws SQLException;
	
	/**
	 * 条件搜索用户
	 * @param criteria
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:58:55
	 */
	List<TUserBO> searchUser(QueryCriteria criteria) throws SQLException ;
	
	/**
	 * 通过账号查找用户
	 * @param account
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:59:13
	 */
	TUserBO findUserByAccout(String account)throws SQLException;
	
	/**
	 * 修改用户密码
	 * @param user
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午09:59:37
	 */
	int updateUserPwd(TUserBO user) throws SQLException;
	
	/**
	 * 查询全部前台用户，用于用户信息导出
	 * @throws Exception 
	 */
	public List<TUserBO> queryUserForFront(TUserBO searchUser)throws SQLException;
	
	/**
	 * 查询角色为1和2的所有用户
	 * @return
	 * @throws SQLException
	 */
	public List<TUserBO> queryUserForAsync() throws SQLException;
	
	/**
	 * 通过orderId查找下订单人的详情
	 * @param account
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-6-25  上午10:59:58
	 */
	TUserBO findUserByOrderId(int orderId)throws SQLException;

	int checkEmail(String email) throws SQLException;

	int saveSelfcaerUser(TUserBO user) throws SQLException;

	int saveSelfcaerUserInfo(TUserBO user) throws SQLException;

	int updateUserEmail(TUserBO user) throws SQLException;

	int updateUserDynPwd(TUserBO user) throws SQLException;
	
	int saveCompany(TCompanyInfo company) throws SQLException;

	List<TUserBO> queryUserForAsync(TUserBO user) throws SQLException;

}
