package com.skycloud.management.portal.front.order.entity;

import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.entity.TProductBO;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;

/**
 * TOrder entity.
 * 
 * @author hefk
 */

public class TInstanceInfoBO implements java.io.Serializable {

	/**
	 * 实例信息实体对象
	 */
	private static final long serialVersionUID = -4578690517794071091L;

	private int id; // 主键

	private int orderId; // 订单ID

	private int orderType;// 订单类型

	private int orderState;// 订单状态

	private int serviceHistoryState;// 服务历史状态

	private int serviceHistoryId;// 服务历史id

	private int serviceId;// 服务ID

	private String serviceName;// 服务名称

	private int templateType; // 资源模板类型；1： 虚拟机；2：小型机；3：X86物理机

	private int templateId; // 资源模板ID

	private String templateName; // 资源模板名称

	private String templateTypeName;// 资源模板类型名称

	private String comment; // 备注

	private String resourceInfo; // 资源定义的JSON串

	// eg:{"CPU":2,"MEMORY":1024,"STORYAGE":500,"OS":"Windows Server 2K3"}

	private int cpuNum; // CPU 数量 查询用

	private int memorySize; // 内存大小 查询用

	private int storageSize; // 存储大小 查询用

	private Long zoneId; // 数据中心ID

	private long clusterId; // 集群ID

	private Date createDt; // 创建时间

	private int state; // 实例状态；1：申请状态；2：可用状态；3：不可用状态；4：已回收

	private Date lastupdateDt;// 最近一次修改时间

	private long eInstanceId; // Elaster实例ID

	private Long eServiceId = Long.valueOf(0); // Elaster计算服务ID

	private long eDiskId; // Elaster磁盘服务ID

	private long eNetworkId; // Elaster网络服务ID

	private long eOsId; // ElasterOS服务ID

	private String osDesc; // 操作系统描述

	private String instanceName; // 实例名称

	private int productId = 0;

	private String reason;

	private String templateCode;

	private String templateDesc;

	private float cpufrequency;

	private int resourcePoolsId;

	private String clusterName;// 集群名称

	private String pool_name;// 资源池名称

	private int vmId;// 依赖的虚机id

	private String vmName;// 依赖的虚机名称

	private String volumestate;

	private String vmInstanceId;

	private int orderMemorySize;// 订单修改后的内存大小

	private int orderCpuNum;// 订单修改后的cpu个数

	// added by zhanghuizheng
	private int ORDER_STORAGE_SIZE; // 订单修改后的storageSize

	// 模板的参数信息 模板中的字段
	private int templateCpuNum;// 模板中cpu个数

	private int templateMemorySize;// 模板中内存大小

	private int templateStorageSize;// 模板中的个数或数值

	// added by hetao
	private String storeType;// 存储类型

	private int special;

	private int vethAdaptorNum;

	// 云数据备份用到的变量
	private String bkType;

	private String bkInstanceName;

	private String bkFrequency;

	private String bkVMUrl;

	// 虚机备份空间使用情况
	private String bkStorageSize;

	// 云监控用到的变量
	// added by hefk 20120514
	// to fix bug: 0001754
	private String networkDesc;// 监控信息内容

	// 对象存储:
	// added by hefk 20120608
	// 模板表的属性扩充字段:文件最大字节数,备份份数
	// maxByteSize,backNum
	private String extendAttrJson;

	private String hostIpAddress;

	private String ipAddress;

	private String ipAddressId;

	private String ipType;

	private String serviceProvider;

	private List<TNicsBO> nicsBOs;

	private List<TInstanceInfoBO> storageInstances;// vm所挂的磁盘

	private String aboutRes;// 依赖资源

	private TProductBO productBO;// 实例关联的产品信息

	private AsyncJobVDCPO asynJobVDCPO; // 实例管理Job信息

	private TUserLogVO userLogVO; // 用户操作日志

	private TProductInstanceInfoRefBO productInstanceInfoRefBO; // 服务实例PAAS

	private TServiceInstanceBO serviceInstanceBO;

	private int userId;// 用户Id

	private String account;// 用户账户

	private String resCode; // 资源池实例资源编编码

	private Date expireDate;// 过期日期

	private int period;

	private String unit;
//fix bug:7736
	private double price;

	// 服务实例ID
	private int serviceInstanceId;

	private String password;

	private int mcType;

	// added by zhanghuizheng 增加部门
	private int deptId;

	private String deptName;

	private int serviceState;

	private int serviceType;

	// add by CQ fix bug 5231
	private String lbProtocol;

	private String lbPort;

	private String lbPolicy;

	private int eHostId;// 主机ID

	public TInstanceInfoBO() {
	}

	public TInstanceInfoBO(int serviceId, int orderId, String serviceName, int templateType, int templateId, String templateName, String comment,
			String resourceInfo, int cpuNum, int memorySize, int storageSize, Long zoneId, long clusterId, Date createDt, int state,
			Date lastupdateDt, long eInstanceId, Long eServiceId, long eDiskId, long eNetworkId, long eOsId, String osDesc,
			String instanceName, int productId, String templateTypeName, String storeType, String account, String hostIpAddress,
	                       String aboutRes, String password, int special, int orderType, int orderState, int serviceHistoryState, int serviceHistoryId) {
		super();
		this.serviceId = serviceId;
		this.orderId = orderId;
		this.orderState = orderState;
		this.serviceName = serviceName;
		this.templateType = templateType;
		this.templateId = templateId;
		this.templateName = templateName;
		this.comment = comment;
		this.resourceInfo = resourceInfo;
		this.cpuNum = cpuNum;
		this.memorySize = memorySize;
		this.storageSize = storageSize;
		this.zoneId = zoneId;
		this.clusterId = clusterId;
		this.createDt = createDt;
		this.state = state;
		this.lastupdateDt = lastupdateDt;
		this.eInstanceId = eInstanceId;
		this.eServiceId = eServiceId;
		this.eDiskId = eDiskId;
		this.eNetworkId = eNetworkId;
		this.eOsId = eOsId;
		this.osDesc = osDesc;
		this.instanceName = instanceName;
		this.productId = productId;
		this.templateTypeName = templateTypeName;
		this.storeType = storeType;
		this.account = account;
		this.hostIpAddress = hostIpAddress;
		this.aboutRes = aboutRes;
		this.password = password;
		this.special = special;
		this.orderType = orderType;
		this.serviceHistoryState = serviceHistoryState;
		this.serviceHistoryId = serviceHistoryId;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public int getServiceHistoryId() {
		return serviceHistoryId;
	}

	public void setServiceHistoryId(int serviceHistoryId) {
		this.serviceHistoryId = serviceHistoryId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getHostIpAddress() {
		return hostIpAddress;
	}

	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBkStorageSize() {
		return bkStorageSize;
	}

	public void setBkStorageSize(String bkStorageSize) {
		this.bkStorageSize = bkStorageSize;
	}

	public String getAboutRes() {
		return aboutRes;
	}

	public void setAboutRes(String aboutRes) {
		this.aboutRes = aboutRes;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddressId() {
		return ipAddressId;
	}

	public void setIpAddressId(String ipAddressId) {
		this.ipAddressId = ipAddressId;
	}

	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(String resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId == null ? 0 : zoneId;// bug 0004876
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getLastupdateDt() {
		return lastupdateDt;
	}

	public void setLastupdateDt(Date lastupdateDt) {
		this.lastupdateDt = lastupdateDt;
	}

	public long geteInstanceId() {
		return eInstanceId;
	}

	public void seteInstanceId(long eInstanceId) {
		this.eInstanceId = eInstanceId;
	}

	public Long geteServiceId() {
		return eServiceId;
	}

	public void seteServiceId(Long eServiceId) {
		this.eServiceId = eServiceId;
	}

	public long geteDiskId() {
		return eDiskId;
	}

	public void seteDiskId(long eDiskId) {
		this.eDiskId = eDiskId;
	}

	public long geteNetworkId() {
		return eNetworkId;
	}

	public void seteNetworkId(long eNetworkId) {
		this.eNetworkId = eNetworkId;
	}

	public long geteOsId() {
		return eOsId;
	}

	public void seteOsId(long eOsId) {
		this.eOsId = eOsId;
	}

	public String getOsDesc() {
		return osDesc;
	}

	public void setOsDesc(String osDesc) {
		this.osDesc = osDesc;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setNicsBOs(List<TNicsBO> nicsBOs) {
		this.nicsBOs = nicsBOs;
	}

	public List<TNicsBO> getNicsBOs() {
		return nicsBOs;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setCpufrequency(float cpufrequency) {
		this.cpufrequency = cpufrequency;
	}

	public float getCpufrequency() {
		return cpufrequency;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public String getPool_name() {
		return pool_name;
	}

	public void setPool_name(String pool_name) {
		this.pool_name = pool_name;
	}

	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVolumestate() {
		return volumestate;
	}

	public void setVolumestate(String volumestate) {
		this.volumestate = volumestate;
	}

	public String getVmInstanceId() {
		return vmInstanceId;
	}

	public void setVmInstanceId(String vmInstanceId) {
		this.vmInstanceId = vmInstanceId;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setOrderMemorySize(int orderMemorySize) {
		this.orderMemorySize = orderMemorySize;
	}

	public int getOrderMemorySize() {
		return orderMemorySize;
	}

	public void setStorageInstances(List<TInstanceInfoBO> storageInstances) {
		this.storageInstances = storageInstances;
	}

	public List<TInstanceInfoBO> getStorageInstances() {
		return storageInstances;
	}

	public int getOrderCpuNum() {
		return orderCpuNum;
	}

	public void setOrderCpuNum(int orderCpuNum) {
		this.orderCpuNum = orderCpuNum;
	}

	public void setProductBO(TProductBO productBO) {
		this.productBO = productBO;
	}

	public TProductBO getProductBO() {
		return productBO;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductId() {
		return productId;
	}

	public AsyncJobVDCPO getAsynJobVDCPO() {
		return asynJobVDCPO;
	}

	public void setAsynJobVDCPO(AsyncJobVDCPO asynJobVDCPO) {
		this.asynJobVDCPO = asynJobVDCPO;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getBkType() {
		return bkType;
	}

	public void setBkType(String bkType) {
		this.bkType = bkType;
	}

	public String getBkInstanceName() {
		return bkInstanceName;
	}

	public void setBkInstanceName(String bkInstanceName) {
		this.bkInstanceName = bkInstanceName;
	}

	public String getBkFrequency() {
		return bkFrequency;
	}

	public void setBkFrequency(String bkFrequency) {
		this.bkFrequency = bkFrequency;
	}

	public String getBkVMUrl() {
		return bkVMUrl;
	}

	public void setBkVMUrl(String bkVMUrl) {
		this.bkVMUrl = bkVMUrl;
	}

	public TUserLogVO getUserLogVO() {
		return userLogVO;
	}

	public void setUserLogVO(TUserLogVO userLogVO) {
		this.userLogVO = userLogVO;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getORDER_STORAGE_SIZE() {
		return ORDER_STORAGE_SIZE;
	}

	public void setORDER_STORAGE_SIZE(int oRDER_STORAGE_SIZE) {
		ORDER_STORAGE_SIZE = oRDER_STORAGE_SIZE;
	}

	public String getNetworkDesc() {
		return networkDesc;
	}

	public void setNetworkDesc(String networkDesc) {
		this.networkDesc = networkDesc;
	}

	public String getExtendAttrJson() {
		return extendAttrJson;
	}

	public void setExtendAttrJson(String extendAttrJson) {
		this.extendAttrJson = extendAttrJson;
	}

	public TProductInstanceInfoRefBO getProductInstanceInfoRefBO() {
		return productInstanceInfoRefBO;
	}

	public void setProductInstanceInfoRefBO(TProductInstanceInfoRefBO productInstanceInfoRefBO) {
		this.productInstanceInfoRefBO = productInstanceInfoRefBO;
	}

	public TServiceInstanceBO getServiceInstanceBO() {
		return serviceInstanceBO;
	}

	public void setServiceInstanceBO(TServiceInstanceBO serviceInstanceBO) {
		this.serviceInstanceBO = serviceInstanceBO;
	}

	public int getTemplateCpuNum() {
		return templateCpuNum;
	}

	public void setTemplateCpuNum(int templateCpuNum) {
		this.templateCpuNum = templateCpuNum;
	}

	public int getTemplateMemorySize() {
		return templateMemorySize;
	}

	public void setTemplateMemorySize(int templateMemorySize) {
		this.templateMemorySize = templateMemorySize;
	}

	public int getTemplateStorageSize() {
		return templateStorageSize;
	}

	public void setTemplateStorageSize(int templateStorageSize) {
		this.templateStorageSize = templateStorageSize;
	}

	public String getTemplateTypeName() {
		return templateTypeName;
	}

	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public int getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(int serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMcType() {
		return mcType;
	}

	public void setMcType(int mcType) {
		this.mcType = mcType;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int geteHostId() {
		return eHostId;
	}

	public void seteHostId(int eHostId) {
		this.eHostId = eHostId;
	}

	public int getServiceState() {
		return serviceState;
	}

	public void setServiceState(int serviceState) {
		this.serviceState = serviceState;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getVethAdaptorNum() {
		return vethAdaptorNum;
	}

	public void setVethAdaptorNum(int vethAdaptorNum) {
		this.vethAdaptorNum = vethAdaptorNum;
	}

	public String getLbProtocol() {
		return lbProtocol;
	}

	public void setLbProtocol(String lbProtocol) {
		this.lbProtocol = lbProtocol;
	}

	public String getLbPort() {
		return lbPort;
	}

	public void setLbPort(String lbPort) {
		this.lbPort = lbPort;
	}

	public String getLbPolicy() {
		return lbPolicy;
	}

	public void setLbPolicy(String lbPolicy) {
		this.lbPolicy = lbPolicy;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getServiceHistoryState() {
		return serviceHistoryState;
	}

	public void setServiceHistoryState(int serviceHistoryState) {
		this.serviceHistoryState = serviceHistoryState;
	}

}
