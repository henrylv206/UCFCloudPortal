package com.skycloud.management.portal.webservice.databackup.jdbc;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * <dl>
 * <dt>SPMS</dt>
 * <dd>Description:基于JDBC的DAO基类,对于需要批量处理,或者涉及到大数据量的操作,请使用此基类。
 * 注意：必须注入参数jdbcTemplate</dd>
 * <dd>Copyright: Copyright (C) 2004</dd>
 * </dl>
 * 
 * @author 刘江宁
 */
public class BaseJdbcDao extends JdbcDaoSupport implements IBaseJdbcDao {
	protected final Logger logger = Logger.getLogger(getClass());

	public BaseJdbcDao() {
		super();
	}

	public int update(String sql) throws DataAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行更新SQL语句：" + sql);
		}
		return getJdbcTemplate().update(sql);
	}

	/**
	 * 执行更新SQL语句
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return 返回成功更新的记录数
	 * @throws DataAccessException
	 */
	public int update(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return getJdbcTemplate().update(sql, args, argTypes);
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回String值
	 * @throws DataAccessException
	 */
	public String queryForString(String sql) throws DataAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回String值，SQL=[" + sql + "]");
		}

		return (String) getJdbcTemplate().queryForObject(sql, String.class);
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回int值
	 * @throws DataAccessException
	 */
	public int queryForInt(String sql) throws DataAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回int值，SQL=[" + sql + "]");
		}
		return getJdbcTemplate().queryForInt(sql);
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回long值
	 * @throws DataAccessException
	 */
	public long queryForLong(String sql) throws DataAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回long值，SQL=[" + sql + "]");
		}
		return getJdbcTemplate().queryForLong(sql);
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @param objClass
	 * @return 返回Class的对象
	 * @throws DataAccessException
	 */
	public <T> T queryForObject(String sql, Class<T> objClass)
			throws DataAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，返回Class的对象，SQL=[" + sql + "]");
		}
		T r = null;
		try{
			r = (T)getJdbcTemplate().queryForObject(sql,new BeanPropertyRowMapper(objClass));
		}catch(EmptyResultDataAccessException e){
			
		}
		return r;
	}

	/**
	 * 执行查询SQL语句,验证该表是否存在
	 * 
	 * @param tableName
	 * @return 返回boolean
	 * @throws DataAccessException
	 */
	public boolean tableExist(String tableName) throws DataAccessException {
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT 1 FROM TAB WHERE UPPER(TNAME)=UPPER('");
		sql.append(tableName).append("')");
		List list = getJdbcTemplate().queryForList(sql.toString());
		return (list != null && !list.isEmpty());
	}

	/**
	 * 执行查询SQL语句,验证是否存在满足whereClause条件的数据且是唯一
	 * 
	 * @param tableName
	 * @param whereClause
	 * @return 返回boolean
	 * @throws DataAccessException
	 */
	public boolean dataExist(String tableName, String whereClause)
			throws DataAccessException {
		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("数据判存失败：表名参数不合法！");
		}
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT 1 FROM ").append(tableName).append(
				" WHERE ROWNUM = 1 ");
		if (StringUtils.isNotBlank(whereClause)) {
			sql.append(" AND (").append(whereClause).append(")");
		}
		logger.debug("查询SQL语句：" + sql);
		return getJdbcTemplate().queryForList(sql.toString()).size() == 1;
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes,
			Class<T> poClass) throws DataAccessException {
		List<T> list = getJdbcTemplate().query(sql, args, argTypes,
				new BeanPropertyRowMapper(poClass));
		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，SQL=[" + sql + "]");
			logger.debug("返回结果列表:" + list.size());
		}
		return list;
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * 
	 * @author weiss
	 */
	public <T> List<T> queryForList(String sql, Class<T> poClass)
			throws DataAccessException {

		List<T> list = getJdbcTemplate().query(sql,
				new BeanPropertyRowMapper(poClass));

		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，SQL=[" + sql + "]");
			logger.debug("返回结果列表:" + list.size());
		}
		return list;
	}

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return Object
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
			Class<T> poClass) throws DataAccessException {

		T r = null;
		try{
			r = (T) getJdbcTemplate().queryForObject(sql, args, argTypes,
					new BeanPropertyRowMapper(poClass));
		}catch(EmptyResultDataAccessException e){
			
		}
		return r;
	}

	public String prepareSql(String sql, PageInfo page) {
		if(page == null) return sql;
		
		StringBuffer sqlBuf = new StringBuffer(50 + sql.length());
		long PageTotalRecords = page.getTotalRecords();
		/*** 总记录数 */
		int PageStartIndex = page.getStartIndex();
		/** 显示记录开始页数 */
		int PageResults = page.getResults();
		/** 页面显示记录条数 */
		int end = PageStartIndex + PageResults;
		/** 显示到第几条记录条数 */
		sqlBuf
				.append("SELECT tt.* FROM")
				.append(" (SELECT ROWNUM rid,t.* FROM (")
				.append(sql)
				.append(") t) tt WHERE rid >")
				.append(
						(PageTotalRecords > 0 && PageStartIndex > PageTotalRecords) ? 0
								: PageStartIndex)
				.append(" AND rid <= ")
				.append(
						(PageTotalRecords > 0 && end > PageTotalRecords) ? PageTotalRecords
								: end);
		return sqlBuf.toString();
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, Object[] args, int[] argTypes,
			PageInfo page, Class<T> poClass) throws DataAccessException {
		return queryByPage(sql, args, argTypes, page,
				new BeanPropertyRowMapper(poClass));
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, PageInfo page, Class<T> poClass)
			throws DataAccessException {
		return queryByPage(sql, page, new BeanPropertyRowMapper(poClass));
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, Object[] args, int[] argTypes,
			PageInfo page, RowMapper mapper) throws DataAccessException {
		if (page != null)
			page.setTotalRecords(getMaxRowCount(sql, args, argTypes));
		List<T> list = getJdbcTemplate().query(prepareSql(sql, page), args,
				argTypes, mapper);

		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，SQL=[" + sql + "]");
			logger.debug("返回结果列表:" + list.size());
		}
		return list;
	}

	/**
	 * 执行分页查询SQL语句
	 * 
	 * @param sql
	 * @return 返回结果列表
	 * @throws DataAccessException
	 * @author weiss
	 */
	public <T> List<T> queryByPage(String sql, PageInfo page, RowMapper mapper)
			throws DataAccessException {
		if(page != null){
			page.setTotalRecords(getMaxRowCount(sql));			
		}
		List<T> list = getJdbcTemplate().query(prepareSql(sql, page), mapper);

		if (logger.isDebugEnabled()) {
			logger.debug("执行查询SQL语句，SQL=[" + sql + "]");
			logger.debug("返回结果列表:" + list.size());
		}
		return list;
	}

	/**
	 * 执行sql语句 得到记录总数
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return int
	 * @throws DataAccessException
	 */
	public int getMaxRowCount(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		StringBuffer countSql = new StringBuffer(30 + sql.length());
		countSql.append(" SELECT COUNT(*) FROM (").append(sql).append(") Z");

		return getJdbcTemplate().queryForInt(countSql.toString(), args,
				argTypes);
	}

	/**
	 * 执行sql语句 得到记录总数
	 * 
	 * @param sql
	 * @return int
	 * @throws DataAccessException
	 */
	public int getMaxRowCount(String sql) throws DataAccessException {
		StringBuffer countSql = new StringBuffer(30 + sql.length());
		countSql.append(" SELECT COUNT(*) FROM (").append(sql).append(") Z");
		return getJdbcTemplate().queryForInt(countSql.toString());
	}
}
