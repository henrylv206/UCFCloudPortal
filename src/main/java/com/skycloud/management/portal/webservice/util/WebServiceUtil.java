/**
 * 2012-2-15  上午01:21:08  $Id:shixq
 */
package com.skycloud.management.portal.webservice.util;

/**
 * @author shixq
 * @version $Revision$ 上午01:21:08
 */
public class WebServiceUtil {

  public static boolean isNotNullString(String str) {
    if (str != null && !"".equals(str) && "0".equals(str)) {
      return true;
    }
    return false;
  }

}
