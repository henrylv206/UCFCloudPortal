package com.skycloud.management.portal.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.rest.BaseResource;

/**
 * 资源域相关Resource
 * @author jiaoyz
 */
@Component
@Path("/zone")
public class ZoneResource extends BaseResource {

  @Autowired
  private ICommandService commandService;
/***********************************
 * 此代码没有人调用注释掉 20121205--MJ
 * @param accept
 * @return
  @GET
  public String getZoneList(@HeaderParam("Accept") String accept) {
    String result = "";
    if(logger.isInfoEnabled()) {
      logger.info("REST -- List zone");
    }
    try {
      EZone cmd = new EZone();
      if(accept.toLowerCase().startsWith(MediaType.APPLICATION_JSON.toLowerCase())) {
        result = String.valueOf(commandService.executeAndJsonReturn(cmd));
      }
      else {
        result = String.valueOf(commandService.executeAndXMLReturn(cmd));
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      logger.error("REST -- List zone failed, Exception : " + e.getMessage(), e);
    }
    if(logger.isInfoEnabled()) {
      logger.info("REST -- List zone succeed");
    }
    return result;
  }
  ***************************************/
}
