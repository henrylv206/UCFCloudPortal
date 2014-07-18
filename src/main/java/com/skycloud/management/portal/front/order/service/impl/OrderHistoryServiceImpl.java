package com.skycloud.management.portal.front.order.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IMCTemplateService;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderHistoryDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.entity.TOrderHistoryBO;
import com.skycloud.management.portal.front.order.service.IOrderHistoryService;

public class OrderHistoryServiceImpl implements IOrderHistoryService {
	private IOrderHistoryDao orderHistoryDao;
    private IInstanceInfoDao instanceInfoDao;
    private IVMTemplateService VMTemplateService;
    private IMCTemplateService MCTemplateService;
	
	@Override
	public int save(TOrderHistoryBO orderHistory) throws SQLException {
		return orderHistoryDao.save(orderHistory);
	}

	@Override
	public int delete(int id) throws SQLException {
		return orderHistoryDao.delete(id);
	}

	@Override
	public int update(TOrderHistoryBO orderHistory) throws SQLException {
		return orderHistoryDao.update(orderHistory);
	}

	@Override
	public TOrderHistoryBO searchById(int id) throws SQLException {
		return orderHistoryDao.searchById(id);
	}

	@Override
	public List<TOrderHistoryBO> searchAll() throws SQLException {
		return orderHistoryDao.searchAll();
	}

	@Override
	public List<TOrderHistoryBO> searchByOrderId(int orderId)
			throws SQLException {
		return orderHistoryDao.searchByOrderId(orderId);
	}

	@Override
	public List<TOrderHistoryBO> searchByProductId(int productId)
			throws SQLException {
		return orderHistoryDao.searchByProductId(productId);
	}

	@Override
	public List<TOrderHistoryBO> searchByInstanceId(int instanceId)
			throws SQLException {
		return orderHistoryDao.searchByInstanceId(instanceId);
	}

	@Override
	public List<TOrderHistoryBO> searchByTemplateId(int templateId)
			throws SQLException {
		return orderHistoryDao.searchByTemplateId(templateId);
	}
	
	@Override
	public int saveByOrder(TOrderBO order) throws SQLException {
		 //获取参数
  	  int instanceId = order.getInstanceInfoId();
  	  int orderId = order.getOrderId();
  	   int productId = -1;
  	   int templateId = -1;
  	 TInstanceInfoBO instanceInfo = null;
	try {
		 int templateType = -1;//模板类型
		 int cpuNum = 0;
		 int memorySize = 0;
		 int storageSize = 0;
		 int eOsId = 0;
		instanceInfo = instanceInfoDao.searchInstanceInfoByID(instanceId);
			if (instanceInfo!=null){
				    productId = instanceInfo.getProductId();
				    templateType = instanceInfo.getTemplateType();
			}
			
			if (order.getType()==2){//修改
				cpuNum = order.getCpuNum();
				 memorySize = order.getMemorySize();
				 storageSize = order.getStorageSize();
			}else if (order.getType()==3){//删除
				cpuNum = instanceInfo.getCpuNum();
				memorySize = instanceInfo.getMemorySize();
				storageSize = instanceInfo.getStorageSize();
				eOsId = (int)instanceInfo.geteOsId();
			}
			templateId = searchTemplateId(templateType,cpuNum,memorySize,storageSize,eOsId); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
  	  //保存历史订单
  	  TOrderHistoryBO orderHistory = new TOrderHistoryBO();
	    	  orderHistory.setInstanceInfoId(instanceId);
	    	  orderHistory.setOrderId(orderId);
	    	  orderHistory.setProductId(productId);
	    	  orderHistory.setTemplateId(templateId);
	    	  return	  this.save(orderHistory);
	}
	
	private int searchTemplateId(int templateType,int cpuNum,int memorySize,int storageSize,int eOsId) throws SQLException {
		int templateId = 0;
		int state = 2;//模板状态；1：已提交；2：确认可用；3：确认不可用；5：作废
		//获取模板ID(templateId)
		try {
			if (templateType == 1){//虚机模板
				  if (storageSize > 0 &&  templateType==1){//块存储,目前块存储没有修改
				    	templateType = 2;
				    }
				List<TTemplateVMBO> templateList = VMTemplateService.advancedSearch(null, templateType, state, cpuNum, memorySize, eOsId, storageSize, null, -1, -1);
				if (templateList != null && templateList.size()>0){
					templateId = templateList.get(0).getId();
				}
			}else  if (templateType == 2){//小型机模板
				 List<TTemplateMCBO> templateList = MCTemplateService.advancedSearch(null, templateType, state, cpuNum, memorySize, storageSize, -1, -1);
				 if (templateList != null && templateList.size()>0){
						templateId = templateList.get(0).getId();
					}
			}else{//其他模板
				List<TTemplateVMBO> templateList = VMTemplateService.advancedSearch(null, templateType, state, 0, 0, 0, storageSize, null, -1, -1);
				if (templateList != null && templateList.size()>0){
					templateId = templateList.get(0).getId();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return templateId;
	}

	public IOrderHistoryDao getOrderHistoryDao() {
		return orderHistoryDao;
	}

	public void setOrderHistoryDao(IOrderHistoryDao orderHistoryDao) {
		this.orderHistoryDao = orderHistoryDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IVMTemplateService getVMTemplateService() {
		return VMTemplateService;
	}

	public void setVMTemplateService(IVMTemplateService vMTemplateService) {
		VMTemplateService = vMTemplateService;
	}

	public IMCTemplateService getMCTemplateService() {
		return MCTemplateService;
	}

	public void setMCTemplateService(IMCTemplateService mCTemplateService) {
		MCTemplateService = mCTemplateService;
	}
	
	

}
