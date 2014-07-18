package com.skycloud.management.portal.rest.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Volume {

	private int id;

	private String name;

	private String vmName;

	private String vmState;

	private int size;

	private String created;

	private String state;

	private String attached;

	private String destroyed;

	private int virtualmachineId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmState() {
		return vmState;
	}

	public void setVmState(String vmState) {
		this.vmState = vmState;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAttached() {
		return attached;
	}

	public void setAttached(String attached) {
		this.attached = attached;
	}

	public String getDestroyed() {
		return destroyed;
	}

	public void setDestroyed(String destroyed) {
		this.destroyed = destroyed;
	}

	public int getVirtualmachineId() {
		return virtualmachineId;
	}

	public void setVirtualmachineId(int virtualmachineId) {
		this.virtualmachineId = virtualmachineId;
	}

}
