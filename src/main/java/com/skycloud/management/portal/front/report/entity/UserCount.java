package com.skycloud.management.portal.front.report.entity;

public class UserCount extends UserInfo {

	private String year;

	private String month;

	private long total;

	private long increase;

	public UserCount() {
		super();
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getIncrease() {
		return increase;
	}

	public void setIncrease(long increase) {
		this.increase = increase;
	}

}
