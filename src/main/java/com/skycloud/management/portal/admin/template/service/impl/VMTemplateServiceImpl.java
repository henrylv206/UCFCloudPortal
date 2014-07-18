package com.skycloud.management.portal.admin.template.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.entity.TemplateCloudStoragePo;
import com.skycloud.management.portal.admin.template.entity.TemplateFireWallPO;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.admin.template.util.TemplateUtils;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

/**
 * 虚机模板业务实现
 * 
 * @author jiaoyz
 */
public class VMTemplateServiceImpl implements IVMTemplateService {

	protected final Logger logger = Logger.getLogger(VMTemplateServiceImpl.class);

	private IVMTemplateDao VMTemplateDao;

	private ISysParametersDao parametersDao;

	private AsyncJobVDCService asyncJobVDCService;
	
	private IProductDao productDao;
	
	// 服务状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线。
	public static final int STATE_WAIT_ADUIT = 1;

	public static final int STATE_WAIT_RELEASE = 2;

	public static final int STATE_RELEASED = 3;

	public static final int STATE_DELETED = 4;

	public static final int STATE_ADUIT_FAILED = 5;

	public static final int STATE_OFFLINE = 6;
	
	// 操作类型
	public static final int OPERATE_TYPE_ADD = 1;

	public static final int OPERATE_TYPE_UPDATE = 2;

	public static final int OPERATE_TYPE_DEL = 3;

	@Override
	public int createTemplate(TTemplateVMBO template) throws Exception {

		// 大家添加自己判断语句加入获取各自的TemplateId（按照规范生成的VDCID）
		if (template == null) {
			throw new Exception("Paramater is missing");
		}
		if (template.getId() > 0) {
			throw new Exception("Paramater is invalid");
		}

		// 1.1和vdc的开关
		int template_id = -1;
		if (ConstDef.curProjectId == 1) {
			// 如果是虚拟机，虚拟机备份，备份服务则按照vdc流程
			int type = template.getType();
			if (type == ConstDef.RESOURCE_TYPE_VM || type == ConstDef.RESOURCE_TYPE_STORAGE) {
				template.setState(ConstDef.STATE_ONE);
			}
//			else {
				// Integer autoConfirm =
				// parametersDao.getParameter("AUTO_CONFIRM");
				// if(autoConfirm != null && autoConfirm.intValue() == 1) {
				// template.setState(ConstDef.STATE_TWO);
				// }
				// else {
				// to fix bug:1597 
				// to fix bug 4811 4829
//				template.setState(8); 
				// }
//			}
			template_id = VMTemplateDao.createTemplate_VDC(template);
		} else if (ConstDef.curProjectId == 2) {
			template.setState(ConstDef.STATE_ONE);
			template_id = VMTemplateDao.createTemplate_VDC(template);
			template.setId(template_id);
			// 生成传给广东移动的模板xml字符串,并更新到模板表中
			this.getTemplateXml(template);
			VMTemplateDao.updateTemplate_VDC(template);
			// 调用asyncJobVDCDao往任务job表里插入记录
			this.addAsyncJobVDC(template);
		}
		return template_id;
	}	

	public void addAsyncJobVDC(TTemplateVMBO template) throws SCSException {
		AsyncJobVDCPO jobPO = new AsyncJobVDCPO();
		jobPO.setOperation(OperationType.CREATE_TEMPLATE);
		jobPO.setOperation(jobPO.getOperation().valueOf("CREATE_TEMPLATE"));
		jobPO.setUser_id(template.getCreatorUserId());
		jobPO.setTemplate_id(template.getId());
		jobPO.setParameter(template.getResourceTemplate());
		jobPO.setAuditstate(jobPO.getAuditstate().valueOf(0));
		jobPO.setTemplate_res_id(ConstDef.getVDCTemplateID(template.getType(), template.getId()));// /获取模板VDC标识
		asyncJobVDCService.insterAsyncJobVDC(jobPO);

		// 添加用户操作日志
		String functionName = getFunctionName("新建", template.getType()) + "Job";
		String parameters = "type=" + template.getType() + ",name=" + template.getTemplateDesc();
		String memo = "res_id=" + jobPO.getTemplate_res_id() + ",   xmlParameter = " + template.getResourceTemplate();
		ConstDef.saveLogInfo(1, "资源模板管理", functionName, "VMTemplateServiceImpl", "addAsyncJobVDC", parameters, null, memo);
	}

	private String getFunctionName(String operType, int type) {
		String functionName = null;
		if (type == 1) {
			functionName = operType + "虚拟机模板";
		} else if (type == 2) {
			functionName = operType + "虚拟硬盘模板";
		} else if (type == 4) {
			functionName = operType + "备份服务模板";
		} else if (type == 5) {
			functionName = operType + "监控服务模板";
		} else if (type == 6) {
			functionName = operType + "负载均衡服务模板";
		} else if (type == 7) {
			functionName = operType + "防火墙服务模板";
		} else if (type == 8) {
			functionName = operType + "公网带宽服务模板";
		} else if (type == 9) {
			functionName = operType + "公网IP服务模板";
		} else if (type == 10) {
			functionName = operType + "物理机资源模板";
		}
		return functionName;
	}

	public void getTemplateXml(TTemplateVMBO template) throws Exception {
		int templateID = template.getId();
		if (template.getType() == ConstDef.RESOURCE_TYPE_VM) {
			String xmlstr = this.getVMTemplateXML(templateID, template);
			template.setResourceTemplate(xmlstr);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_BACKUP) {
			String xmlstr = this.getVMBKTemplateXML(templateID, template);
			template.setResourceTemplate(xmlstr);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_MONITOR) {
			String xmlstr = this.getMOTemplateXML(templateID, template);
			template.setResourceTemplate(xmlstr);
			VMTemplateDao.updateTemplate_VDC(template);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_STORAGE) {
			template.setId(templateID);
			String xmlstr = this.getBSTemplateXML(templateID, template);
			template.setResourceTemplate(xmlstr);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_BANDWIDTH) {
			String xmlstr = this.getBWTemplateXML(templateID, template);
			template.setResourceTemplate(xmlstr);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_FIREWALL) {
			template.setResourceTemplate(this.getSGTemplateXML(templateID, template));
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP) {
			String xmlstr = this.getIPTemplateXML(templateID, template);
			template.setResourceTemplate(xmlstr);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_CLOUDSTORAGE) {
			TemplateCloudStoragePo tempalteCloudStorage = TemplateCloudStoragePo.getIntance(template);
			template.setResourceTemplate(TemplateCloudStoragePo.toString(tempalteCloudStorage));
			// template.setOperType(1);
		} else if (template.getType() == ConstDef.RESOURCE_TYPE_FIREWALL) {
			TemplateFireWallPO templateFireWall = TemplateFireWallPO.getIntance(template);
			template.setResourceTemplate(TemplateFireWallPO.toString(templateFireWall));
		} else {
			throw new Exception("Type is error:" + template.getType());
		}
	}

	public String getSGTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<SecurityGroupTemplate>");
		sb.append("<TemplateID>").append("CIDC-T-SG-").append(String.format("%08d", templateID)).append("</TemplateID>");
		sb.append("<ResourceType>CIDC-RT-SG</ResourceType>");
		sb.append("<MeasureMode>Duration</MeasureMode>");
		sb.append("<TemplateDesc>").append(template.getStorageSize()).append("条安全组规则</TemplateDesc>");
		sb.append("<TemplateStatus>").append(template.getState()).append("</TemplateStatus>");
		sb.append("<TemplateCreator>")
		        .append(((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getName())
		        .append("</TemplateCreator>");
		sb.append("<CreateTime>").append(Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date())).append("</CreateTime>");
		sb.append("<ResourceInfo>").append("<SecurityGroupSize>").append(template.getStorageSize()).append("</SecurityGroupSize>")
		        .append("</ResourceInfo>");
		sb.append("</SecurityGroupTemplate>");
		return sb.toString();

	}

	public String getVMTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuilder strb = new StringBuilder();
		String templateCreator = ((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getAccount();
		String createTime = Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date());
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strb.append("<VMTemplate>");
		strb.append("<TemplateID>").append(ConstDef.getVDCTemplateID(template.getType(), templateID)).append("</TemplateID>");
		strb.append("<ResourceType>").append(ConstDef.getVDCResourceType(template.getType())).append("</ResourceType>");
		strb.append("<MeasureMode>").append(template.getMeasureMode()).append("</MeasureMode>");
		strb.append("<TemplateDesc>").append(template.getCpuNum()).append("个CPU,").append(template.getMemorySize()).append("M内存,")
		        .append(template.getStorageSize()).append("G硬盘,操作系统为").append(template.getVmos()).append("</TemplateDesc>");// 2个CPU，4096M内存，30G硬盘，操作系统为Windows
		                                                                                                                    // 7
		                                                                                                                    // Enterprise
		// strb.append("<TemplateStatus>").append(template.getState())
		// .append("</TemplateStatus>");
		strb.append("<TemplateCreator>").append(templateCreator).append("</TemplateCreator>");
		strb.append("<CreateTime>").append(createTime).append("</CreateTime>");
		strb.append("<ResourceInfo>");
		strb.append("<CPUNum>").append(template.getCpuNum()).append("</CPUNum>");
		strb.append("<MemorySize>").append(template.getMemorySize()).append("</MemorySize>");
		strb.append("<StorageSize>").append(template.getStorageSize()).append("</StorageSize>");
		strb.append("<VMOS>").append(template.getVmos()).append("</VMOS>"); // 64-WIN7-En
		                                                                    // OS按这样的规则：64/32-WINXP/WIN7/WIN2K3/REDHAT/CENTOS/-En/Cn
		strb.append("<MaxBandwidth>").append(template.getExtendAttrJSON()).append("</MaxBandwidth>");
		strb.append("<EthAdaNum>").append(template.getVethAdaptorNum()).append("</EthAdaNum>");
		strb.append("<Grade>").append(template.getGrade()).append("</Grade>");
		strb.append("</ResourceInfo>");
		strb.append("</VMTemplate>");
		return strb.toString();
	}

	public String getVMBKTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuilder strb = new StringBuilder();
		String templateCreator = ((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getName();
		String createTime = Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date());
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strb.append("<VMBackupTemplate>");
		strb.append("<TemplateID>").append(ConstDef.getVDCTemplateID(template.getType(), templateID)).append("</TemplateID>");
		strb.append("<ResourceType>").append(ConstDef.getVDCResourceType(template.getType())).append("</ResourceType>");
		strb.append("<MeasureMode>").append(template.getMeasureMode()).append("</MeasureMode>");
		strb.append("<TemplateDesc>").append(template.getStorageSize()).append("G备份磁盘空间").append("</TemplateDesc>");
		strb.append("<TemplateStatus>").append(template.getState()).append("</TemplateStatus>");
		strb.append("<TemplateCreator>").append(templateCreator).append("</TemplateCreator>");
		strb.append("<CreateTime>").append(createTime).append("</CreateTime>");
		strb.append("<ResourceInfo>");
		strb.append("<VMBackupSize>").append(template.getStorageSize()).append("</VMBackupSize>");
		strb.append("<Grade>").append(template.getGrade()).append("</Grade>");
		strb.append("</ResourceInfo>");
		strb.append("</VMBackupTemplate>");
		return strb.toString();
	}

	public String getBSTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuilder strb = new StringBuilder();
		String templateCreator = ((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getName();
		String createTime = Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date());
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strb.append("<BlockStorageTemplate>");
		strb.append("<TemplateID>").append(ConstDef.getVDCTemplateID(template.getType(), templateID)).append("</TemplateID>");
		strb.append("<ResourceType>").append(ConstDef.getVDCResourceType(template.getType())).append("</ResourceType>");
		strb.append("<MeasureMode>").append(template.getMeasureMode()).append("</MeasureMode>");
		strb.append("<TemplateDesc>").append(template.getStorageSize()).append("</TemplateDesc>");
		strb.append("<TemplateStatus>").append(template.getState()).append("</TemplateStatus>");
		strb.append("<TemplateCreator>").append(templateCreator).append("</TemplateCreator>");
		strb.append("<CreateTime>").append(createTime).append("</CreateTime>");
		strb.append("<ResourceInfo>");
		strb.append("<VolumeSize>").append(template.getStorageSize()).append("</VolumeSize>");
		strb.append("<Grade>").append(template.getGrade()).append("</Grade>");
		strb.append("</ResourceInfo>");
		strb.append("</BlockStorageTemplate>");
		return strb.toString();
	}

	public String getBWTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuilder strb = new StringBuilder();
		String templateCreator = ((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getName();
		String createTime = Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date());
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strb.append("<BandwidthTemplate>");
		strb.append("<TemplateID>").append(ConstDef.getVDCTemplateID(template.getType(), templateID)).append("</TemplateID>");
		strb.append("<ResourceType>").append(ConstDef.getVDCResourceType(template.getType())).append("</ResourceType>");
		strb.append("<MeasureMode>").append(template.getMeasureMode()).append("</MeasureMode>");
		strb.append("<TemplateDesc>").append(template.getStorageSize()).append("Mbps 带宽</TemplateDesc> ");
		strb.append("<TemplateStatus>").append(template.getState()).append(" </TemplateStatus>");
		strb.append("<TemplateCreator>").append(templateCreator).append("</TemplateCreator>");
		strb.append("<CreateTime>").append(createTime).append("</CreateTime>");
		strb.append("<ResourceInfo>");
		strb.append("<BandwidthSize>").append(template.getStorageSize()).append("</BandwidthSize>");
		strb.append("</ResourceInfo>");
		strb.append("</BandwidthTemplate>");
		return strb.toString();
	}

	public String getIPTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuilder strb = new StringBuilder();
		String templateCreator = ((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getName();
		String createTime = Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date());
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strb.append("<PublicIPTemplate>");
		strb.append("<TemplateID>").append(ConstDef.getVDCTemplateID(template.getType(), templateID)).append("</TemplateID>");
		strb.append("<ResourceType>").append(ConstDef.getVDCResourceType(template.getType())).append("</ResourceType>");
		strb.append("<MeasureMode>").append(template.getMeasureMode()).append("</MeasureMode>");
		strb.append("<TemplateDesc>IPv").append(template.getStorageSize()).append("类型公网IP地址</TemplateDesc> ");
		strb.append("<TemplateStatus>").append(template.getState()).append(" </TemplateStatus>");
		strb.append("<TemplateCreator>").append(templateCreator).append("</TemplateCreator>");
		strb.append("<CreateTime>").append(createTime).append("</CreateTime>");
		strb.append("<ResourceInfo>");
		strb.append("<IPType>IPv").append(template.getStorageSize()).append("</IPType>");
		strb.append("</ResourceInfo>");
		strb.append("</PublicIPTemplate>");
		return strb.toString();
	}

	public String getMOTemplateXML(int templateID, TTemplateVMBO template) {
		StringBuilder strb = new StringBuilder();
		String templateCreator = ((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getName();
		String createTime = Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date());
		strb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strb.append("<CloudMonitorTemplate>");
		strb.append("<TemplateID>").append(ConstDef.getVDCTemplateID(template.getType(), templateID)).append("</TemplateID>");
		strb.append("<ResourceType>").append(ConstDef.getVDCResourceType(template.getType())).append("</ResourceType>");
		strb.append("<MeasureMode>").append(template.getMeasureMode()).append("</MeasureMode>");
		strb.append("<TemplateDesc>对").append(template.getNetworkDesc()).append("进行监控 </TemplateDesc> ");
		strb.append("<TemplateStatus>").append(template.getState()).append(" </TemplateStatus>");
		strb.append("<TemplateCreator>").append(templateCreator).append("</TemplateCreator>");
		strb.append("<CreateTime>").append(createTime).append("</CreateTime>");
		strb.append("<ResourceInfo>");
		String resourceType = template.getNetworkDesc();
		boolean hasVmMo = false;
		boolean hasEbsMo = false;
		if (resourceType.indexOf("vm") >= 0) {
			hasVmMo = true;
		}
		if (resourceType.indexOf("vl") >= 0) {
			hasEbsMo = true;
		}
		if (hasVmMo && !hasEbsMo) {
			strb.append("<VMMonitorSwitch>").append("ON").append("</VMMonitorSwitch>");
			strb.append("<BSMonitorSwitch>").append("OFF").append("</BSMonitorSwitch>");
		} else if (!hasVmMo && hasEbsMo) {
			strb.append("<VMMonitorSwitch>").append("OFF").append("</VMMonitorSwitch>");
			strb.append("<BSMonitorSwitch>").append("ON").append("</BSMonitorSwitch>");
		} else if (hasVmMo && hasEbsMo) {
			strb.append("<VMMonitorSwitch>").append("ON").append("</VMMonitorSwitch>");
			strb.append("<BSMonitorSwitch>").append("ON").append("</BSMonitorSwitch>");
		} else if (!hasVmMo && !hasEbsMo) {
			strb.append("<VMMonitorSwitch>").append("OFF").append("</VMMonitorSwitch>");
			strb.append("<BSMonitorSwitch>").append("OFF").append("</BSMonitorSwitch>");
		}
		strb.append("</ResourceInfo>");
		strb.append("</CloudMonitorTemplate>");
		return strb.toString();
	}

	@Override
	public void deleteTemplate(int id) throws Exception {
		// 大家添加自己判断语句加入获取各自的TemplateId（按照规范生成的VDCID）
		if (id < 1) {
			throw new Exception("Paramater is invalid");
		}
		TTemplateVMBO templatePo = VMTemplateDao.getTemplateById(id);
		if (templatePo == null) {
			logger.error("Query Template By id is null:" + id);
			throw new Exception("Query Template By id is null");
		}

		// 1.1和vdc的开关
		int _state = -1;
		if (ConstDef.curProjectId == 1) {
			// 如果是虚拟机，虚拟机备份，备份服务则按照vdc流程
			int type = templatePo.getType();
			if (type == ConstDef.RESOURCE_TYPE_VM || type == ConstDef.RESOURCE_TYPE_STORAGE) {
				_state = ConstDef.STATE_ONE;
			} else {
				_state = 9;
			}
		} else if (ConstDef.curProjectId == 2) {
			_state = ConstDef.STATE_ONE;
			this.delAsyncJobVDC(templatePo);
		}
		VMTemplateDao.deleteTemplate_VDC(id, _state);
	}

	public void delAsyncJobVDC(TTemplateVMBO templatePo) throws SCSException {
		AsyncJobVDCPO jobPO = new AsyncJobVDCPO();
		jobPO.setOperation(jobPO.getOperation().DELETE_TEMPLATE);
		jobPO.setTemplate_res_id(ConstDef.getVDCTemplateID(templatePo.getType(), templatePo.getId()));
		jobPO.setUser_id(templatePo.getCreatorUserId());
		jobPO.setTemplate_id(templatePo.getId());
		jobPO.setParameter(templatePo.getResourceTemplate());
		jobPO.setAuditstate(jobPO.getAuditstate().valueOf(0));
		asyncJobVDCService.insterAsyncJobVDC(jobPO);
		// 添加用户操作日志
		String functionName = getFunctionName("删除", templatePo.getType()) + "Job";
		String parameters = "type=" + templatePo.getType() + ",name=" + templatePo.getTemplateDesc();
		String memo = "res_id=" + jobPO.getTemplate_res_id();
		ConstDef.saveLogInfo(1, "资源模板管理", functionName, this.getClass().getName(), "delAsyncJobVDC", parameters, null, memo);
	}

	@Override
	public TTemplateVMBO getTemplateById(int id) throws Exception {
		if (id == 0) {//特殊模板有小于0情况 ninghao@chinaskycloud.com 2012-12-12
			throw new Exception("Paramater is invalid");
		}
		TTemplateVMBO template = VMTemplateDao.getTemplateById(id);
		String policy = template.getPolicy();
		if (null != policy && !policy.equals("")) {
			String policyname = TemplateUtils.getPolicyByCode(policy);
			//to fix bug:7006
//			template.setPolicy(policyname);
			template.setPolicy(policy);
		}
		return template;

	}

	@Override
	public List<TTemplateVMBO> listTemplate(int type, int state, int curPage, int pageSize, int resourcePoolsId, int zoneId) throws Exception {
		return VMTemplateDao.listTemplate(type, state, curPage, pageSize, resourcePoolsId, zoneId);
	}
	
	public List<TTemplateVMBO> listTemplateNotSpecial(int type, int state, int curPage, int pageSize, int resourcePoolsId, int zoneId) throws Exception {
		return VMTemplateDao.listTemplateNotSpecial(type, state, curPage, pageSize, resourcePoolsId, zoneId);
	}
	

	@Override
	public List<TTemplateVMBO> listTemplateOM(int storageSize) throws Exception {
		return VMTemplateDao.listTemplateOM(storageSize);
	}

	@Override
	public List<TTemplateVMBO> listTemplateDISK(int storageSize) throws Exception {
		return VMTemplateDao.listTemplateDISK(storageSize);
	}

	@Override
	public List<TTemplateVMBO> listTemplateByType(int type, int state, int templateId, int cpu_num, int memory_size) throws Exception {
		return VMTemplateDao.listTemplateByType(type, state, templateId, cpu_num, memory_size);
	}

	@Override
	// fix bug 3970
	public List<TTemplateVMBO> listTemplateByType(int type, int state, int templateId, int cpu_num, int memory_size, String osDesc) throws Exception {
		return VMTemplateDao.listTemplateByType(type, state, templateId, cpu_num, memory_size, osDesc);
	}

	@Override
	public int listTemplateCount(int type, int state) throws Exception {
		return VMTemplateDao.listTemplateCount(type, state);
	}

	@Override
	public void updateTemplate(TTemplateVMBO template) throws Exception {
		if (template == null) {
			throw new Exception("Paramater is missing");
		}
		if (template.getId() < 1) {
			throw new Exception("Paramater is invalid");
		}
		VMTemplateDao.updateTemplate(template);
	}

	@Override
	public List<TTemplateVMBO> searchTemplate(String name, int type, int state, int curPage, int pageSize) throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		return VMTemplateDao.searchTemplate(name, type, state, Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                    Constants.STATUS_COMMONS.IGNORE.getValue(), Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                    Constants.STATUS_COMMONS.IGNORE.getValue(), "", curPage, pageSize);
	}

	@Override
	public int searchTemplateCount(String name, int type, int state) throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		return VMTemplateDao.searchTemplateCount(name, type, state, Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                         Constants.STATUS_COMMONS.IGNORE.getValue(), Constants.STATUS_COMMONS.IGNORE.getValue(),
		                                         Constants.STATUS_COMMONS.IGNORE.getValue(), "");
	}

	public IVMTemplateDao getVMTemplateDao() {
		return VMTemplateDao;
	}

	public void setVMTemplateDao(IVMTemplateDao vMTemplateDao) {
		VMTemplateDao = vMTemplateDao;
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
		return VMTemplateDao.checkNameUniqueness(name);
	}

	@Override
	public List<TTemplateVMBO> advancedSearch(String name, int type, int state, int cpuNum, int memSize, int osId, int storageSize,
	        String networkDesc, int curPage, int pageSize) throws Exception {
		return VMTemplateDao.searchTemplate(name, type, state, cpuNum, memSize, osId, storageSize, networkDesc, curPage, pageSize);
	}

	@Override
	public int advancedSearchCount(String name, int type, int state, int cpuNum, int memSize, int osId, int storageSize, String networkDesc)
	        throws Exception {
		return VMTemplateDao.searchTemplateCount(name, type, state, cpuNum, memSize, osId, storageSize, networkDesc);
	}

	@Override
	public boolean checkMonitorResources(String resourceTypes, int id, int addOrMod) throws Exception {
		boolean result = false;
		if (addOrMod == 1) {
			result = VMTemplateDao.checkMonitorResources(resourceTypes);
		} else if (addOrMod == 2) {
			if (StringUtils.isBlank(resourceTypes)) {
				throw new Exception("Paramater is missing");
			}
			String oldResources = VMTemplateDao.getMonitorResourcesById(id);

			if (oldResources.equals(resourceTypes)) {
				result = true;
			} else {
				result = VMTemplateDao.checkMonitorResources(resourceTypes);
			}
		}
		return result;
	}

	@Override
	public void updateTemplate_VDC(TTemplateVMBO template) throws Exception {
		if (ConstDef.curProjectId == 1) {
			// 如果是虚拟机，虚拟机备份，备份服务则按照vdc流程
			int type = template.getType();
			if (type == ConstDef.RESOURCE_TYPE_VM || type == ConstDef.RESOURCE_TYPE_STORAGE) {
				template.setState(ConstDef.STATE_ONE);
			} else {

				template.setState(8);
			}
		} else if (ConstDef.curProjectId == 2) {
			// 更新xml字段
			this.getTemplateXml(template);
			template.setState(ConstDef.STATE_ONE);
		}
		// 更新模板表
		VMTemplateDao.updateTemplate_VDC(template);
		if (ConstDef.curProjectId == 2) {
			// 往job表中插入一条数据
			this.modAsyncJobVDC(template);
		}
	}		

	public void modAsyncJobVDC(TTemplateVMBO template) throws SCSException {
		AsyncJobVDCPO jobPO = new AsyncJobVDCPO();
		jobPO.setOperation(jobPO.getOperation().valueOf("UPDATE_TEMPLATE"));
		jobPO.setUser_id(template.getCreatorUserId());
		jobPO.setTemplate_id(template.getId());
		jobPO.setParameter(template.getResourceTemplate());
		jobPO.setAuditstate(jobPO.getAuditstate().valueOf(0));
		jobPO.setTemplate_res_id(ConstDef.getVDCTemplateID(template.getType(), template.getId()));// /获取模板VDC标识
		asyncJobVDCService.insterAsyncJobVDC(jobPO);
		// 添加用户操作日志
		String functionName = getFunctionName("修改", template.getType()) + "Job";
		String parameters = "type=" + template.getType() + ",name=" + template.getTemplateDesc();
		String memo = "res_id=" + jobPO.getTemplate_res_id() + ",   xmlParameter = " + template.getResourceTemplate();
		ConstDef.saveLogInfo(1, "资源模板管理", functionName, this.getClass().getName(), "modAsyncJobVDC", parameters, null, memo);
	}

	@Override
	public boolean getTemplateIsBindProduction(int template, int templateType) throws SCSException {
		try {

			boolean isBind = VMTemplateDao.getTemplateIsBindProduction(template, templateType);
			boolean isBind2 = VMTemplateDao.getTemplateIsBindProduction2(template, 50);
			// to fix bug:2199
			boolean result = isBind || isBind2;
			return result;
		}
		catch (Exception e) {
			logger.error("Execute [VMTemplateServiceImpl] method: templateIsBindProduction Exception :", e);
			throw new SCSException("Execute [VMTemplateServiceImpl] method: templateIsBindProduction Exception :", e);
		}
	}

	@Override
	public int upateTemplateState(int templateId, int state) throws SCSException {
		int index = 0;
		try {
			index = VMTemplateDao.upateTemplateState(templateId, state);
		}
		catch (Exception e) {
			logger.error("Execute [VMTemplateServiceImpl] method: templateIsBindProduction Exception :", e);
			throw new SCSException("Execute [VMTemplateServiceImpl] method: templateIsBindProduction Exception :", e);
		}
		return index;
	}

	/**
	 * 搜索物理机模板的全部操作系统列表 创建人： ninghao 创建时间：2012-8-28 09:53:55
	 * 
	 * @return 物理机操作系统列表
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getPhysicalTemplateOS(int type) throws Exception {

		List<Map<String, Object>> oslist = VMTemplateDao.getPhysicalTemplateOS(type);

		return oslist;
	}

	/**
	 * 更新模板（修改订单）
	 * @param template
	 * @return
	 * @throws Exception
	 * @author ninghao@chinaskycloud.com
	 */
	@Override
	public int updateTemplateByOrderModify(TTemplateVMBO template) throws SCSException
	{
		int excuteUpdate = -1;
		
		try {
			excuteUpdate = this.VMTemplateDao.updateTemplateByOrderModify(template);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("Execute [VMTemplateServiceImpl] method: updateTemplateByOrderModify Exception :", e);
			excuteUpdate = -2;
		}
		if(excuteUpdate > 0){//更新成功
			
		}else if(excuteUpdate == -2){//异常
			
		}else{//更新失败
			
		}
		
		return excuteUpdate;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

	public IProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

}
