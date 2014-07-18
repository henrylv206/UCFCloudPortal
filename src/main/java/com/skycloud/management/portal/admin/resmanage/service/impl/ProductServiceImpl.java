package com.skycloud.management.portal.admin.resmanage.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.jfree.util.Log;

import com.skycloud.management.portal.admin.resmanage.dao.IChargeUnitDao;
import com.skycloud.management.portal.admin.resmanage.dao.IProductChargeUnitDao;
import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.resmanage.dao.IProductTemplateRelationDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.entity.ProuctChargeUnit;
import com.skycloud.management.portal.admin.resmanage.entity.vo.ProductTemplateRelationBO;
import com.skycloud.management.portal.admin.resmanage.service.IProductService;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.entity.TemplateCloudStoragePo;
import com.skycloud.management.portal.admin.template.service.IMCTemplateService;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;

public class ProductServiceImpl implements IProductService {
	
	private Logger logger = LogManager.getLogger(this.getClass());

	private IProductDao productDao;

	private IProductTemplateRelationDao productTemplateRelationDao;
	
	private IVMTemplateDao VMTemplateDao;
	
	private IVMTemplateService VMTemplateService;
	
	private IMCTemplateService MCTemplateService; 

	private IProductChargeUnitDao productChargeUnitDao;

	private IChargeUnitDao chargeUnitDao;

	public IProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public IProductTemplateRelationDao getProductTemplateRelationDao() {
		return productTemplateRelationDao;
	}

	public void setProductTemplateRelationDao(IProductTemplateRelationDao productTemplateRelationDao) {
		this.productTemplateRelationDao = productTemplateRelationDao;
	}

	@Override
	public int insertProduct(Product product) throws Exception {
		if (product.getType() == 50) {
			product.setTemplateId(-1);
		}
		int result = productDao.save(product);
		product.setId(result);
		// 增加产品和模板的对应关系
		if (product.getType() == 50) {
			this.addProductTemplateRelation(product);
		}
		
		// 增加多计价单位的支持何军辉 20130313
		String priceList = product.getPriceList();
		if (priceList != null) {
			this.addProductChargeUnit(product.getId(), priceList);
		}

		return result;
	}

	public void addProductTemplateRelation(Product product) throws Exception {
		String templateIds = product.getTemplateIds();
		String templateTypes = product.getTemplateTypes();
		if (null != templateIds && !templateIds.equals("")) {
			String[] ids = templateIds.split(",");
			String[] _types = templateTypes.split(",");
			if (null != ids && ids.length > 0 && null != _types && _types.length>0) {
				for (int i = 0; i < ids.length; i++) {
					ProductTemplateRelationBO relation = new ProductTemplateRelationBO();
					relation.setProductId(product.getId());
					relation.setTemplateType(Integer.parseInt(_types[i]));
					relation.setTemplateId(Integer.parseInt(ids[i]));
					productTemplateRelationDao.createProductTemplateRelation(relation);
				}
			}
		}
	}

	@Override
	public void deleteProduct(int id, int type) throws Exception {
		productDao.delete(id);
		// 删除产品和模板的对应关系
		if (type == 50) {
			productTemplateRelationDao.deleteProductTemplateRelation(id);
		}
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		productDao.update(product);
	}

	@Override
	public void updateProduct2(Product product) throws Exception {
		productDao.update(product);
		// 修改产品和模板的对应关系,先删除,再增加
		if (product.getType() == 50) {
			productTemplateRelationDao.deleteProductTemplateRelation(product.getId());
			this.addProductTemplateRelation(product);
		}
	}

	/**
	 * 增加模板和服务
	 * @param template
	 * @param product
	 * @return
	 * @throws Exception
	 * @author zhanghuizheng
	 */
	public int insertProductAndVMTemplate(int type,Map<String, String> map) throws Exception {
		int template_id = -1;
		if(type == ConstDef.RESOURCE_TYPE_MINICOMPUTER){
			TTemplateMCBO mct = this.setMCTemplateAddedValues(map);
			template_id = this.MCTemplateService.createTemplate(mct);
		}else{
			TTemplateVMBO vmt = this.setVMTemplateAddedValues(type, map);
			//VMTemplateServiceImpl类的createTemplate方法修改
			vmt.setState(8); //资源池可用
			template_id = VMTemplateService.createTemplate(vmt);			
		}
//		template_id = VMTemplateDao.createTemplate_VDC(template);
		Product product = this.setProductAddedValues(map);
		if (product.getType() == 50) {
			product.setTemplateId(-1);
		}
		//设置模板id
		product.setTemplateId(template_id);
		//设置模板类型type
		product.setType(type);
		int result = productDao.save(product);
		product.setId(result);
		// 增加产品和模板的对应关系
		if (product.getType() == 50) {
			this.addProductTemplateRelation(product);
		}
		
		// 增加多计价单位的支持何军辉 20130319
		if (map.containsKey("price_list")) {
			String priceList = String.valueOf(map.get("price_list"));
			if (priceList != null && !priceList.equals("null")) {
				this.addProductChargeUnit(product.getId(), priceList);
			}
		}
		return template_id;
	}
	
	/**
	 * 增加多计价单位的支持
	 * 何军辉 20130311
	 * 
	 * @param productId
	 * @param priceListStr
	 */
	public void addProductChargeUnit(int productId, String priceListStr) {
		Map priceList = null;
		try {
			priceList = (Map) JSONUtil.deserialize(priceListStr);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error(String.format("(Map) JSONUtil.deserialize(%s)",
					priceListStr), e);
			return;
		}

		if (priceList == null || priceList.size() == 0) {
			return;
		}

		// 先清楚当前ProudctId可能有的记录
		List<ProuctChargeUnit> list = null;
		try {
			list = productChargeUnitDao.findByProductId(String
					.valueOf(productId));
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.error(String.format(
					"productChargeUnitDao.findByProductId(String.valueOf(%s))",
					productId), e1);
		}
		if (list != null && list.size() > 0) {
			try {
				productChargeUnitDao.delete(list);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error(
						String.format("productChargeUnitDao.delete(list)"), e);
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						String.format("productChargeUnitDao.delete(list)"), e);
			}
		}

		for (Object unit : priceList.keySet()) {
			ProuctChargeUnit productChargeUnit = new ProuctChargeUnit();
			productChargeUnit.setProductId(String.valueOf(productId));
			productChargeUnit.setUnitId(String.valueOf(chargeUnitDao
					.findIdByUnit(unit.toString())));// 计费单位ID
			productChargeUnit.setPrice(priceList.get(unit).toString());
			// 此字段为广东VDC项目使用，其它项目请忽略
			productChargeUnit.setResourceId("0");
			try {
				productChargeUnitDao.add(productChargeUnit);
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("productChargeUnitDao.add(productChargeUnit);", e);
				continue;
			}
		}
	}
	
	private Product setProductAddedValues(Map<String, String> map){
		//-------------获取服务定义数据-------------------
		Product product = new Product();
		product.setName(map.get("name"));
		product.setCode(map.get("code"));
		product.setDescription(map.get("description"));
		product.setSpecification(map.get("specification"));
		product.setQuotaNum(Integer.parseInt(String.valueOf(map.get("quotaNum"))));
//		product.setPeriod(map.get("period"));
		product.setPrice(Float.parseFloat(String.valueOf(map.get("price"))));
		product.setUnit(map.get("unit"));
		product.setIsDefault(Integer.parseInt(String.valueOf(map.get("isDefault"))));
		if (product != null) {
			// 资源状态：1-待审核、2-待发布、3-已发布、4-已删除、5-审核失败、6-产品下线。
			int state = ConstDef.STATE_WAIT_ADUIT;// 待审核
			if (ConstDef.getPrudoctAduitAuto() == 1 && ConstDef.getPrudoctPublishAuto() == 1) {
				// 已发布
				state = ConstDef.STATE_RELEASED;
			} else if (ConstDef.getPrudoctAduitAuto() == 1) {
				// 已审核
				state = ConstDef.STATE_WAIT_RELEASE;
			}
			product.setState(state);
			product.setCreateDate(new Timestamp(System.currentTimeMillis()));
			product.setModifyDate(new Timestamp(System.currentTimeMillis()));
		}
		return product;
	}
	
	
	private TTemplateVMBO setVMTemplateAddedValues(int type,Map<String, String> map){
		TTemplateVMBO vmt = new TTemplateVMBO();
		if (type == ConstDef.RESOURCE_TYPE_VM) {
			vmt.setType(ConstDef.RESOURCE_TYPE_VM);
			vmt.setCpuNum(Integer.parseInt(map.get("cpuNum")));
			vmt.setCpufrequency(Float.parseFloat(map.get("cpuHz")));
			vmt.setMemorySize(Integer.parseInt(map.get("memorySize")));
			vmt.setVethAdaptorNum(Integer.parseInt(map.get("netCard")));
			String osName = map.containsKey("osName") ? String.valueOf(map.get("osName")) : "";
			vmt.setVmos(osName);
			int osId = map.containsKey("osId") ? Integer.parseInt(String.valueOf(map.get("osId"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
			vmt.seteOsId(osId);
			String storeType = map.containsKey("storeType") ? String.valueOf(map.get("storeType")) : "";
			vmt.setStoreType(storeType);
			int storageSize = map.containsKey("storageSize") ? Integer.parseInt(String.valueOf(map.get("storageSize")))
			        : Constants.STATUS_COMMONS.IGNORE.getValue();
			vmt.setStorageSize(storageSize);
			String bandWidth = map.containsKey("bandWidth") ? String.valueOf(map.get("bandWidth")) : "";
			vmt.setExtendAttrJSON(bandWidth);

		} else if (type == ConstDef.RESOURCE_TYPE_STORAGE) {
			vmt.setType(ConstDef.RESOURCE_TYPE_STORAGE);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
			//1.3功能，虚拟硬盘加入存储类型
			String storeType = map.containsKey("storeType") ? String.valueOf(map.get("storeType")) : "";
			vmt.setStoreType(storeType);
		} else if (type == ConstDef.RESOURCE_TYPE_BACKUP) {
			vmt.setType(ConstDef.RESOURCE_TYPE_BACKUP);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
		} else if (type == ConstDef.RESOURCE_TYPE_MONITOR) {
			vmt.setType(ConstDef.RESOURCE_TYPE_MONITOR);
			String resourceTypes = map.get("resourceTypes");
			if (resourceTypes.length() > 1) {
				String _resourceTypes = resourceTypes.substring(0, resourceTypes.length() - 1);
				vmt.setNetworkDesc(_resourceTypes);
			}
		} else if (type == ConstDef.RESOURCE_TYPE_LOADBALANCED) {
			vmt.setType(ConstDef.RESOURCE_TYPE_LOADBALANCED);
			vmt.setStorageSize(Integer.parseInt(map.get("concurrentNum")));
			vmt.setProtocol(map.get("protocol"));
			vmt.setPolicy(map.get("policy"));
			int port = map.containsKey("port") ? Integer.parseInt(String.valueOf(map.get("port"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
			vmt.setPort(port);
		} else if (type == ConstDef.RESOURCE_TYPE_BANDWIDTH) {
			vmt.setType(ConstDef.RESOURCE_TYPE_BANDWIDTH);
//			String bandWidthName = map.containsKey("bandWidthName") ? String.valueOf(map.get("bandWidthName")) : "";
			int bandWidth = map.containsKey("bandWidth") ? Integer.parseInt(String.valueOf(map.get("bandWidth")))
			        : Constants.STATUS_COMMONS.IGNORE.getValue();
			vmt.setStorageSize(bandWidth);
			vmt.setExtendAttrJSON(String.valueOf(bandWidth));
		} else if (type == ConstDef.RESOURCE_TYPE_FIREWALL) {
			vmt.setType(ConstDef.RESOURCE_TYPE_FIREWALL);
			vmt.setStorageSize(Integer.parseInt(map.get("ruleNum")));
		} else if (type == ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP) {
			vmt.setType(ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP);
			//to fix bug 4733 4098
			if(map.containsKey("ipType")){
				vmt.setStorageSize(Integer.parseInt(map.get("ipType")));
			}else{
				vmt.setStorageSize(4);
			}		
		} else if (type == ConstDef.RESOURCE_TYPE_PM) {
			vmt.setType(ConstDef.RESOURCE_TYPE_PM);
			String cpunumStoragesize = map.get("cpunumStoragesize");
			if(null != cpunumStoragesize && !cpunumStoragesize.isEmpty() && cpunumStoragesize.contains("_")){
				String[] arr = cpunumStoragesize.split("_");///
				vmt.setCpuNum(Integer.parseInt(arr[0]));
				vmt.setCpufrequency(Float.parseFloat(arr[1]));
				vmt.setMemorySize(Integer.parseInt(arr[2]));
				vmt.setStorageSize(Integer.parseInt(arr[3]));				
			}
//			vmt.setCpufrequency(Float.parseFloat(map.get("cpuHz")));
			String osName = map.containsKey("osName") ? String.valueOf(map.get("osName")) : "";
			vmt.setVmos(osName);
			int osId = map.containsKey("osId") ? Integer.parseInt(String.valueOf(map.get("osId"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
			//虚拟机操作系统存在e_os_id字段里，物理机也存在这个字段里吗，确认一下？？？？？？？？？
			vmt.seteOsId(osId);
			vmt.setVethAdaptorNum(Integer.parseInt(map.get("netCard")));
		} else if (type == ConstDef.RESOURCE_TYPE_CLOUDSTORAGE) {
			vmt.setType(ConstDef.RESOURCE_TYPE_DATABACKUP);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
		} else if (type == ConstDef.RESOURCE_TYPE_OBJECTSTORAGE) {
			vmt.setType(ConstDef.RESOURCE_TYPE_OBJECTSTORAGE);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
			Map<String, Object> parameters = new HashMap<String, Object>();
		} else if (type == ConstDef.RESOURCE_TYPE_IPSAN) {
			vmt.setType(ConstDef.RESOURCE_TYPE_IPSAN);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
			//设置raid级别
			String raid = map.containsKey("raid") ? String.valueOf(map.get("raid")) : "";
			Map<String, Object> extendAttrMap = new HashMap<String, Object>();
			extendAttrMap.put("raid", raid);
			String	parameter = JsonUtil.getJsonString4JavaPOJO(extendAttrMap);
			vmt.setExtendAttrJSON(parameter);			
		} else if (type == ConstDef.RESOURCE_TYPE_NAS) { // 1.3功能，nas资源模板
			vmt.setType(ConstDef.RESOURCE_TYPE_NAS);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
		}
		vmt.setResourcePoolsId(Integer.parseInt(map.get("resourcePool")));
		// 1.3功能，支持多资源池，增加资源域zone
		int zoneId = map.containsKey("zoneId") ? Integer.parseInt(String.valueOf(map.get("zoneId"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
		vmt.setZoneId(zoneId);
		vmt.setTemplateDesc(map.get("templateName"));
		vmt.setCreatorUserId(((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER)).getId());
		vmt.setCreateTime(Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date()));
		vmt.setOperType(ConstDef.OPER_ADD);
//		String measureMode = map.containsKey("measureMode") ? String.valueOf(map.get("measureMode")) : "";
//		vmt.setMeasureMode(measureMode);
//		String grade = map.containsKey("grade") ? String.valueOf(map.get("grade")) : "";
//		vmt.setGrade(grade);
		//设置为特殊模板标志，自定义的不显示不能被利用，删除的不显示出来，记得在sql加过滤条件
//		vmt.setSpecial(1);
		return vmt;
	}
	
	private TTemplateMCBO setMCTemplateAddedValues(Map<String, String> map){
		TTemplateMCBO template = new TTemplateMCBO();
		template.setCreatorUserId(((TUserBO) ServletActionContext
				.getRequest().getSession()
				.getAttribute(ConstDef.SESSION_KEY_USER)).getId());
		template.setCreateTime(Constants.SDF.YYYYMMDDHHMMSS
				.getValue().format(new Date()));
		template.setType(Integer.parseInt(map.get("mcType")));
		template.setTemplateDesc(map.get("templateName"));
		template.setResourcePoolsId(Integer.parseInt(map
				.get("resourcePool")));
		template.setCpuNum(Integer.parseInt(map.get("cpuNum")));
		template.setMemorySize(Integer.parseInt(map
				.get("memorySize")));
		template.setStorageSize(Integer.parseInt(map
				.get("storageSize")));
		template.setOperType(ConstDef.OPER_ADD);
//		String measureMode = map.containsKey("measureMode") ? String
//				.valueOf(map.get("measureMode")) : "";
//		template.setMeasureMode(measureMode);
		return template;
	}
	
	/**
	 * 修改产品和模板
	 * @param product
	 * @throws Exception
	 * @author zhanghuizheng
	 */
	public void updateProductAndVMTemplate(int type,Map<String, String> map) throws Exception {
		int id = Integer.parseInt(map.get("id"));
		Product product = this.productDao.get(id);
		//为product赋值为修改后的值
		this.setProductModifiedValues(product, map);
		//更新产品
		productDao.update(product);
//		logger.info("update product ,name=" + product.getName());
		//为模板赋值为修改后的值
		if(type == ConstDef.RESOURCE_TYPE_MINICOMPUTER){
			TTemplateMCBO mct = this.setMCTemplateModifiedValues(product.getTemplateId(), map);
			MCTemplateService.updateTemplate(mct);		
		}else{
			TTemplateVMBO vmt = this.setVMTemplateModifiedValues(product.getTemplateId(), product.getType(), map);
			//更新模板
			this.VMTemplateService.updateTemplate_VDC(vmt);			
		}
		// 修改产品和模板的对应关系,先删除,再增加
		if (product.getType() == 50) {
			productTemplateRelationDao.deleteProductTemplateRelation(product.getId());
			this.addProductTemplateRelation(product);
		}
		
		// 增加多计价单位的支持何军辉 201303119
		if (map.containsKey("price_list")) {
			String priceList = String.valueOf(map.get("price_list"));
			if (priceList != null && !priceList.equals("null")) {
				this.addProductChargeUnit(product.getId(), priceList);
			}
		}
	}

	private void setProductModifiedValues(Product product,Map<String, String> map){
		// to fixed bug:2589 2546 2549
		// 资源状态：1-待审核、2-待发布、3-已发布、4-已删除、5-审核失败、6-产品下线。
		int state = ConstDef.STATE_WAIT_ADUIT;// 待审核
		if (ConstDef.getPrudoctAduitAuto() == 1 && ConstDef.getPrudoctPublishAuto() == 1) {// 自动审核，自动发布
			// 已发布
			state = ConstDef.STATE_RELEASED;
		} else if (ConstDef.getPrudoctAduitAuto() == 1) {// 自动审核
			// 已审核
			state = ConstDef.STATE_WAIT_RELEASE;
		}
		product.setName(map.get("name"));
		product.setDescription(map.get("description"));
		product.setSpecification(map.get("specification"));
		product.setQuotaNum(Integer.parseInt(String.valueOf(map.get("quotaNum"))));
//		product.setQuotaNum(Integer.parseInt(map.get("quotaNum")));
		product.setPrice(Float.parseFloat(map.get("price")));
		product.setUnit(map.get("unit"));
//		product.setType(Integer.parseInt(map.get("type")));
//		product.setTemplateId(Integer.parseInt(map.get("templateId")));
		product.setIsDefault(Integer.parseInt(String.valueOf(map.get("isDefault"))));
//		product.setIsDefault(Integer.parseInt(map.get("isDefault")));
		//多服务实例？？？
		if(product.getType() == 50){
			product.setTemplateIds(map.get("templateIds"));
		}	
		product.setModifyDate(new Timestamp(System.currentTimeMillis()));
		product.setState(state);
		// 将操作类型改为修改
		product.setOperateType(ConstDef.OPERATE_TYPE_UPDATE);
	}

	private TTemplateVMBO setVMTemplateModifiedValues(int templateId,int type,Map<String, String> map) throws Exception{
		
		TTemplateVMBO vmt = VMTemplateService.getTemplateById(templateId);

		if (type == ConstDef.RESOURCE_TYPE_VM) {
			vmt.setType(ConstDef.RESOURCE_TYPE_VM);
			vmt.setCpuNum(Integer.parseInt(map.get("cpuNum")));
			vmt.setCpufrequency(Float.parseFloat(map.get("cpuHz")));
			vmt.setMemorySize(Integer.parseInt(map.get("memorySize")));
			vmt.setVethAdaptorNum(Integer.parseInt(map.get("netCard")));
			String osName = map.containsKey("osName") ? String.valueOf(map.get("osName")) : "";
			vmt.setVmos(osName);
			int osId = map.containsKey("osId") ? Integer.parseInt(String.valueOf(map.get("osId"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
			vmt.seteOsId(osId);
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
			String storeType = map.containsKey("storeType") ? String.valueOf(map.get("storeType")) : "";
			vmt.setStoreType(storeType);
			int storageSize = map.containsKey("storageSize") ? Integer.parseInt(String.valueOf(map.get("storageSize")))
			        : Constants.STATUS_COMMONS.IGNORE.getValue();
			vmt.setStorageSize(storageSize);
			String bandWidth = map.containsKey("bandWidth") ? String.valueOf(map.get("bandWidth")) : "";
			vmt.setExtendAttrJSON(bandWidth);

		} else if (type == ConstDef.RESOURCE_TYPE_STORAGE) {
			vmt.setId(Integer.valueOf(map.get("id")));
			vmt.setType(ConstDef.RESOURCE_TYPE_STORAGE);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
			vmt.setResourcePoolsId(Integer.parseInt(map.get("resourcePool")));
			vmt.setTemplateDesc(map.get("templateName"));
			TemplateCloudStoragePo tempalteCloudStorage = new TemplateCloudStoragePo();
			tempalteCloudStorage = tempalteCloudStorage.getIntance(vmt);
			vmt.setResourceTemplate(TemplateCloudStoragePo.toString(tempalteCloudStorage));
		} else if (type == ConstDef.RESOURCE_TYPE_BACKUP) {
			vmt.setType(ConstDef.RESOURCE_TYPE_BACKUP);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
		} else if (type == ConstDef.RESOURCE_TYPE_MONITOR) {
			vmt.setType(ConstDef.RESOURCE_TYPE_MONITOR);
			String resourceTypes = map.get("resourceTypes");
			if (resourceTypes.length() > 1) {
				String _resourceTypes = resourceTypes.substring(0, resourceTypes.length() - 1);
				vmt.setNetworkDesc(_resourceTypes);
			}
//			vmt.setMeasureMode(map.get("measureMode"));
		} else if (type == ConstDef.RESOURCE_TYPE_LOADBALANCED) {
			vmt.setType(ConstDef.RESOURCE_TYPE_LOADBALANCED);
			vmt.setStorageSize(Integer.parseInt(map.get("concurrentNum")));
//			vmt.setMeasureMode(map.get("measureMode"));
			vmt.setProtocol(map.get("protocol"));
			vmt.setPolicy(map.get("policy"));
			int port = map.containsKey("port") ? Integer.parseInt(String.valueOf(map.get("port"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
			vmt.setPort(port);

		} else if (type == ConstDef.RESOURCE_TYPE_BANDWIDTH) {
			vmt.setType(ConstDef.RESOURCE_TYPE_BANDWIDTH);
//			vmt.setMeasureMode(map.get("measureMode"));
//			String bandWidthName = map.containsKey("bandWidthName") ? String.valueOf(map.get("bandWidthName")) : "";
			int bandWidth = map.containsKey("bandWidth") ? Integer.parseInt(String.valueOf(map.get("bandWidth")))
			        : Constants.STATUS_COMMONS.IGNORE.getValue();
			vmt.setStorageSize(bandWidth);
			vmt.setExtendAttrJSON(String.valueOf(bandWidth));
		} else if (type == ConstDef.RESOURCE_TYPE_FIREWALL) {
			vmt.setType(ConstDef.RESOURCE_TYPE_FIREWALL);
			vmt.setStorageSize(Integer.parseInt(map.get("ruleNum")));
//			vmt.setMeasureMode(map.get("measureMode"));
		} else if (type == ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP) {
			vmt.setType(ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP);
			//to fix bug 4733 4098
			if(map.containsKey("ipType")){
				vmt.setStorageSize(Integer.parseInt(map.get("ipType")));
			}else{
				vmt.setStorageSize(4);
			}			
//			vmt.setMeasureMode(map.get("measureMode"));
		} else if (type == ConstDef.RESOURCE_TYPE_PM) {
			vmt.setType(ConstDef.RESOURCE_TYPE_PM);
			String cpunumStoragesize = map.get("cpunumStoragesize");
			if(null != cpunumStoragesize && !cpunumStoragesize.isEmpty() && cpunumStoragesize.contains("_")){
				String[] arr = cpunumStoragesize.split("_");
				vmt.setCpuNum(Integer.parseInt(arr[0]));
				vmt.setCpufrequency(Float.parseFloat(arr[1]));
				vmt.setMemorySize(Integer.parseInt(arr[2]));
				vmt.setStorageSize(Integer.parseInt(arr[3]));				
			}
//			vmt.setCpufrequency(Float.parseFloat(map.get("cpuHz")));
			String osName = map.containsKey("osName") ? String.valueOf(map.get("osName")) : "";
			vmt.setVmos(osName);
			int osId = map.containsKey("osId") ? Integer.parseInt(String.valueOf(map.get("osId"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();
			vmt.seteOsId(osId);
		} else if (type == ConstDef.RESOURCE_TYPE_DATABACKUP) {
			vmt.setId(Integer.valueOf(map.get("id")));
			vmt.setType(ConstDef.RESOURCE_TYPE_DATABACKUP);
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
			vmt.setResourcePoolsId(Integer.parseInt(map.get("resourcePool")));
			vmt.setTemplateDesc(map.get("templateName"));
			vmt.setCreatorUserId(((TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER))
			        .getId());
			vmt.setCreateTime(Constants.SDF.YYYYMMDDHHMMSS.getValue().format(new Date()));
			TemplateCloudStoragePo tempalteCloudStorage = new TemplateCloudStoragePo();
			tempalteCloudStorage = tempalteCloudStorage.getIntance(vmt);
			vmt.setResourceTemplate(TemplateCloudStoragePo.toString(tempalteCloudStorage));
			vmt.setTemplateDesc(map.get("templateName"));
		} else if (type == ConstDef.RESOURCE_TYPE_OBJECTSTORAGE) {
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
		} else if (type == ConstDef.RESOURCE_TYPE_IPSAN) {
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
			vmt.setResourcePoolsId(Integer.parseInt(map.get("resourcePool")));
			vmt.setTemplateDesc(map.get("templateName"));
			//设置raid级别
			String raid = map.containsKey("raid") ? String.valueOf(map.get("raid")) : "";
			Map<String, Object> extendAttrMap = new HashMap<String, Object>();
			extendAttrMap.put("raid", raid);
			String	parameter = JsonUtil.getJsonString4JavaPOJO(extendAttrMap);
			vmt.setExtendAttrJSON(parameter);			

		} else if (type == ConstDef.RESOURCE_TYPE_NAS) {
			vmt.setStorageSize(Integer.parseInt(map.get("storageSize")));
//			vmt.setMeasureMode(map.get("measureMode"));
//			vmt.setGrade(map.get("grade"));
			vmt.setResourcePoolsId(Integer.parseInt(map.get("resourcePool")));
			vmt.setTemplateDesc(map.get("templateName"));
		}
		if (map.containsKey("resourcePool")) {
			vmt.setResourcePoolsId(Integer.parseInt(map.get("resourcePool")));
		}
		// 1.3功能，支持多资源池，增加资源域zone
		int zoneId = map.containsKey("zoneId") ? Integer.parseInt(String.valueOf(map.get("zoneId"))) : Constants.STATUS_COMMONS.IGNORE
			        .getValue();			
		vmt.setZoneId(zoneId);
		vmt.setTemplateDesc(map.get("templateName"));
		vmt.setOperType(ConstDef.OPER_MOD);		
		return vmt;
	}
	
	private TTemplateMCBO setMCTemplateModifiedValues(int templateId,Map<String, String> map) throws Exception{
		TTemplateMCBO mct = MCTemplateService.getTemplateById(templateId);
		mct.setState(ConstDef.STATE_ONE);
		mct.setCpuNum(Integer.parseInt(map.get("cpuNum")));
		mct.setMemorySize(Integer.parseInt(map.get("memorySize")));
		mct.setStorageSize(Integer.parseInt(map.get("storageSize")));
		mct.setType(Integer.parseInt(map.get("mcType")));
//		String measureMode = map.containsKey("measureMode") ? String
//				.valueOf(map.get("measureMode")) : "";
//		mct.setMeasureMode(measureMode);

		mct.setResourcePoolsId(Integer.parseInt(map
				.get("resourcePool")));
		mct.setTemplateDesc(map.get("templateName"));
		mct.setOperType(ConstDef.OPER_MOD);
		return mct;
	}
	
	@Override
	public List<Product> findAllProduct() {
		return productDao.findAll();
	}

	@Override
	public List<Product> findProduct(String key, int state, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) {
		List<Product> list = productDao.find(0, key, state, price, templateId, type, productItemId, 0, curPage, pageSize);
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}

	public List<Product> findProductForItem(String key, int state, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) {
		List<Product> list = productDao.findForItem(0, key, state, price, templateId, type, productItemId, 0, curPage, pageSize);
		return list;
	}
	
	public List<Product> findProductForOpt(TTemplateVMBO template, Product product, int curPage, int pageSize) {
		List<Product> list = productDao.findForOpt(template, product, curPage, pageSize);
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}
	

	@Override
	public List<Product> findRelativeProduct(int state, int type) {
		List<Product> list = productDao.findRelativeProduct(state, type);
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}

	@Override
	public List<Product> findProductFront(String key, float price, int type, Integer productItemId, int isDefault, int curPage, int pageSize) {
		return productDao.find(0, key, 3, price, 0, type, productItemId, isDefault, curPage, pageSize);
	}

	@Override
	public List<Product> findProductVM(int type, int page, int pagesize) {
		// TODO Auto-generated method stub
		return productDao.find(0, null, 3, 0, 0, type, null, 1, page, pagesize);
	}

	@Override
	public List<Product> findProductFrontDefault(int userId, int type) {
		// TODO Auto-generated method stub
		return productDao.find(userId, null, 3, 0, 0, type, null, 1, 1, 3);
	}

	@Override
	public List<Product> findProductByCatalog(int type) {
		// TODO Auto-generated method stub
		return productDao.findBuyByCatalog(3, type, 1, 3);
	}

	/*
	 * To Fix Bug Id:[1633] 2307
	 */
	@Override
	public List<Product> findProductFrontDefaultAll(int type) {
		// TODO Auto-generated method stub
		return productDao.find(0, null, 3, 0, 0, type, null, 0, 1, 100);
	}

	// to fix bug [2833]
	@Override
	public List<Product> findProductFrontDefaultAllTJ(int type) {
		// TODO Auto-generated method stub
		return productDao.find(0, null, 3, 0, 0, type, null, 1, 1, 100);
	}

	@Override
	public List<Product> findProductById(int id) {
		// TODO Auto-generated method stub
		return productDao.findById(id);
	}

	@Override
	public List<Product> findProductFrontNew() {
		// TODO Auto-generated method stub
		return productDao.findNew();
	}

	@Override
	public List<Product> findProductCommend() {
		// TODO Auto-generated method stub
		return productDao.findCommend();
	}

	@Override
	public Product getProduct(int id) {
		Product product = productDao.get(id);
		// 如果是多虚机服务则获取产品关联的所有模板和数量
		if (product.getType() == ConstDef.RESOURCE_TYPE_MUTIL_VM) {
			//查询服务关联的所有模板，包括虚拟机和小型机
			List<TTemplateVMBO> templates = this.productTemplateRelationDao.getProductTemplates(id);
			if (null != templates && templates.size() > 0) {
				product.setTemplates(templates);
			}
			//查询小型机模板
//			List<TTemplateMCBO> mcTemplates = this.productTemplateRelationDao.getProductMCTemplates(id);
//			if(null != mcTemplates && !mcTemplates.isEmpty()){
//				product.setMcTemplates(mcTemplates);
//			}
			
		}
		
		// 多定价模式支持 added by 何军辉 20130311
		String unitName = "";
		try {
			unitName = this.chargeUnitDao.findByUnit(product.getUnit())
					.getDescription();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
					"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
					e);
		}
		product.setUnitName(unitName);
		product.setPriceList(getProductChargeUnitForView(product));
		
		return product;
	}

	@Override
	public List<Product> findAduit(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) {
		List<Product> list = productDao.findAduit(key, price, templateId, type, productItemId, curPage, pageSize);
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}

	@Override
	public List<Product> findRelease(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) {
		List<Product> list = productDao.findRelease(key, price, templateId, type, productItemId, curPage, pageSize);
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}

	// @Override
	// public void updateProduct(int productItemId, String ids,String
	// currentAllProductIds) throws Exception{
	// this.productDao.update(productItemId,currentAllProductIds);//????????
	// // 添加用户操作日志
	// //String memo = "";
	// //String desc = "服务目录中删除服务资源,即把原来与服务目录绑定的资源全部解绑定";
	// //ConstDef.saveLogInfo(ConstDef.OPER_MOD, "服务目录定义", "资源绑定",
	// "ProductServiceImpl", "updateProduct", parameters, desc, memo);
	//
	// if(null != ids && !ids.equals("")){
	// String[] arrids = ids.split(",");
	// for(int i=0;i<arrids.length;i++){
	// this.productDao.update(productItemId, Integer.parseInt(arrids[i]));
	// // 添加用户操作日志
	// String parameters = "productItemId=" + productItemId;
	// String _memo = "要绑定的资源的id值为:"+arrids[i];
	// String _desc = "把资源添加到服务目录";
	// ConstDef.saveLogInfo(ConstDef.OPER_MOD, "服务目录定义", "资源绑定",
	// "ProductServiceImpl", "updateProduct", parameters, _desc, _memo);
	//
	// }
	// }
	//
	// }

	@Override
	public int listProductCount(String name, int type, int state) throws Exception {

		return this.productDao.listProductCount(name, type, state);
	}

	@Override
	public int getProductCountByName(String name) throws Exception {
		return this.productDao.getProductCountByName(name);
	}

	@Override
	public int listItemProductCount(String name, int type, int state, int item) throws Exception {
		return this.productDao.listItemProductCount(name, type, state, item);
	}

	@Override
	public int getProductCountByCode(String code) throws Exception {
		return this.productDao.getProductCountByCode(code);
	}

	@Override
	public int getInstanceCountByProductIdAndUserId(int productId, int userId) throws Exception {
		return this.productDao.getInstanceCountByProductIdAndUserId(productId, userId);
	}

	@Override
	public int getInstanceCountByProductId(int productId) throws Exception {
		return this.getInstanceCountByProductIdAndUserId(productId, 0);
	}

	@Override
	public int checkProductRename(String name) throws Exception {
		return this.productDao.checkProductRename(name);
	}

	@Override
	public int checkVMSProductRename(String name) throws Exception {
		return this.productDao.checkVMSProductRename(name);
	}

	@Override
	public int checkProductRename(String name, int type) throws Exception {
		return this.productDao.checkProductRename(name, type);
	}

	@Override
	public int updateState(int productId, int state,int templateId) throws Exception {
		//added by zhanghuizheng 2013-1-17
		//审核新建服务模板时，把资源模板的状态从8（资源池可用）改为2（可用）状态
		if(state == ConstDef.STATE_WAIT_RELEASE || state == ConstDef.STATE_RELEASED){
			VMTemplateDao.upateTemplateState(templateId, 2);
		}else if(state == ConstDef.STATE_DELETED){
			VMTemplateDao.upateTemplateState(templateId, 5);
		}				
		return this.productDao.updateState(productId, state);
	}

	@Override
	public int updateState(int productId, int state, int operateType,int templateId) throws Exception {
		//added by zhanghuizheng 2013-1-17 to fix bug 4811 4829 4891 4919
		//审核新建服务模板时，把资源模板的状态从8（资源池可用）改为2（可用）状态
		if(state == ConstDef.STATE_WAIT_RELEASE || state == ConstDef.STATE_RELEASED){
			VMTemplateDao.upateTemplateState(templateId, 2);
		}else if(state == ConstDef.STATE_DELETED){
			VMTemplateDao.upateTemplateState(templateId, 5);
		}		
		int result = this.productDao.updateState(productId, state, operateType);
		return result;
		
	}

	public IVMTemplateDao getVMTemplateDao() {
		return VMTemplateDao;
	}

	public void setVMTemplateDao(IVMTemplateDao vMTemplateDao) {
		VMTemplateDao = vMTemplateDao;
	}

	public IVMTemplateService getVMTemplateService() {
		return VMTemplateService;
	}

	public void setVMTemplateService(IVMTemplateService vMTemplateService) {
		VMTemplateService = vMTemplateService;
	}

	public IMCTemplateService getMCTemplateService() {
		return MCTemplateService;
	}

	public void setMCTemplateService(IMCTemplateService mCTemplateService) {
		MCTemplateService = mCTemplateService;
	}

	@Override
	public List<Product> find(TTemplateVMBO template, Product product,
			int curPage, int pageSize) {
		List<Product> list = this.productDao.find(template, product, curPage, pageSize);
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}

	@Override
	public int getFindCount(TTemplateVMBO template, Product product) {
		return this.productDao.getFindCount(template, product);
	}

	public List<Product> findByType(String searchKey,int type, int curPage, int pageSize) {
		List<Product> list = productDao.findByType(searchKey,type, curPage, pageSize);
		if(type == ConstDef.RESOURCE_TYPE_MUTIL_VM){
			if(null != list && !list.isEmpty()){
				for(Product product : list){
					//查询服务关联的所有模板，包括虚拟机和小型机
					List<TTemplateVMBO> templates = this.productTemplateRelationDao.getProductTemplates(product.getId());
					if (null != templates && templates.size() > 0) {
						product.setTemplates(templates);
					}					
				}
			}
		}
		
		// 多定价模式支持 added by 何军辉 20130311
		for(Product p : list){
			String unitName = "";
			try {
				unitName = this.chargeUnitDao.findByUnit(p.getUnit())
						.getDescription();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(
						"this.chargeUnitDao.findByUnit(product.getUnit()).getDescription()",
						e);
			}
			p.setUnitName(unitName);
			p.setPriceList(getProductChargeUnitForView(p));
		}
		
		return list;
	}

	/**
	 * 输出多定价模式下的产品价格
	 * 多定价模式支持 added by 何军辉 20130311
	 * 
	 * @param product
	 * @return
	 */
	private String getProductChargeUnitForView(Product product) {
		String priceList = "";

		try {
			List<ProuctChargeUnit> list = productChargeUnitDao
					.findByProductId(String.valueOf(product.getId()));
			for (int i = 0; i < list.size(); i++) {
				ProuctChargeUnit u = list.get(i);
				priceList += u.getPrice() + "/" + u.getDescription();
				if (i < list.size() - 1) {
					priceList += ",&nbsp;";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
					"List<ProuctChargeUnit> list = productChargeUnitDao.findByProductId(String.valueOf(product.getId()));",
					e);
		}

		if (priceList == null || priceList.length() == 0) {
			priceList = String.valueOf(product.getPrice()) + "/"
					+ product.getUnitName();
		}

		return priceList;
	}
	
	/**
	 * 取得产品的多个定价信息 何军辉 20130310
	 * 
	 * @param productId
	 * @return
	 */
	@Override
	public List<ProuctChargeUnit> getProductChargeUnitList(int productId) {
		try {
			return productChargeUnitDao.findByProductId(String
					.valueOf(productId));
		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(String.format("getProductChargeUnitList(%s)", productId),
					e);
		}
		return null;
	}

	public IProductChargeUnitDao getProductChargeUnitDao() {
		return productChargeUnitDao;
	}

	public void setProductChargeUnitDao(
			IProductChargeUnitDao productChargeUnitDao) {
		this.productChargeUnitDao = productChargeUnitDao;
	}

	public IChargeUnitDao getChargeUnitDao() {
		return chargeUnitDao;
	}

	public void setChargeUnitDao(IChargeUnitDao chargeUnitDao) {
		this.chargeUnitDao = chargeUnitDao;
	}
}
