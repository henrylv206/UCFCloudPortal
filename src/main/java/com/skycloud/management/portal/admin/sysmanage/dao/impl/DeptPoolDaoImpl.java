package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.skycloud.management.portal.admin.sysmanage.dao.IDeptPoolDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptPoolBO;
import com.skycloud.management.portal.admin.sysmanage.vo.ResPoolDeptRelationVO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

/**
 * 部门与资源池对象持久化接口实现
  *<dl>
  *<dt>类名：DeptPoolDaoImpl</dt>
  *<dd>描述: </dd>
  *<dd>公司: ChinaSkyCloud.com</dd>
  *<dd>创建时间：2012-12-06  上午09:30:10</dd>
  *<dd>创建人：ninghao@chinaskycloud.com</dd>
  *</dl>
 */
public class DeptPoolDaoImpl extends SpringJDBCBaseDao implements IDeptPoolDao{

	private static final Logger logger = Logger.getLogger(DeptPoolDaoImpl.class);
	private String tableName = " T_SCS_DRR ";
	private String tableColumnsSelect = " ID,DEPT_ID,RESOURCE_POOLS_ID resourcePoolId,CREATE_DT,LASTUPDATE_DT,STATE ";
	private String tableColumnsInsert = " DEPT_ID,RESOURCE_POOLS_ID,STATE,CREATE_DT,LASTUPDATE_DT ";

	@Override
	public List<TDeptPoolBO> findDeptPoolByDeptId(final int deptId) throws SQLException {
		List<TDeptPoolBO> dp_list = null;
		if(deptId <= 0){
			return dp_list;
		}

		StringBuffer sb_sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		
		sb_sql.append("SELECT ").append(tableColumnsSelect);
		sb_sql.append("  FROM ").append(tableName);
		sb_sql.append(" WHERE STATE = ? ");
		param.add("1");
		if(deptId > 0){
			sb_sql.append(" AND DEPT_ID = ? ");
			param.add(deptId);
		}
		
	    BeanPropertyRowMapper<TDeptPoolBO> argTypes = new BeanPropertyRowMapper<TDeptPoolBO>(TDeptPoolBO.class);
	    logger.debug(sb_sql.toString()+" | param: " + param.toString());

	    try {
	    	dp_list = this.getJdbcTemplate().query(sb_sql.toString(), param.toArray(), argTypes);
			
	    	logger.info("findDeptPoolByDeptId size==" + dp_list == null ? 0 : dp_list.size());
	    } catch (Exception e) {
	    	logger.error(e);
	    	throw new SQLException(e.getMessage());
	    }
	    
		return dp_list;
	}
	
	@Override
	public int saveDeptPoolRelation(final List<TDeptPoolBO> deptList) throws SQLException {
		int nResult = -1;
		try {
			if(deptList != null && deptList.size() > 0){
				
				StringBuffer sb_sql = new StringBuffer();
				sb_sql.append(" INSERT INTO ").append(tableName);
				sb_sql.append(" ( ").append(tableColumnsInsert);
				sb_sql.append(" ) ");
				sb_sql.append(" VALUES ");
				sb_sql.append(" ( ? , ? , ? , ? , ? ) ");
				
				final Timestamp datetime = new Timestamp(new Date().getTime());
				
				int[] resultArr = this.getJdbcTemplate().batchUpdate(sb_sql.toString(), new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						int index = 1;
						// DEPT_ID,RESOURCE_POOLS_ID,STATE,CREATE_DT
						ps.setInt(index++, deptList.get(i).getDeptId());
						ps.setInt(index++, deptList.get(i).getResourcePoolId());
						ps.setString(index++, deptList.get(i).getState());
						ps.setTimestamp(index++, datetime);
						ps.setTimestamp(index++, datetime);
					}

					@Override
					public int getBatchSize() {
						return deptList.size();
					}
					
				});			
				
				for(int j=0;j<resultArr.length;j++){
					if(resultArr[j] <= 0){
						nResult = -1;
						throw new SQLException("insert部门与资源池关系记录失败。");
					}
				}
			}else{
				nResult = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("insert部门与资源池关系记录失败。失败原因：" + e.getMessage());
		}
		
		return nResult;
	}

	@Override
	public int deleteDeptPoolRelation(final int deptId , int delFlag) throws SQLException {
		int nResult = -1;
		if(deptId <= 0){
			throw new SQLException("删除部门与资源池关系记录失败。失败原因：部门编号" +deptId + "]小于等于0！");
		}
		
		StringBuffer sb_sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();

		if(delFlag == 2){
			sb_sql.append("DELETE ");//物理删除
			sb_sql.append("  FROM ").append(tableName);
		}else{
			sb_sql.append(" UPDATE ").append(tableName);//逻辑删除
			sb_sql.append(" SET ");
			sb_sql.append(" STATE = ? ");
			param.add("2");
			sb_sql.append(" , LASTUPDATE_DT = ? ");
			param.add(new Date());
		}
		
		sb_sql.append(" WHERE STATE = ? ");
		param.add("1");
		
		if(deptId > 0){
			sb_sql.append(" AND DEPT_ID = ? ");
			param.add(deptId);
		}
		
		logger.debug(sb_sql.toString()+" | param: " + param.toString());
		
		try {
    		nResult  = this.getJdbcTemplate().update(sb_sql.toString(), param.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("删除部门与资源池关系记录失败。部门编号：" +deptId + "失败原因：" + e.getMessage());
		}
		
		return nResult ;
	}
	
	/**
	 * 查找订单全部相关资源对应的全部资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findOrderResPoolByOrderId(final int orderId) throws SQLException
	{
		List<ResPoolDeptRelationVO> dp_list = null;
		if(orderId <= 0){
			return dp_list;
		}

		StringBuffer sb_sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		
		sb_sql.append("SELECT ").append(" drr.ID,drr.DEPT_ID deptId,drr.RESOURCE_POOLS_ID poolId ");
		sb_sql.append("      ,pool.POOL_NAME poolName, info.ID resourceId , info.INSTANCE_NAME resourceName ");
		sb_sql.append("  FROM ").append(tableName).append(" drr ");
		sb_sql.append("      ,T_SCS_USER user1 ");//用户表
		sb_sql.append("      ,T_SCS_ORDER order1 ");//订单表
		sb_sql.append("      ,T_SCS_TEMPLATE_VM temp ");//模板表
		sb_sql.append("      ,T_SCS_INSTANCE_INFO info ");//资源表
		sb_sql.append("      ,T_SCS_RESOURCE_POOLS pool ");//资源池
		sb_sql.append(" WHERE drr.STATE = ? ");
		param.add("1");
		sb_sql.append("   AND drr.DEPT_ID = user1.DEPT_ID ");
		sb_sql.append("   AND order1.CREATOR_USER_ID = user1.ID ");
		sb_sql.append("   AND drr.RESOURCE_POOLS_ID = pool.ID ");
		sb_sql.append("   AND temp.RESOURCE_POOLS_ID = pool.ID ");
		sb_sql.append("   AND temp.ID = info.TEMPLATE_ID ");
		sb_sql.append("   AND order1.ORDER_ID = info.ORDER_ID ");
		if(orderId > 0){
			sb_sql.append("   AND order1.ORDER_ID = ? ");
			param.add(orderId);
		}
		
	    BeanPropertyRowMapper<ResPoolDeptRelationVO> argTypes = new BeanPropertyRowMapper<ResPoolDeptRelationVO>(ResPoolDeptRelationVO.class);
	    logger.debug(sb_sql.toString()+" | param: " + param.toString());

	    try {
	    	dp_list = this.getJdbcTemplate().query(sb_sql.toString(), param.toArray(), argTypes);
			
	    	logger.info("findOrderResPoolByOrderId size==" + dp_list == null ? 0 : dp_list.size());
	    } catch (Exception e) {
	    	logger.error(e);
	    	throw new SQLException(e.getMessage());
	    }
	    
		return dp_list;
	
	}
	
	/**
	 * 查找已分配给用户的资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findUserResPoolByUserId(final int userId) throws SQLException
	{
		List<ResPoolDeptRelationVO> dp_list = null;
		if(userId <= 0){
			return dp_list;
		}

		StringBuffer sb_sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		
		sb_sql.append("SELECT ").append(" drr.ID,drr.DEPT_ID deptId,drr.RESOURCE_POOLS_ID poolId ");
		sb_sql.append("      ,pool.POOL_NAME poolName, info.ID resourceId , info.INSTANCE_NAME resourceName ");
		sb_sql.append("  FROM ").append(tableName).append(" drr ");
		sb_sql.append("      ,T_SCS_USER user1 ");//用户表
		sb_sql.append("      ,T_SCS_ORDER order1 ");//订单表
		sb_sql.append("      ,T_SCS_TEMPLATE_VM temp ");//模板表
		sb_sql.append("      ,T_SCS_INSTANCE_INFO info ");//资源表
		sb_sql.append("      ,T_SCS_RESOURCE_POOLS pool ");//资源池
		sb_sql.append(" WHERE drr.STATE = ? ");
		param.add("1");
		sb_sql.append("   AND drr.DEPT_ID = user1.DEPT_ID ");
		sb_sql.append("   AND order1.CREATOR_USER_ID = user1.ID ");
		sb_sql.append("   AND drr.RESOURCE_POOLS_ID = pool.ID ");
		sb_sql.append("   AND temp.RESOURCE_POOLS_ID = pool.ID ");
		sb_sql.append("   AND temp.ID = info.TEMPLATE_ID ");
		sb_sql.append("   AND order1.ORDER_ID = info.ORDER_ID ");
		if(userId > 0){
			sb_sql.append("   AND order1.CREATOR_USER_ID = ? ");
			param.add(userId);
		}
		
	    BeanPropertyRowMapper<ResPoolDeptRelationVO> argTypes = new BeanPropertyRowMapper<ResPoolDeptRelationVO>(ResPoolDeptRelationVO.class);
	    logger.debug(sb_sql.toString()+" | param: " + param.toString());

	    try {
	    	dp_list = this.getJdbcTemplate().query(sb_sql.toString(), param.toArray(), argTypes);
			
	    	logger.info("findUserResPoolByUserId size==" + dp_list == null ? 0 : dp_list.size());
	    } catch (Exception e) {
	    	logger.error(e);
	    	throw new SQLException(e.getMessage());
	    }
	    
		return dp_list;
	
	}
	

}
