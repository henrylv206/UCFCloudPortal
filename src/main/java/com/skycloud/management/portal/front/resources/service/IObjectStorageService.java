package com.skycloud.management.portal.front.resources.service;

import java.io.UnsupportedEncodingException;

public interface IObjectStorageService {
	String getOsUrl(int resourcePoolId) throws UnsupportedEncodingException;
	int getOsUserStatus(String username) throws Exception ;
}
