/**
 * 
 */
package com.skycloud.management.portal.service;

import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class HttpConnector {
  private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
  private static HttpClient client;
  private static Logger log = Logger.getLogger("system");

  static {
    client = new HttpClient(connectionManager);
  }

  public static String doGetRequest(String myConnection) {
    GetMethod method = null;
    String body = "";
    int resCode = 200;
    try {
      connectionManager.closeIdleConnections(60000);
      client.getParams().setConnectionManagerTimeout(10000);
      method = new GetMethod(myConnection);
      // set retry policy
      method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));

      client.executeMethod(method);
      int responseStatus = method.getStatusCode();
      // method.abort();
      if (responseStatus == 500) {
        log.info("connected error with code " + responseStatus);
        resCode = 500;
      } else if (responseStatus == 431) {
        resCode = 431;
      }
      body = new String(method.getResponseBody());
      log.info("connected...." + resCode);
      log.info("HttpConnections in pool :  " + connectionManager.getConnectionsInPool() + "/"
          + connectionManager.getParams().getDefaultMaxConnectionsPerHost());
    } catch (Exception ex) {
      ex.printStackTrace();
      resCode = 500;
    } finally {
      try {
        method.releaseConnection();
      } catch (Exception e) {
      }
    }

    return body;
  }

  public static String request(String uri) throws SocketTimeoutException {
    GetMethod method = null;
    String body = null;
    try {
      connectionManager.closeIdleConnections(60 * 1000);
      // 获取连接超时
      client.getParams().setConnectionManagerTimeout(3 * 1000);
      method = new GetMethod(uri);
      // 失败不重试
      method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
      // 访问超时
      method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20 * 1000);
      int status = client.executeMethod(method);
      body = new String(method.getResponseBody(), "UTF-8");
      if (status >= 300 || status < 200)
        log.error("status:" + status + ", response body: " + body);
    } catch (java.net.SocketTimeoutException timeout) {
      log.error(timeout.getMessage());
      method.abort();
      throw timeout;
    } catch (Exception ex) {
      log.error(uri, ex);
    } finally {
      try {
        method.releaseConnection();
      } catch (Exception e) {
      }
    }
    return body;
  }
}
