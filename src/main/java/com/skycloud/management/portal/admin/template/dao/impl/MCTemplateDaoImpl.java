package com.skycloud.management.portal.admin.template.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.template.dao.IMCTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

/**
 * 小机模板持久化实现
 * 
 * @author jiaoyz
 */
public class MCTemplateDaoImpl extends SpringJDBCBaseDao implements
		IMCTemplateDao {

	private static final RowMapperResultSetExtractor<TTemplateMCBO> MCTemplateExtractor = new RowMapperResultSetExtractor<TTemplateMCBO>(
			new BeanPropertyRowMapper<TTemplateMCBO>(TTemplateMCBO.class));

	@Override
	public int createTemplate(final TTemplateMCBO template) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO T_SCS_TEMPLATE_MC( " 
				+ " TYPE, CODE, RESOURCE_POOLS_ID, TEMPLATE_DESC, CREATOR_USER_ID " 
				+ ",CREATE_TIME, CPU_NUM, MEMORY_SIZE "
				+ ",STORAGE_SIZE, STATE, VMOS,OPER_TYPE,MEASURE_MODE "
				+ ",CPUFREQUENCY,CPUTYPE,ZONE_ID,SPECIAL )" +
				" VALUES (?, ?, ?, ?, ?,      ?, ?, ?,    ?, ?, ?, ?, ?,    ?, ?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1; 
				ps.setInt(index++,    template.getType());
				ps.setString(index++, template.getCode());
				ps.setInt(index++,    template.getResourcePoolsId());
				ps.setString(index++, template.getTemplateDesc());
				ps.setInt(index++,    template.getCreatorUserId());
				
				ps.setString(index++, template.getCreateTime());
				ps.setInt(index++,    template.getCpuNum());
				ps.setInt(index++,    template.getMemorySize());
				
				ps.setInt(index++,    template.getStorageSize());
				ps.setInt(index++,    template.getState());
				ps.setString(index++, template.getVmos());
				ps.setInt(index++,    template.getOperType());
				ps.setString(index++, template.getMeasureMode());
				
				ps.setFloat(index++,  template.getCpufrequency());
				ps.setString(index++, template.getCputype());
				ps.setInt(index++,    template.getZoneId());
				ps.setInt(index++,    template.getSpecial());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void deleteTemplate(int id, int state) throws Exception {
		// String sql = "DELETE FROM T_SCS_TEMPLATE_MC WHERE ID = ?";
		String sql = "UPDATE T_SCS_TEMPLATE_MC SET state = ?,OPER_TYPE = 3 WHERE ID = ?";
		Object[] args = new Object[] { state, id };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		getJdbcTemplate().update(sql, args, argTypes);
	}

	@Override
	public List<TTemplateMCBO> searchTemplate(String name, int type, int state,
			int cpuNum, int memSize, int storageSize, int curPage, int pageSize)
			throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT * FROM T_SCS_TEMPLATE_MC WHERE 1 = 1";
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
		if (storageSize > 0) {
			sql += " AND STORAGE_SIZE = ?";
			param.add(storageSize);
		}
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql, param.toArray(),
				MCTemplateExtractor);
	}

	@Override
	public int searchTemplateCount(String name, int type, int state,
			int cpuNum, int memSize, int storageSize) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_MC WHERE 1 = 1";
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
		if (storageSize > 0) {
			sql += " AND STORAGE_SIZE = ?";
			param.add(storageSize);
		}
		return getJdbcTemplate().queryForObject(sql, param.toArray(),
				Integer.class);
	}

	@Override
	public TTemplateMCBO getTemplateById(int id) throws Exception {
		String sql = "SELECT * FROM T_SCS_TEMPLATE_MC WHERE ID = ?";
		List<TTemplateMCBO> template = null;
		try {
			template = getJdbcTemplate().query(sql, MCTemplateExtractor, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		if (template != null && template.size() > 0) {
			return template.get(0);
		}
		return null;
	}

	@Override
	public List<TTemplateMCBO> listTemplate(int state, int curPage, int pageSize)
			throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT * FROM T_SCS_TEMPLATE_MC WHERE 1 = 1 and id>0 ");
		if (state > 0) {
			sql.append( " AND STATE = ?");
			param.add(state);
		}
		//to fix bug:2094
		sql.append(" order by ID desc ");
		if (curPage > 0 && pageSize > 0) {
			sql.append( " LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql.toString(), param.toArray(),
				MCTemplateExtractor);
	}
	
	public List<TTemplateMCBO> listTemplateNotSpecial(int state, int curPage, int pageSize)
	throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append( "SELECT * FROM T_SCS_TEMPLATE_MC WHERE 1 = 1 and id>0 and SPECIAL=0 ");
		if (state > 0) {
			sql.append( " AND STATE = ?");
			param.add(state);
		}
		//to fix bug:2094
		sql.append(" order by ID desc ");
		if (curPage > 0 && pageSize > 0) {
			sql.append( " LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql.toString(), param.toArray(),
				MCTemplateExtractor);
}
	

	@Override
	public int listTemplateCount(int state) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_MC WHERE 1 = 1";
		if (state > 0) {
			sql += " AND STATE = ?";
			param.add(state);
		}
		return getJdbcTemplate().queryForObject(sql, param.toArray(),
				Integer.class);
	}

	@Override
	public boolean checkNameUniqueness(String name) throws Exception {
		String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_MC WHERE TEMPLATE_DESC = ?";
		return jdbcTemplate.queryForInt(sql, name) == 0;
	}

	public int upateTemplateState(int templateId, int state)
			throws DataAccessException {
		String sql = "UPDATE T_SCS_TEMPLATE_MC SET STATE = ? WHERE ID = ?";
		Object[] args = new Object[] { state, templateId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		int index = 0;
		index = getJdbcTemplate().update(sql, args, argTypes);
		return index;
	}

	public void updateTemplate(final TTemplateMCBO template)
			throws Exception {
		StringBuilder strb = new StringBuilder();
		strb.append(" UPDATE T_SCS_TEMPLATE_MC set ");
		
		// strb.append("CODE=?, ");
		strb.append("RESOURCE_POOLS_ID=?, ");
		strb.append("TEMPLATE_DESC=?, ");
		// strb.append("CREATOR_USER_ID=?, ");
		// strb.append("CREATE_TIME=?, ");
//		strb.append("E_SERVICE_ID=?,");
		strb.append("CPU_NUM=?, ");
		strb.append("MEMORY_SIZE=?, ");
//		strb.append("E_DISK_ID=?, ");
//		strb.append("OS_DISK_TYPE=?, ");
//		strb.append("OS_SIZE=?, ");
//		strb.append("VETH_ADAPTOR_NUM=?, ");
//		strb.append("VSCSI_ADAPTOR_NUM=?, ");
//		strb.append("VMOS=?, ");
//		strb.append("CPUFREQUENCY=?, ");
		strb.append("STATE=?,");
		strb.append("STORAGE_SIZE=?, ");
//		strb.append("E_OS_ID=?,");
//		strb.append("NETWORK_DESC=?,");
//		strb.append("RESOURCE_TEMPLATE=?,");
		strb.append("OPER_TYPE=?,");
		strb.append("MEASURE_MODE=?,");
		strb.append("TYPE=? ");
//		strb.append("GRADE=?,");
//		strb.append("EXTEND_ATTR_JSON=?,");
		// strb.append("ERROR_CODE=?");
//		strb.append("STORE_TYPE=?");
		strb.append(" WHERE ID = ? ");
		getJdbcTemplate().update(strb.toString(),
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setInt(1, template.getResourcePoolsId());
						ps.setString(2, template.getTemplateDesc());
//						ps.setInt(3, template.geteServiceId());
						ps.setInt(3, template.getCpuNum());
						ps.setInt(4, template.getMemorySize());
//						ps.setInt(6, template.geteDiskId());
//						ps.setString(7, template.getOsDiskType());
//						ps.setInt(8, template.getOsSize());
//						ps.setInt(9, template.getVethAdaptorNum());
//						ps.setInt(10, template.getVscsiAdaptorNum());
//						ps.setString(11, template.getVmos());
//						ps.setFloat(12, template.getCpufrequency());
						ps.setInt(5, template.getState());
						ps.setInt(6, template.getStorageSize());
//						ps.setInt(15, template.geteOsId());
//						ps.setString(16, template.getNetworkDesc());
//						ps.setString(17, template.getResourceTemplate());
						ps.setInt(7, template.getOperType());
						ps.setString(8, template.getMeasureMode());
						ps.setInt(9, template.getType());
//						ps.setString(21, template.getExtendAttrJSON());
//						ps.setString(22, template.getStoreType());
						ps.setInt(10, template.getId());
					}
				});
	}

}
