/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.util;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.common.utils.BeanFactoryUtil;
import com.skycloud.management.portal.service.ConfigurationLoader;

/**
 * @author SXQ
 * @version 2012-3-15 下午03:41:04
 */
public class TaskUtils {

  private static int queryCount = 0;// 任务每次执行查询读取条数，数据库默认为100
  private static int threadExcuteCount = 0;// 查询每个线程执行次数，数据库默认为5

  private static String oStoreURI;
  private static String oStorePwdURI;
  private static String oStoreUserURI;

  private static String idcURI;
  private static String idcUserName;
  private static String idcPassword;
  
  private static String idcTemplateURI;

  private static String actVM;
  private static String modifyVM;
  private static String deleteVM;
  private static String createVM;
  private static String queryVMState;

  private static String attachBS;
  private static String deleteBS;
  private static String createBS;
  private static String detachBS;
  private static String queryBS;

  private static String queryCreateBackup;
  private static String createBackup;
  private static String deleteBackup;
  private static String queryDeleteBackup;
  private static String restoreBackup;
  private static String queryRestoreBackup;

  private static String applyBandwidth;
  private static String cancelBandwidth;
  private static String modifyBandwidth;

  private static String releasePublicIP;
  private static String bindingPublicIP;
  private static String applyPublicIP;
  private static String unbindPublicIP;
  
  private static String resourceTemplate;
  
  private static String ipSanURL;
  private static String ipSanUserName;
  private static String ipSanPassword;

  public synchronized static int getQueryCount() {
    if (queryCount == 0) {
      ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
      queryCount = parametersService.getQuerycount();
    }
    return queryCount;
  }

  public synchronized static int getThreadExcuteCount() {
    if (threadExcuteCount == 0) {
      ISysParametersService parametersService = (ISysParametersService) BeanFactoryUtil.getBean("parametersService");
      threadExcuteCount = parametersService.getThreadExcuteCount();
    }
    return threadExcuteCount;
  }

  public static int calculateThreadCount() {
    int calculateThreadCount = 10;
    if (queryCount != 0 && threadExcuteCount != 0 && queryCount > threadExcuteCount && queryCount > 5 * threadExcuteCount) {
      calculateThreadCount = queryCount / threadExcuteCount;
    }
    return calculateThreadCount;
  }

  public static String getOStoreURI() {
    if (StringUtils.isBlank(oStoreURI))
      oStoreURI = ConfigurationLoader.getInstance().getProperty("ostore.uri");
    return oStoreURI;
  }
  
  public static String getOStoreURI2() {
	if (StringUtils.isBlank(oStoreURI))
	  oStoreURI = ConfigurationLoader.getInstance().getProperty("ostore2.uri");
	return oStoreURI;
  }
  
  
  public static String getOStorePwdURI() {
	    if (StringUtils.isBlank(oStorePwdURI))
	    	oStorePwdURI = ConfigurationLoader.getInstance().getProperty("ostore.pwd.uri");
	    return oStorePwdURI;
  }
  public static String getOStoreUserURI() {
	    if (StringUtils.isBlank(oStoreUserURI))
	    	oStoreUserURI = ConfigurationLoader.getInstance().getProperty("ostore.user.uri");
	    return oStoreUserURI;
}

  public static String getOStoreUserURI2() {
	    if (StringUtils.isBlank(oStoreUserURI))
	    	oStoreUserURI = ConfigurationLoader.getInstance().getProperty("ostore2.user.uri");
	    return oStoreUserURI;
}
  
  
  public static String getIdcURI() {
    if (StringUtils.isBlank(idcURI))
      idcURI = ConfigurationLoader.getInstance().getProperty("idc.uri");
    return idcURI;
  }

  public static String getIdcUserName() {
    if (StringUtils.isBlank(idcUserName))
      idcUserName = ConfigurationLoader.getInstance().getProperty("idc.username");
    return idcUserName;
  }

  public static String getIdcPassword() {
    if (StringUtils.isBlank(idcPassword))
      idcPassword = ConfigurationLoader.getInstance().getProperty("idc.password");
    return idcPassword;
  }

  public static String getRestoreBackup() {
    if (StringUtils.isBlank(restoreBackup))
      restoreBackup = ConfigurationLoader.getInstance().getProperty("idc.service.restoreBackup");
    return restoreBackup;
  }

  public static String getUnbindPublicIP() {
    if (StringUtils.isBlank(unbindPublicIP))
      unbindPublicIP = ConfigurationLoader.getInstance().getProperty("idc.service.unbindPublicIP");
    return unbindPublicIP;
  }

  public static String getModifyVM() {
    if (StringUtils.isBlank(modifyVM))
      modifyVM = ConfigurationLoader.getInstance().getProperty("idc.service.modifyVM");
    return modifyVM;
  }

  public static String getCancelBandwidth() {
    if (StringUtils.isBlank(cancelBandwidth))
      cancelBandwidth = ConfigurationLoader.getInstance().getProperty("idc.service.cancelBandwidth");
    return cancelBandwidth;
  }

  public static String getDeleteVM() {
    if (StringUtils.isBlank(deleteVM))
      deleteVM = ConfigurationLoader.getInstance().getProperty("idc.service.deleteVM");
    return deleteVM;
  }

  public static String getAttachBS() {
    if (StringUtils.isBlank(attachBS))
      attachBS = ConfigurationLoader.getInstance().getProperty("idc.service.attachBS");
    ;
    return attachBS;
  }

  public static String getCreateVM() {
    if (StringUtils.isBlank(createVM))
      createVM = ConfigurationLoader.getInstance().getProperty("idc.service.createVM");
    return createVM;
  }

  public static String getDeleteBS() {
    if (StringUtils.isBlank(deleteBS))
      deleteBS = ConfigurationLoader.getInstance().getProperty("idc.service.deleteBS");
    return deleteBS;
  }

  public static String getQueryCreateBackup() {
    if (StringUtils.isBlank(queryCreateBackup))
      queryCreateBackup = ConfigurationLoader.getInstance().getProperty("idc.service.queryCreateBackup");
    return queryCreateBackup;
  }

  public static String getCreateBackup() {
    if (StringUtils.isBlank(createBackup))
      createBackup = ConfigurationLoader.getInstance().getProperty("idc.service.createBackup");
    return createBackup;
  }

  public static String getApplyBandwidth() {
    if (StringUtils.isBlank(applyBandwidth))
      applyBandwidth = ConfigurationLoader.getInstance().getProperty("idc.service.applyBandwidth");
    return applyBandwidth;
  }

  public static String getCreateBS() {
    if (StringUtils.isBlank(createBS))
      createBS = ConfigurationLoader.getInstance().getProperty("idc.service.createBS");
    return createBS;
  }

  public static String getActVM() {
    if (StringUtils.isBlank(actVM))
      actVM = ConfigurationLoader.getInstance().getProperty("idc.service.actVM");
    return actVM;
  }

  public static String getDetachBS() {
    if (StringUtils.isBlank(detachBS))
      detachBS = ConfigurationLoader.getInstance().getProperty("idc.service.detachBS");
    return detachBS;
  }

  public static String getDeleteBackup() {
    if (StringUtils.isBlank(deleteBackup))
      deleteBackup = ConfigurationLoader.getInstance().getProperty("idc.service.deleteBackup");
    return deleteBackup;
  }

  public static String getReleasePublicIP() {
    if (StringUtils.isBlank(releasePublicIP))
      releasePublicIP = ConfigurationLoader.getInstance().getProperty("idc.service.releasePublicIP");
    return releasePublicIP;
  }

  public static String getBindingPublicIP() {
    if (StringUtils.isBlank(bindingPublicIP))
      bindingPublicIP = ConfigurationLoader.getInstance().getProperty("idc.service.bindingPublicIP");
    return bindingPublicIP;
  }

  public static String getApplyPublicIP() {
    if (StringUtils.isBlank(applyPublicIP))
      applyPublicIP = ConfigurationLoader.getInstance().getProperty("idc.service.applyPublicIP");
    return applyPublicIP;
  }

  public static String getQueryVMState() {
    if (StringUtils.isBlank(queryVMState))
      queryVMState = ConfigurationLoader.getInstance().getProperty("idc.service.queryVMState");
    return queryVMState;
  }

  public static String getQueryRestoreBackup() {
    if (StringUtils.isBlank(queryRestoreBackup))
      queryRestoreBackup = ConfigurationLoader.getInstance().getProperty("idc.service.queryRestoreBackup");
    return queryRestoreBackup;
  }

  public static String getQueryBS() {
    if (StringUtils.isBlank(queryBS))
      queryBS = ConfigurationLoader.getInstance().getProperty("idc.service.queryBS");
    return queryBS;
  }

  public static String getModifyBandwidth() {
    if (StringUtils.isBlank(modifyBandwidth))
      modifyBandwidth = ConfigurationLoader.getInstance().getProperty("idc.service.modifyBandwidth");
    return modifyBandwidth;
  }

  public static String getQueryDeleteBackup() {
    if (StringUtils.isBlank(queryDeleteBackup))
      queryDeleteBackup = ConfigurationLoader.getInstance().getProperty("idc.service.queryDeleteBackup");
    return queryDeleteBackup;
  }

  public static String getResourceTemplate() {
    if (StringUtils.isBlank(resourceTemplate))
      resourceTemplate = ConfigurationLoader.getInstance().getProperty("idc.service.resourceTemplate");
    return resourceTemplate;
  }
  
  public static String getIpSanURL() {
	    if (StringUtils.isBlank(ipSanURL))
	    	ipSanURL = ConfigurationLoader.getInstance().getProperty("ipSan.uri");
	    return ipSanURL;
  }  
  
  public static String getIpSanUserName() {
	    if (StringUtils.isBlank(ipSanUserName))
	    	ipSanUserName = ConfigurationLoader.getInstance().getProperty("ipSan.username");
	    return ipSanUserName;
  }  
  
  public static String getIpSanPassword() {
	    if (StringUtils.isBlank(ipSanPassword))
	    	ipSanPassword = ConfigurationLoader.getInstance().getProperty("ipSan.password");
	    return ipSanPassword;
  }  
  
  public static String getIdcTemplateURI() {
    if (StringUtils.isBlank(idcTemplateURI))
      idcTemplateURI = ConfigurationLoader.getInstance().getProperty("idc.template.uri");

    return idcTemplateURI;
  }
}
