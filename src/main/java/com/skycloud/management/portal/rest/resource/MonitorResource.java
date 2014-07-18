/**
 * 2012-1-18  下午02:24:09  $ID:shixq
 */
package com.skycloud.management.portal.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.common.utils.BaseService;
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
import com.skycloud.management.portal.webservice.monitor.service.MonitorService;

/**
 * @author shixq
 * @version $Revision$ 下午02:24:09
 */
@Component
@Path("/monitor")
public class MonitorResource extends BaseService {

  private static Log log = LogFactory.getLog(MonitorResource.class);
  @Autowired
  private MonitorService monitorAPIService;

  /**
   * 虚拟机监控详细信息
   */
  @POST
  @Path("vmInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryVMMonitorInfoService(VMMonitorInfoReq req) {
    VMMonitorInfoResp resp = new VMMonitorInfoResp();
    try {
      resp = monitorAPIService.queryVMMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

  /**
   * 小型机监控详细信息
   */
  @POST
  @Path("mcInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryMCMonitorInfoService(MCMonitorInfoReq req) {
    MCMonitorInfoResp resp = new MCMonitorInfoResp();
    try {
      resp = monitorAPIService.queryMCMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

  /**
   * 块存储监控详细信息
   */
  @POST
  @Path("volumeInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryVolumeMonitorInfoService(VolumeMonitorInfoReq req) {
    VolumeMonitorInfoResp resp = new VolumeMonitorInfoResp();
    try {
      resp = monitorAPIService.queryVolumeMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

  /**
   * 负载均衡监控详细信息
   */
  @POST
  @Path("lbInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryLBMonitorInfoService(LBMonitorInfoReq req) {
    LBMonitorInfoResp resp = new LBMonitorInfoResp();
    try {
      resp = monitorAPIService.queryLBMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

  /**
   * 防火墙监控详细信息
   */
  @POST
  @Path("firewallInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryFirewallMonitorInfoService(FirewallMonitorInfoReq req) {
    FirewallMonitorInfoResp resp = new FirewallMonitorInfoResp();
    try {
      resp = monitorAPIService.queryFirewallMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

  /**
   * 公网IP监控详细信息
   */
  @POST
  @Path("publlcNetworkIPInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryPubllcNetworkIPMonitorInfoService(PubllcNetworkIPMonitorInfoReq req) {
    PubllcNetworkIPMonitorInfoResp resp = new PubllcNetworkIPMonitorInfoResp();
    try {
      resp = monitorAPIService.queryPubllcNetworkIPMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

  /**
   * 带宽监控详细信息
   */
  @POST
  @Path("bwInfoService")
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryBWMonitorInfoService(BWMonitorInfoReq req) {
    BWMonitorInfoResp resp = new BWMonitorInfoResp();
    try {
      resp = monitorAPIService.queryBWMonitorInfoService(req);
    } catch (SCSException e) {
      e.printStackTrace();
      log.equals(e);
    }
    return Response.ok(resp).build();
  }

}
