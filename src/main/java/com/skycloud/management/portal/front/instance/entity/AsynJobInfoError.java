package com.skycloud.management.portal.front.instance.entity;

import java.sql.Timestamp;
import java.util.Date;

public class AsynJobInfoError {

	private int ID;
	
	private int ASY_ID;
	
	private String DESCRIPTION;

	private Date CREATE_DT;
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getASY_ID() {
		return ASY_ID;
	}

	public void setASY_ID(int aSY_ID) {
		ASY_ID = aSY_ID;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public Date getCREATE_DT() {
		return CREATE_DT;
	}

	public void setCREATE_DT(Date cREATE_DT) {
		CREATE_DT = cREATE_DT;
	}

	
	
	
}
