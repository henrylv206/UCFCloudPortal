package com.skycloud.management.portal.front.resources.enumtype;

public enum InstanceState {
	Apply(1), Running(2), ApplyInProcess(3), Destroyed(4), Stopped(5), Operating(
			6), CreateFailed(7), Error(-1);
	private int value = -1;

	InstanceState(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	public static InstanceState getByValue(int value) {
		for (InstanceState type : InstanceState.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}
}
