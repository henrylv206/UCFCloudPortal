package com.skycloud.management.portal.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import freemarker.template.utility.StringUtil;

public class ParameterManager {
  private static Logger log = Logger.getLogger("system");
  private static JSONArray parametersjson;
  private static ParameterManager parameterManager;
  public static HashMap<String, JSONObject> parameterMap = new HashMap<String, JSONObject>();

  public synchronized static ParameterManager getInstance() {
    if (null == parameterManager) {
      parameterManager = new ParameterManager();
    }

    return parameterManager;
  }

  private ParameterManager() {
    getConfig();
  }

  private static HashMap<String, JSONObject> getConfig() {
    if (null == parametersjson) {
      ServletContext servletContext = ServletActionContext.getServletContext();
      Object params = servletContext.getAttribute("parameters");
      parametersjson = (JSONArray) params;
    }
    HashMap<String, JSONObject> map = getParameterMap();
    return map;
  }

  public static HashMap<String, JSONObject> getParameterMap() {
    if (null == parameterMap || parameterMap.isEmpty()) {
      if (null != parametersjson && !parametersjson.isEmpty()) {
        for (int i = 0; i < parametersjson.size(); i++) {
          JSONObject jparam = JSONObject.fromObject(parametersjson.get(i));
          if (jparam.containsKey("type")) {
            String type = jparam.getString("type");
            if (null != type && !type.isEmpty()) {
              parameterMap.put(type, jparam);
            }
          }
        }
      }
    }
    return parameterMap;
  }

  public String getValue(String type) {
    String result = "";
    JSONObject paramJson = parameterMap.get(type);
    if (paramJson != null && paramJson.containsKey("value")) {
      result = paramJson.getString("value");
    }
    return result;
  }

  public String getComment(String type) {
    String result = "";
    JSONObject paramJson = parameterMap.get(type);
    if (paramJson.containsKey("comment")) {
      result = paramJson.getString("comment");
    }
    return result;
  }

  public String getValueForCombox(String type) {
    String result = "";
    List<Map<String, String>> options = new ArrayList<Map<String, String>>();
    String value = this.getValue(type);
    if (null != value && !value.equals("")) {
      if (value.contains("|")) {
        String[] arr = StringUtil.split(value, '|');
        if (null != arr && arr.length > 0) {
          for (int i = 0; i < arr.length; i++) {
            Map<String, String> option = new HashMap<String, String>();
            option.put("value", arr[i]);
            option.put("text", arr[i]);
            options.add(option);
          }
        }

      } else {
        Map<String, String> option = new HashMap<String, String>();
        option.put("value", value);
        option.put("text", value);
        options.add(option);
      }
      result = JSONArray.fromObject(options).toString();
    }
    return result;
  }

  public String getVlaueAndCommentForCombox(String type) throws Exception {
    List<Map<String, String>> options = new ArrayList<Map<String, String>>();
    String value = this.getValue(type);
    String comment = this.getComment(type);
    String[] comments = null;
    if (null != comment && !comment.equals("")) {
      if (comment.contains("|")) {
        comments = StringUtil.split(comment, '|');
      }
    }

    if (null != value && !value.equals("")) {
      if (value.contains("|")) {
        String[] arr = StringUtil.split(value, '|');
        if ((null != arr && arr.length > 0) && (null != comments && comments.length > 0) && (arr.length == comments.length)) {
          for (int i = 0; i < arr.length; i++) {
            Map<String, String> option = new HashMap<String, String>();
            option.put("value", arr[i]);
            option.put("text", comments[i]);
            options.add(option);
          }
        } else {
          throw new Exception("Parameter is missing or invalid");
        }
      }
    }

    return JSONArray.fromObject(options).toString();
  }

  public String getCommentForCombox(String type) {
    List<Map<String, String>> options = new ArrayList<Map<String, String>>();
    String value = this.getComment(type);
    if (null != value && !value.equals("")) {
      if (value.contains("|")) {
        String[] arr = StringUtil.split(value, '|');
        if (null != arr && arr.length > 0) {
          for (int i = 0; i < arr.length; i++) {
            Map<String, String> option = new HashMap<String, String>();
            option.put("value", arr[i]);
            option.put("text", arr[i]);
            options.add(option);
          }
        }
      }
    }
    return JSONArray.fromObject(options).toString();
  }

  public int getIntForValue(String type, int defaultValue) {
    try {
      String value = this.getValue(type);
      if (value == null) {
        return defaultValue;
      }
      return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public boolean getBooleanForValue(String key) {
    return this.getBooleanForValue(key, true);
  }

  public boolean getBooleanForValue(String type, boolean defaultValue) {
    try {
      String value = this.getValue(type);
      if (value == null) {
        return defaultValue;
      }
      return Boolean.valueOf(value.trim());
    } catch (RuntimeException e) {
      return defaultValue;
    }
  }

  public boolean containsKey(String type) {
    return parametersjson.contains(type);
  }

  public static JSONArray getParametersjson() {
    return parametersjson;
  }

  public static void setParametersjson(JSONArray parametersjson) {
    ParameterManager.parametersjson = parametersjson;
  }

  public static void setParameterMap(HashMap<String, JSONObject> parameterMap) {
    ParameterManager.parameterMap = parameterMap;
  }

}
