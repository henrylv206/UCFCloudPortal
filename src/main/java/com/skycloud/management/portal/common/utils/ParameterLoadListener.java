package com.skycloud.management.portal.common.utils;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.parameters.entity.Parameters;



public class ParameterLoadListener implements ServletContextListener{
	private static Logger log = Logger.getLogger("system");
	private static ApplicationContext context;
	private ServletContext servletcontext = null;
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {			
			servletcontext = sce.getServletContext();
			if (context == null) {
				context = WebApplicationContextUtils.getWebApplicationContext(servletcontext);
			}
			ISysParametersDao parametersDao = (ISysParametersDao) context.getBean("parametersDao");
			List<Parameters> parameterlist = parametersDao.findAll();
			JSONArray parametersjson = JSONArray.fromObject(parameterlist);		

			servletcontext.setAttribute("parameters", parametersjson);
			log.debug("get parameters : " + parametersjson);			
			//考虑重新加载??
			log.info("load system parameters OK");
		} catch (Exception e) {
			log.error("load system parameters error...");
			e.printStackTrace();
		}
	}
	
	
}
