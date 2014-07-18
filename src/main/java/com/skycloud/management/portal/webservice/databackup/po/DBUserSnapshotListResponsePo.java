package com.skycloud.management.portal.webservice.databackup.po;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="snapshots")
public class DBUserSnapshotListResponsePo {

	List<Snapshot>  snapshot ;

	public List<Snapshot> getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(List<Snapshot> snapshot) {
		this.snapshot = snapshot;
	}
	
	
}
