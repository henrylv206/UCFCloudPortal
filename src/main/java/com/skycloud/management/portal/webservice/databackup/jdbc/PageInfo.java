package com.skycloud.management.portal.webservice.databackup.jdbc;

/**
 * 
  *<dl>
  *<dt>类名：PageInfo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-16  下午03:27:26</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class PageInfo {
	/*** 总记录数 */
	private long totalRecords = 0;
	/** 显示记录开始页数 */
	private int startIndex = 0;
	/** 页面显示记录条数 */
	private int results = 10;
	/*** 排序项 */
	private String sortItem;
	/*** 排序方式 */
	private String sortType;
	public PageInfo(){
		
	}
    /**
     * @param start
     * @param results
     * @param sortItem
     * @param sortType
     */
	public PageInfo(int start,int results,String sortItem,String sortType){
		this.startIndex=start;
		this.results=results;
		this.sortItem=sortItem;
		this.sortType=sortType;
	}
	/**
	 * @param startIndex
	 * @param results
	 */
	public PageInfo(int startIndex,int results){
		this.startIndex=startIndex;
		this.results=results;
	}
	public int getResults() {
		return results;
	}

	public void setResults(int results) {
		this.results = results;
	}

	public String getSortItem() {
		return sortItem;
	}

	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
}
