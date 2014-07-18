package com.skycloud.management.portal.front.command.res;

import java.util.List;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists listNetworks.
 *
 * @author hefk
 */
public class ENetwork extends QueryCommand {

	public static final String COMMAND = "listNetworks";

	public static final String ID = "id";

	public static final String ZONEID = "zoneid";

	public static final String STATE = "state"; // Implemented,不选Allocated

	public static final String ISDEFAULT = "isdefault"; // 第一个 选 true

	public static final String TYPE = "type"; // 1,Direct,2.Virtual

	private long id;

	private String name;

	private boolean isdefault;

	private String state;

	private String startip;

	private String endip;

	private String netmask;

	private String gateway;

	private int zoneid;

	private String broadcastdomaintype;

	private String displaytext;

	private int vlan;

	private String displayIpRanges;

	// to fix bug:2286
	private String type;

	private String tags;

	private List<EVlanIpRange> listVlanIpRange;

	private int ipTotal;//ip总数

	private int ipFreeNum;//剩余ip数

	private String account;

	public ENetwork() {
		super(COMMAND);
	}

	public ENetwork(long id, String name, boolean isdefault, String state, String startip, String endip, String netmask, String gateway, int zoneid,
	                String broadcastdomaintype, String displaytext) {
		super(COMMAND);
		setId(id);
		this.name = name;
		this.isdefault = isdefault;
		this.state = state;
		this.startip = startip;
		this.endip = endip;
		this.netmask = netmask;
		this.gateway = gateway;
		setZoneid(zoneid);
		this.broadcastdomaintype = broadcastdomaintype;
		this.displaytext = displaytext;
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

	public boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
		this.setParameter(ISDEFAULT, isdefault);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.setParameter(STATE, state);
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

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public int getZoneid() {
		return zoneid;
	}

	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
		this.setParameter(ZONEID, zoneid);
	}

	public String getBroadcastdomaintype() {
		return broadcastdomaintype;
	}

	public void setBroadcastdomaintype(String broadcastdomaintype) {
		this.broadcastdomaintype = broadcastdomaintype;
	}

	public String getDisplaytext() {
		return displaytext;
	}

	public void setDisplaytext(String displaytext) {
		this.displaytext = displaytext;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.setParameter(TYPE, type);
	}


    public List<EVlanIpRange> getListVlanIpRange() {
    	return listVlanIpRange;
    }


    public void setListVlanIpRange(List<EVlanIpRange> listVlanIpRange) {
    	this.listVlanIpRange = listVlanIpRange;
    }


    public int getVlan() {
    	return vlan;
    }


    public void setVlan(int vlan) {
    	this.vlan = vlan;
    }

	public String getDisplayIpRanges() {
		return displayIpRanges;
	}

	public void setDisplayIpRanges(String displayIpRanges) {
		this.displayIpRanges = displayIpRanges;
	}


    public String getTags() {
    	return tags;
    }


    public void setTags(String tags) {
    	this.tags = tags;
    }


    public int getIpTotal() {
    	return ipTotal;
    }


    public void setIpTotal(int ipTotal) {
    	this.ipTotal = ipTotal;
    }


    public int getIpFreeNum() {
    	return ipFreeNum;
    }


    public void setIpFreeNum(int ipFreeNum) {
    	this.ipFreeNum = ipFreeNum;
    }


    public String getAccount() {
    	return account;
    }


    public void setAccount(String account) {
    	this.account = account;
    }




}
