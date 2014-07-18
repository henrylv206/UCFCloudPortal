package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * Lists all supported OS categories for this cloud
 * @author hefk
 */
public class EOsCategory extends QueryCommand {

  public static final String COMMAND = "listOsCategories";
  public static final String ID = "id";
  
  private long id;
  private String name;

  public EOsCategory() {
    super(COMMAND);
  }
	
	public EOsCategory(long id, String name) {
		super(COMMAND);
		this.id = id;
		this.name = name;
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

}
