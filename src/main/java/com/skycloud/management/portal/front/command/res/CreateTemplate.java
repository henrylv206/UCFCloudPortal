package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;
/**
 * 
  *<dl>
  *<dt>类名：CreateTemplate</dt>
  *<dd>描述: 创建模板实体类</dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2011-12-6  下午04:58:27</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class CreateTemplate extends BaseCommandPo {

	public static final String COMMAND = "createTemplate";
	
	public static final String DISPLAYTEXT = "displaytext";
	
	public static final String NAME =  "name";
	
	public static final String OSTYPEID = "ostypeid";
	
	public static final String ISFEATUREED = "isfeatured";
	
	public static final String ISPUBLIC = "ispublic";
	
	public static final String PASSWORDENABLED = "passwordenabled";
	
	public static final String REQUIRESHVM = "requireshvm" ;
	
	public static final String SNAPSHOTID = "snapshotid" ;
	
	public static final String VOLUMEID = "volumeid";
	
	public static final String BITS = "bits";
		
	/**
	 * the display text of the template. This is usually used for display purposes
	 * Not Null
	 */
	private String displaytext;
	/**
	 * Not Null
	 * the name of the template
	 */
	private String name;
	/**
	 * Not Null
	 * the ID of the OS Type that best represents the OS of this template
	 */
	private int ostypeid = 138;
	
	private int snapshotid;
	/**
	 * the ID of the disk volume the template is being created from
	 * Null
	 */
	private int volumeid ; 

	public String getDisplaytext() {
		return displaytext;
	}



	public void setDisplaytext(String displaytext) {
		this.setParameter(displaytext, displaytext);
		this.displaytext = displaytext;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.setParameter(NAME, name);
		this.name = name;
	}



	public int getOstypeid() {
		return ostypeid;
	}



	public void setOstypeid(int ostypeid) {
		this.setParameter(OSTYPEID, ostypeid);
		this.ostypeid = ostypeid;
	}





	public int getSnapshotid() {
		return snapshotid;
	}



	public void setSnapshotid(int snapshotid) {
		this.setParameter(SNAPSHOTID, snapshotid);
		this.snapshotid = snapshotid;
	}



	public int getVolumeid() {
		return volumeid;
	}



	public void setVolumeid(int volumeid) {
		this.setParameter(VOLUMEID, volumeid);
		this.volumeid = volumeid;
	}


	public CreateTemplate (){
		super(COMMAND);
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

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		// TODO Auto-generated method stub
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new RuntimeException("Exceucte [AttachIso] method: fromJsonToOperatePo：paramter is null");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			CreateTemplate po = (CreateTemplate)JsonUtil.getObject4JsonString(tempJson, CreateTemplate.class);
			return po;
		}catch(Exception e){
			logger.error("Execute [CreateTemplate] method :fromJsonToOperatePo Exception：",e);
			throw new RuntimeException("Execute [CreateTemplate] method :fromJsonToOperatePo Exception："+e.getMessage());
		}
	}
}
