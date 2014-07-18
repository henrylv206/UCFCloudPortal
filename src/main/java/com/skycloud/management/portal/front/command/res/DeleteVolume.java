package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class DeleteVolume extends BaseCommandPo {

  public static final String COMMAND = "deleteVolume";
  public static final String ID = "id";
  
  
  private int id;
  
  public DeleteVolume() {
    super(COMMAND);
  }
  public DeleteVolume(int id) {
    super(COMMAND);
    this.setParameter(ID, id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
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
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[DestroyVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			DeleteVolume po = (DeleteVolume)JsonUtil.getObject4JsonString(tempJson, DeleteVolume.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[DeleteVolume]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[DeleteVolume]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
