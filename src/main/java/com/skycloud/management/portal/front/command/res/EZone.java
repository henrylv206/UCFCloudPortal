package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists listZones.
 * 
 * @author hefk
 */
public class EZone extends QueryCommand {

	public static final String COMMAND = "listZones";
	public static final String ID = "id";

	private long id;
	private String name;
	private String dns1;
	private String dns2;
	private String internaldns1;
	private String internaldns2;
	private String vlan;
	private String guestcidraddress;
	private String networktype;
	private String securitygroupsenabled;
	private String allocationstate;

	public EZone() {
		super(COMMAND);
	}

	public EZone(long id, String name, String dns1, String dns2,
			String internaldns1, String internaldns2, String vlan,
			String guestcidraddress, String networktype,
			String securitygroupsenabled, String allocationstate) {
		super(COMMAND);
		setId(id);
		this.name = name;
		this.dns1 = dns1;
		this.dns2 = dns2;
		this.internaldns1 = internaldns1;
		this.internaldns2 = internaldns2;
		this.vlan = vlan;
		this.guestcidraddress = guestcidraddress;
		this.networktype = networktype;
		this.securitygroupsenabled = securitygroupsenabled;
		this.allocationstate = allocationstate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	    this.setParameter(ID, id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns2() {
		return dns2;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public String getInternaldns1() {
		return internaldns1;
	}

	public void setInternaldns1(String internaldns1) {
		this.internaldns1 = internaldns1;
	}

	public String getInternaldns2() {
		return internaldns2;
	}

	public void setInternaldns2(String internaldns2) {
		this.internaldns2 = internaldns2;
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String getGuestcidraddress() {
		return guestcidraddress;
	}

	public void setGuestcidraddress(String guestcidraddress) {
		this.guestcidraddress = guestcidraddress;
	}

	public String getNetworktype() {
		return networktype;
	}

	public void setNetworktype(String networktype) {
		this.networktype = networktype;
	}

	public String getSecuritygroupsenabled() {
		return securitygroupsenabled;
	}

	public void setSecuritygroupsenabled(String securitygroupsenabled) {
		this.securitygroupsenabled = securitygroupsenabled;
	}

	public String getAllocationstate() {
		return allocationstate;
	}

	public void setAllocationstate(String allocationstate) {
		this.allocationstate = allocationstate;
	}

}
