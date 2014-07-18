package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;


public class ListDomains extends QueryCommand {
	public static final String COMMAND = "listDomains";
	
	public static final String ID = "id";
	public static final String KEYWORD = "keyword";
	public static final String LEVEL = "level";
	public static final String DOMAIN_NAME = "name";
	/*
	 id List domain by domain ID. false
	 keyword List by keyword false
	 level List domains by domain level.false 
	 name List domain by domain name.false 
	 page false
	 pagesize
	 */
	private long id;
	private String keyword;
	private int level;
	private String domainName;
	
	public ListDomains(){
		super(COMMAND);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
		this.setParameter(KEYWORD, keyword);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		this.setParameter(LEVEL, level);
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
		this.setParameter(DOMAIN_NAME, domainName);
	}
}
