package com.skycloud.management.portal.admin.template.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;

/**
 * 小型机模板业务接口
 * @author jiaoyz
 */
public interface IMCTemplateService {

  /**
   * 创建模板
   * @param template 模板对象
   * @return 模板id
   * @throws Exception
   */
  public int createTemplate(TTemplateMCBO template) throws Exception;

  /**
   * 删除模板
   * @param id 模板id
   * @throws Exception
   */
  public void deleteTemplate(int id) throws Exception;

  /**
   * 获取模板
   * @param id 模板id
   * @return 模板对象
   * @throws Exception
   */
  public TTemplateMCBO getTemplateById(int id) throws Exception;

  /**
   * 获取模板列表
   * @param state 模板状态
   * @param curPage 当前页码
   * @param pageSize 每页条数
   * @return 模板列表
   * @throws Exception
   */
  public List<TTemplateMCBO> listTemplate(int state, int curPage, int pageSize) throws Exception;
  public List<TTemplateMCBO> listTemplateNotSpecial(int state, int curPage, int pageSize)
	throws Exception;
  /**
   * 获取模板数
   * @param state 模板状态
   * @return 模板数
   * @throws Exception
   */
  public int listTemplateCount(int state) throws Exception;

  /**
   * 搜索模板列表
   * @param name 模板名称
   * @param type 小机类型
   * @param state 模板状态
   * @param curPage 当前页码
   * @param pageSize 每页条数
   * @return 模板列表
   * @throws Exception
   */
  public List<TTemplateMCBO> searchTemplate(String name, int type, int state, int curPage, int pageSize) throws Exception;

  /**
   * 搜索模板数
   * @param name 模板名称
   * @param type 小机类型
   * @param state 模板状态
   * @return 模板数
   * @throws Exception
   */
  public int searchTemplateCount(String name, int type, int state) throws Exception;

  /**
   * 检查名称是否唯一
   * @param name 模板名称
   * @return 是否唯一
   * @throws Exception
   */
  public boolean checkNameUniqueness(String name) throws Exception;

  /**
   * 高级搜索
   * @param name 模板名称
   * @param type 模板类型
   * @param state 模板状态
   * @param cpuNum 处理器个数
   * @param memSize 内存大小
   * @param storageSize 磁盘大小
   * @param curPage 当前页码
   * @param pageSize 每页条数
   * @return 模板列表
   * @throws Exception
   */
  public List<TTemplateMCBO> advancedSearch(String name, int type, int state, int cpuNum, int memSize, int storageSize, int curPage, int pageSize) throws Exception;

  /**
   * 高级搜索数
   * @param name 模板名称
   * @param type 模板类型
   * @param state 模板状态
   * @param cpuNum 处理器个数
   * @param memSize 内存大小
   * @param storageSize 磁盘大小
   * @return 模板数
   * @throws Exception
   */
  public int advancedSearchCount(String name, int type, int state, int cpuNum, int memSize, int storageSize) throws Exception;
  /**
   * 修改模板状态
   * @param templateId
   * @param state
   * @return
   * @throws DataAccessException
   */
  public int upateTemplateState(int templateId, int state)
	throws DataAccessException;
  
  
  /**
   * 修改小型机模板
   * @param template
   * @throws Exception
   */
  void updateTemplate(final TTemplateMCBO template)
	throws Exception;

	/**
	 * 特殊模板申请创建申请模板
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	public TTemplateMCBO creatSpecalMCTemplate(TVmInfo vminfo,TUserBO user,TTemplateMCBO template,Product product) throws SCSException;
	public int insertTemplateAndProduct(TTemplateMCBO template,Product product) throws Exception ;
}
