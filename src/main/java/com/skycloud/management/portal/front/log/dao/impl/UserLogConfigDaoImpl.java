package com.skycloud.management.portal.front.log.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.customer.utils.Utils;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.log.dao.IUserLogConfigDao;
import com.skycloud.management.portal.front.log.entity.TUserLogConfig;

public class UserLogConfigDaoImpl extends SpringJDBCBaseDao implements IUserLogConfigDao {

	private static String ERROR_MESSAGE_PORTAL_OPERLOG_DAO_CREATE = "保存操作日志配置错误%s";

	private PreparedStatementCreator PreparedSaveSQLArgs(final TUserLogConfig config, final String sql) {
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				
				ps.setInt(index++, config.getId());
				ps.setString(index++, config.getName());
				ps.setString(index++, config.getKeyName());
				ps.setInt(index++, config.getMsgFlag());
				ps.setInt(index++, config.getMailFlag());
				ps.setInt(index++, config.getSmsFlag());
				ps.setInt(index++, config.getLogFlag());
				ps.setInt(index++, config.getFileFlag());
				ps.setInt(index++, config.getBak1Flag());
				ps.setInt(index++, config.getBak2Flag());
				ps.setInt(index++, config.getStatus());
				ps.setString(index++, config.getRemark());
				ps.setTimestamp(index++, config.getCreateDt() == null ?  new Timestamp(System.currentTimeMillis()) : new Timestamp(config.getCreateDt().getTime()));
				
				return ps;
			}
		};
	}

	private PreparedStatementCreator PreparedUpdateSQLArgs(final TUserLogConfig config, final String sql) {
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				
				ps.setString(index++, config.getName());
				ps.setString(index++, config.getKeyName());
				ps.setInt(index++, config.getMsgFlag());
				ps.setInt(index++, config.getMailFlag());
				ps.setInt(index++, config.getSmsFlag());
				ps.setInt(index++, config.getLogFlag());
				ps.setInt(index++, config.getFileFlag());
				ps.setInt(index++, config.getBak1Flag());
				ps.setInt(index++, config.getBak2Flag());
				ps.setInt(index++, config.getStatus());
				ps.setString(index++, config.getRemark());
				ps.setInt(index++, config.getId());
				
				return ps;
			}
		};
	}

	@Override
	public int save(TUserLogConfig config) throws SQLException {
		int ret_val = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = " INSERT INTO T_SCS_USER_LOG_CONFIG"
		           + " (ID,NAME,KEY_NAME,   MSG_FLAG,MAIL_FLAG,SMS_FLAG,  LOG_FLAG,FILE_FLAG,BAK1_FLAG,BAK2_FLAG,   STATUS,REMARK,CREATE_DT)"
		           + " VALUES"
		           + " (?,?,?,  ?,?,?,  ?,?,?,?,  ?,?,?);";

		try {
			PreparedStatementCreator preCreator = PreparedSaveSQLArgs(config, sql);
			this.getJdbcTemplate().update(preCreator, keyHolder);
			ret_val = keyHolder.getKey().intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(String.format(ERROR_MESSAGE_PORTAL_OPERLOG_DAO_CREATE, e.getMessage()));
		}
		return ret_val;
	}

	@Override
	public int update(TUserLogConfig config) throws SQLException {
		int ret_val = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = " UPDATE T_SCS_USER_LOG_CONFIG SET "
		           + " NAME = ? ,KEY_NAME = ? ,MSG_FLAG = ? ,MAIL_FLAG = ?,SMS_FLAG = ?"
		           + " , LOG_FLAG = ?,FILE_FLAG = ?,BAK1_FLAG = ?, BAK2_FLAG = ?"
		           + " , STATUS = ? ,REMARK = ? ,LASTUPDATE_DT = now()"
		           + " WHERE ID = ? ";

		PreparedStatementCreator preCreator = PreparedUpdateSQLArgs(config, sql);
		ret_val = this.getJdbcTemplate().update(preCreator, keyHolder);
//		ret_val = keyHolder.getKey().intValue();
			
		return ret_val;
	}

	@Override
	public int delete(final int id) throws SQLException {
		String sql = "DELETE FROM T_SCS_USER_LOG_CONFIG WHERE ID=?";
		return this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		});

	}

	@Override
	public int deleteByCondition(final TUserLogConfig vo) throws SQLException {
		int ret_val = -1;
		StringBuffer sqlBuf = new StringBuffer();
//		List<Object> param = new ArrayList<Object>();
		sqlBuf.append(" DELETE FROM T_SCS_USER_LOG where 1=1 ");
		if(vo != null){
			if(vo.getId() > 0){
				sqlBuf.append(" AND ID = ? ");
//				param.add(vo.getId());
			}
			if(Utils.noEmpty(vo.getName())){
				sqlBuf.append(" AND NAME = ? ");
//				param.add(vo.getName());
			}
			if(Utils.noEmpty(vo.getKeyName())){
				sqlBuf.append(" AND KEY_NAME = ? ");
//				param.add(vo.getKeyName());
			}
			if(vo.getMsgFlag() > 0){
				sqlBuf.append(" AND MSG_FLAG = ? ");
//				param.add(vo.getMsgFlag());
			}
			if(vo.getMailFlag() > 0){
				sqlBuf.append(" AND MAIL_FLAG = ? ");
//				param.add(vo.getMailFlag());
			}
			if(vo.getSmsFlag() > 0){
				sqlBuf.append(" AND SMS_FLAG = ? ");
//				param.add(vo.getSmsFlag());
			}
			if(vo.getStatus() > 0){
				sqlBuf.append(" AND STATUS = ? ");
//				param.add(vo.getStatus());
			}
		}
		
		try {
			ret_val = this.getJdbcTemplate().update(sqlBuf.toString(),new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int index = 1;
					if(vo != null){
						if(vo.getId() > 0){
							ps.setInt(index++, vo.getId());
						}
						if(Utils.noEmpty(vo.getName())){
							ps.setString(index++, vo.getName());
						}
						if(Utils.noEmpty(vo.getKeyName())){
							ps.setString(index++, vo.getKeyName());
						}
						if(vo.getMsgFlag() > 0){
							ps.setInt(index++, vo.getMsgFlag());
						}
						if(vo.getMailFlag() > 0){
							ps.setInt(index++, vo.getMailFlag());
						}
						if(vo.getSmsFlag() > 0){
							ps.setInt(index++, vo.getSmsFlag());
						}
						if(vo.getStatus() > 0){
							ps.setInt(index++, vo.getStatus());
						}
					}
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(String.format("删除日志错误", e.getMessage()));
		}
		return ret_val;
	}


	public TUserLogConfig findUserLogConfigById(int id) throws SQLException
	{
		String tableName = "T_SCS_USER_LOG_CONFIG";
		String tableColumns = "ID,NAME,KEY_NAME,MSG_FLAG,MAIL_FLAG,SMS_FLAG,LOG_FLAG,FILE_FLAG,BAK1_FLAG,BAK2_FLAG,STATUS,CREATE_DT,LASTUPDATE_DT,REMARK";

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT "+tableColumns+" FROM "+tableName+" WHERE ID = ? ");
	    BeanPropertyRowMapper<TUserLogConfig> argTypes = new BeanPropertyRowMapper<TUserLogConfig>(TUserLogConfig.class);
	    
		return this.getJdbcTemplate().queryForObject(sb.toString(),new Object[]{id},argTypes);
	}

	public TUserLogConfig findUserLogConfigByKeyName(String keyName) throws SQLException
	{
		String tableName = "T_SCS_USER_LOG_CONFIG";
		String tableColumns = "ID,NAME,KEY_NAME,MSG_FLAG,MAIL_FLAG,SMS_FLAG,LOG_FLAG,FILE_FLAG,BAK1_FLAG,BAK2_FLAG,STATUS,CREATE_DT,LASTUPDATE_DT,REMARK";

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(tableColumns);
		sb.append(" FROM ");
		sb.append(tableName);
		sb.append(" WHERE KEY_NAME = ? ");
		
	    BeanPropertyRowMapper<TUserLogConfig> argTypes = new BeanPropertyRowMapper<TUserLogConfig>(TUserLogConfig.class);
	    
		return this.getJdbcTemplate().queryForObject(sb.toString(),new Object[]{keyName},argTypes);
	}

	public List<TUserLogConfig> searchAllUserLogConfigByCondition(TUserLogConfig queryVO , PageVO page) throws SQLException
	{
		List<TUserLogConfig> config_list = null;
		
		String tableName = "T_SCS_USER_LOG_CONFIG";
		String tableColumns = "ID,NAME,KEY_NAME,MSG_FLAG,MAIL_FLAG,SMS_FLAG,LOG_FLAG,FILE_FLAG,BAK1_FLAG,BAK2_FLAG,STATUS,CREATE_DT,LASTUPDATE_DT,REMARK";

		List<Object> param = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("SELECT ");
		sqlBuf.append(tableColumns);
		sqlBuf.append(" FROM ");
		sqlBuf.append(tableName);
		sqlBuf.append(" WHERE 1 = 1 ");
		if(queryVO != null){
			if(queryVO.getId() > 0){
				sqlBuf.append(" AND ID = ? ");
				param.add(queryVO.getId());
			}
			if(Utils.noEmpty(queryVO.getName())){
				sqlBuf.append(" AND NAME like ? ");
				param.add("%"+queryVO.getName()+"%");
			}
			if(Utils.noEmpty(queryVO.getKeyName())){
				sqlBuf.append(" AND KEY_NAME like ? ");
				param.add("%"+queryVO.getKeyName()+"%");
			}
			if(queryVO.getMsgFlag() > 0){
				sqlBuf.append(" AND MSG_FLAG = ? ");
				param.add(queryVO.getMsgFlag());
			}
			if(queryVO.getMailFlag() > 0){
				sqlBuf.append(" AND MAIL_FLAG = ? ");
				param.add(queryVO.getMailFlag());
			}
			if(queryVO.getSmsFlag() > 0){
				sqlBuf.append(" AND SMS_FLAG = ? ");
				param.add(queryVO.getSmsFlag());
			}
			if(queryVO.getLogFlag() > 0){
				sqlBuf.append(" AND LOG_FLAG = ? ");
				param.add(queryVO.getLogFlag());
			}
			if(queryVO.getFileFlag() > 0){
				sqlBuf.append(" AND FILE_FLAG = ? ");
				param.add(queryVO.getFileFlag());
			}
			if(queryVO.getBak1Flag() > 0){
				sqlBuf.append(" AND BAK1_FLAG = ? ");
				param.add(queryVO.getBak1Flag());
			}
			if(queryVO.getBak2Flag() > 0){
				sqlBuf.append(" AND BAK2_FLAG = ? ");
				param.add(queryVO.getBak2Flag());
			}
			if(queryVO.getStatus() > 0){
				sqlBuf.append(" AND STATUS = ? ");
				param.add(queryVO.getStatus());
			}
		}
	    if (page != null) {
	        int curPage = page.getCurPage();
	        int pageSize = page.getPageSize();
	        if (curPage > 0 && pageSize > 0) {
	        	sqlBuf.append(" limit ?, ?");
				param.add((curPage - 1) * pageSize);
				param.add(pageSize);
	        }
		}
		
	    BeanPropertyRowMapper<TUserLogConfig> argTypes = new BeanPropertyRowMapper<TUserLogConfig>(TUserLogConfig.class);

    	config_list = this.getJdbcTemplate().query(sqlBuf.toString(), param.toArray(), argTypes);
    	
		return config_list;
	}


	public int countUserLogConfigByCondition(TUserLogConfig queryVO) throws SQLException
	{
		int total = 0;
		
		String tableName = "T_SCS_USER_LOG_CONFIG";

		List<Object> param = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("SELECT  count(ID) ");
		sqlBuf.append(" FROM ");
		sqlBuf.append(tableName);
		sqlBuf.append(" WHERE 1 = 1 ");
		if(queryVO != null){
			if(queryVO.getId() > 0){
				sqlBuf.append(" AND ID = ? ");
				param.add(queryVO.getId());
			}
			if(Utils.noEmpty(queryVO.getName())){
				sqlBuf.append(" AND NAME like ? ");
				param.add("%"+queryVO.getName()+"%");
			}
			if(Utils.noEmpty(queryVO.getKeyName())){
				sqlBuf.append(" AND KEY_NAME like ? ");
				param.add("%"+queryVO.getKeyName()+"%");
			}
			if(queryVO.getMsgFlag() > 0){
				sqlBuf.append(" AND MSG_FLAG = ? ");
				param.add(queryVO.getMsgFlag());
			}
			if(queryVO.getMailFlag() > 0){
				sqlBuf.append(" AND MAIL_FLAG = ? ");
				param.add(queryVO.getMailFlag());
			}
			if(queryVO.getSmsFlag() > 0){
				sqlBuf.append(" AND SMS_FLAG = ? ");
				param.add(queryVO.getSmsFlag());
			}
			if(queryVO.getLogFlag() > 0){
				sqlBuf.append(" AND LOG_FLAG = ? ");
				param.add(queryVO.getLogFlag());
			}
			if(queryVO.getFileFlag() > 0){
				sqlBuf.append(" AND FILE_FLAG = ? ");
				param.add(queryVO.getFileFlag());
			}
			if(queryVO.getBak1Flag() > 0){
				sqlBuf.append(" AND BAK1_FLAG = ? ");
				param.add(queryVO.getBak1Flag());
			}
			if(queryVO.getBak2Flag() > 0){
				sqlBuf.append(" AND BAK2_FLAG = ? ");
				param.add(queryVO.getBak2Flag());
			}
			if(queryVO.getStatus() > 0){
				sqlBuf.append(" AND STATUS = ? ");
				param.add(queryVO.getStatus());
			}
		}
		
		total = getJdbcTemplate().queryForObject(sqlBuf.toString(), param.toArray(), Integer.class);

		return total;
	}
}
