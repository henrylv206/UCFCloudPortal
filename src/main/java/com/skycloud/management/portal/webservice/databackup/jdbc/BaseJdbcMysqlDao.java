package com.skycloud.management.portal.webservice.databackup.jdbc;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
/**
 * mysql jdbc 工具类
  *<dl>
  *<dt>类名：BaseJdbcMysqlDao</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2011-12-31  下午02:55:01</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class BaseJdbcMysqlDao extends BaseJdbcDao {
	public BaseJdbcMysqlDao() {
		super();
	}

	public boolean tableExist(String tableName) throws DataAccessException {
		StringBuffer sql = new StringBuffer(200);
		sql.append("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_NAME`='");
		sql.append(tableName).append("'");
		List list = getJdbcTemplate().queryForList(sql.toString());
		return (list != null && !list.isEmpty());
	}

	public boolean dataExist(String tableName, String whereClause)
			throws DataAccessException {
		if (StringUtils.isBlank(tableName)) {
			throw new IllegalArgumentException("数据判存失败：表名参数不合法！");
		}
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT 1 FROM ").append(tableName).append(
				" WHERE 1 = 1 ");
		if (StringUtils.isNotBlank(whereClause)) {
			sql.append(" AND (").append(whereClause).append(") limit 0,1");
		}
		logger.debug("查询SQL语句：" + sql);
		return getJdbcTemplate().queryForList(sql.toString()).size() == 1;
	}

	public  String prepareSql(String sql, PageInfo page) {
		if(page == null) return sql;
		StringBuffer sqlBuf = new StringBuffer(50 + sql.length());
		sqlBuf.append(sql);
		sqlBuf.append(" limit ");
		sqlBuf.append(page.getStartIndex());
		sqlBuf.append(",");
		sqlBuf.append(page.getResults());
		return sqlBuf.toString();
	}

}
