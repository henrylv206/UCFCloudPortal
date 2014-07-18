package com.skycloud.management.update.portal.admin.resmanage.service;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.update.portal.admin.resmanage.entity.ResourcePoolStatusPO;

public interface ResourcePoolStatusService {
    List<ResourcePoolStatusPO> queryResourcePoolStatus(int curPage, int pageSize) throws SCSException;
    int queryResourcePoolStatusTotal() throws SCSException;
    List<ResourcePoolStatusPO> getAllPoolRecords() throws SCSException;
    void newPoolState(ResourcePoolStatusPO newState) throws SCSException;
    void updatePoolState(ResourcePoolStatusPO poolState) throws SCSException;
}
