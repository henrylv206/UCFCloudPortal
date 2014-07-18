package com.skycloud.management.portal.rest.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.front.command.res.AttachVolume;
import com.skycloud.management.portal.front.command.res.CreateVolume;
import com.skycloud.management.portal.front.command.res.DeleteVolume;
import com.skycloud.management.portal.front.command.res.DetachVolume;
import com.skycloud.management.portal.front.command.res.ListVolumes;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.rest.BaseResource;
import com.skycloud.management.portal.rest.entity.Volume;
import com.skycloud.management.portal.rest.entity.VolumeTemplate;
import com.skycloud.management.portal.rest.model.TemplateListResp;
import com.skycloud.management.portal.rest.model.VolumeAttachReq;
import com.skycloud.management.portal.rest.model.VolumeCreateReq;
import com.skycloud.management.portal.rest.model.VolumeDetachReq;
import com.skycloud.management.portal.rest.model.VolumeGetResp;
import com.skycloud.management.portal.rest.model.VolumeListReq;
import com.skycloud.management.portal.rest.model.VolumeListResp;
import com.skycloud.management.rest.commons.Constants;

/**
 * 块存储相关Resource
 * 
 * @author jiaoyz
 */
@Component
@Path("/volume")
public class VolumeResource extends BaseResource {

	@Autowired
	private IVMTemplateService VMTemplateService;

	@Autowired
	private ICommandService commandService;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getVolumeList(VolumeListReq req) {
		VolumeListResp resp = new VolumeListResp();
		resp.setReq(req);
		if (req == null) {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- List volume failed : paramater missing");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- List volume for vmId:" + req.getVmId());
			}
			try {
				ListVolumes cmd = new ListVolumes();
				if (req.getVmId() > 0) {
					cmd.setVirtualmachineid(String.valueOf(req.getVmId()));
				}
				JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, req.getPoolId()));
				jo = JSONObject.fromObject(jo.getString("listvolumesresponse"));
				JSONArray ja = JSONArray.fromObject(jo.getString("volume"));
				if (ja != null && ja.size() > 0) {
					resp.setResult(new ArrayList<Volume>());
					for (Object obj : ja.toArray()) {
						jo = JSONObject.fromObject(obj);
						Volume volume = new Volume();
						if (jo.containsKey("attached")) {
							volume.setAttached(jo.getString("attached"));
						}
						volume.setCreated(jo.getString("created"));
						volume.setDestroyed(jo.getString("destroyed"));
						volume.setId(jo.getInt("id"));
						volume.setName(jo.getString("name"));
						volume.setSize((int) (jo.getLong("size") / 1024 / 1024 / 1024));
						volume.setState(jo.getString("state"));
						if (jo.containsKey("vmname")) {
							volume.setVmName(jo.getString("vmname"));
						}
						if (jo.containsKey("vmstate")) {
							volume.setVmState(jo.getString("vmstate"));
						}
						resp.getResult().add(volume);
					}
					resp.setTotalCount(resp.getResult().size());
				}
				resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_SUCCESS.getValue());
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error("REST -- List volume for vmId:" + req.getVmId() + " failed, Exception : " + e.getMessage(), e);
				resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_OTHER.getValue());
				return Response.ok(resp).build();
			}
			if (logger.isInfoEnabled()) {
				logger.info("REST -- List volume for vmId:" + req.getVmId() + " succeed");
			}
		}
		return Response.ok(resp).build();
	}

	@GET
	@Path("{pool}/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getVolume(@PathParam("id") int pool, @PathParam("id") int id) {
		if (logger.isInfoEnabled()) {
			logger.info("REST -- Get volume by id : " + id);
		}
		VolumeGetResp resp = new VolumeGetResp();
		try {
			ListVolumes cmd = new ListVolumes();
			cmd.setId(String.valueOf(id));
			JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, pool));
			jo = JSONObject.fromObject(jo.getString("listvolumesresponse"));
			JSONArray ja = JSONArray.fromObject(jo.getString("volume"));
			if (ja != null && ja.size() > 0) {
				jo = JSONObject.fromObject(ja.get(0));
				Volume volume = new Volume();
				if (jo.containsKey("attached")) {
					volume.setAttached(jo.getString("attached"));
				}
				volume.setCreated(jo.getString("created"));
				volume.setDestroyed(jo.getString("destroyed"));
				volume.setId(jo.getInt("id"));
				volume.setName(jo.getString("name"));
				volume.setSize((int) (jo.getLong("size") / 1024 / 1024 / 1024));
				volume.setState(jo.getString("state"));
				if (jo.containsKey("vmname")) {
					volume.setVmName(jo.getString("vmname"));
				}
				if (jo.containsKey("vmstate")) {
					volume.setVmState(jo.getString("vmstate"));
				}
				resp.setVolume(volume);
				resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_SUCCESS.getValue());
			} else {
				resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_BS_ID_NOTEXIST.getValue());
				return Response.ok(resp).build();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("REST -- Get volume by id : " + id + " failed, Exception : " + e.getMessage(), e);
			resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_OTHER.getValue());
			return Response.ok(resp).build();
		}
		if (logger.isInfoEnabled()) {
			logger.info("REST -- Get volume by id : " + id + " succeed");
		}
		return Response.ok(resp).build();
	}

	@GET
	@Path("template")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTemplate() {
		TemplateListResp resp = new TemplateListResp();
		if (logger.isInfoEnabled()) {
			logger.info("REST -- Get volume template list");
		}
		try {
			List<TTemplateVMBO> list = VMTemplateService.listTemplate(ConstDef.RESOURCE_TYPE_STORAGE, ConstDef.STATE_TWO,
			                                                          Constants.STATUS_COMMONS.IGNORE.getValue(),
			                                                          Constants.STATUS_COMMONS.IGNORE.getValue(), 0, 0);
			if (list != null && list.size() > 0) {
				resp.setResult(new ArrayList<VolumeTemplate>());
				for (TTemplateVMBO template : list) {
					if (template.geteDiskId() > 0) {
						VolumeTemplate vt = new VolumeTemplate();
						vt.setCode(template.getCode());
						vt.setCreateTime(template.getCreateTime());
						vt.setCreatorUserId(template.getCreatorUserId());
						vt.setEDiskId(template.geteDiskId());
						vt.setId(template.getId());
						vt.setResourcePoolsId(template.getResourcePoolsId());
						vt.setState(template.getState());
						vt.setStorageSize(template.getStorageSize());
						vt.setTemplateDesc(template.getTemplateDesc());
						vt.setType(template.getType());
						resp.getResult().add(vt);
					}
				}
				resp.setTotalCount(resp.getResult().size());
			}
			resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_SUCCESS.getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("REST -- Get volume template list failed, Exception : " + e.getMessage(), e);
			resp.setResCode(Constants.CMCC_CRITERION_CODE.RESPONSE_CODE_OTHER.getValue());
			return Response.ok(resp).build();
		}
		if (logger.isInfoEnabled()) {
			logger.info("REST -- Get volume template list succeed and list size:" + resp.getTotalCount());
		}
		return Response.ok(resp).build();
	}

	@POST
	@Path("create")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String createVolume(VolumeCreateReq req, @HeaderParam("Accept") String accept) {
		String result = "";
		if (req == null) {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Create volume failed : paramater missing");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Create volume by name:" + req.getName() + ", diskofferingid:" + req.getDiskofferingid() + ", size:"
				        + req.getSize() + ", zongid:" + req.getZoneid());
			}
			try {
				CreateVolume cmd = new CreateVolume();
				cmd.setDiskofferingid(req.getDiskofferingid());
				cmd.setName(req.getName());
				cmd.setSize(req.getSize());
				cmd.setZoneid(req.getZoneid());
				if (accept.toLowerCase().startsWith(MediaType.APPLICATION_JSON.toLowerCase())) {
					result = String.valueOf(commandService.executeAndJsonReturn(cmd, req.getPoolId()));
				} else {
					result = String.valueOf(commandService.executeAndXMLReturn(cmd, req.getPoolId()));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error("REST -- Create volume by name:" + req.getName() + ", diskofferingid:" + req.getDiskofferingid() + ", size:"
				                     + req.getSize() + ", zongid:" + req.getZoneid() + " failed, Exception : " + e.getMessage(), e);
			}
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Create volume by name:" + req.getName() + ", diskofferingid:" + req.getDiskofferingid() + ", size:"
				        + req.getSize() + ", zongid:" + req.getZoneid() + " command submit succeed");
			}
		}
		return result;
	}

	@GET
	@Path("delete/{pool}/{id}")
	public String deleteVolume(@PathParam("id") int pool, @PathParam("id") int id, @HeaderParam("Accept") String accept) {
		if (logger.isInfoEnabled()) {
			logger.info("REST -- Remove volume by id : " + id);
		}
		String result = "";
		try {
			DeleteVolume cmd = new DeleteVolume();
			cmd.setId(id);
			if (accept.toLowerCase().startsWith(MediaType.APPLICATION_JSON.toLowerCase())) {
				result = String.valueOf(commandService.executeAndJsonReturn(cmd, pool));
			} else {
				result = String.valueOf(commandService.executeAndXMLReturn(cmd, pool));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("REST -- Remove volume by id : " + id + " failed, Exception : " + e.getMessage(), e);
		}
		if (logger.isInfoEnabled()) {
			logger.info("REST -- Remove volume by id : " + id + " command submit succeed");
		}
		return result;
	}

	@POST
	@Path("attach")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String attachVolume(VolumeAttachReq req, @HeaderParam("Accept") String accept) {
		String result = "";
		if (req == null) {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Attach volume failed : paramater missing");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Attach volume by id:" + req.getId() + ", vmid:" + req.getVmId());
			}
			try {
				AttachVolume cmd = new AttachVolume();
				cmd.setId(req.getId());
				cmd.setVirtualmachineid(req.getVmId());
				if (accept.toLowerCase().startsWith(MediaType.APPLICATION_JSON.toLowerCase())) {
					result = String.valueOf(commandService.executeAndJsonReturn(cmd, req.getPoolId()));
				} else {
					result = String.valueOf(commandService.executeAndXMLReturn(cmd, req.getPoolId()));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error("REST -- Attach volume by id:" + req.getId() + ", vmid:" + req.getVmId() + " failed, Exception : " + e.getMessage(), e);
			}
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Attach volume by id:" + req.getId() + ", vmid:" + req.getVmId() + " command submit succeed");
			}
		}
		return result;
	}

	@POST
	@Path("detach")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String detachVolume(VolumeDetachReq req, @HeaderParam("Accept") String accept) {
		String result = "";
		if (req == null) {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Detach volume failed : paramater missing");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Detach volume by id:" + req.getId() + ", vmid:" + req.getVmId());
			}
			try {
				DetachVolume cmd = new DetachVolume();
				cmd.setId(req.getId());
				cmd.setVirtualmachineid(req.getVmId());
				if (accept.toLowerCase().startsWith(MediaType.APPLICATION_JSON.toLowerCase())) {
					result = String.valueOf(commandService.executeAndJsonReturn(cmd, req.getPoolId()));
				} else {
					result = String.valueOf(commandService.executeAndXMLReturn(cmd, req.getPoolId()));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				logger.error("REST -- Detach volume by id:" + req.getId() + ", vmid:" + req.getVmId() + " failed, Exception : " + e.getMessage(), e);
			}
			if (logger.isInfoEnabled()) {
				logger.info("REST -- Detach volume by id:" + req.getId() + ", vmid:" + req.getVmId() + " command submit succeed");
			}
		}
		return result;
	}
}
