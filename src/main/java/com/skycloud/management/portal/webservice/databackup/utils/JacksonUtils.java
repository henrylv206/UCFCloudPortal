package com.skycloud.management.portal.webservice.databackup.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Json解析工具
  *<dl>
  *<dt>类名：JsonUtilsL</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-9  下午01:32:56</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class JacksonUtils {
	  private static final ObjectMapper objMapper = new ObjectMapper();

	  	public static ObjectMapper getObjectMapper(){
	  		return objMapper;
	  	}
	    public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass)
	    throws JsonMappingException, JsonParseException, IOException {
	        return objMapper.readValue(jsonAsString, pojoClass);
	    }

	    public static <T> Object fromJson(Reader reader, Class<T> pojoClass)
	    throws JsonParseException, IOException
	    {
	        return objMapper.readValue(reader, pojoClass);
	    }

	    public static String toJson(Object pojo)
	    throws JsonMappingException, IOException {
	      
	        return objMapper.writeValueAsString(pojo);
	    }

	    public static void toJson(Object pojo, Writer writer)
	    throws JsonMappingException, IOException {	      
	    	objMapper.writeValue(writer, pojo);
	    }

}
