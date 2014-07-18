package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

/**
 * 卸载光盘命令实体对象
  *<dl>
  *<dt>类名：DetachIso</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2011-12-31  上午10:11:34</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DetachIso extends BaseCommandPo {

	private static final String COMMAND = "detachIso";
	
	private static final String VIRTUALMACHINEID = "virtualmachineid";
	
	private int virtualmachineid;
	
	public DetachIso() {
		super(COMMAND);
		// TODO Auto-generated constructor stub
	}

	
	public int getVirtualmachineid() {
		return virtualmachineid;
	}


	public void setVirtualmachineid(int virtualmachineid) {
		this.virtualmachineid = virtualmachineid;
		this.setParameter(VIRTUALMACHINEID,virtualmachineid);
	}


	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(virtualmachineid == 0){
			throw new ServiceException("缺少必填参数: virtualmachineid为必填");
		}
		return super.fromPoToJsonStr(po);
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		// TODO Auto-generated method stub
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new RuntimeException("Exceucte [DetachIso] method: fromJsonToOperatePo：paramter is null");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			DetachIso po = (DetachIso)JsonUtil.getObject4JsonString(tempJson, DetachIso.class);
			return po;
		}catch(Exception e){
			logger.error("Execute [DetachIso] method :fromJsonToOperatePo Exception：",e);
			throw new RuntimeException("Execute [DetachIso] method :fromJsonToOperatePo Exception："+e.getMessage());
		}
	}

}
