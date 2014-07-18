package com.skycloud.management.portal.common.utils;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ParameterTag extends TagSupport {


	private String type;
	private String unit;
	private int comment;
	public int doStartTag() throws JspException {
		try {
			ServletContext sc = (ServletContext) pageContext.getServletContext();
			Object params =  sc.getAttribute("parameters");
			JSONArray parametersjson = (JSONArray)params;	
			ParameterManager.setParametersjson(parametersjson);
			String param = "";
			JspWriter out = pageContext.getOut();
			
			if(comment == 0){
				param = ParameterManager.getInstance().getValueForCombox(type);
			}else if(comment == 1){
				param = ParameterManager.getInstance().getVlaueAndCommentForCombox(type);
			}
			if(null != param && !param.isEmpty()){
				JSONArray arr = JSONArray.fromObject(param);
				if(null != arr && !arr.isEmpty()){
					for(int i=0;i<arr.size();i++){
						JSONObject joption = JSONObject.fromObject(arr.get(i));					
						out.print("<option value='" + joption.getString("value")+ "'>" + joption.getString("text")+ unit+ "</option>");
					}
				}				
			}
		} catch (Exception e) {
			throw new JspException("ParameterTag: " + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

}
