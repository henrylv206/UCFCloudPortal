/**
 * 2012-1-31 上午10:49:48 $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.StorageMonitorVO;

/**
 * @author shixq
 * @version $Revision$ 上午10:49:48
 */
public interface StorageMonitorService {

	/**
	 * @param vo
	 * @return
	 * @throws SCSException
	 */
	StorageMonitorVO storageMonitorInfo4Elaster(ResourcesQueryVO vo, int resourcePoolsID) throws SCSException;

}
