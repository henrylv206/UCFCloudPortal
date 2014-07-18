package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists all supported OS types for this cloud.
 * @author hefk
 */
public class EOsType extends QueryCommand {

  public static final String COMMAND = "listOsTypes";
  public static final String ID = "id";

  private long id;
  private long oscategoryid;
  private String description;

  public EOsType() {
    super(COMMAND);
  }

	public EOsType(long id, long oscategoryid, String description) {
		super(COMMAND);
		this.id = id;
		this.oscategoryid = oscategoryid;
		this.description = description;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}
	
	public long getOscategoryid() {
		return oscategoryid;
	}
	
	public void setOscategoryid(long oscategoryid) {
		this.oscategoryid = oscategoryid;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
 
}
