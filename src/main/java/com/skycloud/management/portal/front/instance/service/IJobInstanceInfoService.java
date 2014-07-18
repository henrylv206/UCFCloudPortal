package com.skycloud.management.portal.front.instance.service;

import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public interface IJobInstanceInfoService {
	public List<ResourcesVO> queryInstanceByUser(TUserBO user) throws SCSException;
	public void updateInstanceByUser(TUserBO user)throws SCSException;
	public void deleteInstanceByUser(TUserBO user)throws SCSException;
	public void deleteInstanceByDestoryUser();
	public void deleteInstanceByOrder(TOrderBO order, TUserBO user)throws SCSException;
	public void disabledEAccount(TUserBO user,int resourcePoolsId);
	public Map<String,String> findEUser(TUserBO user);
	public Map<String,String> findEAccount(TUserBO user,int resourcePoolsId);
	public Map<String,String> findEDomain(TUserBO user,int resourcePoolsId);
	public List<ResourcesVO> queryAllInstanceByUser(TUserBO user) throws SCSException; 
	////fix bug 4561
	public void disabledVM(TInstanceInfoBO infoBO) throws Exception;
	public void disabledStorage(TInstanceInfoBO infoBO);
	public void disabledBackup(TInstanceInfoBO infoBO);
}
