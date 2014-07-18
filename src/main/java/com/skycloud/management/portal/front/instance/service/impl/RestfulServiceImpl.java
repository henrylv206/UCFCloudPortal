package com.skycloud.management.portal.front.instance.service.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.common.utils.HttpConnector;
import com.skycloud.management.portal.front.command.Signature;
import com.skycloud.management.portal.front.instance.service.IRestfulService;
import com.skycloud.management.portal.front.resources.rest.RestfulPath;


public class RestfulServiceImpl implements IRestfulService {
	private static final Log log = LogFactory.getLog(RestfulServiceImpl.class);

	private Signature signature;
	
    public Object executeAndJsonReturn(RestfulPath cmd,Integer resourcePoolID) {
		String url;
		Object obj = null;
		try {
	      url = this.signature.buildRestUrl(cmd,resourcePoolID);
	      log.info("API URL: " + url);
	      log.info("API Cmd: " + cmd.getUriParams());
	      if(cmd.getUriParams() != null && !"".equals(cmd.getUriParams())){

	    	  url = url + "&response=json";
	      }else{
	    	  url = url + "?response=json";
	    	  
	      }
	      obj = HttpConnector.request(url);
	      log.debug("API Response: "+obj);
		  } catch (Exception e) {
	      log.error(cmd.getUrlPath() + " API execute error:", e);
	      obj = e;
	    }
	    log.debug("API return: " + obj);
	    return obj;
    }

    public Object executeAndXMLReturn(RestfulPath cmd,Integer resourcePoolID) {
	    String url;
	    Object obj = null;
	    try {
	      url = this.signature.buildRestUrl(cmd,resourcePoolID);
	      log.info("API URL: " + url);
	      log.info("API Cmd: " + cmd.getUriParams());
	      obj = HttpConnector.request(url);
	      log.info("API Response: "+obj);
	    } catch (Exception e) {
	      log.error(cmd.getUrlPath() + " API execute error:", e);
	      obj = e;
	    }
	    log.info("API return: " + obj);
	    return obj;
    }
  
	
	public String getUrl(RestfulPath cmd,Integer resourcePoolID){
		try {
			return this.signature.buildRestUrl(cmd,resourcePoolID);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setSignature(Signature signature) {
		this.signature = signature;
	}

}
