/**
 * 2012-1-18  下午03:52:42  $Id:shixq
 */
package com.skycloud.management.portal.webservice.monitor.service;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.rest.model.BWMonitorInfoReq;
import com.skycloud.management.portal.rest.model.BWMonitorInfoResp;
import com.skycloud.management.portal.rest.model.FirewallMonitorInfoReq;
import com.skycloud.management.portal.rest.model.FirewallMonitorInfoResp;
import com.skycloud.management.portal.rest.model.LBMonitorInfoReq;
import com.skycloud.management.portal.rest.model.LBMonitorInfoResp;
import com.skycloud.management.portal.rest.model.MCMonitorInfoReq;
import com.skycloud.management.portal.rest.model.MCMonitorInfoResp;
import com.skycloud.management.portal.rest.model.PubllcNetworkIPMonitorInfoReq;
import com.skycloud.management.portal.rest.model.PubllcNetworkIPMonitorInfoResp;
import com.skycloud.management.portal.rest.model.VMMonitorInfoReq;
import com.skycloud.management.portal.rest.model.VMMonitorInfoResp;
import com.skycloud.management.portal.rest.model.VolumeMonitorInfoReq;
import com.skycloud.management.portal.rest.model.VolumeMonitorInfoResp;

/**
 * @author shixq
 * @version $Revision$ 下午03:52:42
 */
public interface MonitorService {

  /**
   * 虚拟机监控详细信息
   */
  VMMonitorInfoResp queryVMMonitorInfoService(VMMonitorInfoReq req) throws SCSException;

  /**
   * 小型机监控详细信息
   */
  MCMonitorInfoResp queryMCMonitorInfoService(MCMonitorInfoReq req) throws SCSException;

  /**
   * 块存储监控详细信息
   */
  VolumeMonitorInfoResp queryVolumeMonitorInfoService(VolumeMonitorInfoReq req) throws SCSException;

  /**
   * 负载均衡监控详细信息
   */
  LBMonitorInfoResp queryLBMonitorInfoService(LBMonitorInfoReq req) throws SCSException;

  /**
   * 防火墙监控详细信息
   */
  FirewallMonitorInfoResp queryFirewallMonitorInfoService(FirewallMonitorInfoReq req) throws SCSException;

  /**
   * 公网IP监控详细信息
   */
  PubllcNetworkIPMonitorInfoResp queryPubllcNetworkIPMonitorInfoService(PubllcNetworkIPMonitorInfoReq req) throws SCSException;

  /**
   * 带宽监控详细信息
   */
  BWMonitorInfoResp queryBWMonitorInfoService(BWMonitorInfoReq req) throws SCSException;

}
