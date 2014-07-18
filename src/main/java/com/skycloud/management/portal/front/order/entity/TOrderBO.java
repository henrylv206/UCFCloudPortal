package com.skycloud.management.portal.front.order.entity;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;

/**
 * TOrder entity.
 *
 * @author hefk
 */

public class TOrderBO implements java.io.Serializable {

	/**
	 * 订单信息实体对象
	 */
	private static final long serialVersionUID = 6194574719224981059L;

	private int orderId; // 订单号

	private int type; // 订单类型；1：新申请；2：修改申请；3：删除申请

	private int orderApproveLevelState; // 订单审批状态。该状态对应角色表中的ROLE_APPROVE_LEVEL。
	                                    // eg：状态为1的订单由ROLE_APPROVE_LEVEL为2的用户审批

	private int state; // 订单状态；0：购物车状态；1：申请状态；2：高级用户审核状态；
	                   // 3：管理员审核状态；4：高级管理员审核状态；5：拒绝状态

	private String orderCode; // 订单编码

	private long zoneId; // 数据中心ID

	private long clusterId; // 集群ID

	private int creatorUserId; // 下单人ID

	private String createUserName; // 下单人名称

	private Date createDt; // 下单时间

	private Date lastupdateDt; // 最后修改时间

	private int instanceInfoId; // 资源实例ID

	private int serviceInstanceId; // 服务实例ID

	private int cpuNum; // CPU个数

	private int memorySize; // 内存大小

	private int storageSize; // 存储大小

	private Integer templateId;

	private Integer productId;

	private String reason; // 申请原因

	private String resourceInfo; // 变更内容

	private List<TInstanceInfoBO> instanceInfos;

	private List<TServiceInstanceBO> serviceInstances;// 订单包含的服务实例列表

	private double price; // 价格

	/** default constructor */
	public TOrderBO() {
	}

	public TOrderBO(int type, int orderApproveLevelState, int state, String orderCode, long zoneId, long clusterId, int creatorUserId,
	                String createUserName, Date createDt, Date lastupdateDt, int instanceInfoId, int cpuNum, int memorySize, int storageSize,
	                String reason, int serviceInstanceId) {
		super();
		this.type = type;
		this.orderApproveLevelState = orderApproveLevelState;
		this.state = state;
		this.orderCode = orderCode;
		this.zoneId = zoneId;
		this.clusterId = clusterId;
		this.creatorUserId = creatorUserId;
		this.createUserName = createUserName;
		this.createDt = createDt;
		this.lastupdateDt = lastupdateDt;
		this.instanceInfoId = instanceInfoId;
		this.cpuNum = cpuNum;
		this.memorySize = memorySize;
		this.storageSize = storageSize;
		this.reason = reason;
		this.serviceInstanceId = serviceInstanceId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrderApproveLevelState() {
		return orderApproveLevelState;
	}

	public void setOrderApproveLevelState(int orderApproveLevelState) {
		this.orderApproveLevelState = orderApproveLevelState;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public long getZoneId() {
		return zoneId;
	}

	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getLastupdateDt() {
		return lastupdateDt;
	}

	public void setLastupdateDt(Date lastupdateDt) {
		this.lastupdateDt = lastupdateDt;
	}

	public int getInstanceInfoId() {
		return instanceInfoId;
	}

	public void setInstanceInfoId(int instanceInfoId) {
		this.instanceInfoId = instanceInfoId;
	}

	public int getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(int serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setInstanceInfos(List<TInstanceInfoBO> instanceInfos) {
		this.instanceInfos = instanceInfos;
	}

	public List<TInstanceInfoBO> getInstanceInfos() {
		return instanceInfos;
	}

	public String getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(String resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public List<TServiceInstanceBO> getServiceInstances() {
		return serviceInstances;
	}

	public void setServiceInstances(List<TServiceInstanceBO> serviceInstances) {
		this.serviceInstances = serviceInstances;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getTemplateId() {
		return templateId==null?0:templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getProductId() {
		int myproductId = (productId==null)?0:productId;
		return myproductId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

}