package com.skycloud.management.portal.webservice.databackup.po;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;
/**
 * 中软接口获取Elaster卷列表信息命令对象实体类
  *<dl>
  *<dt>类名：DBListVolumes</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:29:11</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DBListVolumesCommandPo extends BaseCommandPo{
	
	public static final String COMMAND = "listVolumes";
	
	public static final String ID = "id";
	
	public static final String VIRTUALMACHINEID = "virtualmachineid";
	
	private static final String TYPE = "type";
	
	private String id;
	
	private String virtualmachineid ;
	
	private String type ;

	public DBListVolumesCommandPo(){
		 super(COMMAND);
	}

	
	
	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
		this.setParameter(TYPE, type);
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
		this.setParameter(ID, id);
	}



	public String getVirtualmachineid() {
		return virtualmachineid;
	}



	public void setVirtualmachineid(String virtualmachineid) {
		this.virtualmachineid = virtualmachineid;
		this.setParameter(VIRTUALMACHINEID, virtualmachineid);
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
				throw new ServiceException("解析Json传失败[CreateVolume]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			DBListVolumesCommandPo po = (DBListVolumesCommandPo)JsonUtil.getObject4JsonString(tempJson, DBListVolumesCommandPo.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[DBListVolumes]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[DBListVolumes]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}
