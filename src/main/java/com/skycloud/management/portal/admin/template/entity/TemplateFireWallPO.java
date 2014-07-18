package com.skycloud.management.portal.admin.template.entity;

import java.io.StringWriter;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TemplateFireWallPO {

	private String TemplateID;

	private String ResourceType;

	private String MeasureMode;

	private String TemplateDesc;

	private String TemplateStatus;

	private String TemplateCreator;

	private String CreateTime;

	private BaseCloudStackResourceInfo ResourceInfo;

	public TemplateFireWallPO  (){
		
	}
	public static TemplateFireWallPO getIntance(TTemplateVMBO template){
		TemplateFireWallPO templateVdc = new TemplateFireWallPO ();
		templateVdc.setMeasureMode(template.getMeasureMode());
		templateVdc.setResourceType("CIDC-RT-SG");
		templateVdc.setTemplateDesc(template.getTemplateDesc());
		templateVdc.setTemplateCreator(String.valueOf(template.getCreatorUserId()));
		templateVdc.setTemplateStatus("1");
		templateVdc.setTemplateID(ConstDef.getVDCTemplateID(template.getType(),template.getId()));
//		if(StringUtils.isNotBlank(template.getExtendAttrJSON())){
//			templateVdc.TemplateID = template.getExtendAttrJSON();
//			templateVdc.setTemplateID(templateVdc.TemplateID);
//		}else{
//			templateVdc.TemplateID = templateVdc.countTemplateId();
//			templateVdc.setTemplateID(templateVdc.TemplateID);
//		}
		templateVdc.setCreateTime(template.getCreateTime());
		BaseCloudStackResourceInfo ResourceInfo = new BaseCloudStackResourceInfo ();
		ResourceInfo.setQuotaSize(template.getStorageSize());
		ResourceInfo.setGrade(template.getGrade());
		templateVdc.setResourceInfo(ResourceInfo);
		return templateVdc;
	}
	public static String countTemplateId(){
		String str = "";
		try {
			Random rd = new Random();
			int rdGet = 0;
			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < 8; i++) {
				rdGet = Math.abs(rd.nextInt()) % 10;
				strBuf.append(rdGet);
			}
			str = "CIDC-T-SG-"+strBuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	public String getTemplateID() {
		return TemplateID;
	}
	public static String toString (TemplateFireWallPO po){
		XStream xstream = new XStream();
		xstream.alias("ObjectStoreTemplate",TemplateFireWallPO.class);
		xstream.alias("ResourceInfo",BaseCloudStackResourceInfo.class);
		StringWriter sw = new StringWriter();
		xstream.marshal(po, new CompactWriter(sw));
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+sw.toString();
	}
	public static String getXmlTemplateId (String xml){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("ObjectStoreTemplate",TemplateFireWallPO.class);
		xstream.alias("ResourceInfo",BaseCloudStackResourceInfo.class);
		TemplateFireWallPO po = (TemplateFireWallPO)xstream.fromXML(xml);
		return po.getTemplateID();
	}
	public void setTemplateID(String templateID) {
		TemplateID = templateID;
	}

	public String getResourceType() {
		return ResourceType;
	}

	public void setResourceType(String resourceType) {
		ResourceType = resourceType;
	}

	public String getMeasureMode() {
		return MeasureMode;
	}

	public void setMeasureMode(String measureMode) {
		MeasureMode = measureMode;
	}

	public String getTemplateDesc() {
		return TemplateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		TemplateDesc = templateDesc;
	}

	public String getTemplateStatus() {
		return TemplateStatus;
	}

	public void setTemplateStatus(String templateStatus) {
		TemplateStatus = templateStatus;
	}

	public String getTemplateCreator() {
		return TemplateCreator;
	}

	public void setTemplateCreator(String templateCreator) {
		TemplateCreator = templateCreator;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public BaseCloudStackResourceInfo getResourceInfo() {
		return ResourceInfo;
	}

	public void setResourceInfo(BaseCloudStackResourceInfo resourceInfo) {
		ResourceInfo = resourceInfo;
	}

}
