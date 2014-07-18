package com.skycloud.management.portal.front.resources.service;

/**
 * H3C网络service接口
 * 
 * @author jiaoyz
 */
public interface IH3CService {

	/**
	 * 设置静态地址转换
	 * 
	 * @param publicIp
	 *            公网ip
	 * @param privateIp
	 *            私网ip
	 * @param userId 用户id
	 * @throws Exception
	 */
	public void setStaticNAT(String publicIp, String privateIp, int userId) throws Exception;

	/**
	 * 删除静态地址转换
	 * 
	 * @param publicIp
	 *            公网ip
	 * @param privateIp
	 *            私网ip
	 * @param userId 用户id
	 * @throws Exception
	 */
	public void delStaticNAT(String publicIp, String privateIp, int userId) throws Exception;

	/**
   * 在路由器上创建路由
   * @param destIp 目标ip
   * @param mask 目标ip掩码
   * @param jumpTo 下一跳ip
   * @param userId 用户id
   * @throws Exception
   */
  public void createRouterRouting(String destIp, String mask, String jumpTo, int userId) throws Exception;

  /**
   * 删除路由器上的路由
   * @param destIp 目标ip
   * @param mask 目标ip掩码
   * @param jumpTo 下一跳ip
   * @param userId 用户id
   * @throws Exception
   */
  public void deleteRouterRouting(String destIp, String mask, String jumpTo, int userId) throws Exception;
}
