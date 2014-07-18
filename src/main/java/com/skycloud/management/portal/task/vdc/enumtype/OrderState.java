package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * 订单状态；0：购物车状态；1：申请状态；2：高级用户审核状态；3：管理员审核状态；4：高级管理员审核状态；5：拒绝状态; 13: 已取消
 * 
 * @author lvyg
 * 
 */
public enum OrderState {
	/*
	 * 0：购物车状态
	 */
	SHOPCART(0),
	/*
	 * 1：申请状态
	 */
	APPLY(1),
	/*
	 * 2：高级用户审核状态
	 */
	LEVEL1(2),
	/*
	 * 3：管理员审核状态
	 */
	LEVEL2(3),
	/*
	 * 4：高级管理员审核状态
	 */
	LEVEL3(4),
	/*
	 * 5：拒绝状态
	 */
	REFUSE(5),
	/*
	 * 6: 已作废
	 */
	INVALID(6),
	/*
	 * 7：审核失败 
	 */
	FAILED(7),
	/*
	 * -1 未知状态
	 */
	ILLEGAL(-1);

	private int value = 0;

	private OrderState(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param v
	 * @return
	 */
	public static OrderState valueOf(int v) {
		for (OrderState m : OrderState.values()) {
			if (m.getValue() == v) {
				return m;
			}
		}
		return OrderState.ILLEGAL;
	}
}
