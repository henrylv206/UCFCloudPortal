package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.order.dao.IJobInstanceInfoDao;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.action.vo.VMResourcesVO;

public class JobInstanceInfoDaoImpl extends SpringJDBCBaseDao implements IJobInstanceInfoDao {
	 private static Log log = LogFactory.getLog(JobInstanceInfoDaoImpl.class);
	 private long  interval =0;
	 private static final int  RATE = 10000;
	 
	 
	 
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	@Override	
	public List<ResourcesVO> queryInstanceByUser(final TUserBO user) throws SCSException {
		StringBuffer sql = new StringBuffer();
	    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.RES_CODE,i.TEMPLATE_TYPE ");
	    sql.append(" FROM T_SCS_INSTANCE_INFO i  " );
	    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
	    sql.append(" where o.CREATOR_USER_ID=?  and i.TEMPLATE_TYPE <>1 and i.state<>4");	
	    
	    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
	    List<ResourcesVO> returnList = null;
	    List<Object> args = new ArrayList<Object>();
	    try {
	      returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, user.getId());
				}
			}, infoRowMapper);
	      log.info("sql====" + sql.toString());
	      log.info("args====" + args);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      log.error("args====" + args);
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;
	}
	@Override
	public List<VMResourcesVO> queryVMByUser(TUserBO user) throws SCSException {
		StringBuffer sql = new StringBuffer();
	    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.RES_CODE,t.TYPE AS INSTANCE_TYPE");
	    sql.append(" FROM T_SCS_INSTANCE_INFO i  " );
	    sql.append(" left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID");		
	    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
	    sql.append(" where o.CREATOR_USER_ID=?  and  i.TEMPLATE_TYPE='1' and i.state<>4");	
	    
	    BeanPropertyRowMapper<VMResourcesVO> infoRowMapper = new BeanPropertyRowMapper<VMResourcesVO>(VMResourcesVO.class);
	    List<VMResourcesVO> returnList = null;
	    List<Object> args = new ArrayList<Object>();
	    try {
	      if (user.getId() !=0) {
	        args.add(user.getId());
	      }

	      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
	      log.info("sql====" + sql.toString());
	      log.info("args====" + args);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      log.error("args====" + args);
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;
	}
	@Override
	public List<ResourcesVO> queryVMForUpdateByUser(TUserBO user)
			throws SCSException {
		StringBuffer sql = new StringBuffer();
	    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.RES_CODE ");
	    sql.append(" FROM T_SCS_INSTANCE_INFO i  " );
	    sql.append(" left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID");		
	    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
	    sql.append(" where o.CREATOR_USER_ID=? and i.STATE  in (2,5) and  i.TEMPLATE_TYPE='1' and t.TYPE='1'  ");	
	    
	    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
	    List<ResourcesVO> returnList = null;
	    List<Object> args = new ArrayList<Object>();
	    try {
//	      args.add(user.getId());
//	      String name = user.getName();
//	      if (name == null) {
//	        name = "";
//	      }
//	      args.add("%" + name + "%");
	      if (user.getId() !=0) {
	        args.add(user.getId());
	      }

	      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
	      log.info("sql====" + sql.toString());
	      log.info("args====" + args);
	    } catch (Exception e) {
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      log.error("args====" + args);
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;	 
	}
	@Override
	public int deleteUserSnapshot(int createUserId) throws SCSException {
		String sql = "DELETE FROM T_SCS_USER_SNAPSHOT WHERE CREATE_USER_ID =  "+createUserId;
		return this.getJdbcTemplate().update(sql);
	}
	
	public List<TUserBO> queryUserByState(int state) throws SCSException{
		//to fix 1705
		String sql = "SELECT ID FROM T_SCS_USER WHERE STATE=? AND UNIX_TIMESTAMP(NOW()+0)-(UNIX_TIMESTAMP(LASTUPDATE_DT)+0) <= ? ";
		BeanPropertyRowMapper<TUserBO> infoRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
	    List<TUserBO> returnList = null;
	    log.info("---->"+interval);
	    Object[] obj = new Object[]{state,interval};
	    try {
	      returnList = this.getJdbcTemplate().query(sql, obj, infoRowMapper);
	      log.info("sql====" + sql.toString());
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;	 
	}
	@Override
	public List<ResourcesVO> queryInstanceByOrder(final TOrderBO order)throws SCSException {
		StringBuffer sql = new StringBuffer();
	    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.RES_CODE,i.TEMPLATE_TYPE ");
	    sql.append(" FROM T_SCS_INSTANCE_INFO i  " );
	    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
	    sql.append(" where o.ORDER_ID=?  and i.TEMPLATE_TYPE <>1 and i.state<>4");	
	    
	    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
	    List<ResourcesVO> returnList = null;
	    List<Object> args = new ArrayList<Object>();
	    try {
	      returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, order.getOrderId());
				}
			}, infoRowMapper);
	      log.info("sql====" + sql.toString());
	      log.info("args====" + args);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      log.error("args====" + args);
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;
	}
	@Override
	public List<VMResourcesVO> queryVMByOrder(final TOrderBO order)throws SCSException {
		StringBuffer sql = new StringBuffer();
	    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.RES_CODE,t.TYPE AS INSTANCE_TYPE");
	    sql.append(" FROM T_SCS_INSTANCE_INFO i  " );
	    sql.append(" left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID");		
	    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
	    sql.append(" where o.ORDER_ID=?  and  i.TEMPLATE_TYPE='1' and i.state<>4");	
	    
	    BeanPropertyRowMapper<VMResourcesVO> infoRowMapper = new BeanPropertyRowMapper<VMResourcesVO>(VMResourcesVO.class);
	    List<VMResourcesVO> returnList = null;
	    List<Object> args = new ArrayList<Object>();
	    try {
	      if (order.getOrderId()!=0) {
	        args.add(order.getOrderId());
	      }

	      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
	      log.info("sql====" + sql.toString());
	      log.info("args====" + args);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      log.error("args====" + args);
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;
	}
	@Override
	public Iri queryIriByDiskId(int diskId)throws SCSException{
		String sql = "select * from T_SCS_IRI where DISK_INSTANCE_INFO_ID = ? and (STATE=? or STATE=?)";
		BeanPropertyRowMapper<Iri> infoRowMapper = new BeanPropertyRowMapper<Iri>(Iri.class);
		Iri iri = null;
	    try {
	    	Object[] obj = new Object[]{diskId,2,7};	
	    	List<Iri> list = this.getJdbcTemplate().query(sql, obj, infoRowMapper);
	    	if(null!=list&&!list.isEmpty()){
	    		iri = list.get(0);
	    	}	      
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return iri;	
	}
	@Override
	public TPublicIPBO queryPublicIPByVMId(int vmId) throws SCSException {
		String sql = "select * from T_SCS_PUBLIC_IP where INSTANCE_INFO_ID = ? and STATUS=1";
		BeanPropertyRowMapper<TPublicIPBO> infoRowMapper = new BeanPropertyRowMapper<TPublicIPBO>(TPublicIPBO.class);
		TPublicIPBO ip = null;
	    try {
	    	Object[] obj = new Object[]{vmId};	
	    	List<TPublicIPBO> list = this.getJdbcTemplate().query(sql, obj, infoRowMapper);
	    	if(null!=list&&!list.isEmpty()){
	    		ip = list.get(0);
	    	}	      
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return ip;
	}
	@Override	
	public List<ResourcesVO> queryAllInstanceByUser(final TUserBO user) throws SCSException {
		StringBuffer sql = new StringBuffer();
	    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.RES_CODE,i.TEMPLATE_TYPE ");
	    sql.append(" FROM T_SCS_INSTANCE_INFO i  " );
	    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
	    sql.append(" where o.CREATOR_USER_ID=?   and i.state<>4");	
	    
	    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
	    List<ResourcesVO> returnList = null;
	    List<Object> args = new ArrayList<Object>();
	    try {
	      returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, user.getId());
				}
			}, infoRowMapper);
	      log.info("sql====" + sql.toString());
	      log.info("args====" + args);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      log.error(e);
	      log.error("sql====" + sql.toString());
	      log.error("args====" + args);
	      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
	    }
	    return returnList;
	}
}
