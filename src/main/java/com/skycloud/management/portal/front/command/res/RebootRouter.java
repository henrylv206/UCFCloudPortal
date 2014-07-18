package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class RebootRouter extends BaseCommandPo {

  public static final String COMMAND = "rebootRouter";
  public static final String ID = "id";

  private String id ="";

  public RebootRouter(String id) {
    super(COMMAND);
    this.setId(id);
  }
  public RebootRouter() {
	    super(COMMAND);
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    this.setParameter(ID, id);
  }
  @Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
//		return super.fromPoToJsonStrSupportValueIsNull(po);
		return null;
	}
	public static void main (String arg[]){
		RebootRouter root = new RebootRouter ();
		System.out.println(root.getParameter(root));
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[RebootRouter]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			RebootRouter po = (RebootRouter)JsonUtil.getObject4JsonString(tempJson, RebootRouter.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[RebootRouter]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[RebootRouter]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
