package com.skycloud.management.portal.front.log.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.customer.utils.Utils;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.dao.IUserLogDao;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;

public class UserLogDaoImpl extends SpringJDBCBaseDao implements IUserLogDao {

	private static String ERROR_MESSAGE_PORTAL_OPERLOG_DAO_CREATE = "保存操作日志错误%s";

	private PreparedStatementCreator PreparedSaveSQLArgs(final TUserLogVO log, final String sql) {
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index=1;
				ps.setInt(index++, log.getId());
				ps.setInt(index++, log.getUserId());
				ps.setInt(index++, log.getRoleId());
				ps.setString(index++, log.getClassName());
				ps.setString(index++, log.getMethodName());

				ps.setString(index++, log.getParameters());
				ps.setString(index++, log.getModuleName());
				ps.setString(index++, log.getFunctionName());
				ps.setTimestamp(index++, new Timestamp(log.getCreateDt() == null ? System.currentTimeMillis() : log.getCreateDt().getTime()));
				ps.setInt(index++, log.getType());

				ps.setString(index++, log.getComment());
				ps.setString(index++, log.getIp());
				ps.setString(index++, log.getMemo());
				//新增字段
				ps.setString(index++, log.getTargetType());
				ps.setString(index++, log.getSubject());
				ps.setString(index++, log.getFroms());
				ps.setString(index++, log.getTos());
				ps.setString(index++, log.getUserName());
				ps.setInt(index++, log.getStatus());
				ps.setString(index++, log.getStatusName());
				return ps;
			}
		};
	}

	@Override
	public int save(TUserLogVO log) throws SCSException {
		int ret_val = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO T_SCS_USER_LOG(ID,USER_ID,ROLE_ID,CLASS_NAME,METHOD_NAME" 
				  + ",PARAMETERS,MODULE_NAME,FUNCTION_NAME,CREATE_DT,TYPE,COMMENT,IP,MEMO"
				  + ",TARGET_TYPE,SUBJECT,FROMS,TOS,USER_NAME,STATUS,STATUS_NAME"//新增字段
		          + ") VALUES ("
		          + "?,?,?,?,?"
		          + ",?,?,?,?,?,?,?,?"
		          + ",?,?,?,?,?,?,?"
		          + ");";

		try {
			PreparedStatementCreator preCreator = PreparedSaveSQLArgs(log, sql);
			this.getJdbcTemplate().update(preCreator, keyHolder);
			ret_val = keyHolder.getKey().intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SCSException(String.format(ERROR_MESSAGE_PORTAL_OPERLOG_DAO_CREATE, e.getMessage()));
		}
		return ret_val;
	}

	@Override
	public int delete(final int id) throws SQLException {
		String sql = "DELETE FROM T_SCS_USER_LOG WHERE ID=?";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		});

	}

	@Override
	public int update(final TUserLogVO log) throws SQLException {
		int ret_val = -1;
		String sql = " UPDATE T_SCS_USER_LOG SET STATUS = ? , STATUS_NAME = ? WHERE ID = ? ";

		ret_val =  this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int index =1;
				ps.setInt(index++, log.getStatus());
				ps.setString(index++, log.getStatusName());
				ps.setInt(index++, log.getId());
			}
		});
			
		return ret_val;
	}

	@Override
	public List<TUserLogVO> searchAllUserLog(final PageVO vo) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT l.ID ,l.CREATE_DT,l.MODULE_NAME,l.FUNCTION_NAME,r.ACCOUNT FROM T_SCS_USER_LOG l JOIN T_SCS_USER r ON l.USER_ID=r.ID  ORDER BY l.CREATE_DT DESC ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TUserLogVO> logRowMapper = new BeanPropertyRowMapper<TUserLogVO>(TUserLogVO.class);
		List<TUserLogVO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					if (page != null) {
						int curPage = page.getCurPage();
						int pageSize = page.getPageSize();
						if (curPage > 0 && pageSize > 0) {
							ps.setInt(1, (curPage - 1) * pageSize);
							ps.setInt(2, pageSize);
						}
					}
				}
			}, logRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询用户操作日志信息失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int searchAllUserLogAcount(final TUserLogVO vo) throws SQLException {
		List<Object> param = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(0) FROM T_SCS_USER_LOG l  where 1 = 1 ");
		if (vo!=null){
			if (vo.getAccount() != null && vo.getAccount() != "" && vo.getAccount().trim() != "") {
				sql.append(" and l.USER_ID = (select r.ID  from T_SCS_USER r where r.ACCOUNT = ? )");
				param.add(vo.getAccount().trim());
			}
			if (vo.getType() > 0) {
				sql.append(" and l.TYPE = ? ");
				param.add(vo.getType());
			}
			if(Utils.noEmpty(vo.getTargetType())){
				sql.append(" AND l.TARGET_TYPE = ? ");
				param.add(vo.getTargetType());
			}
			if (vo.getModuleName() != null && vo.getModuleName() != "" && vo.getModuleName().trim() != "") {
				sql.append(" and l.MODULE_NAME LIKE ? ");
				param.add("%" + vo.getModuleName().trim() + "%");
			}
			if (vo.getFunctionName() != null && vo.getFunctionName() != "" && vo.getFunctionName().trim() != "") {
				sql.append(" and l.FUNCTION_NAME LIKE ? ");
				param.add("%" + vo.getFunctionName().trim() + "%");
			}
			if (vo.getStartDate() != null && !"".equals(vo.getStartDate())) {
				sql.append(" and l.CREATE_DT >=? ");
				param.add(vo.getStartDate() + " 00:00:00");
			}
			if (vo.getEndDate() != null && !"".equals(vo.getEndDate())) {
				sql.append(" and l.CREATE_DT <=? ");
				param.add(vo.getEndDate() + " 23:59:59");
			}
			if (vo.getStatus() > 0) {
				sql.append(" and l.STATUS = ? ");
				param.add(vo.getStatus());
			}
		}
		
		sql.append(" ORDER BY l.CREATE_DT DESC; ");
		return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
	}

	@Override
	public TUserLogVO findUserLogById(final int id) throws SQLException {
		String sql = "SELECT l.ID ,l.USER_ID,l.ROLE_ID,l.METHOD_NAME,l.CLASS_NAME,l.PARAMETERS,l.FUNCTION_NAME,l.MODULE_NAME,l.IP,l.TYPE,l.`COMMENT`,l.CREATE_DT,l.MEMO,r.ACCOUNT FROM T_SCS_USER_LOG l JOIN T_SCS_USER r ON l.USER_ID=r.ID WHERE l.ID=?;";
		BeanPropertyRowMapper<TUserLogVO> logRowMapper = new BeanPropertyRowMapper<TUserLogVO>(TUserLogVO.class);
		List<TUserLogVO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, id);

				}
			}, logRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询单条用户操作日志信息失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		}
		return null;
	}

	@Override
	public List<TUserLogVO> searchUserLogByCondition(final PageVO vo, final String account, final Integer type, final String moduleName,
	        final String startDate, final String endDate, final String functionName) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT l.ID ,l.CREATE_DT,l.MODULE_NAME,l.FUNCTION_NAME,l.IP,l.PARAMETERS,l.COMMENT,r.ACCOUNT FROM T_SCS_USER_LOG l JOIN T_SCS_USER r ON l.USER_ID=r.ID  where 1 = 1 ");
		if (account != null && account != "" && account.trim() != "") {
			sql.append(" and r.ACCOUNT LIKE ? ");
		}
		if (type != null && type > -1) {
			sql.append(" and l.TYPE = ? ");
		}
		if (moduleName != null && moduleName != "" && moduleName.trim() != "") {
			sql.append(" and l.MODULE_NAME LIKE ? ");
		}
		if (functionName != null && functionName != "" && functionName.trim() != "") {
			sql.append(" and l.FUNCTION_NAME LIKE ? ");
		}
		if (startDate != null && !"".equals(startDate)) {
			sql.append(" and l.CREATE_DT >=? ");
		}
		if (endDate != null && !"".equals(endDate)) {
			sql.append(" and l.CREATE_DT <=? ");
		}
		sql.append(" ORDER BY l.CREATE_DT DESC  ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TUserLogVO> userLogRowMapper = new BeanPropertyRowMapper<TUserLogVO>(TUserLogVO.class);
		List<TUserLogVO> returnList = null;

		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					if (account != null && account != "" && account.trim() != "") {
						ps.setString(i++, "%" + account.trim() + "%");
					}
					if (type != null && type > -1) {
						ps.setInt(i++, type);
					}
					if (moduleName != null && moduleName != "" && moduleName.trim() != "") {
						ps.setString(i++, "%" + moduleName.trim() + "%");
					}
					if (functionName != null && functionName != "" && functionName.trim() != "") {
						ps.setString(i++, "%" + functionName.trim() + "%");
					}
					if (startDate != null && !"".equals(startDate)) {
						ps.setString(i++, startDate + " 00:00:00");
					}
					if (endDate != null && !"".equals(endDate)) {
						ps.setString(i++, endDate + " 23:59:59");
					}
					if (page != null) {
						int curPage = page.getCurPage();
						int pageSize = page.getPageSize();
						if (curPage > 0 && pageSize > 0) {
							ps.setInt(i++, (curPage - 1) * pageSize);
							ps.setInt(i++, pageSize);
						}
					}
				}
			}, userLogRowMapper);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询用户操作日志失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<TUserLogVO> getAllUserLog(PageVO vo) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT l.ID ,l.USER_ID,l.ROLE_ID,l.METHOD_NAME,l.CLASS_NAME,l.PARAMETERS,l.FUNCTION_NAME,l.MODULE_NAME,l.IP,l.TYPE,l.COMMENT,l.CREATE_DT,l.MEMO,r.ACCOUNT FROM T_SCS_USER_LOG l JOIN T_SCS_USER r ON l.USER_ID=r.ID ORDER BY l.CREATE_DT DESC ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TUserLogVO> logRowMapper = new BeanPropertyRowMapper<TUserLogVO>(TUserLogVO.class);
		List<TUserLogVO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					if (page != null) {
						int curPage = page.getCurPage();
						int pageSize = page.getPageSize();
						if (curPage > 0 && pageSize > 0) {
							ps.setInt(1, (curPage - 1) * pageSize);
							ps.setInt(2, pageSize);
						}
					}
				}
			}, logRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询用户操作日志信息失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int deleteAll(int maxid) throws SQLException {
		int ret_val = -1;
		String sql = "delete from T_SCS_USER_LOG where ID<" + maxid;

		try {
			this.getJdbcTemplate().execute(sql);
			ret_val = 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(String.format("删除日志错误", e.getMessage()));
		}
		return ret_val;
	}

	@Override
	public List<Map<String, Object>> findModuleNames() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT(MODULE_NAME) as MODULE_NAME FROM T_SCS_USER_LOG ");
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql.toString());
		return list;
	}

	public List<TUserLogVO> searchUserLogByCondition(TUserLogVO vo , PageVO page) throws SQLException
	{
		List<TUserLogVO> log_list = null;
		
		String tableName = "T_SCS_USER_LOG";
		String tableColumns = "ID ,USER_ID,ROLE_ID,IP,TYPE,METHOD_NAME,CLASS_NAME,PARAMETERS,FUNCTION_NAME,MODULE_NAME,COMMENT,CREATE_DT,MEMO,TARGET_TYPE,SUBJECT,FROMS,TOS,USER_NAME,HANDLE_DT,STATUS,STATUS_NAME";

		List<Object> param = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("SELECT ");
		sqlBuf.append(tableColumns);
		sqlBuf.append(" FROM ");
		sqlBuf.append(tableName);
		sqlBuf.append(" WHERE 1 = 1 ");
		if(vo != null){
			if (vo.getAccount() != null && vo.getAccount() != "" && vo.getAccount().trim() != "") {
				sqlBuf.append(" and USER_ID = (select r.ID  from T_SCS_USER r where r.ACCOUNT = ? )");
				param.add(vo.getAccount().trim());
			}
			if(vo.getId() > 0){
				sqlBuf.append(" AND ID = ? ");
				param.add(vo.getId());
			}
			if(Utils.noEmpty(vo.getTargetType())){
				sqlBuf.append(" AND TARGET_TYPE = ? ");
				param.add(vo.getTargetType());
			}
			if(vo.getStatus() > 0){
				sqlBuf.append(" AND STATUS = ? ");
				param.add(vo.getStatus());
			}
//			if (vo.getAccount() != null && vo.getAccount() != "" && vo.getAccount().trim() != "") {
//				sql.append(" and r.ACCOUNT LIKE ? ");
//				param.add("%" + vo.getAccount().trim() + "%");
//			}
			if (vo.getType() > 0) {
				sqlBuf.append(" and TYPE = ? ");
				param.add(vo.getType());
			}
			if (vo.getModuleName() != null && vo.getModuleName() != "" && vo.getModuleName().trim() != "") {
				sqlBuf.append(" and MODULE_NAME LIKE ? ");
				param.add("%" + vo.getModuleName().trim() + "%");
			}
			if (vo.getFunctionName() != null && vo.getFunctionName() != "" && vo.getFunctionName().trim() != "") {
				sqlBuf.append(" and FUNCTION_NAME LIKE ? ");
				param.add("%" + vo.getFunctionName().trim() + "%");
			}
			if (vo.getStartDate() != null && !"".equals(vo.getStartDate())) {
				sqlBuf.append(" and CREATE_DT >=? ");
				param.add(vo.getStartDate() + " 00:00:00");
			}
			if (vo.getEndDate() != null && !"".equals(vo.getEndDate())) {
				sqlBuf.append(" and CREATE_DT <=? ");
				param.add(vo.getEndDate() + " 23:59:59");
			}
		}
		sqlBuf.append(" order by CREATE_DT desc ");
	    if (page != null) {
	        int curPage = page.getCurPage();
	        int pageSize = page.getPageSize();
	        if (curPage > 0 && pageSize > 0) {
	        	sqlBuf.append(" limit ?, ?");
				param.add((curPage - 1) * pageSize);
				param.add(pageSize);
	        }
		}
		
	    BeanPropertyRowMapper<TUserLogVO> argTypes = new BeanPropertyRowMapper<TUserLogVO>(TUserLogVO.class);

    	try {
			log_list = this.getJdbcTemplate().query(sqlBuf.toString(), param.toArray(), argTypes);
		} catch (DataAccessException e) {
//			log.error("error", e);
			throw new SQLException("error",e.getMessage());
		}
    	
		return log_list;
	}

}
