package com.skycloud.management.portal.admin.resmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.resmanage.dao.IProductTemplateRelationDao;
import com.skycloud.management.portal.admin.resmanage.entity.vo.ProductTemplateRelationBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.util.TemplateUtils;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class ProductTemplateRelationDaoImpl extends SpringJDBCBaseDao implements
		IProductTemplateRelationDao {

	private static Log log = LogFactory
			.getLog(ProductTemplateRelationDaoImpl.class);
	private static final RowMapperResultSetExtractor<TTemplateMCBO> mcTemplateMapperExtractor = new RowMapperResultSetExtractor<TTemplateMCBO>(
	        new BeanPropertyRowMapper<TTemplateMCBO>(TTemplateMCBO.class));

//	private static final class ProductTemplateRelationMapper implements
//			RowMapper {
//
//		@Override
//		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//			ProductTemplateRelationBO item = new ProductTemplateRelationBO();
//			item.setId(rs.getInt("id"));
//			item.setProductId(rs.getInt("PRODUCT_ID"));
//			item.setTemplateId(rs.getInt("TEMPLATE_ID"));
//			item.setNum(rs.getInt("NUM"));
//			return item;
//		}
//
//	}

	@SuppressWarnings("unchecked")
	private static final class VMTemplateMapper2 implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int rn) throws SQLException {
			TTemplateVMBO template = new TTemplateVMBO();
			template.setId(rs.getInt("id"));
			template.setResourcePoolsId(rs.getInt("resource_pools_id"));
			template.setCode(rs.getString("code"));
			template.setTemplateDesc(rs.getString("template_desc"));
			template.setCreateTime(Constants.SDF.YYYYMMDDHHMMSS.getValue()
					.format(rs.getTimestamp("create_time")));
			template.setCreatorUserId(rs.getInt("creator_user_id"));
			template.seteServiceId(rs.getInt("e_service_id"));
			template.setCpuNum(rs.getInt("cpu_num"));
			template.setMemorySize(rs.getInt("memory_size"));
			template.seteDiskId(rs.getInt("e_disk_id"));
			template.setOsDiskType(rs.getString("os_disk_type"));
			template.setOsSize(rs.getInt("os_size"));
			template.setVethAdaptorNum(rs.getInt("veth_adaptor_num"));
			template.setVscsiAdaptorNum(rs.getInt("vscsi_adaptor_num"));
			template.setVmos(rs.getString("vmos"));
			template.setType(rs.getInt("type"));
			template.setCpufrequency(rs.getFloat("cpufrequency"));
			int state = rs.getInt("state");
			template.setState(state);
			int operType = rs.getInt("OPER_TYPE");
			template.setOperType(operType);
			String stateName = "";
			if (state == 1) {
				String operTypeName = "";
				operTypeName = ConstDef.OPER_TYPE_MAP.get(operType);
				if (null != operTypeName && !operTypeName.equals("")) {
					stateName = operTypeName
							+ ConstDef.TEMPLATE_STATE_MAP.get(state);
				} else {
					stateName = ConstDef.TEMPLATE_STATE_MAP.get(state);
				}

			} else {
				stateName = ConstDef.TEMPLATE_STATE_MAP.get(state);
			}

			template.setStateName(stateName);
			template.setStorageSize(rs.getInt("storage_size"));
			template.seteOsId(rs.getInt("e_os_id"));
			// added by zhanghuizheng
			if (rs.getInt("type") == 5) {
				String resourceTypeCodes = rs.getString("network_desc");
				String resourceTypes = TemplateUtils
						.getResourceTypesByCodes(resourceTypeCodes);
				template.setNetworkDesc(resourceTypes);
			}
			template.setResourceTemplate(rs.getString("RESOURCE_TEMPLATE"));

			template.setMeasureMode(rs.getString("MEASURE_MODE"));
			template.setGrade(rs.getString("GRADE"));
			template.setExtendAttrJSON(rs.getString("EXTEND_ATTR_JSON"));
			template.setErrorCode(rs.getString("ERROR_CODE"));
			template.setStoreType(rs.getString("STORE_TYPE"));
			template.setZoneId(rs.getInt("ZONE_ID"));
//			template.setTemplateNum(rs.getInt("templateNum"));
			return template;
		}
	}

	@Override
	public int createProductTemplateRelation(
			final ProductTemplateRelationBO relation) throws Exception {
		final String sql = "INSERT INTO T_SCS_PRODUCT_TEMPLATE_RELATION(PRODUCT_ID,TEMPLATE_ID,NUM,TEMPLATE_TYPE) VALUES (?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, relation.getProductId());
				ps.setInt(2, relation.getTemplateId());
				ps.setInt(3, relation.getNum());
				ps.setInt(4, relation.getTemplateType());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	private String sqlProductTemplate(){
		String sql="";
		sql="select t1.ID, t1.RESOURCE_POOLS_ID,	t1.CODE,	t1.TEMPLATE_DESC,	t1.CREATE_TIME,		" +
				" t1.CREATOR_USER_ID, t1.E_SERVICE_ID,	t1.CPU_NUM,	t1.MEMORY_SIZE,	t1.E_DISK_ID, " +
				" t1.OS_DISK_TYPE,	t1.OS_SIZE,	t1.VETH_ADAPTOR_NUM,	t1.VSCSI_ADAPTOR_NUM,	t1.VMOS,	t1.TYPE, " +
				" t1.CPUFREQUENCY,	t1.STATE,	t1.OPER_TYPE,	t1.STORAGE_SIZE,	t1.E_OS_ID,	" +
				" t1.NETWORK_DESC,	t1.RESOURCE_TEMPLATE,	t1.MEASURE_MODE,	t1.GRADE,	t1.EXTEND_ATTR_JSON, " +
				" t1.ERROR_CODE,	t1.STORE_TYPE,	t1.ZONE_ID " +
				" from T_SCS_TEMPLATE_VM t1 "
		        +" union "
		+" SELECT t2.ID, t2.RESOURCE_POOLS_ID,	t2.CODE,	t2.TEMPLATE_DESC,	t2.CREATE_TIME, " +
				" t2.CREATOR_USER_ID, 0,	t2.CPU_NUM ,	t2.MEMORY_SIZE ,	0, " +
				" t2.OS_DISK_TYPE,	t2.OS_SIZE,	t2.VETH_ADAPTOR_NUM,	t2.VSCSI_ADAPTOR_NUM,	t2.VMOS,	3," +//物理机模板类型设定为3
				" t2.CPUFREQUENCY,	t2.STATE,	t2.OPER_TYPE,	t2.STORAGE_SIZE,	t2.TYPE as E_OS_ID,	" +//t2.TYPE as E_OS_ID小型机类型；1：SUN；2：IBM；3：HP
				" null,	null,	t2.MEASURE_MODE,	null,	null," +
				" null,	null,	t2.ZONE_ID" +
				" from T_SCS_TEMPLATE_MC t2 ";
		return sql;
	}

	/**
	 * 查询机模板
	 */
	@Override
	public List<TTemplateVMBO> getProductTemplates(int productId) {
		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT t.*,r.NUM as templateNum FROM T_SCS_TEMPLATE_VM t,T_SCS_PRODUCT_TEMPLATE_RELATION r");
//		sql.append("SELECT t.* FROM T_SCS_TEMPLATE_VM t,T_SCS_PRODUCT_TEMPLATE_RELATION r");
		sql.append("SELECT t.* FROM ("+sqlProductTemplate()+") t,T_SCS_PRODUCT_TEMPLATE_RELATION r");
		sql.append(" WHERE t.ID=r.TEMPLATE_ID and t.TYPE=r.TEMPLATE_TYPE and r.PRODUCT_ID=?");
		sql.append(" ORDER BY r.id");
		return getJdbcTemplate().query(sql.toString(),
				new Object[] { productId }, new VMTemplateMapper2());

	}

	/**
	 * 查询小型机模板
	 */
//	@Override
//    public List<TTemplateMCBO> getProductMCTemplates(int productId) {
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT t.* FROM T_SCS_TEMPLATE_MC t,T_SCS_PRODUCT_TEMPLATE_RELATION r");
//
//		sql.append(" WHERE t.ID=r.TEMPLATE_ID and r.TEMPLATE_TYPE=3 and r.PRODUCT_ID=?");
//		sql.append(" ORDER BY r.id");
//		return getJdbcTemplate().query(sql.toString(),
//				new Object[] { productId }, mcTemplateMapperExtractor);
//
//	}

	@Override
	public void deleteProductTemplateRelation(int productId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM T_SCS_PRODUCT_TEMPLATE_RELATION WHERE product_id=? ");
		getJdbcTemplate().update(sql.toString(), productId);
	}

	// @Override
	// public ProductTemplateRelationBO getProductTemplateRelationById(int id)
	// throws Exception {
	// String sql = "SELECT * FROM T_SCS_PRODUCT_TEMPLATE_RELATION WHERE id=?";
	// ProductTemplateRelationBO item = null;
	// try {
	// item = (ProductTemplateRelationBO) getJdbcTemplate().queryForObject(
	// sql, new ProductTemplateRelationMapper(), id);
	// } catch (EmptyResultDataAccessException e) {
	// return null;
	// }
	// return item;
	// }

	// @Override
	// public List<ProductTemplateRelationBO> listProductTemplateRelation(int
	// itemId)
	// throws Exception {
	// List<ProductTemplateRelationBO> list = new
	// ArrayList<ProductTemplateRelationBO>();
	// List<Object> param = new ArrayList<Object>();
	// StringBuilder sbuilder = new StringBuilder();
	// sbuilder.append("SELECT * FROM T_SCS_PRODUCT_TEMPLATE_RELATION t where 1=1 ");
	// if (itemId >= 0) {
	// sbuilder.append(" and t.product_id=? ");
	// param.add(itemId);
	// }
	// list = getJdbcTemplate().query(sbuilder.toString(), param.toArray(),
	// new ProductTemplateRelationMapper());
	// return list;
	// }

}
