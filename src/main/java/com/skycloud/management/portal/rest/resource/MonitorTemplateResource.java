/**
 * 2012-1-18  下午02:24:34  $Id:shixq
 */
package com.skycloud.management.portal.rest.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;
import com.skycloud.management.portal.rest.entity.MonitorTemplate;
import com.skycloud.management.portal.rest.model.MonitorTemplateResp;

/**
 * @author shixq
 * @version $Revision$ 下午02:24:34
 */
@Component
@Path("/monitor/templateService")
public class MonitorTemplateResource extends BaseService {
  @Autowired
  private IVMTemplateDao VMTemplateDao;

  /**
   * 查询监控即服务模板信息列表
   */
  @GET
  @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response queryMonitorTemplateService() {
    MonitorTemplateResp resp = new MonitorTemplateResp();
    List<TemplateVMBO> list = new ArrayList<TemplateVMBO>();
    List<MonitorTemplate> listResult = new ArrayList<MonitorTemplate>();
    try {
      list = VMTemplateDao.queryVMTemplateMonitor();
      for (TemplateVMBO bo : list) {
        String networkDesc = bo.getNetwork_desc();
        MonitorTemplate result = new MonitorTemplate();
        if (networkDesc == null || "".equals(networkDesc)) {
          break;
        }
        String[] arrCodes = networkDesc.split(",");
        for (String str : arrCodes) {
          if (str.indexOf("vm") != -1) {
            result.setVm(true);
          } else if (str.indexOf("mc") != -1) {
            result.setMc(true);
          } else if (str.indexOf("vl") != -1) {
            result.setVolume(true);
          } else if (str.indexOf("lb") != -1) {
            result.setLb(true);
          } else if (str.indexOf("fw") != -1) {
            result.setFirewall(true);
          } else if (str.indexOf("pnip") != -1) {
            result.setPubllcnetworkip(true);
          } else if (str.indexOf("bw") != -1) {
            result.setBw(true);
          }
          result.setId(bo.getId());
          result.setName(bo.getTemplate_desc());
        }
        listResult.add(result);
      }
      resp.setResult(listResult);
      resp.setTotalCount(listResult.size());
    } catch (SCSException e) {
      e.printStackTrace();
    }
    return Response.ok(resp).build();
  }
}
