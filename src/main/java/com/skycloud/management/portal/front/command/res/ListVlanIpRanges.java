package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class ListVlanIpRanges extends QueryCommand {
	public static final String COMMAND = "listVlanIpRanges";
	public static final String ID = "id";
	public static final String NETWORK = "networkid";
	public static final String ZONEID = "zoneid";
	/*
	 * id List domain by domain ID. false keyword List by keyword false level
	 * List domains by domain level.false name List domain by domain name.false
	 * page false pagesize
	 */
	private long id;
	private long networkId;
	private int zoneId;
	
	public ListVlanIpRanges() {
		super(COMMAND);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(long networkId) {
		this.networkId = networkId;
		this.setParameter(NETWORK, networkId);
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONEID, zoneId);
	}
}
