package com.skycloud.management.portal.front.instance.service.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.common.utils.HttpConnector;
import com.skycloud.management.portal.front.command.ICommand;
import com.skycloud.management.portal.front.command.IResult;
import com.skycloud.management.portal.front.command.Signature;
import com.skycloud.management.portal.front.command.impl.Result;
import com.skycloud.management.portal.front.instance.service.ICommandService;


public class CommandServiceImpl implements ICommandService {
	private static final Log log = LogFactory.getLog(CommandServiceImpl.class);

	private Signature signature;
	
  public Object executeAndJsonReturn(ICommand cmd,Integer resourcePoolID) {
	  String url;
	  Object obj = null;
	  try {
      url = this.signature.buildUrl(cmd,resourcePoolID);
      log.debug("API URL: " + url);
      log.debug("API Cmd: " + cmd.getUriParams());
      obj = HttpConnector.request(url + "&response=json");
      log.debug("API Response: "+obj);
	  } catch (Exception e) {
      log.error(cmd.getCommandName() + " API execute error:", e);
      obj = e;
    }
    log.debug("API return: " + obj);
    return obj;
  }

  public Object executeAndXMLReturn(ICommand cmd,Integer resourcePoolID) {
    String url;
    Object obj = null;
    try {
      url = this.signature.buildUrl(cmd,resourcePoolID);
      log.debug("API URL: " + url);
      log.debug("API Cmd: " + cmd.getUriParams());
      obj = HttpConnector.request(url);
      log.debug("API Response: "+obj);
    } catch (Exception e) {
      log.error(cmd.getCommandName() + " API execute error:", e);
      obj = e;
    }
    log.debug("API return: " + obj);
    return obj;
  }
  
	public IResult executeAsync(ICommand cmd,Integer resourcePoolID) {
		String url;
		Object obj = null;
		try {
			url = this.signature.buildUrl(cmd,resourcePoolID);
			log.debug("API URL: "+url);
			log.debug("API Cmd: " + cmd);
			obj = HttpConnector.request(url + "&response=json");
			log.debug("API Response: "+obj);
//			obj = xmlParser.parse(xmlParser.readString(resp));
		} catch (Exception e) {
			log.error(cmd.getCommandName() + " API execute error:", e);
			obj = e;
		}
		log.debug("API return: " + obj);
		return new Result(obj);
	}
	
	public String getUrl(ICommand cmd,Integer resourcePoolID){
		try {
			return this.signature.buildUrl(cmd,resourcePoolID);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setSignature(Signature signature) {
		this.signature = signature;
	}

}
