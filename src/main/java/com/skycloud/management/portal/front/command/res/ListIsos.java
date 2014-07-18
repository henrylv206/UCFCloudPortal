package com.skycloud.management.portal.front.command.res;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class ListIsos extends BaseCommandPo {

	private static final String COMMAND = "listIsos";
	
	private static final String ACCOUNT = "account";
	
	private static final String BOOTABLE = "bootable";
	
	private static final String DOMAINID = "domainid";
	
	private static final String HYPERVISOR = "hypervisor";
	
	private static final String ID = "id";
	
	private static final String ISOFILTER = "isofilter";
	
	private static final String ISPUBLC = "ispublic";
	
	private static final String ISREADY = "isready";
	
	private static final String KEYWORD = "keyword";
	
	private static final String NAME = "name";
	
	private static final String PAGE = "page";
	
	private static final String PAGESIZE = "pagesize";
	
	private static final String ZONEID = "zoneid";
	
	private String account;
	
	private String bootable;
	
	private String domainid;
	
	private String hypervisor;
	
	private int id;
	
	private String isofilter;
	
	private boolean ispublic;
	
	private boolean isready;
	
	private List<String> keyword;
	
	private String name;
	
	private int page;
	
	private int pagesize;
	
	private int zoneid;
	
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
		this.setParameter(account, account);
	}

	public String getBootable() {
		return bootable;
	}

	public void setBootable(String bootable) {
		this.bootable = bootable;
		this.setParameter(BOOTABLE, bootable);
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
		this.setParameter(DOMAINID, domainid);
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
		this.setParameter(HYPERVISOR, hypervisor);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public String getIsofilter() {
		return isofilter;
	}

	public void setIsofilter(String isofilter) {
		this.isofilter = isofilter;
		this.setParameter(ISOFILTER, isofilter);
	}

	public boolean isIspublic() {
		return ispublic;
	}

	public void setIspublic(boolean ispublic) {
		this.ispublic = ispublic;
		this.setParameter(ISPUBLC, ispublic);
	}

	public boolean isIsready() {
		return isready;
	}

	public void setIsready(boolean isready) {
		this.isready = isready;
		this.setParameter(ISREADY, isready);
	}

	public List<String> getKeyword() {
		return keyword;
	}

	public void setKeyword(List<String> keyword) {
		this.keyword = keyword;
		this.setParameter(KEYWORD, keyword);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		this.setParameter(PAGE, page);
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
		this.setParameter(PAGESIZE, pagesize);
	}

	public int getZoneid() {
		return zoneid;
	}

	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
		this.setParameter(ZONEID, zoneid);
	}

	public ListIsos() {
		super(COMMAND);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		return super.fromPoToJsonStr(po);
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		// TODO Auto-generated method stub
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new RuntimeException("Exceucte [ListIsos] method: fromJsonToOperatePo：paramter is null");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			ListIsos po = (ListIsos)JsonUtil.getObject4JsonString(tempJson, ListIsos.class);
			return po;
		}catch(Exception e){
			logger.error("Execute [ListIsos] method :fromJsonToOperatePo Exception：",e);
			throw new RuntimeException("Execute [ListIsos] method :fromJsonToOperatePo Exception："+e.getMessage());
		}
	}

}
