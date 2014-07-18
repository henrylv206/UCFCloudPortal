package com.skycloud.management.portal.front.mall.vo;

import java.io.Serializable;

/**
 * 我的拓扑图数据的关系VO
 * @author ninghao
 * @time 2012-09-05
 * @version 1.2
 */
public class RelationVO implements Serializable 
{
	private static final long serialVersionUID = 5183426407900666222L;

	private int sid; 	 // 关系编号
	private int fromId;	 // 连接起 始资源编号
	private int fromPort;// 连接起始资源端口
	private int toId;		 // 连接结束资源编号
	private int toPort;	 // 连接结 束资源端口
	
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getFromPort() {
		return fromPort;
	}
	public void setFromPort(int fromPort) {
		this.fromPort = fromPort;
	}
	public int getToPort() {
		return toPort;
	}
	public void setToPort(int toPort) {
		this.toPort = toPort;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public int getToId() {
		return toId;
	}
	public void setToId(int toId) {
		this.toId = toId;
	}

}
