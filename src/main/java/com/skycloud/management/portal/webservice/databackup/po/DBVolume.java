package com.skycloud.management.portal.webservice.databackup.po;

import com.skycloud.management.portal.webservice.databackup.utils.JacksonUtils;

/**
 * Elater接口查询卷详细信息对应实体类
  *<dl>
  *<dt>类名：DBVolume</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:32:12</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DBVolume {

	private String id = "";
	
	private String name = "";
	
	private String zoneid = "";
	
	private String zonename = "";
	
	private String type = "";
	
	private String deviceid = "";
	
	private String virtualmachineid = "";
	
	private String vmname = "";
	
	private String vmdisplayname = "";
	
	private String vmstate = "";
	
	private String size = "";
	
	private String created = "";
	
	private String state = "";
	
	private String account = "";
	
	private String domainid = "";
	
	private String domain = "";
	
	private String storagetype = "";
	
	private String hypervisor = "";
	
	private String storage = "";
	
	private String destroyed = "";
	
	private String serviceofferingid = "";
	
	private String serviceofferingname = "";
	
	private String serviceofferingdisplaytext = "";
	
	private String isextractable = "";

	private String diskofferingid ="";
	
	private String diskofferingname = "";
	
	private String attached ="";
	
	private String diskofferingdisplaytext="";
	
	
	public String getDiskofferingdisplaytext() {
		return diskofferingdisplaytext;
	}

	public void setDiskofferingdisplaytext(String diskofferingdisplaytext) {
		this.diskofferingdisplaytext = diskofferingdisplaytext;
	}

	public String getDiskofferingid() {
		return diskofferingid;
	}

	public void setDiskofferingid(String diskofferingid) {
		this.diskofferingid = diskofferingid;
	}

	public String getDiskofferingname() {
		return diskofferingname;
	}

	public void setDiskofferingname(String diskofferingname) {
		this.diskofferingname = diskofferingname;
	}

	public String getAttached() {
		return attached;
	}

	public void setAttached(String attached) {
		this.attached = attached;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZoneid() {
		return zoneid;
	}

	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	public String getZonename() {
		return zonename;
	}

	public void setZonename(String zonename) {
		this.zonename = zonename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getVirtualmachineid() {
		return virtualmachineid;
	}

	public void setVirtualmachineid(String virtualmachineid) {
		this.virtualmachineid = virtualmachineid;
	}

	public String getVmname() {
		return vmname;
	}

	public void setVmname(String vmname) {
		this.vmname = vmname;
	}

	public String getVmdisplayname() {
		return vmdisplayname;
	}

	public void setVmdisplayname(String vmdisplayname) {
		this.vmdisplayname = vmdisplayname;
	}

	public String getVmstate() {
		return vmstate;
	}

	public void setVmstate(String vmstate) {
		this.vmstate = vmstate;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getStoragetype() {
		return storagetype;
	}

	public void setStoragetype(String storagetype) {
		this.storagetype = storagetype;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getDestroyed() {
		return destroyed;
	}

	public void setDestroyed(String destroyed) {
		this.destroyed = destroyed;
	}

	public String getServiceofferingid() {
		return serviceofferingid;
	}

	public void setServiceofferingid(String serviceofferingid) {
		this.serviceofferingid = serviceofferingid;
	}

	public String getServiceofferingname() {
		return serviceofferingname;
	}

	public void setServiceofferingname(String serviceofferingname) {
		this.serviceofferingname = serviceofferingname;
	}

	public String getServiceofferingdisplaytext() {
		return serviceofferingdisplaytext;
	}

	public void setServiceofferingdisplaytext(String serviceofferingdisplaytext) {
		this.serviceofferingdisplaytext = serviceofferingdisplaytext;
	}

	public String getIsextractable() {
		return isextractable;
	}

	public void setIsextractable(String isextractable) {
		this.isextractable = isextractable;
	}
	public static void main (String arg []) throws Exception{
		String str = "{\"id\":519,\"name\":\"ROOT-350\",\"zoneid\":1,\"zonename\":\"wgzx-1\",\"type\":\"ROOT\",\"deviceid\":0,\"virtualmachineid\":350,\"vmname\":\"i-1-350-VM\",\"vmdisplayname\":\"1221testvm6\",\"vmstate\":\"Running\",\"size\":53687091200,\"created\":\"2012-01-04T10:47:02+0800\",\"state\":\"Ready\",\"account\":\"system\",\"domainid\":1,\"domain\":\"ROOT\",\"storagetype\":\"shared\",\"hypervisor\":\"XenServer\",\"storage\":\"nfs-pool1\",\"destroyed\":false,\"serviceofferingid\":2,\"serviceofferingname\":\"中型实例\",\"serviceofferingdisplaytext\":\"中型实例\",\"isextractable\":false} ";
		DBVolume volume = (DBVolume)JacksonUtils.fromJson(str,DBVolume.class);
		System.out.println(volume.getZonename());
	}
}
