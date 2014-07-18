package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="snapshots")
public class DBQuerySnapshotInfoResponsePo {

	private Snapshot snapshot;

	public Snapshot getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
}
