package com.skycloud.management.update.portal.admin.resmanage.dao;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.update.portal.admin.resmanage.entity.ResourcePoolStatusPO;

public interface ResourcePoolStatusDao {
    List<ResourcePoolStatusPO> queryResourcePoolStatus(int curPage, int pageSize) throws SCSException;

    int queryResourcePoolStatusTotal() throws SCSException;

    void newPoolState(ResourcePoolStatusPO newState) throws SCSException;

    void updatePoolState(ResourcePoolStatusPO poolState) throws SCSException;
}
