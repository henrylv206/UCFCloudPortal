package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.Command;
import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class CreateNetwork extends Command {
	public static final String COMMAND = "createNetwork";
	public static final String DISPLAY_TEXT = "displaytext";
	public static final String NETWORK_NAME = "name";
	public static final String NETWORK_OFFERING_ID = "networkofferingid";
	public static final String ZONE_ID = "zoneid";
	public static final String ACCOUNT = "account";
	public static final String DOMAIN_ID = "domainid";
	public static final String START_IP = "startip";
	public static final String END_IP = "endip";
	public static final String GATEWAY = "gateway";
	public static final String NETMASK = "netmask";
	public static final String IS_DEFAULT = "isdefault";
	public static final String IS_SHARED = "isshared";
	public static final String NETWORK_DOMAIN = "networkdomain";
	public static final String VLAN = "vlan";
	
	
	private String displayText;
	private String networkName;
	private long networkOfferingId;
	private long zoneId;
	private String account;
	private long domainId;
	private String startIp;
	private String endIp;
	private String gateway;
	private String netmask;
	private boolean isDefault;
	private boolean isShared;
	private String networkDomain;
	private long vlan;
	/*
	displaytext	the display text of the network	TRUE
	name	the name of the network	TRUE
	networkofferingid	the network offering id	TRUE
	zoneid	the Zone ID for the network	TRUE
	account	account who will own the network	FALSE
	domainid	domain ID of the account owning a network	FALSE
	startip	the beginning IP address in the network IP range	FALSE
	endip	the ending IP address in the network IP range. If not specified, will be defaulted to startIP	FALSE
	gateway	the gateway of the network	FALSE
	netmask	the netmask of the network	FALSE
	isdefault	true if network is default, false otherwise	FALSE
	isshared	true is network is shared across accounts in the Zone	FALSE
	networkdomain	network domain	FALSE
	vlan	the ID or VID of the network	FALSE
	*/
	/*
	command=createNetwork
	isDefault=true
	name=vlan_1
	displayText=vlan_1
	networkOfferingId=7
	zoneId=3
	vlan=71
	domainId=3
	account=asia_admin
	gateway=192.168.11.1
	netmask=255.255.255.0
	startip=192.168.11.10
	endip=192.168.11.240
	 */
	public CreateNetwork(String displayText, String networkName, long networkOfferingId, long zoneId,
			String account, long domainId, String startIp, String endIp, String gateway, String netmask,
			long vlan){
		super(COMMAND);
		this.setDisplayText(displayText);
		this.setNetworkName(networkName);
		this.setNetworkOfferingId(networkOfferingId);
		this.setZoneId(zoneId);
		this.setAccount(account);
		this.setDomainId(domainId);
		this.setStartIp(startIp);
		this.setEndIp(endIp);
		this.setGateway(gateway);
		this.setNetmask(netmask);
		this.setVlan(vlan);
		this.setDefault(true);
		this.setShared(false);
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
		this.setParameter(DISPLAY_TEXT, displayText);
	}

	public long getNetworkOfferingId() {
		return networkOfferingId;
	}

	public void setNetworkOfferingId(long networkOfferingId) {
		this.networkOfferingId = networkOfferingId;
		this.setParameter(NETWORK_OFFERING_ID, networkOfferingId);
	}

	public long getZoneId() {
		return zoneId;
	}

	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONE_ID, zoneId);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
		this.setParameter(ACCOUNT, account);
	}

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
		this.setParameter(DOMAIN_ID, domainId);
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
		this.setParameter(START_IP, startIp);
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
		this.setParameter(END_IP, endIp);
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
		this.setParameter(GATEWAY, gateway);
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
		this.setParameter(NETMASK, netmask);
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
		this.setParameter(IS_DEFAULT, isDefault);
	}

	public boolean isShared() {
		return isShared;
	}

	public void setShared(boolean isShared) {
		this.isShared = isShared;
		this.setParameter(IS_SHARED, isShared);
	}

	public String getNetworkDomain() {
		return networkDomain;
	}

	public void setNetworkDomain(String networkDomain) {
		this.networkDomain = networkDomain;
		this.setParameter(NETWORK_DOMAIN, networkDomain);
	}

	public long getVlan() {
		return vlan;
	}

	public void setVlan(long vlan) {
		this.vlan = vlan;
		this.setParameter(VLAN, vlan);
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
		this.setParameter(NETWORK_NAME, networkName);
	}
}
