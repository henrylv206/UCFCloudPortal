package com.skycloud.management.portal.task.vdc.enumtype;

public enum OrderType {
	/*
	 * 1：新申请
	 */
	APPLY(1),
	/*
	 * 2：修改申请
	 */
	MODIFY(2),
	/*
	 * 3：删除申请
	 */
	DELETE(3),
	/*
	 * -1 未知状态
	 */
	ILLEGAL(-1);

	private int value = 0;

	private OrderType(int value) {
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
	public static OrderType valueOf(int v) {
		for (OrderType m : OrderType.values()) {
			if (m.getValue() == v) {
				return m;
			}
		}
		return OrderType.ILLEGAL;
	}
}
