package com.skycloud.tezz.query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.skycloud.tezz.commons.Pagination;
import com.skycloud.tezz.dao.ToptenDAO;
import com.skycloud.tezz.model.ToptenInfo;

public class ToptenServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ToptenServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		String start = request.getParameter("startQuery");
		String end = request.getParameter("endQuery");
		String fytype=request.getParameter("fuwutype");
		String page = request.getParameter("page");
		String pageSize = request.getParameter("pageSize");
		Pagination pagin=new Pagination();
		if (page != null && !"".equals(page)) {
			 pagin.setPage(Integer.parseInt(page));
		}
	    if (pageSize != null && !"".equals(pageSize)) {
	    	pagin.setPageSize(Integer.parseInt(pageSize));
		}else{
			pagin.setPageSize(pagin.getPageSize());
		}
		String typename="";
		if("cpu".equals(fytype)){
			typename="CPU";
		}
		if("mem".equals(fytype)){
			typename="内存";
		}
		if("nwread".equals(fytype)){
			typename="磁盘读";
		}
		if("nwwrite".equals(fytype)){
			typename="磁盘写";
		}
		ToptenDAO dao =new ToptenDAO();
		List<ToptenInfo> list = dao.getToptenInfoList(start, end, fytype,pagin);
		HttpSession session = request.getSession();
		request.setAttribute("start", start);
		request.setAttribute("end", end);
		session.setAttribute("page", pagin);
		session.setAttribute("type", typename);
		session.setAttribute("list", list);
		response.sendRedirect("Tezz/topten.jsp");
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
