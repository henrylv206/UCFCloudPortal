package com.skycloud.management.portal.front.order.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.service.IProductService;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.impl.PublicIpServiceImpl;
import com.skycloud.management.portal.admin.sysmanage.service.impl.UserVlanServiceImpl;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IMCTemplateService;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.command.res.ECluster;
import com.skycloud.management.portal.front.command.res.EListConfigurations;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EVlanIpRange;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.command.res.ListTemplates;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.instance.service.IJobInstanceInfoService;
import com.skycloud.management.portal.front.loadbalance.service.IoadBalanceH3CService;
import com.skycloud.management.portal.front.log.aop.LogInfo;
import com.skycloud.management.portal.front.log.entity.TUserLogBase;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.order.service.INicsService;
import com.skycloud.management.portal.front.order.service.IOrderSerivce;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.service.IFirewallService;
import com.skycloud.management.portal.front.resources.service.NasResourceService;
import com.skycloud.management.portal.front.resources.service.PhysicalMachinesService;
import com.skycloud.management.portal.front.resources.service.VirtualMachineModifyService;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;

@SuppressWarnings("serial")
public class OrderAction extends BaseAction {

	private final Logger logger = Logger.getLogger(OrderAction.class); // 日志

	public static final String SUCCESS = "success";

	public static final String ERROR = "error";

	public static final int ROLE_APPROVE_LEVEL_WAIT = 1;// 等待审批

	public static final int ORDER_TYPE_NEW = 1;// 订单类型；1：新申请；2：修改申请；3：删除申请

	public static final int ORDER_TYPE_UPDATE = 2;

	public static final int ORDER_TYPE_DEL = 3;

	// 订单状态；0：购物车状态；1：申请状态；2：高级用户审核状态；3：管理员审核状态；4：高级管理员审核状态；5：拒绝状态
	public static final int ORDER_STATE_SHOPCART = 0;

	public static final int ORDER_STATE_APPLY = 1;

	public static final int ORDER_STATE_CHECK = 2;

	public static final int ORDER_STATE_ADMIN_CHECK = 3;

	public static final int ORDER_STATE_ADMIN_HEAD_CHECK = 4;

	public static final int ORDER_STATE_REFUSE = 4;

	// 模板类型
	public static final int TEMPLATE_TYPE_VM = 1;

	public static final int TEMPLATE_TYPE_EBS = 2;

	public static final int TEMPLATE_TYPE_MC = 3;

	// 虚机模板类型
	public static final int VM_TEMPLATE_TYPE_VM = 1; // 虚机

	public static final int VM_TEMPLATE_TYPE_EBS = 2; // 扩展存储

	public static final int VM_TEMPLATE_TYPE_DISK = 12; // 扩展存储

	// public static final int VM_TEMPLATE_TYPE_BACKUP_SERVICE = 4; //备份及服务
	// 模板状态

	public static final int TEMPLATE_STATE_SUBMIT = 1; // 提交

	public static final int TEMPLATE_STATE_AVAILABLE = 2; // 可用

	// service
	private IProductService productService;

	private IOrderSerivce orderService;

	private ICloudAPISerivce cloudAPIService;

	private IVMTemplateService VMTemplateService;

	private ICommandService commandService;

	private IMCTemplateService MCTemplateService;

	private IAuditSevice auditService;

	private PublicIpServiceImpl publicIPService;

	private IJobInstanceInfoService jobInstanceInfoService;

	private IInstanceService instanceService;

	private INicsService nicsService;

	private UserVlanServiceImpl userVlanService;

	private List<TTemplateVMBO> templateVMList;

	private List<TTemplateMCBO> templateMCList;

	private List<TTemplateVMBO> templateDISKList;

	private List<EVlanIpRange> vlanList;

	private List<ECluster> clusterList;

	private List<EZone> zoneList;

	private List<ENetwork> networkList;

	private List<ENetwork> networkListOthers;

	private List<TPublicIPBO> publicIpList;


	private int templateId;

	private TTemplateVMBO templateVM;

	private String templateVMstr;

	private int id;

	private String message;

	TOrderBO orderBO;

	private int templateType;

	private String templateInfo;

	// 查询订单的查询参数
	private int orderState = -1;

	private String payState;

	private String startdate;

	private String enddate;

	//
	private int resourcePoolId;

	private String resourcePoolName;

	private String searchStr;

	private int flag;

	private String queryJson;

	// zs
	private Map<String, Object> listResp;

	private int curPage = 1;// 当前页数

	private int pageSize = 10;// 每页显示多少条

	private int countTotal;

	private int countTotal2;

	private Map<String, Object> myWanIpVM; // 公网IP和其虚拟机对象信息

	private List<Map<String, Object>> myVM; // 单纯的虚拟机信息

	private List<Long> vlanIdList;

	private int vethAdaptorNum;

	private int total;

	private int zoneId;

	private int userId;

	//private IServiceInstanceService serviceInstanceService;

	private int ordertype;

	private int resourcePoolsId;
	
	private int networkType;// 网卡类型

	private PhysicalMachinesService physicalMachineService;

	private IFirewallService firewallService;

	private IoadBalanceH3CService loadbalanceH3Cservice;

	private VirtualMachineModifyService vmModifyService;

	private NasResourceService nasResourceService;
	/**
	 * 1.0版订单保存（门户）
	 *
	 * @return
	 */
	@LogInfo(desc = "templateType=1=>functionName=虚机提交订单,templateType=2=>functionName=小型机提交订单,templateType=3=>functionName=扩展存储提交订单,templateType=4=>functionName=备份服务提交订单,templateType=5=>functionName=监控服务提交订单,templateType=6=>functionName=负载均衡服务提交订单 ,templateType=7=>functionName=防火墙资源服务提交订单,templateType=8=>functionName=公网带宽资源服务提交订单,templateType=9=>functionName=公网IP资源服务提交订单,templateType=10=>functionName=物理机资源提交订单", operateType = 1, moduleName = "服务目录", functionName = "提交订单", parameters = "templateType")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String saveNewOrder() {
		int saveOrderId = -1;
		if (templateType == 0 && templateInfo == null) {
			this.message = "保存失败";
			return ERROR;
		}
		try {
			TVmInfo vminfo = (TVmInfo) JsonUtil.getObject4JsonString(templateInfo, TVmInfo.class);
			TOrderBO order = new TOrderBO();
			List<TInstanceInfoBO> vms = new ArrayList<TInstanceInfoBO>();
			List<TNicsBO> nicsList = new ArrayList<TNicsBO>();
			// 订单对象
			int userId = 0;
			TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
			if (user != null) {
				userId = user.getId();
			}
			order.setCreatorUserId(userId);
			order.setState(user.getRoleApproveLevel());// 订单状态为用户角色审批级别，申请中
			// ORDER_STATE_APPLY
			order.setOrderApproveLevelState(user.getRoleApproveLevel());// 订单审批状态ROLE_APPROVE_LEVEL_WAIT;
			order.setType(ORDER_TYPE_NEW);// 订单类型设为新订单
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
			// 日期+6位随机数
			order.setCreateDt(new Timestamp(System.currentTimeMillis()));
			order.setLastupdateDt(new Timestamp(System.currentTimeMillis()));
			String parameter = "";
			Map<String, Object> map = new HashMap<String, Object>();
			if (templateType == 1 || templateType == 2) {
				TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
				if (null == templateInfo) {
					this.message = "保存失败";
					return ERROR;
				}
				if (templateType == 1) {
					order.setZoneId(Long.valueOf(vminfo.getZoneId()));
					order.setCpuNum(templateInfo.getCpuNum());
					order.setMemorySize(templateInfo.getMemorySize());

					map.put("cpuNum", templateInfo.getCpuNum());
					map.put("memorySize", templateInfo.getMemorySize());
					map.put("zoneId", vminfo.getZoneId());
					parameter = JsonUtil.getJsonString4JavaPOJO(map);

				} else {
					order.setZoneId(Long.valueOf(vminfo.getZoneId()));
					if (vminfo.getTemplateId() == 1) {
						order.setStorageSize(vminfo.getStorageSize());
						map.put("storageSize", vminfo.getStorageSize());
					} else {
						order.setStorageSize(templateInfo.getStorageSize());
						map.put("storageSize", templateInfo.getStorageSize());
					}
					map.put("zoneId", vminfo.getZoneId());
					parameter = JsonUtil.getJsonString4JavaPOJO(map);
				}
				// 实例对象
				// for(int i = 5; i<10;i++){
				TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
				instanceInfo.setTemplateId(vminfo.getTemplateId());
				instanceInfo.setTemplateType(TEMPLATE_TYPE_VM);
				instanceInfo.setInstanceName(vminfo.getInstanceName());
				instanceInfo.setComment(vminfo.getDescription());
				instanceInfo.setState(ORDER_STATE_APPLY);
				instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setResourceInfo(parameter);
				if (templateType == 1) {
					instanceInfo.setZoneId(Long.valueOf(vminfo.getZoneId()));
					instanceInfo.setOsDesc(templateInfo.getVmos());
					instanceInfo.setStorageSize(0);
					instanceInfo.setCpuNum(templateInfo.getCpuNum());
					instanceInfo.setMemorySize(templateInfo.getMemorySize());
					instanceInfo.setCpufrequency(0);
					instanceInfo.seteOsId(templateInfo.geteOsId());
				} else {
					// 判断是否为自定义存储(==1为自定义存储)
					if (vminfo.getTemplateId() == 1) {
						instanceInfo.setStorageSize(vminfo.getStorageSize());
					} else {
						instanceInfo.setStorageSize(templateInfo.getStorageSize());
					}
					instanceInfo.setZoneId(Long.valueOf(vminfo.getZoneId()));
				}
				vms.add(instanceInfo);
				// 虚机挂硬盘
				if (templateType == 1 && vminfo.getStorageSize() > 0) {
					TInstanceInfoBO instanceInfoEBS = new TInstanceInfoBO();
					// 虚机挂硬盘，默认ID为1
					instanceInfoEBS.setTemplateId(1);
					instanceInfoEBS.setTemplateType(TEMPLATE_TYPE_VM);
					instanceInfoEBS.setInstanceName(vminfo.getInstanceName());
					instanceInfoEBS.setComment(vminfo.getDescription());
					instanceInfoEBS.setState(ORDER_STATE_APPLY);
					instanceInfoEBS.setCreateDt(new Timestamp(new Date().getTime()));
					instanceInfoEBS.setLastupdateDt(new Timestamp(new Date().getTime()));
					Map<String, Object> mapebs = new HashMap<String, Object>();
					mapebs.put("storageSize", templateInfo.getStorageSize());
					parameter = JsonUtil.getJsonString4JavaPOJO(mapebs);
					instanceInfoEBS.setResourceInfo(parameter);
					instanceInfoEBS.setStorageSize(vminfo.getStorageSize());
					instanceInfoEBS.setZoneId(Long.valueOf(vminfo.getZoneId()));
					vms.add(instanceInfoEBS);
				}

				// }
				// 网卡信息
				// 判断为虚拟机
				if (templateType == 1) {
					// 如果为是动态获取IP,则将ip地址置为“0”；
					int dhcpValue = orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
					if (dhcpValue == 1) {
						vminfo.setIpAddress("0");
						vminfo.setIpAddress2("0");
					}
					// 网卡有1个以上
					if (vminfo.getVethAdaptorNum() >= 1) {
						TNicsBO nics = new TNicsBO();
						nics.seteVlanId(vminfo.getVlan());
						nics.setIp(vminfo.getIpAddress());
						nicsList.add(nics);
					}
					// 网卡有2个
					if (vminfo.getVethAdaptorNum() == 2) {
						TNicsBO nics = new TNicsBO();
						nics.seteVlanId(vminfo.getVlan2());
						nics.setIp(vminfo.getIpAddress2());
						nicsList.add(nics);
					}
				}
			} else if (templateType == 3) {
				TTemplateMCBO templateInfo = this.MCTemplateService.getTemplateById(vminfo.getTemplateId());
				if (null == templateInfo) {
					this.message = "保存失败";
					return ERROR;
				}
				map.put("cpuNum", templateInfo.getCpuNum());
				map.put("storageSize", templateInfo.getStorageSize());
				parameter = JsonUtil.getJsonString4JavaPOJO(map);
				// 订单信息
				order.setCpuNum(templateInfo.getCpuNum());
				order.setStorageSize(templateInfo.getStorageSize());
				order.setMemorySize(templateInfo.getStorageSize());

				// 实例对象
				TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
				instanceInfo.setTemplateId(vminfo.getTemplateId());
				instanceInfo.setTemplateType(TEMPLATE_TYPE_MC);
				instanceInfo.setInstanceName(vminfo.getInstanceName());
				instanceInfo.setComment(vminfo.getDescription());
				instanceInfo.setState(ORDER_STATE_APPLY);
				instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setResourceInfo(parameter);
				instanceInfo.setOsDesc(templateInfo.getVmos());
				instanceInfo.setCpuNum(templateInfo.getCpuNum());
				instanceInfo.setMemorySize(templateInfo.getMemorySize());
				instanceInfo.setStorageSize(templateInfo.getStorageSize());

				vms.add(instanceInfo);

				// 网卡信息

				if (null != vminfo.getIpAddress()) {
					TNicsBO nics = new TNicsBO();
					nics.seteVlanId(vminfo.getVlan());
					nics.setIp(vminfo.getIpAddress());
					nicsList.add(nics);
				}
			} else if (templateType == 4) {
				// 4:备份服务
				TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
				if (null == templateInfo) {
					this.message = "保存失败";
					return ERROR;
				}
				order.setStorageSize(templateInfo.getStorageSize());
				map.put("storageSize", templateInfo.getStorageSize());
				parameter = JsonUtil.getJsonString4JavaPOJO(map);
				TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
				instanceInfo.setTemplateId(vminfo.getTemplateId());
				instanceInfo.setTemplateType(templateInfo.getType());
				instanceInfo.setInstanceName(vminfo.getInstanceName());
				instanceInfo.setComment(vminfo.getDescription());
				instanceInfo.setState(ORDER_STATE_APPLY);
				instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setResourceInfo(parameter);
				// 备份服务->磁盘大小
				instanceInfo.setStorageSize(templateInfo.getStorageSize());
				vms.add(instanceInfo);
			} else if (templateType == 5) {
				// 5：监控服务
				TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
				if (null == templateInfo) {
					this.message = "保存失败";
					return ERROR;
				}
				TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
				instanceInfo.setTemplateId(vminfo.getTemplateId());
				instanceInfo.setTemplateType(templateInfo.getType());
				instanceInfo.setInstanceName(vminfo.getInstanceName());
				instanceInfo.setComment(vminfo.getDescription());
				instanceInfo.setState(ORDER_STATE_APPLY);
				instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
				// 5:监控服务->监控资源
				instanceInfo.setResourceInfo(templateInfo.getNetworkDesc());

				vms.add(instanceInfo);
			} else if (templateType == 6 || templateType == 7 || templateType == 8 || templateType == 9) {
				// 6:负载均衡服务 ,7:防火墙资源服务,8:带宽资源服务,9:公网IP资源服务
				TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
				if (null == templateInfo) {
					this.message = "保存失败";
					return ERROR;
				}
				TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
				instanceInfo.setTemplateId(vminfo.getTemplateId());
				instanceInfo.setTemplateType(templateInfo.getType());
				instanceInfo.setInstanceName(vminfo.getInstanceName());
				instanceInfo.setComment(vminfo.getDescription());
				instanceInfo.setState(ORDER_STATE_APPLY);
				instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
				// 6:负载均衡并发数 7:防火墙规则条数 8:带宽大小Mbps ,//9:公网IP资源服务->IP类型
				instanceInfo.setStorageSize(templateInfo.getStorageSize());
				// 8:带宽IP地址
				if (templateType == 8) {
					map.put("ipAddress", vminfo.getIpAddress());
					parameter = JsonUtil.getJsonString4JavaPOJO(map);
					instanceInfo.setResourceInfo(parameter);
				}
				// 9:公网IP资源服务->提供商，ip地址
				if (templateType == 9) {
					map.put("serviceProvider", vminfo.getZoneId()); // 供应商编码
					map.put("ipAddressId", vminfo.getIpAddressId()); // 公网IP ID
					map.put("ipAddress", vminfo.getIpAddress()); // 公网ip地址
					map.put("ipType", vminfo.getIpType()); // 公网IP类型，IPV4或IPV6
					map.put("period", vminfo.getPeriod()); // 购买周期
					parameter = JsonUtil.getJsonString4JavaPOJO(map);
					instanceInfo.setResourceInfo(parameter);
				}
				vms.add(instanceInfo);
			} else if (templateType == 10) {
				TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
				if (null == templateInfo) {
					this.message = "保存失败";
					return ERROR;
				}
				order.setZoneId(Long.valueOf(vminfo.getZoneId()));
				order.setCpuNum(templateInfo.getCpuNum());
				order.setMemorySize(templateInfo.getMemorySize());

				map.put("cpuNum", templateInfo.getCpuNum());
				map.put("memorySize", templateInfo.getMemorySize());
				map.put("zoneId", vminfo.getZoneId());
				parameter = JsonUtil.getJsonString4JavaPOJO(map);
				// 实例对象
				// for(int i = 5; i<10;i++){
				TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
				instanceInfo.setTemplateId(vminfo.getTemplateId());
				instanceInfo.setTemplateType(templateInfo.getType());
				instanceInfo.setInstanceName(vminfo.getInstanceName());
				instanceInfo.setComment(vminfo.getDescription());
				instanceInfo.setState(ORDER_STATE_APPLY);
				instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
				instanceInfo.setResourceInfo(parameter);
				instanceInfo.setZoneId(Long.valueOf(vminfo.getZoneId()));
				instanceInfo.setOsDesc(templateInfo.getVmos());
				instanceInfo.setStorageSize(templateInfo.getStorageSize());
				instanceInfo.setCpuNum(templateInfo.getCpuNum());
				instanceInfo.setMemorySize(templateInfo.getMemorySize());
				instanceInfo.setCpufrequency(0);
				instanceInfo.seteOsId(templateInfo.geteOsId());
				instanceInfo.seteNetworkId(vminfo.getIpAddressId());
				vms.add(instanceInfo);
				// }
				// 网卡信息
				// 如果为是动态获取IP,则将ip地址置为“0”；
				// int dhcpValue =
				// orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
				// if (dhcpValue == 1){
				// vminfo.setIpAddress("0");
				// vminfo.setIpAddress2("0");
				// }
				// //网卡有1个以上
				// if (vminfo.getVethAdaptorNum()>=1){
				// TNicsBO nics = new TNicsBO();
				// nics.seteVlanId(vminfo.getVlan());
				// nics.setIp(vminfo.getIpAddress());
				// nicsList.add(nics);
				// }
				// //网卡有2个
				// if (vminfo.getVethAdaptorNum()==2){
				// TNicsBO nics = new TNicsBO();
				// nics.seteVlanId(vminfo.getVlan2());
				// nics.setIp(vminfo.getIpAddress2());
				// nicsList.add(nics);
				// }
			}

			// saveOrderId = this.orderService.saveOrder(order, vms, nicsList);
			if (saveOrderId > 0) {
				if (user.getRoleApproveLevel() == 4) {
					auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 1, templateType);
				} else if (user.getRoleApproveLevel() < 4) {
					auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 1,
					                               templateType);
				}
				this.message = "成功：订单信息成功写入数据库";
			} else {
				this.message = "失败：写数据库失败";
			}
			return SUCCESS;
		}
		catch (Exception e) {
			this.message = "失败：保存订单信息失败,message : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 版本：SkyForm1.1，1.2 前台门户 下单
	 *
	 * @return
	 */
	@LogInfo(desc = "产品区,通过购物车下订单", operateType = 1, moduleName = "产品区", functionName = "提交订单", parameters = "vminfos")
	public String portal_saveNewOrder() {
		int saveOrderId = -1;
		if (templateInfo == null) {
			this.message = "保存失败";
			return ERROR;
		}
		try {
			List<TVmInfo> vminfos = JsonUtil.getListComp4Json(templateInfo, TVmInfo.class);
			TOrderBO order = new TOrderBO();
			List<TInstanceInfoBO> vms = new ArrayList<TInstanceInfoBO>();

			// 订单对象
			TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
			if (user != null) {
				this.setUserId(user.getId());
			}
			order.setCreatorUserId(this.getUserId());
			order.setState(user.getRoleApproveLevel());// 订单状态为用户角色审批级别，申请中
			// ORDER_STATE_APPLY
			order.setOrderApproveLevelState(user.getRoleApproveLevel());// 订单审批状态ROLE_APPROVE_LEVEL_WAIT;
			order.setType(ORDER_TYPE_NEW);// 订单类型设为新订单
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
			// 日期+6位随机数
			order.setCreateDt(new Timestamp(System.currentTimeMillis()));
			order.setLastupdateDt(new Timestamp(System.currentTimeMillis()));
			int vmNum = 0;
			Map<Integer, Integer> nicsVethMap = getNicsVethAdaptorVlans(vminfos);
			for (TVmInfo vminfo : vminfos) {
				Map<String, Object> map = new HashMap<String, Object>();
				templateType = vminfo.getTemplateType();
				if (templateType == 1) {
					vmNum++;
					vminfo.setTarget(vmNum);
					// 1:虚拟机
					getInstanceInfoVM(vminfo, vms, user, null, 1);
				} else if (templateType == 2) {
					// 2:虚机磁盘
					getInstanceInfoVdisk(vminfo, vms, user);
				} else if (templateType == 3) {
					// 3:小型机
					getInstanceInfoMC(vminfo, vms, null, 1);
				} else if (templateType == 4) {
					// 4:备份服务
					getInstanceInfoBackup(vminfo, vms, user);
				} else if (templateType == 5) {
					// 5：监控服务
					getInstanceInfoMonitor(vminfo, vms);
				} else if (templateType == 6) {
					// 6：负载均衡服务
					getInstanceInfoLoadBalance(vminfo, vms);
				} else if (templateType == 7) {
					// 7：防火墙服务
					getInstanceInfoFireWall(vminfo, vms);
				} else if (templateType == 8) {
					// 8：带宽服务
					getInstanceInfoLoadBandWidth(vminfo, vms, user);
				} else if (templateType == 9) {
					// 9：公网IP服务
					getInstanceInfoPublicIP(vminfo, vms, user);
				} else if (templateType == 10) {
					// 10:物理机
					getInstanceInfoPhysicalMC(vminfo, vms, null, 1);
				} else if (templateType == 11) {
					// 11:对象存储
					getInstanceInfoObjetStorage(vminfo, vms);
				} else if (templateType == 12) {
					// 12:弹性块存储
					getInstanceInfoIpSan(vminfo, vms);
				} else if (templateType == 13) {
					// 13:nas文件系统
					getInstanceInfoNas(vminfo, vms);
				} else if (templateType == 15) {
					// 15:云数据备份
					getInstanceInfoDataBackup(vminfo, vms);
				} else if (templateType == 50) {
					// 50:多虚机服务
					getInstanceInfoOfVMS(vminfo, vms, user);
				}
			}

			saveOrderId = this.orderService.insertOrder(order, vms);
			if (saveOrderId > 0) {
				if (user.getRoleApproveLevel() == 4) {
					auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 1, templateType);
				} else if (user.getRoleApproveLevel() < 4) {
					auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 1,
					                               templateType);
				}
				if (StringUtils.isEmpty(this.message)) {
					this.message = "成功：订单信息成功写入数据库";
				}
			} else {
				this.message = "失败：写数据库失败";
			}

			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			this.message = "失败：保存订单信息失败,message : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;

	}


	private Map<Integer, Integer> getNicsVethAdaptorVlans(List<TVmInfo> vminfos) throws Exception {
		Map<Integer, Integer> vethAdaptorMap = new HashMap<Integer, Integer>();
		vethAdaptorMap.put(1, 0);
		vethAdaptorMap.put(2, 0);
		vethAdaptorMap.put(3, 0);
		for (TVmInfo vminfo : vminfos) {
			if (templateType == 1) {
				int vethNum = vminfo.getVethAdaptorNum();
				int vethNumMap = vethAdaptorMap.get(vethNum);
				vethAdaptorMap.put(vethNum, vethNumMap + 1);
			}
			if (templateType == 50) {
				int productId = vminfo.getProductId();
				Product product = productService.getProduct(productId);
				List<TTemplateVMBO> templateVMList = product.getTemplates();

				for (TTemplateVMBO vm : templateVMList) {
					int vethNum = vm.getVethAdaptorNum();
					int tempNum = vm.getTemplateNum();
					int vethNumMap = vethAdaptorMap.get(vethNum);
					vethAdaptorMap.put(vethNum, vethNumMap + tempNum);
				}
			}
		}
		return vethAdaptorMap;
	}

	// 获取网卡信息
	// to fix bug:0001678
	private String getNicsInfo(TVmInfo vminfo, List<TNicsBO> nicsList, Integer resourcePoolID) throws Exception {
		// 网卡信息
		// 如果为是动态获取IP,则将ip地址置为“0”；

		//		int dhcpValue = orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
		int dhcpValue = ConstDef.getNicsDhcpSwitch();
		// 动态获取IP
		List<Long> vlanlist = new ArrayList<Long>();
		if (dhcpValue == 1) {
			// 网卡有1个以上
			//			if (vminfo.getVethAdaptorNum() >= 1) {
			//				// vlanlist = this.getUserVlanId(vminfo.getVethAdaptorNum(),
			//				// vminfo.getTarget(), vminfo.getZoneId(), resourcePoolID);
			//				// vlanlist != null ? vlanlist.get(0) : 0;
			//				long vlanId1 = 0;
			//				TNicsBO nics = new TNicsBO();
			//				nics.seteVlanId(vlanId1);
			//				nics.setIp("0");
			//				nicsList.add(nics);
			//			}
			//			// 网卡有2个
			//			if (vminfo.getVethAdaptorNum() >= 2) {
			//				// vlanlist = this.getUserVlanId(vminfo.getVethAdaptorNum(),
			//				// vminfo.getTarget(), vminfo.getZoneId(), resourcePoolID);
			//				long vlanId2 = 0;
			//				TNicsBO nics = new TNicsBO();
			//				nics.seteVlanId(vlanId2);
			//				nics.setIp("0");
			//				nicsList.add(nics);
			//			}
			for(int i=0;i<vminfo.getVethAdaptorNum();i++){
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(0);
				nics.setIp("0");
				nicsList.add(nics);
			}
		} else {
			String ip = "0";
			// 网卡有1个以上
			if (vminfo.getVethAdaptorNum() >= 1) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan());
				ip = vminfo.getIpAddress();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
			// 网卡有2个
			if (vminfo.getVethAdaptorNum() >= 2) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan2());
				ip = vminfo.getIpAddress2();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
			if (vminfo.getVethAdaptorNum() >= 3) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan3());
				ip = vminfo.getIpAddress3();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
			if (vminfo.getVethAdaptorNum() >= 4) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan4());
				ip = vminfo.getIpAddress4();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
		}
		return SUCCESS;
	}

	private String getNicsInfoForMulti(TVmInfo vminfo, List<TNicsBO> nicsList) throws Exception {
		// 网卡信息
		// 如果为是动态获取IP,则将ip地址置为“0”；

		//		int dhcpValue = orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
		int dhcpValue = ConstDef.getNicsDhcpSwitch();
		// 动态获取IP
		if (dhcpValue == 1) {
			// 网卡有1个以上
			//			if (vminfo.getVethAdaptorNum() >= 1) {
			//				TNicsBO nics = new TNicsBO();
			//				nics.seteVlanId(vminfo.getVlan());
			//				nics.setIp(vminfo.getIpAddress());
			//				nicsList.add(nics);
			//			}
			//			// 网卡有2个
			//			if (vminfo.getVethAdaptorNum() >= 2) {
			//				TNicsBO nics = new TNicsBO();
			//				nics.seteVlanId(vminfo.getVlan2());
			//				nics.setIp(vminfo.getIpAddress2());
			//				nicsList.add(nics);
			//			}

			for(int i=0;i<vminfo.getVethAdaptorNum();i++){
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(0);
				nics.setIp("0");
				nicsList.add(nics);
			}
		} else {
			String ip = "0";
			// 网卡有1个以上
			if (vminfo.getVethAdaptorNum() >= 1) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan());
				ip = vminfo.getIpAddress();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
			// 网卡有2个
			if (vminfo.getVethAdaptorNum() >= 2) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan2());
				ip = vminfo.getIpAddress2();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
			if (vminfo.getVethAdaptorNum() >= 3) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan3());
				ip = vminfo.getIpAddress3();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
			if (vminfo.getVethAdaptorNum() >= 4) {
				TNicsBO nics = new TNicsBO();
				nics.seteVlanId(vminfo.getVlan4());
				ip = vminfo.getIpAddress4();
				if (ip == null) {
					ip = "0";
				}
				nics.setIp(ip);
				nicsList.add(nics);
			}
		}
		return SUCCESS;
	}

	// 类型 1：PrivateNetwork，2：StorageNetwork
	private List<Long> getUserVlanId(int vethAdaptorNum, int total, int zoneId, Integer resourcePoolID) {
		int userId = 0;
		List<Long> vlanIdList = null;
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user != null) {
			userId = user.getId();
		}
		try {
			vlanIdList = nicsService.getAvailableNetworkByUserId(userId, vethAdaptorNum, total, zoneId, resourcePoolID);
			// if(null!=vlanIdList&&!vlanIdList.isEmpty()){
			// vlanId = vlanIdList.get(type-1);
			// }
			// else {
			// //初次，随机分配一个vlanId
			// Random random = new Random();
			// TUserVlanBO tUserVlanBo = new TUserVlanBO();
			// tUserVlanBo.setUserId(userId);
			// tUserVlanBo.setType(type);
			// List<TUserVlanBO> list =
			// userVlanService.findUserVlan(tUserVlanBo);
			// Map<Long,Long> map = new TreeMap<Long,Long>();
			// if(null!=list&&!list.isEmpty()){
			// for(TUserVlanBO bo: list){
			// map.put(bo.getVlanId(),bo.getVlanId());
			// }
			// vlanId = map.get(Math.abs(random.nextInt())%(map.size()));
			// }
			// }
			// 如果vlan的空余ip不足，设置message,给出用户提示。 fix bug 3932
			// if(null!=vlanIdList&&!vlanIdList.isEmpty()){
			// if(0==vlanIdList.get(0)){
			// this.setVlanPoorMessage();
			// }
			// if(vlanIdList.size()==2){
			// if(0==vlanIdList.get(1)){
			// this.setVlanPoorMessage();
			// }
			// }
			// }
			// //fix bug 3932
			// else {
			// vlanIdList = new ArrayList<Long>();
			// vlanIdList.add(0L);
			// vlanIdList.add(0L);
			// this.setVlanPoorMessage();
			// }
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			// this.setVlanPoorMessage();
			e.printStackTrace();
		}
		return vlanIdList;
	}

	// 类型 1：PrivateNetwork，2：StorageNetwork //fix bug 3958
	public String checkUserVlanId() {
		flag = 1;
		int userId = 0;
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user != null) {
			userId = user.getId();
		}
		try {
			// 此处陈强来修改, resourcePoolID 将从用户组中获取
			int resourcePoolID = 1;
			vlanIdList = nicsService.getAvailableNetworkByUserId(userId, vethAdaptorNum, total, zoneId, resourcePoolID);
			if (null != vlanIdList && !vlanIdList.isEmpty()) {
				if (0 == vlanIdList.get(0)) {
					flag = 0;
				}
				if (vlanIdList.size() == 2) {
					if (0 == vlanIdList.get(1)) {
						flag = 0;
					}
				}
			}
			// fix bug 3932
			else {
				flag = 0;
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			flag = 0;
			e.printStackTrace();
		}
		return SUCCESS;
	}

	private TProductInstanceInfoRefBO getProductInstanceInfoRef(TVmInfo vminfo, TUserBO user) {
		// Pass实例对象
		TProductInstanceInfoRefBO piRef = new TProductInstanceInfoRefBO();
		piRef.setPiName(vminfo.getInstanceName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 服务实例编码
		String picode = sdf.format(new Date()) + (int) ((Math.random() * 9 + 1) * 100000); // 日期+6位随机数
		int userid = 0;
		if (user != null) {
			userid = user.getId();
		}
		piRef.setPiCode(userid + "_" + picode);
		piRef.setProductId(vminfo.getProductId());
		piRef.setTemplateId(vminfo.getTemplateId());
		piRef.setPiId(1);// 默认为服务实例
		piRef.setDescription(vminfo.getDescription());// 服务描述
		return piRef;
	}

	// 50.获取Paas服务实例
	private String getInstanceInfoOfVMS(TVmInfo vminfo, List<TInstanceInfoBO> vms, TUserBO user) throws Exception {
		int productId = vminfo.getProductId();
		Product product = productService.getProduct(productId);
		List<TTemplateVMBO> templateVMList = product.getTemplates();
		//		int dhcpValue = orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
		int dhcpValue = ConstDef.getNicsDhcpSwitch();
		int j = 0;
		int index = 0;
		int nameIndex = 0;
		// 虚机总数
		if (dhcpValue == 1) {
			int total = this.getTotalNumMultiVM(vminfo);
			int vethAdaptorNum = this.getMaxVethAdaptorNum(templateVMList);
			if (templateVMList != null && templateVMList.size() > 0) {
				int resourcePoolsId = templateVMList.get(0).getResourcePoolsId();
				List<Long> vlanIdList = this.getUserVlanId(vethAdaptorNum, total, vminfo.getZoneId(), resourcePoolsId);
				if (null != vlanIdList && !vlanIdList.isEmpty()) {
					vminfo.setVlan(vlanIdList.get(0));
					if (vlanIdList.size() == 2) {
						vminfo.setVlan2(vlanIdList.get(1));
					}
				}
			}

		}
		for (TTemplateVMBO vm : templateVMList) {
			int template_type = vm.getType();
			// 由UI输入的购买数量重设
			vm.setTemplateNum(vminfo.getTemplateNum().get(j));
			j++;
			//fix bug:7773
			TVmInfo vminfoPass = new TVmInfo();
			BeanUtils.copyProperties(vminfoPass, vminfo);
			vminfoPass.setTemplateId(vm.getId());
			// Pass实例对象
			// TProductInstanceInfoRefBO piRef = new
			// TProductInstanceInfoRefBO();
			// piRef.setPiName(vminfo.getInstanceName());
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//
			// 服务实例编码
			// String picode = sdf.format(new Date()) + (int) ((Math.random() *
			// 9 + 1) * 100000); // 日期+6位随机数
			// piRef.setPiCode(user.getId() + "_" + picode);
			// piRef.setProductId(vminfo.getProductId());
			// piRef.setTemplateId(vminfo.getTemplateId());

			List<String> vmNameArray = vminfo.getVmNameArray();
			List<String> vmDescArray = vminfo.getVmDescArray();
			for (int i = 1; i <= vm.getTemplateNum(); i++) {
				TProductInstanceInfoRefBO piRef = getProductInstanceInfoRef(vminfo, user);
				// 多虚机的虚拟机名称,描述均由UI设置 update by CQ
				vminfoPass.setFlag(vmNameArray.get(nameIndex));
				vminfoPass.setInstanceName(vmNameArray.get(nameIndex));
				vminfoPass.setDescription(vmDescArray.get(nameIndex));
				vminfoPass.setVethAdaptorNum(vm.getVethAdaptorNum());
				nameIndex++;
				if (template_type == 10){
					index = this.setInfoForMultiPM(index, vminfoPass, vminfo);
					index++;
				}else if (template_type == 3){
					index = this.setInfoForMultiMC(index, vminfoPass, vminfo);
					index++;
				}
				// 0手工设置IP 1DHCP
				else if (template_type == 1 && dhcpValue == 0 ) {
					index = this.setInfoForMultiVM(index, vminfoPass, vminfo);
					index++;
				} else if (template_type == 1 ){
					//					index = this.setInfoForMultiVMDHCP(index, vminfoPass, vminfo);
					//					index++;
				}
				if (j == 1 && i == 1) {
					piRef.setPiId(2);// 主服务实例(paas)
				} else {
					piRef.setPiId(0);// 辅助管理服务实例
				}
				if (template_type ==1){
					this.getInstanceInfoVM(vminfoPass, vms, user, piRef, 2);
				}else if (template_type ==3){
					this.getInstanceInfoMC(vminfoPass, vms, piRef, 2);
				}else if (template_type ==10){
					this.getInstanceInfoPhysicalMC(vminfoPass, vms, piRef, 2);
				}
			}
		}
		return SUCCESS;
	}

	// 13.获取nas资源实例
	private void getInstanceInfoNas(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		// 判断是否为特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			nasResourceService.creatSpecalNasTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			throw new Exception("no find templateinfo by templateId : " + vminfo.getTemplateId());
		}

		if (vminfo.getTemplateId() == 1) {
			map.put("storageSize", vminfo.getStorageSize());
		} else {
			map.put("storageSize", templateInfo.getStorageSize());
		}
		map.put("account", user.getAccount());
		// 购买周期
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);

		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setResourceInfo(parameter);
		instanceInfo.setProductId(vminfo.getProductId());

		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 判断是否为自定义存储(==1为自定义存储)
		if (vminfo.getTemplateId() == 1) {
			instanceInfo.setStorageSize(vminfo.getStorageSize());
		} else {
			instanceInfo.setStorageSize(templateInfo.getStorageSize());
		}
		instanceInfo.setZoneId(Long.valueOf(templateInfo.getZoneId()));

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		vms.add(instanceInfo);
		// return SUCCESS;
	}

	// 获取虚机总数
	private int getTotalNumMultiVM(TVmInfo vminfo) {
		int total = 0;
		for (int i = 0; i < vminfo.getTemplateNum().size(); i++) {
			total += vminfo.getTemplateNum().get(i);
		}
		return total;
	}

	// 获取遍历服务中模板,取每个模版的最大网卡数,可能为2
	private int getMaxVethAdaptorNum(List<TTemplateVMBO> templateVMList) {
		int vethAdaptorNum = 1;
		for (TTemplateVMBO tt : templateVMList) {
			if (tt.getVethAdaptorNum() == 2) {
				vethAdaptorNum = 2;
				break;
			}
		}
		return vethAdaptorNum;
	}

	// 多虚机，非DHCP，手工配置网络
	private int setInfoForMultiPM(int index, TVmInfo vminfoPass, TVmInfo vminfo) {
		List<String> ipArrayNew = new ArrayList<String>();
		List<String> vlanArrayNew = new ArrayList<String>();
		List<String> ipArray = vminfo.getIpArray();
		List<String> vlanArray = vminfo.getVlanArray();
		for(int num =0 ;num < vminfo.getVethAdaptorNum() ; num++){
			if (num >= 1)
			{
				index++;
			}
			ipArrayNew.add(ipArray.get(index));
			vlanArrayNew.add(vlanArray.get(index));
		}
		vminfoPass.setVlanArray(vlanArrayNew);
		vminfoPass.setIpArray(ipArrayNew);
		return index;
	}

	// 多虚机，非DHCP，手工配置网络
	private int setInfoForMultiMC(int index, TVmInfo vminfoPass, TVmInfo vminfo) {
		List<String> ipArray = vminfo.getIpArray();
		List<String> vlanArray = vminfo.getVlanArray();
		vminfoPass.setIpAddress(ipArray.get(index));
		vminfoPass.setVlan(Integer.parseInt(vlanArray.get(index)));
		return index;
	}

	// 多虚机，非DHCP，手工配置网络
	private int setInfoForMultiVM(int index, TVmInfo vminfoPass, TVmInfo vminfo) {
		List<String> ipArray = vminfo.getIpArray();
		List<String> vlanArray = vminfo.getVlanArray();
		if (vminfoPass.getVethAdaptorNum() >= 1) {
			vminfoPass.setIpAddress(ipArray.get(index));
			vminfoPass.setVlan(Integer.parseInt(vlanArray.get(index)));
		}
		if (vminfoPass.getVethAdaptorNum() >= 2) {
			index++;
			vminfoPass.setIpAddress2(ipArray.get(index));
			vminfoPass.setVlan2(Integer.parseInt(vlanArray.get(index)));
		}
		if (vminfoPass.getVethAdaptorNum() >= 3) {
			index++;
			vminfoPass.setIpAddress3(ipArray.get(index));
			vminfoPass.setVlan3(Integer.parseInt(vlanArray.get(index)));
		}
		if (vminfoPass.getVethAdaptorNum() >= 4) {
			index++;
			vminfoPass.setIpAddress4(ipArray.get(index));
			vminfoPass.setVlan4(Integer.parseInt(vlanArray.get(index)));
		}
		return index;
	}

	// 1.获取虚拟机服务实例
	private String getInstanceInfoVM(TVmInfo vminfo, List<TInstanceInfoBO> vms, TUserBO user, TProductInstanceInfoRefBO piRef, int type)
	throws Exception {

		if (vminfo.getTemplateId() <= 0) {

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			vmModifyService.creatSpecalVMTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}

		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}

		map.put("cpuNum", templateInfo.getCpuNum());
		map.put("memorySize", templateInfo.getMemorySize());
		map.put("zoneId", vminfo.getZoneId());
		map.put("account", user.getAccount());
		map.put("period", Integer.valueOf(vminfo.getPeriod()) + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);

		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		String instanceName = vminfo.getInstanceName();
		// 对paas的处理
		if (piRef != null) {// 多虚机
			// 多虚机的虚拟机名称,描述均由UI设置 update by CQ
			// int dhcpValue =
			// orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
			// if (dhcpValue == 0) {
			// instanceName = vminfo.getFlag();
			// } else {
			// instanceName = vminfo.getInstanceName() + "_" +
			// vminfo.getProductId() + "_" + vminfo.getFlag();
			// }
			// fix bug 3699
			piRef.setCharge(vminfo.getCharge());
			instanceName = vminfo.getFlag();
			instanceInfo.setProductInstanceInfoRefBO(piRef);
			// 购买周期
			instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));
			// 设置模板网卡个数
			vminfo.setVethAdaptorNum(templateInfo.getVethAdaptorNum());
		} else {// 单虚机
			TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
			instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
			// 购买周期
			instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));
		}

		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(TEMPLATE_TYPE_VM);
		instanceInfo.setInstanceName(instanceName);
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setResourceInfo(parameter);
		// 产品ID
		instanceInfo.setProductId(vminfo.getProductId());
		// 虚机信息
		instanceInfo.setZoneId(Long.valueOf(vminfo.getZoneId()));
		instanceInfo.setOsDesc(templateInfo.getVmos());
		instanceInfo.setStorageSize(0);
		instanceInfo.setCpuNum(templateInfo.getCpuNum());
		instanceInfo.setMemorySize(templateInfo.getMemorySize());
		instanceInfo.setCpufrequency(0);
		instanceInfo.seteOsId(templateInfo.geteOsId());

		// 虚机过期时间
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 网卡信息
		List<TNicsBO> nicsList = new ArrayList<TNicsBO>();
		// 获取网卡信息,区分虚机和多虚机
		if (1 == type) {
			this.getNicsInfo(vminfo, nicsList, templateInfo.getResourcePoolsId());
		} else if (2 == type) {
			this.getNicsInfoForMulti(vminfo, nicsList);
		}
		instanceInfo.setNicsBOs(nicsList);

		// 虚机挂硬盘
		if (templateType == 1 && vminfo.getStorageSize() > 0) {
			TVmInfo storageInfo = vminfo;
			List<TInstanceInfoBO> storageInstances = new ArrayList<TInstanceInfoBO>();
			// 虚机挂硬盘，默认ID为1
			storageInfo.setTemplateId(1);
			getInstanceInfoVdisk(storageInfo, storageInstances, user);
			// 虚机实例挂硬盘对象
			instanceInfo.setStorageInstances(storageInstances);
		}
		// JOB信息
		AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		String parameters = "";// 操作参数，调用api所需要的参数
		String ResourceTemplateID = ConstDef.getVDCTemplateID(vminfo.getTemplateType(), vminfo.getTemplateId());
		int Count = 1;
		String VLanID = String.valueOf(vminfo.getVlan());
		//		int dhcpValue = orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
		int dhcpValue = ConstDef.getNicsDhcpSwitch();
		if (dhcpValue == 1) {// 动态获取IP
			int userVlanId = nicsService.searchVlanIdByUserId(user.getId());
			VLanID = String.valueOf(userVlanId);
		}
		List<String> VMNames = new ArrayList<String>();
		VMNames.add(instanceName);
		Map<String, Object> mapJob = new HashMap<String, Object>();
		// mapJob.put(key, value)
		mapJob.put("ResourceTemplateID", ResourceTemplateID);
		mapJob.put("Count", Count);
		mapJob.put("VLanID", VLanID);
		mapJob.put("VMNames", VMNames);
		mapJob.put("UserID", user.getId());
		parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
		asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		asynJobVDCPO.setParameter(parameters);
		asynJobVDCPO.setUser_id(this.getUserId());
		asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		asynJobVDCPO.setOperation(OperationType.CREATE_VM);
		instanceInfo.setAsynJobVDCPO(asynJobVDCPO);
		// JOB结束

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("虚拟机资源");
		userLogB.setFunctionName("虚拟机申请");
		userLogB.setClassName("OrderAction.java");
		userLogB.setMethodName("getInstanceInfoVM()");
		userLogB.setType(1);
		userLogB.setParameters("type=" + vminfo.getTemplateType() + ",id=" + vminfo.getTemplateId());
		userLogB.setComment("向job表插入数据,asyncJobVDCService.insterAsyncJobVDC()");
		userLogB.setMemo(parameters);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);
		// 用户操作日志封装结束
		vms.add(instanceInfo);
		return SUCCESS;
	}

	// 获取到期日期
	private static Date getExpireDate(int period, String unit) {
		if (period <= 0 || unit == null || unit.equals("")) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		int day = 0;
		if (unit.equals("Y")) {
			day = calendar.get(Calendar.YEAR);
			calendar.set(Calendar.YEAR, day + period);
		} else if (unit.equals("M")) {
			day = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, day + period);
		} else if (unit.equals("W")) {
			day = calendar.get(Calendar.WEEK_OF_YEAR);
			calendar.set(Calendar.WEEK_OF_YEAR, day + period);
		} else if (unit.equals("D")) {
			day = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, day + period);
		} else if (unit.equals("H")) {
			day = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.set(Calendar.HOUR_OF_DAY, day + period);
		} else {
			return null;
		}
		return calendar.getTime();
	}

	// 2.获取虚拟硬盘资源实例
	private String getInstanceInfoVdisk(TVmInfo vminfo, List<TInstanceInfoBO> vms, TUserBO user) throws Exception {

		if (vminfo.getTemplateId() <= 0) {

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			vmModifyService.creatSpecalVdiskTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}

		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}

		if (vminfo.getTemplateId() == 1) {
			map.put("storageSize", vminfo.getStorageSize());
		} else {
			map.put("storageSize", templateInfo.getStorageSize());
		}
		map.put("zoneId", vminfo.getZoneId());
		map.put("account", user.getAccount());
		// 购买周期
		// map.put("period", vminfo.getPeriodInfo());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);

		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(TEMPLATE_TYPE_EBS);
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setResourceInfo(parameter);
		instanceInfo.setProductId(vminfo.getProductId());

		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 判断是否为自定义存储(==1为自定义存储)
		if (vminfo.getTemplateId() == 1) {
			instanceInfo.setStorageSize(vminfo.getStorageSize());
		} else {
			instanceInfo.setStorageSize(templateInfo.getStorageSize());
		}
		instanceInfo.setZoneId(Long.valueOf(vminfo.getZoneId()));
		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// JOB信息封装开始
		AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		String parameters = "";// 操作参数，调用api所需要的参数
		String ResourceTemplateID = ConstDef.getVDCTemplateID(vminfo.getTemplateType(), vminfo.getTemplateId());
		List<String> VMNames = new ArrayList<String>();
		VMNames.add(vminfo.getInstanceName());
		Map<String, Object> mapJob = new HashMap<String, Object>();
		mapJob.put("BSTemplateId", ResourceTemplateID);
		mapJob.put("BSName", vminfo.getInstanceName());
		// mapJob.put("Grade", templateInfo.getGrade());
		mapJob.put("UserID", user.getId());
		parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
		asynJobVDCPO.setParameter(parameters);
		asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		asynJobVDCPO.setUser_id(this.getUserId());
		asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		asynJobVDCPO.setOperation(OperationType.CREATE_EBS);
		instanceInfo.setAsynJobVDCPO(asynJobVDCPO);
		// JOB信息封装结束

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("虚拟硬盘资源");
		userLogB.setFunctionName("虚拟硬盘申请");
		userLogB.setClassName("OrderAction.java");
		userLogB.setMethodName("getInstanceInfoEBS()");
		userLogB.setType(1);
		userLogB.setParameters("type=" + vminfo.getTemplateType() + ",id=" + vminfo.getTemplateId());
		userLogB.setComment("向job表插入数据,asyncJobVDCService.insterAsyncJobVDC()");
		userLogB.setMemo(parameters);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);
		// 用户操作日志封装结束
		vms.add(instanceInfo);
		return SUCCESS;
	}

	// 12.获取弹性块存储资源实例
	private String getInstanceInfoIpSan(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		// 判断是否为特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			//修改raid级别
			String raid = vminfo.getExtendAttrJson();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raid", raid);
			String extendAttrJSON = JsonUtil.getJsonString4JavaPOJO(map);
		    templateSpecal.setExtendAttrJSON(extendAttrJSON);
			nasResourceService.creatSpecalIpSanTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}

		if (vminfo.getTemplateId() == 1) {
			map.put("storageSize", vminfo.getStorageSize());
		} else {
			map.put("storageSize", templateInfo.getStorageSize());
		}
		// map.put("zoneId",vminfo.getZoneId());
		map.put("account", user.getAccount());
		// 购买周期
		// map.put("period", vminfo.getPeriodInfo());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);

		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(VM_TEMPLATE_TYPE_DISK);
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setResourceInfo(parameter);
		instanceInfo.setProductId(vminfo.getProductId());

		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 判断是否为自定义存储(==1为自定义存储)
		if (vminfo.getTemplateId() == 1) {
			instanceInfo.setStorageSize(vminfo.getStorageSize());
		} else {
			instanceInfo.setStorageSize(templateInfo.getStorageSize());
		}
		instanceInfo.setZoneId(Long.valueOf(0));

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// JOB信息封装开始
		AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		String parameters = "";// 操作参数，调用api所需要的参数
		String ResourceTemplateID = ConstDef.getVDCTemplateID(vminfo.getTemplateType(), vminfo.getTemplateId());
		List<String> VMNames = new ArrayList<String>();
		VMNames.add(vminfo.getInstanceName());
		Map<String, Object> mapJob = new HashMap<String, Object>();
		mapJob.put("BSTemplateId", ResourceTemplateID);
		mapJob.put("BSName", vminfo.getInstanceName());
		mapJob.put("UserID", user.getId());
		parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
		asynJobVDCPO.setParameter(parameters);
		asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		asynJobVDCPO.setUser_id(this.getUserId());
		asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		asynJobVDCPO.setOperation(OperationType.CREATE_EBS);
		instanceInfo.setAsynJobVDCPO(asynJobVDCPO);
		// JOB信息封装结束

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("块存储资源");
		userLogB.setFunctionName("块存储申请");
		userLogB.setClassName("OrderAction.java");
		userLogB.setMethodName("getInstanceInfoEBS()");
		userLogB.setType(1);
		userLogB.setParameters("type=" + vminfo.getTemplateType() + ",id=" + vminfo.getTemplateId());
		userLogB.setComment("向job表插入数据,asyncJobVDCService.insterAsyncJobVDC()");
		userLogB.setMemo(parameters);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);
		// 用户操作日志封装结束
		vms.add(instanceInfo);
		return SUCCESS;
	}

	// 3.获取小型机服务实例
	private String getInstanceInfoMC(TVmInfo vminfo, List<TInstanceInfoBO> vms, TProductInstanceInfoRefBO piRef, int type) throws Exception {

		// 判断是否为小型机的特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象
			TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);

			// 获取特殊模板
			TTemplateMCBO templateSpecal = new TTemplateMCBO();
			Product productSpecal = new Product();
			MCTemplateService.creatSpecalMCTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.MCTemplateService.createTemplate(templateSpecal);

			//保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}

		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateMCBO templateInfo = this.MCTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		map.put("cpuNum", templateInfo.getCpuNum());
		map.put("cpuType", templateInfo.getCputype());
		map.put("memorySize", templateInfo.getMemorySize());
		map.put("storageSize", templateInfo.getStorageSize());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);

		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(TEMPLATE_TYPE_MC);
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setResourceInfo(parameter);
		instanceInfo.setOsDesc(templateInfo.getVmos());
		instanceInfo.setCpuNum(templateInfo.getCpuNum());
		instanceInfo.setMemorySize(templateInfo.getMemorySize());
		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		instanceInfo.setProductId(vminfo.getProductId());
		// 小型机过期时间
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}
		// 网卡信息
		List<TNicsBO> nicsList = new ArrayList<TNicsBO>();
		if (null != vminfo.getIpAddress()) {
			TNicsBO nics = new TNicsBO();
			nics.seteVlanId(vminfo.getVlan());
			nics.setIp(vminfo.getIpAddress());
			nicsList.add(nics);
		}
		instanceInfo.setNicsBOs(nicsList);

		if (piRef != null) {// 多实例
			piRef.setCharge(vminfo.getCharge());
			instanceInfo.setProductInstanceInfoRefBO(piRef);
			// 购买周期
			instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));
			// 设置模板网卡个数
			vminfo.setVethAdaptorNum(templateInfo.getVethAdaptorNum());
		} else {// 单机
			// 服务实例
			TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
			instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
			// 购买周期
			instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));
		}


		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 4.获取备份服务实例
	private String getInstanceInfoBackup(TVmInfo vminfo, List<TInstanceInfoBO> vms,  TUserBO user) throws Exception {

		if (vminfo.getTemplateId() <= 0) {

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			vmModifyService.creatSpecalBKTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}

		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 4:备份服务
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}

		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());
		// 备份服务->磁盘大小
		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		map.put("storageSize", templateInfo.getStorageSize());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);

		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// // JOB信息
		// AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		//
		// asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		// String parameters = "";
		// Map<String, Object> mapJob = new HashMap<String, Object>();
		// mapJob.put("VMID", vminfo.getIpAddress());
		// parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
		// asynJobVDCPO.setParameter(parameters);
		// asynJobVDCPO.setUser_id(this.getUserId());
		// asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		// asynJobVDCPO.setOperation(OperationType.CREATE_VM);
		// instanceInfo.setAsynJobVDCPO(asynJobVDCPO);

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("备份服务");
		userLogB.setFunctionName("备份服务申请");
		userLogB.setClassName("OrderAction.java");
		userLogB.setMethodName("getInstanceInfoBackup()");
		userLogB.setType(1);
		userLogB.setParameters("type=" + vminfo.getTemplateType() + ",id=" + vminfo.getTemplateId());
		userLogB.setComment("向job表插入数据,asyncJobVDCService.insterAsyncJobVDC()");
		userLogB.setMemo(parameter);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);
		// 用户操作日志封装结束
		vms.add(instanceInfo);
		return SUCCESS;
	}

	// 5.获取监控服务实例
	private String getInstanceInfoMonitor(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 5:监控服务
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());
		// 5:监控服务->监控资源
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);

		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("云监控服务");
		userLogB.setFunctionName("云监控申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoMonitor()");
		userLogB.setType(1);
		String params = "ResourceTemplateID=" + vminfo.getTemplateId() + ",name=" + vminfo.getInstanceName() + ",productId=" + vminfo.getProductId()
		+ ",type=" + templateInfo.getType() + ",pl=" + vminfo.getFrequency();
		userLogB.setParameters(params);
		userLogB.setComment("将参数存入数据库InstanceInfo表中");
		String memo = templateInfo.getNetworkDesc();
		userLogB.setMemo(memo);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);

		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 6.获取负载均衡服务实例
	private String getInstanceInfoLoadBalance(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {
		// 6:负载均衡服务
		// 判断是否为特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象
			TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			loadbalanceH3Cservice.creatSpecalLoadbalanceTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());
		// 6:负载均衡并发数
		instanceInfo.setStorageSize(templateInfo.getStorageSize());

		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		//修改端口号模式
		//map.put("flag", vminfo.getOsId());
		if(StringUtils.isNotEmpty(vminfo.getIpAddress())){
			map.put("vsIp", vminfo.getIpAddress());
			map.put("vsPort", vminfo.getPort());
		}		
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);
		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("负载均衡服务");
		userLogB.setFunctionName("负载均衡申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoLoadBalance()");
		userLogB.setType(1);
		String params = "ResourceTemplateID=" + vminfo.getTemplateId() + ",name=" + vminfo.getInstanceName() + ",productId=" + vminfo.getProductId()
		+ ",type=" + templateInfo.getType();
		userLogB.setParameters(params);
		userLogB.setComment("将参数存入数据库InstanceInfo表中");
		String memo = "负载均衡并发数：" + templateInfo.getStorageSize();
		userLogB.setMemo(memo);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);
		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 7.获取防火墙资源服务实例
	private String getInstanceInfoFireWall(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {

		// 判断是否为特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象
			TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			firewallService.creatSpecalFirewallTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}
		// 7:防火墙资源服务
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());
		// 7:防火墙规则条数
		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);
		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("防火墙服务");
		userLogB.setFunctionName("防火墙申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoFireWall()");
		userLogB.setType(1);
		String params = "ResourceTemplateID=" + vminfo.getTemplateId() + ",name=" + vminfo.getInstanceName() + ",productId=" + vminfo.getProductId()
		+ ",type=" + templateInfo.getType();
		userLogB.setParameters(params);
		userLogB.setComment("将参数存入数据库InstanceInfo表中");
		String memo = "防火墙规则条数：" + templateInfo.getStorageSize();
		userLogB.setMemo(memo);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);

		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 11.获取对象存储实例
	private String getInstanceInfoObjetStorage(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		// 判断是否为特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			nasResourceService.creatSpecalObjectStorageTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());

		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);
		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("对象存储服务");
		userLogB.setFunctionName("对象存储申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoObjetStorage()");
		userLogB.setType(1);
		String params = "ResourceTemplateID=" + vminfo.getTemplateId() + ",name=" + vminfo.getInstanceName() + ",productId=" + vminfo.getProductId()
		+ ",type=" + templateInfo.getType();
		userLogB.setParameters(params);
		userLogB.setComment("将参数存入数据库InstanceInfo表中");
		String memo = "" + templateInfo.getStorageSize();
		userLogB.setMemo(memo);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);

		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 8.获取带宽资源服务实例
	private String getInstanceInfoLoadBandWidth(TVmInfo vminfo, List<TInstanceInfoBO> vms, TUserBO user) throws Exception {

		// 判断是否为特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();

			templateSpecal.setCode(null);// 模板编码
			// 特殊模板 1：用户定义的特殊模板
			templateSpecal.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);
			templateSpecal.setCpufrequency(0);
			templateSpecal.setCpuNum(0);
			templateSpecal.setMemorySize(0);
			vminfo.setDisknumber(0);
			templateSpecal.setStorageSize(vminfo.getStorageSize());//带宽大小
			templateSpecal.setVethAdaptorNum(0);
			if (user != null) {
				templateSpecal.setCreatorUserId(user.getId());
			}
			// template.setZoneId(vminfo.getZoneId());
			templateSpecal.setResourcePoolsId(vminfo.getPoolId());
			templateSpecal.setType(vminfo.getTemplateType());
			templateSpecal.setOperType(1);
			Timestamp ts = new Timestamp(new Date().getTime());
			templateSpecal.setCreateTime(ts.toString());
			templateSpecal.setMeasureMode("Duration");// Duration：按时长计量
			templateSpecal.setState(ConstDef.STATE_TWO);//可用状态modify by hfk 13-1-22
			templateSpecal.seteOsId(0);
			// 根据物理机创建模板,操作系统信息组织要创建服务信息
			productSpecal.setName("BandWidth");
			productSpecal.setCreateDate(ts);
			productSpecal.setType(templateSpecal.getType());
			productSpecal.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
			productSpecal.setDescription("公网带宽自动创建服务");
			productSpecal.setSpecification(productSpecal.getDescription());
			productSpecal.setQuotaNum(1);
			productSpecal.setPrice(0f);
			productSpecal.setUnit(vminfo.getUnit());
			productSpecal.setOperateType(1);
			productSpecal.setIsDefault(0);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			// 保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}

		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 8:带宽资源服务
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());
		// 8:带宽大小Mbps
		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		// 8:带宽IP地址
		if (templateType == 8) {
			map.put("ipAddress", vminfo.getIpAddress());
			map.put("instanceId", vminfo.getIpInstanceId());
			// map.put("period", vminfo.getPeriodInfo());
			map.put("period", vminfo.getPeriod() + vminfo.getUnit());
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfo.setResourceInfo(parameter);
		}

		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod().replace("Y", ""));
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// JOB信息
		AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		String parameters = "";// 操作参数，调用api所需要的参数
		String ResourceTemplateID = ConstDef.getVDCTemplateID(vminfo.getTemplateType(), vminfo.getTemplateId());
		Map<String, Object> mapJob = new HashMap<String, Object>();
		mapJob.put("BWTemplateID", ResourceTemplateID);
		mapJob.put("IP", vminfo.getIpAddress());
		mapJob.put("UserID", user.getId());
		parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
		asynJobVDCPO.setParameter(parameters);
		asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		asynJobVDCPO.setUser_id(this.getUserId());
		asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		asynJobVDCPO.setTemplate_res_id(ResourceTemplateID);
		asynJobVDCPO.setOperation(OperationType.CREATE_BANDWIDTH);
		instanceInfo.setAsynJobVDCPO(asynJobVDCPO);

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("公网带宽服务");
		userLogB.setFunctionName("公网带宽申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoLoadBandWidth()");
		userLogB.setType(1);
		String params = "ResourceTemplateID=" + ResourceTemplateID + ",name=" + vminfo.getInstanceName() + ",storageSize=" + vminfo.getStorageSize()
		+ ",insId=" + vminfo.getTarget() + ",type=" + vminfo.getVstype() + ",pl=" + vminfo.getFrequency() + ",insName="
		+ vminfo.getTargetName();
		userLogB.setParameters(params);
		userLogB.setComment(vminfo.getComment());
		userLogB.setMemo(parameters);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);

		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 9.获取公网IP资源服务实例
	private String getInstanceInfoPublicIP(TVmInfo vminfo, List<TInstanceInfoBO> vms, TUserBO user) throws Exception {
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 9:公网IP资源服务
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.seteNetworkId(vminfo.getIpAddressId());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setProductId(vminfo.getProductId());
		// 9:公网IP资源服务->IP类型
		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		// 9:公网IP资源服务->提供商，ip地址

		map.put("serviceProvider", vminfo.getZoneId()); // 供应商编码
		map.put("ipAddressId", vminfo.getIpAddressId()); // 公网IP ID
		map.put("ipAddress", vminfo.getIpAddress()); // 公网ip地址
		map.put("ipType", vminfo.getIpType()); // 公网IP类型，IPV4或IPV6
		map.put("period", vminfo.getPeriod() + vminfo.getUnit()); // 购买周期
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);
		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// JOB信息
		AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		String parameters = "";// 操作参数，调用api所需要的参数
		String ResourceTemplateID = ConstDef.getVDCTemplateID(vminfo.getTemplateType(), vminfo.getTemplateId());
		Map<String, Object> mapJob = new HashMap<String, Object>();
		mapJob.put("IPTemplateID", ResourceTemplateID);
		mapJob.put("UserID", user.getId());
		parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
		asynJobVDCPO.setParameter(parameters);
		asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		asynJobVDCPO.setUser_id(this.getUserId());
		asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		asynJobVDCPO.setOperation(OperationType.CREATE_WANIP);
		instanceInfo.setAsynJobVDCPO(asynJobVDCPO);

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("弹性公网IP资源管理");
		userLogB.setFunctionName("弹性公网IP地址申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoPublicIP");
		userLogB.setType(1);
		userLogB.setParameters("IPTemplateID=" + ResourceTemplateID);
		userLogB.setComment("通过弹性公网IP资源模板编码申请一个弹性公网IP地址");
		userLogB.setMemo("通过弹性公网IP资源模板编码申请一个弹性公网IP地址");
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);

		vms.add(instanceInfo);

		return SUCCESS;
	}

	// 15.获取云数据备份服务实例
	private String getInstanceInfoDataBackup(TVmInfo vminfo, List<TInstanceInfoBO> vms) throws Exception {
		String parameter = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 15:云数据备份服务
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		map.put("name", vminfo.getInstanceName());
		map.put("storageSize", vminfo.getStorageSize());
		map.put("type", vminfo.getVstype());
		map.put("insId", vminfo.getTarget());
		if (vminfo.getBackupPath() != null) {
			map.put("url", vminfo.getBackupPath());
		}
		map.put("pl", vminfo.getFrequency());
		map.put("plunit", vminfo.getPlunit());
		map.put("decs", vminfo.getComment());
		map.put("insName", vminfo.getTargetName());
		map.put("period", vminfo.getPeriod() + vminfo.getUnit());
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getComment());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setResourceInfo(parameter);
		instanceInfo.setStorageSize(vminfo.getStorageSize());
		instanceInfo.setProductId(vminfo.getProductId());
		// 获取到期日期
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}

		// 服务实例
		TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
		instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
		// 购买周期
		instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));

		// JOB信息
		AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
		String jobparameter = "";// 操作参数，调用api所需要的参数
		String ResourceTemplateID = ConstDef.getVDCTemplateID(vminfo.getTemplateType(), vminfo.getTemplateId());
		int Count = 1;
		Map<String, Object> mapJob = new HashMap<String, Object>();
		mapJob.put("ResourceTemplateID", ResourceTemplateID);
		mapJob.put("Count", Count);
		mapJob.put("name", vminfo.getInstanceName());
		mapJob.put("storageSize", vminfo.getStorageSize());
		mapJob.put("type", vminfo.getVstype());
		mapJob.put("insId", vminfo.getTarget());
		if (vminfo.getBackupPath() != null) {
			mapJob.put("url", vminfo.getBackupPath());
		}
		mapJob.put("pl", vminfo.getFrequency());
		mapJob.put("plunit", vminfo.getPlunit());
		mapJob.put("decs", vminfo.getComment());
		mapJob.put("insName", vminfo.getTargetName());
		mapJob.put("period", vminfo.getPeriod());
		jobparameter = JsonUtil.getJsonString4JavaPOJO(mapJob);
		asynJobVDCPO.setParameter(jobparameter);
		asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
		asynJobVDCPO.setUser_id(this.getUserId());
		asynJobVDCPO.setTemplate_id(vminfo.getTemplateId());
		asynJobVDCPO.setOperation(OperationType.CREATE_DATABAK);
		instanceInfo.setAsynJobVDCPO(asynJobVDCPO);

		// 用户操作日志封装
		TUserLogBase userLogB = new TUserLogVO();
		userLogB.setModuleName("数据云备份");
		userLogB.setFunctionName("申请");
		userLogB.setClassName("OrderAction");
		userLogB.setMethodName("getInstanceInfoDataBackup()");
		userLogB.setType(1);
		String params = "ResourceTemplateID=" + ResourceTemplateID + ",name=" + vminfo.getInstanceName() + ",storageSize=" + vminfo.getStorageSize()
		+ ",insId=" + vminfo.getTarget() + ",type=" + vminfo.getVstype() + ",url=" + vminfo.getBackupPath() == null ? "" : vminfo
				.getBackupPath() + ",pl=" + vminfo.getFrequency() + ",decs=" + vminfo.getComment() + ",insName=" + vminfo.getTargetName();
		userLogB.setParameters(params);
		userLogB.setComment("");
		userLogB.setMemo(jobparameter);
		instanceInfo.setUserLogVO((TUserLogVO) userLogB);

		vms.add(instanceInfo);
		return SUCCESS;
	}

	// 10.获取物理机资源服务实例
	private String getInstanceInfoPhysicalMC(TVmInfo vminfo, List<TInstanceInfoBO> vms, TProductInstanceInfoRefBO piRef, int type) throws Exception {

		// 判断是否为物理机的特殊模板申请
		if (vminfo.getTemplateId() <= 0) {
			// 订单对象
			TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);

			// 获取特殊模板
			TTemplateVMBO templateSpecal = new TTemplateVMBO();
			Product productSpecal = new Product();
			physicalMachineService.creatSpecalPhysicalTemplate(vminfo, user, templateSpecal, productSpecal);

			// 保存特殊模板的申请实例模板
			int template_id = this.VMTemplateService.createTemplate(templateSpecal);

			//保存特殊模板的申请服务实例product
			productSpecal.setTemplateId(template_id);
			int product_id = this.productService.insertProduct(productSpecal);

			// 更新最新模板ID与服务ID
			vminfo.setTemplateId(template_id);
			vminfo.setProductId(product_id);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		// 10:物理机资源服务
		TTemplateVMBO templateInfo = this.VMTemplateService.getTemplateById(vminfo.getTemplateId());
		if (null == templateInfo) {
			this.message = "保存失败";
			return ERROR;
		}
		// 实例对象
		TInstanceInfoBO instanceInfo = new TInstanceInfoBO();
		instanceInfo.setTemplateId(vminfo.getTemplateId());
		instanceInfo.setTemplateType(templateInfo.getType());
		instanceInfo.setInstanceName(vminfo.getInstanceName());
		instanceInfo.setComment(vminfo.getDescription());
		instanceInfo.setState(ORDER_STATE_APPLY);
		instanceInfo.setCreateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setLastupdateDt(new Timestamp(new Date().getTime()));
		instanceInfo.setZoneId(Long.valueOf(vminfo.getZoneId()));
		instanceInfo.setOsDesc(templateInfo.getVmos());
		instanceInfo.setStorageSize(templateInfo.getStorageSize());
		instanceInfo.setCpuNum(templateInfo.getCpuNum());
		instanceInfo.setMemorySize(templateInfo.getMemorySize());
		instanceInfo.setCpufrequency(0);
		instanceInfo.seteOsId(templateInfo.geteOsId());
		instanceInfo.setIpAddress(vminfo.getIpAddress());
		instanceInfo.setProductId(vminfo.getProductId());

		if (piRef != null) {// 多实例
			piRef.setCharge(vminfo.getCharge());
			instanceInfo.setProductInstanceInfoRefBO(piRef);
			// 购买周期
			instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));
			// 设置模板网卡个数
			vminfo.setVethAdaptorNum(templateInfo.getVethAdaptorNum());
		} else {// 单机
			// 服务实例
			TProductInstanceInfoRefBO piRefinfo = getProductInstanceInfoRef(vminfo, null);
			instanceInfo.setProductInstanceInfoRefBO(piRefinfo);
			// 购买周期
			instanceInfo.setPeriod(Integer.valueOf(vminfo.getPeriod()));
		}
		// 过期时间
		int period = Integer.valueOf(vminfo.getPeriod());
		String unit = vminfo.getUnit();
		Date expireDate = this.getExpireDate(period, unit);
		if (expireDate != null) {
			instanceInfo.setExpireDate(new Timestamp(expireDate.getTime()));
		}
		// fix bug 3396 物理机产品详情中缺少网卡-IP地址参数
		// 网卡信息
		List<TNicsBO> nicsList = new ArrayList<TNicsBO>();
		List<Map> mapList = new ArrayList<Map>();

		Map<String, Object> mapNicEach = new HashMap<String, Object>();
		TNicsBO eNics = null;
		int vlanId = 0;
		//to fix bug:4577
		int dhcpValue = 0;// this.orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
		if (dhcpValue == 0) {//1 ： 动态获取IP; 0 : 人工填写IP
			List<String> ipArray = vminfo.getIpArray();
			List<String> vlanArray = vminfo.getVlanArray();
			if (vminfo.getVethAdaptorNum() == vlanArray.size() && ipArray.size() == vlanArray.size())
			{
				for(int num =0 ;num < vminfo.getVethAdaptorNum() ; num++){
					eNics = new TNicsBO();
					if(vlanArray.get(num) != null) {
						vlanId = Integer.parseInt(vlanArray.get(num));
					}
					eNics.seteVlanId(vlanId);
					eNics.setIp(ipArray.get(num));

					mapNicEach = new HashMap<String, Object>();
					mapNicEach.put("vlanid", eNics.geteVlanId());
					mapNicEach.put("ip", eNics.getIp());
					mapList.add(mapNicEach);

					nicsList.add(eNics);
				}
			}
		} else {
			for(int num =0 ;num < vminfo.getVethAdaptorNum() ; num++){
				eNics = new TNicsBO();
				eNics.seteVlanId(vlanId);
				eNics.setIp("0");

				mapNicEach.put("vlanid", eNics.geteVlanId());
				mapNicEach.put("ip", eNics.getIp());
				mapList.add(mapNicEach);

				nicsList.add(eNics);
			}
		}

		String parameter = "";
		map.put("cpunumber", templateInfo.getCpuNum());
		map.put("cpuspeed", templateInfo.getCpufrequency());
		map.put("memory", templateInfo.getMemorySize());
		map.put("disknumber", vminfo.getDisknumber());
		map.put("disksize", vminfo.getStorageSize());
		map.put("ostype", templateInfo.geteOsId());
		map.put("nics", JSONArray.fromObject(mapList));
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfo.setResourceInfo(parameter);

		instanceInfo.setNicsBOs(nicsList);

		vms.add(instanceInfo);
		return SUCCESS;
	}

	/*
	 * 前台-我的订单-取消订单Action 首先判断系统类型 属于那种开关 VDC 还是 1.1
	 */
	public String cancelOrder() {
		// 判断系统类型
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user != null) {
			this.setUserId(user.getId());
		}
		try {
			TOrderBO order = orderService.selectOrderByOrderId(id);
			jobInstanceInfoService.deleteInstanceByOrder(order, user);
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return SUCCESS;
	}

	public String findAllTemplateVM() {
		try {
			int type = VM_TEMPLATE_TYPE_VM;
			String typeStr = ServletActionContext.getRequest().getParameter("type");
			if (typeStr != null) {
				type = Integer.parseInt(typeStr);
			}
			if (null == searchStr || ("").equals(searchStr)) {
				templateVMList = VMTemplateService.listTemplate(type, TEMPLATE_STATE_AVAILABLE, -1, -1, 0, 0);
			} else {
				templateVMList = VMTemplateService.searchTemplate(searchStr, type, TEMPLATE_STATE_AVAILABLE, -1, -1);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

  

	public String findAllTemplateService() {
		try {
			int type = Integer.parseInt(ServletActionContext.getRequest().getParameter("type"));
			if (null == searchStr || ("").equals(searchStr)) {
				templateVMList = VMTemplateService.listTemplate(type, TEMPLATE_STATE_AVAILABLE, -1, -1, 0, 0);
			} else {
				templateVMList = VMTemplateService.searchTemplate(searchStr, type, TEMPLATE_STATE_AVAILABLE, -1, -1);
			}
			return "findAllTemplateService";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	/**
	 * 虚拟机和 块存储的高级查询
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@LogInfo(desc = "type=1:虚机查询，=2:扩展存储查询", operateType = 4, moduleName = "服务目录", functionName = "虚机或扩展存储高级查询", parameters = "type")
	public String advancedSearchVMList() {
		int type = Integer.parseInt(ServletActionContext.getRequest().getParameter("type"));
		try {
			Map<String, String> map = null;
			if (StringUtils.isNotBlank(getQueryJson())) {
				map = JsonUtil.getMap4Json(queryJson);
				if (map != null && map.size() > 0) {
					String name = map.get("name");
					int osId = map.containsKey("vmos") ? Integer.parseInt(String.valueOf(map.get("vmos"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					int cpuNum = map.containsKey("cpuNum") ? Integer.parseInt(String.valueOf(map.get("cpuNum"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					int memSize = map.containsKey("memSize") ? Integer.parseInt(String.valueOf(map.get("memSize"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					int storage = map.containsKey("storage") ? Integer.parseInt(String.valueOf(map.get("storage"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					// templateVMList = VMTemplateService.advancedSearch(name,
					// type, TEMPLATE_STATE_AVAILABLE, cpuNum, memSize, osId,
					// storage, -1, -1);
					String networkDesc = map.containsKey("networkDesc") ? String.valueOf(map.get("networkDesc")) : "";
					templateVMList = VMTemplateService.advancedSearch(name, type, TEMPLATE_STATE_AVAILABLE, cpuNum, memSize, osId, storage,
					                                                  networkDesc, -1, -1);

				} else {
					message = "error : parameter is missing";
					logger.error(message);
					return ERROR;
				}
			} else {
				message = "error : parameter is missing";
				logger.error(message);
				return ERROR;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return "advancedSearchVMList";
	}

	@LogInfo(desc = "type=3:小型机查询", operateType = 4, moduleName = "服务目录", functionName = "小型机高级查询", parameters = "type")
	@SuppressWarnings("unchecked")
	public String advancedSearchMCList() {
		int type = Integer.parseInt(ServletActionContext.getRequest().getParameter("type"));
		try {
			Map<String, String> map = null;
			if (StringUtils.isNotBlank(getQueryJson())) {
				map = JsonUtil.getMap4Json(queryJson);
				if (map != null && map.size() > 0) {
					String name = map.get("name");
					int osId = map.containsKey("vmos") ? Integer.parseInt(String.valueOf(map.get("vmos"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					int cpuNum = map.containsKey("cpuNum") ? Integer.parseInt(String.valueOf(map.get("cpuNum"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					int memSize = map.containsKey("memSize") ? Integer.parseInt(String.valueOf(map.get("memSize"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					int storage = map.containsKey("storage") ? Integer.parseInt(String.valueOf(map.get("storage"))) : Constants.STATUS_COMMONS.IGNORE
							.getValue();
					templateMCList = MCTemplateService.advancedSearch(name, 3, TEMPLATE_STATE_AVAILABLE, cpuNum, memSize, storage, -1, -1);
				} else {
					message = "error : parameter is missing";
					logger.error(message);
					return ERROR;
				}
			} else {
				message = "error : parameter is missing";
				logger.error(message);
				return ERROR;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return "advancedSearchMCList";
	}

	public String findTemplateVMByID() {
		try {
			templateVM = VMTemplateService.getTemplateById(templateId);

			templateVMstr = JsonUtil.getJsonString4JavaPOJO(templateVM).toString();
			return SUCCESS;
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// 列出vlan列表
	public String findAllVlan() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				vlanList = null;
				return "findAllVlan";
			}

			vlanList = cloudAPIService.listVlan(0, 0, 0, resourcePoolsId);
			return "findAllVlan";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// 列出vlan列表
	public String findVlanByZoneidNetworkid() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				vlanList = null;
				return "findVlanByZoneidNetworkid";
			}

			int zoneid = id;
			int networkid = flag;
			vlanList = cloudAPIService.listVlan(0, zoneid, networkid, resourcePoolsId);
			return "findVlanByZoneidNetworkid";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// 列出Cluster列表
	public String findAllCluster() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				clusterList = null;
				return "findAllCluster";
			}
			clusterList = cloudAPIService.listCluster(0, flag, resourcePoolsId);
			return "findAllCluster";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	public List<Map<String, String>> getOsnameParams(int type, Integer resourcePoolID) {
		ListTemplates temp = new ListTemplates();
		temp.setTemplateFilter("community");
		Map<String, String> map = JsonUtil.getMap4Json(String.valueOf(commandService.executeAndJsonReturn(temp, resourcePoolID)));
		map = JsonUtil.getMap4Json(String.valueOf(map.get("listtemplatesresponse")));
		List<Map> list = JsonUtil.getList4Json(String.valueOf(map.get("template")), Map.class);
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		for (Map template : list) {
			if (template.get("status").equals("Download Complete")) {
				if (type == ConstDef.RESOURCE_TYPE_VM) {
					if (!template.get("hypervisor").equals("BareMetal")) {
						Map<String, String> optionMap = new HashMap<String, String>();
						optionMap.put("zoneid", String.valueOf(template.get("zoneid")));
						optionMap.put("crossZones", String.valueOf(template.get("crossZones")));
						optionMap.put("text", String.valueOf(template.get("name")));
						optionMap.put("value", String.valueOf(template.get("id")));
						reList.add(optionMap);
					}
				} else if (type == ConstDef.RESOURCE_TYPE_PM) {
					if (template.get("hypervisor").equals("BareMetal")) {
						Map<String, String> optionMap = new HashMap<String, String>();
						optionMap.put("text", String.valueOf(template.get("name")));
						optionMap.put("value", String.valueOf(template.get("id")));
						reList.add(optionMap);
					}
				}
			}
		}
		return reList;
	}

	// 列出Zone列表
	public String findZoneList() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				zoneList = null;
				return "findZoneList";
			}
			int type = VM_TEMPLATE_TYPE_VM;
			String typeStr = ServletActionContext.getRequest().getParameter("type");
			if (typeStr != null) {
				type = Integer.parseInt(typeStr);
			}
			List<EZone> myzoneList;
			myzoneList = cloudAPIService.listZones(0, resourcePoolsId);
			// 查询含有物理机的Cluster
			this.setFlag(1);
			findAllCluster();
			List<Long> zoneidList = new ArrayList<Long>();
			if (type == 10) {
				// 物理机
				if (clusterList != null) {
					for (ECluster cluster : clusterList) {
						Long zoneid = cluster.getZoneid();
						zoneidList.add(zoneid);
					}
					List<EZone> vmzoneList = new ArrayList<EZone>();

					for (EZone zone : myzoneList) {
						if (zoneidList.contains(zone.getId())) {
							vmzoneList.add(zone);
						}
					}
					zoneList = vmzoneList;
				} else {
					zoneList = null;
				}
			} else {
				// 虚拟机
				if (clusterList != null) {
					for (ECluster cluster : clusterList) {
						Long zoneid = cluster.getZoneid();
						zoneidList.add(zoneid);
					}
					List<EZone> vmzoneList = new ArrayList<EZone>();

					for (EZone zone : myzoneList) {
						if (!zoneidList.contains(zone.getId())) {
							vmzoneList.add(zone);
						}
					}
					zoneList = vmzoneList;
				} else {
					zoneList = myzoneList;
				}
			}
			String templateIdPortal = ServletActionContext.getRequest().getParameter("id");
			if (templateIdPortal != null) {
				// 获取Elaster模板列表
				List<Map<String, String>> templateList = getOsnameParams(type, resourcePoolsId);
				// 获取当前模板对象
				TTemplateVMBO templateVM = VMTemplateService.getTemplateById(Integer.parseInt(templateIdPortal));
				int eOsId = templateVM.geteOsId();
				boolean crossZones = false;
				String Temp_zoneId = "0";
				// 查询当前模板是否跨Zone
				for (Map<String, String> map : templateList) {
					String templateEOSId = map.get("value");
					if (templateEOSId.equals(eOsId + "")) {
						crossZones = Boolean.parseBoolean(map.get("crossZones"));
						Temp_zoneId = map.get("zoneid");
					}
				}
				List<EZone> finalZoneList = new ArrayList<EZone>();
				// 如果当前模板不允许跨Zone，取当前模板对应的Zone信息
				// 如果跨Zone,返回所有Zone信息.
				if (!crossZones) {
					for (EZone e : myzoneList) {
						if (Temp_zoneId.equals(e.getId() + "")) {
							finalZoneList.add(e);
						}
					}
					zoneList = finalZoneList;
				}
			}
			return "findZoneList";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	/**
	 * 查找Zone信息,根据Id
	 */
	public String findZoneById() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				message = null;
				return "findZoneById";
			}

			int zoneId = -1;
			List<EZone> myzoneList = null;
			String idStr = ServletActionContext.getRequest().getParameter("id");
			if (idStr != null) {
				zoneId = Integer.parseInt(idStr);
				myzoneList = cloudAPIService.listZones(zoneId, resourcePoolsId);
			}
			if (myzoneList.size() > 0) {
				message = myzoneList.get(0).getName();
			}
			return "findZoneById";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// 列出Network列表
	public String findNetworkList() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				networkList = null;
				return "findNetworkList";
			}
			//to fix bug:7618
			List<ENetwork> listNetworkNew = nicsService.searchNetworkListDefault(flag, resourcePoolsId,networkType);
			networkList = listNetworkNew;
			return "findNetworkList";
		}
		catch (Exception e) {
			e.printStackTrace();
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// 列出Network列表
	public String findNetworkListOther() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				networkListOthers = null;
				return "findNetworkListOther";
			}
			//to fix bug:7618
			List<ENetwork> listNetworkNew = nicsService.searchNetworkListOther(flag, resourcePoolsId,networkType);
			networkListOthers = listNetworkNew;
			return "findNetworkListOther";
		}
		catch (Exception e) {
			e.printStackTrace();
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// to fix bug:0001910
	// 查询Elaster全局变量名所对应的值
	public String findConfigurationsByName() {
		try {
			// 广东vdc
			if (ConstDef.getCurProjectId() == 2) {
				this.message = "2000";
				return "findConfigurationsByName";
			}
			List<EListConfigurations> eConfList = new ArrayList<EListConfigurations>();
			// String searchStr = "storage.max.volume.size";
			String value = null;
			eConfList = cloudAPIService.listConfigurations(searchStr, resourcePoolsId);
			if (eConfList != null && eConfList.size() == 1) {
				value = eConfList.get(0).getValue();
			}
			this.message = value;
			return "findConfigurationsByName";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	public String showResourcePoolNameById() {
		try {
			resourcePoolName = cloudAPIService.getResourcePoolNameById(resourcePoolId);
			return "showResourcePoolNameById";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	public String deleteNewOrderById() {
		try {
			TOrderBO order = orderService.selectOrderByOrderId(id);
			if (order != null && order.getState() == order.getOrderApproveLevelState()) {
				int orderType = order.getType();
				int instanceInfoId = order.getInstanceInfoId();
				int del_stat = orderService.deleteNewOrderByid(id, orderType, instanceInfoId);
				if (del_stat > 0) {
					this.message = "删除成功!";
				} else {
					this.message = "删除失败.";
				}
			} else {
				this.message = "订单不存在.";
			}
			return "deleteNewOrderById";
		}
		catch (Exception e) {
			this.message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}

	// windows系统 ping ip是否连通
	public String testPingWindows() {
		String result = "0";
		try {
			Runtime run = Runtime.getRuntime();
			String ipstr = searchStr; // "220.181.118.87";
			String cmdText = "ping -n 1 " + ipstr;// -n为个数
			Process process = run.exec(cmdText);
			String temp = new String();
			StringBuffer line = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			while ((temp = br.readLine()) != null) {
				line.append(temp);
				line.append(System.getProperty("line.separator"));
			}
			if ((line.indexOf("ttl") != -1) || (line.indexOf("TTL") != -1)) {
				result = "1";
			}
			// process.waitFor();
			// System.out.println(line);
			this.message = result;
			return "testPingWindows";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	// //Linux系统 ping ip是否连通
	// Destination Host Unreachable
	public String testPingLinux() {
		String result = "0";
		try {
			String ipStr = searchStr; // "172.16.210.65";
			String cmdText = "ping -c 6 " + ipStr;// -n为个数
			Process process = null;
			process = Runtime.getRuntime().exec(cmdText);
			String temp = new String();
			StringBuffer line = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			while ((temp = br.readLine()) != null) {
				line.append(temp);
				line.append(System.getProperty("line.separator"));
			}
			if ((line.indexOf("ttl=") != -1) || (line.indexOf("TTL=") != -1)) {
				result = "1";
			}
			// process.waitFor();
			//			System.out.println(line.toString());
			this.message = result;
			return "testPingLinux";
		}
		catch (Exception e) {
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	/**
	 * 判断实例名是否存在
	 *
	 * @return
	 */
	public String showInstanceInfoNameExists() {
		int userId = 0;
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user != null) {
			userId = user.getId();
		}
		int createUserId = userId;
		String instanceName = searchStr;
		String result = "0";
		try {
			int num = orderService.searchInstanceInfoByInstanceNameAndUserId(createUserId, instanceName);
			if (num > 0) {
				result = "1";
			}
			this.message = result;
			return "showInstanceInfoNameExists";
		}
		catch (Exception e) {
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	/**
	 * 判断网卡IP是否为动态获取
	 *
	 * @return
	 */
	public String showNicsDhcpSwitch() {
		try {
			//			int value = orderService.searchParametersValueByType("NICS_DHCP_SWITCH");
			int value = ConstDef.getNicsDhcpSwitch();
			this.message = String.valueOf(value);
			return "showNicsDhcpSwitch";
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	/**
	 * 显示公网IP
	 *
	 * @return 创建人： 何福康 创建时间：2012-2-9 上午09:17:59
	 */
	public String showPublicIp() {
		try {
			int serviceProvider = flag;
			publicIpList = publicIPService.listPublicIPByServiceProvider(serviceProvider);
			return "showPublicIp";
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	/**
	 * 获取当前用户实例信息总条数 param flag flag = 4 表示备份服务 flag = 5 表示监控服务
	 *
	 * @return 创建人： 何福康 创建时间：2012-2-9 下午02:37:04
	 */
	public String ShowInstanceCountByUser() {
		ResourcesQueryVO vo = new ResourcesQueryVO();
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user == null) {
			this.message = String.valueOf(-1);
			return "ShowInstanceCountByUser";
		}
		vo.setUser(user);
		vo.setOperateSqlType(flag);
		try {
			int countTotal = instanceService.queryInstanceCountByUser(vo, resourcePoolsId, zoneId);
			this.message = String.valueOf(countTotal);
			return "ShowInstanceCountByUser";
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}


	public String myOrderServiceList() throws Exception {
		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		try {
			// to fix bug [1846]
			QueryCriteria query = new QueryCriteria();
			query.setOrderStatus(this.orderState);
			query.setStartDate(this.startdate);
			query.setEndDate(this.enddate);
			query.setPayStatus(-1);
			// to fix bug 2289
			query.setOrderId(this.id);

			countTotal = orderService.searchOrdersAmount(user, query);
			query.setOrderStatusStr("1,2,3");
			int checkbeforeNum = orderService.searchOrdersAmount(user, query);
			query.setOrderStatusStr("4,5");
			int checkedNum = orderService.searchOrdersAmount(user, query);
			query.setOrderStatusStr("6");
			int cancelNum = orderService.searchOrdersAmount(user, query);

			PageVO page = null;
			if (countTotal > 0) {
				page = new PageVO();
				page.setCurPage(curPage);
				page.setPageSize(pageSize);
			}

			listResp.put("list", orderService.searchOrderServices(page, user, query));// 从页面传参数query
			listResp.put("total", countTotal);
			listResp.put("checkbeforeNum", checkbeforeNum);
			listResp.put("checkedNum", checkedNum);
			listResp.put("cancelNum", cancelNum);
			logger.info("get all my orders. ");
			return SUCCESS;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	// private void setVlanPoorMessage(){
	// if(1==ConstDef.getCloudId()){
	// this.message = this.PUB_VLAN_POOR;
	// }
	// else if(2==ConstDef.getCloudId()){
	// this.message = this.PRV_VLAN_POOR;
	// }
	// }

	public Map<String, Object> getMyWanIpVM() {
		return myWanIpVM;
	}

	public void setMyWanIpVM(Map<String, Object> myWanIpVM) {
		this.myWanIpVM = myWanIpVM;
	}

	public List<Map<String, Object>> getMyVM() {
		return myVM;
	}

	public void setMyVM(List<Map<String, Object>> myVM) {
		this.myVM = myVM;
	}

	public IOrderSerivce getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderSerivce orderService) {
		this.orderService = orderService;
	}

	public IVMTemplateService getVMTemplateService() {
		return VMTemplateService;
	}

	public void setVMTemplateService(IVMTemplateService vMTemplateService) {
		VMTemplateService = vMTemplateService;
	}

	public IMCTemplateService getMCTemplateService() {
		return MCTemplateService;
	}

	public void setMCTemplateService(IMCTemplateService mCTemplateService) {
		MCTemplateService = mCTemplateService;
	}

	public Map<String, Object> getListResp() {
		return listResp;
	}

	public void setListResp(Map<String, Object> listResp) {
		this.listResp = listResp;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public PublicIpServiceImpl getPublicIPService() {
		return publicIPService;
	}

	public void setPublicIPService(PublicIpServiceImpl publicIPService) {
		this.publicIPService = publicIPService;
	}

	public List<TTemplateVMBO> getTemplateVMList() {
		return templateVMList;
	}

	public void setTemplateVMList(List<TTemplateVMBO> templateVMList) {
		this.templateVMList = templateVMList;
	}

	public List<TTemplateMCBO> getTemplateMCList() {
		return templateMCList;
	}

	public void setTemplateMCList(List<TTemplateMCBO> templateMCList) {
		this.templateMCList = templateMCList;
	}

	public void setTemplateDISKList(List<TTemplateVMBO> templateDISKList) {
		this.templateDISKList = templateDISKList;
	}

	public List<TTemplateVMBO> getTemplateDISKList() {
		return templateDISKList;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public TTemplateVMBO getTemplateVM() {
		return templateVM;
	}

	public void setTemplateVM(TTemplateVMBO templateVM) {
		this.templateVM = templateVM;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTemplateVMstr() {
		return templateVMstr;
	}

	public void setTemplateVMstr(String templateVMstr) {
		this.templateVMstr = templateVMstr;
	}

	public TOrderBO getOrderBO() {
		return orderBO;
	}

	public void setOrderBO(TOrderBO orderBO) {
		this.orderBO = orderBO;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public String getTemplateInfo() {
		return templateInfo;
	}

	public void setTemplateInfo(String templateInfo) {
		this.templateInfo = templateInfo;
	}

	public ICloudAPISerivce getCloudAPIService() {
		return cloudAPIService;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	public List<EVlanIpRange> getVlanList() {
		return vlanList;
	}

	public void setVlanList(List<EVlanIpRange> vlanList) {
		this.vlanList = vlanList;
	}

	public List<ECluster> getClusterList() {
		return clusterList;
	}

	public void setClusterList(List<ECluster> clusterList) {
		this.clusterList = clusterList;
	}

	public int getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(int resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	public String getResourcePoolName() {
		return resourcePoolName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}

	public IAuditSevice getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditSevice auditService) {
		this.auditService = auditService;
	}

	public IInstanceService getInstanceService() {
		return instanceService;
	}

	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}

	public List<EZone> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<EZone> zoneList) {
		this.zoneList = zoneList;
	}

	public List<ENetwork> getNetworkList() {
		return networkList;
	}

	public void setNetworkList(List<ENetwork> networkList) {
		this.networkList = networkList;
	}

	public List<TPublicIPBO> getPublicIpList() {
		return publicIpList;
	}

	public void setPublicIpList(List<TPublicIPBO> publicIpList) {
		this.publicIpList = publicIpList;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List<ENetwork> getNetworkListOthers() {
		return networkListOthers;
	}

	public void setNetworkListOthers(List<ENetwork> networkListOthers) {
		this.networkListOthers = networkListOthers;
	}

	public String getQueryJson() {
		return queryJson;
	}

	public void setQueryJson(String queryJson) {
		this.queryJson = queryJson;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public IJobInstanceInfoService getJobInstanceInfoService() {
		return jobInstanceInfoService;
	}

	public void setJobInstanceInfoService(IJobInstanceInfoService jobInstanceInfoService) {
		this.jobInstanceInfoService = jobInstanceInfoService;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public INicsService getNicsService() {
		return nicsService;
	}

	public void setNicsService(INicsService nicsService) {
		this.nicsService = nicsService;
	}

	public UserVlanServiceImpl getUserVlanService() {
		return userVlanService;
	}

	public void setUserVlanService(UserVlanServiceImpl userVlanService) {
		this.userVlanService = userVlanService;
	}

	

	public PhysicalMachinesService getPhysicalMachineService() {
		return physicalMachineService;
	}

	public void setPhysicalMachineService(PhysicalMachinesService physicalMachineService) {
		this.physicalMachineService = physicalMachineService;
	}

	public IFirewallService getFirewallService() {
		return firewallService;
	}

	public void setFirewallService(IFirewallService firewallService) {
		this.firewallService = firewallService;
	}

	public int getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}

	public int getCountTotal2() {
		return countTotal2;
	}

	public void setCountTotal2(int countTotal2) {
		this.countTotal2 = countTotal2;
	}

	public List<Long> getVlanIdList() {
		return vlanIdList;
	}

	public void setVlanIdList(List<Long> vlanIdList) {
		this.vlanIdList = vlanIdList;
	}

	public int getVethAdaptorNum() {
		return vethAdaptorNum;
	}

	public void setVethAdaptorNum(int vethAdaptorNum) {
		this.vethAdaptorNum = vethAdaptorNum;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public IoadBalanceH3CService getLoadbalanceH3Cservice() {
		return loadbalanceH3Cservice;
	}

	public void setLoadbalanceH3Cservice(IoadBalanceH3CService loadbalanceH3Cservice) {
		this.loadbalanceH3Cservice = loadbalanceH3Cservice;
	}

	public VirtualMachineModifyService getVmModifyService() {
		return vmModifyService;
	}

	public void setVmModifyService(VirtualMachineModifyService vmModifyService) {
		this.vmModifyService = vmModifyService;
	}

	public NasResourceService getNasResourceService() {
		return nasResourceService;
	}

	public void setNasResourceService(NasResourceService nasResourceService) {
		this.nasResourceService = nasResourceService;
	}

	
    public int getNetworkType() {
    	return networkType;
    }

	
    public void setNetworkType(int networkType) {
    	this.networkType = networkType;
    }
	
	

}
