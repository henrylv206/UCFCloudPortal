package com.skycloud.management.portal.task.vdc.enumtype;

public enum SnapshotState {
	/*
	 * 0：可用
	 */
	AVAILABLE(0),
	/*
	 * 1：不可用
	 */
	NOAVAILABLE(1),
	/**
	 * 2：已删除
	 */
	DELETED(2),
	
	/*
	 * -1 非法状态
	 */
	ILLEGAL(-1);

	private int value = 0;

	private SnapshotState(int value) {
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
	public static SnapshotState valueOf(int v) {
		for (SnapshotState m : SnapshotState.values()) {
			if (m.getValue() == v) {
				return m;
			}
		}
		return SnapshotState.ILLEGAL;
	}
}
