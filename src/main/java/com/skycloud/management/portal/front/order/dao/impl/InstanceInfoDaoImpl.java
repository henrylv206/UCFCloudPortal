package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TVirtualServiceBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.enumtype.InstanceState;
import com.skycloud.management.portal.front.resources.enumtype.ServiceType;

public class InstanceInfoDaoImpl extends SpringJDBCBaseDao implements IInstanceInfoDao {

  private static Log log = LogFactory.getLog(InstanceInfoDaoImpl.class);

  private String returnVMSql(String id, int resourcePoolsId, int zoneId) {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID, t.resource_pools_id,t.zone_id  ");
    if (id != null && !"".equals(id)) {
      sql.append(" ,t.TEMPLATE_DESC,o.REASON,i.CLUSTER_ID,i.TEMPLATE_TYPE,t.TEMPLATE_DESC,tsrp.pool_name,t.CPUFREQUENCY,i.CPU_NUM,i.MEMORY_SIZE,i.OS_DESC,t.id templateId,i.STATE,t.network_desc , i.RES_CODE  ");
      Idwhere = " and i.ID=?";
    }
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID  ");
    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,3,5,6,7) and  i.TEMPLATE_TYPE='1' and t.TYPE='1'");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);
    return sql.toString();
  }

  private String returnMonitorSql(String id, int resourcePoolsId, int zoneId) {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    sql.append("select  i.ID,i.RESOURCE_INFO resource_info,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID   ");
    sql.append(", t.resource_pools_id,t.zone_id  ");
    if (id != null && !"".equals(id)) {
      sql.append(" ,t.TEMPLATE_DESC,o.REASON,i.CLUSTER_ID,i.TEMPLATE_TYPE,t.TEMPLATE_DESC,tsrp.pool_name,t.CPUFREQUENCY,i.CPU_NUM,i.MEMORY_SIZE,i.OS_DESC,i.E_INSTANCE_ID,t.id templateId,i.STATE,t.network_desc , i.RES_CODE  ");
      Idwhere = " and i.ID=?";
    }
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID  ");
    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,5,6,7) and  i.TEMPLATE_TYPE='5' and t.TYPE='5'");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);
    return sql.toString();
  }

  private String returnLoadbalanceSql(String id, int resourcePoolsId, int zoneId) {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    sql.append("select  i.ID,i.RESOURCE_INFO resource_info,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID,i.OS_DESC,i.TEMPLATE_TYPE,i.CREATE_DT,i.LASTUPDATE_DT");
    sql.append(" , t.id templateId, t.LB_PROTOCOL, t.LB_POLICY, t.LB_PORT, t.resource_pools_id,t.zone_id  ");// 何涛：补充黑龙江poc字段
    if (id != null && !"".equals(id)) {
      sql.append(" ,t.TEMPLATE_DESC,o.REASON,i.CLUSTER_ID,i.TEMPLATE_TYPE,t.TEMPLATE_DESC,tsrp.pool_name,t.CPUFREQUENCY,i.CPU_NUM,i.MEMORY_SIZE,i.OS_DESC,i.E_INSTANCE_ID,i.STATE,t.network_desc , i.RES_CODE  ");
      Idwhere = " and i.ID=?";
    }
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID  ");
    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,5,6,7) and  i.TEMPLATE_TYPE='6' and t.TYPE='6'");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);
    return sql.toString();
  }

  // template_type =3
  private String returnHCSql(String id, int resourcePoolsId, int zoneId) {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.PRODUCT_ID ");
    sql.append(", t.resource_pools_id,t.zone_id  ");
    if (id != null && !"".equals(id)) {
      sql.append(" ,i.TEMPLATE_TYPE,i.CLUSTER_ID ,t.TEMPLATE_DESC,tsrp.pool_name,t.CPUFREQUENCY,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.OS_DESC,i.E_INSTANCE_ID,t.id templateId,i.STATE  ");
      Idwhere = " and i.ID=?";
    }
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,5,6) and i.TEMPLATE_TYPE='3' ");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);
    return sql.toString();
  }

  // template_type =3
  private String returnHCSqlAllInfo() {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    sql.append("select  i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.PRODUCT_ID ,i.CREATE_DT ,i.E_INSTANCE_ID, t.resource_pools_id,t.zone_id  ");
    sql.append(" ,i.TEMPLATE_TYPE,i.CLUSTER_ID ,t.TEMPLATE_DESC,tsrp.pool_name,t.CPUFREQUENCY,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.OS_DESC,i.E_INSTANCE_ID,t.id templateId,i.STATE  ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,5,3,6) and i.TEMPLATE_TYPE='3' ");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    return sql.toString();
  }

  private String returnVolumeSql(String id, int resourcePoolsId, int zoneId) {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    sql.append("select i.ID,i.INSTANCE_NAME,i.STORAGE_SIZE,tvm.STATE VOLUMESTATE,i.STATE,i.E_INSTANCE_ID,tsrp.pool_name, tvm.INSTANCE_NAME VMNAME,tvm.id vmInstanceId,tvm.vmElasterID,i.PRODUCT_ID,i.ZONE_ID,i.COMMENT,i.CREATE_DT, i.LASTUPDATE_DT ");
    sql.append(", t.resource_pools_id,t.zone_id  ");
    if (id != null) {
      sql.append(" ,i.E_INSTANCE_ID ,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON ,tsrp.pool_name, tvm.INSTANCE_NAME VMNAME,tvm.id vmInstanceId,tvm.vmElasterID");
      Idwhere = " and i.ID=? ";
    }
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join (select tsr.DISK_INSTANCE_INFO_ID ,tsii.INSTANCE_NAME ,tsr.STATE,tsii.id,tsii.E_INSTANCE_ID vmElasterID from T_SCS_IRI tsr ");
    sql.append("inner join (select tn.DISK_INSTANCE_INFO_ID,DATE_FORMAT(MAX(tn.CREATE_DT),'%Y%m%d%k%i%s') CREATE_DT from T_SCS_IRI  tn group by tn.DISK_INSTANCE_INFO_ID) tnt on tnt.DISK_INSTANCE_INFO_ID=tsr.DISK_INSTANCE_INFO_ID and tnt.CREATE_DT= DATE_FORMAT(tsr.CREATE_DT,'%Y%m%d%k%i%s') ");
    sql.append("left join T_SCS_INSTANCE_INFO tsii on tsr.VM_INSTANCE_INFO_ID=tsii.id where tsr.state NOT IN (3)) tvm on tvm.DISK_INSTANCE_INFO_ID = i.ID ");
    sql.append("left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE in (2,6) and  i.TEMPLATE_TYPE=2 and t.TYPE='2'");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);
    return sql.toString();
  }

  private String returnDiskSql(String id, int resourcePoolsId, int zoneId) {
    StringBuffer sql = new StringBuffer();
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    sql.append("select i.ID,i.INSTANCE_NAME,i.STORAGE_SIZE,tvm.STATE VOLUMESTATE,i.STATE,i.E_INSTANCE_ID,tsrp.pool_name, tvm.INSTANCE_NAME VMNAME,tvm.id vmInstanceId,tvm.vmElasterID,i.PRODUCT_ID,i.ZONE_ID,i.RES_CODE ");
    sql.append(", t.resource_pools_id,t.zone_id  ");
    if (id != null) {
      sql.append(" ,i.E_INSTANCE_ID ,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON ,tsrp.pool_name, tvm.INSTANCE_NAME VMNAME,tvm.id vmInstanceId,tvm.vmElasterID");
      Idwhere = " and i.ID=? ";
    }
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join (select tsr.DISK_INSTANCE_INFO_ID ,tsii.INSTANCE_NAME ,tsr.STATE,tsii.id,tsii.E_INSTANCE_ID vmElasterID from T_SCS_IRI tsr ");
    sql.append("inner join (select tn.DISK_INSTANCE_INFO_ID,DATE_FORMAT(MAX(tn.CREATE_DT),'%Y%m%d%k%i%s') CREATE_DT from T_SCS_IRI  tn group by tn.DISK_INSTANCE_INFO_ID) tnt on tnt.DISK_INSTANCE_INFO_ID=tsr.DISK_INSTANCE_INFO_ID and tnt.CREATE_DT= DATE_FORMAT(tsr.CREATE_DT,'%Y%m%d%k%i%s') ");
    sql.append("left join T_SCS_INSTANCE_INFO tsii on tsr.VM_INSTANCE_INFO_ID=tsii.id where tsr.state NOT IN (3)) tvm on tvm.DISK_INSTANCE_INFO_ID = i.ID ");
    sql.append("left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE in (2,6) and  i.TEMPLATE_TYPE=12 and t.TYPE='12'");
    sql.append(" and i.INSTANCE_NAME like ?");
    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);
    return sql.toString();
  }

  // ////////
  private String returnVMCountSql(String name, int resourcePoolsId, int zoneId) {
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    StringBuffer sql = new StringBuffer();
    sql.append("select  COUNT(0) ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID  ");
    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,3,5,6,7) and  i.TEMPLATE_TYPE='1' and t.TYPE='1'");
    sql.append(" and i.INSTANCE_NAME like ?");
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
      sql.append(resourcePoolsIdwhere);
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
      sql.append(zoneIdwhere);
    }
    return sql.toString();
  }

  private String returnHCCountSql(String name, int resourcePoolsId, int zoneId) {
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    StringBuffer sql = new StringBuffer();
    sql.append("select  COUNT(0) ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,5,3,6) and i.TEMPLATE_TYPE='2' ");
    sql.append(" and i.INSTANCE_NAME like ?");
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
      sql.append(resourcePoolsIdwhere);
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
      sql.append(zoneIdwhere);
    }
    return sql.toString();
  }

  private String returnVolumeCountSql(String name, int resourcePoolsId, int zoneId) {
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    StringBuffer sql = new StringBuffer();
    sql.append("select COUNT(i.ID)  ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join (select tsr.DISK_INSTANCE_INFO_ID ,tsii.INSTANCE_NAME ,tsr.STATE,tsii.id   from T_SCS_IRI tsr ");
    sql.append("inner join (select tn.id,MAX(tn.CREATE_DT) from T_SCS_IRI  tn group by tn.DISK_INSTANCE_INFO_ID) tnt on tnt.id=tsr.id ");
    sql.append("left join T_SCS_INSTANCE_INFO tsii on tsr.VM_INSTANCE_INFO_ID=tsii.id where tsr.state NOT IN (3)) tvm on tvm.DISK_INSTANCE_INFO_ID = i.ID ");
    sql.append("left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE in (2,6) and  i.TEMPLATE_TYPE=2 and t.TYPE='2'");
    sql.append(" and i.INSTANCE_NAME like ?");
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
      sql.append(resourcePoolsIdwhere);
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
      sql.append(zoneIdwhere);
    }
    return sql.toString();
  }

  private String returnDiskCountSql(String name, int resourcePoolsId, int zoneId) {
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    StringBuffer sql = new StringBuffer();
    sql.append("select COUNT(i.ID)  ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join (select tsr.DISK_INSTANCE_INFO_ID ,tsii.INSTANCE_NAME ,tsr.STATE,tsii.id   from T_SCS_IRI tsr ");
    sql.append("inner join (select tn.id,MAX(tn.CREATE_DT) from T_SCS_IRI  tn group by tn.DISK_INSTANCE_INFO_ID) tnt on tnt.id=tsr.id ");
    sql.append("left join T_SCS_INSTANCE_INFO tsii on tsr.VM_INSTANCE_INFO_ID=tsii.id where tsr.state NOT IN (3)) tvm on tvm.DISK_INSTANCE_INFO_ID = i.ID ");
    sql.append("left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE in (2,6) and  i.TEMPLATE_TYPE=12 and t.TYPE='12'");
    // sql.append(" and i.INSTANCE_NAME like ?");
    if (resourcePoolsId != 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
      sql.append(resourcePoolsIdwhere);
    }
    if (zoneId != 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
      sql.append(zoneIdwhere);
    }
    return sql.toString();
  }

  private String returnBackupCountSql() {
    StringBuffer sql = new StringBuffer();
    sql.append("select COUNT(i.ID)  ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE<>4 and  i.TEMPLATE_TYPE=4");
    return sql.toString();
  }

  private String returnMonitorCountSql() {
    StringBuffer sql = new StringBuffer();
    sql.append("select COUNT(i.ID)  ");
    sql.append(" FROM T_SCS_INSTANCE_INFO i join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE<>4 and  i.TEMPLATE_TYPE=5");
    return sql.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.front.order.dao.IInstanceInfoDao#
   * queryInstanceCountByUser
   * (com.skycloud.management.portal.front.resources.action
   * .vo.ResourcesQueryVO)
   */
  @Override
  public int queryInstanceCountByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    log.debug("-----------------------queryInstanceCountByUser(ResourcesQueryVO vo)  begin!------------------------");
    String sql;
    String name = vo.getName();
    switch (vo.getOperateSqlType()) {
    case 1:
      sql = returnVMCountSql(name, resourcePoolsId, zoneId);
      break;
    case 2:
      sql = returnVolumeCountSql(name, resourcePoolsId, zoneId);
      break;
    case 3:
      sql = returnHCCountSql(name, resourcePoolsId, zoneId);
      break;
    case 4:// 4是备份服务
      sql = returnBackupCountSql();
      break;
    case 5:// 5是监控服务
      sql = returnMonitorCountSql();
      break;
    case 12:
      sql = returnDiskCountSql(name, resourcePoolsId, zoneId);
      break;
    default:
      return 0;
    }
    int index = 0;
    // Object[] args = new Object[2];
    // try {
    // args[0] = vo.getUser().getId();
    // if (name == null) {
    // name = "";
    // }
    // args[1] = "%" + name + "%";
    // index = this.getJdbcTemplate().queryForInt(sql, args);
    List<Object> args = new ArrayList<Object>();
    try {
      args.add(vo.getUser().getId());
      if (vo.getOperateSqlType() <= 3) {
        if (name == null) {
          name = "";
        }
        name = "%" + name + "%";
        args.add(name);
      }
      index = this.getJdbcTemplate().queryForInt(sql, args.toArray());
      log.debug("sql====" + sql.toString());
    } catch (Exception e) {
      log.error(e);
      log.info("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_COUNT_DESC);
    }
    log.debug("-----------------------queryInstanceCountByUser(ResourcesQueryVO vo)  end!------------------------");
    return index;
  }

  @Override
  public int checkDiskBind(int instanceID, int userID) throws SCSException {

    StringBuffer sql = new StringBuffer();

    sql.append("SELECT COUNT(ID) FROM T_SCS_IRI where DISK_INSTANCE_INFO_ID = ? and STATE = 2 and CREATE_USER_ID = ? ");

    int index = 0;

    List<Object> args = new ArrayList<Object>();
    try {
      args.add(instanceID);
      args.add(userID);
      index = this.getJdbcTemplate().queryForInt(sql.toString(), args.toArray());
    } catch (Exception e) {
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_COUNT_DESC);
    }

    return index;
  }

  private PreparedStatementCreator PreparedSaveSQLArgs(final TInstanceInfoBO instanceInfo, final String sql) {
    return new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, instanceInfo.getOrderId());
        ps.setInt(2, instanceInfo.getTemplateType());
        ps.setInt(3, instanceInfo.getTemplateId());
        ps.setString(4, instanceInfo.getComment());
        ps.setString(5, instanceInfo.getResourceInfo());

        ps.setInt(6, instanceInfo.getCpuNum());
        ps.setInt(7, instanceInfo.getMemorySize());
        ps.setInt(8, instanceInfo.getStorageSize());
        ps.setLong(9, instanceInfo.getZoneId());
        ps.setLong(10, instanceInfo.getClusterId());

        ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
        ps.setInt(12, instanceInfo.getState());

        ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
        ps.setLong(14, instanceInfo.geteInstanceId());
        ps.setLong(15, instanceInfo.geteServiceId());
        ps.setLong(16, instanceInfo.geteDiskId());
        ps.setLong(17, instanceInfo.geteNetworkId());

        ps.setLong(18, instanceInfo.geteOsId());
        ps.setString(19, instanceInfo.getOsDesc());
        ps.setString(20, instanceInfo.getInstanceName());
        ps.setInt(21, instanceInfo.getProductId());
        if (instanceInfo.getExpireDate() != null) {
          ps.setTimestamp(22, new Timestamp(instanceInfo.getExpireDate().getTime()));
        } else {
          ps.setTimestamp(22, null);
        }

        return ps;
      }
    };
  }

  @Override
  public int save(final TInstanceInfoBO instanceInfo) throws SCSException {
    int ret_val = -1;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql = "INSERT INTO T_SCS_INSTANCE_INFO(" + "ORDER_ID,TEMPLATE_TYPE,TEMPLATE_ID,COMMENT,RESOURCE_INFO,"
        + "CPU_NUM,MEMORY_SIZE,STORAGE_SIZE," + "ZONE_ID,CLUSTER_ID,CREATE_DT,STATE," + "LASTUPDATE_DT,E_INSTANCE_ID,E_SERVICE_ID,"
        + "E_DISK_ID,E_NETWORK_ID,E_OS_ID,OS_DESC,INSTANCE_NAME,PRODUCT_ID,EXPIRE_DATE) VALUES "
        + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    try {
    	log.debug("-------------save t_scs_instance_info   start!------------------------");
    	// List<Object> args = initInsertSQLArgs(instanceInfo);
    	// ret_val = this.getJdbcTemplate().update(sql, args.toArray());
    	PreparedStatementCreator preCreator = PreparedSaveSQLArgs(instanceInfo, sql);
    	this.getJdbcTemplate().update(preCreator, keyHolder);
    	ret_val = keyHolder.getKey().intValue();
    } catch (Exception e) {
    	e.printStackTrace();
    	log.error(e);
    	throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_CREATE, instanceInfo.getResourceInfo(),
          instanceInfo.getCreateDt()));
    }
    log.debug("-------------save t_scs_instance_info   end!------------------------");
    return ret_val;
  }

  @Override
  public int delete(final int id) throws SQLException {
    int ret_val = -1;
    String sql = "DELETE FROM T_SCS_INSTANCE_INFO WHERE ID = ?;";
    try {
      log.debug("-------------delete t_scs_instance_info   begin!------------------------");
      ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, id);
        }
      });
    } catch (Exception e) {
      log.error("delete t_scs_instance_info   error:" + e.getMessage());
      throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_DELETE, id, e.getMessage()));
    }
    log.debug("-------------delete t_scs_instance_info   end!------------------------");
    return ret_val;
  }

  @Override
  public int update(final TInstanceInfoBO instanceInfo) throws SCSException {
    int ret_val = 0;
    String sql = "UPDATE T_SCS_INSTANCE_INFO set ORDER_ID = ?,TEMPLATE_TYPE = ?,TEMPLATE_ID = ?,COMMENT = ?,RESOURCE_INFO = ?,CPU_NUM = ?,MEMORY_SIZE = ?,STORAGE_SIZE = ?,ZONE_ID = ?,"
        + "CLUSTER_ID = ?,STATE = ?,LASTUPDATE_DT = ?,E_INSTANCE_ID = ?,E_SERVICE_ID = ?,E_DISK_ID = ?, E_NETWORK_ID = ?,E_OS_ID = ?,OS_DESC = ?,INSTANCE_NAME = ?,PRODUCT_ID=?,E_HOST_ID=? WHERE ID = ?;";
    List<Object> args = new ArrayList<Object>();

    args.add(instanceInfo.getOrderId());
    args.add(instanceInfo.getTemplateType());
    args.add(instanceInfo.getTemplateId());
    args.add(instanceInfo.getComment());
    args.add(instanceInfo.getResourceInfo());

    args.add(instanceInfo.getCpuNum());
    args.add(instanceInfo.getMemorySize());
    args.add(instanceInfo.getStorageSize());
    args.add(instanceInfo.getZoneId());
    args.add(instanceInfo.getClusterId());

    args.add(instanceInfo.getState());
    args.add(new Timestamp(System.currentTimeMillis()));
    args.add(instanceInfo.geteInstanceId());
    args.add(instanceInfo.geteServiceId());
    args.add(instanceInfo.geteDiskId());
    args.add(instanceInfo.geteNetworkId());

    args.add(instanceInfo.geteOsId());
    args.add(instanceInfo.getOsDesc());
    args.add(instanceInfo.getInstanceName());
    args.add(instanceInfo.getProductId());

    args.add(instanceInfo.geteHostId());

    args.add(instanceInfo.getId());

    try {
      log.debug("-------------update t_scs_instance_info   begin!------------------------");
      ret_val = this.getJdbcTemplate().update(sql, args.toArray());
    } catch (Exception e) {
      log.error("update t_scs_instance_info  error:" + e.getMessage());
      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_UPDATE, instanceInfo.getResourceInfo(),
          instanceInfo.getLastupdateDt()));
    }
    log.debug("-------------update t_scs_instance_info   end!------------------------");
    return ret_val;
  }

  @Override
  public int updateServiceState(final int serviceID) throws SCSException {
    int ret_val = 0;
    // 将状态备份
    String sql_backupState = "update T_SCS_SERVICE_INSTANCE set NUM = STATE where id = " + serviceID;
    this.getJdbcTemplate().execute(sql_backupState);
    // 服务实例的申请退订状态是5 update by hefk 20120908
    // String sql =
    // "update T_SCS_SERVICE_INSTANCE set T_SCS_SERVICE_INSTANCE.STATE=6 where id = ?;";
    String sql = "update T_SCS_SERVICE_INSTANCE set T_SCS_SERVICE_INSTANCE.STATE=5 where id = ?;";
    List<Object> args = new ArrayList<Object>();
    args.add(serviceID);
    try {
      ret_val = this.getJdbcTemplate().update(sql, args.toArray());
    } catch (Exception e) {
    }
    return ret_val;
  }

  @Override
  public int updateServiceState4(final int serviceID) throws SCSException {
    int ret_val = 0;
    String sql = "update T_SCS_SERVICE_INSTANCE set T_SCS_SERVICE_INSTANCE.STATE=4 where id = ?;";
    List<Object> args = new ArrayList<Object>();
    args.add(serviceID);
    try {
      ret_val = this.getJdbcTemplate().update(sql, args.toArray());
    } catch (Exception e) {
    }
    return ret_val;
  }

  @Override
  public TServiceInstanceBO searchServiceInstanceById(int ID) throws SQLException {
    BeanPropertyRowMapper<TServiceInstanceBO> argTypes = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
    String sql = "SELECT info.ID, info.EXPIRY_DATE, info.PERIOD, info.UNIT, p.PRICE FROM T_SCS_SERVICE_INSTANCE info INNER JOIN T_SCS_PRODUCT p ON p.ID = info.PRODUCT_ID WHERE info.ID = ? ";
    Object[] args = new Object[] { ID };
    List<TServiceInstanceBO> result = null;
    try {
      result = this.getJdbcTemplate().query(sql, args, argTypes);
    } catch (Exception e) {
      throw new SQLException("query InstanceInfo error:" + e.getMessage());
    }
    if (result != null && result.size() > 0) {
      return result.get(0);
    }
    return null;
  }

  @Override
  public TServiceInstanceBO findServiceInstanceById(int ID) throws SQLException {
    BeanPropertyRowMapper<TServiceInstanceBO> argTypes = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
    String sql = "SELECT * FROM T_SCS_SERVICE_INSTANCE info WHERE info.ID = ? ";
    Object[] args = new Object[] { ID };
    List<TServiceInstanceBO> result = null;
    try {
      result = this.getJdbcTemplate().query(sql, args, argTypes);
    } catch (Exception e) {
      throw new SQLException("query InstanceInfo error:" + e.getMessage());
    }
    if (result != null && result.size() > 0) {
      return result.get(0);
    }
    return null;
  }

  private String sqlInstanceTemplate() {
    String sql = "";
    sql = "select i1.*,tvm.RESOURCE_POOLS_ID from T_SCS_INSTANCE_INFO i1 " + " left join T_SCS_TEMPLATE_VM tvm on tvm.ID = i1.TEMPLATE_ID "
        + " where i1.TEMPLATE_TYPE<>3 " + " union " + " select i2.*,tmc.RESOURCE_POOLS_ID from T_SCS_INSTANCE_INFO i2 "
        + " left join T_SCS_TEMPLATE_MC tmc on tmc.ID = i2.TEMPLATE_ID" + " where i2.TEMPLATE_TYPE=3 ";
    return sql;
  }

  @Override
  public TInstanceInfoBO searchInstanceInfoByID(final int ID) throws SCSException {
    String sql = "SELECT i.ID, i.ORDER_ID, i.TEMPLATE_TYPE, i.TEMPLATE_ID, i.COMMENT, i.RESOURCE_INFO, i.CPU_NUM, i.MEMORY_SIZE, i.STORAGE_SIZE, i.ZONE_ID, "
        + " i.CLUSTER_ID, i.CREATE_DT, i.STATE, i.LASTUPDATE_DT, i.E_INSTANCE_ID, i.E_SERVICE_ID, i.E_DISK_ID, i.E_NETWORK_ID, i.E_OS_ID, "
        + " i.OS_DESC, i.INSTANCE_NAME, i.PRODUCT_ID, i.RES_CODE, o.CREATOR_USER_ID as userid ,i.RESOURCE_POOLS_ID, i.E_HOST_ID "
        + " FROM ("
        + sqlInstanceTemplate() + ") i  left join T_SCS_ORDER o on i.ORDER_ID = o.ORDER_ID " + " WHERE i.ID = ?;";

    BeanPropertyRowMapper<TInstanceInfoBO> instanceinfoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      log.debug("-------------searchInstanceInfoByID(int ID)  begin!------------------------");
      log.debug(" id=" + ID);
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, ID);
        }
      }, new RowMapperResultSetExtractor<TInstanceInfoBO>(instanceinfoRowMapper));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("searchInstanceInfoByID(int ID)  error: " + e.getMessage());
      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_QUERY));
    }
    log.debug("-------------searchInstanceInfoByID(int ID)  end!------------------------");
    if (returnList != null && returnList.size() > 0) {
      return returnList.get(0);
    } else {
      return null;
    }
  }

  @Override
  public List<TInstanceInfoBO> searchAllInstanceInfo() throws SCSException {
    String sql = "SELECT ORDER_ID,TEMPLATE_TYPE,TEMPLATE_ID,COMMENT,RESOURCE_INFO," + "CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,"
        + "ZONE_ID,CLUSTER_ID,CREATE_DT,STATE," + "LASTUPDATE_DT,E_INSTANCE_ID,E_SERVICE_ID,"
        + "E_DISK_ID,E_NETWORK_ID,E_OS_ID,OS_DESC,INSTANCE_NAME,PRODUCT_ID FROM T_SCS_INSTANCE_INFO order by ID desc ;";
    BeanPropertyRowMapper<TInstanceInfoBO> instanceinfoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);

    List<TInstanceInfoBO> returnList = null;
    try {
      log.debug("-------------searchAllInstanceInfo() begin!------------------------");
      returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TInstanceInfoBO>(instanceinfoRowMapper));
    } catch (Exception e) {
      log.error("searchAllInstanceInfo()  error: " + e.getMessage());
      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_QUERY));
    }
    log.debug("-------------searchAllInstanceInfo() begin!------------------------");
    return returnList;
  }

  // @Override
  // public List<TInstanceInfoBO> searchAllAnInstanceInfo(int type, int
  // curPage,
  // int pageSize) throws SCSException {
  // curPage = (curPage - 1) * pageSize;
  // String sql =
  // "SELECT ORDER_ID,TEMPLATE_TYPE,TEMPLATE_ID,COMMENT,RESOURCE_INFO," +
  // "CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,"
  // + "ZONE_ID,CLUSTER_ID,CREATE_DT,STATE," +
  // "LASTUPDATE_DT,E_INSTANCE_ID,E_SERVICE_ID,"
  // +
  // "E_DISK_ID,E_NETWORK_ID,E_OS_ID,OS_DESC,INSTANCE_NAME,PRODUCT_ID FROM T_SCS_INSTANCE_INFO"
  // + " where TEMPLATE_TYPE = "+ type + " order by ID desc limit " + curPage
  // +
  // "," + pageSize + ";";
  // BeanPropertyRowMapper<TInstanceInfoBO> instanceinfoRowMapper = new
  // BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
  //
  // List<TInstanceInfoBO> returnList = null;
  // try {
  // returnList = this.getJdbcTemplate().query(sql, new
  // RowMapperResultSetExtractor<TInstanceInfoBO>(instanceinfoRowMapper));
  // } catch (Exception e) {
  // throw new
  // SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_QUERY));
  // }
  // return returnList;
  // }
  //
  // @Override
  // public List<TInstanceInfoBO> searchAllAnInstanceInfo(int type) throws
  // SCSException {
  // String sql =
  // "SELECT ORDER_ID,TEMPLATE_TYPE,TEMPLATE_ID,COMMENT,RESOURCE_INFO," +
  // "CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,"
  // + "ZONE_ID,CLUSTER_ID,CREATE_DT,STATE," +
  // "LASTUPDATE_DT,E_INSTANCE_ID,E_SERVICE_ID,"
  // +
  // "E_DISK_ID,E_NETWORK_ID,E_OS_ID,OS_DESC,INSTANCE_NAME,PRODUCT_ID FROM T_SCS_INSTANCE_INFO"
  // + " where TEMPLATE_TYPE = "+ type + ";";
  // BeanPropertyRowMapper<TInstanceInfoBO> instanceinfoRowMapper = new
  // BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
  //
  // List<TInstanceInfoBO> returnList = null;
  // try {
  // returnList = this.getJdbcTemplate().query(sql, new
  // RowMapperResultSetExtractor<TInstanceInfoBO>(instanceinfoRowMapper));
  // } catch (Exception e) {
  // throw new
  // SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_QUERY));
  // }
  // return returnList;
  // }

  /**
   * 获取当前用户实例信息
   */
  @Override
  public List<ResourcesVO> queryInstanceInfoByUser(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    String sql;

    String id = vo.getId();
    switch (vo.getOperateSqlType()) {
    case 1:
    case 10:
      sql = returnVMSql(id, resourcePoolsId, zoneId);
      break;
    case 2:
      sql = returnVolumeSql(id, resourcePoolsId, zoneId);
      break;
    case 3:
      if (id == null || "".equals(id)) {
        sql = returnHCSqlAllInfo();
      } else {
        sql = returnHCSql(id, resourcePoolsId, zoneId);
      }
      break;
    case 5:// 5是监控服务
      sql = returnMonitorSql(id, resourcePoolsId, zoneId);
      break;
    case 6:// 6是负载均衡
      sql = returnLoadbalanceSql(id, resourcePoolsId, zoneId);
      break;
    case 12:
      sql = returnDiskSql(id, resourcePoolsId, zoneId);
      break;
    default:
      return null;
    }
    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
    List<ResourcesVO> returnList = null;
    List<Object> args = new ArrayList<Object>();
    try {
      log.debug("---------------------queryInstanceInfoByUser()              begin!---------------");
      args.add(vo.getUser().getId());
      String name = vo.getName();
      if (name == null) {
        name = "";
      }
      args.add("%" + name + "%");
      if (id != null && !"".equals(id)) {
        args.add(id);
      }
      PageVO page = vo.getPage();
      if (page != null) {
        int curPage = page.getCurPage();
        int pageSize = page.getPageSize();
        if (curPage > 0 && pageSize > 0) {
          sql += " limit ?, ?";
          args.add((curPage - 1) * pageSize);
          args.add(pageSize);
        }
      }
      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
      log.debug("sql====" + sql.toString());
      log.debug("args====" + args);
    } catch (Exception e) {
      log.error(e);
      log.error("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
    }
    log.debug("---------------------queryInstanceInfoByUser()              end!---------------");
    return returnList;
  }

  @Override
  public List<TPublicIPBO> queryBindVM() throws SCSException {
    log.debug("---------------------queryBindVM()              start!---------------");
    String sql = "select * from T_SCS_PUBLIC_IP where STATUS=1 and INSTANCE_INFO_ID>0";
    BeanPropertyRowMapper<TPublicIPBO> infoRowMapper = new BeanPropertyRowMapper<TPublicIPBO>(TPublicIPBO.class);
    List<TPublicIPBO> returnList = null;
    log.debug("sql====" + sql.toString());
    returnList = this.getJdbcTemplate().query(sql, infoRowMapper);
    log.debug("---------------------queryBindVM()              end!---------------");
    return returnList;
  }

  @Override
  public TVirtualServiceBO queryVirtualServiceByUser(int userId) throws SCSException {
    TVirtualServiceBO vs = null;
    // "SELECT * FROM T_SCS_H3C_VIRTUAL_SERVICE WHERE USERID = ?";
    String sql = "select * from T_SCS_ARRAY_VIRTUAL_SERVICE where userid=?";
    BeanPropertyRowMapper<TVirtualServiceBO> infoRowMapper = new BeanPropertyRowMapper<TVirtualServiceBO>(TVirtualServiceBO.class);
    List<TVirtualServiceBO> returnList = null;
    List<Object> args = new ArrayList<Object>();
    try {
      log.debug("---------------------queryH3CVirtualServiceByUser(int userId)              start!---------------");
      args.add(Integer.valueOf(userId));
      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
      if (null != returnList && !returnList.isEmpty()) {
        vs = returnList.get(0);
      }
      log.debug("sql====" + sql.toString());
      log.debug("args====" + args);
    } catch (Exception e) {
      log.error(e);
      log.error("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
    }
    log.debug("---------------------queryVirtualServiceByUser(int userId)              end!---------------");
    return vs;
  }

  /**
   * 获取当前用户实例信息
   */
  @Override
  public List<ResourcesVO> queryInstanceInfoByUserId(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    String sql;

    String id = vo.getId();
    switch (vo.getOperateSqlType()) {
    case 1:
      sql = returnVMSql(id, resourcePoolsId, zoneId);
      break;
    case 2:
      sql = returnVolumeSql(id, resourcePoolsId, zoneId);
      break;
    case 3:
      sql = returnHCSql(id, resourcePoolsId, zoneId);
      break;
    case 5:// 5是监控服务
      sql = returnMonitorSql(id, resourcePoolsId, zoneId);
      break;
    case 6:// 6是负载均衡
      sql = returnLoadbalanceSql(id, resourcePoolsId, zoneId);
      break;
    default:
      return null;
    }
    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
    List<ResourcesVO> returnList = null;
    List<Object> args = new ArrayList<Object>();
    try {
      log.debug("---------------------queryInstanceInfoByUserId(ResourcesQueryVO vo)              begin!---------------");
      args.add(vo.getUser().getId());
      String name = vo.getName();
      if (name == null) {
        name = "";
      }
      args.add("%" + name + "%");
      if (id != null && !"".equals(id)) {
        args.add(id);
      }

      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
      log.debug("sql====" + sql.toString());
      log.debug("args====" + args);
    } catch (Exception e) {
      log.error(e);
      log.error("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
    }
    log.debug("---------------------queryInstanceInfoByUserId(ResourcesQueryVO vo)              end!---------------");
    return returnList;
  }

  /**
   * 获取当前用户nics信息
   */
  @Override
  public List<TNicsBO> vmVlanInfos(final TUserBO user) throws SCSException {
    String sql = "SELECT r.* FROM T_SCS_INSTANCE_INFO info LEFT JOIN T_SCS_NICS r ON info.ID=r.VM_INSTANCE_INFO_ID LEFT JOIN T_SCS_ORDER o ON o.ORDER_ID=info.ORDER_ID  WHERE o.CREATOR_USER_ID=? and r.ID is not NULL;";
    BeanPropertyRowMapper<TNicsBO> vlanRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
    List<TNicsBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, user.getId());
        }
      }, vlanRowMapper);
    } catch (Exception e) {
      log.error("vmVlanInfos(TUserBO user) error:" + e.getMessage());
      throw new SCSException("query vlaninfos error：" + e.getMessage());
    }
    log.debug("---------------------vmVlanInfos(TUserBO user)              end!---------------");
    return returnList;
  }

  @Override
  public int searchLastId() throws SQLException {
    int ret_Id = -1;
    try {
      String sql = "SELECT ID FROM T_SCS_INSTANCE_INFO order by ID desc LIMIT 0,1 ;";
      ret_Id = this.getJdbcTemplate().queryForInt(sql);
    } catch (Exception e) {
      log.error("searchLastId() error:" + e.getMessage());
      throw new SQLException("query T_SCS_INSTANCE_INFO error：" + e.getMessage());
    }
    return ret_Id;
  }

  @Override
  public void updateTScsIntanceInfoStateById(int id, int sate) throws DataAccessException {
    // TODO Auto-generated method stub
    log.debug("---------------------updateTScsIntanceInfoStateById(int id, int sate)             begin!---------------");
    log.debug("id=" + id + ",sate=" + sate);
    String sql = "UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = ? WHERE ID = ?";
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), sate, id };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER, Types.INTEGER };
    getJdbcTemplate().update(sql, args, argTypes);
    log.debug("---------------------updateTScsIntanceInfoStateById(int id, int sate)             end!---------------");
  }

  @Override
  public void rollbackIntanceInfoStateById(int id) throws DataAccessException {
    // TODO Auto-generated method stub
    log.debug("---------------------rollbackIntanceInfoStateById(int id, int sate)             begin!---------------");
    log.debug("id=" + id);
    String sql = "UPDATE T_SCS_INSTANCE_INFO i SET LASTUPDATE_DT = ?,STATE = " + "  case when i.CLUSTER_ID >=1  then  i.CLUSTER_ID  "
        + "  when i.CLUSTER_ID <=0  then  2  end " + "  WHERE ID = ?";
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), id };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER };
    int re_val = getJdbcTemplate().update(sql, args, argTypes);
    log.debug("---------------------updateTScsIntanceInfoStateById(int id, int sate)             end!---------------");
  }

  @Override
  public void updateTScsIntanceInfoStateResIdById(int id, int sate, int resId) throws DataAccessException {
    // TODO Auto-generated method stub
    log.debug("---------------------updateTScsIntanceInfoStateResIdById(int id, int sate, int resId)            begin!---------------");
    log.debug("id=" + id + ",sate=" + sate + ",resId=" + resId);
    String sql = "UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = ? ,E_INSTANCE_ID = ? WHERE ID = ?";
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), sate, resId, id };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.INTEGER };
    getJdbcTemplate().update(sql, args, argTypes);
    log.debug("---------------------updateTScsIntanceInfoStateResIdById(int id, int sate, int resId)            end!---------------");
  }

  @Override
  public void updateTScsInstanceInfoStateAndResourceInfoById(final int id, final int state, final String resourceInfo) throws SCSException {
    log.debug("---------------------updateTScsInstanceInfoStateAndResourceInfoById(final int id, final int state, final String resourceInfo)            begin!---------------");
    log.debug("id=" + id + ",state=" + state + ",resourceInfo=" + resourceInfo);
    String sql = "UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = ?,RESOURCE_INFO = ? WHERE ID = ?";
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), state, resourceInfo, id };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER, Types.VARCHAR, Types.INTEGER };
    getJdbcTemplate().update(sql, args, argTypes);
    log.debug("---------------------updateTScsInstanceInfoStateAndResourceInfoById(final int id, final int state, final String resourceInfo)            end!---------------");
  }

  @Override
  public int deleteByOrderId(final int orderId) throws SQLException {
    log.debug("---------------------deleteByOrderId(final int orderId)            begin!---------------");
    log.debug("orderId=" + orderId);
    int ret_val = -1;
    String sql = "DELETE FROM T_SCS_INSTANCE_INFO WHERE ORDER_ID = ?;";
    try {
      ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, orderId);
        }
      });
    } catch (Exception e) {
      log.error("deleteByOrderId(final int orderId)     error:" + e.getMessage());
      throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_DELETE, orderId, e.getMessage()));
    }
    log.debug("---------------------deleteByOrderId(final int orderId)            end!---------------");
    return ret_val;
  }

  @Override
  public int searchInstanceInfoByInstanceNameAndUserId(final int createUserId, final String instanceName) throws SCSException {
    log.debug("---------------------searchInstanceInfoByInstanceNameAndUserId( int createUserId,  String instanceName)            begin!---------------");
    log.debug("createUserId=" + createUserId + ",instanceName=" + instanceName);
    // 4退订；7创建失败 fix bug 3789
    String sql = "SELECT info.* FROM T_SCS_INSTANCE_INFO info LEFT JOIN T_SCS_ORDER o ON o.ORDER_ID=info.ORDER_ID  WHERE o.CREATOR_USER_ID=? and info.INSTANCE_NAME=? AND (info.STATE <>4 AND info.STATE <>7);";
    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, createUserId);
          ps.setString(2, instanceName);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("searchInstanceInfoByInstanceNameAndUserId  error:" + e.getMessage());
      throw new SCSException("query T_SCS_INSTANCE_INFO,T_SCS_ORDER error：" + e.getMessage());
    }
    log.debug("---------------------searchInstanceInfoByInstanceNameAndUserId(int createUserId, String instanceName)            begin!---------------");
    return returnList.size();
  }

  @Override
  public List<TInstanceInfoBO> searchEBSInstanceInfosByVMInstanceId(final int vmInstanceId) throws SCSException {
    log.debug("---------------------searchEBSInstanceInfosByVMInstanceId( int vmInstanceId)   begin!------------------");
    log.debug("vmInstanceId=" + vmInstanceId);
    String sql = "SELECT info.* FROM T_SCS_INSTANCE_INFO info LEFT JOIN T_SCS_IRI iri ON info.ID=iri.DISK_INSTANCE_INFO_ID  WHERE iri.VM_INSTANCE_INFO_ID=? and iri.STATE=1;";
    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, vmInstanceId);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("searchEBSInstanceInfosByVMInstanceId  error:" + e.getMessage());
      throw new SCSException("query T_SCS_INSTANCE_INFO,T_SCS_IRI error：" + e.getMessage());
    }
    log.debug("---------------------searchEBSInstanceInfosByVMInstanceId( int vmInstanceId)   end!------------------");
    return returnList;
  }

  @Override
  public void recoveryIntanceInfoStateById(int instanceInfoId) throws DataAccessException {
    String sql = "UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = CLUSTER_ID WHERE ID = ?";
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), instanceInfoId };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER };
    getJdbcTemplate().update(sql, args, argTypes);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.front.order.dao.IInstanceInfoDao#
   * updateVMState4Elaster(java.lang.String)
   */
  @Override
  public int updateVMState4Elaster(int instanceID) throws SCSException {
    log.debug("------------------------updateVMState4Elaster(int instanceID)   start!-------------------");
    log.debug("instanceID=" + instanceID);
    String sql = "UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = '4' WHERE ID = ?";
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), instanceID };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER };
    int index = getJdbcTemplate().update(sql, args, argTypes);
    log.debug("------------------------updateVMState4Elaster(int instanceID)   end!-------------------");
    return index;
  }

  // @Override
  // public int searchInstanceCountByTemplateId(final int templateId,final int
  // type) throws SCSException {
  // int count = 0;
  // try {
  // String sql =
  // "SELECT count(ID) FROM T_SCS_PRODUCT WHERE TEMPLATE_ID = ? and TYPE = ?;";
  // Object[] args = new Object[] { templateId, type };
  // count = this.getJdbcTemplate().queryForInt(sql,args);
  // } catch (Exception e) {
  // throw new SCSException("query T_SCS_INSTANCE_INFO error：" +
  // e.getMessage());
  // }
  // return count;
  // }

  @Override
  public int searchInstanceCountByProductId(int productId) throws SCSException {
    log.debug("------------------------searchInstanceCountByProductId(int productId)   start!-------------------");
    log.debug("productId=" + productId);
    int count = 0;
    try {
      // to fix bug 2163 ：所有基于服务生成的实例均退订，并且服务下线之后，删除服务，提示产品被使用，删除失败
      // 解决：增加退订实例过滤STATE<>4 and
      String sql = "SELECT count(ID) FROM T_SCS_INSTANCE_INFO WHERE STATE<>4 and PRODUCT_ID = ? ;";
      Object[] args = new Object[] { productId };
      count = this.getJdbcTemplate().queryForInt(sql, args);
    } catch (Exception e) {
      log.error("searchInstanceCountByProductId error:" + e.getMessage());
      throw new SCSException("query T_SCS_INSTANCE_INFO error：" + e.getMessage());
    }
    log.debug("------------------------searchInstanceCountByProductId(int productId)   end!-------------------");
    return count;
  }

  @Override
  public List<TInstanceInfoBO> findUsableIpInstance(final int userId) throws SQLException {
    log.debug("------------------------findUsableIpInstance(int userId)   begin!-------------------");
    log.debug("userId=" + userId);
    String sql = "select info.ID, info.RESOURCE_INFO from T_SCS_INSTANCE_INFO info INNER JOIN T_SCS_ORDER o ON info.ORDER_ID=o.ORDER_ID WHERE info.TEMPLATE_TYPE=9 AND info.STATE=2 and  o.CREATOR_USER_ID=?";
    BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, userId);
        }
      }, infoRowMapper);
    } catch (Exception e) {
      log.error("findUsableIpInstance error:" + e.getMessage());
      throw new SQLException("查询ip实例信息失败。失败原因：" + e.getMessage());
    }
    log.debug("------------------------findUsableIpInstance(int userId)   end!-------------------");
    return returnList;
  }

  @Override
  /**
   * 1.2新UI用到的查询
   * 1.3支持查询同一资源池和同一资源域下的可用的公网ip
   */
  public List<TInstanceInfoBO> findUsableIpInstance2(final int userId, final int resourcePoolsId, final int zoneId) throws SQLException {
    log.debug("------------------------findUsableIpInstance(int userId)   begin!-------------------");
    log.debug("userId=" + userId);
    StringBuilder sbf = new StringBuilder();
    sbf.append(" select info.ID, info.RESOURCE_INFO ");
    sbf.append(" from T_SCS_INSTANCE_INFO info ");
    sbf.append(" INNER JOIN T_SCS_ORDER o ");
    sbf.append(" ON info.ORDER_ID=o.ORDER_ID ");
    sbf.append(" INNER JOIN T_SCS_PUBLIC_IP ip ");
    sbf.append(" ON info.E_NETWORK_ID=ip.ID ");
    // 1.3功能，支持多资源池
    // 关联模板表查询
    // sbf.append(" INNER JOIN T_SCS_TEMPLATE_VM t ");
    // sbf.append(" ON info.TEMPLATE_ID=t.ID ");
    // 1.3功能，支持多资源池

    sbf.append(" WHERE info.TEMPLATE_TYPE=9 AND info.STATE=2 ");
    sbf.append(" AND ip.`STATUS`=1 ");
    // sbf.append(
    // " and (ip.INSTANCE_INFO_ID>0 or ip.INSTANCE_INFO_ID=-1)");
    sbf.append(" and (ip.BANDWIDTH_ID=0 or ip.BANDWIDTH_ID is NULL) ");
    sbf.append(" and  o.CREATOR_USER_ID=? ");

    // 1.3功能，支持多资源池
    // sbf.append(" and  t.RESOURCE_POOLS_ID=? ");
    // sbf.append(" and  t.ZONE_ID=? ");
    // 1.3功能，支持多资源池

    // String sql =
    // "select info.ID, info.RESOURCE_INFO from T_SCS_INSTANCE_INFO info INNER JOIN T_SCS_ORDER o ON info.ORDER_ID=o.ORDER_ID WHERE info.TEMPLATE_TYPE=9 AND info.STATE=2 and  o.CREATOR_USER_ID=?";
    BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sbf.toString(), new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, userId);

          // 1.3功能，支持多资源池
          // ps.setInt(2, resourcePoolsId);
          // ps.setInt(3, zoneId);
          // 1.3功能，支持多资源池
        }
      }, infoRowMapper);
    } catch (Exception e) {
      log.error("findUsableIpInstance error:" + e.getMessage());
      throw new SQLException("查询ip实例信息失败。失败原因：" + e.getMessage());
    }
    log.debug("------------------------findUsableIpInstance(int userId)   end!-------------------");
    return returnList;
  }

  @Override
  public int findIPAddressExist(String ipAddress, int templateType, int userId) throws SQLException {
    log.debug("------------------------findIPAddressExist(String ipAddress, int templateType, int userId)   begin!-------------------");
    log.debug("ipAddress=" + ipAddress + ",  templateType=" + templateType + ", userId=" + userId);
    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM T_SCS_INSTANCE_INFO info INNER JOIN T_SCS_ORDER o ON info.ORDER_ID=o.ORDER_ID WHERE o.CREATOR_USER_ID=? AND info.TEMPLATE_TYPE=? AND info.STATE <>4 AND info.RESOURCE_INFO LIKE ?";
      Object[] args = new Object[] { userId, templateType, "%ipAddress\":\"" + ipAddress + "%" };
      count = this.getJdbcTemplate().queryForInt(sql, args);
    } catch (Exception e) {
      log.debug("findIPAddressExist error:" + e.getMessage());
      throw new SQLException("query T_SCS_INSTANCE_INFO error：" + e.getMessage());
    }
    log.debug("------------------------findIPAddressExist(String ipAddress, int templateType, int userId)   end!-------------------");
    return count;
  }

  @Override
  public int findIPAddressExist(String ipAddress, int templateType) throws SQLException {
    log.debug("------------------------findIPAddressExist(String ipAddress, int templateType)   begin!-------------------");
    log.debug("ipAddress=" + ipAddress + ",  templateType=" + templateType);

    int count = 0;
    try {
      String sql = "SELECT COUNT(*) FROM T_SCS_INSTANCE_INFO info  WHERE info.TEMPLATE_TYPE=? AND info.STATE <>4 AND info.RESOURCE_INFO LIKE ?";
      Object[] args = new Object[] { templateType, "%ipAddress\":\"" + ipAddress + "\"%" };
      count = this.getJdbcTemplate().queryForInt(sql, args);
    } catch (Exception e) {
      log.debug("findIPAddressExist error:" + e.getMessage());
      throw new SQLException("query T_SCS_INSTANCE_INFO error：" + e.getMessage());
    }
    log.debug("------------------------findIPAddressExist(String ipAddress, int templateType)   end!-------------------");
    return count;
  }

  @Override
  public TInstanceInfoBO findInstanceInfoById(final int instanceId) throws SQLException {
    log.debug("------------------------findInstanceInfoById(int instanceId)   start!-------------------");
    log.debug("instanceId=" + instanceId);
    String sql = "select ID,TEMPLATE_TYPE,TEMPLATE_ID,ORDER_ID,STATE,LASTUPDATE_DT,COMMENT,RESOURCE_INFO,CPU_NUM,"
        + "MEMORY_SIZE,STORAGE_SIZE,ZONE_ID,CLUSTER_ID,CREATE_DT,STATE,E_INSTANCE_ID,E_SERVICE_ID,PRODUCT_ID,INSTANCE_NAME,OS_DESC, RESOURCE_INFO from T_SCS_INSTANCE_INFO WHERE ID=?  ";
    BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, instanceId);
        }
      }, infoRowMapper);
    } catch (Exception e) {
      log.error("findInstanceInfoById error:" + e.getMessage());
      throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
    }
    log.debug("------------------------findInstanceInfoById(int instanceId)   end!-------------------");
    if (returnList != null && returnList.size() > 0) {
      return returnList.get(0);
    }
    return null;
  }

  @Override
  public int updateTScsIntanceInfoStateandRescodeById(int insid, int state, String rescode) throws DataAccessException {
    log.debug("------------------------updateTScsIntanceInfoStateandRescodeById(int insid, int state, String rescode)   start!-------------------");
    int index = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = ? ");
    List<Object> args = new ArrayList<Object>();
    args.add(new Timestamp(System.currentTimeMillis()));
    args.add(state);
    if (rescode != null && !"".equals(rescode)) {
      sql.append(",RES_CODE = ? ");
      args.add(rescode);
    }
    args.add(insid);
    sql.append(" WHERE ID = ?");
    // Object[] args = new Object[] {new
    // Timestamp(System.currentTimeMillis()) ,
    // state, rescode, insid };
    // int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER,
    // Types.VARCHAR, Types.INTEGER };
    index = getJdbcTemplate().update(sql.toString(), args.toArray());
    log.debug("@@@@@@@@@@@@@@@sql=" + sql + "@@@@@@@@insid=" + insid + "@@@@@@@@state=" + state + "@@@@@@@@rescode=" + rescode
        + "@@@@@@@@index=" + index);
    log.debug("------------------------updateTScsIntanceInfoStateandRescodeById(int insid, int state, String rescode)   end!-------------------");
    return index;
  }

  @Override
  public void updateTScsInstanceInfo(TInstanceInfoBO infoBO) throws SCSException {
    log.debug("------------------------updateTScsInstanceInfo(TInstanceInfoBO infoBO)    begin!-------------------");
    StringBuffer sql = new StringBuffer(
        "UPDATE T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID  SET i.LASTUPDATE_DT = ?,i.STATE = ?,i.RES_CODE = ? ");
    Object[] args = new Object[] { new Timestamp(System.currentTimeMillis()), infoBO.getState(), infoBO.getReason(), infoBO.getId() };
    int[] argTypes = new int[] { Types.TIMESTAMP, Types.INTEGER, Types.VARCHAR, Types.INTEGER };
    if (infoBO.getTemplateType() == 8 && infoBO.getState() != 4) {
      sql.append(" i.STORAGE_SIZE = r.STORAGE_SIZE");
    } else if (infoBO.getTemplateType() == 15 && infoBO.getState() != 4) {
      sql.append(" i.RESOURCE_INFO = r.RESOURCE_INFO");
    }
    sql.append(" WHERE i.ID = ?");

    getJdbcTemplate().update(sql.toString(), args, argTypes);
    log.debug("------------------------updateTScsInstanceInfo(TInstanceInfoBO infoBO)    end!-------------------");
  }

  @Override
  public List<String> queryLoadBalanceWithVirtualServer() {
    log.debug("------------------------queryLoadBalanceWithVirtualServer()    begin!-------------------");
    String sql = " select OS_DESC from T_SCS_INSTANCE_INFO where TEMPLATE_TYPE='6' and OS_DESC <> null ";
    List<Map<String, Object>> List = getJdbcTemplate().queryForList(sql);
    List<String> returnList = new ArrayList<String>();
    for (Map<String, Object> map : List) {
      if (map.get("OS_DESC") != null) {
        returnList.add((String) map.get("OS_DESC"));
      }
    }
    log.debug("------------------------queryLoadBalanceWithVirtualServer()    end!-------------------");
    return returnList;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.front.order.dao.IInstanceInfoDao#
   * updateTScsIntanceInfoByVMModify
   * (com.skycloud.management.portal.front.order.entity.TInstanceInfoBO)
   */
  @Override
  public int updateTScsIntanceInfoByVMModify(TInstanceInfoBO infoBO) throws SCSException {
    log.debug("------------------------updateTScsIntanceInfoByVMModify(TInstanceInfoBO infoBO)   start!-------------------");
    int index = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = ?,CPU_NUM=?,MEMORY_SIZE=? ");
    sql.append(" WHERE ID = ?");
    List<Object> args = new ArrayList<Object>();
    args.add(new Timestamp(System.currentTimeMillis()));
    args.add(infoBO.getState());
    args.add(infoBO.getCpuNum());
    args.add(infoBO.getMemorySize());
    args.add(infoBO.getId());

    index = getJdbcTemplate().update(sql.toString(), args.toArray());
    log.debug("sql=" + sql + ",State=" + infoBO.getState() + ",CpuNum=" + infoBO.getCpuNum() + ",MemorySize=" + infoBO.getMemorySize()
        + ",Id=" + infoBO.getId() + ",index=" + index);
    log.debug("------------------------updateTScsIntanceInfoByVMModify(TInstanceInfoBO infoBO)   end!-------------------");
    return index;
  }

  @Override
  public List<TInstanceInfoBO> queryInstanceInfoByOrderId(final int orderId) throws SCSException {
    // String sql =
    // "SELECT info.*,t.RESOURCE_POOLS_ID FROM T_SCS_INSTANCE_INFO info "
    // +" WHERE info.ORDER_ID=? ;";
    // 获取资源池 t.RESOURCE_POOLS_ID，方便调用Elaster API 接口
    String sql = "SELECT * FROM ( " + " SELECT t.RESOURCE_POOLS_ID,info.* FROM T_SCS_INSTANCE_INFO info  "
        + "  left join T_SCS_TEMPLATE_MC t on  info.TEMPLATE_ID = t.ID  " + "  WHERE info.TEMPLATE_TYPE = 3  " + " UNION "
        + " SELECT t.RESOURCE_POOLS_ID,info.* FROM T_SCS_INSTANCE_INFO info  "
        + "  left join T_SCS_TEMPLATE_VM t on  info.TEMPLATE_ID = t.ID  " + "   WHERE info.TEMPLATE_TYPE <> 3) InsT "
        + " WHERE  InsT.ORDER_ID=? ;";

    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, orderId);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("searchEBSInstanceInfosByVMInstanceId  error:" + e.getMessage());
      throw new SCSException("queryInstanceInfoByOrderId() error：" + e.getMessage());
    }
    return returnList;
  }

  @Override
  public List<TInstanceInfoBO> searchInstanceInfoByServiceInstanceId(final int serviceInstanceId) throws SCSException {
    String sql = "SELECT info.*,r.SERVICE_INSTANCE_ID FROM T_SCS_INSTANCE_INFO info, T_SCS_PRODUCT_INSTANCE_REF r "
        + "WHERE info.ID=r.INSTANCE_INFO_ID and   r.SERVICE_INSTANCE_ID=? ;";
    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, serviceInstanceId);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("searchEBSInstanceInfosByVMInstanceId  error:" + e.getMessage());
      throw new SCSException("queryInstanceInfoByOrderId() error：" + e.getMessage());
    }
    return returnList;
  }

  @Override
  public int[] updateVMStateMdf(List<TInstanceInfoBO> listMdfState) throws SCSException {
    int[] index;
    final List<TInstanceInfoBO> infos = listMdfState;
    String sql = "UPDATE T_SCS_INSTANCE_INFO SET LASTUPDATE_DT = ?,STATE = ? WHERE ID = ? ";
    BatchPreparedStatementSetter pss = new BatchPreparedStatementSetter() {

      @Override
      public int getBatchSize() {
        return infos.size();
      }

      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        TInstanceInfoBO instanceinfo = infos.get(i);
        ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        log.info("async job update instance state ,state is " + instanceinfo.getState() + ",instance id is " + instanceinfo.getId());
        ps.setInt(2, instanceinfo.getState());
        ps.setInt(3, instanceinfo.getId());
      }
    };
    try {
      index = this.getJdbcTemplate().batchUpdate(sql, pss);
    } catch (Exception e) {
      log.error(e);
      throw new SCSException("updateVMStateMdf() error：" + e.getMessage());
    }
    return index;
  }

  @Override
  public List<TInstanceInfoBO> searchInstanceInfoByUserId(final int createUserId, final ServiceType serviceType,
      List<InstanceState> instanceStates, final int resourceDomain) throws SCSException {
    log.debug("---------------------searchInstanceInfoByUserId( int createUserId,  ServiceType serviceType) begin!---------------");
    log.debug("createUserId=" + createUserId + ",serviceType=" + serviceType.getValue());
    // 4退订；7创建失败 fix bug 3789
    String sql = "SELECT " + "info.* " + "FROM " + "T_SCS_INSTANCE_INFO info," + "T_SCS_ORDER o " + "WHERE "
        + "info.ORDER_ID = o.ORDER_ID " +
        // 不包括作废的订单
        "AND (o.STATE <> 6) " + "AND o.CREATOR_USER_ID = ? " + "AND info.TEMPLATE_TYPE = ? " + "AND info.ZONE_ID = ? ";
    if (instanceStates != null && !instanceStates.isEmpty()) {
      sql = sql + "AND info.STATE IN (";
      for (InstanceState instanceState : instanceStates) {
        sql = sql + instanceState.getValue() + ",";
      }
      sql = sql.substring(0, sql.lastIndexOf(",")) + ") ";
    }
    sql = sql + "ORDER BY info.ID";
    log.info("Search instance info by user[" + createUserId + "]: " + sql);
    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, createUserId);
          ps.setInt(2, serviceType.getValue());
          ps.setInt(3, resourceDomain);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("Search instance info by user[" + createUserId + "] error: " + e.getMessage());
      throw new SCSException("Search instance info by user[" + createUserId + "] error: " + e.getMessage());
    }
    log.debug("---------------------searchInstanceInfoByUserId(int createUserId, ServiceType serviceType) end!---------------");
    return returnList;
  }

  @Override
  public List<TInstanceInfoBO> searchInstanceAppliedByUserId(final int createUserId, final ServiceType serviceType,
      List<InstanceState> instanceStates, final int resourceDomain) throws SCSException {
    log.debug("---------------------searchInstanceInfoByUserId( int createUserId,  ServiceType serviceType) begin!---------------");
    log.debug("createUserId=" + createUserId + ",serviceType=" + serviceType.getValue());
    String sql = "SELECT " + "info.* " + "FROM " + "T_SCS_INSTANCE_INFO info," + "T_SCS_ORDER o " + "WHERE "
        + "info.ORDER_ID = o.ORDER_ID " + "AND o.STATE = 4 " + "AND o.CREATOR_USER_ID = ? " + "AND info.TEMPLATE_TYPE = ? "
        + "AND info.ZONE_ID = ? ";
    if (instanceStates != null && !instanceStates.isEmpty()) {
      sql = sql + "AND info.STATE IN (";
      for (InstanceState instanceState : instanceStates) {
        sql = sql + instanceState.getValue() + ",";
      }
      sql = sql.substring(0, sql.lastIndexOf(",")) + ") ";
    }
    sql = sql + "ORDER BY info.ID";
    log.info("Search instance info by user[" + createUserId + "]: " + sql);
    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, createUserId);
          ps.setInt(2, serviceType.getValue());
          ps.setInt(3, resourceDomain);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("Search instance info by user[" + createUserId + "] error: " + e.getMessage());
      throw new SCSException("Search instance info by user[" + createUserId + "] error: " + e.getMessage());
    }
    log.debug("---------------------searchInstanceInfoByUserId(int createUserId, ServiceType serviceType) end!---------------");
    return returnList;
  }

  @Override
  public List<TInstanceInfoBO> searchInstanceAppliedByNetworkId(final long networkId, final int resourceDomain) throws SCSException {
    log.debug("---------------------searchInstanceAppliedByNetworkId(int networkId,  int resourceDomain) begin!---------------");
    log.debug("networkId=" + networkId);
    String sql = "SELECT " + "info.* " + "FROM " + "T_SCS_INSTANCE_INFO info," + "T_SCS_NICS n " + "WHERE "
        + "info.ID = n.VM_INSTANCE_INFO_ID " + "AND info.STATE NOT IN (4, 7) " + "AND info.ZONE_ID = ? " + "AND n.E_VLAN_ID = ?";
    log.info("Search instance info by networkId " + networkId + ": " + sql);
    BeanPropertyRowMapper<TInstanceInfoBO> instanceRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
    List<TInstanceInfoBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
          ps.setInt(1, resourceDomain);
          ps.setLong(2, networkId);
        }
      }, instanceRowMapper);
    } catch (Exception e) {
      log.error("Search instance info by networkId " + networkId + " error: " + e.getMessage());
      throw new SCSException("Search instance info by networkId " + networkId + " error: " + e.getMessage());
    }
    log.debug("---------------------searchInstanceAppliedByNetworkId(int networkId,  int resourceDomain) end!---------------");
    return returnList;
  }

  /**
   * 根据资源实例id(例如虚机id)查询其所属的资源池id
   * 
   * @param instanceId
   * @return
   * @throws Exception
   */
  @Override
  public int getResourcePoolIdByInstanceId(int instanceId) throws Exception {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT t.RESOURCE_POOLS_ID FROM T_SCS_TEMPLATE_VM t,T_SCS_INSTANCE_INFO i ");
    sql.append(" where i.TEMPLATE_ID=t.ID and i.TEMPLATE_TYPE=t.TYPE ");
    sql.append(" and i.ID=? ");
    return jdbcTemplate.queryForObject(sql.toString(), new Object[] { instanceId }, new int[] { Types.INTEGER }, Integer.class);
  }

  @Override
  public List<ResourcesVO> queryInstanceInfoExist(ResourcesQueryVO vo, int resourcePoolsId, int zoneId) throws SCSException {
    StringBuffer sql = new StringBuffer(
        " SELECT i.ID,i.INSTANCE_NAME,i.COMMENT,i.STATE,i.E_INSTANCE_ID,i.PRODUCT_ID, t.resource_pools_id,t.zone_id ");
    String Idwhere = "";
    String resourcePoolsIdwhere = "";
    String zoneIdwhere = "";
    String id = vo.getId();
    if (id != null && !"".equals(id)) {
      sql.append(" ,t.TEMPLATE_DESC,o.REASON,i.CLUSTER_ID,i.TEMPLATE_TYPE,t.TEMPLATE_DESC,tsrp.pool_name,t.CPUFREQUENCY,i.CPU_NUM,i.MEMORY_SIZE,i.OS_DESC,t.id templateId,i.STATE,t.network_desc , i.RES_CODE  ");
      Idwhere = " and i.ID=?";
    }
    if (resourcePoolsId > 0) {
      resourcePoolsIdwhere = " and t.resource_pools_id=" + resourcePoolsId;
    }
    if (zoneId > 0) {
      zoneIdwhere = " and t.zone_id=" + zoneId;
    }
    sql.append(" FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
    sql.append(" left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID  ");
    sql.append(" join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
    sql.append(" where o.CREATOR_USER_ID=? and i.STATE in (2,3,6) and  i.TEMPLATE_TYPE in (1,3,10) and t.TYPE in (1,3,10) ");

    sql.append(Idwhere);
    sql.append(resourcePoolsIdwhere);
    sql.append(zoneIdwhere);

    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
    List<ResourcesVO> returnList = null;
    List<Object> args = new ArrayList<Object>();
    try {
      log.debug("---------------------queryInstanceInfoExist()              begin!---------------");

      if (id != null && !"".equals(id)) {
        args.add(id);
      }
      args.add(vo.getUser().getId());
      log.debug("sql====" + sql.toString());
      log.debug("args====" + args);
      returnList = this.getJdbcTemplate().query(sql.toString(), args.toArray(), infoRowMapper);
    } catch (Exception e) {
      log.error(e);
      log.error("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC);
    }
    log.debug("---------------------queryInstanceInfoExist()              end!---------------");
    return returnList;
  }

  @Override
  public List<ResourcesVO> queryInstanceInfo4Bind(ResourcesQueryVO rqvo, int resourcePoolsId, int zoneId) throws SCSException {
    StringBuffer sql = new StringBuffer();
    // 通过type区分,服务类型
    int type = rqvo.getOperateSqlType();

    if (type == 3) {
      sql.append("select i.ID,i.RES_CODE,i.TEMPLATE_ID,i.TEMPLATE_TYPE,i.INSTANCE_NAME,i.COMMENT,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.STATE,i.E_INSTANCE_ID ,i.RESOURCE_INFO,i.CREATE_DT,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.CPUFREQUENCY, ");
      sql.append("o.REASON ,tsrp.pool_name,i.PRODUCT_ID, t.resource_pools_id, t.zone_id FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ");
      sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
      sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
      sql.append("where o.CREATOR_USER_ID=?  and  i.STATE in (2,5,6) and t.state=2 and  i.TEMPLATE_TYPE=3 and t.TYPE=?  ");
      sql.append("and i.INSTANCE_NAME like ? ");
    } else {
      sql.append("select i.ID,i.RES_CODE,i.TEMPLATE_ID,i.TEMPLATE_TYPE,i.INSTANCE_NAME,i.COMMENT,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.STATE,i.E_INSTANCE_ID ,i.RESOURCE_INFO,i.CREATE_DT,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.CPUFREQUENCY, ");
      sql.append("o.REASON ,tsrp.pool_name,t.network_desc,t.EXTEND_ATTR_JSON,i.PRODUCT_ID, t.resource_pools_id, t.zone_id FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
      sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
      sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
      sql.append("where o.CREATOR_USER_ID=?  and  i.STATE in (2,5,6) and t.state=2 and  i.TEMPLATE_TYPE<>'3' and t.TYPE=? ");
      sql.append("and i.INSTANCE_NAME like ? ");
    }
    if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
      sql.append(" and i.ID not in (select iri.VM_INSTANCE_INFO_ID from T_SCS_IRI iri where iri.STATE = 2 AND iri.DISK_INSTANCE_INFO_ID = ? ) ");
    }
    if (resourcePoolsId != 0) {
      sql.append(" and t.resource_pools_id = ? ");
    }
    if (zoneId != 0) {
      sql.append("  and t.zone_id= ? ");
    }

    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
    List<ResourcesVO> returnList = null;
    List<Object> args = new ArrayList<Object>();
    try {
      args.add(rqvo.getUser().getId());// 用户ID
      args.add(type);// 模板类型
      String name = rqvo.getName();
      if (name == null) {
        name = "";
      }
      args.add("%" + name + "%");// 实例名称

      if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
        args.add(rqvo.getId());// 实例ID
      }
      if (resourcePoolsId != 0) {
        args.add(resourcePoolsId);// 资源池
      }
      if (zoneId != 0) {
        args.add(zoneId);// 资源域
      }

      PageVO page = rqvo.getPage();
      if (page != null) {
        int curPage = page.getCurPage();
        int pageSize = page.getPageSize();
        if (curPage > 0 && pageSize > 0) {
          sql.append(" limit ?, ?");
          args.add((curPage - 1) * pageSize);
          args.add(pageSize);
        }
      }
      returnList = this.jdbcTemplate.query(sql.toString(), args.toArray(), infoRowMapper);
      // System.out.println("sqltest = "+sql);
      log.info("sql====" + sql.toString());
      log.info("args====" + args);
    } catch (Exception e) {
      log.error(e);
      log.error("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(e.getMessage());
    }
    return returnList;
  }

  @Override
  public int queryResourcePoolIDByIRIID(int iriID) throws Exception {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT t.RESOURCE_POOLS_ID FROM T_SCS_TEMPLATE_VM t,T_SCS_INSTANCE_INFO i,T_SCS_IRI iri ");
    sql.append("where i.TEMPLATE_ID=t.ID and i.TEMPLATE_TYPE=t.TYPE AND iri.VM_INSTANCE_INFO_ID = i.ID and iri.ID=?");
    return jdbcTemplate.queryForObject(sql.toString(), new Object[] { iriID }, new int[] { Types.INTEGER }, Integer.class);
  }

@Override
public List<ResourcesVO> searchInstanceInfoByIds(final List<Integer> insIds) throws SCSException {
    if (insIds==null || insIds.size()==0){
    	return null;
    }
    //分解实例ID参数
	final StringBuffer ids = new StringBuffer();
	for(int id:insIds){
		ids.append(id).append(",");
	}
	final String idStr = ids.substring(0, ids.length()-1);
	
	String sql = "SELECT i.ID, i.ORDER_ID, i.TEMPLATE_TYPE, i.TEMPLATE_ID, i.COMMENT, i.RESOURCE_INFO, i.CPU_NUM, i.MEMORY_SIZE, i.STORAGE_SIZE, i.ZONE_ID, "
	        + " i.CLUSTER_ID, i.CREATE_DT, i.STATE, i.LASTUPDATE_DT, i.E_INSTANCE_ID, i.E_SERVICE_ID, i.E_DISK_ID, i.E_NETWORK_ID, i.E_OS_ID, "
	        + " i.OS_DESC, i.INSTANCE_NAME, i.PRODUCT_ID, i.RES_CODE, o.CREATOR_USER_ID as userid ,i.RESOURCE_POOLS_ID, i.E_HOST_ID "
	        + " FROM ("
	        + sqlInstanceTemplate() + ") i  left join T_SCS_ORDER o on i.ORDER_ID = o.ORDER_ID " +
	        " WHERE i.ID in ("+idStr+")";

	    BeanPropertyRowMapper<ResourcesVO> instanceinfoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
	    List<ResourcesVO> returnList = null;
	    try {
	    	returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<ResourcesVO>(instanceinfoRowMapper));
	    } catch (Exception e) {
	      e.printStackTrace();
	      log.error("searchInstanceInfoByIds(int ID)  error: " + e.getMessage());
	      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_QUERY));
	    }
	    return returnList;
	  }
  
  

}
