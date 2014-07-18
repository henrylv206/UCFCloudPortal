package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.skycloud.management.portal.admin.audit.sevice.IMailSender;
import com.skycloud.management.portal.admin.audit.sevice.SendMailContent;
import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.dao.IUserManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.ListTUserBO;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TRoleBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IRoleManageService;
import com.skycloud.management.portal.admin.sysmanage.service.IUserManageService;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.CreateAccount;
import com.skycloud.management.portal.front.command.res.CreateDomain;
import com.skycloud.management.portal.front.command.res.DeleteAccount;
import com.skycloud.management.portal.front.command.res.DeleteDomain;
import com.skycloud.management.portal.front.command.res.ListAccounts;
import com.skycloud.management.portal.front.command.res.ListDomains;
import com.skycloud.management.portal.front.command.res.UpdateUser;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.instance.service.IJobInstanceInfoService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.task.vdc.util.TaskUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 用户管理业务实现
  *<dl>
  *<dt>类名：UserManageServiceImpl</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午10:09:17</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public class UserManageServiceImpl implements IUserManageService{
	private IUserManageDao userDao;
	private ICommandService commandService;
	private IJobInstanceInfoService jobInstanceService;
	private IRoleManageService roleService;
	private String fromMail;
	private String subject;
	private String text;
	private IMailSender mailSend;
	private final String CLOUD = "cloud";
	private RestTemplate restTemplate;
	private IResourcePoolsDao resourcePoolsDao;

	private final static String activeURL = ConfigManager.getInstance().getString("company.reg.portal.active.url");

	//用户注册时是否需要激活
	private final static String activeFlag= ConfigManager.getInstance().getString("company.reg.portal.active.flag");

	
	private static Logger log = LoggerFactory.getLogger(UserManageServiceImpl.class);
	
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public IRoleManageService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleManageService roleService) {
		this.roleService = roleService;
	}

	public IJobInstanceInfoService getJobInstanceService() {
		return jobInstanceService;
	}

	public void setJobInstanceService(IJobInstanceInfoService jobInstanceService) {
		this.jobInstanceService = jobInstanceService;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public void setUserDao(IUserManageDao userDao) {
		this.userDao = userDao;
	}

	public IUserManageDao getUserDao() {
		return userDao;
	}

	
	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	public IMailSender getMailSend() {
		return mailSend;
	}

	public void setMailSend(IMailSender mailSend) {
		this.mailSend = mailSend;
	}

	@Override
	public List<TUserBO> getAllUsers() throws SQLException {
		return userDao.userALL();
	}

	@Override
	public TUserBO getUserById(int userID) throws SQLException {
		return userDao.findUserById(userID);
	}

	@Override
	public int deleteUser(Integer userID) throws SQLException {
		this.deleteResourcePoolUser(userID);
		return userDao.deleteUser(userID);
	}
	@Override
	public void deleteResourcePoolUser(Integer userID){
		//1.3功能，支持多资源池，查询出所有的资源池
		List<TResourcePoolsBO> poollist = null;
		try {
			poollist = resourcePoolsDao.searchAllPools();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		//循环所有的资源池
		if(null != poollist && poollist.size()>0){
			for (TResourcePoolsBO pool : poollist) {
				this.deleteEAccount(userID,pool.getId());
			}			
		}		
	}

	public IResourcePoolsDao getResourcePoolsDao() {
		return resourcePoolsDao;
	}

	public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
		this.resourcePoolsDao = resourcePoolsDao;
	}

	@Override
	public int insertUser(TUserBO user) throws SQLException {
		int ret = 0;
		try {
			ret = userDao.saveUser(user);
			//如果非VDC,在elaster上面创建用户
			if(ret>0&&ConstDef.curProjectId != 2){
				//在elaster上面创建用户 
				List<TRoleBO> roleList =  roleService.findAllRole();
				user = this.getApproveLevel(roleList, user);
				//1.3功能，支持多资源池，查询出所有的资源池
				List<TResourcePoolsBO> poollist = null;
				try {
					poollist = resourcePoolsDao.searchAllPools();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
				//循环所有的资源池
				if(null != poollist && poollist.size()>0){
					for (TResourcePoolsBO pool : poollist) {
						//检查该用户是否已经存在elater
						Map<String,String> map = jobInstanceService.findEAccount(user,pool.getId());
						if(null==map||map.isEmpty()){					
							CreateAccount cmd = new CreateAccount();		        
					        cmd.setUsername(user.getAccount());
					        cmd.setAccount(user.getAccount());
					        cmd.setEmail(user.getEmail());
					        //fix bug 3187
					        cmd.setFirstname(user.getAccount());
					        cmd.setLastname(user.getAccount());		        
					        cmd.setPassword(this.createEPassword(user));
					        //为帐户创建一个domain,如果审核级别是管理员或超级管理员,无须创建，使用默认domain。to fix 1982		        
					        String domainId = "0";
					        if(3==user.getRoleApproveLevel()||4==user.getRoleApproveLevel()){
					        	//fix bug 7591
					        	domainId = this.getDomainIdByName("ROOT", pool.getId());
					        	cmd.setAccounttype("1");
					        }
					        else {
					        	cmd.setAccounttype("2");
					        	domainId = getNewDomainId(user,pool.getId());				        	        
					        }
					        if(org.apache.commons.lang.StringUtils.isNotBlank(domainId)){
					        	cmd.setDomainid(domainId);	
						        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,pool.getId()));
						        jo = JSONObject.fromObject(jo.getString("createaccountresponse"));
						        //同步account失败 to fix 1873
						        if(jo.containsKey("errorcode")){
						        	ret = -1;
						        }
						        log.debug("createAccount:"+jo);   		        
					        }	
					        else ret = -1;
						}				
					}
					
				}
			}
			//公有云下，自服务用户发送注册成功邮件
			if(1==ConstDef.curProjectId&&ret>0&&1==user.getDeptId()){
				this.sendMail(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = 0;
		}
		return ret;
	}
	
	@Override
	/**
	 * selfcare用户注册
	 * */
	public int registerUser(TUserBO user,TCompanyInfo company) throws SQLException {
		int retselfcare = 0;
		//1.3功能，支持多资源池，查询出所有的资源池
		List<TResourcePoolsBO> poollist = null;
		try {
			poollist = resourcePoolsDao.searchAllPools();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			//循环所有的资源池
			int userId = -1;
			if(null != poollist && poollist.size()>0){
				for (TResourcePoolsBO pool : poollist) {
					//先检查该用户是否已经存在elater，存在则注册不成功 fix bug 2709
					Map<String,String> map = jobInstanceService.findEAccount(user,pool.getId());
					if(null==map||map.isEmpty()){
						//未存在，继续
					}else{
						//在elater已存在，返回提示信息  fix bug 3709
						retselfcare = -2;
						return retselfcare;
					}
					//检查网域是否已存在，存在则注册不成功 fix bug 2709
					Map<String,String> mapDomain = jobInstanceService.findEDomain(user,pool.getId());
					if(null==mapDomain||mapDomain.isEmpty()){
						//未存在，继续
					}else{
						//网域已存在，返回提示信息  fix bug 3709
						retselfcare = -3;
						return retselfcare;
					}
					
					
					if(userId <= 0){//用户注册时，多资源池只需要插入一次门户用户表
						int comId=userDao.saveCompany(company);
						if(comId>0){
							user.setCompId(comId);
							userId = retselfcare = userDao.saveSelfcaerUser(user);
						}
						
					}
					//如果非VDC,在elaster上面创建用户  
					if(retselfcare>0&&ConstDef.curProjectId != 2){
						//在elaster上面创建用户 
						List<TRoleBO> roleList =  roleService.findAllRole();
						user = this.getApproveLevel(roleList, user);
						if(null==map||map.isEmpty()){					
							CreateAccount cmd = new CreateAccount();		        
					        cmd.setUsername(user.getAccount());
					        cmd.setAccount(user.getAccount());
					        cmd.setEmail(user.getEmail());
					        cmd.setFirstname(user.getAccount());
					        cmd.setLastname(user.getAccount());		        
					        cmd.setPassword(this.createEPassword(user));
					        //为帐户创建一个domain,如果审核级别是管理员或超级管理员,无须创建，使用默认domain。to fix 1982		        
					        String domainId = "";
					        if(3==user.getRoleApproveLevel()||4==user.getRoleApproveLevel()){
					        	domainId = this.getDomainIdByName("ROOT", pool.getId());
					        	cmd.setAccounttype("1");
					        }
					        else {
					        	cmd.setAccounttype("2");
					        	domainId = getNewDomainId(user,pool.getId());					        	        
					        }					        	
					        if(domainId != null && !"".equals(domainId)){
					        	cmd.setDomainid(domainId);
						        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,pool.getId()));
						        jo = JSONObject.fromObject(jo.getString("createaccountresponse"));
						        System.out.println(jo);
						        //同步account失败 to fix 1873
						        if(jo.containsKey("errorcode")){
						        	retselfcare = -1;
						        }
						        log.debug("createAccount:"+jo);   		        
					        }	
					        else retselfcare = -1;
						}				
					}
					
				}			
			}
			//公有云下，自服务用户发送注册成功邮件
			if(1==ConstDef.curProjectId&&retselfcare>0&&1==user.getDeptId()){
				//this.sendMail(user);
			}		
		} catch (Exception e) {
			e.printStackTrace();
			retselfcare = 0;
		}
		return retselfcare;
	}
	
	
//	@Override
	/**k
	 * selfcare用户个人信息注册
	 * */
//	public int registerUserSelfinfo(TUserBO user) throws SQLException {
//		int retselfcare = 0;
//		try {
//			//先检查该用户是否已经存在elater，存在则注册不成功 fix bug 2709
//			Map<String,String> map = jobInstanceService.findEAccount(user);
//			if(null==map||map.isEmpty()){
//				//未存在，继续
//			}else{
//				//在elater已存在，返回提示信息  fix bug 3709
//				retselfcare = -2;
//				return retselfcare;
//			}
//			//检查网域是否已存在，存在则注册不成功 fix bug 2709
//			Map<String,String> mapDomain = jobInstanceService.findEDomain(user);
//			if(null==mapDomain||mapDomain.isEmpty()){
//				//未存在，继续
//			}else{
//				//网域已存在，返回提示信息  fix bug 3709
//				retselfcare = -3;
//				return retselfcare;
//			}
//			retselfcare = userDao.saveSelfcaerUser(user);
//			//如果非VDC,在elaster上面创建用户
//			if(retselfcare>0&&ConstDef.curProjectId != 2){
//				//在elaster上面创建用户 
//				List<TRoleBO> roleList =  roleService.findAllRole();
//				user = this.getApproveLevel(roleList, user);
//				if(null==map||map.isEmpty()){					
//					CreateAccount cmd = new CreateAccount();		        
//			        cmd.setUsername(user.getAccount());
//			        cmd.setAccount(user.getAccount());
//			        cmd.setEmail(user.getEmail());
//			        cmd.setFirstname(user.getAccount());
//			        cmd.setLastname(user.getAccount());		        
//			        cmd.setPassword(this.createEPassword(user));
//			        //为帐户创建一个domain,如果审核级别是管理员或超级管理员,无须创建，使用默认domain。to fix 1982		        
//			        int domainId = 0;
//			        if(3==user.getRoleApproveLevel()||4==user.getRoleApproveLevel()){
//			        	domainId = 1;
//			        	cmd.setAccounttype("1");
//			        }
//			        else {
//			        	cmd.setAccounttype("2");
//			        	domainId = getNewDomainId(user);
//			        }
//			        if(domainId>0){
//			        	cmd.setDomainid(""+domainId);	        
//				        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,1));
//				        jo = JSONObject.fromObject(jo.getString("createaccountresponse"));
//				        System.out.println(jo);
//				        //同步account失败 to fix 1873
//				        if(jo.containsKey("errorcode")){
//				        	retselfcare = -1;
//				        }
//				        log.debug("createAccount:"+jo);   		        
//			        }	
//			        else retselfcare = -1;
//				}				
//			}
//			//公有云下，自服务用户发送注册成功邮件
//			if(1==ConstDef.curProjectId&&retselfcare>0&&1==user.getDeptId()){
//				this.sendMail(user);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			retselfcare = 0;
//		}
//		return retselfcare;
//	}
	
	
	/**
	 * 定制elaster密码，仅对自服务用户、审核级别为普通用户、高级用户
	 * */
	private String createEPassword(TUserBO user){
		StringBuilder ePass = new StringBuilder(50);		
		try {			
			//ePass.append(user.getDecPwd());	
			//使用用户名 update by CQ 20130204
			ePass.append(user.getAccount());
			if(1==user.getDeptId()||2==user.getDeptId()||user.getRoleApproveLevel()<3){				
				ePass.append(this.CLOUD);
			}
			ePass =  new StringBuilder(DigestUtils.md5Hex((ePass.toString()).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ePass.toString();
	}
	/**
	 * 获取用户的审核权限等级
	 */	
	private TUserBO getApproveLevel(List<TRoleBO> roleList,TUserBO user){		
		if(null!=roleList&&!roleList.isEmpty()){
			for(TRoleBO role :roleList){
				if(user.getRoleId()== role.getRoleId()){
					user.setRoleApproveLevel(role.getRoleApproveLevel());
				}
			}
		}
		return user;
	}
	
	public void sendMail(TUserBO user) {		
	  SendMailContent content  = new SendMailContent();
	  content.setToMail(user.getEmail());
	  content.setFromMail(fromMail);
	  content.setSubject(this.getSubject());
	  StringBuilder text = new StringBuilder(this.getText());
	  int index = text.indexOf("#username#");		  
	  text = text.replace(index, index+10, user.getAccount());
	  index = text.indexOf("#password#");
	  content.setSendText(text.replace(index, index+10, user.getDecPwd()).toString());
	  index = text.indexOf("#activeUrl#");
	  String activeUrl = activeURL;
	  activeUrl = activeUrl + "?activeCode=" + user.getCheckCode();
	  activeUrl = activeUrl + "&loginacct=" + user.getAccount();
//	  activeUrl = activeUrl + "&password" + user.getDecPwd();
//	  content.setSendText(text.replace(index, index+11, activeUrl).toString());
//	  index = text.indexOf("#activeCode#");
//	  content.setSendText(text.replace(index, index+12, activeUrl).toString());
	  if("yes".equals(activeFlag)){//需要激活
		  content.setSendText(text.replace(index, index+11, activeUrl).toString());
	  }else{//不需要激活
		  content.setSendText(text.replace(index, index+11, "#").toString());
	  }
	  index = text.indexOf("#activeCode#");
	  if("yes".equals(activeFlag)){//需要激活
		  content.setSendText(text.replace(index, index+12, user.getCheckCode()).toString());
	  }else{//不需要激活
		  content.setSendText(text.replace(index, index+12, "不需要激活").toString());
	  }
	  
	  content.setToMobile(user.getMobile());
	  if(ConfigManager.getInstance().containsKey("sms.reg.success.text")){
		  content.setSendSmsText(String.format(ConfigManager.getInstance().getString("sms.reg.success.text"), user.getAccount(),user.getDecPwd()));
	  }
	  mailSend.sendMail(content);		
	}
	private String getNewDomainId(TUserBO user,int resourcePoolsId) throws Exception{
		String domainId = "";
		CreateDomain cmd = new CreateDomain();
        cmd.setName(user.getAccount());  
        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,resourcePoolsId));
        log.info(jo.toString());
		jo = JSONObject.fromObject(jo.getString("createdomainresponse"));
		if (jo.containsKey("domain")) {
			jo = JSONObject.fromObject(jo.getString("domain"));
			domainId = jo.getString("id");
		}		
		log.debug("createDomain:"+domainId);   
        return domainId;
	}
	
	private String getDomainIdByName(String name,int  resourcePoolsId)throws Exception{
		String domainId = "";
		ListDomains cmd = new ListDomains();
        cmd.setDomainName(name);  
        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,resourcePoolsId));
        log.info(jo.toString());
		jo = JSONObject.fromObject(jo.getString("listdomainsresponse"));
		if (jo.containsKey("domain")) {
			JSONArray ja = jo.getJSONArray("domain");
			if(null!=ja&&!ja.isEmpty()){
				jo = JSONObject.fromObject(ja.get(0));
				domainId = jo.getString("id");
			}			
		}		
		log.debug("listDomains:"+domainId);   
        return domainId;
	}


	@Override
		public int updateUser(TUserBO user)
				throws Exception {
			//如果用户状态修改为挂起，需要挂起资源
			if(CompanyCheckStateEnum.PAUSE == user.getState()){
				try {
					jobInstanceService.updateInstanceByUser(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//如果用户状态修改为注销，需要释放资源,测试用
//			else if(CompanyCheckStateEnum.FAILURE == user.getState()){
//				jobInstanceService.deleteInstanceByUser(user);
//				jobInstanceService.disabledEAccount(user);
//			}
			int ret = userDao.udpateUser(user);
			if(ret>0&&StringUtils.isNotBlank(user.getPwd())){
				this.updateObjectStorePwd(user);
			}
			return ret;
		}

	@Override
	public List<TUserBO> queryUser(QueryCriteria criteria)
	
			throws SQLException {
		return userDao.searchUser(criteria);
	}

	@Override
	public TUserBO getUserByAccout(String account) throws SQLException {
		return userDao.findUserByAccout(account);
	}

	@Override
	public int updateUserPwd(TUserBO user) throws SQLException {
		int ret = 0;
		//1.3功能，支持多资源池，查询出所有的资源池
		List<TResourcePoolsBO> poollist = null;
		try {
			poollist = resourcePoolsDao.searchAllPools();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}		
		//如果审核级别是管理员或超级管理员,修改elaster的account密码
		if(3==user.getRoleApproveLevel()||4==user.getRoleApproveLevel()){
			Map<String,String> eUserMap = jobInstanceService.findEUser(user);
			if(null!=eUserMap&&!eUserMap.isEmpty()){
				
				//循环所有的资源池
				if(null != poollist && poollist.size()>0){
					for (TResourcePoolsBO pool : poollist) {
						UpdateUser cmd = new UpdateUser();
				        cmd.setId(eUserMap.get("userid"));
				        cmd.setPassword(user.getPwd());
				        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,pool.getId()));
				        jo = JSONObject.fromObject(jo.getString("updateuserresponse"));
				        log.debug("updateuserresponse :"+jo); 		
					}			
				}
				
			}
			else log.info("Find user from ELASTER is NULL");
		}	
		ret = userDao.updateUserPwd(user);
		if(ret == 1){
			this.updateObjectStorePwd(user);
		}
		return ret;
	}
	@Override
	public void updateObjectStorePwd(TUserBO user) throws SQLException{
		//是否已经购买过对象存储
		//String url =  "http://172.16.211.40:8080/OStore/ostore/useradmin/{username}";
		String url = TaskUtils.getOStorePwdURI();
		List<TResourcePoolsBO> poollist = resourcePoolsDao.searchAllPools();
		try {			
			//modified by zhanghuizheng to fix bug 6750
			//循环所有的资源池 
			if(null != poollist && poollist.size()>0){
				for (TResourcePoolsBO pool : poollist) {
					String ip = pool.getIp();
					String allocated = this.getObjectStoreByUser(user,ip);
					if(StringUtils.isNotBlank(allocated)){						
						HttpHeaders headers = new HttpHeaders();
						headers.add("password", user.getPwd());
						//fix bug 6830
						//headers.add("allocated", allocated);
						HttpEntity<String> entity = new HttpEntity<String>("ostorePwd", headers);

						url = url.replace("127.0.0.1", ip);
						restTemplate.put(url,entity,user.getAccount());
					}
				}			
			}				
			log.info( "{} has updated password for ObjectStore",user.getAccount());					
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		
	}
	private String getObjectStoreByUser(TUserBO user,String ip){
		String allocated = null;
		try{
			String url = TaskUtils.getOStoreUserURI();		
			url = url.replace("127.0.0.1", ip);
			Map<String,String> urlVariables = new HashMap<String,String>();			
			JSONObject jo = JSONObject.fromObject(restTemplate.getForObject(url, String.class,user.getAccount()));
			if(null!=jo&&jo.containsKey("allocated")){
				allocated = jo.getString("allocated");
			}
			log.info( "{} getAllocated:{} from ObjectStore",user.getAccount(),allocated);
		}
		catch(Exception ex){
			log.info(ex.getMessage());
			return allocated;
		}		
		return allocated;
	}
	
	@Override
	public List<TUserBO> queryUserForFront() throws SQLException {
		return userDao.queryUserForFront(null);
	}
	

	@Override
	public String exportUser(List<TUserBO> list) {
		String xml = null;
		if(null!=list&&!list.isEmpty()){
			XStream xstream = new XStream(new  DomDriver("UTF-8"));
			xstream.alias("root", ListTUserBO.class);
			xstream.alias("user", TUserBO.class);
			ListTUserBO rootList = new ListTUserBO();
			rootList.setListUser(list);			
			xstream.setMode(XStream.NO_REFERENCES);//不引用
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+xstream.toXML(rootList);
		}	
		log.debug(xml);
		//System.out.println(xml);
		return xml;
	}
	private void deleteEAccount(int id,int resourcePoolsId){
		try {
			TUserBO user = this.getUserById(id);
			if(null!=user&&user.getId()>0){
				Map<String,String> userMap = jobInstanceService.findEAccount(user,resourcePoolsId);
				if(null!=userMap&&!userMap.isEmpty()){	
					//删除帐户
					DeleteAccount cmd = new DeleteAccount();
					cmd.setId(userMap.get("accountid"));
					JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,resourcePoolsId));
					Thread.sleep(1000);
					//删除domain fix bug 3721
					if(!"1".equals(userMap.get("domainid"))){
						DeleteDomain dCmd = new DeleteDomain();
						dCmd.setId(userMap.get("domainid"));
						jo = JSONObject.fromObject(commandService.executeAndJsonReturn(dCmd,resourcePoolsId));
					}					
				}				
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//to fix 1919
	@Override
	public List<ResourcesVO> getAllInstanceByUser(TUserBO user)throws SCSException{
		return jobInstanceService.queryAllInstanceByUser(user);
	}
	/**判断邮箱是否存在,范围包括企业帐号的邮箱和子帐号的邮箱*/
	@Override
	public boolean checkEmailIsExist(String email){
		boolean flg = false;		
		try {
			int count = userDao.checkEmail(email);			
			if(count>0){
				flg = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return flg;
	}
	@Override
	public int updateUserEmail(TUserBO user)throws SQLException{
		return userDao.updateUserEmail(user);
	}
	/**
	 * 1.3功能，支持多资源池，查询所有的资源池
	 * @return
	 */
	public List<TResourcePoolsBO> getAllResourcePools(){
		List<TResourcePoolsBO> poollist = null;
		try {
			//查询出所有的资源池
			poollist = resourcePoolsDao.searchAllPools();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return poollist;
	}
	/**************未同步的用户功能 start**********************/
	@Override
	public List<Map<String,Object>> queryNotExistUserAsync(TUserBO user) throws Exception{
		List<Map<String,Object>> extAllList = new ArrayList<Map<String,Object>>();
		List<TUserBO> dbUser = userDao.queryUserForAsync(user);
		if(user.getResPoolId()<1){
			List<TResourcePoolsBO> poolList = this.getAllResourcePools();		
			if(null!=poolList&&!poolList.isEmpty()){
				for(TResourcePoolsBO pool: poolList){					
					List<TUserBO> userList = new ArrayList<TUserBO>(dbUser) ;
					List<TUserBO> estList = this.getNotExistUser(userList,pool.getId());	
					extAllList.add(this.initExistUserMap(estList, pool.getId()));
				}
			}
		}
		else {
			List<TUserBO> estList = this.getNotExistUser(dbUser,user.getResPoolId());
			extAllList.add(this.initExistUserMap(estList, user.getResPoolId()));
		}
		return extAllList;
	}
	private Map<String,Object> initExistUserMap(List<TUserBO> estList,int resPoolId){
		Map<String,Object> extMap = null;
		if(null!=estList&&!estList.isEmpty()){
			extMap = new HashMap<String,Object>();
			extMap.put("resPoolId", resPoolId);
			extMap.put("list", estList);
		}
		return extMap;		
	}

	private  List<TUserBO> getNotExistUser(List<TUserBO> dbUser,int resPoolId){	
		//测试数据
		//TUserBO user = dbUser.get(0);
		//user.setAccount("noexist");
		List<TUserBO> eAccountList = this.queryElasterAccount(resPoolId); 
		if(null!=eAccountList&&!eAccountList.isEmpty()){
			dbUser.removeAll(eAccountList);
		}		
		return	dbUser;	
	}
	
	public List<TUserBO> queryElasterAccount(int resourcePoolsId){
		List<TUserBO> eAccountList = null;
		try {
			// 从elaster中取出所有的用户
			ListAccounts cmd_listaccounts = new ListAccounts();
			JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd_listaccounts,
					resourcePoolsId));
			if(null != jo ){
				JSONArray arrays = null;
				if (jo.containsKey("listaccountsresponse")) {
					jo = jo.getJSONObject("listaccountsresponse");
					if (jo.containsKey("account")) {
						arrays = jo.getJSONArray("account");
						if (null != arrays && arrays.size() > 0) {
							eAccountList = new ArrayList<TUserBO>();
							for (int i = 0; i < arrays.size(); i++) {
								TUserBO user = new TUserBO();
								JSONObject jAccount = JSONObject.fromObject(arrays
										.get(i));
								String name = String.valueOf(jAccount.get("name"));
								user.setAccount(name);
								eAccountList.add(user);
							}
						}	
					}
				}							
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eAccountList;
	}
	@Override
	public int manualAsyncUser(TUserBO user) throws SQLException{
		int ret = 0;
		//在elaster上面创建用户 
		List<TRoleBO> roleList =  roleService.findAllRole();
		user = this.getApproveLevel(roleList, user);
		//1.3功能，支持多资源池，查询出所有的资源池
		List<TResourcePoolsBO> poollist = null;
		try {
			//检查该用户是否已经存在elater
			int resPoolId = user.getResPoolId();
			Map<String,String> map = jobInstanceService.findEAccount(user,resPoolId);
			if(null==map||map.isEmpty()){					
				CreateAccount cmd = new CreateAccount();		        
		        cmd.setUsername(user.getAccount());
		        cmd.setAccount(user.getAccount());
		        cmd.setEmail(user.getEmail());
		        //fix bug 3187
		        cmd.setFirstname(user.getAccount());
		        cmd.setLastname(user.getAccount());		        
		        cmd.setPassword(this.createEPassword(user));
		        //为帐户创建一个domain,如果审核级别是管理员或超级管理员,无须创建，使用默认domain。to fix 1982		        
		        String domainId = "";
		        if(3==user.getRoleApproveLevel()||4==user.getRoleApproveLevel()){
		        	domainId = "1";
		        	cmd.setAccounttype("1");
		        }
		        else {
		        	cmd.setAccounttype("2");
		        	domainId = getNewDomainId(user,resPoolId);
		        	cmd.setDomainid(domainId);	        
		        }
		        if(domainId != null && !"".equals(domainId)){
			        JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd,resPoolId));
			        jo = JSONObject.fromObject(jo.getString("createaccountresponse"));
			        //同步account失败 to fix 1873
			        if(jo.containsKey("errorcode")){
			        	ret = -1;
			        }
			        ret = 1;
			        log.debug("createAccount:"+jo);   		        
		        }	
		        else ret = -1;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return ret;		
	}
	/**************未同步的用户功能 end**********************/
}
