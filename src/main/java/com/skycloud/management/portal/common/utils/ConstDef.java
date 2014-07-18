package com.skycloud.management.portal.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.admin.productitem.service.IProductItemService;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.log.action.UserLogAction;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;

// 常量定义类
public class ConstDef {

	public static final String SHOW_INITPASSWORD = "SHOW_INITPASSWORD";

	public static final int RESOURCE_TYPE_VM = 1;

	public static final int RESOURCE_TYPE_STORAGE = 2; // vdisk

	// added by zhanghuizheng
	public static final int RESOURCE_TYPE_MINICOMPUTER = 3;

	public static final int RESOURCE_TYPE_BACKUP = 4;

	public static final int RESOURCE_TYPE_MONITOR = 5;

	public static final int RESOURCE_TYPE_DATABACKUP = 15;

	public static final int RESOURCE_TYPE_LOADBALANCED = 6;

	public static final int RESOURCE_TYPE_FIREWALL = 7;

	public static final int RESOURCE_TYPE_BANDWIDTH = 8;

	public static final int RESOURCE_TYPE_PUBLICNETWORKIP = 9;

	public static final int RESOURCE_TYPE_PM = 10;

	public static final int RESOURCE_TYPE_OBJECTSTORAGE = 11; // S3,对象存储

	// public static final int RESOURCE_TYPE_STORAGE2 = 12; // 弹性块存储
	public static final int RESOURCE_TYPE_IPSAN = 12; // 弹性块存储 ipsan

	public static final int RESOURCE_TYPE_NAS = 13; // 文件系统

	public static final int RESOURCE_TYPE_CLOUDSTORAGE = 15;

	public static final int RESOURCE_TYPE_MUTIL_VM = 50;

	// 2012.2.23 fengyk添加訂單類型常量
	public static final int ORDER_CHANGE_TYPE = 2;

	public static final int ORDER_DEL_TYPE = 3;

	private static UserLogAction userLogAction;

	// public static final int TEST = 6;


	public static final String ROLE_2_AUTO = "ROLE_2_AUTO";  //角色2是否自动审核；1：自动审核；0：手动审核
	public static final String ROLE_3_AUTO = "ROLE_3_AUTO";//角色3是否自动审核；1：自动审核；0：手动审核
	public static final String ROLE_4_AUTO = "ROLE_4_AUTO";//角色4是否自动审核；1：自动审核；0：手动审核
	//public static final String AUTO_CONFIRM = "AUTO_CONFIRM";//模板创建是否自动确认；1：自动确认；0：手动确认
	public static final String E_MAX_CPU_NUM = "E_MAX_CPU_NUM";//X86模板最大CPU个数
	public static final String E_MAX_MEM_SIZE = "E_MAX_MEM_SIZE";//X86模板最大内存大小，单位MBytes
	public static final String MC_MAX_CPU_NUM = "MC_MAX_CPU_NUM";//MC模板最大CPU个数
	public static final String MC_MAX_MEM_SIZE = "MC_MAX_MEM_SIZE";//MC模板最大内存大小，单位MBytes
	public static final String NICS_DHCP_SWITCH = "NICS_DHCP_SWITCH";//网卡IP动态获取开关; 1 ： 动态获取IP; 0 : 人工填写IP, 0
	public static final String PROJECT_SWITCH = "PROJECT_SWITCH";//项目开关：1:SkyCloud1.1 ; 2:广东移动VDC;3:北研院;, 0
	public static final String JOB_QUERY_COUNT = "JOB_QUERY_COUNT";//任务每次执行查询读取条数，默认为100, 1
	public static final String JOB_THREAD_EXCUTE_COUNT = "JOB_THREAD_EXCUTE_COUNT";//查询每个线程执行次数，默认为5, 1
	public static final String INTEGRATION_4A = "INTEGRATION_4A";//是否集成4A：0：是; 1：否, 0
	public static final String LOG_CAP_THRESHOLD = "LOG_CAP_THRESHOLD";//日志容量门限，默认5000条, 1
	public static final String LOG_BAK_DURATION = "LOG_BAK_DURATION";//日志备份时长，默认7天, 1
	public static final String LOG_STORE_DURATION = "LOG_STORE_DURATION";//备份文件保存的天数，默认60天, 1
	public static final String MAIL_REMIND_BEFORE_PERIOD = "MAIL_REMIND_BEFORE_PERIOD";//产品到期提前提醒周期，默认提前7天提醒, 1
	public static final String MAIL_REMIND_PERIOD = "MAIL_REMIND_PERIOD";//邮件提醒周期，默认1天一提醒, 1
	public static final String MAIL_REMIND_SEND_COUNT = "MAIL_REMIND_SEND_COUNT";//邮件提醒发送次数, 1
	public static final String LOG_FILE_SIZE = "LOG_FILE_SIZE";//日志文件大小，默认为10M, 1
	public static final String LOG_BAK_DIR_ALM_SIZE = "LOG_BAK_DIR_ALM_SIZE";//备份目录日志容量告警门限值，默认200M, 1
	public static final String PUBLIC_PRIVATE_CLOUD = "PUBLIC_PRIVATE_CLOUD";//公有云；1：私有云；2, 0
	public static final String PROJECT_ISHA = "PROJECT_ISHA";//是否支持HA; 0:不支持，1：支持, 1
	public static final String PRODUCT_ADUIT_AUTO = "PRODUCT_ADUIT_AUTO";//服务审核是否自动审核；1：自动审核；0：手动审核, 1
	public static final String PRODUCT_PUBLISH_AUTO = "PRODUCT_PUBLISH_AUTO";//服务发布是否自动发布；1：自动发布；0：手动发布, 1
	public static final String DEPLOYVM_BY_NEWAPI = "DEPLOYVM_BY_NEWAPI";//创建vm手工指定ip是否使用X86-3.3以上新api：1：使用；0：不使用, 1
	public static final String FIRST_NETWORK_TAG = "FIRST_NETWORK_TAG";//第一块网卡tag, 1
	public static final String SECOND_NETWORK_TAG = "SECOND_NETWORK_TAG";//第二块网卡tag, 1

	//页面上需要初始化的下拉列表 原来config.properties中以 dropdownlist.开头的配置项现改为从数据库中获取
	public static final String COMBOX_CPUHZ_GHZ = "COMBOX_CPUHZ_GHZ"; //   dropdownlist.cpuhz_ghz=0.6
	public static final String COMBOX_CPUNUM = "COMBOX_CPUNUM"; //cpu个数   dropdownlist.cpunum
	public static final String COMBOX_MEMORYSIZE = "COMBOX_MEMORYSIZE"; //内存大小   dropdownlist.memorysize
	public static final String COMBOX_STORAGESIZE = "COMBOX_STORAGESIZE"; //磁盘大小   dropdownlist.storagesize
	public static final String COMBOX_LOADBALANCE_CONCURRENTNUM = "COMBOX_LOADBALANCE_CONCURRENTNUM"; //负载均衡-并发数    dropdownlist.concurrentnum
	public static final String COMBOX_LOADBALANCE_PROTOCOL = "COMBOX_LOADBALANCE_PROTOCOL"; //负载均衡-协议类型   dropdownlist.protocol
	public static final String COMBOX_LOADBALANCE_POLICY = "COMBOX_LOADBALANCE_POLICY"; //负载均衡-策略   dropdownlist.policy
	public static final String COMBOX_FIREWALL_RULES = "COMBOX_FIREWALL_RULES"; //防火墙-规则条数   dropdownlist.rulenum
	public static final String COMBOX_BANDWIDTH = "COMBOX_BANDWIDTH"; //带宽大小   dropdownlist.bandwidth
	public static final String COMBOX_CPUNUM_STORAGESIZE = "COMBOX_CPUNUM_STORAGESIZE"; //cpu个数和磁盘大小   dropdownlist.cpunumstoragesize
	public static final String COMBOX_MEASUREMODE = "COMBOX_MEASUREMODE"; //计量方式   dropdownlist.measuremode
	public static final String COMBOX_GRADE = "COMBOX_GRADE"; //资源等级   dropdownlist.grade
	public static final String COMBOX_STORETYPE = "COMBOX_STORETYPE"; //存储类型   dropdownlist.storetype
	public static final String COMBOX_VMOS = "COMBOX_VMOS"; //操作系统   dropdownlist.vmos
	public static final String COMBOX_PRICERANGE = "COMBOX_PRICERANGE"; //门户服务价格   dropdownlist.pricerange
	public static final String COMBOX_NETWORKNUM = "COMBOX_NETWORKNUM"; //网卡个数

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

	/**
	 * <p>
	 * 功能描述:[监控资源代码对应的资源类型的map]
	 * </p>
	 */
	public static final Map<Integer, String> PRODUCT_TYPE_MAP = new HashMap<Integer, String>();
	static {
		// to fix bug:3418
		PRODUCT_TYPE_MAP.put(1, "虚拟机");
		PRODUCT_TYPE_MAP.put(2, "虚拟硬盘");
		PRODUCT_TYPE_MAP.put(3, "小型机");
		PRODUCT_TYPE_MAP.put(4, "虚拟机备份");
		PRODUCT_TYPE_MAP.put(5, "云监控");
		PRODUCT_TYPE_MAP.put(6, "负载均衡");
		PRODUCT_TYPE_MAP.put(8, "公网带宽");
		PRODUCT_TYPE_MAP.put(9, "弹性公网IP");
		PRODUCT_TYPE_MAP.put(10, "物理机");
		PRODUCT_TYPE_MAP.put(15, "数据云备份");
		PRODUCT_TYPE_MAP.put(7, "防火墙");
		PRODUCT_TYPE_MAP.put(11, "对象存储");
		PRODUCT_TYPE_MAP.put(12, "弹性块存储");
		//to fix bug 5011
		PRODUCT_TYPE_MAP.put(13, "文件系统");
		PRODUCT_TYPE_MAP.put(16, "vpc");
		PRODUCT_TYPE_MAP.put(17, "vpn站点互联");
		PRODUCT_TYPE_MAP.put(18, "vpn接入");
		PRODUCT_TYPE_MAP.put(19, "应用主机");
		PRODUCT_TYPE_MAP.put(20, "SaaS服务");
		PRODUCT_TYPE_MAP.put(21, "云桌面服务");
		PRODUCT_TYPE_MAP.put(50, "多实例");
	}

	public static final Map<String, String> UNIT_TYPE_MAP = new HashMap<String, String>();
	static {
		UNIT_TYPE_MAP.put("Y", "年");
		UNIT_TYPE_MAP.put("M", "月");
		UNIT_TYPE_MAP.put("W", "周");
		UNIT_TYPE_MAP.put("D", "天");
		UNIT_TYPE_MAP.put("H", "小时");
		UNIT_TYPE_MAP.put("S", "按流量");
	}

	public static final Map<String, String> RESOURCE_TYPE_MAP = new HashMap<String, String>();
	static {
		RESOURCE_TYPE_MAP.put("vm", "虚拟机");
		RESOURCE_TYPE_MAP.put("mc", "小型机");
		RESOURCE_TYPE_MAP.put("vl", "块存储");
		RESOURCE_TYPE_MAP.put("lb", "负载均衡");
		RESOURCE_TYPE_MAP.put("fw", "防火墙");
		RESOURCE_TYPE_MAP.put("pnip", "公网IP");
		RESOURCE_TYPE_MAP.put("bw", "公网带宽");
		RESOURCE_TYPE_MAP.put("pm", "物理机");
	}

	// public static final Map<String, String> VDC_RESOURCE_TYPE_MAP = new
	// HashMap<String, String>();
	// static{
	// VDC_RESOURCE_TYPE_MAP.put("VM", "虚拟机资源");
	// VDC_RESOURCE_TYPE_MAP.put("SRV", "物理服务器资源");
	// VDC_RESOURCE_TYPE_MAP.put("VMBK", "虚拟机备份资源");
	// VDC_RESOURCE_TYPE_MAP.put("BS", "块存储资源");
	// VDC_RESOURCE_TYPE_MAP.put("OS", "对象存储资源");
	// VDC_RESOURCE_TYPE_MAP.put("IP", "公网IP地址资源");
	// VDC_RESOURCE_TYPE_MAP.put("BW", "公网带宽资源");
	// VDC_RESOURCE_TYPE_MAP.put("SG", "安全组资源");
	// VDC_RESOURCE_TYPE_MAP.put("CM", "云监控资源");
	// }

	public static final Map<Integer, String> VDC_SKYCLOUD_RESOURCE_TYPE_MAP = new HashMap<Integer, String>();
	static {
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_VM, "VM");
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_BACKUP, "VMBK");
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_STORAGE, "BS");
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_DATABACKUP, "OS"); // 待确定
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP, "IP");
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_BANDWIDTH, "BW");
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_FIREWALL, "SG");
		VDC_SKYCLOUD_RESOURCE_TYPE_MAP.put(ConstDef.RESOURCE_TYPE_MONITOR, "CM");
	}

	public static final Map<String, Integer> RESOURCE_TYPE_MENU_MAP = new HashMap<String, Integer>();
	static {
		RESOURCE_TYPE_MENU_MAP.put("VM", 1);
		RESOURCE_TYPE_MENU_MAP.put("STORAGE", 2);
		RESOURCE_TYPE_MENU_MAP.put("MINICOMPUTER", 3);
		RESOURCE_TYPE_MENU_MAP.put("BACKUP", 4);
		RESOURCE_TYPE_MENU_MAP.put("MONITOR", 5);
		RESOURCE_TYPE_MENU_MAP.put("LOADBALANCED", 6);
		RESOURCE_TYPE_MENU_MAP.put("FIREWALL", 7);
		RESOURCE_TYPE_MENU_MAP.put("BANDWIDTH", 8);
		RESOURCE_TYPE_MENU_MAP.put("PUBLICNETWORKIP", 9);
		RESOURCE_TYPE_MENU_MAP.put("PM", 10);
		RESOURCE_TYPE_MENU_MAP.put("OBJECTSTORAGE", 11);
		RESOURCE_TYPE_MENU_MAP.put("STORAGE2", 12);
		// 加上nas
		RESOURCE_TYPE_MENU_MAP.put("NAS", 13);
		RESOURCE_TYPE_MENU_MAP.put("DATABACKUP", 15);

		RESOURCE_TYPE_MENU_MAP.put("VPC", 16);
		RESOURCE_TYPE_MENU_MAP.put("VPNJOIN", 17);
		RESOURCE_TYPE_MENU_MAP.put("VPNLINK", 18);
		RESOURCE_TYPE_MENU_MAP.put("REVM", 19);
		RESOURCE_TYPE_MENU_MAP.put("SAAS", 20);
		RESOURCE_TYPE_MENU_MAP.put("DESKTOP", 21);
	}

	// 按照广东移动规范VDC资源类型编码的前缀
	public static final String VDC_RESOURCE_TYPE_PREFIX = "CIDC-RT-";

	// 按照广东移动规范VDC资源模板编码的前缀
	public static final String VDC_TEMPLATEID_PREFIX = "CIDC-T-";

	/**
	 * 获取VDC资源类型编码 输入参数:resourceType 例如 对于虚拟机传入ConstDef.RESOURCE_TYPE_VM即可
	 */
	public static final String getVDCResourceType(int resourceType) {
		return VDC_RESOURCE_TYPE_PREFIX + ConstDef.VDC_SKYCLOUD_RESOURCE_TYPE_MAP.get(resourceType);
	}

	/**
	 * 获取VDC资源模板编码 输入参数:resourceType,id 例如
	 * 对于虚拟机传入ConstDef.RESOURCE_TYPE_VM和模板的id字段值即可
	 */
	public static final String getVDCTemplateID(int resourceType, int id) {
		if (id > 0) {
			return VDC_TEMPLATEID_PREFIX + ConstDef.VDC_SKYCLOUD_RESOURCE_TYPE_MAP.get(resourceType) + "-" + String.format("%08d", id);
		} else {
			return "";
		}
	}

	// 模板状态中文
	public static final Map<Integer, String> TEMPLATE_STATE_MAP = new HashMap<Integer, String>();
	static {
		TEMPLATE_STATE_MAP.put(1, "申请");
		TEMPLATE_STATE_MAP.put(2, "可用");
		TEMPLATE_STATE_MAP.put(3, "正在处理");
		TEMPLATE_STATE_MAP.put(5, "已删除");
		TEMPLATE_STATE_MAP.put(7, "操作失败");
		TEMPLATE_STATE_MAP.put(8, "资源池可用");
		TEMPLATE_STATE_MAP.put(9, "资源池不可用");
	}

	public static final int BUSINESS_EXECUTE_SUCCESS = 0;

	public static final int BUSINESS_EXECUTE_FAILED = 1;

	public static final String SESSION_KEY_USER = "SESSION_KEY_USER";

	public static final String SYSTEM_CONFIG = "/config.properties";

	public static final int RESOURCE_MODEL_SPECAL_FLAG = 1;

	// 状态值
	public static final int STATE_ZERO = 0;

	public static final int STATE_ONE = 1;

	public static final int STATE_TWO = 2;

	public static final int STATE_THREE = 3;

	public static final int STATE_FOUR = 4;

	public static final int STATE_FIVE = 5;

	// 操作类型
	public static final int OPER_ADD = 1;

	public static final int OPER_MOD = 2;

	public static final int OPER_DEL = 3;

	public static final int OPER_QUERY = 4;

	// 用户日志操作类型
	public static final int USERLOG_ADD = 1;

	public static final int USERLOG_DEL = 2;

	public static final int USERLOG_MOD = 3;

	public static final int USERLOG_QUERY = 4;

	// 操作类型中文
	public static final Map<Integer, String> OPER_TYPE_MAP = new HashMap<Integer, String>();
	static {
		OPER_TYPE_MAP.put(OPER_ADD, "新建");
		OPER_TYPE_MAP.put(OPER_MOD, "修改");
		OPER_TYPE_MAP.put(OPER_DEL, "删除");
		OPER_TYPE_MAP.put(OPER_QUERY, "查询");
	}
	public static final int YES_OR_NO_YES = 1;// 是

	public static final int YES_OR_NO_NO = 0; // 否


	// 用户日志 处理类型;msg: 消息，mail: 邮件，sms: 短信，log:日志，alert:提示，warn:警告，error:错误
	public static final String USERLOG_TARGET_TYPE_MSG = "msg";

	public static final String USERLOG_TARGET_TYPE_MAIL = "mail";

	public static final String USERLOG_TARGET_TYPE_SMS = "sms";

	public static final String USERLOG_TARGET_TYPE_LOG = "log";

	public static final String USERLOG_TARGET_TYPE_ALERT = "alert";

	public static final String USERLOG_TARGET_TYPE_WARN = "warn";

	public static final String USERLOG_TARGET_TYPE_ERROR = "error";

	// 处理类型中文
	public static final Map<String, String> USER_LOG_TARGET_TYPE_MAP = new HashMap<String, String>();
	static {
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_MSG  , "消息");
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_MAIL , "邮件");
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_SMS  , "短信");
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_LOG  , "日志");
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_ALERT, "提示");
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_WARN , "警告");
		USER_LOG_TARGET_TYPE_MAP.put(USERLOG_TARGET_TYPE_ERROR, "错误");
	}

	// 用户日志状态[1:已创建，2:已查看，3:已发送，4:处理失败，5:已处理，6:已过期]
	public static final int USER_LOG_STATUS_CREATE = 1;//已创建

	public static final int USER_LOG_STATUS_VIEW = 2;//已查看

	public static final int USER_LOG_STATUS_SEND = 3;//已发送

	public static final int USER_LOG_STATUS_FAIL = 4;//已失败

	public static final int USER_LOG_STATUS_HANDLE = 5;//已处理

	public static final int USER_LOG_STATUS_OVER = 6;//已过期

	// 状态中文
	public static final Map<Integer, String> USER_LOG_STATUS_MAP = new HashMap<Integer, String>();
	static {
		USER_LOG_STATUS_MAP.put(USER_LOG_STATUS_CREATE, "已创建");
		USER_LOG_STATUS_MAP.put(USER_LOG_STATUS_VIEW, "已查看");
		USER_LOG_STATUS_MAP.put(USER_LOG_STATUS_SEND, "已发送");
		USER_LOG_STATUS_MAP.put(USER_LOG_STATUS_FAIL, "处理失败");
		USER_LOG_STATUS_MAP.put(USER_LOG_STATUS_HANDLE, "已处理");
		USER_LOG_STATUS_MAP.put(USER_LOG_STATUS_OVER, "已过期");
	}

	// 模板
	public static final String ERROR_MESSAGE_ADMIN_TEMPLATE_DAO_CREATE = "创建模板失败。模板描述：%s，申请者：%s，申请时间：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_ADMIN_TEMPLATE_DAO_UPDATE = "修改模板失败。模板描述：%s，申请者：%s，申请时间：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_ADMIN_TEMPLATE_DAO_DELETE = "删除模板失败。模板ID：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_ADMIN_TEMPLATE_DAO_QUERY = "查询模板失败。失败原因：%s";

	// 定单
	public static final String ERROR_MESSAGE_PORTAL_ORDER_DAO_CREATE = "创建定单失败。定单描述：%s，申请人：%s，申请时间：%s";

	public static final String ERROR_MESSAGE_PORTAL_ORDER_DAO_UPDATE = "修改定单失败。定单描述：%s，申请人：%s，修改时间：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_ORDER_DAO_DELETE = "删除定单失败。定单ID：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY = "查询定单失败。失败原因：%s";

	// 实例
	public static final String ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_CREATE = "创建实例失败。实例描述：%s，申请时间：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_UPDATE = "修改实例失败。实例描述：%s，修改时间：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_DELETE = "删除实例失败。实例ID：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_INSTANCEINFO_DAO_QUERY = "查询实例失败。";

	// 网卡
	public static final String ERROR_MESSAGE_PORTAL_NICS_DAO_CREATE = "创建实例网卡失败。实例ID：%s，IP：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_NICS_DAO_UPDATE = "修改实例网卡失败。实例ID：%s，IP：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_NICS_DAO_DELETE = "删除实例网卡失败。实例网卡ID：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY = "查询实例网卡失败。失败原因：%s";

	/**
	 * 命令级命令添加到queue使用map去重
	 *
	 * @author fengyk
	 * @date 2011.11.22
	 */
	public static Map<Integer, AsyncJobInfo> ASYNCJOBINFO_MAP = new HashMap<Integer, AsyncJobInfo>();

	// projectId
	public static enum ProjectId {
		SkyFormOpt(1, "SkyForm"), VDC(2, "广东移动VDC"), shanghai(3, "上海浦软");

		private final int code;

		private final String desc;

		ProjectId(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static int curProjectId = 0;
	static {
		curProjectId = getCurProjectId();
	}

	public synchronized static int getCurProjectId() {
		if (curProjectId == 0) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			curProjectId = parametersService.getCurProjectId();
		}
		return curProjectId;
	}

	public static String templateTypesJSONArray = "";

	public synchronized static String getTemplateTypes() throws Exception {
		IProductItemService productItemService = (IProductItemService) BeanFactoryUtil.getBean("productItemService");
		templateTypesJSONArray = productItemService.listMenu(1);
		return templateTypesJSONArray;
	}

	public static int curCloudId = 0;

	/**
	 * 是否公有云： 1：公有云； 2：私有云；
	 *
	 * @return 创建人： 何福康 创建时间：2012-6-4 下午01:56:08
	 */
	public synchronized static int getCloudId() {
		if (curCloudId == 0) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			curCloudId = parametersService.getCloudId();
		}
		return curCloudId;
	}

	/**
	 * 日志记录函数
	 *
	 * @param operateType
	 *            操作类型 1:增,2:,删,3:改,4:查
	 * @param moduleName
	 *            模块名称
	 * @param functionName
	 *            功能名称
	 * @param className
	 *            类名
	 * @param methodName
	 *            函数名
	 * @param parameters
	 *            重要参数的取值情况,如：type=7
	 * @param desc
	 *            参数与功能的关系
	 * @param memo
	 *            备注说明
	 * @return 创建人： 何福康 创建时间：2012-3-23 下午01:05:45
	 */
	public static int saveLogInfo(int operateType, String moduleName, String functionName, String className, String methodName, String parameters,
	        String desc, String memo) {
		try {
			TUserLogVO log = new TUserLogVO();
			log.setModuleName(moduleName);
			log.setFunctionName(functionName);
			log.setClassName(className);
			log.setMethodName(methodName);
			log.setParameters(parameters);
			log.setType(operateType);
			log.setComment(desc);
			log.setMemo(memo);
			getUserLogService().saveLog(log);
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	public static UserLogAction getUserLogService() {
		if (userLogAction == null) {
			return (UserLogAction) BeanFactoryUtil.getBean("userLogAction");
		}
		return userLogAction;
	}

	/**
	 * 网卡IP动态获取开关; 1 ： 动态获取IP; 0 : 人工填写IP
	 */
	// to fix bug:0001678
	public static int curNicsDhcpSwitch = -1;

	public synchronized static int getNicsDhcpSwitch() {
		if (curNicsDhcpSwitch == -1) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			curNicsDhcpSwitch = parametersService.getParameterByType("NICS_DHCP_SWITCH");
		}
		return curNicsDhcpSwitch;
	}

	/**
	 * 服务自动审核开关; 1:自动审核; 0:人工审核
	 */
	public static int prudoctAduitAuto = -1;

	public synchronized static int getPrudoctAduitAuto() {
		if (prudoctAduitAuto == -1) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			prudoctAduitAuto = parametersService.getParameterByType("PRODUCT_ADUIT_AUTO");
		}
		return prudoctAduitAuto;
	}

	/**
	 * 服务自动发布开关; 1:自动审核; 0:人工审核
	 */
	public static int prudoctPublishAuto = -1;

	public synchronized static int getPrudoctPublishAuto() {
		if (prudoctPublishAuto == -1) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			prudoctPublishAuto = parametersService.getParameterByType("PRODUCT_PUBLISH_AUTO");
		}
		return prudoctPublishAuto;
	}

	/**
	 * 初始密码显示时间
	 */
	public static int initPassword = -1;

	public synchronized static int getInitPassword() {
		if (initPassword == -1) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			initPassword = parametersService.getParameterByType("SHOW_INITPASSWORD");
		}
		return initPassword;
	}

	/**
	 * 第一块网卡tag
	 */
	public static String firstNetworkTag = null;

	public synchronized static String getFirstNetworkTag() {
		if (firstNetworkTag == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			firstNetworkTag = parametersService.getParameterValueByType("FIRST_NETWORK_TAG");
		}
		return firstNetworkTag;
	}

	/**
	 * 第二块网卡tag
	 */
	public static String secondNetworkTag = null;

	public synchronized static String getSecondNetworkTag() {
		if (secondNetworkTag == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			secondNetworkTag = parametersService.getParameterValueByType("SECOND_NETWORK_TAG");
		}
		return secondNetworkTag;
	}

	/**
	 * 第三块网卡tag
	 */
	public static String thirdNetworkTag = null;

	public synchronized static String getThirdNetworkTag() {
		if (thirdNetworkTag == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			thirdNetworkTag = parametersService.getParameterValueByType("THIRD_NETWORK_TAG");
		}
		return thirdNetworkTag;
	}

	/**
	 * 第四块网卡tag
	 */
	public static String forthNetworkTag = null;

	public synchronized static String getForthNetworkTag() {
		if (forthNetworkTag == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			forthNetworkTag = parametersService.getParameterValueByType("FORTH_NETWORK_TAG");
		}
		return forthNetworkTag;
	}

	/**
	 * bug 0004359 创建vm手工指定ip是否使用X86-3.3以上新api：1：使用；0：不使用
	 */
	public static String deployVmByNewAPI = null;

	public synchronized static String getDeployVmByNewAPI() {
		if (deployVmByNewAPI == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			deployVmByNewAPI = parametersService.getParameterValueByType("DEPLOYVM_BY_NEWAPI");
		}
		return deployVmByNewAPI;
	}

	/**
	 * 用户注册是否需要激活开关
	 */
	public static String PORTAL_REGISTER_ACTIVE_FLAG = null;
	public static String PORTAL_REGISTER_ACTIVE_URL = null;

	public synchronized static String getRegisterActiveFlag() {
		//获取激活开关参数
//		if (PORTAL_REGISTER_ACTIVE_FLAG == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			PORTAL_REGISTER_ACTIVE_FLAG = parametersService.getParameterValueByType("PORTAL_REGISTER_ACTIVE_FLAG");
//		}
		return PORTAL_REGISTER_ACTIVE_FLAG;
	}

	public synchronized static String getRegisterActiveURL() {
		//获取激活公网IP和端口
//		if (PORTAL_REGISTER_ACTIVE_URL == null) {
			ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
			PORTAL_REGISTER_ACTIVE_URL = parametersService.getParameterValueByType("PORTAL_REGISTER_ACTIVE_URL");
//		}
		return PORTAL_REGISTER_ACTIVE_URL;
	}

}
