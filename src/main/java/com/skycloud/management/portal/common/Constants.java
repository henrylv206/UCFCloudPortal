package com.skycloud.management.portal.common;

import java.text.SimpleDateFormat;

/**
 * 常量类
 * 
 * @author jiaoyz
 */
public class Constants {

	// 日期格式化
	public static enum SDF {
		YYYYMMDDHHMMSS(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")), YYYYMMDD(new SimpleDateFormat(
				"yyyy-MM-dd")), HHMMSS(new SimpleDateFormat("HH:mm:ss"));
		private final SimpleDateFormat format;

		private SDF(SimpleDateFormat format) {
			this.format = format;
		}

		public SimpleDateFormat getValue() {
			return format;
		}
	}

	// 统一状态值
	public static enum STATUS_COMMONS {
		ON(1), OFF(0), TRUE(1), FALSE(0), IGNORE(-1);
		private final int flag;

		private STATUS_COMMONS(int flag) {
			this.flag = flag;
		}

		public int getValue() {
			return flag;
		}
	}

	// 中移动规范
	public static enum CMCC_CRITERION_CODE {
		SCOPE_PRIVATE("CPC"), // 私有云
		SCOPE_PUBLIC("CIDC"), // 公众云
		TEMPLATE_ID_PREFIX_PRIVATE(SCOPE_PRIVATE.getValue() + "-T"), // 私有云模版ID前缀
		TEMPLATE_ID_PREFIX_PUBLIC(SCOPE_PUBLIC.getValue() + "-T"), // 公众云模版ID前缀
		RESPONSE_CODE_SUCCESS("00000000"), // 成功
		RESPONSE_CODE_T_ID_INVALID("00000001"), // 资源模板编码不合法
		RESPONSE_CODE_T_ID_NOTEXIST("00000002"), // 资源模板编码不存在
		RESPONSE_CODE_VM_ID_INVALID("00000003"), // 虚拟机编码不合法
		RESPONSE_CODE_VM_ID_NOTEXIST("00000004"), // 虚拟机编码不存在
		RESPONSE_CODE_VMBK_ID_INVALID("00000005"), // 虚拟机备份编码不合法
		RESPONSE_CODE_VMBK_ID_NOTEXIST("00000006"), // 虚备份备份编码不存在
		RESPONSE_CODE_USER_ID_INVALID("00000007"), // 用户编码不合法
		RESPONSE_CODE_USER_ID_NOTEXIST("00000008"), // 用户编码不存在
		RESPONSE_CODE_ZONE_ID_INVALID("00000009"), // 资源分区编码不合法
		RESPONSE_CODE_ZONE_ID_NOTEXIST("00000010"), // 资源分区编码不存在
		RESPONSE_CODE_DF_ID_INVALID("00000011"), // 分布式文件存储的编码不合法
		RESPONSE_CODE_DF_ID_NOTEXIST("00000012"), // 分布式文件存储的编码不存在
		RESPONSE_CODE_BS_ID_INVALID("00000013"), // 块存储的编码不合法
		RESPONSE_CODE_BS_ID_NOTEXIST("00000014"), // 块存储的编码不存在
		RESPONSE_CODE_IP_INVALID("00000015"), // IP地址不合法
		RESPONSE_CODE_IP_NOTEXIST("00000016"), // IP地址不存在
		RESPONSE_CODE_FW_ID_INVALID("00000017"), // 虚拟机防火墙编码不合法
		RESPONSE_CODE_FW_ID_NOTEXIST("00000018"), // 虚拟机防火墙编码不存在
		RESPONSE_CODE_LB_ID_INVALID("00000019"), // 负载均衡器编码不合法
		RESPONSE_CODE_LB_ID_NOTEXIST("00000020"), // 负载均衡器编码不存在
		RESPONSE_CODE_SRV_ID_INVALID("00000021"), // X86物理机编码不合法
		RESPONSE_CODE_SRV_ID_NOTEXIST("00000022"), // X86物理机编码不存在
		RESPONSE_CODE_MC_ID_INVALID("00000023"), // 小型机分区编码不合法
		RESPONSE_CODE_MC_ID_NOTEXIST("00000024"), // 小型机分区编码不存在
		RESPONSE_CODE_SRV_LACKING("00000101"), // X86物理机资源不足
		RESPONSE_CODE_VM_LACKING("00000102"), // 虚拟机资源不足
		RESPONSE_CODE_MC_LACKING("00000103"), // 小型机分区资源不足
		RESPONSE_CODE_DF_LACKING("00000104"), // 分布式文件存储不足
		RESPONSE_CODE_BS_LACKING("00000105"), // FC-SAN弹性块存储不足
		RESPONSE_CODE_IP_LACKING("00000106"), // IP地址不足
		RESPONSE_CODE_BW_LACKING("00000107"), // 带宽不足
		RESPONSE_CODE_FW_LACKING("00000108"), // 防火墙不足
		RESPONSE_CODE_LB_LACKING("00000109"), // 虚拟负载均衡不足
		RESPONSE_CODE_BK_LACKING("00000110"), // 数据备份不足
		RESPONSE_CODE_OTHER("99999999"); // 其他
		private final String code;

		private CMCC_CRITERION_CODE(String code) {
			this.code = code;
		}

		public String getValue() {
			return code;
		}
	}
}
