package com.skycloud.management.portal.front.order.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.dao.IServiceInstanceDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;

public class ServiceInstanceServiceImpl implements IServiceInstanceService {

	private IServiceInstanceDao serviceInstanceDao;

	private IInstanceInfoDao instanceInfoDao;

	private IOrderDao orderDao;

	public IServiceInstanceDao getServiceInstanceDao() {
		return serviceInstanceDao;
	}

	public void setServiceInstanceDao(IServiceInstanceDao serviceInstanceDao) {
		this.serviceInstanceDao = serviceInstanceDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public int save(TServiceInstanceBO serviceInstance) throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.save(serviceInstance);
	}

	@Override
	public int delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.delete(id);
	}

	@Override
	public int update(TServiceInstanceBO serviceInstance) throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.update(serviceInstance);
	}

	@Override
	public TServiceInstanceBO searchById(int id) throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.searchById(id);
	}

	@Override
	public List<TServiceInstanceBO> searchAll() throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.searchAll();
	}

	@Override
	public List<TServiceInstanceBO> searchByOrderId(int orderId, int orderType) throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.searchByOrderId(orderId, orderType);
	}

	@Override
	public List<TServiceInstanceBO> searchByOrderId(PageVO vo,int orderId, int orderType) throws SQLException {
		// TODO Auto-generated method stub
		return serviceInstanceDao.searchByOrderId(vo,orderId, orderType);
	}

	@Override
    public int countDetailByOrderId(int orderId,int orderType) throws SQLException{
		return serviceInstanceDao.countDetailByOrderId(orderId, orderType);
	}
	@Override
	public int checkServiceInstanceStateById(int serviceId) throws SQLException {
		TServiceInstanceBO sInstance = serviceInstanceDao.searchById(serviceId);
		int state = -1;
		if (sInstance != null) {
			// 1.申请开通，2.可用，3.正在开通，4.作废，5.申请退订，6.正在退订，7.不可用
			int stateS = sInstance.getState();
			if (stateS == 2 || stateS == 4 || stateS == 7) {// 相信job命令反写回来的状态
				// 申请开通可能状态：1.申请开通，2.可用，3.正在开通，4.作废, 7.不可用
				state = stateS;
			} else if (stateS == 1 || stateS == 3) {
				state = stateS;
				TOrderBO order = orderDao.searchOrderById(sInstance.getOrderId());
				if (order != null) {// 查询订单状态成功
					// bug 0003263
					if (order.getState() == 5 || order.getState() == 6) {// 审核拒绝
						// 修改实例状态为作废
						sInstance.setState(4);
						update(sInstance);
						// 返回作废状态
						state = 4;
					} else if (order.getState() == 4) {// 审核通过
						int statei = this.searchServiceInstanceState(sInstance.getId());
						if (statei != -1) {// 调用失败
							if (statei == 2 || statei == 5) {
								// 修改服务实例状态为可用
								sInstance.setState(2);
								update(sInstance);
							}
//							else if (statei == 7){//创建资源失败状态=7
//								// 修改服务实例状态为不可用
//								sInstance.setState(7);
//								update(sInstance);
//							}
							// 返回实例状态可能为:1,2,3,7
							state = statei;
						}
					}
				}// 正在退订可能状态：2.可用，4.作废， 6.正在退订，5.申请退订，7.不可用
			} else if (stateS == 6 || stateS == 5) {
				state = stateS;
				int statei = this.searchServiceInstanceState(sInstance.getId());
				if (statei != -1) {// 调用失败
					if (statei == 4) {// 4.作废
						sInstance.setState(4);
						// 修改实例状态为作废
						update(sInstance);
						state = statei;
					}
					//to fix bug [3725]
//					else if (statei == 3) {// 资源实例正在退订时状态为3
//						//to fix bug [3707]
//						//state = 6;
//						//state = 5;
//						//to fix bug:3422
//						state = 5;
//					}
					else if (statei == 6 ) {// ,6.正在退订,7.不可用
						//to fix bug:3422
						state = 5;
					} else if ( statei == 7) {// ,6.正在退订,7.不可用
						state = statei;
					}else {
						//黑龙江需要打开下面的状态机, 北京用job维护此状态，
						//state = 2;// 可用
					}
				}
			}
		}

		return state;
	}

	private int searchServiceInstanceState(int serviceInstanceId) throws SQLException {
		int return_state = -1;
		try {
			int state = 0;
			List<TInstanceInfoBO> instanceinfos = this.instanceInfoDao.searchInstanceInfoByServiceInstanceId(serviceInstanceId);
			if (instanceinfos != null) {
				for (TInstanceInfoBO ins : instanceinfos) {
					int state1 = ins.getState();
					if (state == 0) {// 初始化
						state = state1;
					}
					if (state1 == 3) {// 正在开通
						state = 3;
						break;  //to fix bug:3954
					}else if (state1 == 7) {// 命令处理失败
						state = 7;
					} else if (state1 == 6) {// 命令处理中
						state = 6;
					}
				}
			}
			return_state = state;
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_state;
	}

	@Override
	public int updateServiceStateByInstanceInfoId(int state, int instanceInfoId) throws SCSException {
		return serviceInstanceDao.updateServiceStateByInstanceInfoId(state, instanceInfoId);
	}

	@Override
	public int updateServiceStateByOrderId(int state, int orderId) throws SCSException {
		return serviceInstanceDao.updateServiceStateByOrderId(state, orderId);
	}

	@Override
	public int updateServiceStateByOrder(int state, TOrderBO order) throws SCSException {
		int ret_val = -1;
		if (order.getType() == 1) {
			ret_val = this.updateServiceStateByOrderId(state, order.getOrderId());
		} else if (order.getType() == 3) {
			// to fix bug:3530
			ret_val = serviceInstanceDao.updateServiceStateByServiceId(state, order.getServiceInstanceId());
			if (ret_val <= 0) {
				ret_val = this.updateServiceStateByInstanceInfoId(state, order.getInstanceInfoId());
			}
		}
		return ret_val;
	}

	@Override
	public int updateServiceStateByServiceId(int state, int serviceId) throws SCSException {
		return serviceInstanceDao.updateServiceStateByServiceId(state, serviceId);
	}

	@Override
    public int rollbackServiceStateByServiceId(int serviceId) throws SCSException {
	    return serviceInstanceDao.rollbackServiceStateByServiceId(serviceId);
    }

	@Override
    public TServiceInstanceBO searchServiceInstanceByInstanceInfoId(int instanceInfoid) throws SCSException {
		return serviceInstanceDao.searchServiceInstanceByInstanceInfoId(instanceInfoid);
    }

	
	@Override
    public int cancelUpdateServiceInstanceByOrderId(int orderId) throws SQLException {
		//修改申请的服务实例,回滚到修改前的服务实例
		TOrderBO orderBO = orderDao.searchOrderById(orderId);
		if(orderBO!=null){
			int serviceId = orderBO.getServiceInstanceId();
			TServiceInstanceBO  serviceInstanceBo = this.searchById(serviceId);
			int serviceHistoryId = 0;
			if(serviceInstanceBo!=null){
				serviceHistoryId = serviceInstanceBo.getHistoryId();
				TServiceInstanceBO  serviceInstanceHistoryBo = this.searchById(serviceHistoryId);
				//将被修改的服务实例变为当前服务实例
				serviceInstanceHistoryBo.setHistoryState(0);
				this.update(serviceInstanceHistoryBo);
				//将当前服务实例变为历史服务
				serviceInstanceBo.setHistoryState(1);
				this.update(serviceInstanceBo);
			}
		}
	    return 0;
    }


}
