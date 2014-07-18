package com.skycloud.management.portal.admin.sysmanage.entity;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
  *<dl>
  *<dt>类名：PublicIPBO</dt>
  *<dd>描述: 公网IP实体类</dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-2  下午04:04:19</dd>
  *<dd>创建人： 何福康</dd>
  *</dl>
 */
public class TPublicIPBO  implements java.io.Serializable {
	
	/**
	 * 创建人：   何福康    
	 * 创建时间：2012-2-2  下午04:17:30
	 */
			
	private static final long serialVersionUID = 2992904785815346335L;
	private int id;
	private String ipAddress;
	private int status;
	private int ipType;
	private int serviceProvider;
	private int creatorUserId;
	private Date createdDate;
	private String createDtString;
	private Date lastupdateDate;
	private int instanceInfoId;
	private int bandwidthId;
	private String privateIp;
	
	//显示增加
	private String ipTypeName;
	private String serviceProviderName;
	
	/**
	 * default constructor
	 * 创建人：   何福康  
	 * 创建时间：2012-2-2  下午04:14:14
	 */
	public TPublicIPBO() {
		super();
	}

	public TPublicIPBO(int id, String ipAddress, int status, int ipType,
			int serviceProvider, int creatorUserId, Date createdDate,
			Date lastupdateDate, int instanceInfoId) {
		super();
		this.id = id;
		this.ipAddress = ipAddress;
		this.status = status;
		this.ipType = ipType;
		this.serviceProvider = serviceProvider;
		this.creatorUserId = creatorUserId;
		this.createdDate = createdDate;
		this.lastupdateDate = lastupdateDate;
		this.instanceInfoId = instanceInfoId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIpType() {
		return ipType;
	}
	public void setIpType(int ipType) {
		if(ipType==0){
			this.setIpTypeName("IPV4");
		}else if(ipType==1){
			this.setIpTypeName("IPV6");
		}
		this.ipType = ipType;
	}
	public int getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(int serviceProvider) {
	    if(serviceProvider==0){
            this.setServiceProviderName("中国电信");
        }else if(serviceProvider==1){
            this.setServiceProviderName("中国联通");
        }else if(serviceProvider==2){
            this.setServiceProviderName("中国移动");
        }else if(serviceProvider==3){
            this.setServiceProviderName("中国铁通");
        }
		this.serviceProvider = serviceProvider;
	}
	public int getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.setCreateDtString((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(createdDate)); 
		this.createdDate = createdDate;
	}
	public Date getLastupdateDate() {
		return lastupdateDate;
	}
	public void setLastupdateDate(Date lastupdateDate) {
		this.lastupdateDate = lastupdateDate;
	}
	public int getInstanceInfoId() {
		return instanceInfoId;
	}
	public void setInstanceInfoId(int instanceInfoId) {
		this.instanceInfoId = instanceInfoId;
	}

	public void setCreateDtString(String createDtString) {
		this.createDtString = createDtString;
	}

	public String getCreateDtString() {
		return createDtString;
	}

	public String getIpTypeName() {
		return ipTypeName;
	}

	public void setIpTypeName(String ipTypeName) {
		this.ipTypeName = ipTypeName;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public int getBandwidthId() {
		return bandwidthId;
	}

	public void setBandwidthId(int bandwidthId) {
		this.bandwidthId = bandwidthId;
	}

  public String getPrivateIp() {
    return privateIp;
  }

  public void setPrivateIp(String privateIp) {
    this.privateIp = privateIp;
  }

}
