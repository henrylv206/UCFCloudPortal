package com.skycloud.management.portal.admin.customer.entity;

import java.sql.Timestamp;

public class CTBRI_TCompanyFile {
	private int id;
	private int comId;
	private int fileType;
	private String filePath;
	private String fileName;
	private String fileDesc;
	private Timestamp uploadDt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getComId() {
		return comId;
	}
	public void setComId(int comId) {
		this.comId = comId;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	public Timestamp getUploadDt() {
		return uploadDt;
	}
	public void setUploadDt(Timestamp uploadDt) {
		this.uploadDt = uploadDt;
	}
	

}
