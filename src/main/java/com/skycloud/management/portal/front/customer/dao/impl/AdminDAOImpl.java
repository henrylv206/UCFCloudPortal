package com.skycloud.management.portal.front.customer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.customer.dao.IAdminDAO;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;

public class AdminDAOImpl extends SpringJDBCBaseDao implements IAdminDAO {
	private static Log log = LogFactory.getLog(AdminDAOImpl.class);
	/**
	 * 数据库校验用户名密码,私有云用户登录自服务  to fix 1970
	 */
	@Override
	public TUserBO checkUser(final String username,final String password) {
		/*String sql = "SELECT `T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, "
			+" `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, `T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, "
			+" `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`,"
			+" `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`,"
			+" `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`,"
			+" `T_SCS_ROLE`.`ROLE_DESCR`, `T_SCS_ROLE`.`CREATOR_USER_ID`, `T_SCS_ROLE`.`CREATE_TIME`, `T_SCS_ROLE`.`LASTUPDATE_DT`,"
			+" `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL` FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID` "
			+"  AND DEPT_ID>1 AND  T_SCS_USER.`ACCOUNT`=? and  T_SCS_USER.`PWD`=? AND  (T_SCS_USER.STATE=? OR T_SCS_USER.STATE=?)";*/
		
		String sql = "SELECT `T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, "
				+" `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, `T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, "
				+" `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`,"
				+" `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`,"
				+" `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`,"
				+" `T_SCS_ROLE`.`ROLE_DESCR`, `T_SCS_ROLE`.`CREATOR_USER_ID`, `T_SCS_ROLE`.`CREATE_TIME`, `T_SCS_ROLE`.`LASTUPDATE_DT`,"
				+" `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL` FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID` "
				+"  AND DEPT_ID>1 AND  T_SCS_USER.`ACCOUNT`=? and  T_SCS_USER.`PWD`=? AND  (T_SCS_USER.STATE=? OR T_SCS_USER.STATE=?)";
		List<TUserBO> userList = null;
		TUserBO user = null;
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		try {

			try {
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, username);
						ps.setString(2, password);
						ps.setInt(3, CompanyCheckStateEnum.SUCCESS);
						ps.setInt(4, CompanyCheckStateEnum.PAUSE);
					}
				}, userRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0){
				user = userList.get(0);
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**数据库校验用户名密码,注册用户前后台排重,只用于公有云用户登录登录自服务
	 * To Fix Bug Id:[1391][1697]
	 */
	//to fix 1970
	@Override
	public TUserBO checkUser(final String username,final String password,final int deptId) {
		String sql = "SELECT `T_SCS_USER`.`COMP_ID`,`T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, "
			+" `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, `T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, "
			+" `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`,"
			+" `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`,"
			+" `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`,`T_SCS_USER`.`COMP_ID`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`,"
			+" `T_SCS_ROLE`.`ROLE_DESCR`, `T_SCS_ROLE`.`CREATOR_USER_ID`, `T_SCS_ROLE`.`CREATE_TIME`, `T_SCS_ROLE`.`LASTUPDATE_DT`,"
			+" `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL` FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID` "
			+" AND T_SCS_USER.DEPT_ID =?  AND  T_SCS_USER.`ACCOUNT`=? and  T_SCS_USER.`PWD`=? AND  T_SCS_USER.STATE in (?,?,?,?)";
		List<TUserBO> userList = null;
		TUserBO user = null;
		log.info(sql);
		log.info("login----username:"+username+",deptId:"+deptId);
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		try {

			try {
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, deptId);
						ps.setString(2, username);
						ps.setString(3, password);
						ps.setInt(4, CompanyCheckStateEnum.SUCCESS);
						ps.setInt(5, CompanyCheckStateEnum.PAUSE);
						ps.setInt(6, CompanyCheckStateEnum.WAITING_FOR_2ND_CHECK);
						ps.setInt(7, CompanyCheckStateEnum.WAITING_ACTIVATE);
					}
				}, userRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0){
				user = userList.get(0);
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*public TUserBO login(final String username, final String password) throws SQLException {
		Object[] args = new Object[2];
		args[0] = username;
		args[1] = password;
		TUserBO user = null;
		try {
			BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);

			String sql = "SELECT `T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`,`T_SCS_USER`.`COMP_ID`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, "+"`T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`, `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`, `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`, `T_SCS_ROLE`.`ROLE_DESCR`, `T_SCS_ROLE`.`CREATOR_USER_ID`, `T_SCS_ROLE`.`CREATE_TIME`, `T_SCS_ROLE`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL` FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID`  WHERE `ACCOUNT`=? and `PWD`=? ";
			List<TUserBO> userList = null;

			try {
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, username);
						ps.setString(2, password);
					}
				}, userRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0)
				user = userList.get(0);
			else
				return null;

			sql = "SELECT  `T_SCS_MENU`.`MENU_ID`, `T_SCS_MENU`.`MENU_NAME`, `T_SCS_MENU`.`MENU_CODE`, `T_SCS_MENU`.`MENU_DESCR`, `T_SCS_MENU`.`PARENT_SCS_MENU_ID`, `T_SCS_MENU`.`ACTION_URL`, `T_SCS_MENU`.`IMG_URL`, `T_SCS_MENU`.`STATE`, `T_SCS_MENU`.`MENU_ORDER` FROM `T_SCS_MENU` JOIN `T_SCS_RRM` ON  `T_SCS_MENU`.`MENU_ID` = `T_SCS_RRM`.`MENU_ID` WHERE `T_SCS_RRM`.`ROLE_ID` = ?;";
			BeanPropertyRowMapper<TMenuBO> menuRowMapper = new BeanPropertyRowMapper<TMenuBO>(TMenuBO.class);

			List<TMenuBO> unsortedMenuList = null;
			try {
				final int roleID=user.getRoleId();
				unsortedMenuList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, roleID);
					}
				}, menuRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询用户菜单。失败原因：" + e.getMessage());
			}
			user.setUnsortedMenuList(unsortedMenuList);

			sql = "SELECT DISTINCT ACTION_URL from T_SCS_MENU WHERE ACTION_URL IS NOT NULL AND ACTION_URL <> ''";

			List<String> allActionURLList = null;
			try {
				allActionURLList = this.getJdbcTemplate().queryForList(sql, String.class);
			} catch (Exception e) {
				throw new SQLException("查询全部功能项失败。失败原因：" + e.getMessage());
			}
			user.setAllActionURL(allActionURLList);
		} catch (Exception e) {
			throw new SQLException("db error");
		}
		return user;
	}
	 **/
	@Override
	public TUserBO findAdminById(final int id){
		String sql = "SELECT `T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`,`T_SCS_USER`.`COMP_ID`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, "+"`T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`, `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`, `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`, `T_SCS_ROLE`.`ROLE_DESCR`, `T_SCS_ROLE`.`CREATOR_USER_ID`, `T_SCS_ROLE`.`CREATE_TIME`, `T_SCS_ROLE`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL` FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID` WHERE `T_SCS_USER`.`ID` = ?";
		List<TUserBO> userList = null;
		TUserBO user = null;
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		try {

			try {
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
					}
				}, userRowMapper);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0){
				user = userList.get(0);
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//to fix 1970 fix bug 3394
	@Override
	public TUserBO findAdminByAccount(final String account){
		String sql = "SELECT `T_SCS_USER`.`COMP_ID`,`T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, "
		        + " `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, `T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, `T_SCS_USER`.`DYN_PWD`,"
			+" `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`,"
			+" `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`,"
			+" `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`,`T_SCS_USER`.`COMP_ID`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`,"
			+" `T_SCS_ROLE`.`ROLE_DESCR`, `T_SCS_ROLE`.`CREATOR_USER_ID`, `T_SCS_ROLE`.`CREATE_TIME`, `T_SCS_ROLE`.`LASTUPDATE_DT`,"
			+" `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL` FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID` "
			+" AND T_SCS_USER.DEPT_ID =1  AND  T_SCS_USER.`ACCOUNT`=? AND T_SCS_USER.state <> 3";
		List<TUserBO> userList = null;
		TUserBO user = null;
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		try {

			try {
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, account);
					}
				}, userRowMapper);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0){
				user = userList.get(0);
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveDynPwd(TUserBO user) {
		String sql = "update T_SCS_USER set DYN_PWD = ? where account = ?";
		return this.getJdbcTemplate().update(sql, new Object[] { user.getDynPwd(), user.getAccount() });
	}
	@Override
	public int savePwd(TUserBO user){
		String sql = "update T_SCS_USER set PWD = ? where ID = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{user.getPwd(),user.getId()});
	}

	@Override
	public int disabledAdmin(int userId){
		//to fix 1705
		String sql = "update T_SCS_USER set STATE = ? , LASTUPDATE_DT=? where ID = ?";
		return this.getJdbcTemplate().update(sql, new Object[]{CompanyCheckStateEnum.FAILURE,new Timestamp(new Date().getTime()),userId});
	}

	/**
	 * 找回密码方法
	 * return state：-1 不存在
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TUserBO findPazzWithState(final String username,final String email,final int deptId)throws Exception {
		int state = -1;
		List<TUserBO> userList = null;
		TUserBO user = null;
		//to fix 1821
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		String sql = "SELECT `T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`,`T_SCS_USER`.`COMP_ID`, `T_SCS_USER`.`PWD`, "
			+" `T_SCS_USER`.`NAME`, `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, "
			+" `T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`,"
			+" `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`, `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, "
			+" `T_SCS_USER`.`CREATOR_USER_ID`, `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT` "
			+" FROM T_SCS_USER WHERE `T_SCS_USER`.`ACCOUNT` = ? AND EMAIL = ? ";
		//fix bug 2276
		if(1==deptId){
			sql += "AND DEPT_ID=1";
		}
		try {
			userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, username);
					ps.setString(2, email);
					//ps.setInt(3, CompanyCheckStateEnum.SUCCESS);
				}
			}, userRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
		}
		if (userList != null && userList.size() > 0){
			user = userList.get(0);
			return user;
		} else {
			return null;
		}
	}

	@Override
	public int updatePassword(String account,String email,String pwd){
		String sql = "update T_SCS_USER set PWD = ? where ACCOUNT = ? and EMAIL =?";
		return this.getJdbcTemplate().update(sql, new Object[]{pwd,account,email});
	}
}
