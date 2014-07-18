/**
 * 2011-12-13 上午10:28:14 $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;

/**
 * @author shixq
 * @version $Revision$ 上午10:28:14
 */
public class ResourcesVO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1424483792091615641L;

	private String monitorType;

	private int id;

	private String templateId;

	private String order_id;

	private String template_type;

	private String template_id;

	private String comment;

	private String resource_info;

	private String cpu_num;

	private String memory_size;

	private String storage_size;

	private String zone_id;

	private String cluster_id;

	private String create_dt;

	private String state;

	private String volumestate;

	private String lastupdate_dt;

	private String e_instance_id;

	private String e_service_id;

	private String e_disk_id;

	private String e_network_id;

	private String res_code;

	private String e_os_id;

	private String os_desc;

	private String instance_name;

	private String template_desc;

	private String reason;

	private String pool_name;

	private String cpufrequency;

	private String template_code;

	private String vmname;

	private String vmInstanceId;

	private String eInstanceId;

	private String network_desc;

	private String extend_attr_json;

	private String lbProtocol;

	private String lbPolicy;

	private int lbPort;

	private String vmElasterID;

	private String isois;

	private String serverName;// 物理服务器

	private List<TNicsBO> nicsBOs;

	private String productId;

	private String piName;// 多虚机服务组名

	private String piCode;// 多虚机服务组编码

	private int bsState;

	private String targetIP;

	private String initiatorName;

	private String vmid;

	private int special;

	private int resourcePoolsId;

	private long zoneId; // 数据中心ID

	public int getBsState() {
		return bsState;
	}

	public void setBsState(int bsState) {
		this.bsState = bsState;
	}

	public String getTargetIP() {
		return targetIP;
	}

	public void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}

	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	public String getVmid() {
		return vmid;
	}

	public void setVmid(String vmid) {
		this.vmid = vmid;
	}

	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}

	public String getPiCode() {
		return piCode;
	}

	public void setPiCode(String piCode) {
		this.piCode = piCode;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public List<TNicsBO> getNicsBOs() {
		return nicsBOs;
	}

	public void setNicsBOs(List<TNicsBO> nicsBOs) {
		this.nicsBOs = nicsBOs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getTemplate_type() {
		return template_type;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public long getZoneId() {
		return zoneId;
	}

	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getResource_info() {
		return resource_info;
	}

	public void setResource_info(String resource_info) {
		this.resource_info = resource_info;
	}

	public String getCpu_num() {
		return cpu_num;
	}

	public void setCpu_num(String cpu_num) {
		this.cpu_num = cpu_num;
	}

	public String getMemory_size() {
		return memory_size;
	}

	public void setMemory_size(String memory_size) {
		this.memory_size = memory_size;
	}

	public String getStorage_size() {
		return storage_size;
	}

	public void setStorage_size(String storage_size) {
		this.storage_size = storage_size;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	public String getCluster_id() {
		return cluster_id;
	}

	public void setCluster_id(String cluster_id) {
		this.cluster_id = cluster_id;
	}

	public String getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVolumestate() {
		return volumestate;
	}

	public void setVolumestate(String volumestate) {
		this.volumestate = volumestate;
	}

	public String getLastupdate_dt() {
		return lastupdate_dt;
	}

	public void setLastupdate_dt(String lastupdate_dt) {
		this.lastupdate_dt = lastupdate_dt;
	}

	public String getE_instance_id() {
		return e_instance_id;
	}

	public void setE_instance_id(String e_instance_id) {
		this.e_instance_id = e_instance_id;
	}

	public String getE_service_id() {
		return e_service_id;
	}

	public void setE_service_id(String e_service_id) {
		this.e_service_id = e_service_id;
	}

	public String getE_disk_id() {
		return e_disk_id;
	}

	public void setE_disk_id(String e_disk_id) {
		this.e_disk_id = e_disk_id;
	}

	public String getE_network_id() {
		return e_network_id;
	}

	public void setE_network_id(String e_network_id) {
		this.e_network_id = e_network_id;
	}

	public String getE_os_id() {
		return e_os_id;
	}

	public void setE_os_id(String e_os_id) {
		this.e_os_id = e_os_id;
	}

	public String getOs_desc() {
		return os_desc;
	}

	public void setOs_desc(String os_desc) {
		this.os_desc = os_desc;
	}

	public String getInstance_name() {
		return instance_name;
	}

	public void setInstance_name(String instance_name) {
		this.instance_name = instance_name;
	}

	public String getTemplate_desc() {
		return template_desc;
	}

	public void setTemplate_desc(String template_desc) {
		this.template_desc = template_desc;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPool_name() {
		return pool_name;
	}

	public void setPool_name(String pool_name) {
		this.pool_name = pool_name;
	}

	public String getCpufrequency() {
		return cpufrequency;
	}

	public void setCpufrequency(String cpufrequency) {
		this.cpufrequency = cpufrequency;
	}

	public String getTemplate_code() {
		return template_code;
	}

	public void setTemplate_code(String template_code) {
		this.template_code = template_code;
	}

	public String getVmname() {
		return vmname;
	}

	public void setVmname(String vmname) {
		this.vmname = vmname;
	}

	public String getVmInstanceId() {
		return vmInstanceId;
	}

	public void setVmInstanceId(String vmInstanceId) {
		this.vmInstanceId = vmInstanceId;
	}

	public String geteInstanceId() {
		return eInstanceId;
	}

	public void seteInstanceId(String eInstanceId) {
		this.eInstanceId = eInstanceId;
	}

	public String getIsois() {
		return isois;
	}

	public void setIsois(String isois) {
		this.isois = isois;
	}

	public String getNetwork_desc() {
		return network_desc;
	}

	public void setNetwork_desc(String network_desc) {
		this.network_desc = network_desc;
	}

	public String getVmElasterID() {
		return vmElasterID;
	}

	public void setVmElasterID(String vmElasterID) {
		this.vmElasterID = vmElasterID;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRes_code() {
		return res_code;
	}

	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}

	public String getExtend_attr_json() {
		return extend_attr_json;
	}

	public void setExtend_attr_json(String extend_attr_json) {
		this.extend_attr_json = extend_attr_json;
	}

	public String getLbProtocol() {
		return lbProtocol;
	}

	public void setLbProtocol(String lbProtocol) {
		this.lbProtocol = lbProtocol;
	}

	public String getLbPolicy() {
		return lbPolicy;
	}

	public void setLbPolicy(String lbPolicy) {
		this.lbPolicy = lbPolicy;
	}

	public int getLbPort() {
		return lbPort;
	}

	public void setLbPort(int lbPort) {
		this.lbPort = lbPort;
	}

}
