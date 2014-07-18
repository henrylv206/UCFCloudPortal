package com.skycloud.management.portal.front.instance.entity;

import java.util.Date;

public class Iri {

	private int ID = 0;
	
	private int VM_INSTANCE_INFO_ID;
	
	private int DISK_INSTANCE_INFO_ID;
	
	private Date CREATE_DT;
	
	private int CREATE_USER_ID;
	
	private int STATE;
	
	private int E_VM_INSTANCE_INFO_ID;
	
	private int  E_DISK_INSTANCE_INFO_ID;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getVM_INSTANCE_INFO_ID() {
		return VM_INSTANCE_INFO_ID;
	}

	public void setVM_INSTANCE_INFO_ID(int vM_INSTANCE_INFO_ID) {
		VM_INSTANCE_INFO_ID = vM_INSTANCE_INFO_ID;
	}

	public int getDISK_INSTANCE_INFO_ID() {
		return DISK_INSTANCE_INFO_ID;
	}

	public void setDISK_INSTANCE_INFO_ID(int dISK_INSTANCE_INFO_ID) {
		DISK_INSTANCE_INFO_ID = dISK_INSTANCE_INFO_ID;
	}

	public Date getCREATE_DT() {
		return CREATE_DT;
	}

	public void setCREATE_DT(Date cREATE_DT) {
		CREATE_DT = cREATE_DT;
	}

	public int getCREATE_USER_ID() {
		return CREATE_USER_ID;
	}

	public void setCREATE_USER_ID(int cREATE_USER_ID) {
		CREATE_USER_ID = cREATE_USER_ID;
	}

	public int getSTATE() {
		return STATE;
	}

	public void setSTATE(int sTATE) {
		STATE = sTATE;
	}

	public int getE_VM_INSTANCE_INFO_ID() {
		return E_VM_INSTANCE_INFO_ID;
	}

	public void setE_VM_INSTANCE_INFO_ID(int e_VM_INSTANCE_INFO_ID) {
		E_VM_INSTANCE_INFO_ID = e_VM_INSTANCE_INFO_ID;
	}

	public int getE_DISK_INSTANCE_INFO_ID() {
		return E_DISK_INSTANCE_INFO_ID;
	}

	public void setE_DISK_INSTANCE_INFO_ID(int e_DISK_INSTANCE_INFO_ID) {
		E_DISK_INSTANCE_INFO_ID = e_DISK_INSTANCE_INFO_ID;
	}
	
	
}
