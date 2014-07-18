package com.skycloud.management.portal.front.sg.service;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.sg.entity.SGRule;

/**
 * 安全组规则业务接口
 * 
 * @author jiaoyz
 */
public interface ISGRuleService {

  public enum SG_RULE_STATUS {
    ON(1), OFF(2), ING(0);
    private final int status;

    private SG_RULE_STATUS(int status) {
      this.status = status;
    }

    public int getValue() {
      return status;
    }
  }

  public enum SG_RULE_OPERATE {
    CREATE(1), REMOVE(0);
    private final int operate;

    private SG_RULE_OPERATE(int operate) {
      this.operate = operate;
    }

    public int getValue() {
      return operate;
    }
  }

  public String SGID_PREFIX = "CIDC-R-01-001-SG-";

  /**
   * 创建规则
   * 
   * @param rule
   *          规则对象
   * @return 规则ID
   * @throws Exception
   */
  public int createRule(SGRule rule) throws Exception;

  /**
   * 根据ID获取规则
   * 
   * @param id
   *          规则ID
   * @return 规则对象
   * @throws Exception
   */
  public SGRule getRuleById(int id) throws Exception;

  /**
   * 删除规则
   * 
   * @param id
   *          规则ID
   * @throws Exception
   */
  public void deleteRule(int id) throws Exception;

  /**
   * 更新规则
   * 
   * @param rule
   *          规则对象
   * @throws Exception
   */
  public void updateRule(SGRule rule) throws Exception;

  /**
   * 获取规则列表
   * 
   * @param carrier
   *          条件载体
   * @param curPage
   *          当前页码
   * @param pageSize
   *          每页条数
   * @return 规则列表
   * @throws Exception
   */
  public List<SGRule> getRuleList(SGRule carrier, int curPage, int pageSize) throws Exception;

  /**
   * 获取规则数
   * 
   * @param carrier
   *          条件载体
   * @return 规则数
   * @throws Exception
   */
  public int getRuleListCount(SGRule carrier) throws Exception;

  /**
   * 后台任务更新状态
   * 
   * @author shixq
   * @create-time 2012-3-26 下午03:22:33
   * @version $Id:$
   */
  int updateRuleJob(SGRule carrier) throws SCSException;

  /**
   * 根据instanceId获取规则
   * 
   * @param id
   *          条件载体
   * @return 规则列表
   * @throws Exception
   */
  public List<SGRule> getRuleListByInstanceId(String id) throws Exception;

  /**
   * 用户注销，资源回收   * 
   * @param id
   *          条件载体
   * @throws Exception
   */
  public void deleteRuleForDestory(int id) throws Exception;

  /**
   * 获取用户可以用于设置域间策略的公网ip
   * @param userId 用户id
   * @return 公网ip列表
   * @throws Exception
   */
  public List<String> getPublicIpByUser(int userId) throws Exception;

  /**
   * 获取用户可以用于设置域间策略的私网ip
   * @param userId 用户id
   * @return 私网ip列表
   * @throws Exception
   */
  public List<String> getLocalIpByUser(int userId) throws Exception;

  public List<SGRule> getRuleListByBindIP(SGRule carrier) throws Exception;
}
