package com.skycloud.management.portal.admin.template.entity;

public class TTemplateMCBO extends TTemplateBaseBO {

	private String code; //模板编码
	private String cputype; //CPU类型   CPU的类型，例如“IBM POWER7”
	private float cpufrequency = -1; //CPU主频    处理器主频 :单位：GHz
	private int cpuNum = -1; //CPU单元数量    小型机的CPU单元数量，单位：个
//	private int VProcessor; //虚拟处理器个数    小型机的虚拟处理器个数，单位：个
	private int memorySize = -1; //内存容量   小型机的内存大小，单位：MB
	private int storageSize = -1; //硬盘大小 单位:G
	private String osDiskType; //操作系统磁盘类型
	private int osSize = -1; //操作系统磁盘空间    小型机的操作系统磁盘空间，单位：GB
	private int vethAdaptorNum = -1; //虚拟网卡个数    小型机的虚拟网卡个数，单位：个
	private int vscsiAdaptorNum = -1; //虚拟SCSI卡个数    小型机的虚拟SCSI卡个数，单位：个
	private int vhbaNum = -1; //虚拟HBA卡个数    小型机的虚拟HBA个数，单位：个???
	private String vmos; //操作系统标识    操作系统标识，不同的标识代表虚拟机的操作系统类型、版本、32/64位以及软件配置等信息
	private int state = -1; //模板状态；1：草稿；2：已提交；3：确认可用；4：确认不可用；5：发布；
	private int resourcePoolTemplateId = -1; //资源池中的小型机模板ID
	private int operType = -1;
	private String stateName;
	private String measureMode;

    //1.3功能，支持多资源池，增加资源域zoneId字段
    private int zoneId;
    private String zoneName;
    //1.3功能，支持特殊资源模板，增加special字段，用以区分是否用户定义的特殊模板 1：管理员定义的模板，2：用户定义的特殊模板
    private int special;
	private String resourcePoolsName;
	//小型机模板id
	private int id;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCputype() {
		return cputype;
	}
	public void setCputype(String cputype) {
		this.cputype = cputype;
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
	public int getVhbaNum() {
		return vhbaNum;
	}
	public void setVhbaNum(int vhbaNum) {
		this.vhbaNum = vhbaNum;
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
	public int getResourcePoolTemplateId() {
		return resourcePoolTemplateId;
	}
	public void setResourcePoolTemplateId(int resourcePoolTemplateId) {
		this.resourcePoolTemplateId = resourcePoolTemplateId;
	}
	public int getOperType() {
		return operType;
	}
	public void setOperType(int operType) {
		this.operType = operType;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getMeasureMode() {
		return measureMode;
	}
	public void setMeasureMode(String measureMode) {
		this.measureMode = measureMode;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
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
