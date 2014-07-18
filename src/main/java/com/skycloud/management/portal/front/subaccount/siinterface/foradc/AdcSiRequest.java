package com.skycloud.management.portal.front.subaccount.siinterface.foradc;

public class AdcSiRequest {
	private String BizCode;
	private String TransID;
	private Integer ActionCode;
	private String TimeStamp;
	private String SIAppID;
	private Integer TestFlag;
	private Integer Dealkind;
	private String Version;
	private Integer Priority;
	private String SvcCont;

	public String getTransID() {
		return TransID;
	}

	public void setTransID(String transID) {
		TransID = transID;
	}

	public Integer getActionCode() {
		return ActionCode;
	}

	public void setActionCode(Integer actionCode) {
		ActionCode = actionCode;
	}

	public String getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

	public String getSIAppID() {
		return SIAppID;
	}

	public void setSIAppID(String sIAppID) {
		SIAppID = sIAppID;
	}

	public Integer getTestFlag() {
		return TestFlag;
	}

	public void setTestFlag(Integer testFlag) {
		TestFlag = testFlag;
	}

	public Integer getDealkind() {
		return Dealkind;
	}

	public void setDealkind(Integer dealkind) {
		Dealkind = dealkind;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public Integer getPriority() {
		return Priority;
	}

	public void setPriority(Integer priority) {
		Priority = priority;
	}

	public String getSvcCont() {
		return SvcCont;
	}

	public void setSvcCont(String svcCont) {
		SvcCont = svcCont;
	}

	public String getBizCode() {
		return BizCode;
	}

	public void setBizCode(String bizCode) {
		BizCode = bizCode;
	}
}
