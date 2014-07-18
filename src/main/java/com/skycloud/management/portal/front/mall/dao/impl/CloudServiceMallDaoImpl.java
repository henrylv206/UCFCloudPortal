package com.skycloud.management.portal.front.mall.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.mall.dao.CloudServiceMallDao;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.mall.entity.TemplateTypeBO;
import com.skycloud.management.portal.front.mall.vo.RelationVO;
import com.skycloud.management.portal.front.mall.vo.ResourceVO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public class CloudServiceMallDaoImpl extends SpringJDBCBaseDao implements CloudServiceMallDao {

	private final Logger logger = Logger.getLogger(CloudServiceMallDaoImpl.class);

	private static final RowMapperResultSetExtractor<Product> productMapperExtractor = new RowMapperResultSetExtractor<Product>(
			new BeanPropertyRowMapper<Product>(Product.class));

	private static final RowMapperResultSetExtractor<TServiceInstanceBO> serviceMapperExtractor = new RowMapperResultSetExtractor<TServiceInstanceBO>(
			new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class));

	private static final RowMapperResultSetExtractor<TInstanceInfoBO> instanceInfoMapperExtractor = new RowMapperResultSetExtractor<TInstanceInfoBO>(
			new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class));

	private static final RowMapperResultSetExtractor<ResourcesVO> resourcesVOMapperExtractor = new RowMapperResultSetExtractor<ResourcesVO>(
			new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class));

	// bug 0003354
	private void returnVolumeSql(StringBuffer sql, long id) {
		String Idwhere = "";
		sql.append("select i.ID,i.INSTANCE_NAME,i.STORAGE_SIZE,tvm.STATE VOLUMESTATE,i.STATE,i.E_INSTANCE_ID,tsrp.pool_name, tvm.INSTANCE_NAME VMNAME,tvm.id vmInstanceId,tvm.vmElasterID,i.PRODUCT_ID,i.ZONE_ID,i.COMMENT,i.CREATE_DT, i.LASTUPDATE_DT ");
		if (id != 0) {
			sql.append(" ,i.E_INSTANCE_ID ,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON ,tsrp.pool_name, tvm.INSTANCE_NAME VMNAME,tvm.id vmInstanceId,tvm.vmElasterID");
			Idwhere = " and i.ID=? ";
		}
		sql.append(" FROM T_SCS_INSTANCE_INFO i left join (select tsr.DISK_INSTANCE_INFO_ID ,tsii.INSTANCE_NAME ,tsr.STATE,tsii.id,tsii.E_INSTANCE_ID vmElasterID from T_SCS_IRI tsr ");
		sql.append("inner join (select tn.DISK_INSTANCE_INFO_ID,DATE_FORMAT(MAX(tn.CREATE_DT),'%Y%m%d%k%i%s') CREATE_DT from T_SCS_IRI  tn group by tn.DISK_INSTANCE_INFO_ID) tnt on tnt.DISK_INSTANCE_INFO_ID=tsr.DISK_INSTANCE_INFO_ID and tnt.CREATE_DT= DATE_FORMAT(tsr.CREATE_DT,'%Y%m%d%k%i%s') ");
		sql.append("left join T_SCS_INSTANCE_INFO tsii on tsr.VM_INSTANCE_INFO_ID=tsii.id where tsr.state NOT IN (3)) tvm on tvm.DISK_INSTANCE_INFO_ID = i.ID ");
		sql.append("left join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.CREATOR_USER_ID=? and i.STATE in (2,3,4,6,7) and  i.TEMPLATE_TYPE=2 and t.TYPE='2'");

		sql.append(Idwhere);
	}

	@Override
	public ResourcesVO getDeviceNameById(long eid, String type) throws Exception {
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		if ("VM".equalsIgnoreCase(type)) {// 虚拟机
			sql.append("select t.resource_pools_id  from T_SCS_INSTANCE_INFO i, T_SCS_TEMPLATE_VM t  where 1=1 ");
			sql.append(" and i.template_id=t.id and i.template_type=1 ");
		}// bug 0006943
		if ("HOST".equalsIgnoreCase(type)) {// 物理机
			// device_name取自Monitor.phy_server表，也同T_SCS_PHYSICAL_HOST表
			sql.append("select p.id e_instance_id, p.host_name instance_name, t.resource_pools_id  from T_SCS_INSTANCE_INFO i, T_SCS_PHYSICAL_HOST p, T_SCS_TEMPLATE_VM t  where 1=1 ");
			sql.append(" and p.id=i.e_instance_id and i.template_id=t.id and p.state='Running' and i.template_type=10");
		}
		if ("MINPHY".equalsIgnoreCase(type)) {// 小型机
			sql.append("select id e_instance_id, name instance_name  from cloud.host h, T_SCS_INSTANCE_INFO i where 1=1 and i.template_type=3");
		}
		ResourcesVO rs = null;
		if (eid != 0) {
			sql.append("  and i.e_instance_id=? ");
			param.add(eid);
		}
		try {
			List<ResourcesVO> list = getJdbcTemplate().query(sql.toString(), param.toArray(), resourcesVOMapperExtractor);
			if (null != list && !list.isEmpty()) {
				rs = list.get(0);
			}
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getVmElasterNameByEid()   error：" + e.getMessage());
		}
		return rs;

	}

	@Override
	// bug 0003354
	public ResourcesVO getVolumeVO(long userid, long id) throws Exception {
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		returnVolumeSql(sql, id);
		ResourcesVO rs = null;
		if (userid != 0) {
			param.add(userid);
		}
		if (id != 0) {
			param.add(id);
		}
		try {
			List<ResourcesVO> list = getJdbcTemplate().query(sql.toString(), param.toArray(), resourcesVOMapperExtractor);
			if (null != list && !list.isEmpty()) {
				rs = list.get(0);
			}
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getVolumeVO()   error：" + e.getMessage());
		}
		return rs;
	}

	@Override
	public int getAllServiceCount(String key, String typeId, int userId) throws Exception {
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		paramSqlDo("cnt", sql, null, null, null, null, key, typeId, userId, param);
		int rs = 0;
		try {
			rs = getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllServiceCount()   error：" + e.getMessage());
		}
		return rs;
	}

	// private String serviceSql(String key, String typeId) {
	// final StringBuffer sql = new StringBuffer();
	// sql.append("select p.*,  tt.template_name, ifnull(tt.sales, 0) sales from T_SCS_PRODUCT p,");
	// sql.append(" (	select  tp.*, oc.sales  from	T_SCS_TEMPLATE_TYPE tp  LEFT JOIN (");
	// sql.append("		select c.type, c.type_name, count(0) sales from	T_SCS_ORDER_COUNT c");
	// sql.append("		group by		c.type,		c.type_name ) oc on tp.id = oc.type ) tt ");
	// sql.append("		where p.type = tt.id		");
	// if (StringUtils.isNotEmpty(typeId) && !"0".equals(typeId)) {
	// sql.append("   and tt.id=?");
	// }
	// if (StringUtils.isNotEmpty(key)) {
	// sql.append("   and instr(concat(tt.template_name, tt.menu_code), ?)>0");
	// }
	// return sql.toString();
	// }

	// 资源sql
	private void paramSqlDo(String str, Integer serviceId, StringBuffer sql, List<Object> param) {

		if (str.equals("cnt")) {
			sql.append("select sum(t.cnt) from (");
		} else {
			sql.append("select * from (");
		}

		if (str.equals("cnt")) {
			sql.append("select count(0) cnt ");
		} else {
			sql.append("select ");
			sql.append(" ins.id, ins.order_id, ins.template_id, ins.product_id, ins.instance_name, ins.resource_info, ins.comment, ins.create_dt, ins.lastupdate_Dt, ");
			sql.append(" ins.state, ins.cpu_num, ins.memory_size, ins.os_desc, ins.storage_size, ins.res_code, ins.e_instance_id, tm.resource_pools_id, tm.zone_id, tm.special,  tm.type mc_type, ");
			// fix bug 5231
			sql.append(" tm.LB_POLICY,tm.LB_PORT,tm.LB_PROTOCOL,");
			sql.append("  si.id service_id, si.service_name, tp.id template_type, tp.template_name  template_type_name, tm.network_desc, tm.extend_attr_json, tm.code template_code, tm.template_desc, tm.store_type ");
		}
		sql.append(" from T_SCS_SERVICE_INSTANCE si, T_SCS_PRODUCT_INSTANCE_REF ref, T_SCS_INSTANCE_INFO ins, T_SCS_TEMPLATE_VM tm, T_SCS_TEMPLATE_TYPE tp ");
		sql.append(" where si.id = ref.service_instance_id ");
		sql.append("      and ref.instance_info_id = ins.id ");
		sql.append("      and ins.template_type<>3 ");
		// fix bug 3747 3782
		sql.append("		and si.id = ? and ins.state <> 1 ");
		// sql.append("		and si.id = ? and ins.state not in (1,4)");
		sql.append("		and ins.template_id = tm.id and tm.type = tp.id ");

		sql.append("  union ");

		if (str.equals("cnt")) {
			sql.append("select count(0) cnt ");
		} else {
			sql.append("select ");
			sql.append(" ins.id, ins.order_id, ins.template_id, ins.product_id, ins.instance_name, ins.resource_info, ins.comment, ins.create_dt, ins.lastupdate_Dt, ");
			sql.append(" ins.state, ins.cpu_num, ins.memory_size, ins.os_desc, ins.storage_size, ins.res_code, ins.e_instance_id, tm.resource_pools_id, tm.zone_id, tm.special, tm.type mc_type, ");
			// fix bug 5231
			sql.append(" '' LB_POLICY,'' LB_PORT, '' LB_PROTOCOL,");
			sql.append(" si.id service_id, si.service_name, 3 template_type, '小型机' as template_type_name, '' as network_desc, '' as extend_attr_json, '' as template_code, '' as template_desc, '' AS store_type  ");
		}
		sql.append(" from T_SCS_SERVICE_INSTANCE si, T_SCS_PRODUCT_INSTANCE_REF ref, T_SCS_INSTANCE_INFO ins, T_SCS_TEMPLATE_MC tm ");
		sql.append(" where si.id = ref.service_instance_id ");
		sql.append("      and ref.instance_info_id = ins.id ");
		// to fix bug:0003452 0003273: 【门户新UI】：申请一台虚拟机，审核通过后，生成一台虚拟机和一台小型机
		sql.append("      and ins.template_type=3 ");
		sql.append("		and si.id = ? and ins.state <> 1 ");
		// sql.append("		and si.id = ? and ins.state not in (1,4)");
		sql.append("		and ins.template_id = tm.id  ) t ");
		param.add(serviceId);
		param.add(serviceId);
	}

	// 我的服务退订
	// to fix bug [3967]
	private void paramQuitSqlDo(TUserBO user, StringBuffer sql, List<Object> param, int serviceID) {
		sql.append("SELECT si.ID, si.ORDER_ID, si.PRODUCT_ID, si.SERVICE_NAME,si.SERVICE_TYPE,ty.template_name service_type_name,i.ID vmid,i.STORAGE_SIZE,i.E_INSTANCE_ID,i.CLUSTER_ID,i.RES_CODE ");
		sql.append("FROM T_SCS_SERVICE_INSTANCE si,T_SCS_ORDER o,T_SCS_TEMPLATE_TYPE ty,T_SCS_INSTANCE_INFO i,T_SCS_PRODUCT_INSTANCE_REF r" +
				" WHERE si.order_id = o.order_id AND si.service_type = ty.id " +
				//to fix bug:7786 (注释余的约束条件)
//				"AND si.ORDER_ID = i.ORDER_ID " +
				"AND si.id = r.service_instance_id AND r.instance_info_id = i.id ");

		if (user != null) {
			sql.append("AND o.CREATOR_USER_ID = ? ");
			param.add(user.getId());
		}
		if (serviceID > 0) {
			sql.append("AND si.ID = ? ");
			param.add(serviceID);
		}
	}

	private String sql4myService() {
		StringBuilder sb = new StringBuilder(100);
		sb.append(" select s.*, tm.RESOURCE_POOLS_ID, tm.ZONE_ID, tm.SPECIAL ");
		sb.append(" from T_SCS_SERVICE_INSTANCE s, T_SCS_PRODUCT p, T_SCS_TEMPLATE_VM tm ");
		sb.append(" where s.SERVICE_TYPE not in (3,50) and s.PRODUCT_ID=p.ID and p.TEMPLATE_ID=tm.ID ");
		sb.append(" union ");
		sb.append(" select s.*, tm.RESOURCE_POOLS_ID, tm.ZONE_ID, tm.SPECIAL");
		sb.append("  from T_SCS_SERVICE_INSTANCE s, T_SCS_PRODUCT p, T_SCS_TEMPLATE_MC tm ");
		sb.append(" where s.SERVICE_TYPE=3 and s.PRODUCT_ID=p.ID and p.TEMPLATE_ID=tm.ID ");
		// added by hetao 20130109
		// 加上多实例类型的服务
		sb.append(" union ");
		sb.append(" select s.* from (" + sql4TemplateRelation() + ") s group by s.ID  ");
		return sb.toString();
	}

	private String sql4TemplateRelation() {
		//to fix bug:5417
		//多实例关联关系变更：T_SCS_PRODUCT_TEMPLATE_RELATION
		//                             改为   T_SCS_PRODUCT_INSTANCE_REF
		StringBuffer sql = new StringBuffer();
		sql.append(" select  s.*,tm1.RESOURCE_POOLS_ID,tm1.ZONE_ID,tm1.SPECIAL  ");
		sql.append(" from T_SCS_SERVICE_INSTANCE s, T_SCS_PRODUCT p, T_SCS_TEMPLATE_VM tm1, T_SCS_PRODUCT_INSTANCE_REF r ");
		sql.append(" where tm1.TYPE not in (3,50) and s.SERVICE_TYPE=50 and s.PRODUCT_ID=p.ID and p.ID=r.PRODUCT_ID and r.TEMPLATE_ID=tm1.ID ");
		sql.append(" UNION ");
		sql.append(" select  s.*,tm2.RESOURCE_POOLS_ID,tm2.ZONE_ID,tm2.SPECIAL ");
		sql.append("  from T_SCS_SERVICE_INSTANCE s, T_SCS_PRODUCT p, T_SCS_TEMPLATE_MC tm2, T_SCS_PRODUCT_INSTANCE_REF r  ");
		sql.append(" where tm2.TYPE=3 and s.SERVICE_TYPE=50 and s.PRODUCT_ID=p.ID and p.ID=r.PRODUCT_ID and r.TEMPLATE_ID=tm2.ID  ");
		return sql.toString();
	}

	// 我的服务sql
	private void paramSqlDo(TUserBO user, String str, String start, String end, String key, String typeId, String state, StringBuffer sql,
			List<Object> param) {
		if ("cnt".equals(str)) {
			sql.append("select count(*)");
		} else if ("list".equals(str)) {
			// to fix bug [3123]
			sql.append(" select si.*, ty.template_name service_type_name ");
			// sql.append(" select si.*, ty.template_name service_type_name, i.ID vmid, i.STORAGE_SIZE, i.E_INSTANCE_ID, i.CLUSTER_ID, i.RES_CODE ");
		}
		// to fix bug [3228]
		sql.append(" from (" + sql4myService() + ") si, ");
		sql.append(" T_SCS_ORDER o, T_SCS_TEMPLATE_TYPE ty where si.order_id = o.order_id and si.service_type = ty.id ");
		sql.append(" and si.history_state = 0 ");
		// sql.append(" from T_SCS_SERVICE_INSTANCE si, T_SCS_ORDER o, T_SCS_TEMPLATE_TYPE ty, T_SCS_INSTANCE_INFO i where si.order_id = o.order_id and si.service_type = ty.id and si.ORDER_ID = i.ORDER_ID ");

		if (user != null && user.getId() != 0) {
			sql.append(" and o.creator_user_id = ? ");
			param.add(user.getId());
		}
		if (StringUtils.isNotEmpty(typeId) && !"0".equals(typeId)) {
			sql.append(" and si.service_type = ? ");
			param.add(typeId);
		}
		if (StringUtils.isNotEmpty(key)) {
			sql.append(" and instr(si.service_name, ?)>0 ");
			param.add(key);
		}
		if (StringUtils.isNotEmpty(start)) {
			sql.append(" and substr(si.create_dt, 1, 10)>=?");
			param.add(start.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(end)) {
			sql.append(" and substr(si.create_dt, 1, 10)<=?");
			param.add(end.substring(0, 10));
		}
		if (StringUtils.isNotEmpty(state)) {
			sql.append(" and si.state=?");
			param.add(state);
		}
		if (!str.equals("cnt")) {
			// sql.append(" order by si.create_dt desc ");
			sql.append(" order by si.id desc ");// fix bug 3801
			// 同一时间下序号混乱，修改为按照ID倒排
		}
	}

	/**
	 * 多实例排重
	 */
	private String returnSql4TemplateRelation() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select  pr1.*,vm1.RESOURCE_POOLS_ID,vm1.ZONE_ID,vm1.SPECIAL  ");
		sql.append(" from T_SCS_PRODUCT pr1,T_SCS_TEMPLATE_VM vm1,T_SCS_PRODUCT_TEMPLATE_RELATION r1 ");
		sql.append(" where r1.TEMPLATE_TYPE not in (3,50) and pr1.TYPE=50 and pr1.ID=r1.PRODUCT_ID and r1.TEMPLATE_ID=vm1.ID ");
		sql.append(" UNION ");
		sql.append(" select  pr2.*,vm2.RESOURCE_POOLS_ID,vm2.ZONE_ID,vm2.SPECIAL ");
		sql.append(" from T_SCS_PRODUCT pr2,T_SCS_TEMPLATE_MC vm2,T_SCS_PRODUCT_TEMPLATE_RELATION r2  ");
		sql.append(" where r2.TEMPLATE_TYPE=3 and pr2.TYPE=50 and pr2.ID=r2.PRODUCT_ID and r2.TEMPLATE_ID=vm2.ID  ");
		return sql.toString();
	}

	private String returnSql4respool() {
		StringBuffer sql = new StringBuffer();
		sql.append("select p1.*, vm.RESOURCE_POOLS_ID, vm.ZONE_ID, vm.SPECIAL ");
		sql.append(" from T_SCS_PRODUCT p1, T_SCS_TEMPLATE_VM vm ");
		sql.append(" where 	p1.TYPE not in(3,50) and p1.TEMPLATE_ID=vm.ID ");
		sql.append(" union ");
		sql.append("select p2.*, mc.RESOURCE_POOLS_ID, mc.ZONE_ID, mc.SPECIAL ");
		sql.append(" from T_SCS_PRODUCT p2, T_SCS_TEMPLATE_MC mc ");
		sql.append(" where 	p2.TYPE=3 and p2.TEMPLATE_ID=mc.ID ");
		// added by hetao 20130109
		// 加上多虚机类型的服务
		sql.append(" union ");
		sql.append(" select pr.* from (" + returnSql4TemplateRelation() + ") pr group by pr.ID  ");
		return sql.toString();
	}

	// bug 0003180
	private String returnSql4allservice(String str, String key, String typeId, String sales, String price, Integer start, Integer end, Integer userId) {
		final StringBuffer sql = new StringBuffer();
		if (str.equals("cnt")) {
			sql.append("select count(0) from (");
		} else {
			sql.append("select * from (");
		}
		sql.append("select p.*,  tp.template_name, ifnull(ss.sales, 0) sales from (" + returnSql4respool() + ") p left join ");
		sql.append(" (	select   product_id,count(0) sales from	T_SCS_SERVICE_INSTANCE ");
		sql.append("		where 1=1  group by product_id ");
		sql.append("		 ) ss on p.id = ss.product_id, T_SCS_TEMPLATE_TYPE tp  ");
		sql.append("		where 1=1	and p.type = tp.id and p.state = 3	and p.special = 0");// 过滤特殊服务申请的自动生成模板
		if (StringUtils.isNotEmpty(typeId) && !"0".equals(typeId)) {
			sql.append("   and p.type=?");
		}
		if (StringUtils.isNotEmpty(key)) {// bug 0003197
			sql.append("   and instr(upper(concat(p.name, tp.template_name)), ?)>0");
		}
		if (!str.equals("cnt")) {
			if (StringUtils.isEmpty(sales) && StringUtils.isEmpty(price)) {
				sql.append(" order by create_date desc ");
			}
			if (StringUtils.isNotEmpty(sales)) {
				sql.append(" order by sales ");// fix 0003180
				sql.append("1".equals(sales) ? "desc" : "asc");
				sql.append(", id desc");
			}
			if (StringUtils.isNotEmpty(price)) {// bug 0003180
				sql.append(" order by price ");// fix 0003103 价格排序，排序时应将价格放前
				sql.append("1".equals(price) ? "asc" : "desc");
				sql.append(", unit desc ");
				sql.append(", id desc");
			}
		}

		if (start != null && end != null) {
			if ((start + end) != 0) {
				sql.append("  limit ?, ?  ");
			}
		}
		sql.append(" ) a");
		if (!str.equals("cnt")) {
			if (StringUtils.isEmpty(sales) && StringUtils.isEmpty(price)) {
				sql.append(" order by create_date desc ");
			}
			if (StringUtils.isNotEmpty(sales)) {
				sql.append(" order by sales ");// fix 0003180
				sql.append("1".equals(sales) ? "desc" : "asc");
				sql.append(", id desc");
			}
			if (StringUtils.isNotEmpty(price)) {// bug 0003180
				sql.append(" order by price ");// fix 0003103 价格排序，排序时应将价格放前
				sql.append("1".equals(price) ? "asc" : "desc");
				sql.append(", unit desc ");
				sql.append(", id desc");
			}
		}
		return sql.toString();
	}

	// bug 0003180
	private void paramSqlDo(String str, StringBuffer sql, final Integer start, final Integer end, String sales, String price, String key,
			String typeId, final Integer userId, List<Object> param) {
		sql.append(returnSql4allservice(str, key, typeId, sales, price, start, end, userId));

		if (StringUtils.isNotEmpty(typeId) && !"0".equals(typeId)) {
			param.add(typeId);
		}
		if (StringUtils.isNotEmpty(key)) {
			param.add(key.toUpperCase());
		}
		if (start != null && end != null) {
			if ((start + end) != 0) {
				param.add(start - 1);
				param.add(end);
			}
		}
	}

	@Override
	public List<TServiceInstanceBO> getAllServiceOfOrder(String ids, int orderType) throws Exception {
		String sql = "select o.SERVICE_INSTANCE_ID as id, o.state as orderFlag from   T_SCS_ORDER o" + " where o.state<4 and o.type = " + orderType
		+ " and o.SERVICE_INSTANCE_ID in " + ids;
		List<TServiceInstanceBO> rslist = new ArrayList<TServiceInstanceBO>();

		try {
			logger.debug("----------------------------:sql:" + sql.toString());
			rslist = getJdbcTemplate().query(sql.toString(), serviceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllService()   error：" + e.getMessage());
		}
		return rslist;
	}

	/**
	 * 云商城所有服务
	 */
	@Override
	public List<Product> getAllService(final PageVO vo, final int start, final int end, String sales, String price, String key, String typeId,
	                                   final int userId) throws Exception {
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<Product> rslist = new ArrayList<Product>();
		paramSqlDo("list", sql, start, end, sales, price, key, typeId, userId, param);
		if (vo != null) {
			int curPage = vo.getCurPage();
			int pageSize = vo.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
				param.add((curPage - 1) * pageSize);
				param.add(pageSize);
			}
		}
		try {
			logger.debug("----------------------------:sql:" + sql.toString());
			logger.debug("----------------------------:param[0]:" + param.get(0).toString());
			logger.debug("----------------------------:param[1]:" + param.get(1).toString());
			logger.debug("----------------------------:param[2]:" + typeId);
			logger.debug("----------------------------:param[3]:" + key);
			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), productMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllService()   error：" + e.getMessage());
		}
		return rslist;
	}

	@Override
	public int getCommendServiceCount(int userId) {
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		sql.append("select count(0) from (" + returnSql4respool() + ") t  where t.state = 3 and t.is_default = 1");
		return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
	}

	/**
	 * 云商城推荐服务
	 */
	@Override
	public List<Product> getCommendService(PageVO vo, int userId) throws Exception {
		logger.debug("---------------getCommendService() begin-----------");
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<Product> rslist = new ArrayList<Product>();
		sql.append("select * from ");
		sql.append("	 (" + returnSql4respool() + ") c  ");
		sql.append("		where c.state = 3 and c.is_default = 1 ");
		sql.append(" order by c.create_date desc ");
		if (vo != null) {
			int curPage = vo.getCurPage();
			int pageSize = vo.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
				param.add((curPage - 1) * pageSize);
				param.add(pageSize);
			}
		}
		try {
			logger.debug("---------------getCommendService-------------:sql:" + sql.toString());
			logger.debug("---------------getCommendService-------------:param[0]:" + param.get(0).toString());
			logger.debug("---------------getCommendService-------------:param[1]:" + param.get(1).toString());
			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), productMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getCommendService()   error：" + e.getMessage());
		}
		logger.debug("---------------getCommendService() end!-----------");
		return rslist;
	}

	@Override
	public List<TemplateTypeBO> getTemplateTypeList() throws SQLException {
		logger.debug("---------------getTemplateTypeList() begin-----------");
		List<TemplateTypeBO> rs = new ArrayList<TemplateTypeBO>();
		// FIX BUG 3909
		String sql = "SELECT A.* FROM T_SCS_TEMPLATE_TYPE A  ";// bug 0004849
		sql += " union ";
		sql += "SELECT e.* FROM T_SCS_TEMPLATE_TYPE e WHERE e.id=50 ";
		sql = "select c.* from (" + sql + ") c order by c.id";
		try {
			List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
			TemplateTypeBO info = new TemplateTypeBO();
			info.setId(0);
			info.setTemplateName("全部");
			rs.add(info);
			for (int i = 0, l = list.size(); i < l; i++) {
				info = new TemplateTypeBO();
				info.setId((Integer) list.get(i).get("id"));
				info.setTemplateName(((String) list.get(i).get("template_name")).replace("模板", "").replace("模版", ""));// bug
				// 0003928
				rs.add(info);
			}
		}
		catch (DataAccessException e) {
			logger.error("getTemplateTypeList error:" + e.getMessage());
			throw new SQLException("select failed reason：" + e.getMessage());
		}
		logger.debug("---------------getTemplateTypeList() end!-----------");
		return rs;
	}

	@Override
	public int getAllServiceCount(TUserBO user, String serviceName, String typeId, String state, String start, String end) throws Exception {
		logger.debug("---------------getAllServiceCount() begin-----------");
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		paramSqlDo(user, "cnt", start, end, serviceName, typeId, state, sql, param);
		int rs = 0;
		try {
			rs = getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllServiceCount()   error：" + e.getMessage());
		}
		logger.debug("---------------getCommendService() end!-----------");
		return rs;
	}

	/**
	 * 我的服务
	 */
	@Override
	public List<TServiceInstanceBO> getAllService(PageVO vo, TUserBO user, String serviceName, String typeId, String state, String start, String end)
	throws Exception {

		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<TServiceInstanceBO> rslist = new ArrayList<TServiceInstanceBO>();
		paramSqlDo(user, "list", start, end, serviceName, typeId, state, sql, param);
		if (vo != null) {
			int curPage = vo.getCurPage();
			int pageSize = vo.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
				param.add((curPage - 1) * pageSize);
				param.add(pageSize);
			}
		}
		try {

			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), serviceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllService()   error：" + e.getMessage());
		}
		return rslist;
	}

	@Override
	public List<TServiceInstanceBO> getQuitService(TUserBO user, int serviceID) throws Exception {

		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<TServiceInstanceBO> rslist = new ArrayList<TServiceInstanceBO>();
		paramQuitSqlDo(user, sql, param, serviceID);
		try {
			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), serviceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllService()   error：" + e.getMessage());
		}
		return rslist;
	}

	@Override
	public List<TServiceInstanceBO> getQuitInstance(TUserBO user, int serviceID) throws Exception {

		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<TServiceInstanceBO> rslist = new ArrayList<TServiceInstanceBO>();

		sql.append("SELECT i.INSTANCE_NAME serviceName, i.ID vmid, i.E_INSTANCE_ID eInstanceId, i.CLUSTER_ID clusterId, i.RES_CODE resCode, i.STATE, i.ORDER_ID FROM T_SCS_INSTANCE_INFO i, T_SCS_SERVICE_INSTANCE s, T_SCS_PRODUCT_INSTANCE_REF r WHERE s.id = ? AND r.SERVICE_INSTANCE_ID = s.id AND r.INSTANCE_INFO_ID = i.ID");
		param.add(serviceID);
		try {
			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), serviceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllService()   error：" + e.getMessage());
		}
		return rslist;
	}

	@Override
	public List<TServiceInstanceBO> checkLB2FW(int orderId) throws Exception {

		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<TServiceInstanceBO> rslist = new ArrayList<TServiceInstanceBO>();

		sql.append("SELECT i.id vmId, s.ID id, s.SERVICE_TYPE serviceType FROM T_SCS_INSTANCE_INFO i, T_SCS_PRODUCT_INSTANCE_REF r, T_SCS_SERVICE_INSTANCE s WHERE ");
		sql.append(" s.ORDER_ID = ? and s.SERVICE_TYPE in (6,7)  and r.SERVICE_INSTANCE_ID=s.ID and r.INSTANCE_INFO_ID=i.ID");
		param.add(orderId);

		try {
			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), serviceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllService()   error：" + e.getMessage());
		}
		return rslist;
	}

	@Override
	public int getAllReourceCount(int serviceId) throws Exception {
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		paramSqlDo("cnt", serviceId, sql, param);
		int rs = 0;
		try {
			rs = getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllReourceInstanceCount()   error：" + e.getMessage());
		}
		return rs;
	}

	@Override
	public List<TInstanceInfoBO> getAllReource(PageVO vo, int serviceId) throws Exception {
		logger.debug("---------------getAllReource() begin-----------");
		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		List<TInstanceInfoBO> rslist = new ArrayList<TInstanceInfoBO>();
		paramSqlDo("list", serviceId, sql, param);
		if (vo != null) {
			int curPage = vo.getCurPage();
			int pageSize = vo.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
				param.add((curPage - 1) * pageSize);
				param.add(pageSize);
			}
		}
		try {
			logger.debug("---------------getAllReource-------------:sql:" + sql.toString());
			logger.debug("---------------getAllReource-------------:param[0]:" + serviceId);
			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), instanceInfoMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getAllReource()   error：" + e.getMessage());
		}
		logger.debug("---------------getAllReource() end!-----------");
		return rslist;
	}

	/************************************** added by zhanghuizheng *************************************************/
	/**
	 * 门户上云服务根据各种查询条件查询服务（产品）
	 */
	@Override
	public List<Product> find(TTemplateVMBO t, Product p, int curPage, int pageSize) {
		// to fix bug:3190
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(" select * FROM  ");
		sql.append(" (  ");
		this.getSql4ItemService(sql, param, t, p);
		sql.append(" ) pro  ");

		if (p.getSpecification() == null || p.getSpecification().equals("") || p.getSpecification().equals("data")) {
			sql.append(" ORDER BY create_date DESC");
		} else if (p.getSpecification().equals("price")) {
			sql.append(" ORDER BY price DESC");
		}

		if (curPage > 0 && pageSize > 0) {
			sql.append(" LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		Object[] arr = param.toArray();
		String _sql = sql.toString();
		List<Product> list = getJdbcTemplate().query(_sql, arr, productMapperExtractor);
		return list;
	}

	@Override
	public int getItemServiceCount(TTemplateVMBO t, Product p) {
		final StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		sql.append(" select count(*) FROM  ");
		sql.append(" (  ");
		this.getSql4ItemService(sql, param, t, p);
		sql.append(" ) pro  ");
		String _sql = sql.toString();
		int intcount = getJdbcTemplate().queryForObject(_sql, param.toArray(), Integer.class);
		return intcount;
	}

	private void getSql4ItemService(StringBuilder sql, List<Object> param, TTemplateVMBO t, Product p) {
		/************************** 非小型机和多虚机情况 ********************************/
		sql.append(" select p.*,t.RESOURCE_POOLS_ID resourcePoolsId,t.ZONE_ID zoneId,t.SPECIAL special ");
		sql.append(" from T_SCS_PRODUCT p,T_SCS_TEMPLATE_VM t,T_SCS_PRODUCT_ITEM_RELATION_FRONT f  ");
		sql.append(" where p.TEMPLATE_ID=t.ID  ");
		sql.append(" and p.TYPE !=3  and p.TYPE != 50 ");
		sql.append(" and p.ID=f.PRODUCT_ID  ");
		// 跟马哥确认一下，用户的特殊模板有没有复用的可能？？？？？？？？？？？？？？？？？？？？？？？？
		// 还有要不要加判断条件过滤用户只能看到自己所属用户组对应的资源池的服务(产品)，公有云有用户组和所属的资源池的概念吗？？？？？？
		// sql.append(" and t.SPECIAL=0  ");

		// 内存大小
		if (t.getMemorySize() > 0) {
			sql.append(" and t.MEMORY_SIZE = ? ");
			param.add(t.getMemorySize());
		}

		// 存储类型
		if (StringUtils.isNotBlank(t.getStoreType())) {
			sql.append(" and t.STORE_TYPE = ? ");
			// param.add("'" + t.getStoreType().trim() + "'");
			// to fix bug:3324
			param.add(t.getStoreType().trim());
		}
		// cpu个数
		if (t.getCpuNum() > 0) {
			sql.append(" and t.CPU_NUM = ? ");
			param.add(t.getCpuNum());
		}

		// 网卡个数
		if (t.getVethAdaptorNum() > 0) {
			sql.append(" and t.VETH_ADAPTOR_NUM = ? ");
			param.add(t.getVethAdaptorNum());
		}
		// 磁盘大小
		if (t.getStorageSize() > 0) {
			sql.append(" and t.STORAGE_SIZE = ? ");
			param.add(t.getStorageSize());
		}

		// 服务类型
		if (p.getType() > 0) {
			sql.append(" and p.TYPE = ? ");
			param.add(p.getType());
		}
		// 名称
		if (null != p.getName() && !p.getName().equals("")) {
			sql.append(" and instr(concat(p.NAME, p.CODE), ?)>0 ");
			param.add(p.getName());
		}

		// 状态
		if (p.getState() > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(p.getState());
		}

		// 价格
		if (p.getPrice() > 0) {
			sql.append(" AND p.PRICE >= ?");
			param.add(p.getPrice());
		}

		if (p.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(p.getPrice2());
		}
		// 是否首页推荐
		if (p.getIsDefault() > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(p.getIsDefault());
		}

		if (p.getProductItemId() != null) {
			sql.append(" AND f.PRODUCT_ITEM_ID = ?");
			param.add(p.getProductItemId());
		}
		/************************** 非小型机和多虚机情况 ********************************/

		/******************************** 小型机 ***************************************/
		sql.append(" UNION all  ");
		sql.append(" select p.*,t.RESOURCE_POOLS_ID resourcePoolsId,t.ZONE_ID zoneId,t.SPECIAL special ");
		sql.append(" from T_SCS_PRODUCT p,T_SCS_TEMPLATE_MC t,T_SCS_PRODUCT_ITEM_RELATION_FRONT f  ");
		sql.append(" where p.TEMPLATE_ID=t.ID  ");
		sql.append(" and p.TYPE =3  ");
		sql.append(" and p.ID=f.PRODUCT_ID  ");
		// sql.append(" and t.SPECIAL=0  ");
		// 内存大小
		if (t.getMemorySize() > 0) {
			sql.append(" and t.MEMORY_SIZE = ? ");
			param.add(t.getMemorySize());
		}
		// cpu个数
		if (t.getCpuNum() > 0) {
			sql.append(" and t.CPU_NUM = ? ");
			param.add(t.getCpuNum());
		}

		// 网卡个数
		if (t.getVethAdaptorNum() > 0) {
			sql.append(" and t.VETH_ADAPTOR_NUM = ? ");
			param.add(t.getVethAdaptorNum());
		}
		// 磁盘大小
		if (t.getStorageSize() > 0) {
			sql.append(" and t.STORAGE_SIZE = ? ");
			param.add(t.getStorageSize());
		}

		// 名称
		if (null != p.getName() && !p.getName().equals("")) {
			sql.append(" and instr(concat(p.NAME, p.CODE), ?)>0 ");
			param.add(p.getName());
		}

		// 状态
		if (p.getState() > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(p.getState());
		}

		// 价格
		if (p.getPrice() > 0) {
			sql.append(" AND p.PRICE >= ?");
			param.add(p.getPrice());
		}

		if (p.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(p.getPrice2());
		}
		// 是否首页推荐
		if (p.getIsDefault() > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(p.getIsDefault());
		}

		if (p.getProductItemId() != null) {
			sql.append(" AND f.PRODUCT_ITEM_ID = ?");
			param.add(p.getProductItemId());
		}
		// to fix bug:3655
		// 服务类型
		if (p.getType() > 0) {
			sql.append(" and p.TYPE = ? ");
			param.add(p.getType());
		}
		/******************************** 小型机 ***************************************/

		/******************************** 多虚机 ***************************************/
		sql.append(" UNION ALL  ");
		sql.append(" select DISTINCT p.*,t.RESOURCE_POOLS_ID resourcePoolsId,t.ZONE_ID zoneId,t.SPECIAL special ");
		sql.append(" from T_SCS_PRODUCT p,T_SCS_PRODUCT_TEMPLATE_RELATION r,T_SCS_PRODUCT_ITEM_RELATION_FRONT f,T_SCS_TEMPLATE_VM t  ");
		sql.append(" where p.ID=r.PRODUCT_ID  ");
		sql.append(" and p.TYPE =50  ");
		sql.append(" and p.ID=f.PRODUCT_ID  ");
		sql.append(" and r.TEMPLATE_ID=t.ID  ");
		// sql.append(" and t.SPECIAL=0  ");

		// 内存大小
		if (t.getMemorySize() > 0) {
			sql.append(" and t.MEMORY_SIZE = ? ");
			param.add(t.getMemorySize());
		}

		// 存储类型
		if (StringUtils.isNotBlank(t.getStoreType())) {
			sql.append(" and t.STORE_TYPE = ? ");
			// param.add("'" + t.getStoreType().trim() + "'");
			param.add(t.getStoreType().trim());
		}
		// cpu个数
		if (t.getCpuNum() > 0) {
			sql.append(" and t.CPU_NUM = ? ");
			param.add(t.getCpuNum());
		}

		// 网卡个数
		if (t.getVethAdaptorNum() > 0) {
			sql.append(" and t.VETH_ADAPTOR_NUM = ? ");
			param.add(t.getVethAdaptorNum());
		}
		// 磁盘大小
		if (t.getStorageSize() > 0) {
			sql.append(" and t.STORAGE_SIZE = ? ");
			param.add(t.getStorageSize());
		}

		// 服务类型
		if (p.getType() > 0) {
			sql.append(" and p.TYPE = ? ");
			param.add(p.getType());
		}
		// 名称
		if (null != p.getName() && !p.getName().equals("")) {
			sql.append(" and instr(concat(p.NAME, p.CODE), ?)>0 ");
			param.add(p.getName());
		}

		// 状态
		if (p.getState() > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(p.getState());
		}

		// 价格
		if (p.getPrice() > 0) {
			sql.append(" AND p.PRICE >= ?");
			param.add(p.getPrice());
		}

		if (p.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(p.getPrice2());
		}
		// 是否首页推荐
		if (p.getIsDefault() > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(p.getIsDefault());
		}

		if (p.getProductItemId() != null) {
			sql.append(" AND f.PRODUCT_ITEM_ID = ?");
			param.add(p.getProductItemId());
		}
		/******************************** 多虚机 ***************************************/

	}



	@Override
	public List<Product> findRand() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from T_SCS_PRODUCT order by rand() limit 3 ");
		return getJdbcTemplate().query(sql.toString(), productMapperExtractor);
	}

	/**
	 * 查询拓扑图资源信息 ninghao 2012-09-05
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ResourceVO> getMyTopoReourceVO(TUserBO user, String[] type) throws Exception {
		logger.debug("---------------getMyTopoReourceVO() begin-----------");
		List<ResourceVO> rslist = new ArrayList<ResourceVO>();

		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();

		// paramSqlDo("list", serviceId, sql, param);

		sql.append(" select info.ID sid, info.E_INSTANCE_ID id, info.INSTANCE_NAME name, null host ");
		sql.append("       ,tm.RESOURCE_POOLS_ID poolid ");// 资源池ID
		sql.append("       ,IF(info.template_type = 1,\"VM\",IF(info.template_type = 6,\"LOADBALANCE\",IF(info.template_type = 7,\"FIREWALL\",\"\"))) type ");
		sql.append(" from T_SCS_INSTANCE_INFO info, T_SCS_ORDER o, T_SCS_TEMPLATE_TYPE ty ");
		sql.append("       ,T_SCS_TEMPLATE_VM tm "); // 模板对应资源池
		// sql.append("     ,T_SCS_SERVICE_INSTANCE ins  ");//fix bug 3759
		// 拓扑图仅关联资源状态，不需要关联服务状态
		// sql.append("     ,T_SCS_PRODUCT_INSTANCE_REF ref  ");
		sql.append(" where info.ORDER_ID = o.order_id  ");
		sql.append(" and info.template_type = ty.id  ");
		sql.append(" and info.template_id = tm.id  "); // 模板对应资源池
		// sql.append(" AND ins.ID = ref.service_instance_id ");// 增加资源与服务实例关系表
		// sql.append(" AND info.ID = ref.instance_info_id ");
		sql.append(" and info.state<>1 ");// 资源状态：可用（排除：申请中）
		sql.append(" and info.state<>4 ");// 资源状态：可用（排除：已回收）
		sql.append(" and info.state<>7 ");// 资源状态：可用（排除：创建失败）
		// sql.append(" and info.STATE = 2 ");//资源状态：可用
		// sql.append(" and ins.service_type = ty.id  ");
		// sql.append(" and ins.state<>4  ");// 服务状态 不等于 作废
		// sql.append(" and ins.state=2  ");// 服务状态：可用
		// sql.append(" and ins.ORDER_ID = info.ORDER_ID ");
		// sql.append(" and ins.service_name = info.INSTANCE_NAME ");

		if (user != null) {
			sql.append(" and o.creator_user_id = ? ");
			param.add(user.getId());
		}
		if (type != null && type.length > 0) {
			if (type.length == 1) {
				if (StringUtils.isNotEmpty(type[0]) && !"0".equals(type[0])) {
					sql.append(" and info.template_type = ? ");
					param.add(type[0]);
				}
			} else {
				for (int ti = 0; ti < type.length; ti++) {
					if (ti == 0) {
						sql.append(" and (  ");
						if (StringUtils.isNotEmpty(type[ti]) && !"0".equals(type[ti])) {
							sql.append(" info.template_type = ? ");
							param.add(type[ti]);
						}
					} else {
						if (StringUtils.isNotEmpty(type[ti]) && !"0".equals(type[ti])) {
							sql.append(" or info.template_type = ?  ");
							param.add(type[ti]);
						}
					}
					if (ti == type.length - 1) {
						sql.append(" )");
					}
				}
			}
		}
		// UNION VLAN
		sql.append(" union all  ");
		// fix bug 3672,3749 SQL查询结果中文是乱码问题
		sql.append(" select rel.E_VLAN_ID sid, rel.E_VLAN_ID id, CONCAT('VLAN',''+rel.E_VLAN_ID,'') name, null host ");
		sql.append("       ,tm.RESOURCE_POOLS_ID poolid ");// 资源池ID
		sql.append("       ,'VLAN' type ");
		sql.append("   from T_SCS_NICS rel , T_SCS_INSTANCE_INFO info, T_SCS_ORDER o ");
		sql.append("       ,T_SCS_TEMPLATE_VM tm "); // 模板对应资源池
		sql.append("  where 1=1 ");
		sql.append("    and rel.E_VLAN_ID > 0 ");
		sql.append("    and rel.VM_INSTANCE_INFO_ID = info.ID ");
		sql.append("    and info.template_id = tm.id  "); // 模板对应资源池
		sql.append("    and o.ORDER_ID = info.ORDER_ID  ");
		sql.append("    and rel.STATE = 1 ");
		sql.append("    and info.state<>1 ");// 资源状态：可用（排除：申请中）
		sql.append("    and info.state<>4 ");// 资源状态：可用（排除：已回收）
		sql.append("    and info.state<>7 ");// 资源状态：可用（排除：创建失败）
		// sql.append("    and info.STATE = 2 ");//资源状态：可用//fix bug 3691
		// VLAN关联的资源为可用才显示
		// sql.append("    and rel.IP != '0' ");//先放开VLAN的IP判断
		if (user != null) {
			sql.append(" and o.creator_user_id = ? ");
			param.add(user.getId());
		}
		if (type != null && type.length > 0) {
			if (type.length == 1) {
				if (StringUtils.isNotEmpty(type[0]) && !"0".equals(type[0])) {
					sql.append(" and info.template_type = ? ");
					param.add(type[0]);
				}
			} else {
				for (int ti = 0; ti < type.length; ti++) {
					if (ti == 0) {
						sql.append(" and (  ");
						if (StringUtils.isNotEmpty(type[ti]) && !"0".equals(type[ti])) {
							sql.append(" info.template_type = ? ");
							param.add(type[ti]);
						}
					} else {
						if (StringUtils.isNotEmpty(type[ti]) && !"0".equals(type[ti])) {
							sql.append(" or info.template_type = ?  ");
							param.add(type[ti]);
						}
					}
					if (ti == type.length - 1) {
						sql.append(" )");
					}
				}
			}
		}
		sql.append(" GROUP BY rel.E_VLAN_ID,tm.RESOURCE_POOLS_ID ");

		try {
			logger.debug("---------------getMyTopoReourceVO-------------:sql:" + sql.toString());
			// logger.debug("---------------getMyTopoReourceVO-------------:param[0]:"
			// + serviceId);
			RowMapperResultSetExtractor<ResourceVO> resourceMapperExtractor = new RowMapperResultSetExtractor<ResourceVO>(
					new BeanPropertyRowMapper<ResourceVO>(ResourceVO.class));

			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), resourceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getMyTopoReourceVO()   error：" + e.getMessage());
		}

		logger.debug("---------------getMyTopoReourceVO() end!-----------");
		return rslist;
	}

	/**
	 * 查询拓扑图资源关系信息 ninghao 2012-09-05
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<RelationVO> getMyTopoRelationVO(TUserBO user, String[] type) throws Exception {
		logger.debug("---------------getMyTopoRelationVO() begin-----------");
		List<RelationVO> rslist = new ArrayList<RelationVO>();

		final StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();

		// paramSqlDo("list", serviceId, sql, param);

		sql.append(" select rel.id sid,rel.E_VLAN_ID fromId, 0 fromPort, rel.VM_INSTANCE_INFO_ID toId, 0 toPort");
		sql.append("   from T_SCS_NICS rel, T_SCS_INSTANCE_INFO info, T_SCS_ORDER o, T_SCS_TEMPLATE_TYPE ty ");
		sql.append("  where 1=1 ");
		sql.append(" and rel.E_VLAN_ID > 0 ");
		sql.append(" and info.order_id = o.order_id  ");
		sql.append(" and info.id = rel.VM_INSTANCE_INFO_ID  ");
		sql.append(" and info.template_type = ty.id  ");
		sql.append(" and info.state<>1 ");// 资源状态：可用（排除：申请中）
		sql.append(" and info.state<>4 ");// 资源状态：可用（排除：已回收）
		sql.append(" and info.state<>7 ");// 资源状态：可用（排除：创建失败）

		if (user != null) {
			sql.append(" and o.creator_user_id = ? ");
			param.add(user.getId());
		}
		if (type != null && type.length > 0) {
			if (type.length == 1) {
				if (StringUtils.isNotEmpty(type[0]) && !"0".equals(type[0])) {
					sql.append(" and info.template_type = ? ");
					param.add(type[0]);
				}
			} else {
				for (int ti = 0; ti < type.length; ti++) {
					if (ti == 0) {
						sql.append(" and (  ");
						if (StringUtils.isNotEmpty(type[ti]) && !"0".equals(type[ti])) {
							sql.append(" info.template_type = ? ");
							param.add(type[ti]);
						}
					} else {
						if (StringUtils.isNotEmpty(type[ti]) && !"0".equals(type[ti])) {
							sql.append(" or info.template_type = ?  ");
							param.add(type[ti]);
						}
					}
					if (ti == type.length - 1) {
						sql.append(" )");
					}
				}
			}
		}

		try {
			logger.debug("---------------getMyTopoReourceVO-------------:sql:" + sql.toString());
			// logger.debug("---------------getMyTopoReourceVO-------------:param[0]:"
			// + serviceId);
			RowMapperResultSetExtractor<RelationVO> resourceMapperExtractor = new RowMapperResultSetExtractor<RelationVO>(
					new BeanPropertyRowMapper<RelationVO>(RelationVO.class));

			rslist = getJdbcTemplate().query(sql.toString(), param.toArray(), resourceMapperExtractor);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new Exception("getMyTopoReourceVO()   error：" + e.getMessage());
		}

		logger.debug("---------------getMyTopoReourceVO() end!-----------");
		return rslist;
	}

}
