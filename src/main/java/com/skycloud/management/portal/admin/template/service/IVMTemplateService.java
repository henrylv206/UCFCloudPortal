package com.skycloud.management.portal.admin.template.service;

import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;

/**
 * 模板业务接口
 * 
 * @author jiaoyz
 */
public interface IVMTemplateService {

	/**
	 * 创建模板
	 * 
	 * @param template
	 *            模板对象
	 * @return 模板id
	 * @throws Exception
	 */
	public int createTemplate(TTemplateVMBO template) throws Exception;

	/**
	 * 删除模板
	 * 
	 * @param id
	 *            模板id
	 * @throws Exception
	 */
	public void deleteTemplate(int id) throws Exception;

	/**
	 * 更新模板
	 * 
	 * @param template
	 *            模板对象
	 * @throws Exception
	 */
	public void updateTemplate(TTemplateVMBO template) throws Exception;

	/**
	 * 获取模板
	 * 
	 * @param id
	 *            模板id
	 * @return 模板对象
	 * @throws Exception
	 */
	public TTemplateVMBO getTemplateById(int id) throws Exception;

	/**
	 * 获取模板列表
	 * 
	 * @param type
	 *            模板类型
	 * @param state
	 *            模板状态
	 * @param curPage
	 *            当前页码
	 * @param pageSize
	 *            每页条数
	 * @return 模板列表
	 * @throws Exception
	 */
	public List<TTemplateVMBO> listTemplateOM(int storageSize) throws Exception;

	public List<TTemplateVMBO> listTemplateDISK(int storageSize) throws Exception;

	public List<TTemplateVMBO> listTemplate(int type, int state, int curPage, int pageSize, int resourcePoolsId, int zoneId) throws Exception;
	public List<TTemplateVMBO> listTemplateNotSpecial(int type, int state, int curPage, int pageSize, int resourcePoolsId, int zoneId) throws Exception ;
	public List<TTemplateVMBO> listTemplateByType(int type, int state, int templateId, int cpu_num, int memory_size) throws Exception;

	/**
	 * 获取模板数
	 * 
	 * @param type
	 *            模板类型
	 * @param state
	 *            模板状态
	 * @return 模板数
	 * @throws Exception
	 */
	public int listTemplateCount(int type, int state) throws Exception;

	/**
	 * 搜索模板列表
	 * 
	 * @param name
	 *            模板名称
	 * @param type
	 *            模板类型
	 * @param state
	 *            模板状态
	 * @param curPage
	 *            当前页码
	 * @param pageSize
	 *            每页条数
	 * @return 模板列表
	 * @throws Exception
	 */
	public List<TTemplateVMBO> searchTemplate(String name, int type, int state, int curPage, int pageSize) throws Exception;

	/**
	 * 搜索模板数
	 * 
	 * @param name
	 *            模板名称
	 * @param type
	 *            模板类型
	 * @param state
	 *            模板状态
	 * @return 模板数
	 * @throws Exception
	 */
	public int searchTemplateCount(String name, int type, int state) throws Exception;

	/**
	 * 检查名称是否唯一
	 * 
	 * @param name
	 *            模板名称
	 * @return 是否唯一
	 * @throws Exception
	 */
	public boolean checkNameUniqueness(String name) throws Exception;

	/**
	 * 高级搜索
	 * 
	 * @param name
	 *            模板名称
	 * @param type
	 *            模板类型
	 * @param state
	 *            模板状态
	 * @param cpuNum
	 *            处理器个数
	 * @param memSize
	 *            内存大小
	 * @param osId
	 *            OS ID
	 * @param storageSize
	 *            磁盘大小或负载均衡-并发数或 防火墙-规则条数或 公网IP-IP类型或带宽-带宽大小
	 * @param networkDesc
	 *            监控服务-资源类型组合字符串
	 * @param curPage
	 *            当前页码
	 * @param pageSize
	 *            每页条数
	 * @return 模板列表
	 * @throws Exception
	 */
	public List<TTemplateVMBO> advancedSearch(String name, int type, int state, int cpuNum, int memSize, int osId, int storageSize,
	        String networkDesc, int curPage, int pageSize) throws Exception;

	/**
	 * 高级搜索数
	 * 
	 * @param name
	 *            模板名称
	 * @param type
	 *            模板类型
	 * @param state
	 *            模板状态
	 * @param cpuNum
	 *            处理器个数
	 * @param memSize
	 *            内存大小
	 * @param osId
	 *            OS ID
	 * @param storageSize
	 *            磁盘大小或负载均衡-并发数或 防火墙-规则条数或 公网IP-IP类型或带宽-带宽大小
	 * @param networkDesc
	 *            监控服务-资源类型组合字符串
	 * @return 模板数
	 * @throws Exception
	 */
	public int advancedSearchCount(String name, int type, int state, int cpuNum, int memSize, int osId, int storageSize, String networkDesc)
	        throws Exception;

	/*
	 * 检查被监控的资源类型组合是否唯一
	 * @param resourceTypes 资源类型以逗号分隔的形式的组成的字符串
	 * @return 是否唯一
	 * @throws Exception
	 */
	public boolean checkMonitorResources(String resourceTypes, int id, int addOrMod) throws Exception;

	/**
	 * 更新模板,适用于VDC项目,扩展了一些字段
	 * 
	 * @param template
	 *            扩展后的模板对象
	 */
	public void updateTemplate_VDC(final TTemplateVMBO template) throws Exception;

	/**
	 * 查看模板是否已经被产品绑定
	 * 
	 * @param template
	 * @param templateType
	 * @return
	 * @throws Exception
	 *             创建人： 刘江宁 创建时间：2012-3-15 下午01:34:01
	 */
	public boolean getTemplateIsBindProduction(int template, int templateType) throws SCSException;

	/**
	 * 根据模板标识更新模板状态
	 * 
	 * @param templateId
	 * @param state
	 * @throws SCSException
	 *             创建人： 刘江宁 创建时间：2012-3-15 下午05:33:55
	 */
	public int upateTemplateState(int templateId, int state) throws SCSException;

	/**
	 * 搜索物理机模板的全部操作系统列表 创建人： ninghao 创建时间：2012-8-28 09:53:55
	 * 
	 * @return 物理机操作系统列表
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPhysicalTemplateOS(int type) throws Exception;

	// fix bug 3970
	List<TTemplateVMBO> listTemplateByType(int type, int state, int templateId, int cpu_num, int memory_size, String osDesc) throws Exception;

	/**
	 * 更新模板（修改订单）
	 * @param template
	 * @return
	 * @throws Exception
	 * @author ninghao@chinaskycloud.com
	 */
	public int updateTemplateByOrderModify(TTemplateVMBO template) throws SCSException ;
}
