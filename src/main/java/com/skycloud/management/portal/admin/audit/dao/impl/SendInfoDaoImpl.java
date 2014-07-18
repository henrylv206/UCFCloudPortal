package com.skycloud.management.portal.admin.audit.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.audit.dao.ISendInfoDao;
import com.skycloud.management.portal.admin.audit.entity.TSMSInfoBO;
import com.skycloud.management.portal.admin.audit.entity.TSendInfoBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class SendInfoDaoImpl extends SpringJDBCBaseDao implements ISendInfoDao{
//	public JdbcTemplate meipJdbcTemplate;
	
	@Override
	public int insertSendInfo(final TSendInfoBO info) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_SEND_INFO(ID,CREATE_DT,RECEIVE_ADDRESS,ORDER_ID,APPROVE_REASON,STATE) values(?,?,?,?,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, info.getId());
					ps.setTimestamp(i++, new Timestamp(info.getCreateDt().getTime()));
					ps.setString(i++, info.getReceiveAddress());
					ps.setInt(i++, info.getOrderId());
					ps.setString(i++, info.getApproveReason());
					ps.setInt(i++, info.getState());
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			throw new SQLException("保存要发送信息。发信人Email：" +info.getReceiveAddress() + "失败原因：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}

	//查询结果集中添加 o.TYPE 字段,//bugid = 0001707
	@Override
	public List<TSendInfoBO> findAllPedding() throws SQLException {
		String sql="select f.ID,f.RECEIVE_ADDRESS,f.ORDER_ID,f.APPROVE_REASON,f.STATE,o.ORDER_CODE,o.TYPE from T_SCS_SEND_INFO f join T_SCS_ORDER o on f.ORDER_ID=o.ORDER_ID  where f.STATE <> 3;";
		BeanPropertyRowMapper<TSendInfoBO> RowMapper = new BeanPropertyRowMapper<TSendInfoBO>(TSendInfoBO.class);
		List<TSendInfoBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new RowMapperResultSetExtractor<TSendInfoBO>(RowMapper));
		} catch (Exception e) {
			throw new SQLException("查询发送信息失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int updateSendInfo(final List<TSendInfoBO> infos) throws SQLException {
		String sql="update  T_SCS_SEND_INFO set STATE=? where ID=?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, 3);
				ps.setInt(2, infos.get(i).getId());
			}
			@Override
			public int getBatchSize() {
				return infos.size();
			}
		});
		return 0;
	}

//	@Override
//	public int insertSMSInfo(final TSMSInfoBO info) throws SQLException {
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		final String sql="insert into dfsdl(Id,Mobile,Content,Deadtime,Status,Eid,Userid,Password,usrport) values(?,?,?,?,?,?,?,?,?);";
//		try {
//			this.getMeipJdbcTemplate().update(new PreparedStatementCreator(){
//				int i=1;
//				@Override
//				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
//					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//					ps.setInt(i++, info.getId());
//					ps.setString(i++, info.getMobile());
//					ps.setString(i++, info.getContent());
//					ps.setTimestamp(i++, new Timestamp(info.getDeadTime().getTime()));
//					ps.setInt(i++, info.getStatus());
//					ps.setString(i++, info.getEid());
//					ps.setString(i++, info.getUserId());
//					ps.setString(i++, info.getPassword());
//					ps.setInt(i++, info.getUsrport());
//					return ps;
//				}
//			}, keyHolder); 
//		} catch (Exception e) {
//			throw new SQLException("保存要发送信息。发信人Mobile：" +info.getMobile() + "失败原因：" + e.getMessage());
//		}
//		return keyHolder.getKey().intValue();
//	}
	
	@Override
	public int insertSMSInfo(final TSMSInfoBO info) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_MESSAGE(TYPE,STATUS,TOS,CONTENT,CREATE_TIME) values(?,?,?,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(i++, "SMS");
					ps.setInt(i++, 1);
					ps.setString(i++, info.getMobile());
					ps.setString(i++, info.getContent());
					ps.setTimestamp(i++, new Timestamp(info.getDeadTime().getTime()));
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			throw new SQLException("保存要发送信息。发信人Mobile：" +info.getMobile() + "失败原因：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}


//	public JdbcTemplate getMeipJdbcTemplate() {
//		return meipJdbcTemplate;
//	}
//
//	public void setMeipJdbcTemplate(JdbcTemplate meipJdbcTemplate) {
//		this.meipJdbcTemplate = meipJdbcTemplate;
//	}

}
