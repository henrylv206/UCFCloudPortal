package com.skycloud.management.portal.front.loadbalance.service;
/**
 * ******H3C****
 * 虚服务	--代表Loadbalance一个实例，这个实例是从 Loadbalance物理设备上虚拟化来的。
 * 实服务组	--代表虚拟Loadbalance实例，下包含的IP组（主机组）
 * 实服务	--代表一个IP地址（一台主机）
 */
import java.util.List;

import com.skycloud.h3c.loadbalance.po.base.BaseResponsePO;
import com.skycloud.h3c.loadbalance.po.service.ServiceRowPO;
import com.skycloud.h3c.loadbalance.po.servicegroup.ServiceGroupRowPO;
import com.skycloud.h3c.loadbalance.po.vservice.VServiceRowPO;
import com.skycloud.h3c.loadbalance.vo.LoadBalanceVO;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;

public interface IoadBalanceH3CService {

	//增加
	public BaseResponsePO addServer(ServiceRowPO row);
	//查询
	public List<VServiceRowPO> queryVirtualServiceList();
	public VServiceRowPO queryVirtualServiceByName(LoadBalanceVO vo);
	public List<ServiceRowPO> queryServiceList(LoadBalanceVO vo);

	public ServiceGroupRowPO queryServiceGroupByName(LoadBalanceVO vo);

	//更新
	public BaseResponsePO updateVirtualService(VServiceRowPO row);

	//销毁
	public BaseResponsePO destroyVirtualService(VServiceRowPO row);


	public BaseResponsePO destroyServer(ServiceRowPO row);
	/**
	 * 更新实服务组
	 * 对应前台 修改 负载均衡 算法规则 和 绑定虚拟机实例
	 * @param ServiceGroupRowPO  row
	 * @return BaseResponsePO
	 * @throws Exception
	 */
	public BaseResponsePO updateServerGroup(ServiceGroupRowPO row) throws Exception;
	/**
	 * 销毁服务组
	 * 对应自服务门户 销毁负载均衡实例
	 * @param ServiceGroupRowPO  row
	 * @return BaseResponsePO
	 * @throws Exception
	 */
	public BaseResponsePO destroyServerGroup(ServiceGroupRowPO row) throws Exception;

	/**
	 * 更新服务关系
	 * 对应自服务门户
	 *  负载均衡与虚拟机之间的绑定关系
	 * @return BaseResponsePO
	 * @throws Exception
	 */
	public BaseResponsePO updateServer(ServiceRowPO row)throws Exception;

	/**
	 * 特殊模板申请创建申请模板
	 * 
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	public TTemplateVMBO creatSpecalLoadbalanceTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;

}
