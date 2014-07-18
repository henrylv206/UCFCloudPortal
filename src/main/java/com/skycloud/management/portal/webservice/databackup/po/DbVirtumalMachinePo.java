package com.skycloud.management.portal.webservice.databackup.po;

import java.util.List;

import com.skycloud.management.portal.webservice.databackup.utils.JacksonUtils;
/**
 * Elaster接口返回虚拟机信息实体对应类
  *<dl>
  *<dt>类名：DbVirtumalMachinePo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:29:59</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DbVirtumalMachinePo {

	private String id;
	
	private String account;
	
	private String cpuused;
	
	private String forvirtualnetwork;
	
	private String hostid;
	
    private String hostname;
    
	private String isoid;
	
	private String isoname;
	
	private String houstname;
	
	private String networkkbsread;
	
	private String networkkbswrite;
	
	private String password;
	
	private String name;	
	
	private String domainid;
	
	private String domain;
	
	private String create;
	
	private String state;
	
	private String haenable;
	
	private String groupid;
	
	private String group;
	
	private String displayname;
	
	private String zoneid;
	
	private String zonename;
	
	private String templateid;
	
	private String templatename;
	
	private String templatedisplaytext;
	
	private String passwordenabled;
	
	private String serviceofferingid;
	
	private String cpunumber;
	
	private String cpuspeed;
	
	private String memory;
	
	private String guestosid;
	
	private String rootdeviceid;
	
	private String rootdevicetype;
	
	private String serviceofferingname;
	
	private String[] securitygroup;
	
	private List<DbNic> nic;

	private String hypervisor;

	private String created;
	
	private String memoryInternalFree;
	
	private List<NicReaderPo> nicReadKBs;
	
	private List<HardDiskPo> harddiskKbsRead;
	
	private List<HardDiskPo> harddiskKbsWrite;

	private List<NicReaderPo> nicWriteKBs;

	
	public List<NicReaderPo> getNicWriteKBs() {
		return nicWriteKBs;
	}

	public void setNicWriteKBs(List<NicReaderPo> nicWriteKBs) {
		this.nicWriteKBs = nicWriteKBs;
	}

	public List<HardDiskPo> getHarddiskKbsRead() {
		return harddiskKbsRead;
	}

	public void setHarddiskKbsRead(List<HardDiskPo> harddiskKbsRead) {
		this.harddiskKbsRead = harddiskKbsRead;
	}

	public List<HardDiskPo> getHarddiskKbsWrite() {
		return harddiskKbsWrite;
	}

	public void setHarddiskKbsWrite(List<HardDiskPo> harddiskKbsWrite) {
		this.harddiskKbsWrite = harddiskKbsWrite;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public List<NicReaderPo> getNicReadKBs() {
		return nicReadKBs;
	}

	public void setNicReadKBs(List<NicReaderPo> nicReadKBs) {
		this.nicReadKBs = nicReadKBs;
	}

	public String getMemoryInternalFree() {
		return memoryInternalFree;
	}

	public void setMemoryInternalFree(String memoryInternalFree) {
		this.memoryInternalFree = memoryInternalFree;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getZoneid() {
		return zoneid;
	}

	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public String getServiceofferingid() {
		return serviceofferingid;
	}

	public void setServiceofferingid(String serviceofferingid) {
		this.serviceofferingid = serviceofferingid;
	}

	public List<DbNic> getNic() {
		return nic;
	}

	public void setNic(List<DbNic> nic) {
		this.nic = nic;
	}
	
	public String[] getSecuritygroup() {
		return securitygroup;
	}

	public void setSecuritygroup(String[] securitygroup) {
		this.securitygroup = securitygroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHaenable() {
		return haenable;
	}

	public void setHaenable(String haenable) {
		this.haenable = haenable;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getZonename() {
		return zonename;
	}

	public void setZonename(String zonename) {
		this.zonename = zonename;
	}

	public String getTemplatename() {
		return templatename;
	}

	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	public String getTemplatedisplaytext() {
		return templatedisplaytext;
	}

	public void setTemplatedisplaytext(String templatedisplaytext) {
		this.templatedisplaytext = templatedisplaytext;
	}

	public String getPasswordenabled() {
		return passwordenabled;
	}

	public void setPasswordenabled(String passwordenabled) {
		this.passwordenabled = passwordenabled;
	}

	public String getCpunumber() {
		return cpunumber;
	}

	public void setCpunumber(String cpunumber) {
		this.cpunumber = cpunumber;
	}

	public String getCpuspeed() {
		return cpuspeed;
	}

	public void setCpuspeed(String cpuspeed) {
		this.cpuspeed = cpuspeed;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getGuestosid() {
		return guestosid;
	}

	public void setGuestosid(String guestosid) {
		this.guestosid = guestosid;
	}

	public String getRootdeviceid() {
		return rootdeviceid;
	}

	public void setRootdeviceid(String rootdeviceid) {
		this.rootdeviceid = rootdeviceid;
	}

	public String getRootdevicetype() {
		return rootdevicetype;
	}

	public void setRootdevicetype(String rootdevicetype) {
		this.rootdevicetype = rootdevicetype;
	}

	public String getServiceofferingname() {
		return serviceofferingname;
	}

	public void setServiceofferingname(String serviceofferingname) {
		this.serviceofferingname = serviceofferingname;
	}

	public String getCpuused() {
		return cpuused;
	}

	public void setCpuused(String cpuused) {
		this.cpuused = cpuused;
	}

	public String getForvirtualnetwork() {
		return forvirtualnetwork;
	}

	public void setForvirtualnetwork(String forvirtualnetwork) {
		this.forvirtualnetwork = forvirtualnetwork;
	}

	public String getHostid() {
		return hostid;
	}

	public void setHostid(String hostid) {
		this.hostid = hostid;
	}

	public String getIsoid() {
		return isoid;
	}

	public void setIsoid(String isoid) {
		this.isoid = isoid;
	}

	public String getIsoname() {
		return isoname;
	}

	public void setIsoname(String isoname) {
		this.isoname = isoname;
	}

	public String getHoustname() {
		return houstname;
	}

	public void setHoustname(String houstname) {
		this.houstname = houstname;
	}

	public String getNetworkkbsread() {
		return networkkbsread;
	}

	public void setNetworkkbsread(String networkkbsread) {
		this.networkkbsread = networkkbsread;
	}

	public String getNetworkkbswrite() {
		return networkkbswrite;
	}

	public void setNetworkkbswrite(String networkkbswrite) {
		this.networkkbswrite = networkkbswrite;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
