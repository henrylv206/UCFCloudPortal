package com.skycloud.management.portal.admin.sysmanage.service;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.exception.SCSException;


public interface IResourcePoolsService {
	public int insert(TResourcePoolsBO resourcePools) throws SCSException;
	public int update(TResourcePoolsBO resourcePools) throws SCSException;
	public int delete(int id) throws SCSException;
	public TResourcePoolsBO searchById(int id) throws SCSException;
	public List<TResourcePoolsBO> searchAll() throws SCSException;
  public List<TResourcePoolsBO> searchForReview() throws SCSException;
  List<TResourcePoolsBO> searchResourcePoolsByName(String resourceName) throws SCSException;  
    
}
