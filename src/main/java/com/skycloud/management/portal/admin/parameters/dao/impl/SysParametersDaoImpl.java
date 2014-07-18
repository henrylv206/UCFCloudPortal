package com.skycloud.management.portal.admin.parameters.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.parameters.entity.ApiCallLog;
import com.skycloud.management.portal.admin.parameters.entity.Parameters;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

/**
 * 系统参数持久化实现
 * 
 * @author jiaoyz
 */
public class SysParametersDaoImpl extends SpringJDBCBaseDao implements ISysParametersDao {

	private final Logger log = Logger.getLogger(SysParametersDaoImpl.class);

	@Override
	public int updateResetJob(final int instanceId) {
//		String sql = "UPDATE  T_SCS_ASYNCJOB SET jobstate=1, jobid=0, resid=0 WHERE INSTANCE_INFO_ID=? and resid=-1 ";
		//fix bug 7700 按照job任务ID重置任务
		String sql = "UPDATE  T_SCS_ASYNCJOB SET jobstate=1, jobid=0, resid=0 WHERE ID=? and resid=-1 ";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, instanceId);
			}
		});
	}

	@Override
	public String getParameter(String type) throws Exception {
		if (StringUtils.isBlank(type)) {
			return null;
		}
		String sql = "SELECT VALUE FROM T_SCS_PARAMETERS WHERE TYPE = ?";
		//to fix bug 4957
		//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
		String value = null;
		try {
			value = getJdbcTemplate().queryForObject(sql, String.class, type);
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		return value;
	}

	@Override
	public String getParameterValueByType(String type) throws Exception {
		if (StringUtils.isBlank(type)) {
			return null;
		}
		String sql = "SELECT VALUE FROM T_SCS_PARAMETERS WHERE TYPE = ?";
		String value = null;
		try {
			value = getJdbcTemplate().queryForObject(sql, String.class, type);
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		return value;
	}

	@Override
	public int updateParameter(final Parameters param) {
		String sql = "UPDATE  T_SCS_PARAMETERS SET VALUE=?,COMMENT=? WHERE ID=?";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, param.getValue());
				ps.setString(2, param.getComment());
				ps.setInt(3, param.getId());
			}
		});
	}

	@Override
	public void updateParameter(final List<Parameters> params) {
		String sql = "UPDATE  T_SCS_PARAMETERS SET VALUE=? WHERE ID=?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, params.get(i).getValue());
				ps.setInt(2, params.get(i).getId());
			}

			@Override
			public int getBatchSize() {
				return params.size();
			}
		});
	}

	@Override
	public List<Parameters> findAll() {
		String sql = "SELECT * FROM T_SCS_PARAMETERS ORDER BY ID,TYPE";
		BeanPropertyRowMapper<Parameters> parameterRowMapper = new BeanPropertyRowMapper<Parameters>(Parameters.class);
		List<Parameters> list = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<Parameters>(parameterRowMapper));
		return list;
	}

	@Override
	public int insertParameter(final Parameters param) {
		String sql = "INSERT INTO T_SCS_PARAMETERS (TYPE,VALUE,COMMENT) VALUES(?,?,?)";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, param.getType());
				ps.setString(2, param.getValue());
				ps.setString(3, param.getComment());
			}
		});
	}

	@Override
	public int deleteParameter(final int id) {
		String sql = "DELETE  T_SCS_PARAMETERS WHERE ID=?";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		});
	}

	@Override
	public Parameters queryParameterById(int id) {
		String sql = "SELECT * FROM T_SCS_PARAMETERS WHERE ID=?";
		Parameters parameters = null;
		BeanPropertyRowMapper<Parameters> parameterRowMapper = new BeanPropertyRowMapper<Parameters>(Parameters.class);
		List<Parameters> list = null;
		try {
			list = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<Parameters>(parameterRowMapper));
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ id : " + id + "]");
		}
		if (null != list && !list.isEmpty()) {
			parameters = list.get(0);
		}
		return parameters;
	}

	@Override
	public List<Parameters> queryParameters(int curPage, int pageSize, String searchKey) {
		List<Object> param = new ArrayList<Object>();
		BeanPropertyRowMapper<Parameters> parameterRowMapper = new BeanPropertyRowMapper<Parameters>(Parameters.class);
		StringBuffer sql = new StringBuffer();
		List<Parameters> rs = new ArrayList<Parameters>();
		try {
			sql.append("select * from T_SCS_PARAMETERS where  state=1 ");
			if (StringUtils.isNotEmpty(searchKey)) {
				sql.append(" and instr(type,  ?)>0 ");
				param.add(searchKey.trim());
			}
			sql.append(" order by id,type ");
			sql.append(" limit ?,?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			rs = jdbcTemplate.query(sql.toString(), param.toArray(), new RowMapperResultSetExtractor<Parameters>(parameterRowMapper));
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ searchKey : " + searchKey + "]");
		}
		return rs;
	}

	@Override
	public List<Parameters> queryParameters2(int curPage, int pageSize, String searchKey) {
		List<Object> param = new ArrayList<Object>();
		BeanPropertyRowMapper<Parameters> parameterRowMapper = new BeanPropertyRowMapper<Parameters>(Parameters.class);
		StringBuffer sql = new StringBuffer();
		List<Parameters> rs = new ArrayList<Parameters>();
		try {
			sql.append("select * from T_SCS_PARAMETERS where  1=1 ");
			if (StringUtils.isNotEmpty(searchKey)) {
				sql.append(" and instr(type,  ?)>0 ");
				param.add(searchKey.trim());
			}
			sql.append(" order by id,type ");
			sql.append(" limit ?,?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			rs = jdbcTemplate.query(sql.toString(), param.toArray(), new RowMapperResultSetExtractor<Parameters>(parameterRowMapper));
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ searchKey : " + searchKey + "]");
		}
		return rs;
	}

	@Override
	public int countParameters(String searchKey) {
		List<Object> param = new ArrayList<Object>();
		int rs = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from T_SCS_PARAMETERS where state = 1 ");
		if (StringUtils.isNotEmpty(searchKey)) {
			sql.append(" and instr(type, ?)>0 ");
			param.add(searchKey.trim());
		}
		try {
			rs = jdbcTemplate.queryForInt(sql.toString(), param.toArray());
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ searchKey : " + searchKey + "]");
		}
		return rs;
	}

	@Override
	public List<ApiCallLog> queryElasterApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName) {
		List<Object> param = new ArrayList<Object>();
		BeanPropertyRowMapper<ApiCallLog> rowMapper = new BeanPropertyRowMapper<ApiCallLog>(ApiCallLog.class);
		StringBuffer sql = new StringBuffer();
		List<ApiCallLog> rs = new ArrayList<ApiCallLog>();

		sql.append("select * from (");
		sql.append(Sql4SysParams.queryElasterApiCallLogSql);
		sql.append(") tt where 1=1 ");
		if (qid != 0) {
			sql.append(" and ins_id= ?");
			param.add(qid);
		}
		// fixed bug 2433
		if (StringUtils.isNotEmpty(insName)) {
			sql.append(" and instr(lower(ins_name), lower(?))>0");
			param.add(insName);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and create_dt>= ?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and create_dt<= ?");
			param.add(end.substring(0, 10));
		}// bug 0002667
		sql.append(" order by  create_dt desc");
		if(curPage >0 && pageSize>0){
			sql.append(" limit ?,?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);			
		}
		try {
			rs = jdbcTemplate.query(sql.toString(), param.toArray(), new RowMapperResultSetExtractor<ApiCallLog>(rowMapper));
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ start : " + start + ", end : " + end + ", qid : " + qid + ", insName : " + insName + "]");
		}
		return rs;
	}

	@Override
	public int countElasterApiCallLog(String start, String end, int qid, String insName) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		int rs = 0;

		sql.append("select * from (");
		sql.append(Sql4SysParams.queryElasterApiCallLogSql);
		sql.append(") tt where 1=1 ");
		if (qid != 0) {
			sql.append(" and ins_id=?");
			param.add(qid);
		}
		// fixed bug 2433
		if (StringUtils.isNotEmpty(insName)) {
			sql.append(" and instr(lower(ins_name), lower(?))>0");
			param.add(insName);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and substr(create_dt,1,10)>= ?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and substr(create_dt,1,10)<= ?");
			param.add(end.substring(0, 10));
		}
		String str = "select count(*) from (" + sql.toString() + ") cc";
		try {
			rs = jdbcTemplate.queryForInt(str, param.toArray());
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ start : " + start + ", end : " + end + ", qid : " + qid + ", insName : " + insName + "]");
		}
		return rs;
	}

	@Override
	public List<ApiCallLog> queryH3cApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName) {
		List<Object> param = new ArrayList<Object>();
		BeanPropertyRowMapper<ApiCallLog> rowMapper = new BeanPropertyRowMapper<ApiCallLog>(ApiCallLog.class);
		StringBuffer sql = new StringBuffer();
		List<ApiCallLog> rs = new ArrayList<ApiCallLog>();
		sql.append("select * from (");
		sql.append(Sql4SysParams.queryH3cApiCallLogSql);
		sql.append(") tt where 1=1 ");
		if (qid != 0) {
			sql.append(" and user_id=? ");
			param.add(qid);
		}
		// fixed bug 2433
		if (StringUtils.isNotEmpty(insName)) {
			sql.append(" and instr(lower(ins_name), lower(?))>0");
			param.add(insName);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and create_dt>= ?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and create_dt<= ?");
			param.add(end.substring(0, 10));
		}// bug 0002667
		sql.append(" order by   create_dt desc");
		sql.append(" limit ?,?");
		param.add((curPage - 1) * pageSize);
		param.add(pageSize);
		try {
			rs = jdbcTemplate.query(sql.toString(), param.toArray(), new RowMapperResultSetExtractor<ApiCallLog>(rowMapper));
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ start : " + start + ", end : " + end + ", qid : " + qid + ", insName : " + insName + "]");
		}
		return rs;
	}

	@Override
	public int countH3cApiCallLog(String start, String end, int qid, String insName) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		int rs = 0;

		sql.append("select * from (");
		sql.append(Sql4SysParams.queryH3cApiCallLogSql);
		sql.append(") tt where 1=1 ");
		if (qid != 0) {
			sql.append(" and user_id=? ");
			param.add(qid);
		}
		// fixed bug 2433
		if (StringUtils.isNotEmpty(insName)) {
			sql.append(" and instr(lower(ins_name), lower(?))>0");
			param.add(insName);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and substr(create_dt,1,10)>= ?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and substr(create_dt,1,10)<= ?");
			param.add(end.substring(0, 10));
		}
		String str = "select count(*) from (" + sql.toString() + ") cc";
		try {
			rs = jdbcTemplate.queryForInt(str, param.toArray());
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ start : " + start + ", end : " + end + ", qid : " + qid + ", insName : " + insName + "]");
		}
		return rs;
	}

	@Override
	public List<ApiCallLog> queryS3ApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName) {
		List<Object> param = new ArrayList<Object>();
		BeanPropertyRowMapper<ApiCallLog> rowMapper = new BeanPropertyRowMapper<ApiCallLog>(ApiCallLog.class);
		StringBuffer sql = new StringBuffer();
		List<ApiCallLog> rs = new ArrayList<ApiCallLog>();

		sql.append("select * from (");
		sql.append(Sql4SysParams.queryS3ApiCallLogSql);
		sql.append(") tt where 1=1 ");
		if (qid != 0) {
			sql.append(" and ins_id=? ");
			param.add(qid);
		}
		// fixed bug 2433
		if (StringUtils.isNotEmpty(insName)) {
			sql.append(" and instr(lower(ins_name), lower(?))>0");
			param.add(insName);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and create_dt>= ?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and create_dt<= ?");
			param.add(end.substring(0, 10));
		}// bug 0002667
		sql.append(" order by  create_dt desc");
		sql.append(" limit ?,?");
		param.add((curPage - 1) * pageSize);
		param.add(pageSize);
		try {
			rs = jdbcTemplate.query(sql.toString(), param.toArray(), new RowMapperResultSetExtractor<ApiCallLog>(rowMapper));
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ start : " + start + ", end : " + end + ", qid : " + qid + ", insName : " + insName + "]");
		}
		return rs;
	}

	@Override
	public int countS3ApiCallLog(String start, String end, int qid, String insName) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		int rs = 0;

		sql.append("select * from (");
		sql.append(Sql4SysParams.queryS3ApiCallLogSql);
		sql.append(") tt where 1=1 ");
		if (qid != 0) {
			sql.append(" and ins_id=? ");
			param.add(qid);
		}
		// fixed bug 2433
		if (StringUtils.isNotEmpty(insName)) {
			sql.append(" and instr(lower(ins_name), lower(?))>0");
			param.add(insName);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and substr(create_dt,1,10)>= ?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and substr(create_dt,1,10)<= ?");
			param.add(end.substring(0, 10));
		}
		String str = "select count(*) from (" + sql.toString() + ") cc";
		try {
			rs = jdbcTemplate.queryForInt(str, param.toArray());
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error(e);
			log.error("sql == " + sql.toString());
			log.error("args == [ start : " + start + ", end : " + end + ", qid : " + qid + ", insName : " + insName + "]");
		}
		return rs;
	}

	@Override
	public int findThresholdsByFieldName(String fieldName) {
		String sql = "SELECT VALUE FROM T_SCS_PARAMETERS WHERE TYPE = ? ";
		return getJdbcTemplate().queryForInt(sql, fieldName);
	}

}
