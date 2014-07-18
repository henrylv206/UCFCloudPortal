package com.skycloud.management.portal.task.vdc.service.bo;

import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;

public class RespInfosBO {

	private String res_code = "";// 资源ID
	private String error_code = "";// 错误CODE
	private String error_code_desc = "";// 错误描述
	private String resp_parameter = "";// 返回参数，调用api所返回的参数
	private AsyncJobVDCPO asyncJobPO;
	private int state;//需要更新到instance的状态
	private int iriState;//关系表中的状态
	
	public String getRes_code() {
		return res_code;
	}
	public void setRes_code(String res_code) {
		this.res_code = res_code;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_code_desc() {
		return error_code_desc;
	}
	public void setError_code_desc(String error_code_desc) {
		this.error_code_desc = error_code_desc;
	}
	public String getResp_parameter() {
		return resp_parameter;
	}
	public void setResp_parameter(String resp_parameter) {
		this.resp_parameter = resp_parameter;
	}
	public AsyncJobVDCPO getAsyncJobPO() {
		return asyncJobPO;
	}
	public void setAsyncJobPO(AsyncJobVDCPO asyncJobPO) {
		this.asyncJobPO = asyncJobPO;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getIriState() {
		return iriState;
	}
	public void setIriState(int iriState) {
		this.iriState = iriState;
	}
	
}
