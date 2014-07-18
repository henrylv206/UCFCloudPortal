package com.skycloud.management.portal.front.command.impl;

public abstract class QueryCommand extends Command {
	public static String PAGE = "page";
	public static String PAGESIZE = "pagesize";
	
	private int page;
	private int pagesize;
	
	public QueryCommand(String name) {
		super(name);
	}
	
	public QueryCommand(String name, int page, int pagesize){
		super(name);
		this.setPage(page);
		this.setPagesize(pagesize);
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
	
}
