/**
 * 2012-1-16  下午02:24:56  $Id:shixq
 */
package com.skycloud.management.portal.admin.template.util;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ParameterManager;

/**
 * @author shixq
 * @version $Revision$ 下午02:24:56
 */
public class TemplateUtils {

  public static String getResourceTypesByCodes(String resourceTypeCodes) {
    if (null == resourceTypeCodes) {
      return "";
    }
    String[] arrCodes = resourceTypeCodes.split(",");
    StringBuilder bd = new StringBuilder();
    for (int i = 0; i < arrCodes.length; i++) {
      String code = arrCodes[i];
      String name = ConstDef.RESOURCE_TYPE_MAP.get(code);
      bd.append(name);
      if (i != arrCodes.length - 1) {
        bd.append(",");
      }
    }
    return bd.toString();
  }
  
  public static String getPolicyByCode(String code) throws Exception{
	  String policys = ParameterManager.getInstance().getVlaueAndCommentForCombox(ConstDef.COMBOX_LOADBALANCE_POLICY);
	  String _text = "";
	  if(null != policys && !policys.isEmpty()){
			JSONArray arr = JSONArray.fromObject(policys);
			if(null != arr && !arr.isEmpty()){
				for(int i=0;i<arr.size();i++){
					JSONObject joption = JSONObject.fromObject(arr.get(i));	
					if(joption.getString("value").equals(code)){
						_text = joption.getString("text");
					}
				}
			}				
	  }
	  return _text;
  }

}
