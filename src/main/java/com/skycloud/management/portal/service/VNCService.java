package com.skycloud.management.portal.service;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.dao.IUserManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConfigManager;

/**
 * 
 * @author shixq
 * @version $Revision$ 上午10:50:43
 */
public class VNCService {
    private static final Logger syslog = Logger.getLogger("system");

    private String username = "admin";
    private IUserManageDao userDao;

    private final static String loginURL = ConfigManager.getInstance().getString("elaster.vnc.url.login");
    private final static String accessURL = ConfigManager.getInstance().getString("elaster.vnc.url.access");
    public VNCService(){}

    public String openVM(String vmResId) {

        String returnStr = null;
        String passwd = null;
        try {
            TUserBO user = userDao.findUserByAccout(username);
            passwd = user.getPwd();
        } catch (SQLException e1) {
            syslog.error(e1.getMessage());
        }
        try {
            HttpConnector.request(loginURL + passwd);
            String htmlStr = HttpConnector.request(accessURL + vmResId);

            if (htmlStr != null && !htmlStr.equals("")) {
                if (htmlStr.indexOf("http") != -1) {
                    returnStr = htmlStr.substring(htmlStr.indexOf("http"), htmlStr.indexOf("\"></frame>"));
                    // 为以后做ip转译预留
                    // String vnc =
                    // ConfigurationLoader.getInstance().getProperty("vnc." +
                    // returnStr.substring(7, returnStr.indexOf("/ajax")));
                    // if (!Utils.isNullOrEmpty(vnc)) {
                    // returnStr = "http://" + vnc +
                    // returnStr.substring(returnStr.indexOf("/ajax"));
                    // } else
                    // returnStr = "抱歉，服务器请求错误，请联系管理员！";
                } else
                    returnStr = "服务器正在切换状态......请稍候再试！";
            } else {
                returnStr = "抱歉，服务器已经关闭，请稍候再试！";
            }
        } catch (Exception e) {
            syslog.error("======VNC请求异常====", e);
            returnStr = "抱歉，服务器请求错误，请联系管理员！";
        }
        return returnStr;
    }

    public void setUserDao(IUserManageDao userDao) {
        this.userDao = userDao;
    }

    
}
