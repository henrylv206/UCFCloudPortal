package com.skycloud.management.portal.admin.template.entity;

public class TTemplateVMBO extends TTemplateBaseBO {

	private String code; //模板编码
	private float cpufrequency; //处理器主频 :单位：GHz
	private int cpuNum; //虚拟机的CPU单元数量，单位：个
	private int memorySize; //虚拟机的内存大小，单位：MB
	private int storageSize; //硬盘大小 单位:G
	private String osDiskType; //虚拟机OS安装盘的类型，内置硬盘、共享FC SAN、共享IP SAN等
	private int osSize; //虚拟机的操作系统磁盘空间，单位：GB。
	private int vethAdaptorNum; //虚拟机的虚拟网卡个数，单位：个
	private int vscsiAdaptorNum; //虚拟机的虚拟SCSI卡个数，单位：个
	private String vmos; //操作系统标识，不同的标识代表虚拟机的操作系统类型、版本、32/64位以及软件配置等信息
	private int state; //模板状态；1：申请；2：可用；3：正在处理； 5：已删除； 7：操作失败；8：资源池可用；9；资源池不可用
	private int eServiceId; //?
	private int eDiskId; //?
	private int eNetworkId; //?
	private String networkDesc; //?
	private int eOsId; //?
	private String osDesc; //?

	private String resourceTemplate;
	private int operType;
	private String measureMode;
	private String grade;
	private String extendAttrJSON;
	private String errorCode;
    private int maxCpuNum;
    private int maxMemorySize;
    private int maxStorageSize;
    private String stateName;
    private String storeType;
    private int templateNum;    //模板数量,用于多虚机情况下
    private String protocol;
    private String policy;
    private int port;
    //父类中已有private int resourcePoolsId; //资源池ID
//    private int resourcePoolsId;//20121204 资源池ID 调Elaster接口时区分资源池
    //1.3功能，支持多资源池，增加资源域zoneId字段
    private int zoneId;
    private String zoneName;
    //1.3功能，支持特殊资源模板，增加special字段，用以区分是否用户定义的特殊模板 1：管理员定义的模板，2：用户定义的特殊模板
    private int special;
    private String resourcePoolsName;
    //to fix bug:5330 
    //添加id字段属性，根据id更新模板
    //模板id 
    private int id;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public int getVethAdaptorNum() {
		return vethAdaptorNum;
	}
	public void setVethAdaptorNum(int vethAdaptorNum) {
		this.vethAdaptorNum = vethAdaptorNum;
	}
	public int getVscsiAdaptorNum() {
		return vscsiAdaptorNum;
	}
	public void setVscsiAdaptorNum(int vscsiAdaptorNum) {
		this.vscsiAdaptorNum = vscsiAdaptorNum;
	}
	public String getVmos() {
		return vmos;
	}
	public void setVmos(String vmos) {
		this.vmos = vmos;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int geteServiceId() {
		return eServiceId;
	}
	public void seteServiceId(int eServiceId) {
		this.eServiceId = eServiceId;
	}
	public int geteDiskId() {
		return eDiskId;
	}
	public void seteDiskId(int eDiskId) {
		this.eDiskId = eDiskId;
	}
	public int geteNetworkId() {
		return eNetworkId;
	}
	public void seteNetworkId(int eNetworkId) {
		this.eNetworkId = eNetworkId;
	}
	public String getNetworkDesc() {
		return networkDesc;
	}
	public void setNetworkDesc(String networkDesc) {
		this.networkDesc = networkDesc;
	}
	public int geteOsId() {
		return eOsId;
	}
	public void seteOsId(int eOsId) {
		this.eOsId = eOsId;
	}
	public String getOsDesc() {
		return osDesc;
	}
	public void setOsDesc(String osDesc) {
		this.osDesc = osDesc;
	}
	public String getResourceTemplate() {
		return resourceTemplate;
	}
	public void setResourceTemplate(String resourceTemplate) {
		this.resourceTemplate = resourceTemplate;
	}
	public int getOperType() {
		return operType;
	}
	public void setOperType(int operType) {
		this.operType = operType;
	}
	public String getMeasureMode() {
		return measureMode;
	}
	public void setMeasureMode(String measureMode) {
		this.measureMode = measureMode;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getExtendAttrJSON() {
		return extendAttrJSON;
	}
	public void setExtendAttrJSON(String extendAttrJSON) {
		this.extendAttrJSON = extendAttrJSON;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public int getMaxCpuNum() {
		return maxCpuNum;
	}
	public void setMaxCpuNum(int maxCpuNum) {
		this.maxCpuNum = maxCpuNum;
	}
	public int getMaxMemorySize() {
		return maxMemorySize;
	}
	public void setMaxMemorySize(int maxMemorySize) {
		this.maxMemorySize = maxMemorySize;
	}
	public int getMaxStorageSize() {
		return maxStorageSize;
	}
	public void setMaxStorageSize(int maxStorageSize) {
		this.maxStorageSize = maxStorageSize;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public int getTemplateNum() {
		return templateNum;
	}
	public void setTemplateNum(int templateNum) {
		this.templateNum = templateNum;
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
	//1.3功能，支持多资源池，增加资源域zoneId字段
	public int getZoneId() {
		return zoneId;
	}
	//1.3功能，支持多资源池，增加资源域zoneId字段
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	//1.3功能，支持特殊资源模板，增加special字段
	public int getSpecial() {
		return special;
	}
	//1.3功能，支持特殊资源模板，增加special字段
	public void setSpecial(int special) {
		this.special = special;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getResourcePoolsName() {
		return resourcePoolsName;
	}
	public void setResourcePoolsName(String resourcePoolsName) {
		this.resourcePoolsName = resourcePoolsName;
	}

    @Override
    public int getId() {
    	return id;
    }

    @Override
    public void setId(int id) {
    	this.id = id;
    }





}
