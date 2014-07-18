package com.skycloud.management.portal.admin.template.entity;

public class BaseCloudStackResourceInfo {

	private int QuotaSize;
	
	private String Grade = "高";

	private String BackupNum ="2";

	public int getQuotaSize() {
		return QuotaSize;
	}

	public void setQuotaSize(int quotaSize) {
		QuotaSize = quotaSize;
	}

	public String getGrade() {
		return Grade;
	}

	public void setGrade(String grade) {
		Grade = grade;
	}

	public String getBackupNum() {
		return BackupNum;
	}

	public void setBackupNum(String backupNum) {
		BackupNum = backupNum;
	}
	
	
}
