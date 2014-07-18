package com.skycloud.management.portal.admin.sysmanage.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

/**
 * 用户管理业务接口
  *<dl>
  *<dt>类名：IUserManageService</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午10:10:03</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public interface IUserManageService {
	
	/**
	 * 获得用户列表
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-2-13  上午10:10:22
	 */
   List<TUserBO> getAllUsers() throws SQLException;
   
   /**
    * 获得用户信息
    * @param userID
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:26
    */
   TUserBO getUserById(int userID) throws SQLException;
   
   /**
    * 通过用户账户获得用户
    * @param account
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:29
    */
   TUserBO getUserByAccout(String account) throws SQLException;
   
   /**
    * 创建用户
    * @param user
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:33
    */
   int insertUser(TUserBO user) throws SQLException;
   
   /**
    * 更新用户
    * @param user
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:37
    */
   int updateUser(TUserBO user) throws Exception;
   
   /**
    * 删除用户
    * @param userID
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:39
    */
   int deleteUser(Integer userID) throws SQLException;
   
   /**
    * 查询用户
    * @param criteria
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:42
    */
   List<TUserBO> queryUser(QueryCriteria criteria) throws SQLException;
   
   /**
    * 修改用户密码
    * @param user
    * @return
    * @throws SQLException
    * 创建人：   张爽    
    * 创建时间：2012-2-13  上午10:10:45
    */
   int updateUserPwd(TUserBO user) throws SQLException;

   List<TUserBO> queryUserForFront() throws SQLException;

   String exportUser(List<TUserBO> list);

   public List<ResourcesVO> getAllInstanceByUser(TUserBO user) throws SCSException;

   boolean checkEmailIsExist(String email);

   
   /**
    * 创建用户
    * @param user
    * @return
    * @throws SQLException
    * 创建人：   鲁丹    
    * 创建时间：2012-8-14  下午17:08:25
    */
int registerUser(TUserBO user,TCompanyInfo company) throws SQLException;

int updateUserEmail(TUserBO user)throws SQLException;

void deleteResourcePoolUser(Integer userID);

List<Map<String,Object>> queryNotExistUserAsync(TUserBO user) throws Exception;

int manualAsyncUser(TUserBO user) throws SQLException;

void updateObjectStorePwd(TUserBO user) throws SQLException;

//int registerUserSelfinfo(TUserBO user) throws SQLException;


}
