package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.service.IResourcePoolsService;
import com.skycloud.management.portal.exception.SCSException;

public class ResourcePoolsServiceImpl implements IResourcePoolsService {

  private static final Logger logger = Logger.getLogger(ResourcePoolsServiceImpl.class);

  private IResourcePoolsDao resourcePoolsDao;

  public IResourcePoolsDao getResourcePoolsDao() {
    return resourcePoolsDao;
  }

  public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
    this.resourcePoolsDao = resourcePoolsDao;
  }

  @Override
  public int insert(TResourcePoolsBO resourcePools) throws SCSException {
    try {
      return resourcePoolsDao.save(resourcePools);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new SCSException(e.getMessage());
    }
  }

  @Override
  public int update(TResourcePoolsBO resourcePools) throws SCSException {
    try {
      return resourcePoolsDao.update(resourcePools);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new SCSException(e.getMessage());
    }

  }

  @Override
  public int delete(int id) throws SCSException {
    try {
      return resourcePoolsDao.delete(id);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new SCSException(e.getMessage());
    }
  }

  @Override
  public TResourcePoolsBO searchById(int id) throws SCSException {
    try {
      return resourcePoolsDao.searchResourcePoolsById(id);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new SCSException(e.getMessage());
    }
  }

  @Override
  public List<TResourcePoolsBO> searchAll() throws SCSException {
    try {
      return resourcePoolsDao.searchAllPools();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new SCSException(e.getMessage());
    }
  }

  @Override
  public List<TResourcePoolsBO> searchForReview() throws SCSException {
    try {
      return resourcePoolsDao.searchForReviewPools();
    } catch (SQLException e) {
      logger.warn("Exception searchForReview", e);
      throw new SCSException(e.getMessage(), e);
    }
  }

  @Override
  public List<TResourcePoolsBO> searchResourcePoolsByName(String resourceName) throws SCSException {
    try {
      return resourcePoolsDao.searchResourcePoolsByName(resourceName);
    } catch (SQLException e) {
      throw new SCSException(e.getMessage());
    }
  }

}
