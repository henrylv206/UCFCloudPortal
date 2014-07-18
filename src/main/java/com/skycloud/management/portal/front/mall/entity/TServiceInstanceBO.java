package com.skycloud.management.portal.front.mall.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TServiceInstanceBO {

	private int id;

	private int orderId;// 订单ID

	private int productId;// 服务模板ID

	private String serviceName;// 服务名称

	private String sName;// 服务缩略名

	private int serviceType;// 服务类型

	private String serviceTypeName;

	private String serviceDesc;// 服务描述

	private String sDesc;// 服务描述简略

	private Date createDt;// 创建时间

	private Date expiryDate;// 过期时间

	private String createDtStr;// 创建时间

	private String expiryDateStr;// 过期时间

	private int state;// 服务状态

	private int num;// 资源数量

	private String flag;// 标志(iaas,paas,saas), 默认iaas

	private int period;// 购买周期

	private String unit;// 计费单位

	// to fix bug:3408
	private double price;// 服务价格

	// 以下只显示
	private double totalPrice;// 价格合计=period*price

	private int newOrderId;// 新订单ID(修改单，删除单对应t_scs_order表的order_id)

	private int instanceState;// 修改和删除订单时，获取实例状态

	private int sum;

	private String storageSize;

	private String eInstanceId;

	private String clusterId;

	private String resCode;

	private String vmid;

	private int resourcePoolsId;

	private Integer zoneId;

	private Integer special;// 1：特殊模板；0：标准模板

	private int historyId;// >0：服务实例变更前的服务ID；0：当前服务实例

	private int historyState;// 0：当前服务实例; 1：历史服务实例
	
	private int orderFlag;//未完成订单

	public String getVmid() {
		return vmid;
	}

	public void setVmid(String vmid) {
		this.vmid = vmid;
	}

	public String geteInstanceId() {
		return eInstanceId;
	}

	public void seteInstanceId(String eInstanceId) {
		this.eInstanceId = eInstanceId;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(String storageSize) {
		this.storageSize = storageSize;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getsDesc() {
		return sDesc;
	}

	public void setsDesc(String sDesc) {
		this.sDesc = sDesc;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDtStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(createDt);
		this.createDt = createDt;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDateStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(expiryDate);
		this.expiryDate = expiryDate;
	}

	public String getCreateDtStr() {
		return createDtStr;
	}

	public void setCreateDtStr(String createDtStr) {
		this.createDtStr = createDtStr;
	}

	public String getExpiryDateStr() {
		return expiryDateStr;
	}

	public void setExpiryDateStr(String expiryDateStr) {
		this.expiryDateStr = expiryDateStr;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		this.totalPrice = (double) Math.round(this.period * this.price * 100) / 100;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getNewOrderId() {
		return newOrderId;
	}

	public void setNewOrderId(int newOrderId) {
		this.newOrderId = newOrderId;
	}

	public int getInstanceState() {
		return instanceState;
	}

	public void setInstanceState(int instanceState) {
		this.instanceState = instanceState;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId==null?0:zoneId;
	}

	public Integer getSpecial() {
		return special;
	}

	public void setSpecial(Integer special) {
		this.special = special==null?0:special;
	}


    public int getHistoryId() {
    	return historyId;
    }


    public void setHistoryId(int historyId) {
    	this.historyId = historyId;
    }


    public int getHistoryState() {
    	return historyState;
    }


    public void setHistoryState(int historyState) {
    	this.historyState = historyState;
    }

	public int getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(int orderFlag) {
		this.orderFlag = orderFlag;
	}

    

}
