package com.skycloud.management.portal.admin.template.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.dao.IMCTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.service.IMCTemplateService;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;

/**
 * 小型机模板业务实现
 * 
 * @author jiaoyz
 */
public class MCTemplateServiceImpl implements IMCTemplateService {

	private IMCTemplateDao MCTemplateDao;

	private ISysParametersDao parametersDao;

	private IProductDao productDao;

	@Override
	public int createTemplate(TTemplateMCBO template) throws Exception {
		if (template == null) {
			throw new Exception("Paramater is missing");
		}
		if (template.getId() > 0) {
			throw new Exception("Paramater is invalid");
		}
		//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
		//String autoConfirm = parametersDao.getParameter("AUTO_CONFIRM");
		// if(autoConfirm != null && autoConfirm.intValue() == 1) {
		// template.setState(ConstDef.STATE_TWO);
		// }
		// else {
		template.setState(ConstDef.STATE_ONE);
		// }
		// template.setState(ConstDef.STATE_TWO);
		return MCTemplateDao.createTemplate(template);
	}
	/**
	 * 增加小型机模板和产品
	 */
	@Override
	public int insertTemplateAndProduct(TTemplateMCBO template,Product product) throws Exception {
		if (template == null) {
			throw new Exception("Paramater is missing");
		}
		if (template.getId() > 0) {
			throw new Exception("Paramater is invalid");
		}
		//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
		String autoConfirm = parametersDao.getParameter("AUTO_CONFIRM");
		// if(autoConfirm != null && autoConfirm.intValue() == 1) {
		// template.setState(ConstDef.STATE_TWO);
		// }
		// else {
		template.setState(ConstDef.STATE_ONE);
		// }
		// template.setState(ConstDef.STATE_TWO);
		int template_id = -1;

		template_id = MCTemplateDao.createTemplate(template);
		//增加产品
		//设置模板id
		product.setTemplateId(template_id);
		//设置模板类型type
		product.setType(3);
		int result = productDao.save(product);
		product.setId(result);

		return template_id;

	}


	@Override
	public void deleteTemplate(int id) throws Exception {
		if (id < 1) {
			throw new Exception("Paramater is invalid");
		}
		MCTemplateDao.deleteTemplate(id,ConstDef.STATE_ONE);
	}

	@Override
	public TTemplateMCBO getTemplateById(int id) throws Exception {
		if (id == 0) {
			throw new Exception("Paramater is invalid");
		}
		TTemplateMCBO mc = MCTemplateDao.getTemplateById(id);
		String stateName = "";
		if(null != mc){
			int state = mc.getState();
			if (state == 1) {
				String operTypeName = "";
				operTypeName = ConstDef.OPER_TYPE_MAP.get(mc.getOperType());
				if (null != operTypeName && !operTypeName.equals("")) {
					stateName = operTypeName
					+ ConstDef.TEMPLATE_STATE_MAP.get(state);
				} else {
					stateName = ConstDef.TEMPLATE_STATE_MAP.get(state);
				}

			} else {
				stateName = ConstDef.TEMPLATE_STATE_MAP.get(state);
			}
			mc.setStateName(stateName);
		}
		return mc;
	}

	@Override
	public List<TTemplateMCBO> listTemplate(int state, int curPage, int pageSize)
	throws Exception {
		List<TTemplateMCBO> list = MCTemplateDao.listTemplate(state, curPage,
		                                                      pageSize);
		transStateName(list);
		return list;
	}
	
	public List<TTemplateMCBO> listTemplateNotSpecial(int state, int curPage, int pageSize)
	throws Exception {
		List<TTemplateMCBO> list = MCTemplateDao.listTemplateNotSpecial(state, curPage,
		                                                      pageSize);
		transStateName(list);
		return list;
	}
	

	public void transStateName(List<TTemplateMCBO> list){
		if (null != list && list.size() > 0) {
			for (TTemplateMCBO mc : list) {
				String stateName = "";
				int _state = mc.getState();
				if (_state == 1) {
					String operTypeName = "";
					operTypeName = ConstDef.OPER_TYPE_MAP.get(mc.getOperType());
					if (null != operTypeName && !operTypeName.equals("")) {
						stateName = operTypeName
						+ ConstDef.TEMPLATE_STATE_MAP.get(_state);
					} else {
						stateName = ConstDef.TEMPLATE_STATE_MAP.get(_state);
					}

				} else {
					stateName = ConstDef.TEMPLATE_STATE_MAP.get(_state);
				}
				mc.setStateName(stateName);
			}
		}

	}

	@Override
	public int listTemplateCount(int state) throws Exception {
		return MCTemplateDao.listTemplateCount(state);
	}

	@Override
	public List<TTemplateMCBO> searchTemplate(String name, int type, int state,
	                                          int curPage, int pageSize) throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		//to fix bug:2952
		List<TTemplateMCBO> list = MCTemplateDao.searchTemplate(name, type, state,
		                                                        Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                                        Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                                        Constants.STATUS_COMMONS.IGNORE.getValue(), curPage, pageSize);

		transStateName(list);
		return list;
	}

	/**
	 * 特殊模板申请创建申请模板
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	@Override
	public TTemplateMCBO creatSpecalMCTemplate(TVmInfo vminfo,TUserBO user,TTemplateMCBO template,Product product) throws SCSException
	{

		//根据用户选择定义信息组织要创建模板信息
		if(vminfo != null && template != null && product != null && user != null){
			template.setCode(null);//模板编码
			template.setType(vminfo.getTemplateType());
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);//特殊模板 1：管理员定义的模板，2：用户定义的特殊模板
			template.setResourcePoolsId(vminfo.getPoolId());
			if(vminfo.getZoneId() > 0){
				template.setZoneId(vminfo.getZoneId());
			}
			template.setCputype(vminfo.getVstype());
			template.setCpuNum(vminfo.getCpuNum());
			template.setCpufrequency(vminfo.getCpufrequency());
			template.setMemorySize(vminfo.getMemorySize());
			template.setStorageSize(vminfo.getStorageSize());
			vminfo.setDisknumber(1);
			template.setVethAdaptorNum(1);//网卡个数 TODO
			if(user != null){
				template.setCreatorUserId(user.getId());
			}
			template.setOperType(1);
			Timestamp ts = new Timestamp(new Date().getTime());
			template.setCreateTime(ts.toString());
			template.setMeasureMode("Duration");//Duration：按时长计量
			template.setState(1);
			//			template.seteOsId();
			//			template.setOsDesc(osVO.getName());

			//根据模板组织要创建服务信息
			product.setName("MC");
			product.setCreateDate(ts);
			product.setType(template.getType());
			product.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
			product.setDescription("小型机自动创建服务");
			product.setSpecification(product.getDescription());
			product.setQuotaNum(1);//待确定 TODO
			product.setPrice(0f);
			product.setUnit(vminfo.getUnit());
			product.setOperateType(1);
			product.setIsDefault(0);//是否首页推荐，0否，1是
		}

		return template;
	}


	@Override
	public int searchTemplateCount(String name, int type, int state)
	throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		return MCTemplateDao.searchTemplateCount(name, type, state,
		                                         Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                         Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                         Constants.STATUS_COMMONS.IGNORE.getValue());
	}

	public IMCTemplateDao getMCTemplateDao() {
		return MCTemplateDao;
	}

	public void setMCTemplateDao(IMCTemplateDao mCTemplateDao) {
		MCTemplateDao = mCTemplateDao;
	}

	public ISysParametersDao getParametersDao() {
		return parametersDao;
	}

	public void setParametersDao(ISysParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}

	@Override
	public boolean checkNameUniqueness(String name) throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		return MCTemplateDao.checkNameUniqueness(name);
	}

	@Override
	public List<TTemplateMCBO> advancedSearch(String name, int type, int state,
	                                          int cpuNum, int memSize, int storageSize, int curPage, int pageSize)
	                                          throws Exception {
		List<TTemplateMCBO> list = MCTemplateDao.searchTemplate(name, type, state, cpuNum, memSize,
		                                                        storageSize, curPage, pageSize);
		transStateName(list);
		return list;
	}

	@Override
	public int advancedSearchCount(String name, int type, int state,
			int cpuNum, int memSize, int storageSize) throws Exception {
		return MCTemplateDao.searchTemplateCount(name, type, state, cpuNum,
		                                         memSize, storageSize);
	}

	@Override
	public int upateTemplateState(int templateId, int state)
	throws DataAccessException{
		return MCTemplateDao.upateTemplateState(templateId, state);
	}

	@Override
	public void updateTemplate(TTemplateMCBO template) throws Exception {
		MCTemplateDao.updateTemplate(template);

	}
	public IProductDao getProductDao() {
		return productDao;
	}
	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

}
