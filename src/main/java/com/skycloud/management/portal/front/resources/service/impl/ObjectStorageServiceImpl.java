package com.skycloud.management.portal.front.resources.service.impl;
import java.io.UnsupportedEncodingException;

import com.skycloud.management.portal.front.command.Signature;
import com.skycloud.management.portal.front.resources.dao.IObjectStorageDao;
import com.skycloud.management.portal.front.resources.service.IObjectStorageService;
public class ObjectStorageServiceImpl implements IObjectStorageService{
	private Signature signature;
	private IObjectStorageDao objectStoragedao;
	
	@Override
	public String getOsUrl(int resourcePoolId) throws UnsupportedEncodingException{
		return signature.buildOsUrl(resourcePoolId);
	}
	
	@Override
	public int getOsUserStatus(String username) throws Exception {
		return this.objectStoragedao.getOsUserStatus(username);
	}
	
	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public IObjectStorageDao getObjectStoragedao() {
		return objectStoragedao;
	}

	public void setObjectStoragedao(IObjectStorageDao objectStoragedao) {
		this.objectStoragedao = objectStoragedao;
	}

}
