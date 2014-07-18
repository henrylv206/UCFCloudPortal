package com.skycloud.management.portal.front.order.entity;
import java.util.Date;

/**
 * 历史订单
 * tableName: T_SCS_ORDER_HISTORY
 * TOrderHistoryBO entity. 
 * @author hefk
 */


public class TOrderHistoryBO implements java.io.Serializable {

	
	/**
	 * 创建人：   何福康    
	 * 创建时间：2012-6-15  下午01:45:00
	 */
			
	private static final long serialVersionUID = 1L;

	private int id;               //主键
	private int templateId;    //模板ID
	private int instanceInfoId;     //实例ID
	private int productId;     //服务模板ID
	private int orderId;         //订单ID
	private Date createDt;     //创建日期
	
	public TOrderHistoryBO(){
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

    
	public int getInstanceInfoId() {
		return instanceInfoId;
	}



	public void setInstanceInfoId(int instanceInfoId) {
		this.instanceInfoId = instanceInfoId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

    
	
	
	

}
