package com.skycloud.management.portal.front.resources.enumtype;

public enum ServiceType {
	/**
	 * 虚拟机
	 */
	VM(1),
	/**
	 * 虚拟硬盘
	 */
	VDISK(2), 
	/**
	 * 小型机
	 */
	MC(3), 
	/**
	 * 虚拟机备份
	 */
	VMBAK(4), 
	/**
	 * 云监控
	 */
	MON(5), 
	/**
	 * 负载均衡
	 */
	LB(6), 
	/**
	 * 防火墙
	 */
	FW(7), 
	/**
	 * 公网带宽
	 */
	BW(8), 
	/**
	 * 弹性公网IP
	 */
	PUBLICIP(9),
	/**
	 * 物理机
	 */
	PHY(10),
	/**
	 * 对象存储
	 */
	OBS(11), 
	/**
	 * 弹性块存储
	 */
	EBS(12), 
	/**
	 * 多虚拟机
	 */
	MVM(50);
	
	private int value;

	ServiceType(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	public static ServiceType getByValue(int value) {
		for (ServiceType type : ServiceType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}

}
