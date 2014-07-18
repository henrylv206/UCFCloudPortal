package com.skycloud.tezz.commons;

/**
 * <p>
 * Project: 浦软汇智 云服务项目 子系统 SaaS监控系统
 * </p>
 * <p>
 *  工具类
 * </p>
 * <p>
 * 本类功能：规范定义了页面信息对象并
 * </p>
 * <p>
 * JDK version used: jdk1.6.0 java compiler jdk1.5.XX
 * </p>
 * 
 * @version 1
 * @author dufc
 *         <p>
 *         时间：2011-10-27
 *         </p>
 *         <p>
 *         修改记录:
 *         </p>
 */
public class Pagination {
	private int page = 1;
	private int pageSize = 2;
	private int prePage = 1;
	private int nextPage = 2;
	private Long lastPage = Long.MAX_VALUE;
	private int startRow = 1;
	private int endRow = startRow + pageSize;
	
	
	
	public Pagination() {
		super();
	}
	public Pagination(int page, int pageSize, int prePage, int nextPage,
			Long lastPage, int startRow, int endRow) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.prePage = prePage;
		this.nextPage = nextPage;
		this.lastPage = lastPage;
		this.startRow = startRow;
		this.endRow = endRow;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		if(page<1) page=1;
		this.page = page;
		prePage = page==1?1:page-1;
		nextPage = (int) (page>=lastPage?lastPage:page+1);
		startRow = (page-1) * pageSize + 1;
		endRow = startRow + pageSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		if(pageSize<3) pageSize=3;
		this.pageSize = pageSize;
		this.setPage(this.page);
	}
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public Long getLastPage() {
		return lastPage;
	}
	public void setLastPage(Long lastPage2) {
		if(lastPage2<1) lastPage2=1l;
		this.lastPage = lastPage2;
		this.setPage(this.page);
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
	//	@Override
	//	public String toString() {
	//		int start = ((page-1) / 10) * 10 + 1;
	//		
	//	}
}	
