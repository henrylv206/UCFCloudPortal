package com.skycloud.management.update.portal.admin.resmanage.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.update.portal.admin.resmanage.dao.ResourcePoolStatusDao;
import com.skycloud.management.update.portal.admin.resmanage.entity.ResourcePoolStatusPO;

public class ResourcePoolStatusServiceImpl extends SpringJDBCBaseDao 
implements ResourcePoolStatusService {

    private static final Logger logger = Logger.getLogger(ResourcePoolStatusServiceImpl.class);
    private ResourcePoolStatusDao resPoolStatusDao;
    
    @Override
    public List<ResourcePoolStatusPO> queryResourcePoolStatus(int curPage,
            int pageSize) throws SCSException {
        List<ResourcePoolStatusPO> res = new ArrayList<ResourcePoolStatusPO>();
        List<ResourcePoolStatusPO> pools = resPoolStatusDao.queryResourcePoolStatus(curPage, pageSize);
        
        if (pools != null && pools.size() > 0) {
            for (ResourcePoolStatusPO pool: pools) {
                if (pool.getType() <= 4)
                    res.add(pool);
            }
        }
        return res;
    }

    public ResourcePoolStatusDao getResPoolStatusDao() {
        return resPoolStatusDao;
    }

    public void setResPoolStatusDao(ResourcePoolStatusDao resPoolStatusDao) {
        this.resPoolStatusDao = resPoolStatusDao;
    }

    @Override
    public int queryResourcePoolStatusTotal() throws SCSException {
        return this.resPoolStatusDao.queryResourcePoolStatusTotal();
    }

    @Override
    public List<ResourcePoolStatusPO> getAllPoolRecords() throws SCSException {
        
        return resPoolStatusDao.queryResourcePoolStatus(1, 10);
    }

    @Override
    public void newPoolState(ResourcePoolStatusPO newState) throws SCSException {
        resPoolStatusDao.newPoolState(newState);
    }

    @Override
    public void updatePoolState(ResourcePoolStatusPO poolState)
            throws SCSException {
        resPoolStatusDao.updatePoolState(poolState);
    }

}
