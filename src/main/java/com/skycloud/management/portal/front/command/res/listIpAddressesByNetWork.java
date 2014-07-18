package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class listIpAddressesByNetWork extends QueryCommand{

	  public static String COMMAND = "listIpAddressesByNetWork";
 	  public static final String NETWORKID = "networkId";


	  private long networkId;
	  private String ipaddress;
	  private String allocated;

	  public listIpAddressesByNetWork() {
	    super(COMMAND);
	  }

        public long getNetworkId() {
        	return networkId;
        }


        public void setNetworkid(long networkId) {
        	this.networkId = networkId;
        	this.setParameter(NETWORKID, networkId);
        }


        public String getIpaddress() {
        	return ipaddress;
        }


        public void setIpaddress(String ipaddress) {
        	this.ipaddress = ipaddress;
        }


        public String getAllocated() {
        	return allocated;
        }


        public void setAllocated(String allocated) {
        	this.allocated = allocated;
        }


}
