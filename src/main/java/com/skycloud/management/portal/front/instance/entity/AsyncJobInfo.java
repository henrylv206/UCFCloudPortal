package com.skycloud.management.portal.front.instance.entity;

import java.util.Date;

public class AsyncJobInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6318843580574942744L;

	
	private int ID;
	private int ORDER_ID;
	private int INSTANCE_INFO_ID;	
	private int APPLY_ID;
	private String OPERATION;
	private String PARAMETER;
	private int JOBSTATE;
	private int JOBID;
	private int RESID;
	private Date CREATE_DT;
	private String COMMON;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public int getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(int oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public int getINSTANCE_INFO_ID() {
		return INSTANCE_INFO_ID;
	}
	public void setINSTANCE_INFO_ID(int iNSTANCE_INFO_ID) {
		INSTANCE_INFO_ID = iNSTANCE_INFO_ID;
	}
	public int getAPPLY_ID() {
		return APPLY_ID;
	}
	public void setAPPLY_ID(int aPPLY_ID) {
		APPLY_ID = aPPLY_ID;
	}
	public String getOPERATION() {
		return OPERATION;
	}
	public void setOPERATION(String oPERATION) {
		OPERATION = oPERATION;
	}
	public String getPARAMETER() {
		return PARAMETER;
	}
	public void setPARAMETER(String pARAMETER) {
		PARAMETER = pARAMETER;
	}
	public int getJOBSTATE() {
		return JOBSTATE;
	}
	public void setJOBSTATE(int jOBSTATE) {
		JOBSTATE = jOBSTATE;
	}
	public int getJOBID() {
		return JOBID;
	}
	public void setJOBID(int jOBID) {
		JOBID = jOBID;
	}
	public int getRESID() {
		return RESID;
	}
	public void setRESID(int rESID) {
		RESID = rESID;
	}
	
	public Date getCREATE_DT() {
		return CREATE_DT;
	}
	public void setCREATE_DT(Date cREATE_DT) {
		CREATE_DT = cREATE_DT;
	}
	public String getCOMMON() {
		return COMMON;
	}
	public void setCOMMON(String cOMMON) {
		COMMON = cOMMON;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
