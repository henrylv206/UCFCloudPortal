package com.skycloud.management.portal.admin.template.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.util.TemplateUtils;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;

/**
 * 虚机模板持久化实现
 *
 * @author jiaoyz
 */
public class VMTemplateDaoImpl extends SpringJDBCBaseDao implements IVMTemplateDao {

	private static Log log = LogFactory.getLog(VMTemplateDaoImpl.class);

	@SuppressWarnings("unchecked")
	private static final class VMTemplateMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rn) throws SQLException {
			TTemplateVMBO template = new TTemplateVMBO();
			template.setId(rs.getInt("id"));
			template.setResourcePoolsId(rs.getInt("resource_pools_id"));
			template.setCode(rs.getString("code"));
			template.setTemplateDesc(rs.getString("template_desc"));
			template.setCreateTime(Constants.SDF.YYYYMMDDHHMMSS.getValue().format(rs.getTimestamp("create_time")));
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
					stateName = operTypeName + ConstDef.TEMPLATE_STATE_MAP.get(state);
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
				String resourceTypes = TemplateUtils.getResourceTypesByCodes(resourceTypeCodes);
				template.setNetworkDesc(resourceTypes);
			}
			template.setResourceTemplate(rs.getString("RESOURCE_TEMPLATE"));

			template.setMeasureMode(rs.getString("MEASURE_MODE"));
			template.setGrade(rs.getString("GRADE"));
			template.setExtendAttrJSON(rs.getString("EXTEND_ATTR_JSON"));
			template.setErrorCode(rs.getString("ERROR_CODE"));
			template.setStoreType(rs.getString("STORE_TYPE"));
			template.setProtocol(rs.getString("LB_PROTOCOL"));
			template.setPolicy(rs.getString("LB_POLICY"));
			template.setPort(rs.getInt("LB_PORT"));
			//1.3功能，增加资源域和特殊模板标志
			template.setZoneId(rs.getInt("ZONE_ID"));
			template.setSpecial(rs.getInt("SPECIAL"));
			return template;
		}
	}

	@Override
	public int createTemplate(final TTemplateVMBO template) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO T_SCS_TEMPLATE_VM(TYPE, CODE, RESOURCE_POOLS_ID, TEMPLATE_DESC, CREATOR_USER_ID, CREATE_TIME, E_SERVICE_ID,"
		        + "  CPU_NUM, MEMORY_SIZE, E_DISK_ID, OS_DISK_TYPE, OS_SIZE, VETH_ADAPTOR_NUM, VSCSI_ADAPTOR_NUM, VMOS, CPUFREQUENCY, STATE,"
		        + " STORAGE_SIZE, E_OS_ID,NETWORK_DESC) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, template.getType());
				ps.setString(2, template.getCode());
				ps.setInt(3, template.getResourcePoolsId());
				ps.setString(4, template.getTemplateDesc());
				ps.setInt(5, template.getCreatorUserId());
				ps.setString(6, template.getCreateTime());
				ps.setInt(7, template.geteServiceId());
				ps.setInt(8, template.getCpuNum());
				ps.setInt(9, template.getMemorySize());
				ps.setInt(10, template.geteDiskId());
				ps.setString(11, template.getOsDiskType());
				ps.setInt(12, template.getOsSize());
				ps.setInt(13, template.getVethAdaptorNum());
				ps.setInt(14, template.getVscsiAdaptorNum());
				ps.setString(15, template.getVmos());
				ps.setFloat(16, template.getCpufrequency());
				ps.setInt(17, template.getState());
				ps.setInt(18, template.getStorageSize());
				ps.setInt(19, template.geteOsId());
				ps.setString(20, template.getNetworkDesc());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public int createTemplate_VDC(final TTemplateVMBO template) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		StringBuilder strb = new StringBuilder();
		strb.append("INSERT INTO T_SCS_TEMPLATE_VM(");
		strb.append("TYPE, ");
		strb.append("CODE, ");
		strb.append("RESOURCE_POOLS_ID, ");
		strb.append("TEMPLATE_DESC, ");
		strb.append("CREATOR_USER_ID, ");
		strb.append("CREATE_TIME, ");
		strb.append("E_SERVICE_ID,");
		strb.append("CPU_NUM, ");
		strb.append("MEMORY_SIZE, ");
		strb.append("E_DISK_ID, ");
		strb.append("OS_DISK_TYPE, ");
		strb.append("OS_SIZE, ");
		strb.append("VETH_ADAPTOR_NUM, ");
		strb.append("VSCSI_ADAPTOR_NUM, ");
		strb.append("VMOS, ");
		strb.append("CPUFREQUENCY, ");
		strb.append("STATE,");
		strb.append("STORAGE_SIZE, ");
		strb.append("E_OS_ID,");
		strb.append("NETWORK_DESC,");
		strb.append("RESOURCE_TEMPLATE,");
		strb.append("OPER_TYPE,");
		strb.append("MEASURE_MODE,");
		strb.append("GRADE,");
		strb.append("EXTEND_ATTR_JSON,");
		strb.append("ERROR_CODE,");
		strb.append("STORE_TYPE,");
		strb.append("LB_PROTOCOL,");
		strb.append("LB_POLICY,");
		strb.append("LB_PORT,");
		// 1.3功能，增加资源域zone
		strb.append("ZONE_ID,");
		// 1.3功能，增加special
		strb.append("SPECIAL");
		strb.append(") VALUES (");
		strb.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
		strb.append(")");

		final String sql = strb.toString();
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, template.getType());
				ps.setString(2, template.getCode());
				ps.setInt(3, template.getResourcePoolsId());
				ps.setString(4, template.getTemplateDesc());
				ps.setInt(5, template.getCreatorUserId());
				ps.setString(6, template.getCreateTime());
				ps.setInt(7, template.geteServiceId());
				ps.setInt(8, template.getCpuNum());
				ps.setInt(9, template.getMemorySize());
				ps.setInt(10, template.geteDiskId());
				ps.setString(11, template.getOsDiskType());
				ps.setInt(12, template.getOsSize());
				ps.setInt(13, template.getVethAdaptorNum());
				ps.setInt(14, template.getVscsiAdaptorNum());
				ps.setString(15, template.getVmos());
				ps.setFloat(16, template.getCpufrequency());
				ps.setInt(17, template.getState());
				ps.setInt(18, template.getStorageSize());
				ps.setInt(19, template.geteOsId());
				ps.setString(20, template.getNetworkDesc());

				ps.setString(21, template.getResourceTemplate());
				ps.setInt(22, template.getOperType());
				ps.setString(23, template.getMeasureMode());
				ps.setString(24, template.getGrade());
				ps.setString(25, template.getExtendAttrJSON());
				ps.setString(26, template.getErrorCode());
				ps.setString(27, template.getStoreType());
				ps.setString(28, template.getProtocol());
				ps.setString(29, template.getPolicy());
				ps.setInt(30, template.getPort());
				// 1.3功能，增加资源域zone
				ps.setInt(31, template.getZoneId());
				// 1.3功能，增加special
				ps.setInt(32, template.getSpecial());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void deleteTemplate_VDC(int id, int state) throws Exception {
		// String sql = "DELETE FROM T_SCS_TEMPLATE_VM WHERE ID = ?";
		String sql = "UPDATE T_SCS_TEMPLATE_VM SET state = ?,OPER_TYPE = 3 WHERE ID = ?";
		Object[] args = new Object[] { state, id };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		getJdbcTemplate().update(sql, args, argTypes);
	}

	@Override
	public void deleteTemplate(int id) throws Exception {
		// String sql = "DELETE FROM T_SCS_TEMPLATE_VM WHERE ID = ?";
		String sql = "UPDATE T_SCS_TEMPLATE_VM SET state = 5 WHERE ID = ?";
		getJdbcTemplate().update(sql, id);
	}

	@Override
	public int upateTemplateState(int templateId, int state) throws DataAccessException {
		String sql = "UPDATE T_SCS_TEMPLATE_VM SET STATE = ? WHERE ID = ?";
		Object[] args = new Object[] { state, templateId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		int index = 0;
		index = getJdbcTemplate().update(sql, args, argTypes);
		return index;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TTemplateVMBO getTemplateById(int id) throws SCSException {
		//增加负载均衡字段
		String sql = "SELECT *,LB_PROTOCOL  protocol,LB_POLICY policy,LB_PORT port  FROM T_SCS_TEMPLATE_VM WHERE ID = ?";
		TTemplateVMBO template = null;
		try {
			template = (TTemplateVMBO) getJdbcTemplate().queryForObject(sql, new VMTemplateMapper(), id);
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		return template;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TTemplateVMBO> listTemplate(int type, int state, int curPage, int pageSize, int resourcePoolsId, int zoneId) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		//把特殊模板id=-1的模板隐藏
		sql.append("SELECT * FROM T_SCS_TEMPLATE_VM WHERE 1 = 1 and id>0 ");
		if (type > 0) {
			sql.append(" AND TYPE = ?");
			param.add(type);
		}
		if (state > 0) {
			sql.append(" AND STATE = ?");
			param.add(state);
		}
		if (resourcePoolsId > 0) {
			sql.append(" and resource_pools_id = " + resourcePoolsId);
		}
		if (zoneId > 0) {
			sql.append(" and zone_id = " + zoneId);
		}
		sql.append(" order by ID desc ");
		if (curPage > 0 && pageSize > 0) {
			sql.append(" LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql.toString(), param.toArray(), new VMTemplateMapper());
	}
	
	public List<TTemplateVMBO> listTemplateNotSpecial(int type, int state, int curPage, int pageSize, int resourcePoolsId, int zoneId) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		//把特殊模板id=-1的模板隐藏
		sql.append("SELECT * FROM T_SCS_TEMPLATE_VM WHERE 1 = 1 and id>0 and SPECIAL=0 ");
		if (type > 0) {
			sql.append(" AND TYPE = ?");
			param.add(type);
		}
		if (state > 0) {
			sql.append(" AND STATE = ?");
			param.add(state);
		}
		if (resourcePoolsId > 0) {
			sql.append(" and resource_pools_id = " + resourcePoolsId);
		}
		if (zoneId > 0) {
			sql.append(" and zone_id = " + zoneId);
		}
		sql.append(" order by ID desc ");
		if (curPage > 0 && pageSize > 0) {
			sql.append(" LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql.toString(), param.toArray(), new VMTemplateMapper());
	}
	

	@Override
	public List<TTemplateVMBO> listTemplateByType(int type, int state, int templateId, int cpu_num, int memory_size) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		// to fix bug [1951]

		sql.append("SELECT * FROM T_SCS_TEMPLATE_VM WHERE 1 = 1 ");
		if (type > 0) {
			sql.append(" AND TYPE = ?");
			param.add(type);
		}
		if (state > 0) {
			sql.append(" AND STATE = ?");
			param.add(state);
		}

		if (cpu_num > 0) {
			sql.append(" AND (CPU_NUM <> ?");
			param.add(cpu_num);
		}
		if (memory_size > 0) {
			sql.append(" OR MEMORY_SIZE <> ?");
			param.add(memory_size);
		}

		if (templateId > 0) {
			sql.append(") AND STORE_TYPE in (SELECT  STORE_TYPE  FROM T_SCS_TEMPLATE_VM where id = ?)");
			param.add(templateId);
		}

		sql.append(" group by CPU_NUM,MEMORY_SIZE ");
		return getJdbcTemplate().query(sql.toString(), param.toArray(), new VMTemplateMapper());
	}

	@Override
	public List<TTemplateVMBO> listTemplateByType(int type, int state, int templateId, int cpu_num, int memory_size, String osDesc) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		// to fix bug [1951]

		sql.append("SELECT * FROM T_SCS_TEMPLATE_VM WHERE 1 = 1 ");
		if (type > 0) {
			sql.append(" AND TYPE = ?");
			param.add(type);
		}
		if (state > 0) {
			sql.append(" AND STATE = ?");
			param.add(state);
		}
		// fix bug 3970
		if (StringUtils.isNotEmpty(osDesc)) {
			sql.append(" AND VMOS = ?");
			param.add(osDesc);
		}
		if (cpu_num > 0) {
			sql.append(" AND (CPU_NUM <> ?");
			param.add(cpu_num);
		}
		if (memory_size > 0) {
			sql.append(" OR MEMORY_SIZE <> ?");
			param.add(memory_size);
		}
		if (templateId > 0) {
			sql.append(") AND STORE_TYPE in (SELECT  STORE_TYPE  FROM T_SCS_TEMPLATE_VM where id = ?)");
			param.add(templateId);
		}
		sql.append(" group by CPU_NUM,MEMORY_SIZE ");
		return getJdbcTemplate().query(sql.toString(), param.toArray(), new VMTemplateMapper());
	}

	@Override
	public List<TTemplateVMBO> listTemplateOM(int storageSize) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT * FROM T_SCS_TEMPLATE_VM where type = '11' and STORAGE_SIZE > ? ");

		param.add(storageSize);

		return getJdbcTemplate().query(sql.toString(), param.toArray(), new VMTemplateMapper());
	}

	@Override
	public List<TTemplateVMBO> listTemplateDISK(int storageSize) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT * FROM T_SCS_TEMPLATE_VM where type = '12' and STATE = '2' and STORAGE_SIZE > ?  group by STORAGE_SIZE");

		param.add(storageSize);

		return getJdbcTemplate().query(sql.toString(), param.toArray(), new VMTemplateMapper());
	}

	@Override
	public int listTemplateCount(int type, int state) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_VM WHERE 1 = 1";
		if (type > 0) {
			sql += " AND TYPE = ?";
			param.add(type);
		}
		if (state > 0) {
			sql += " AND STATE = ?";
			param.add(state);
		}
		return getJdbcTemplate().queryForObject(sql, param.toArray(), Integer.class);
	}

	@Override
	public void updateTemplate(final TTemplateVMBO template) throws Exception {
		String sql = "UPDATE T_SCS_TEMPLATE_VM set RESOURCE_POOLS_ID = ?, TEMPLATE_DESC = ?, E_SERVICE_ID = ?, CPU_NUM = ?, MEMORY_SIZE = ?, E_DISK_ID = ?,"
		        + " OS_DISK_TYPE = ?, OS_SIZE = ?, VETH_ADAPTOR_NUM = ?, VSCSI_ADAPTOR_NUM = ?, VMOS = ? ,RESOURCE_TEMPLATE = ? " + "WHERE ID = ?";
		getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, template.getResourcePoolsId());
				ps.setString(2, template.getTemplateDesc());
				ps.setInt(3, template.geteServiceId());
				ps.setInt(4, template.getCpuNum());
				ps.setInt(5, template.getMemorySize());
				ps.setInt(6, template.geteDiskId());
				ps.setString(7, template.getOsDiskType());
				ps.setInt(8, template.getOsSize());
				ps.setInt(9, template.getVethAdaptorNum());
				ps.setInt(10, template.getVscsiAdaptorNum());
				ps.setString(11, template.getVmos());
				ps.setInt(12, template.getId());
			}
		});
	}

	@Override
	public void updateTemplate_VDC(final TTemplateVMBO template) throws Exception {
		StringBuilder strb = new StringBuilder();
		strb.append(" UPDATE T_SCS_TEMPLATE_VM set ");
		// strb.append("TYPE=?, ");
		// strb.append("CODE=?, ");
		strb.append("RESOURCE_POOLS_ID=?, ");
		strb.append("TEMPLATE_DESC=?, ");
		// strb.append("CREATOR_USER_ID=?, ");
		// strb.append("CREATE_TIME=?, ");
		strb.append("E_SERVICE_ID=?,");
		strb.append("CPU_NUM=?, ");
		strb.append("MEMORY_SIZE=?, ");
		strb.append("E_DISK_ID=?, ");
		strb.append("OS_DISK_TYPE=?, ");
		strb.append("OS_SIZE=?, ");
		strb.append("VETH_ADAPTOR_NUM=?, ");
		strb.append("VSCSI_ADAPTOR_NUM=?, ");
		strb.append("VMOS=?, ");
		strb.append("CPUFREQUENCY=?, ");
		strb.append("STATE=?,");
		strb.append("STORAGE_SIZE=?, ");
		strb.append("E_OS_ID=?,");
		strb.append("NETWORK_DESC=?,");
		strb.append("RESOURCE_TEMPLATE=?,");
		strb.append("OPER_TYPE=?,");
		strb.append("MEASURE_MODE=?,");
		strb.append("GRADE=?,");
		strb.append("EXTEND_ATTR_JSON=?,");
		// strb.append("ERROR_CODE=?");
		strb.append("STORE_TYPE=?,");
		strb.append("LB_PROTOCOL=?,");
		strb.append("LB_POLICY=?,");
		strb.append("LB_PORT=?,");
		// 1.3功能，增加资源域zoneId
		strb.append("ZONE_ID=?");
		// 1.3功能，增加special
		// strb.append("SPECIAL=?");

		strb.append(" WHERE ID = ? ");
		getJdbcTemplate().update(strb.toString(), new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, template.getResourcePoolsId());
				ps.setString(2, template.getTemplateDesc());
				ps.setInt(3, template.geteServiceId());
				ps.setInt(4, template.getCpuNum());
				ps.setInt(5, template.getMemorySize());
				ps.setInt(6, template.geteDiskId());
				ps.setString(7, template.getOsDiskType());
				ps.setInt(8, template.getOsSize());
				ps.setInt(9, template.getVethAdaptorNum());
				ps.setInt(10, template.getVscsiAdaptorNum());
				ps.setString(11, template.getVmos());
				ps.setFloat(12, template.getCpufrequency());
				ps.setInt(13, template.getState());
				ps.setInt(14, template.getStorageSize());
				ps.setInt(15, template.geteOsId());
				ps.setString(16, template.getNetworkDesc());
				ps.setString(17, template.getResourceTemplate());
				ps.setInt(18, template.getOperType());
				ps.setString(19, template.getMeasureMode());
				ps.setString(20, template.getGrade());
				ps.setString(21, template.getExtendAttrJSON());
				ps.setString(22, template.getStoreType());
				ps.setString(23, template.getProtocol());
				ps.setString(24, template.getPolicy());
				ps.setInt(25, template.getPort());
				ps.setInt(26, template.getZoneId());
				ps.setInt(27, template.getId());

				// ps.setInt(28, template.getSpecial());
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TTemplateVMBO> searchTemplate(String name, int type, int state, int cpuNum, int memSize, int osId, int storageSize,
	        String networkDesc, int curPage, int pageSize) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT * FROM T_SCS_TEMPLATE_VM WHERE 1 = 1";
		if (StringUtils.isNotBlank(name)) {
			sql += " AND TEMPLATE_DESC like ?";
			param.add("%" + name.replaceAll("_", "\\\\_") + "%");
		}
		if (type > 0) {
			sql += " AND TYPE = ?";
			param.add(type);
		}
		if (state > 0) {
			sql += " AND STATE = ?";
			param.add(state);
		}
		if (cpuNum > 0) {
			sql += " AND CPU_NUM = ?";
			param.add(cpuNum);
		}
		if (memSize > 0) {
			sql += " AND MEMORY_SIZE = ?";
			param.add(memSize);
		}
		if (osId > 0) {
			sql += " AND E_OS_ID = ?";
			param.add(osId);
		}
		if (storageSize > 0) {
			sql += " AND STORAGE_SIZE = ?";
			param.add(storageSize);
		}
		// added by zhanghuizheng
		if (StringUtils.isNotBlank(networkDesc)) {
			sql += " AND NETWORK_DESC like ?";
			param.add("%" + networkDesc.replaceAll("_", "\\\\_") + "%");
		}
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql, param.toArray(), new VMTemplateMapper());
	}

	@Override
	public int searchTemplateCount(String name, int type, int state, int cpuNum, int memSize, int osId, int storageSize, String networkDesc)
	        throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_VM WHERE 1 = 1";
		if (StringUtils.isNotBlank(name)) {
			sql += " AND TEMPLATE_DESC like ?";
			param.add("%" + name.replaceAll("_", "\\\\_") + "%");
		}
		if (type > 0) {
			sql += " AND TYPE = ?";
			param.add(type);
		}
		if (state > 0) {
			sql += " AND STATE = ?";
			param.add(state);
		}
		if (cpuNum > 0) {
			sql += " AND CPU_NUM = ?";
			param.add(cpuNum);
		}
		if (memSize > 0) {
			sql += " AND MEMORY_SIZE = ?";
			param.add(memSize);
		}
		if (osId > 0) {
			sql += " AND E_OS_ID = ?";
			param.add(osId);
		}
		if (storageSize > 0) {
			sql += " AND STORAGE_SIZE = ?";
			param.add(storageSize);
		}
		// added by zhanghuizheng
		if (StringUtils.isNotBlank(networkDesc)) {
			sql += " AND NETWORK_DESC like ?";
			param.add("%" + networkDesc.replaceAll("_", "\\\\_") + "%");
		}
		return getJdbcTemplate().queryForObject(sql, param.toArray(), Integer.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.skycloud.management.portal.front.resources.dao.VMTemplateDao#
	 * queryVMTemplateCPUAndMemoryAvailableList()
	 */
	@Override
	public List<TemplateVMBO> queryVMTemplateCpuAndMemAvailableList(String type) throws SCSException {
		BeanPropertyRowMapper<TemplateVMBO> argTypes = new BeanPropertyRowMapper<TemplateVMBO>(TemplateVMBO.class);
		List<TemplateVMBO> list = new ArrayList<TemplateVMBO>();
		try {
			list = getJdbcTemplate().query("select DISTINCT " + type + " from T_SCS_TEMPLATE_VM where state='2' and type='1' ", argTypes);
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_DESC);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.skycloud.management.portal.front.resources.dao.VMTemplateDao#
	 * queryVMTemplateMonitor()
	 */
	@Override
	public List<TemplateVMBO> queryVMTemplateMonitor() throws SCSException {
		BeanPropertyRowMapper<TemplateVMBO> argTypes = new BeanPropertyRowMapper<TemplateVMBO>(TemplateVMBO.class);
		List<TemplateVMBO> list = new ArrayList<TemplateVMBO>();
		try {
			list = getJdbcTemplate().query("select ID,TEMPLATE_DESC,NETWORK_DESC from T_SCS_TEMPLATE_VM where state='2' and type='5' ", argTypes);
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_DESC);
		}
		return list;
	}

	@Override
	public boolean checkNameUniqueness(String name) throws Exception {
		// to fix bug:2535
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_VM WHERE TEMPLATE_DESC = BINARY ?";
		return jdbcTemplate.queryForInt(sql, name) == 0;
	}

	@Override
	public boolean checkMonitorResources(String resourceTypes) throws Exception {
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_VM WHERE TYPE=5 and STATE!=5 and NETWORK_DESC = ?";
		return jdbcTemplate.queryForInt(sql, resourceTypes) == 0;
	}

	@Override
	public boolean getTemplateIsBindProduction(int template, int templateType) throws DataAccessException {
		//服务模板定义的时候就使用了资源模板
		String sql = "SELECT count(0) FROM T_SCS_PRODUCT WHERE TEMPLATE_ID = ? AND TYPE = ? AND STATE != ? and state != 1 ";
		Object[] args = new Object[] { template, templateType, 4 };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER };
		return jdbcTemplate.queryForInt(sql, args, argTypes) > 0;
	}

	@Override
	public boolean getTemplateIsBindProduction2(int template, int templateType) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(0) FROM T_SCS_PRODUCT p,T_SCS_PRODUCT_TEMPLATE_RELATION r ");
		sql.append(" WHERE p.ID=r.PRODUCT_ID and r.TEMPLATE_ID = ? AND p.TYPE = ? AND p.STATE != ? AND p.STATE != 1 ");
		Object[] args = new Object[] { template, templateType, 4 };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER };
		return jdbcTemplate.queryForInt(sql.toString(), args, argTypes) > 0;
	}

	@Override
	public String getMonitorResourcesById(int id) throws Exception {
		String sql = "SELECT NETWORK_DESC FROM T_SCS_TEMPLATE_VM WHERE TYPE=5 and STATE!=5 and id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new int[] { Types.INTEGER }, String.class);
	}

	/**
	 * 搜索物理机模板的全部操作系统列表 创建人： ninghao 创建时间：2012-8-28 09:53:55
	 *
	 * @return 操作系统列表
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getPhysicalTemplateOS(int type) throws Exception {

		String sql = "SELECT ID,OS_NAME NAME FROM T_SCS_PHYSICAL_OSTYPE WHERE 1=1 and STATE='Active'";
		List<Map<String, Object>> oslist = jdbcTemplate.queryForList(sql);

		return oslist;
	}

	@Override
	public int updateTemplateByOrderModify(final TTemplateVMBO template) throws Exception {
		int excuteUpdate = -1;
		StringBuilder sbSQL = new StringBuilder();
		sbSQL.append(" UPDATE T_SCS_TEMPLATE_VM set ");
		// strb.append("TYPE=?, ");
		// strb.append("CODE=?, ");
		sbSQL.append("RESOURCE_POOLS_ID=?, ");
		sbSQL.append("TEMPLATE_DESC=?, ");
		// strb.append("CREATOR_USER_ID=?, ");
		// strb.append("CREATE_TIME=?, ");
		sbSQL.append("E_SERVICE_ID=?,");
		sbSQL.append("CPU_NUM=?, ");
		sbSQL.append("MEMORY_SIZE=?, ");
		sbSQL.append("E_DISK_ID=?, ");
		sbSQL.append("OS_DISK_TYPE=?, ");
		sbSQL.append("OS_SIZE=?, ");
		sbSQL.append("VETH_ADAPTOR_NUM=?, ");
		sbSQL.append("VSCSI_ADAPTOR_NUM=?, ");
		sbSQL.append("VMOS=?, ");
		sbSQL.append("CPUFREQUENCY=?, ");
		sbSQL.append("STATE=?,");
		sbSQL.append("STORAGE_SIZE=?, ");
		sbSQL.append("E_OS_ID=?,");
		sbSQL.append("NETWORK_DESC=?,");
		sbSQL.append("RESOURCE_TEMPLATE=?,");
		sbSQL.append("OPER_TYPE=?,");
		sbSQL.append("MEASURE_MODE=?,");
		sbSQL.append("GRADE=?,");
		sbSQL.append("EXTEND_ATTR_JSON=?,");
		// strb.append("ERROR_CODE=?");
		sbSQL.append("STORE_TYPE=?,");
		sbSQL.append("LB_PROTOCOL=?,");
		sbSQL.append("LB_POLICY=?,");
		sbSQL.append("LB_PORT=?,");
		// 1.3功能，增加资源域zoneId
		sbSQL.append("ZONE_ID=?");
		// 1.3功能，增加special
		// strb.append("SPECIAL=?");

		sbSQL.append(" WHERE ID = ? ");
		excuteUpdate = getJdbcTemplate().update(sbSQL.toString(), new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, template.getResourcePoolsId());
				ps.setString(2, template.getTemplateDesc());
				ps.setInt(3, template.geteServiceId());
				ps.setInt(4, template.getCpuNum());
				ps.setInt(5, template.getMemorySize());
				ps.setInt(6, template.geteDiskId());
				ps.setString(7, template.getOsDiskType());
				ps.setInt(8, template.getOsSize());
				ps.setInt(9, template.getVethAdaptorNum());
				ps.setInt(10, template.getVscsiAdaptorNum());
				ps.setString(11, template.getVmos());
				ps.setFloat(12, template.getCpufrequency());
				ps.setInt(13, template.getState());
				ps.setInt(14, template.getStorageSize());
				ps.setInt(15, template.geteOsId());
				ps.setString(16, template.getNetworkDesc());
				ps.setString(17, template.getResourceTemplate());
				ps.setInt(18, template.getOperType());
				ps.setString(19, template.getMeasureMode());
				ps.setString(20, template.getGrade());
				ps.setString(21, template.getExtendAttrJSON());
				ps.setString(22, template.getStoreType());
				ps.setString(23, template.getProtocol());
				ps.setString(24, template.getPolicy());
				ps.setInt(25, template.getPort());
				ps.setInt(26, template.getZoneId());
				ps.setInt(27, template.getId());

				// ps.setInt(28, template.getSpecial());
			}
		});

		return excuteUpdate;
	}
}
