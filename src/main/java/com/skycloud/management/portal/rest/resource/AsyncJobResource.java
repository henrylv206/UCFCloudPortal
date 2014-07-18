package com.skycloud.management.portal.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.front.command.res.QueryAsyncJobResult;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.rest.BaseResource;

/**
 * 异步任务相关Resource
 * @author jiaoyz
 */
@Component
@Path("/asyncJob")
public class AsyncJobResource extends BaseResource {

  @Autowired
  private ICommandService commandService;
/******************************************
 * 此代码没有人调用注释掉 20121205--MJ
 * getAsyncJob
 * @param id
 * @param accept
 * @return
  @GET
  @Path("{id}")
  public String getAsyncJob(@PathParam("id") int id, @HeaderParam("Accept") String accept) {
    if(logger.isInfoEnabled()) {
      logger.info("REST -- Get asyncJob by id : " + id);
    }
    String result = "";
    try {
      QueryAsyncJobResult cmd = new QueryAsyncJobResult(id);
      if(accept.toLowerCase().startsWith(MediaType.APPLICATION_JSON.toLowerCase())) {
        result = String.valueOf(commandService.executeAndJsonReturn(cmd));
      }
      else {
        result = String.valueOf(commandService.executeAndXMLReturn(cmd));
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      logger.error("REST -- Get asyncJob by id : " + id + " failed, Exception : " + e.getMessage(), e);
    }
    if(logger.isInfoEnabled()) {
      logger.info("REST -- Get asyncJob by id : " + id + " succeed");
    }
    return result;
  }
******************************************/  
}
