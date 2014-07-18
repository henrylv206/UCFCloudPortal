package com.skycloud.management.portal.admin.productitem.entity;

public class ProductItemRelationFrontBO {
	private int id;
	private int productId; // 产品id
	private int productItemId; //目录id
	private int state; // 状态

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductItemId() {
		return productItemId;
	}

	public void setProductItemId(int productItemId) {
		this.productItemId = productItemId;
	}


}
