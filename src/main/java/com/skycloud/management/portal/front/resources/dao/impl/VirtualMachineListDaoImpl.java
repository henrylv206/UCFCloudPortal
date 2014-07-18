/**
 * 2011-11-23 下午01:37:28 $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;

/**
 * @author shixq
 * @version $Revision$ 下午04:17:20
 */
public class VirtualMachineListDaoImpl implements VirtualMachineListDao {

	private static Log log = LogFactory.getLog(VirtualMachineListDaoImpl.class);

	private JdbcTemplate jt;

	private ICommandService commandService;

	private JSONObject queryVMList4Elaster(String instanceId, int resourcePoolsID) throws SCSException {
		ListVirtualMachines elasterVmList = new ListVirtualMachines();
		if (StringUtils.isNotBlank(instanceId)) {
			elasterVmList.setId(Long.valueOf(instanceId));
		}
		Object response = null;
		JSONObject resultObject = null;
		try {
			response = commandService.executeAndJsonReturn(elasterVmList, resourcePoolsID);
			if (response == null) {
				return resultObject;
			}
			JSONObject jsonRes = JSONObject.fromObject(response);
			String result = jsonRes.getString("listvirtualmachinesresponse");
			resultObject = JSONObject.fromObject(result);
		}
		catch (Exception e) {
			throw new SCSException(e.getMessage());
		}
		return resultObject;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao
	 * #queryVMList()
	 */
	@Override
	public List<ResourcesVO> queryVMList(int resourcePoolsID) throws SCSException {
		BeanPropertyRowMapper<ResourcesVO> argTypes = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);
		StringBuffer sql = new StringBuffer("SELECT i.E_INSTANCE_ID,i.ID,i.STATE,t.RESOURCE_POOLS_ID ");
		sql.append("FROM T_SCS_INSTANCE_INFO i,T_SCS_TEMPLATE_VM t ");
		sql.append("where i.TEMPLATE_ID = t.id and i.TEMPLATE_TYPE='1' ");
		sql.append("and i.STATE in ('2','5') and t.TYPE='1' and t.RESOURCE_POOLS_ID=? ");

		Object[] args = new Object[] { resourcePoolsID };
		List<ResourcesVO> list = new ArrayList<ResourcesVO>();
		try {
			list = jt.query(sql.toString(), args, argTypes);
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_DESC);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.front.resources.dao.VirtualMachineListDao
	 * #instanceInfoList4Elaster(java.util.List)
	 */
	@Override
	public JSONObject instanceInfoList4Elaster(String instanceId, int resourcePoolsID) throws SCSException {
		return queryVMList4Elaster(instanceId, resourcePoolsID);
	}

	@Override
	public JSONObject instanceInfoList4Elaster(int resourcePoolsID) throws SCSException {
		return queryVMList4Elaster(null, resourcePoolsID);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.web.dao.virtualmachine.VirtualMachineDao#queryVirtualMachineList
	 * (
	 * com.skycloud.json.hostandcluster.action.vo.VirtualMachineTemplateQueryVO)
	 */
	@Override
	public List<TemplateVMBO> queryVirtualMachineList() throws SCSException {
		BeanPropertyRowMapper<TemplateVMBO> argTypes = new BeanPropertyRowMapper<TemplateVMBO>(TemplateVMBO.class);
		StringBuffer buffer = new StringBuffer();
		buffer.append("select ID,CODE,RESOURCE_POOLS_ID,TYPE,TEMPLATE_DESC,CPUFREQUENCY,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,OS_DISK_TYPE,");
		buffer.append("OS_SIZE,VETH_ADAPTOR_NUM,VSCSI_ADAPTOR_NUM,VMOS,STATE,CREATOR_USER_ID,CREATE_TIME,E_SERVICE_ID,E_DISK_ID,E_NETWORK_ID,");
		buffer.append("NETWORK_DESC,E_OS_ID,OS_DESC from T_SCS_TEMPLATE_VM WHERE   STATE ='1' ");
		List<TemplateVMBO> list = new ArrayList<TemplateVMBO>();
		try {
			list = jt.query(buffer.toString(), argTypes);
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_TEMPLATE_VM_COUNT_DESC);
		}
		return list;
	}

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

}
