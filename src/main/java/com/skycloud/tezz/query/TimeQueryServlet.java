package com.skycloud.tezz.query;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mondrian.olap.CacheControl;
import mondrian.rolap.CacheControlImpl;

import com.skycloud.tezz.dao.UsersnumDAO;
import com.skycloud.tezz.db.DBOperation;

public class TimeQueryServlet extends HttpServlet {

	/**
	 * This Servlet for Time Query  
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			int re=0;
			String type = request.getParameter("type");
			String start = request.getParameter("startQuery");
			String end = request.getParameter("endQuery");
			Integer uid=null;
			CacheControl cc = new CacheControlImpl();
			cc.flushSchemaCache();
			//Database operation
			DBOperation operation = new DBOperation();
			if("topten".equals(type) || "service".equals(type) || "storage".equals(type) || "storageUsability".equals(type)){
				type=request.getParameter("fuwutype");
			}
			
			if("users".equals(type)){
				UsersnumDAO udao=new UsersnumDAO();
				udao.copyUsers();
				udao.updateUsers();
			}
			if("order".equals(type)||"unsubscribeorder".equals(type)){
				uid=Integer.parseInt(request.getParameter("userid"));
			}
			re=operation.GeneratingDeviceData(type, start, end,uid);
			
			//Send to page
			//config chart
			//ChartComponent cc = new ChartComponent();
			if(re==0){
				response.sendRedirect("Tezz/noresult.jsp");
			}else{
				HttpSession session =request.getSession();
				session.setAttribute("ty", type);
				response.sendRedirect("Tezz/testpage.jsp?query="+type+"&time="+(new Date()).getTime()+"&start="+start+"&end="+end);
			}
			
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
