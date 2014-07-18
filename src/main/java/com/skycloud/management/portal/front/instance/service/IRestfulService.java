package com.skycloud.management.portal.front.instance.service;

import com.skycloud.management.portal.front.resources.rest.RestfulPath;

public interface IRestfulService {
    public Object executeAndJsonReturn(RestfulPath rest,Integer resourcePoolID);

    public Object executeAndXMLReturn(RestfulPath rest,Integer resourcePoolID);
	
	/**
	 * 取得URL
	 * @param cmd
	 * @return String url
	 */
	public String getUrl(RestfulPath rest,Integer resourcePoolID);
	
}
