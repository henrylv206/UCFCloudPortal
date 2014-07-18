package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists storage pools.
 * @author hefk
 */
public class EStoragePool extends QueryCommand {

  public static final String COMMAND = "listStoragePools";
  public static final String ID = "id";
  public static final String STATE = "state";
  public static final String CLUSTERID = "clusterid";
  public static final String ZONEID = "zoneid";
  
  private long id;
  private String name;
  private long clusterid;
  private String clustername;
  private long podid;
  private String podname;
  private long zoneid;
  private String zonename;
  private long jobid;
  private String jobstatus;
  private long disksizeallocated;
  private long disksizetotal;
  private String ipaddress;
  private String tags;
  private String type;
  private String state;

  public EStoragePool() {
    super(COMMAND);
  }

	public EStoragePool( long id, String name, long clusterid,
			String clustername, long podid, String podname, long zoneid,
			String zonename, long jobid, String jobstatus, long disksizeallocated,
			long disksizetotal, String ipaddress, String tags, String type) {
		super(COMMAND);
		setId(id);
		this.name = name;
		this.clusterid = clusterid;
		this.clustername = clustername;
		this.podid = podid;
		this.podname = podname;
		this.zoneid = zoneid;
		this.zonename = zonename;
		this.jobid = jobid;
		this.jobstatus = jobstatus;
		this.disksizeallocated = disksizeallocated;
		this.disksizetotal = disksizetotal;
		this.ipaddress = ipaddress;
		this.tags = tags;
		this.type = type;
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


	public long getClusterid() {
		return clusterid;
	}


	public void setClusterid(long clusterid) {
		this.clusterid = clusterid;
		this.setParameter(CLUSTERID, clusterid);
	}


	public String getClustername() {
		return clustername;
	}


	public void setClustername(String clustername) {
		this.clustername = clustername;
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
		this.setParameter(ZONEID, zoneid);	
	}


	public String getZonename() {
		return zonename;
	}


	public void setZonename(String zonename) {
		this.zonename = zonename;
	}


	public long getJobid() {
		return jobid;
	}


	public void setJobid(long jobid) {
		this.jobid = jobid;
	}


	public String getJobstatus() {
		return jobstatus;
	}


	public void setJobstatus(String jobstatus) {
		this.jobstatus = jobstatus;
	}


	public long getDisksizeallocated() {
		return disksizeallocated;
	}


	public void setDisksizeallocated(long disksizeallocated) {
		this.disksizeallocated = disksizeallocated;
	}


	public long getDisksizetotal() {
		return disksizetotal;
	}


	public void setDisksizetotal(long disksizetotal) {
		this.disksizetotal = disksizetotal;
	}


	public String getIpaddress() {
		return ipaddress;
	}


	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}


	public String getTags() {
		return tags;
	}


	public void setTags(String tags) {
		this.tags = tags;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.setParameter(STATE, state);
	}
	  
	
	
	
 
}
