/**
 * 2012-1-18 下午02:53:34 $Id:shixq
 */
package com.skycloud.management.portal.front.mall.entity;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;

/**
 * @author shixq
 * @version $Revision$ 下午02:53:34
 */
@XmlRootElement
public class VMMonitorInfo extends BaseResp {

	private long vmId;

	private String state;

	private String vmName;

	private String timeStamp;// 时间

	private String cpuNum; // CPU利用率

	private String ipAddress;

	private String templateName;

	private String hypervisor;

	private String vmType;

	private int cpuHz;

	private float cpuUt;// cpu占有率

	private float memAllocated;

	private float memTotal;

	private String networkRead; // 网络入口带宽速度

	private String networkWrite; // 网络出口带宽速度

	private String diskRead;// 系统磁盘读取速率

	private String diskWrite; // 系统磁盘写入速率

	private String harddiskRead;// 系统磁盘读取速率

	private String harddiskWrite; // 系统磁盘写入速率
	private float memUt; // 内存利用率

	public long getVmId() {
		return vmId;
	}

	public void setVmId(long vmId) {
		this.vmId = vmId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(String cpuNum) {
		this.cpuNum = cpuNum;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public String getVmType() {
		return vmType;
	}

	public void setVmType(String vmType) {
		this.vmType = vmType;
	}

	public int getCpuHz() {
		return cpuHz;
	}

	public void setCpuHz(int cpuHz) {
		this.cpuHz = cpuHz;
	}

	public float getCpuUt() {
		return cpuUt;
	}

	public void setCpuUt(float cpuUt) {
		this.cpuUt = cpuUt;
	}

	public float getMemAllocated() {
		return memAllocated;
	}

	public void setMemAllocated(float memAllocated) {
		this.memAllocated = memAllocated;
	}

	public float getMemTotal() {
		return memTotal;
	}

	public void setMemTotal(float memTotal) {
		this.memTotal = memTotal;
	}

	public String getNetworkRead() {
		return networkRead;
	}

	public void setNetworkRead(String networkRead) {
		this.networkRead = networkRead;
	}

	public String getNetworkWrite() {
		return networkWrite;
	}

	public void setNetworkWrite(String networkWrite) {
		this.networkWrite = networkWrite;
	}

	public String getDiskRead() {
		return diskRead;
	}

	public void setDiskRead(String diskRead) {
		this.diskRead = diskRead;
	}

	public String getDiskWrite() {
		return diskWrite;
	}

	public void setDiskWrite(String diskWrite) {
		this.diskWrite = diskWrite;
	}


	public String getHarddiskRead() {
		return harddiskRead;
	}


	public void setHarddiskRead(String harddiskRead) {
		this.harddiskRead = harddiskRead;
	}


	public String getHarddiskWrite() {
		return harddiskWrite;
	}


	public void setHarddiskWrite(String harddiskWrite) {
		this.harddiskWrite = harddiskWrite;
	}

	public float getMemUt() {
		return memUt;
	}

	public void setMemUt(float memUt) {
		this.memUt = memUt;
	}

}
