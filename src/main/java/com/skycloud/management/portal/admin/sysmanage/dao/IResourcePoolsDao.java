package com.skycloud.management.portal.admin.sysmanage.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;

public interface IResourcePoolsDao {
  int save(TResourcePoolsBO resourcePools) throws SQLException;

  int delete(int resourcePoolsId) throws SQLException;

  int update(TResourcePoolsBO resourcePools) throws SQLException;

  TResourcePoolsBO searchResourcePoolsById(int resourcePoolsId) throws SQLException;

  List<TResourcePoolsBO> searchAllPools() throws SQLException;

  int searchLastId() throws SQLException;

  List<TResourcePoolsBO> searchForReviewPools() throws SQLException;

  List<TResourcePoolsBO> searchResourcePoolsByName(String resourceName) throws SQLException;

}
