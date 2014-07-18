package com.skycloud.management.portal.webservice.databackup.po;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import com.skycloud.management.portal.webservice.databackup.utils.JacksonUtils;
/**
 * 中软发布虚拟机命令对象实体类
  *<dl>
  *<dt>类名：DbDeployVirtualMachine</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:28:09</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DbDeployVirtualMachine {

	public static final String COMMAND = "deployVirtualMachine";
	
	private String serviceofferingid;
	
	private String ZONEID;
	
	private String templateid;
	
	private int[] networkids = new int[]{};
	
	private String displayname;

	public String getServiceofferingid() {
		return serviceofferingid;
	}

	public void setServiceofferingid(String serviceofferingid) {
		this.serviceofferingid = serviceofferingid;
	}

	

	public static String getCommand() {
		return COMMAND;
	}

	public String getZONEID() {
		return ZONEID;
	}

	public void setZONEID(String zONEID) {
		ZONEID = zONEID;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public int[] getNetworkids() {
		return networkids;
	}

	public void setNetworkids(int[] networkids) {
		this.networkids = networkids;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	
	public String getParameter (DbVirtumalMachinePo vmPo) throws Exception {
		Map<String,Object> resultmap = new HashMap<String,Object> ();
		DbDeployVirtualMachine vm = new DbDeployVirtualMachine ();
		vm.setDisplayname(vmPo.getDisplayname());
		vm.setServiceofferingid(vmPo.getServiceofferingid());
		vm.setTemplateid("");
		vm.setZONEID(vmPo.getZoneid());
		resultmap.put("deployVirtualMachine", vm);
		if(vmPo.getNic() != null){
			Map<String,Object> ip = new IdentityHashMap<String,Object> ();
			int[] networdIds = new int[vmPo.getNic().size()];
			int countNum = 0;
			for(DbNic nic : vmPo.getNic()){
				networdIds[countNum] = Integer.valueOf(nic.getNetworkid());
				ip.put(nic.getIpaddress(),networdIds[countNum]);
				countNum++;
			}
			vm.setNetworkids(networdIds);
			resultmap.put("IP", ip);
		}
		return JacksonUtils.toJson(resultmap);
	}
	public static void main (String arg[])throws Exception{
		DbDeployVirtualMachine dbvm = new DbDeployVirtualMachine ();
		String json = "{\"id\":283,\"name\":\"teslink\",\"displayname\":\"teslink\",\"account\":\"mj\",\"domainid\":1,\"domain\":\"ROOT\",\"created\":\"2011-12-21T17:15:22+0800\",\"state\":\"Running\",\"haenable\":false,\"groupid\":54,\"group\":\"lworkgroup\",\"zoneid\":1,\"zonename\":\"wgzx-1\",\"hostid\":1,\"hostname\":\"xs172d16d210d221\",\"templateid\":209,\"templatename\":\"Win_XP_SP3\",\"templatedisplaytext\":\"Win_XP_SP3\",\"passwordenabled\":false,\"serviceofferingid\":11,\"serviceofferingname\":\"   \",\"cpunumber\":1,\"cpuspeed\":500,\"memory\":512,\"cpuused\":\"0.72%\",\"networkkbsread\":0,\"networkkbswrite\":89,\"guestosid\":58,\"rootdeviceid\":0,\"rootdevicetype\":\"NetworkFilesystem\",\"securitygroup\":[],\"nic\":[{\"id\":355,\"networkid\":219,\"netmask\":\"255.255.255.0\",\"gateway\":\"10.1.1.1\",\"ipaddress\":\"10.1.1.232\",\"isolationuri\":\"vlan://2000\",\"broadcasturi\":\"vlan://2000\",\"traffictype\":\"Guest\",\"type\":\"Virtual\",\"isdefault\":true,\"macaddress\":\"02:00:35:83:00:0b\"}],\"hypervisor\":\"XenServer\",\"harddiskKbsRead\":[{\"hdId\":\"415\",\"name\":\"ROOT-283\",\"rate\":\"0\"}],\"harddiskKbsWrite\":[{\"hdId\":\"415\",\"name\":\"ROOT-283\",\"rate\":\"0\"}],\"memoryInternalFree\":367,\"nicReadKBs\":[{\"nicId\":\"355\",\"ip\":\"10.1.1.232\",\"rate\":\"0\"}],\"nicWriteKBs\":[{\"nicId\":\"355\",\"ip\":\"10.1.1.232\",\"rate\":\"0\"}]}";
		DbVirtumalMachinePo virtualMachine =(DbVirtumalMachinePo)JacksonUtils.fromJson(json, DbVirtumalMachinePo.class) ;
		System.out.println(dbvm.getParameter(virtualMachine));
	}
}
