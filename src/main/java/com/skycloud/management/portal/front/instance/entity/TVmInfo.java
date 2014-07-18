package com.skycloud.management.portal.front.instance.entity;

import java.util.List;

/**
 * TVmInfo entity. @author MyEclipse Persistence Tools
 */

public class TVmInfo implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -67750782343853819L;
	private int productId;
	private String flag;
	private String mutliVM;
	private long vlan;
	private String ipAddress;
	private int ipAddressId;
	private long vlan2;
	private String ipAddress2;
	private long vlan3;
	private String ipAddress3;
	private long vlan4;
	private String ipAddress4;
	private int clusterId; // 集群ID
	private int zoneId; // ZoneId

	private int poolId;// 资源池
	private int hostId;// 主机ID，物理机、小机
	private int osId;// 操作系统ID
	private int orderId;// 订单ID
	private String osDesc;
	private String description;
	private String instanceName;
	private int templateId; // 模板ID
	private int state; // 1.未开通 2.使用中3.释放
	private int templateType; // 资源模板类型；1： 虚拟机；2：小型机；3：X86物理机
	private String templateCode;
	private String templateDesc;
	private String osDiskType;
	private int osSize;
	private String vmos;// 操作系统标识，不同的标识代表虚拟机的操作系统类型、版本、32/64位以及软件配置等信息
	private float cpufrequency;
	private int cpuNum;
	private int memorySize;
	private int disknumber;
	private int storageSize;
	private int vethAdaptorNum;

	private int concurrentNum;// Lb并发数

	private String protocol;// Lb协议类型

	private String policy;// Lb策略

	private int port;// Lb端口

	//------
	private String zoneName;
	private String period;
	private String unit;
	private String template;
	private String virtual;

	//------
	private String backupPath;
	private String frequency;
	private String vstype;
	private int target;
	private String comment;
	private String targetName;
	private String plunit;  //备份频率单位



	private String ipInstanceId;
	private String ipType; //公网IP类型，4--IPV4或6--IPV6
	private String periodInfo;//前台页面传过来的购买周期格式：数量+单位 如：period:2Y 表示两年。如果前台页面中period字段没有其他的用途，用这个字段存也行
	private String productName;
	//fix bug 3699 3559
	private float charge;

	private List<String> ipArray;//多虚机服务的指定IP设置
	private List<String> vlanArray; //多虚机服务的指定vlanId
	private List<String> vmNameArray; //多虚机名称
	private List<Integer> templateNum; //模板的购买数量
	private List<String> vmDescArray;//多虚机描述
	//fix bug 3881
	private List<Long> multiTemplateId;//多虚机模版

	private String storeType;
	
	private String extendAttrJson;

	private int instanceInfoId;

	public TVmInfo() {
		super();
	}

	public TVmInfo(String flag, long vlan, String ipAddress, int ipAddressId, long vlan2,long vlan3,long vlan4,
			String ipAddress2,String ipAddress3,String ipAddress4, int clusterId, String osDesc,
			String description, String instanceName, int templateId, int state,
			int templateType, String templateCode, String templateDesc,
			String osDiskType, int osSize, String vmos, float cpufrequency,
			int cpuNum, int memorySize, int storageSize, int vethAdaptorNum,
			int concurrentNum, String protocol, String policy, int port,
			int product, String zoneName, String period, String template, String virtual, int poolId,
			int orderId, int hostId, int osId,
			List<String> ipArray,List<String> vlanArray,List<String> vmNameArray,String mutliVM, String storeType, String extendAttrJson) {
		super();
		this.ipArray = ipArray;
		this.vlanArray = vlanArray;
		this.vmNameArray = vmNameArray;
		this.flag = flag;
		this.vlan = vlan;
		this.mutliVM = mutliVM;
		this.ipAddress = ipAddress;
		this.vlan2 = vlan2;
		this.vlan3 = vlan3;
		this.vlan4 = vlan4;
		this.ipAddress2 = ipAddress2;
		this.ipAddress3 = ipAddress3;
		this.ipAddress4 = ipAddress4;
		this.ipAddressId = ipAddressId;
		this.clusterId = clusterId;
		this.osDesc = osDesc;
		this.description = description;
		this.instanceName = instanceName;
		this.templateId = templateId;
		this.state = state;
		this.templateType = templateType;
		this.templateCode = templateCode;
		this.templateDesc = templateDesc;
		this.osDiskType = osDiskType;
		this.osSize = osSize;
		this.vmos = vmos;
		this.cpufrequency = cpufrequency;
		this.cpuNum = cpuNum;
		this.memorySize = memorySize;
		this.storageSize = storageSize;
		this.vethAdaptorNum = vethAdaptorNum;
		this.productId = productId;
		this.zoneName = zoneName;
		this.period = period;
		this.template = template;
		this.virtual = virtual;
		this.poolId = poolId;
		this.concurrentNum = concurrentNum;
		this.protocol = protocol;
		this.policy = policy;
		this.port = port;
		this.orderId = orderId;
		this.hostId = hostId;
		this.osId = osId;
		this.storeType = storeType;
		this.extendAttrJson = extendAttrJson;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public long getVlan() {
		return vlan;
	}

	public void setVlan(long vlan) {
		this.vlan = vlan;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getVlan2() {
		return vlan2;
	}

	public void setVlan2(long vlan2) {
		this.vlan2 = vlan2;
	}

	public String getIpAddress2() {
		return ipAddress2;
	}

	public void setIpAddress2(String ipAddress2) {
		this.ipAddress2 = ipAddress2;
	}

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public String getOsDesc() {
		return osDesc;
	}

	public void setOsDesc(String osDesc) {
		this.osDesc = osDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
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

	public String getOsDiskType() {
		return osDiskType;
	}

	public void setOsDiskType(String osDiskType) {
		this.osDiskType = osDiskType;
	}

	public int getOsSize() {
		return osSize;
	}

	public void setOsSize(int osSize) {
		this.osSize = osSize;
	}

	public String getVmos() {
		return vmos;
	}

	public void setVmos(String vmos) {
		this.vmos = vmos;
	}

	public float getCpufrequency() {
		return cpufrequency;
	}

	public void setCpufrequency(float cpufrequency) {
		this.cpufrequency = cpufrequency;
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

	public int getVethAdaptorNum() {
		return vethAdaptorNum;
	}

	public void setVethAdaptorNum(int vethAdaptorNum) {
		this.vethAdaptorNum = vethAdaptorNum;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getVirtual() {
		return virtual;
	}

	public void setVirtual(String virtual) {
		this.virtual = virtual;
	}
	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getVstype() {
		return vstype;
	}

	public void setVstype(String vstype) {
		this.vstype = vstype;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setIpInstanceId(String ipInstanceId) {
		this.ipInstanceId = ipInstanceId;
	}

	public String getIpInstanceId() {
		return ipInstanceId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getIpType()
	{
		return ipType;
	}

	public void setIpType(String ipType)
	{
		this.ipType = ipType;
	}

	public void setPeriodInfo(String periodInfo) {
		this.periodInfo = periodInfo;
	}

	public String getPeriodInfo() {
		return periodInfo;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPlunit() {
		return plunit;
	}

	public void setPlunit(String plunit) {
		this.plunit = plunit;
	}

	public int getIpAddressId() {
		return ipAddressId;
	}

	public void setIpAddressId(int ipAddressId) {
		this.ipAddressId = ipAddressId;
	}

	public List<String> getIpArray() {
		return ipArray;
	}

	public void setIpArray(List<String> ipArray) {
		this.ipArray = ipArray;
	}

	public List<String> getVlanArray() {
		return vlanArray;
	}

	public void setVlanArray(List<String> vlanArray) {
		this.vlanArray = vlanArray;
	}

	public List<String> getVmNameArray() {
		return vmNameArray;
	}

	public void setVmNameArray(List<String> vmNameArray) {
		this.vmNameArray = vmNameArray;
	}

	public String getMutliVM() {
		return mutliVM;
	}

	public void setMutliVM(String mutliVM) {
		this.mutliVM = mutliVM;
	}

	public List<Integer> getTemplateNum() {
		return templateNum;
	}

	public void setTemplateNum(List<Integer> templateNum) {
		this.templateNum = templateNum;
	}

	public List<String> getVmDescArray() {
		return vmDescArray;
	}

	public void setVmDescArray(List<String> vmDescArray) {
		this.vmDescArray = vmDescArray;
	}

	public float getCharge() {
		return charge;
	}

	public void setCharge(float charge) {
		this.charge = charge;
	}

	public List<Long> getMultiTemplateId() {
		return multiTemplateId;
	}

	public void setMultiTemplateId(List<Long> multiTemplateId) {
		this.multiTemplateId = multiTemplateId;
	}

	public int getDisknumber() {
		return disknumber;
	}

	public void setDisknumber(int disknumber) {
		this.disknumber = disknumber;
	}


	public long getVlan3() {
		return vlan3;
	}


	public void setVlan3(long vlan3) {
		this.vlan3 = vlan3;
	}


	public String getIpAddress3() {
		return ipAddress3;
	}


	public void setIpAddress3(String ipAddress3) {
		this.ipAddress3 = ipAddress3;
	}


	public long getVlan4() {
		return vlan4;
	}


	public void setVlan4(long vlan4) {
		this.vlan4 = vlan4;
	}


	public String getIpAddress4() {
		return ipAddress4;
	}


	public void setIpAddress4(String ipAddress4) {
		this.ipAddress4 = ipAddress4;
	}

	public int getPoolId() {
		return poolId;
	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

	public int getConcurrentNum() {
		return concurrentNum;
	}

	public void setConcurrentNum(int concurrentNum) {
		this.concurrentNum = concurrentNum;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public int getInstanceInfoId() {
		return instanceInfoId;
	}

	public void setInstanceInfoId(int instanceInfoId) {
		this.instanceInfoId = instanceInfoId;
	}

	public String getExtendAttrJson() {
		return extendAttrJson;
	}

	public void setExtendAttrJson(String extendAttrJson) {
		this.extendAttrJson = extendAttrJson;
	}

}