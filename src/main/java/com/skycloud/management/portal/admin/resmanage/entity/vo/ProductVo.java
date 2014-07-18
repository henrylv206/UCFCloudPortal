package com.skycloud.management.portal.admin.resmanage.entity.vo;

import java.util.ArrayList;
import java.util.List;

public class ProductVo {
	private List<String> ids= new ArrayList<String>();

	public ProductVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductVo(List<String> ids) {
		super();
		this.ids = ids;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}







	
}
