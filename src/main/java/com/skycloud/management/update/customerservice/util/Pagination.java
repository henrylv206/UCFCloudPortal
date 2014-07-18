package com.skycloud.management.update.customerservice.util;

import java.util.List;
import java.util.Map;

/**
 * 分页模型
 * 
 * @author guoguangjun
 * 
 */
public class Pagination<T> {

	/** 保存分页查询的数据集合 */
	private List<T> list;
	/** 总记录数 */
	private int totalRecords;
	/** 第几页 */
	private int pageNo;
	/** 每页显示数量 */
	private int pageSize;
	/** 分页查询条件集合 */
	private Map<String, String> searchParams;

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return (totalRecords + pageSize - 1) / pageSize;
	}

	/**
	 * 首页
	 * 
	 * @return
	 */
	public int getTopPageNo() {
		return 1;
	}

	/**
	 * 上一页
	 * 
	 * @return
	 */
	public int getPreviousPageNo() {
		return pageNo > 1 ? pageNo - 1 : 1;
	}

	/**
	 * 下一页
	 * 
	 * @return
	 */
	public int getNextPageNo() {
		return pageNo == getTotalPages() ? pageNo : pageNo + 1;
	}

	/**
	 * 末页
	 * 
	 * @return
	 */
	public int getBottomPageNo() {
		return getTotalPages();
	}

	// setter and getter
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, String> getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(Map<String, String> searchParams) {
		this.searchParams = searchParams;
	}
	
}
