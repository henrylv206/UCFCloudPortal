package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.sysmanage.dao.IUserManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.DegistUtil;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;

public class UserManageDaoImpl extends SpringJDBCBaseDao implements IUserManageDao{
	private DeptManageDaoImpl deptDao;
	private static Timestamp createDate = null;
	@Override
	public int saveUser( final TUserBO user) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_USER(" +
				       "ID,ACCOUNT,PWD,NAME," +
				       "DEPT_ID,ROLE_ID,EMAIL,PHONE," +
				       "MOBILE,FAX,POSITION,STATE," +
				       "COMMENT,CHECK_CODE,IS_AUTO_APPROVE,CREATOR_USER_ID," +
				       "CREATE_DT,LASTUPDATE_DT,COMP_ID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, user.getId());
					ps.setString(i++, user.getAccount());
					ps.setString(i++, user.getPwd());
					ps.setString(i++, user.getName());
					ps.setInt(i++, user.getDeptId());
					ps.setInt(i++, user.getRoleId());
					ps.setString(i++, user.getEmail());
					ps.setString(i++, user.getPhone());
					ps.setString(i++, user.getMobile());
					ps.setString(i++, user.getFax());
					ps.setString(i++, user.getPosition());
					ps.setInt(i++, user.getState());
					ps.setString(i++, user.getComment());
					ps.setString(i++, user.getCheckCode());
					ps.setInt(i++, user.getIsAutoApprove());
					ps.setInt(i++, user.getCreatorUserId());
					ps.setTimestamp(i++, new Timestamp(user.getCreateDt().getTime()));
					//update by CQ
					ps.setTimestamp(i++, new Timestamp(user.getLastupdateDt().getTime()));
					ps.setInt(i++, user.getCompId());
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			throw new SQLException("创建用户失败。用户描述："+user.getComment()+" 创建人ID："+user.getCreatorUserId()+
					   " 创建时间："+user.getCreateDt()+" 失败原因：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}
	@Override	
	public int saveSelfcaerUser( final TUserBO user) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_USER(" +
				       "ID,ACCOUNT,PWD," +
				       "DEPT_ID,ROLE_ID,EMAIL," +
				       "POSITION,STATE," +
				       "COMMENT,CHECK_CODE,IS_AUTO_APPROVE,CREATOR_USER_ID," +
				       "CREATE_DT,LASTUPDATE_DT,COMP_ID,MOBILE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, user.getId());
					ps.setString(i++, user.getAccount());
					ps.setString(i++, user.getPwd());
//					ps.setString(i++, user.getName());
					ps.setInt(i++, user.getDeptId());
					ps.setInt(i++, user.getRoleId());
					ps.setString(i++, user.getEmail());
					ps.setString(i++, user.getPosition());
					ps.setInt(i++, user.getState());
					ps.setString(i++, user.getComment());
					ps.setString(i++, user.getCheckCode());
					ps.setInt(i++, user.getIsAutoApprove());
					ps.setInt(i++, user.getCreatorUserId());
					ps.setTimestamp(i++, new Timestamp(user.getCreateDt().getTime()));
					//update by CQ
					ps.setTimestamp(i++, new Timestamp(user.getLastupdateDt().getTime()));
					ps.setInt(i++, user.getCompId());
					ps.setString(i++, user.getMobile());
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			throw new SQLException("创建用户失败。用户描述："+user.getComment()+" 创建人ID："+user.getCreatorUserId()+
					   " 创建时间："+user.getCreateDt()+" 失败原因：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}
	
	
	@Override	
	public int saveSelfcaerUserInfo( final TUserBO user) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_COMPANY_USER(" +
				       "ID,ACCOUNT,PWD," +
				       "DEPT_ID,ROLE_ID,EMAIL," +
				       "POSITION,STATE," +
				       "COMMENT,CHECK_CODE,IS_AUTO_APPROVE,CREATOR_USER_ID," +
				       "CREATE_DT,LASTUPDATE_DT) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, user.getId());
					ps.setString(i++, user.getAccount());
					ps.setString(i++, user.getPwd());
//					ps.setString(i++, user.getName());
					ps.setInt(i++, user.getDeptId());
					ps.setInt(i++, user.getRoleId());
					ps.setString(i++, user.getEmail());
					ps.setString(i++, user.getPosition());
					ps.setInt(i++, user.getState());
					ps.setString(i++, user.getComment());
					ps.setString(i++, user.getCheckCode());
					ps.setInt(i++, user.getIsAutoApprove());
					ps.setInt(i++, user.getCreatorUserId());
					ps.setTimestamp(i++, new Timestamp(user.getCreateDt().getTime()));
					//update by CQ
					ps.setTimestamp(i++, new Timestamp(user.getLastupdateDt().getTime()));
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			throw new SQLException("创建用户失败。用户描述："+user.getComment()+" 创建人ID："+user.getCreatorUserId()+
					   " 创建时间："+user.getCreateDt()+" 失败原因：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}
	@Override
	public int udpateUser(final TUserBO user) throws SQLException {
		StringBuilder sql = new StringBuilder("update T_SCS_USER set NAME=?,DEPT_ID=?,ROLE_ID=?,");
		sql.append(" EMAIL=?,PHONE=?,MOBILE=?,FAX=?,");
		sql.append(" LASTUPDATE_DT=?,STATE=?,CHECK_CODE=?");
		if(StringUtils.isNotEmpty(user.getPwd())){
			sql.append(" ,PWD=?");
		}		
		sql.append(" where ID=?");
		int result=0;
		try {
			result=this.getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
				int i = 1;
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, user.getName());
					ps.setInt(i++, user.getDeptId());
					ps.setInt(i++, user.getRoleId());
					ps.setString(i++, user.getEmail());
					ps.setString(i++, user.getPhone());
					ps.setString(i++, user.getMobile());
					ps.setString(i++, user.getFax());
					ps.setTimestamp(i++, new Timestamp(user.getLastupdateDt().getTime()));
					ps.setInt(i++, user.getState());
					ps.setString(i++, user.getCheckCode());//fix buf 3040 增加用户激活功能
					if(StringUtils.isNotEmpty(user.getPwd())){
						ps.setString(i++,DegistUtil.md5(user.getPwd()));
					}
					ps.setInt(i++, user.getId());
				}
			});
		} catch (Exception e) {
			throw new SQLException("更新用户失败。 更新时间："
					+ user.getLastupdateDt() + "  失败原因：" + e.getMessage());
		}
		return result;
	}
	@Override
	public int deleteUser(final Integer userId) throws SQLException {
		String sql="delete from T_SCS_USER where ID=?;";
		try {
			this.getJdbcTemplate().update(sql,new PreparedStatementSetter(){
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, userId);
				}
			});
		} catch (Exception e) {
			throw new SQLException("删除用户失败，用户ID："+userId+",  失败原因："+e.getMessage());
		}
		return 0;
	}
	
	@Override
	public List<TUserBO> userALL() throws SQLException {
//		String sql="select u.ID,u.ACCOUNT,u.PWD,u.NAME,u.DEPT_ID,u.ROLE_ID,u.EMAIL,u.PHONE,u.MOBILE," +
//	       "u.FAX,u.POSITION,u.STATE,u.COMMENT,u.CHECK_CODE,u.IS_AUTO_APPROVE," +
//	       "u.CREATOR_USER_ID,u.CREATE_DT,u.LASTUPDATE_DT,c.NAME CREATOR_USER_NAME " +
//	       "from T_SCS_USER u " +
//	       "LEFT JOIN T_SCS_USER c on u.CREATOR_USER_ID=c.ID order by u.ID desc;";
		String sql="select u.* from T_SCS_USER u where u.DEPT_ID<>1 order by u.ID desc;";
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		List<TUserBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new RowMapperResultSetExtractor<TUserBO>(userRowMapper));
		} catch (Exception e) {
			throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}
	
	@Override
	public TUserBO findUserById(final int userId) throws SQLException {
		String sql="select u.ID,u.ACCOUNT,u.PWD,u.NAME,u.DEPT_ID,u.ROLE_ID,u.EMAIL,u.PHONE,u.MOBILE," +
				       "u.FAX,u.POSITION,u.STATE,u.COMMENT,u.CHECK_CODE,u.IS_AUTO_APPROVE," +
				       "u.CREATOR_USER_ID,u.CREATE_DT,u.LASTUPDATE_DT, r.ROLE_NAME " +
				       "from T_SCS_USER u LEFT JOIN " +
				       "T_SCS_ROLE r on u.ROLE_ID=r.ROLE_ID  where u.ID=?;";
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class); 
		List<TUserBO> returnList = null;
		TUserBO user = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, userId);
				}
			},userRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用戶失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			user= returnList.get(0);
		else
			return null;
		user.setDept(deptDao.findDeptById(user.getDeptId()));
		return user;
	}
	@Override
	public List<TUserBO> searchUser(final QueryCriteria criteria)throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select ID,ACCOUNT,NAME from T_SCS_USER where 1 = 1 and DEPT_ID<>1 ");		
		if(criteria.getUserName()!=null&&criteria.getUserName()!="") 
			sql.append(" and ACCOUNT LIKE ? ");		
		sql.append(" order by ID desc; ");		
		/*if(criteria.getDeptId()>-1) 
			sql.append(" and DEPT_ID = ? ");		 
		if(criteria.getRoleId()>-1) 
			sql.append(" and ROLE_ID = ? ");		 
		if(criteria.getStartTime()!=null && criteria.getEndTime()!=null) 
			sql.append(" and CREATE_DT between ? and ? "); */
		
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class); 
		List<TUserBO> returnList = null;
		
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {			
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					if(criteria.getUserName()!=null&&criteria.getUserName()!="")
						ps.setString(i++, "%"+criteria.getUserName()+"%");
					/*if(criteria.getDeptId()>-1) 
						ps.setInt(i++, criteria.getDeptId());
					if(criteria.getRoleId()>-1) 
						ps.setInt(i++, criteria.getRoleId());
					if(criteria.getStartTime()!=null && criteria.getEndTime()!=null){ 
						ps.setDate(i++, criteria.getStartTime());
					    ps.setDate(i++, criteria.getEndTime());
					}else if(criteria.getStartTime()==null && criteria.getEndTime()!=null){
						ps.setDate(i++, criteria.getStartTime());
						  ps.setDate(i++, criteria.getEndTime());
					}else{
						ps.setDate(i++, criteria.getStartTime());
					    ps.setDate(i++, criteria.getEndTime());
					}*/
				}
			},userRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用户失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}
	@Override
	/*注册用户前后台排重
	 * To Fix Bug Id:[1391][1697]
	 */

	public TUserBO findUserByAccout(final String account) throws SQLException {
		String sql = "select u.ID,u.ACCOUNT,u.PWD,u.NAME,u.DEPT_ID,u.ROLE_ID,u.EMAIL,u.PHONE,u.MOBILE,"
				+ "u.FAX,u.POSITION,u.STATE,u.COMMENT,u.CHECK_CODE,u.IS_AUTO_APPROVE,"
				+ "u.CREATOR_USER_ID,u.CREATE_DT,u.LASTUPDATE_DT,c.NAME CREATOR_USER_NAME "
				+ "from T_SCS_USER u LEFT JOIN  "
				+ "T_SCS_USER c on u.CREATOR_USER_ID=c.ID where u.ACCOUNT=? AND u.DEPT_ID>1;";
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class); 
		List<TUserBO> returnList = null;
		TUserBO user = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, account);
						}
					}, userRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用戶失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			user= returnList.get(0);
		else
			return null;
		user.setDept(deptDao.findDeptById(user.getDeptId()));
		return user;
	}
	public void setDeptDao(DeptManageDaoImpl deptDao) {
		this.deptDao = deptDao;
	}
	public DeptManageDaoImpl getDeptDao() {
		return deptDao;
	}
	@Override
	public int updateUserPwd(final TUserBO user) throws SQLException {
		int ret = 0;
		String sql = "update T_SCS_USER set PWD=?, LASTUPDATE_DT=? where ID=?";
		try {
			ret = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, user.getPwd());
					ps.setTimestamp(i++, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
					ps.setInt(i++, user.getId());
				}
			});
		} catch (Exception e) {
			throw new SQLException("更新用户密码失败。 更新时间：" + user.getLastupdateDt()
					+ "  失败原因：" + e.getMessage());
		}
		return ret;
	}
	
	@Override
	public int updateUserDynPwd(final TUserBO user) throws SQLException {
		int ret = 0;
		String sql = "update T_SCS_USER set DYN_PWD=?, LASTUPDATE_DT=? where ID=?";
		try {
			ret = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, user.getDecPwd());
					ps.setTimestamp(i++, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
					ps.setInt(i++, user.getId());
				}
			});
		} catch (Exception e) {
			throw new SQLException("更新用户密码失败。 更新时间：" + user.getLastupdateDt()
					+ "  失败原因：" + e.getMessage());
		}
		return ret;
	}

	@Override	
	public List<TUserBO> queryUserForFront(TUserBO searchUser) throws SQLException {		
		String query = "SELECT `T_SCS_USER`.`ID`, `T_SCS_USER`.`ACCOUNT`, `T_SCS_USER`.`PWD`, `T_SCS_USER`.`NAME`, "
			+" `T_SCS_USER`.`DEPT_ID`, `T_SCS_USER`.`ROLE_ID`, `T_SCS_USER`.`EMAIL`, `T_SCS_USER`.`PHONE`, "
			+" `T_SCS_USER`.`MOBILE`, `T_SCS_USER`.`FAX`, `T_SCS_USER`.`POSITION`, `T_SCS_USER`.`STATE`, `T_SCS_USER`.`COMMENT`,"
			+" `T_SCS_USER`.`CHECK_CODE`, IFNULL(IS_AUTO_APPROVE,0) as  IS_AUTO_APPROVE, `T_SCS_USER`.`CREATOR_USER_ID`,"
			+" `T_SCS_USER`.`CREATE_DT`, `T_SCS_USER`.`LASTUPDATE_DT`, `T_SCS_ROLE`.`ROLE_ID`, `T_SCS_ROLE`.`ROLE_NAME`,"
			+" `T_SCS_ROLE`.`ROLE_APPROVE_LEVEL`,T_SCS_DEPARTMENT.DEPT_NAME, `T_SCS_COMPANY_INFO`.`COMP_CN_NAME`"
			+" FROM `T_SCS_USER` JOIN `T_SCS_ROLE` ON `T_SCS_USER`.`ROLE_ID` = `T_SCS_ROLE`.`ROLE_ID` "			
			+" JOIN T_SCS_DEPARTMENT ON `T_SCS_USER`.`DEPT_ID` = `T_SCS_DEPARTMENT`.`DEPT_ID` "
			+" LEFT JOIN `T_SCS_COMPANY_INFO` ON `T_SCS_USER`.`COMP_ID` = `T_SCS_COMPANY_INFO`.`COMP_ID` "
			+" AND  (T_SCS_USER.STATE=?)";
		StringBuilder sql = new StringBuilder(500);
		sql.append(query);
		if(null!=searchUser){
			if(searchUser.getId()>0){
				sql.append(" AND T_SCS_USER.ID = "+searchUser.getId());
			}
			if(StringUtils.isNotEmpty(searchUser.getAccount())){
				sql.append(" AND T_SCS_USER.ACCOUNT like %"+searchUser.getId()+"%");
			}
			if(StringUtils.isNotEmpty(searchUser.getName())){
				sql.append(" AND T_SCS_USER.NAME like %"+searchUser.getId()+"%");
			}
			if(searchUser.getDeptId()>0){
				sql.append(" AND T_SCS_USER.DEPT_ID = "+searchUser.getDeptId());
			}
			if(searchUser.getRoleId()>0){
				sql.append(" AND T_SCS_USER.ROLE_ID = "+searchUser.getRoleId());
			}
			if(searchUser.getCompId()>0){
				sql.append(" AND T_SCS_USER.COMP_ID = "+searchUser.getCompId());
			}
		}
		sql.append(" ORDER BY T_SCS_USER.ID");
		List<TUserBO> userList = null;
		TUserBO user = null;
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
				
		userList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, CompanyCheckStateEnum.SUCCESS);
			}
		}, userRowMapper);			

		return userList;
	}
	
	@Override
	public List<TUserBO> queryUserForAsync() throws SQLException {
		//fix bug 5006
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT b.* FROM `T_SCS_ROLE` a,`T_SCS_USER` b ");
		sql.append("where a.role_approve_level in (1,2) ");
		sql.append("and a.role_id = b.role_id and b.state = "+CompanyCheckStateEnum.SUCCESS);
//		Date currDate = new Date(System.currentTimeMillis());	    
//		sql.append(" and b.LASTUPDATE_DT <= '"+new Timestamp(currDate.getTime())+"'");
//		if(null!=createDate){
//			sql.append(" and b.LASTUPDATE_DT >'"+createDate+"'");
//		}
//		System.out.println(createDate);
//		System.out.println(currDate);
//		System.out.println(sql.toString());
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		List<TUserBO> userList = null;
		userList = this.getJdbcTemplate().query(sql.toString(), userRowMapper);
//		createDate = new Timestamp(currDate.getTime());
		return userList;
	}
	@Override
	public List<TUserBO> queryUserForAsync(final TUserBO user) throws SQLException {
		//fix bug 5006
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT b.* FROM `T_SCS_ROLE` a,`T_SCS_USER` b ");
		sql.append("where a.role_approve_level in (1,2) ");
		sql.append("and a.role_id = b.role_id and b.state = "+CompanyCheckStateEnum.SUCCESS);
		if(StringUtils.isNotEmpty(user.getStartLastUpdate())){
			sql.append(" AND b.LASTUPDATE_DT >= ?");
		}
		if(StringUtils.isNotEmpty(user.getEndLastUpdate())){
			sql.append(" AND b.LASTUPDATE_DT < ?");
		}
		if(StringUtils.isNotEmpty(user.getAccount())){
			sql.append(" AND b.ACCOUNT like '%"+user.getAccount()+"%'");
		}

		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		List<TUserBO> userList = null;
		userList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				if(StringUtils.isNotEmpty(user.getStartLastUpdate())){
					ps.setString(i++, user.getStartLastUpdate());
				}
				if(StringUtils.isNotEmpty(user.getEndLastUpdate())){
					ps.setString(i++, user.getEndLastUpdate());
				}
//				if(StringUtils.isNotEmpty(user.getAccount())){
//					ps.setString(i++, "'%"+user.getAccount()+"%'");
//				}
			}
		}, userRowMapper);
//		createDate = new Timestamp(currDate.getTime());
		return userList;
	}
	@Override
	public TUserBO findUserByOrderId(final int orderId) throws SQLException {
		String sql = "select u.ID,u.ACCOUNT,u.PWD,u.NAME,u.DEPT_ID,u.ROLE_ID,u.EMAIL,u.PHONE,u.MOBILE,"
				+ "u.FAX,u.POSITION,u.STATE,u.COMMENT,u.CHECK_CODE,u.IS_AUTO_APPROVE,"
				+ "u.CREATOR_USER_ID,u.CREATE_DT,u.LASTUPDATE_DT "
				+ "from T_SCS_ORDER o  JOIN  "
				+ "T_SCS_USER u on o.CREATOR_USER_ID=u.ID where o.ORDER_ID=? ";
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(
				TUserBO.class);
		List<TUserBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setInt(1, orderId);
						}
					}, userRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用戶失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			return returnList.get(0);
		else
			return null;
	}
	@Override
	public int checkEmail(String email)throws SQLException{
		int count = 0;
		try {
			String sql = "select count(*) from T_SCS_USER where EMAIL= ? AND state <>3";
			count = this.getJdbcTemplate().queryForInt(sql, new Object[]{email});
		} catch (Exception e) {
			throw new SQLException("select user's email error ：" + e.getMessage());
		}
		return count;
	}
	@Override
	public int updateUserEmail(TUserBO user)throws SQLException{
		int count = 0;
		try {
			String sql = "update T_SCS_USER set EMAIL= ? where ID= ?";
			count = this.getJdbcTemplate().update(sql, new Object[]{user.getEmail(),user.getId()});
		} catch (Exception e) {
			throw new SQLException("select user's email error ：" + e.getMessage());
		}
		return count;
	}
	
	@Override
	public int saveCompany(final TCompanyInfo tcompanyinfo) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();			
		final String sql = "insert into T_SCS_COMPANY_INFO (COMP_LEGAL_PERSON,COMP_CN_NAME,"
			+"COMP_LEGAL_PERSON_ID,"
			+" COMP_ADDRESS,POST_CODE,"
			+" COMP_PHONE,RELA_MOBILE,COMP_FAX,COMP_EMAIL,"
			+" COMP_ORG_CODE,BUS_LICENSE_NUM, "
			+" COMP_BANK_NAME,COMP_BANK_ACCOUNT,FIR_CHECK_COMMENT,CHECK_STATE,BLN_START_TIME,BLN_END_TIME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,4,?,?);";

			try {
				this.getJdbcTemplate().update(new PreparedStatementCreator(){
					int i=1;
					@Override
					public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						ps.setString(i++, tcompanyinfo.getCompLegalPerson());	
						ps.setString(i++, tcompanyinfo.getCompCnName());						
						ps.setString(i++, tcompanyinfo.getCompLegalPersonId());
						ps.setString(i++, tcompanyinfo.getCompAddress());
						ps.setString(i++, tcompanyinfo.getPostCode());
						ps.setString(i++, tcompanyinfo.getCompPhone());
						ps.setString(i++, tcompanyinfo.getRelaMobile());
						ps.setString(i++, tcompanyinfo.getCompFax());
						ps.setString(i++, tcompanyinfo.getCompEmail());
						ps.setString(i++, tcompanyinfo.getCompOrgCode());
						ps.setString(i++, tcompanyinfo.getBusLicenseNum());
						ps.setString(i++, tcompanyinfo.getCompBankName());
						ps.setString(i++, tcompanyinfo.getCompBankAccount());
						ps.setString(i++, tcompanyinfo.getFirCheckComment());
						ps.setTimestamp(i++, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
						ps.setTimestamp(i++, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
						return ps;
					}
				}, keyHolder); 
		} catch (Exception e) {		
			e.printStackTrace();
			throw new SQLException("创建企业失败。");
		}
		return keyHolder.getKey().intValue();
	}
}
  
