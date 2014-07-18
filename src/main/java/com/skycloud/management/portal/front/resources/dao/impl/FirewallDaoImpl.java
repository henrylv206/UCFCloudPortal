package com.skycloud.management.portal.front.resources.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.h3c.loadbalance.po.vservice.VServiceRowPO;
import com.skycloud.management.portal.admin.audit.dao.impl.AuditDaoImpl;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.FirewallVO;
import com.skycloud.management.portal.front.resources.action.vo.ObjectStorageVO;
import com.skycloud.management.portal.front.resources.dao.IFirewallDao;

/**
 * 防火墙资源使用相关持久化实现
 * @author jiaoyz
 */
public class FirewallDaoImpl extends SpringJDBCBaseDao implements IFirewallDao {
	private static Log log = LogFactory.getLog(FirewallDaoImpl.class);
	
  @SuppressWarnings("unchecked")
  private static final class FirewallVOMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rn) throws SQLException {
      FirewallVO vo = new FirewallVO();
      vo.setComment(rs.getString("comment"));
      vo.setInstanceId(rs.getInt("instanceId"));
      vo.setInstanceName(rs.getString("instanceName"));
      vo.setResourcePool(rs.getString("resourcePool"));
      vo.setRuleNum(rs.getInt("ruleNum"));
      vo.setTemplateId(rs.getInt("templateId"));
      vo.setTemplateName(rs.getString("templateName"));
      vo.setState(rs.getInt("state"));
      vo.setCreateDt(rs.getString("CREATE_DT"));
      vo.setUpdateDt(rs.getString("LASTUPDATE_DT"));
      return vo;
    }
  }
  
  @SuppressWarnings("unchecked")
  private static final class ObjectStorageVOMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rn) throws SQLException {
    	ObjectStorageVO vo = new ObjectStorageVO();
        vo.setComment(rs.getString("comment"));
        vo.setInstanceId(rs.getInt("instanceId"));
        vo.setInstanceName(rs.getString("instanceName"));
        vo.setResourcePool(rs.getString("resourcePool"));
        vo.setStorageSize(rs.getInt("storageSize"));
        vo.setTemplateId(rs.getInt("templateId"));
        vo.setTemplateName(rs.getString("templateName"));
        vo.setState(rs.getInt("state"));
      return vo;
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<ObjectStorageVO> getObjectStorage(int userId, int curPage, int pageSize) throws Exception {
    List<Object> param = new ArrayList<Object>();
    String sql = "SELECT i.ID AS instanceId, i.STATE as state, i.INSTANCE_NAME AS instanceName, i.COMMENT, " +
    		         "  t.ID AS templateId, t.TEMPLATE_DESC AS templateName, i.STORAGE_SIZE AS storageSize, " +
    		         "  p.POOL_NAME AS resourcePool " +
    		         "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
    		         "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
    		         "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
    		         "WHERE o.CREATOR_USER_ID = ? AND i.STATE in (2,3,6) AND t.TYPE = 11";
    param.add(userId);
    if(curPage > 0 && pageSize > 0) {
      sql += " LIMIT ?, ?";
      param.add((curPage - 1) * pageSize);
      param.add(pageSize);
    }
    return getJdbcTemplate().query(sql, param.toArray(), new ObjectStorageVOMapper());
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<FirewallVO> getFirewallList(int userId, int curPage, int pageSize) throws Exception {
    List<Object> param = new ArrayList<Object>();
  //fix bug 2479
    String sql = "SELECT i.ID AS instanceId, i.INSTANCE_NAME AS instanceName, i.COMMENT, i.STATE ,i.CREATE_DT,i.LASTUPDATE_DT," +
    		         "  t.ID AS templateId, t.TEMPLATE_DESC AS templateName, t.STORAGE_SIZE AS ruleNum, " +
    		         "  p.POOL_NAME AS resourcePool " +
    		         "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
    		         "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
    		         "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
    		         "WHERE o.CREATOR_USER_ID = ? AND i.STATE in (2,5,6,7)  AND t.TYPE = 7";
    param.add(userId);
    if(curPage > 0 && pageSize > 0) {
      sql += " LIMIT ?, ?";
      param.add((curPage - 1) * pageSize);
      param.add(pageSize);
    }
    return getJdbcTemplate().query(sql, param.toArray(), new FirewallVOMapper());
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<TInstanceInfoBO> getFWOrLBList(final int userId) throws Exception {
    List<Object> param = new ArrayList<Object>();
    //fix bug 3789
    String sql = "SELECT i.ID , i.INSTANCE_NAME AS instanceName, i.COMMENT, i.TEMPLATE_TYPE," +
    		         "  t.ID AS templateId, t.TEMPLATE_DESC AS templateName, t.STORAGE_SIZE AS ruleNum, " +
    		         "  p.POOL_NAME AS resourcePool " +
    		         "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
    		         "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
    		         "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
    		         "WHERE o.CREATOR_USER_ID = ? AND i.STATE<>4 AND i.STATE<>7 AND (t.TYPE = 7 OR t.TYPE=6) ";
  
    BeanPropertyRowMapper<TInstanceInfoBO> insRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
	List<TInstanceInfoBO> returnList = null;
	try {
		returnList = this.getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, userId);
			}
		},insRowMapper);
	} catch (Exception e) {
		throw new SQLException("查询防火墙负载均衡失败。失败原因：" + e.getMessage());
	}
	return returnList;
  }
  
  @Override
  public List<VServiceRowPO> queryVirtialServiceListByUser(final TUserBO user) throws Exception {
    List<Object> param = new ArrayList<Object>();
    //fix bug 3789
    String sql = "select a.* from T_SCS_ARRAY_VIRTUAL_SERVICE a "
    	+" where a.VPNID = (select v.VPNID from T_SCS_H3C_HLJ_VPN_INSTANCE v where v.USERID = ?)"
    	+" and (a.USERID is NULL || a.USERID =0)  ";
  
    BeanPropertyRowMapper<VServiceRowPO> vsRowMapper = new BeanPropertyRowMapper<VServiceRowPO>(VServiceRowPO.class);
	List<VServiceRowPO> returnList = null;
	try {
		returnList = this.getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, user.getId());
			}
		},vsRowMapper);
	} catch (Exception e) {
		throw new SQLException("查询负载均衡VPN失败。失败原因：" + e.getMessage());
	}
	return returnList;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<TInstanceInfoBO> getBoughtOMList(final int userId) throws Exception {
    List<Object> param = new ArrayList<Object>();
    String sql = "SELECT i.ID , i.INSTANCE_NAME AS instanceName, i.COMMENT, i.TEMPLATE_TYPE," +
    		         "  t.ID AS templateId, t.TEMPLATE_DESC AS templateName, t.STORAGE_SIZE AS ruleNum, " +
    		         "  p.POOL_NAME AS resourcePool " +
    		         "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
    		         "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
    		         "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
    		         "WHERE o.CREATOR_USER_ID = ? AND i.STATE<>4 AND (t.TYPE = 11) ";
  
    BeanPropertyRowMapper<TInstanceInfoBO> insRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
	List<TInstanceInfoBO> returnList = null;
	try {
		returnList = this.getJdbcTemplate().query(sql,new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, userId);
			}
		},insRowMapper);
	} catch (Exception e) {
		throw new SQLException("查询防火墙负载均衡失败。失败原因：" + e.getMessage());
	}
	return returnList;
  }  

  @Override
  public int getFirewallListCount(int userId) throws Exception {
    String sql = "SELECT COUNT(0) " +
                 "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
                 "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
                 "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
                 "WHERE o.CREATOR_USER_ID = ? AND i.STATE in (2,5,6,7) AND t.TYPE = 7";
    return getJdbcTemplate().queryForInt(sql, userId);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<FirewallVO> searchFirewall(String name, int userId, int curPage, int pageSize) throws Exception {
    List<Object> param = new ArrayList<Object>();
    String sql = "SELECT i.ID AS instanceId, i.INSTANCE_NAME AS instanceName, i.COMMENT, " +
                 "  t.ID AS templateId, t.TEMPLATE_DESC AS templateName, t.STORAGE_SIZE AS ruleNum, " +
                 "  p.POOL_NAME AS resourcePool " +
                 "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
                 "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
                 "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
                 "WHERE o.CREATOR_USER_ID = ? AND i.STATE = 2 AND t.TYPE = 7";
    param.add(userId);
    if(StringUtils.isNotBlank(name)) {
      sql += " AND i.INSTANCE_NAME like ?";
      param.add("%" + name.replaceAll("_", "\\\\_") + "%");
    }
    if(curPage > 0 && pageSize > 0) {
      sql += " LIMIT ?, ?";
      param.add((curPage - 1) * pageSize);
      param.add(pageSize);
    }
    return getJdbcTemplate().query(sql, param.toArray(), new FirewallVOMapper());
  }

  @Override
  public int searchFirewallCount(String name, int userId) throws Exception {
    String sql = "SELECT COUNT(0) " +
                 "FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_TEMPLATE_VM t ON i.TEMPLATE_ID = t.ID " +
                 "  LEFT JOIN T_SCS_RESOURCE_POOLS p ON t.RESOURCE_POOLS_ID = p.ID " +
                 "  INNER JOIN T_SCS_ORDER o ON o.ORDER_ID = i.ORDER_ID " +
                 "WHERE o.CREATOR_USER_ID = ? AND i.STATE = 2 AND t.TYPE = 7";
    if(StringUtils.isNotBlank(name)) {
      sql += " AND i.INSTANCE_NAME like ?";
      return getJdbcTemplate().queryForInt(sql, userId, "%" + name.replaceAll("_", "\\\\_") + "%");
    }
    return getJdbcTemplate().queryForInt(sql, userId);
  }
  @Override
  //fix bug 2417
  public List queryBindedVSByIP(int userId){
	  StringBuffer sql = new StringBuffer("SELECT  tsip.`ID` id, tsip.`IP_ADDRESS` ipAddress, tsip.`STATUS` status,tsip.`CREATOR_USER_ID` creatorUserId, tsip.`IP_TYPE` ipType, tsip.`INSTANCE_INFO_ID` instanceInfoId, tsip.`SERVICE_PROVIDER` serviceProvider, tsip.`CREATED_DATE` createdDate, tsip.`LASTUPDATE_DATE` lastupdateDate from T_SCS_INSTANCE_INFO tsii "); 
	  sql.append("inner join T_SCS_ORDER tso on tsii.ORDER_ID=tso.ORDER_ID ");
	  sql.append("inner join T_SCS_PUBLIC_IP tsip on tsii.E_NETWORK_ID = tsip.ID ");
	  sql.append("where tsip.INSTANCE_INFO_ID = -1 and tsii.state <> 4 and tso.CREATOR_USER_ID = ? ");
	  log.debug("--------------------:"+sql);
	  log.debug("--------------------:"+userId);
	  return this.getJdbcTemplate().queryForList(sql.toString(), new Object[]{userId});
  }
}
