package com.skycloud.management.portal.front.command;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.front.resources.rest.RestfulPath;


public class Signature {
	private final String host;
	private final String physicalHost;
	private IResourcePoolsDao resourcePoolsDao;
//	private String apiKey;
//	private String encodedApiKey;
//	private String secretKey;
//	private SecretKeySpec secretKeySpec;
	//added by zhanghuizheng
	//private String obsmgrUri;

	public Signature(){
		this.host =  ConfigManager.getInstance().getString("elaster.host");
		this.physicalHost =  ConfigManager.getInstance().getString("resource.pool.physical.service.name");
		//this.obsmgrUri = ConfigManager.getInstance().getString("obsmgr.uri");
	}

//	public Signature(String host, String apiKey, String secretKey) throws UnsupportedEncodingException{
//
//		this.apiKey = apiKey;
//		this.secretKey = secretKey;
//
//		this.encodedApiKey = URLEncoder.encode(apiKey.toLowerCase(), ICommand.ENCODING);
//		this.secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA1");
//
//	}

	public String getHost() {
		return host;
	}

	public String buildUrl(ICommand cmd,Integer resourcePoolId) throws UnsupportedEncodingException{
		String signature = null;
		String ip = "";
		String port ="";
		try {
		  TResourcePoolsBO resourcePool = resourcePoolsDao.searchResourcePoolsById(resourcePoolId);
		  if(resourcePool!=null){
		    ip =  resourcePool.getIp();
		    port = resourcePool.getPort();
		  }
		  if(StringUtils.isBlank(ip)||StringUtils.isBlank(port)){
		    throw new Exception("don't find resourcePool ip or port! resourcePoolId="+resourcePoolId);

		  }
			signature = this.sign(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringBuilder("http://").append(ip).append(":").append(port).append("/?").append(cmd.getUriParams()).toString();
			// sign
			//.append("&apiKey=").append(apiKey).append("&signature=").append(signature)

	}

	public String sign(ICommand cmd) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException{
		List<String> params = this.getSortedParams(cmd);
		return this.sign(this.getSortedUri(params));
	}

	/**
	 * ��������key=value����ʽ����ĸ˳������,��apiKey
	 * @param map
	 * @return
	 */
	private List<String> getSortedParams(ICommand cmd){
		List<String> params = cmd.getKeyValues();
//		params.add("apiKey="+encodedApiKey);
		Collections.sort(params);
		return params;
	}

	/**
	 *
	 * @param keyValues
	 * @return
	 */
	private String getSortedUri(List<String> keyValues){
		StringBuilder sortedUrl = new StringBuilder(100);
		boolean first = true;
		for (String param : keyValues) {
			if (first) {
				sortedUrl.append(param);
				first = false;
			} else {
				sortedUrl.append("&").append(param);
			}
		}
		return sortedUrl.toString();
	}

	/**
	 * ��apiKey+secretKeyǩ��request
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String sign(String request) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{
//		Mac mac = Mac.getInstance("HmacSHA1");
////		mac.init(secretKeySpec);
//		mac.update(request.toLowerCase().getBytes());
//		byte[] encryptedBytes = mac.doFinal();
		return URLEncoder.encode(request, ICommand.ENCODING);
	}

  public IResourcePoolsDao getResourcePoolsDao() {
    return resourcePoolsDao;
  }

  public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
    this.resourcePoolsDao = resourcePoolsDao;
  }

	public String buildRestUrl(RestfulPath rest,Integer resourcePoolId) throws UnsupportedEncodingException{
		String signature = null;
//		String ip = "";
//		String port ="";
		String phyRestPath ="";
		try {
		  TResourcePoolsBO resourcePool = resourcePoolsDao.searchResourcePoolsById(resourcePoolId);
		  if(resourcePool!=null){
//		    ip =  resourcePool.getIp();
//		    port = resourcePool.getPort();
			  phyRestPath = resourcePool.getPhyRestPath();
		  }
//		  if(StringUtils.isBlank(ip)||StringUtils.isBlank(port)){
		  if(StringUtils.isBlank(phyRestPath)){
		    throw new Exception("don't find resourcePool's physical rest path!");

		  }
			signature = this.signRest(rest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//组装REST的URL路径path和参数Params
		StringBuilder sb = new StringBuilder();
//		sb.append("http://").append(ip).append(":").append(port).append("/").append(physicalHost).append(rest.getUrlPath());
		sb.append("http://").append(phyRestPath).append("/").append(physicalHost).append(rest.getUrlPath());
		if(rest.getUriParams() != null && !"".equals(rest.getUriParams())){
			sb.append("?").append(rest.getUriParams());
		}

		return sb.toString();
	}

	public String signRest(RestfulPath rest) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException{
		List<String> params = this.getRestSortedParams(rest);
		return this.sign(this.getSortedUri(params));
	}
	private List<String> getRestSortedParams(RestfulPath rest){
		List<String> params = rest.getKeyValues();
		Collections.sort(params);
		return params;
	}
	
	public String buildOsUrl(Integer resourcePoolId) throws UnsupportedEncodingException{
		String signature = null;
		String ip = "";
		String port ="";
		try {
		  TResourcePoolsBO resourcePool = resourcePoolsDao.searchResourcePoolsById(resourcePoolId);
		  if(resourcePool!=null){
		    ip =  resourcePool.getIp();
		    port = resourcePool.getPort();
		  }
		  if(StringUtils.isBlank(ip)||StringUtils.isBlank(port)){
		    throw new Exception("don't find resourcePool ip or port!");
		    
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
		//return new StringBuilder("http://").append(ip).append(":").append(port).append("/").append(obsmgrUri).toString();		
	}
	
}
