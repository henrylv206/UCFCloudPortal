package com.skycloud.management.portal.front.resources.enumtype;

public enum MenuType {
	/**
	 * 资源模板管理
	 */
	RESOURCE_TEMPLATE_DEF(1),
	/**
	 * 虚拟机资源模板
	 */
	TEMPLATE_VM(2),
	/**
	 * 物理机资源模板
	 */
	TEMPLATE_PM(3),
	/**
	 * 小型机资源模板
	 */
	TEMPLATE_MINICOMPUTER(4),
	/**
	 * 虚拟硬盘资源模板
	 */
	TEMPLATE_STORAGE(5),
	/**
	 * 虚拟机备份资源模板
	 */
	TEMPLATE_BACKUP(6),
	/**
	 * 负载均衡资源模板
	 */
	TEMPLATE_LOADBALANCED(8),
	/**
	 * 防火墙资源模板
	 */
	TEMPLATE_FIREWALL(9),
	/**
	 * 弹性公网IP资源模板
	 */
	TEMPLATE_PUBLICNETWORKIP(10),
	/**
	 * 公网带宽资源模板
	 */
	TEMPLATE_BANDWIDTH(11),
	/**
	 * 云监控资源模板
	 */
	TEMPLATE_MONITOR(12),
	/**
	 * 服务模板管理
	 */
	BUSINESS_MANAGEMENT(13),
	/**
	 * 服务模板定义
	 */
	PRODUCT_DEFINE(14),
	/**
	 * 服务模板审核
	 */
	PRODUCT_ADUIT(15),
	/**
	 * 服务模板发布
	 */
	PRODUCT_RELEASE(16),
	/**
	 * 弹性公网IP服务模板
	 */
	TEMPLATE_PUBLICNETWORKIP_PRODUCT(77);

	private int value;

	MenuType(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	public static MenuType getByValue(int value) {
		for (MenuType type : MenuType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}

}
