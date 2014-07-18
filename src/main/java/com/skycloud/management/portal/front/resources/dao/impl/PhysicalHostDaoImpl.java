package com.skycloud.management.portal.front.resources.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skycloud.management.portal.front.resources.action.vo.PhysicalHostVO;
import com.skycloud.management.portal.front.resources.dao.PhysicalHostDao;

public class PhysicalHostDaoImpl implements PhysicalHostDao{

	private static final Logger logger = Logger.getLogger(PhysicalHostDaoImpl.class);
	private JdbcTemplate jt;
	private String tableName = "T_SCS_PHYSICAL_HOST";
	private String tableColumns = "ID,IPMI_IP,IPMI_USERNAME,IPMI_PASSWORD,NETWORK_CARD_ID,NETWORK_CARD_MAC,NETWORK_IP,NETWORK_MASK,NETWORK_DNS,HOST_NAME,OS_LOGIN_USER,OS_LOGIN_PASSWORD,OS_TYPE,ZONE_ID,POD_ID,CLUSTER_ID,STATE,CREATE_TIME,UPDATE_TIME";
//	private String tableColumnsForInsert = "IPMI_IP,IPMI_USERNAME,IPMI_PASSWORD,NETWORK_CARD_ID,NETWORK_CARD_MAC,NETWORK_IP,NETWORK_SUBNET,NETWORK_MASK,NETWORK_DNS,HOST_NAME,OS_LOGIN_USER,OS_LOGIN_PASSWORD,OS_TYPE,ZONE_ID,POD_ID,CLUSTER_ID,STATE,CREATE_TIME,UPDATE_TIME";

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}
	
	@Override
	public PhysicalHostVO findById(Long id){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT "+tableColumns+" FROM "+tableName+" WHERE ID = ? ");
	    BeanPropertyRowMapper<PhysicalHostVO> argTypes = new BeanPropertyRowMapper<PhysicalHostVO>(PhysicalHostVO.class);
	    logger.debug(sb.toString()+" | paras: " + id);
		return jt.queryForObject(sb.toString(),new Object[]{id},argTypes);
	}


	/**
	 * 根据条件查询物理机信息列表
	 * ninghao@chinaskycloud.com
	 * 2012-12-03
	 * @param pmHostVO
	 * @return List
	 */
	@Override
	public List<PhysicalHostVO> findPhysicalHostByOption(PhysicalHostVO pmHostVO) throws Exception
	{
		List<PhysicalHostVO> ph_list = null;

		StringBuffer sb_sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		
		sb_sql.append("SELECT ").append(tableColumns);
		sb_sql.append("  FROM ").append(tableName);
		sb_sql.append(" WHERE 1=1 ");
		if(pmHostVO != null){
			//按照状态查询物理机：比如STATE=Active表示可申请
			if(pmHostVO.getState() != null){
				sb_sql.append(" AND STATE = ? ");
				param.add(pmHostVO.getState());
			}
			if(pmHostVO.getOstype() != null){
				sb_sql.append(" AND OS_TYPE = ? ");
				param.add(pmHostVO.getOstype());
			}
			if(pmHostVO.getZoneid() != null){
				sb_sql.append(" AND ZONE_ID = ? ");
				param.add(pmHostVO.getZoneid());
			}
			if(pmHostVO.getPodid() != null){
				sb_sql.append(" AND POD_ID = ? ");
				param.add(pmHostVO.getPodid());
			}
			if(pmHostVO.getClusterid() != null){
				sb_sql.append(" AND CLUSTER_ID = ? ");
				param.add(pmHostVO.getClusterid());
			}
			if(pmHostVO.getName() != null){
				sb_sql.append(" AND HOST_NAME = ? ");
				param.add(pmHostVO.getName());
			}
			if(pmHostVO.getId() != null){
				sb_sql.append(" AND ID = ? ");
				param.add(pmHostVO.getId());
			}
		}
		sb_sql.append(" ");
		sb_sql.append(" ");
	    BeanPropertyRowMapper<PhysicalHostVO> argTypes = new BeanPropertyRowMapper<PhysicalHostVO>(PhysicalHostVO.class);
	    logger.debug(sb_sql.toString()+" | param: " + param.toString());

	    try {
	    	ph_list = jt.query(sb_sql.toString(), param.toArray(), argTypes);
			
	    	logger.info("findPhysicalHostByOption size==" + ph_list == null ? 0 : ph_list.size());
	    } catch (Exception e) {
	    	logger.error(e);
	    	throw e;
	    }
	    
		return ph_list;
	}
}
