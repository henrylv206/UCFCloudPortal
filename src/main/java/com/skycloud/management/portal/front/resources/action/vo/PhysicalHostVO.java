package com.skycloud.management.portal.front.resources.action.vo;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * 
 * @author wangcf
 * 
 */
public class PhysicalHostVO  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4381446693761955577L;
//	private Long ID;
//	private String IPMI_IP;
//	private String IPMI_USERNAME;
//	private String IPMI_PASSWORD;
	private Long id;
	private String ipmiip;
	private String ipmiusername;
	private String ipmipassword;
	
//	private String NETWORK_CARD_ID;
//	private String NETWORK_CARD_MAC;
//	private String NETWORK_IP;
//	private String NETWORK_SUBNET;//子网掩码，（当时命名有误）
//	private String NETWORK_MASK;//网关，（当时命名有误）
//	private String NETWORK_DNS;
	
//	private String HOST_NAME;
//	private String OS_LOGIN_USER;
//	private String OS_LOGIN_PASSWORD;
	private String name;
	private String loginuser;
	private String loginpwd;
	private String username;
	
//	private Long OS_TYPE;
	private String ostype;
//	private Long ZONE_ID;
//	private Long POD_ID;
//	private Long CLUSTER_ID;
	private Long zoneid;
	private Long podid;
	private Long clusterid;
	
//	private String STATE;
	private String state;
//	private Date CREATE_TIME;
//	private Date UPDATE_TIME;
	private Date createtime;
	private Date updatetime;

	//other properties
//	private String ZONE_NAME;
//	private String POD_NAME;
//	private String CLUSTER_NAME;
	private String zonename;
	private String podname;
	private String clustername;
	
//	private Long MEMORY;
	private Long memory;
	
//	private Long DISK_NUMBER;
//	private Long DISK_SIZE;
	private Long disknumber;
	private Long disksize;
	
//	private Long CPU_NUMBER;
//	private Long CPU_SPEED;
	private Long cpunumber;
	private Long cpuspeed;
	
//	private String PUBLIC_IP;
	private String publicip;
//	private String NET_MAC;
	private String netmac;
//	private String IP_ADDRESS;
	private String ipaddress;

//	private Long VLAN_ID;
	private Long vlanId;
	
	private String powerstate;
	private String osip;

	private String errorcode;
	private String errordesc;
	private String jobid;
	private String result;
	private String reqstate;
	
//	@JSON(name="id")
//	public Long getID() {
//		return ID;
//	}
//
//	public void setID(Long iD) {
//		ID = iD;
//	}
//
//	@JSON(name="ipmiIP")
//	public String getIPMI_IP() {
//		return IPMI_IP;
//	}
//
//	public void setIPMI_IP(String iPMI_IP) {
//		IPMI_IP = iPMI_IP;
//	}
//
//	@JSON(name="ipmiUserName")
//	public String getIPMI_USERNAME() {
//		return IPMI_USERNAME;
//	}
//
//	public void setIPMI_USERNAME(String iPMI_USERNAME) {
//		IPMI_USERNAME = iPMI_USERNAME;
//	}
//
//	@JSON(name="ipmiPassword")
//	public String getIPMI_PASSWORD() {
//		return IPMI_PASSWORD;
//	}
//
//	public void setIPMI_PASSWORD(String iPMI_PASSWORD) {
//		IPMI_PASSWORD = iPMI_PASSWORD;
//	}
//
//	@JSON(name="networkCardId")
//	public String getNETWORK_CARD_ID() {
//		return NETWORK_CARD_ID;
//	}
//
//	public void setNETWORK_CARD_ID(String nETWORK_CARD_ID) {
//		NETWORK_CARD_ID = nETWORK_CARD_ID;
//	}
//
//	@JSON(name="networkCardMac")
//	public String getNETWORK_CARD_MAC() {
//		return NETWORK_CARD_MAC;
//	}
//
//	public void setNETWORK_CARD_MAC(String nETWORK_CARD_MAC) {
//		NETWORK_CARD_MAC = nETWORK_CARD_MAC;
//	}
//
//	@JSON(name="networkIP")
//	public String getNETWORK_IP() {
//		return NETWORK_IP;
//	}
//
//	public void setNETWORK_IP(String nETWORK_IP) {
//		NETWORK_IP = nETWORK_IP;
//	}
//
//	@JSON(name="networkSubnet")
//	public String getNETWORK_SUBNET() {
//		return NETWORK_SUBNET;
//	}
//
//	public void setNETWORK_SUBNET(String nETWORK_SUBNET) {
//		NETWORK_SUBNET = nETWORK_SUBNET;
//	}
//
//	@JSON(name="networkMask")
//	public String getNETWORK_MASK() {
//		return NETWORK_MASK;
//	}
//
//	public void setNETWORK_MASK(String nETWORK_MASK) {
//		NETWORK_MASK = nETWORK_MASK;
//	}
//
//	@JSON(name="networkDns")
//	public String getNETWORK_DNS() {
//		return NETWORK_DNS;
//	}
//
//	public void setNETWORK_DNS(String nETWORK_DNS) {
//		NETWORK_DNS = nETWORK_DNS;
//	}
//
//	@JSON(name="name")
//	public String getHOST_NAME() {
//		return HOST_NAME;
//	}
//
//	public void setHOST_NAME(String hOST_NAME) {
//		HOST_NAME = hOST_NAME;
//	}
//	@JSON(name="loginuser")
//	public String getOS_LOGIN_USER() {
//		return OS_LOGIN_USER;
//	}
//
//	public void setOS_LOGIN_USER(String oS_LOGIN_USER) {
//		OS_LOGIN_USER = oS_LOGIN_USER;
//	}
//
//	@JSON(name="loginpwd")
//	public String getOS_LOGIN_PASSWORD() {
//		return OS_LOGIN_PASSWORD;
//	}
//
//	public void setOS_LOGIN_PASSWORD(String oS_LOGIN_PASSWORD) {
//		OS_LOGIN_PASSWORD = oS_LOGIN_PASSWORD;
//	}
//
//	@JSON(name="zoneId")
//	public Long getZONE_ID() {
//		return ZONE_ID;
//	}
//
//	public void setZONE_ID(Long zONE_ID) {
//		ZONE_ID = zONE_ID;
//	}
//
//	@JSON(name="podId")
//	public Long getPOD_ID() {
//		return POD_ID;
//	}
//
//	public void setPOD_ID(Long pOD_ID) {
//		POD_ID = pOD_ID;
//	}
//
//	@JSON(name="clusterId")
//	public Long getCLUSTER_ID() {
//		return CLUSTER_ID;
//	}
//
//	public void setCLUSTER_ID(Long cLUSTER_ID) {
//		CLUSTER_ID = cLUSTER_ID;
//	}
//
//	@JSON(name="state")
//	public String getSTATE() {
//		return STATE;
//	}
//
//	public void setSTATE(String sTATE) {
//		STATE = sTATE;
//	}
//
//	@JSON(name="createTime")
//	public Date getCREATE_TIME() {
//		return CREATE_TIME;
//	}
//
//	
//	public void setCREATE_TIME(Date cREATE_TIME) {
//		CREATE_TIME = cREATE_TIME;
//	}
//	
//	@JSON(name="updateTime")
//	public Date getUPDATE_TIME() {
//		return UPDATE_TIME;
//	}
//
//	
//	public void setUPDATE_TIME(Date uPDATE_TIME) {
//		UPDATE_TIME = uPDATE_TIME;
//	}

//	@JSON(name="os_type")
//	public Long getOS_TYPE() {
//		return OS_TYPE;
//	}
//
//	public void setOS_TYPE(Long oS_TYPE) {
//		OS_TYPE = oS_TYPE;
//	}
//
//	@JSON(name="clustername")
//	public String getCLUSTER_NAME() {
//		return CLUSTER_NAME;
//	}
//
//	public void setCLUSTER_NAME(String cLUSTER_NAME) {
//		CLUSTER_NAME = cLUSTER_NAME;
//	}
//
//	@JSON(name="disknumber")
//	public Long getDISK_NUMBER() {
//		return DISK_NUMBER;
//	}
//
//	public void setDISK_NUMBER(Long dISK_NUMBER) {
//		DISK_NUMBER = dISK_NUMBER;
//	}
//
//	@JSON(name="disksize")
//	public Long getDISK_SIZE() {
//		return DISK_SIZE;
//	}
//
//	public void setDISK_SIZE(Long dISK_SIZE) {
//		DISK_SIZE = dISK_SIZE;
//	}
//
//	@JSON(name="cpunumber")
//	public Long getCPU_NUMBER() {
//		return CPU_NUMBER;
//	}
//
//	public void setCPU_NUMBER(Long cPU_NUMBER) {
//		CPU_NUMBER = cPU_NUMBER;
//	}
//
//	@JSON(name="cpuspeed")
//	public Long getCPU_SPEED() {
//		return CPU_SPEED;
//	}
//
//	public void setCPU_SPEED(Long cPU_SPEED) {
//		CPU_SPEED = cPU_SPEED;
//	}
//
//	@JSON(name="zonename")
//	public String getZONE_NAME() {
//		return ZONE_NAME;
//	}
//
//	public void setZONE_NAME(String zONE_NAME) {
//		ZONE_NAME = zONE_NAME;
//	}
//
//	@JSON(name="podname")
//	public String getPOD_NAME() {
//		return POD_NAME;
//	}
//
//	public void setPOD_NAME(String pOD_NAME) {
//		POD_NAME = pOD_NAME;
//	}
//
//	@JSON(name="memory")
//	public Long getMEMORY() {
//		return MEMORY;
//	}
//
//	public void setMEMORY(Long mEMORY) {
//		MEMORY = mEMORY;
//	}

//	@JSON(name="publicip")
//	public String getPUBLIC_IP() {
//		return PUBLIC_IP;
//	}
//
//	public void setPUBLIC_IP(String pUBLIC_IP) {
//		PUBLIC_IP = pUBLIC_IP;
//	}
//
//	@JSON(name="netmac")
//	public String getNET_MAC() {
//		return NET_MAC;
//	}
//
//	public void setNET_MAC(String nET_MAC) {
//		NET_MAC = nET_MAC;
//	}
//
//	@JSON(name="vlanId")
//	public Long getVLAN_ID() {
//		return VLAN_ID;
//	}
//
//	public void setVLAN_ID(Long vLAN_ID) {
//		VLAN_ID = vLAN_ID;
//	}
//
//	@JSON(name="ipaddress")
//	public String getIP_ADDRESS() {
//		return IP_ADDRESS;
//	}
//
//	public void setIP_ADDRESS(String iP_ADDRESS) {
//		IP_ADDRESS = iP_ADDRESS;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpmiip() {
		return ipmiip;
	}

	public void setIpmiip(String ipmiip) {
		this.ipmiip = ipmiip;
	}

	public String getIpmiusername() {
		return ipmiusername;
	}

	public void setIpmiusername(String ipmiusername) {
		this.ipmiusername = ipmiusername;
	}

	public String getIpmipassword() {
		return ipmipassword;
	}

	public void setIpmipassword(String ipmipassword) {
		this.ipmipassword = ipmipassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public String getLoginpwd() {
		return loginpwd;
	}

	public void setLoginpwd(String loginpwd) {
		this.loginpwd = loginpwd;
	}

	public Long getZoneid() {
		return zoneid;
	}

	public void setZoneid(Long zoneid) {
		this.zoneid = zoneid;
	}

	public Long getPodid() {
		return podid;
	}

	public void setPodid(Long podid) {
		this.podid = podid;
	}

	public Long getClusterid() {
		return clusterid;
	}

	public void setClusterid(Long clusterid) {
		this.clusterid = clusterid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getZonename() {
		return zonename;
	}

	public void setZonename(String zonename) {
		this.zonename = zonename;
	}

	public String getPodname() {
		return podname;
	}

	public void setPodname(String podname) {
		this.podname = podname;
	}

	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public Long getMemory() {
		return memory;
	}

	public void setMemory(Long memory) {
		this.memory = memory;
	}
	

	public Long getDisknumber() {
		return disknumber;
	}

	public void setDisknumber(Long disknumber) {
		this.disknumber = disknumber;
	}

	public Long getDisksize() {
		return disksize;
	}

	public void setDisksize(Long disksize) {
		this.disksize = disksize;
	}

	public Long getCpunumber() {
		return cpunumber;
	}

	public void setCpunumber(Long cpunumber) {
		this.cpunumber = cpunumber;
	}

	public Long getCpuspeed() {
		return cpuspeed;
	}

	public void setCpuspeed(Long cpuspeed) {
		this.cpuspeed = cpuspeed;
	}

	public String getPublicip() {
		return publicip;
	}

	public void setPublicip(String publicip) {
		this.publicip = publicip;
	}

	public String getNetmac() {
		return netmac;
	}

	public void setNetmac(String netmac) {
		this.netmac = netmac;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	@JSON(name="ostype")
	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	@JSON(name="powerstate")
	public String getPowerstate() {
		return powerstate;
	}

	public void setPowerstate(String powerstate) {
		this.powerstate = powerstate;
	}

	@JSON(name="osip")
	public String getOsip() {
		return osip;
	}

	public void setOsip(String osip) {
		this.osip = osip;
	}

	@JSON(name="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JSON(name="errorcode")
	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	@JSON(name="errordesc")
	public String getErrordesc() {
		return errordesc;
	}

	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}

	@JSON(name="jobid")
	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	@JSON(name="result")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getVlanId() {
		return vlanId;
	}

	public void setVlanId(Long vlanId) {
		this.vlanId = vlanId;
	}

	public String getReqstate() {
		return reqstate;
	}

	public void setReqstate(String reqstate) {
		this.reqstate = reqstate;
	}

}
