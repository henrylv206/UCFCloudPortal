package com.skycloud.tezz.model;

public class Users {
	private String year;
	private String month;
	private String day;
	private Integer usersnum;
	private Integer lastusersnum;
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Users(String year, String month, String day, Integer usersnum,
			Integer lastusersnum) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.usersnum = usersnum;
		this.lastusersnum = lastusersnum;
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
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Integer getUsersnum() {
		return usersnum;
	}
	public void setUsersnum(Integer usersnum) {
		this.usersnum = usersnum;
	}
	public Integer getLastusersnum() {
		return lastusersnum;
	}
	public void setLastusersnum(Integer lastusersnum) {
		this.lastusersnum = lastusersnum;
	}
	
}
