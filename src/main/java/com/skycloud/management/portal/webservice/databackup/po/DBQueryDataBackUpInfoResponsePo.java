package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dataBackups")
public class DBQueryDataBackUpInfoResponsePo {
	
	private DataBackUp dataBackup ;

	public DataBackUp getDataBackup() {
		return dataBackup;
	}

	public void setDataBackup(DataBackUp dataBackup) {
		this.dataBackup = dataBackup;
	}
	
	

}
