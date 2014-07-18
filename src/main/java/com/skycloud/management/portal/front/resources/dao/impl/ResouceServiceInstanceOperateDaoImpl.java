package com.skycloud.management.portal.front.resources.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;

public class ResouceServiceInstanceOperateDaoImpl implements ResouceServiceInstanceOperateDao {

	private static Log log = LogFactory.getLog(ResouceServiceInstanceOperateDaoImpl.class);

	private JdbcTemplate jt;

	@Override
	public int queryResouceServiceInstanceInfoCount(ResourcesQueryVO rqvo) throws SCSException {

		StringBuffer sql = new StringBuffer();
		String name = rqvo.getName();
		// 通过type区分,服务类型
		int type = rqvo.getOperateSqlType();
		String monitorType = rqvo.getMonitorType();
		int index = 0;
		// Object[] args = new Object[3];
		List<Object> args = new ArrayList<Object>();
		if (name == null) {
			name = "";
		}
		sql.append("select count(0) FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
		sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
		sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
		sql.append(" where o.CREATOR_USER_ID=?  and  i.STATE not in (4,7) and  i.TEMPLATE_TYPE<>'2' and t.TYPE='" + type + "' ");
		sql.append(" and i.INSTANCE_NAME like ? ");

		args.add(rqvo.getUser().getId());
		args.add("%" + name + "%");
		if (monitorType == null) {
			monitorType = "";
		} else {
			sql.append(" and t.network_desc like ? ");
			args.add("%" + monitorType + "%");
		}

		try {

			index = jt.queryForInt(sql.toString(), args.toArray());
			log.info("sql====" + sql.toString());
			// System.out.println("sql.toString() = " + sql.toString());
		}
		catch (Exception e) {
			log.error(e);
			log.info("sql====" + sql.toString());
			log.error("args====" + args);
			throw new SCSException("queryBackUpInstance error: " + e.getMessage());
		}

		return index;
	}

	@Override
	public int queryResouceServiceInstanceInfoCountBeforApprove(ResourcesQueryVO rqvo) throws SCSException {

		StringBuffer sql = new StringBuffer();
		String name = rqvo.getName();
		// 通过type区分,服务类型
		int type = rqvo.getOperateSqlType();
		String monitorType = rqvo.getMonitorType();
		int index = 0;
		// Object[] args = new Object[3];
		List<Object> args = new ArrayList<Object>();
		if (name == null) {
			name = "";
		}
		// to fix bug [3790]
		sql.append("select count(0) FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
		sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
		sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
		sql.append(" where o.CREATOR_USER_ID=?  and  i.STATE in (1,2,3,6) and  i.TEMPLATE_TYPE<>'2' and t.TYPE='" + type + "' ");
		sql.append(" and o.STATE in (1,2,3,4) and i.INSTANCE_NAME like ? ");

		args.add(rqvo.getUser().getId());
		args.add("%" + name + "%");
		if (monitorType == null) {
			monitorType = "";
		} else {
			sql.append(" and t.network_desc like ? ");
			args.add("%" + monitorType + "%");
		}

		try {

			index = jt.queryForInt(sql.toString(), args.toArray());
			log.info("sql====" + sql.toString());
			// System.out.println("sql.toString() = " + sql.toString());
		}
		catch (Exception e) {
			log.error(e);
			log.info("sql====" + sql.toString());
			log.error("args====" + args);
			throw new SCSException("queryBackUpInstance error: " + e.getMessage());
		}

		return index;
	}

	@Override
	public List<ResourcesVO> queryResouceServiceInstanceInfo(ResourcesQueryVO rqvo) throws SCSException {

		StringBuffer sql = new StringBuffer();
		// 通过type区分,服务类型
		int type = rqvo.getOperateSqlType();

		sql.append("select i.ID,i.RES_CODE,i.TEMPLATE_ID,i.INSTANCE_NAME,i.COMMENT,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.STATE,i.E_INSTANCE_ID ,i.RESOURCE_INFO,i.CREATE_DT,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.CPUFREQUENCY, ");
		sql.append("o.REASON ,tsrp.pool_name,t.network_desc,t.EXTEND_ATTR_JSON,i.PRODUCT_ID, t.resource_pools_id, t.zone_id FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
		sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
		sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
		sql.append("where o.CREATOR_USER_ID=?  and  i.STATE in (2,6) and t.state=2 and  i.TEMPLATE_TYPE<>'2' and t.TYPE='" + type + "' ");
		sql.append("and i.INSTANCE_NAME like ? ");

		if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
			sql.append("and i.ID = ? ");
		}

		BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
		List<ResourcesVO> returnList = null;
		List<Object> args = new ArrayList<Object>();
		try {
			args.add(rqvo.getUser().getId());
			String name = rqvo.getName();
			if (name == null) {
				name = "";
			}
			args.add("%" + name + "%");

			if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
				args.add(rqvo.getId());
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
			returnList = jt.query(sql.toString(), args.toArray(), infoRowMapper);
			// System.out.println("sqltest = "+sql);
			log.info("sql====" + sql.toString());
			log.info("args====" + args);
		}
		catch (Exception e) {
			log.error(e);
			log.error("sql====" + sql.toString());
			log.error("args====" + args);
			throw new SCSException(e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<ResourcesVO> queryResouceServiceInstanceInfoBeforApprove(ResourcesQueryVO rqvo) throws SCSException {

		StringBuffer sql = new StringBuffer();
		// 通过type区分,服务类型
		int type = rqvo.getOperateSqlType();

		sql.append("select i.ID,i.RES_CODE,i.TEMPLATE_ID,i.INSTANCE_NAME,i.COMMENT,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.STATE,i.E_INSTANCE_ID ,i.RESOURCE_INFO,i.CREATE_DT,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.CPUFREQUENCY, ");
		sql.append("o.REASON ,tsrp.pool_name,t.network_desc, t.resource_pools_id, t.zone_id FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
		sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
		sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
		sql.append("where o.CREATOR_USER_ID=?  ");
		sql.append("  and  i.STATE in (1,2,3,6) ");// fix bug:2798
		                                           // 对于一个用户只能申请单一资源，正在处理中的也要查询出来，有正在处理中也不允许再申请
		sql.append("  and t.state=2 and  i.TEMPLATE_TYPE<>'2' and t.TYPE='" + type + "' ");
		// to fix bug [1923]
		sql.append(" and o.STATE in (1,2,3,4) and i.INSTANCE_NAME like ? ");

		if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
			sql.append("and i.ID = ? ");
		}

		BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
		List<ResourcesVO> returnList = null;
		List<Object> args = new ArrayList<Object>();
		try {
			args.add(rqvo.getUser().getId());
			String name = rqvo.getName();
			if (name == null) {
				name = "";
			}
			args.add("%" + name + "%");

			if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
				args.add(rqvo.getId());
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
			returnList = jt.query(sql.toString(), args.toArray(), infoRowMapper);
			// System.out.println("sqltest = "+sql);
			log.info("sql====" + sql.toString());
			log.info("args====" + args);
		}
		catch (Exception e) {
			log.error(e);
			log.error("sql====" + sql.toString());
			log.error("args====" + args);
			throw new SCSException(e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<TemplateVMBO> queryResouceTemplateAvailableList(ResourcesQueryVO rqvo) throws Exception {
		int type = rqvo.getOperateSqlType();
		BeanPropertyRowMapper<TemplateVMBO> argTypes = new BeanPropertyRowMapper<TemplateVMBO>(TemplateVMBO.class);
		List<TemplateVMBO> list = new ArrayList<TemplateVMBO>();
		try {
			list = jt.query("select DISTINCT STORAGE_SIZE from T_SCS_TEMPLATE_VM where state='2' and type='" + type + "' ", argTypes);
		}
		catch (Exception e) {
			log.error("查看服务模板错误: " + e);
			throw new Exception(e.getMessage());
		}
		return list;
	}

	@Override
  public List<ResourcesVO> queryResouceInstanceInfoIncludeMc(ResourcesQueryVO rqvo) throws SCSException {


    StringBuffer sql = new StringBuffer();
    // 通过type区分,服务类型
    int type = rqvo.getOperateSqlType();

    if(type == 3){
      sql.append("select i.ID,i.RES_CODE,i.TEMPLATE_ID,i.TEMPLATE_TYPE,i.INSTANCE_NAME,i.COMMENT,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.STATE,i.E_INSTANCE_ID ,i.RESOURCE_INFO,i.CREATE_DT,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.CPUFREQUENCY, ");
      sql.append("o.REASON ,tsrp.pool_name,i.PRODUCT_ID, t.resource_pools_id, t.zone_id FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ");
      sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
      sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
      sql.append("where o.CREATOR_USER_ID=?  and  i.STATE in (2,5,6)  and  i.TEMPLATE_TYPE=3 and t.TYPE='" + type + "' ");
      sql.append("and i.INSTANCE_NAME like ? ");
    }else{
      sql.append("select i.ID,i.RES_CODE,i.TEMPLATE_ID,i.TEMPLATE_TYPE,i.INSTANCE_NAME,i.COMMENT,i.CPU_NUM,i.MEMORY_SIZE,i.STORAGE_SIZE,i.STATE,i.E_INSTANCE_ID ,i.RESOURCE_INFO,i.CREATE_DT,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.CPUFREQUENCY, ");
      sql.append("o.REASON ,tsrp.pool_name,t.network_desc,t.EXTEND_ATTR_JSON,i.PRODUCT_ID, t.resource_pools_id, t.zone_id FROM T_SCS_INSTANCE_INFO i left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ");
      sql.append("left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID ");
      sql.append("inner join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ");
      sql.append("where o.CREATOR_USER_ID=?  and  i.STATE in (2,5,6)  and  i.TEMPLATE_TYPE<>'3' and t.TYPE='" + type + "' ");
      sql.append("and i.INSTANCE_NAME like ? ");
    }
    if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
      sql.append("and i.ID = ? ");
    }

    BeanPropertyRowMapper<ResourcesVO> infoRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
    List<ResourcesVO> returnList = null;
    List<Object> args = new ArrayList<Object>();
    try {
      args.add(rqvo.getUser().getId());
      String name = rqvo.getName();
      if (name == null) {
        name = "";
      }
      args.add("%" + name + "%");

      if (rqvo.getId() != null && !"".equals(rqvo.getId())) {
        args.add(rqvo.getId());
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
      returnList = jt.query(sql.toString(), args.toArray(), infoRowMapper);
      // System.out.println("sqltest = "+sql);
      log.info("sql====" + sql.toString());
      log.info("args====" + args);
    }
    catch (Exception e) {
      log.error(e);
      log.error("sql====" + sql.toString());
      log.error("args====" + args);
      throw new SCSException(e.getMessage());
    }
    return returnList;
  
  }

  public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	@Override
	public int update(TPublicIPBO publicIP) throws SCSException {
		int ret_val = 0;
		String sql = "UPDATE T_SCS_PUBLIC_IP SET BANDWIDTH_ID=? WHERE ID=? ";
		try {
			ret_val = jt.update(sql, new Object[] { publicIP.getBandwidthId(), publicIP.getId() });
		}
		catch (Exception e) {
			throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_CREATE, publicIP.getId(), publicIP.getLastupdateDate()));
		}
		return ret_val;
	}

}
