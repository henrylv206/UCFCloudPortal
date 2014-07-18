package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists all VLAN IP ranges
 * @author hefk
 */
public class EVlanIpRange extends QueryCommand {

  public static String COMMAND = "listVlanIpRanges";
  public static final String ID = "id";
  public static final String ZONEID = "zoneid";
  public static final String NETWORKID = "networkid";


  private long id;
  private String vlan;
  private String startip;
  private String endip;
  private String gateway;
  private String netmask;
  private String networkid;
  private String zoneid;
  private String forvirtualnetwork;
  private String account;
  private String domainid;


  public EVlanIpRange() {
    super(COMMAND);
  }

	public EVlanIpRange(long id, String vlan, String startip,
			String endip, String gateway, String netmask, String networkid,
			String zoneid, String forvirtualnetwork) {
		super(COMMAND);
		setId(id);
		this.vlan = vlan;
		this.startip = startip;
		this.endip = endip;
		this.gateway = gateway;
		this.netmask = netmask;
		this.networkid = networkid;
		this.zoneid = zoneid;
		this.forvirtualnetwork = forvirtualnetwork;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String getStartip() {
		return startip;
	}

	public void setStartip(String startip) {
		this.startip = startip;
	}

	public String getEndip() {
		return endip;
	}

	public void setEndip(String endip) {
		this.endip = endip;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getNetworkid() {
		return networkid;
	}

	public void setNetworkid(String networkid) {
		this.setParameter(NETWORKID, networkid);
		this.networkid = networkid;
	}

	public String getZoneid() {
		return zoneid;
	}

	public void setZoneid(String zoneid) {
		this.setParameter(ZONEID, zoneid);
		this.zoneid = zoneid;
	}

	public String getForvirtualnetwork() {
		return forvirtualnetwork;
	}

	public void setForvirtualnetwork(String forvirtualnetwork) {
		this.forvirtualnetwork = forvirtualnetwork;
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



}
