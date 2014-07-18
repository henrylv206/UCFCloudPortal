package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists clusters.
 * @author hefk
 */
public class ECluster extends QueryCommand {

  public static final String COMMAND = "listClusters";
  public static final String ID = "id";
  public static final String HYPERVISORTYPE = "hypervisor";

  private long id;
  private String name;
  private long podid;
  private String podname;
  private long zoneid;
  private String zonename;
  private String hypervisortype;
  private String clustertype;
  private String allocationstate;
  

  public ECluster() {
    super(COMMAND);
  }

	public ECluster(long id, String name, long podid, String podname,
			long zoneid, String zonename, String hypervisortype,
			String clustertype, String allocationstate) {
		super(COMMAND);
		setId(id);
		this.name = name;
		this.podid = podid;
		this.podname = podname;
		this.zoneid = zoneid;
		this.zonename = zonename;
		this.hypervisortype = hypervisortype;
		this.clustertype = clustertype;
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
	
	
	public long getPodid() {
		return podid;
	}
	
	
	public void setPodid(long podid) {
		this.podid = podid;
	}
	
	
	public String getPodname() {
		return podname;
	}
	
	
	public void setPodname(String podname) {
		this.podname = podname;
	}
	
	
	public long getZoneid() {
		return zoneid;
	}
	
	
	public void setZoneid(long zoneid) {
		this.zoneid = zoneid;
	}
	
	
	public String getZonename() {
		return zonename;
	}
	
	
	public void setZonename(String zonename) {
		this.zonename = zonename;
	}
	
	
	public String getHypervisortype() {
		return hypervisortype;
	}
	
	
	public void setHypervisortype(String hypervisortype) {
		this.hypervisortype = hypervisortype;
	    this.setParameter(HYPERVISORTYPE, hypervisortype);
	}
	
	
	public String getClustertype() {
		return clustertype;
	}
	
	
	public void setClustertype(String clustertype) {
		this.clustertype = clustertype;
	}
	
	
	public String getAllocationstate() {
		return allocationstate;
	}
	
	
	public void setAllocationstate(String allocationstate) {
		this.allocationstate = allocationstate;
	}



}
