package com.skycloud.management.portal.admin.sysmanage.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;

/**
 * 跟elaster同步用户接口
  *<dl>
  *<dt>类名：IUserAsyncService</dt>
  *<dd>描述: 跟elaster同步用户接口</dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-5-17  下午16:00:00</dd>
  *<dd>创建人： 张慧征</dd>
  *</dl>
 */
public interface IUserAsyncService {
   
   /**
    * 创建用户
    * @param user
    * @return
    * @throws SQLException
    * 创建人：   张慧征
    * 创建时间：2012-5-17  下午16:00:00
    */
//   int insertNotAsyncUser(Integer resourcePoolsId) throws SQLException;
         
   /**
    * 为所有资源池同步用户
    * @return
    */
   int addNotAsyncUserForAllResourcePools();
}
