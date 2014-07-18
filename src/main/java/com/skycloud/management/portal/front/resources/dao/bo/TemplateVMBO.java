/**
 * 2011-11-21  下午02:44:00  $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.dao.bo;

import java.io.Serializable;

/**
 * @author shixq
 * @version $Revision$ $Date$
 */
public class TemplateVMBO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7368306519351531462L;
  private int id;
  private String code;
  private int resource_pools_id;
  private int type;
  private String template_desc;
  private String cpufrequency;
  private int cpu_num;
  private int memory_size;
  private int storage_size;
  private String os_disk_type;
  private int os_size;
  private int veth_adaptor_num;
  private int vscsi_adaptor_num;
  private String vmos;
  private int state;
  private int creator_user_id;
  private String create_time;
  private String e_service_id;
  private String e_disk_id;
  private String e_network_id;
  private String network_desc;
  private String e_os_id;
  private String os_desc;
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public int getResource_pools_id() {
    return resource_pools_id;
  }
  public void setResource_pools_id(int resource_pools_id) {
    this.resource_pools_id = resource_pools_id;
  }
  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }
  public String getTemplate_desc() {
    return template_desc;
  }
  public void setTemplate_desc(String template_desc) {
    this.template_desc = template_desc;
  }
  public String getCpufrequency() {
    return cpufrequency;
  }
  public void setCpufrequency(String cpufrequency) {
    this.cpufrequency = cpufrequency;
  }
  public int getCpu_num() {
    return cpu_num;
  }
  public void setCpu_num(int cpu_num) {
    this.cpu_num = cpu_num;
  }
  public int getMemory_size() {
    return memory_size;
  }
  public void setMemory_size(int memory_size) {
    this.memory_size = memory_size;
  }
  public int getStorage_size() {
    return storage_size;
  }
  public void setStorage_size(int storage_size) {
    this.storage_size = storage_size;
  }
  public String getOs_disk_type() {
    return os_disk_type;
  }
  public void setOs_disk_type(String os_disk_type) {
    this.os_disk_type = os_disk_type;
  }
  public int getOs_size() {
    return os_size;
  }
  public void setOs_size(int os_size) {
    this.os_size = os_size;
  }
  public int getVeth_adaptor_num() {
    return veth_adaptor_num;
  }
  public void setVeth_adaptor_num(int veth_adaptor_num) {
    this.veth_adaptor_num = veth_adaptor_num;
  }
  public int getVscsi_adaptor_num() {
    return vscsi_adaptor_num;
  }
  public void setVscsi_adaptor_num(int vscsi_adaptor_num) {
    this.vscsi_adaptor_num = vscsi_adaptor_num;
  }
  public String getVmos() {
    return vmos;
  }
  public void setVmos(String vmos) {
    this.vmos = vmos;
  }
  public int getState() {
    return state;
  }
  public void setState(int state) {
    this.state = state;
  }
  public int getCreator_user_id() {
    return creator_user_id;
  }
  public void setCreator_user_id(int creator_user_id) {
    this.creator_user_id = creator_user_id;
  }
  public String getCreate_time() {
    return create_time;
  }
  public void setCreate_time(String create_time) {
    this.create_time = create_time;
  }
  public String getE_service_id() {
    return e_service_id;
  }
  public void setE_service_id(String e_service_id) {
    this.e_service_id = e_service_id;
  }
  public String getE_disk_id() {
    return e_disk_id;
  }
  public void setE_disk_id(String e_disk_id) {
    this.e_disk_id = e_disk_id;
  }
  public String getE_network_id() {
    return e_network_id;
  }
  public void setE_network_id(String e_network_id) {
    this.e_network_id = e_network_id;
  }
  public String getNetwork_desc() {
    return network_desc;
  }
  public void setNetwork_desc(String network_desc) {
    this.network_desc = network_desc;
  }
  public String getE_os_id() {
    return e_os_id;
  }
  public void setE_os_id(String e_os_id) {
    this.e_os_id = e_os_id;
  }
  public String getOs_desc() {
    return os_desc;
  }
  public void setOs_desc(String os_desc) {
    this.os_desc = os_desc;
  }

 

}
