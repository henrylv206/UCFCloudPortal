package com.skycloud.management.portal.front.customer.action;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IMailSender;
import com.skycloud.management.portal.admin.audit.sevice.SendMailContent;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserManageService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.DegistUtil;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.customer.entity.TCompUgroup;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;
import com.skycloud.management.portal.front.customer.service.IAdminService;
import com.skycloud.management.portal.front.log.aop.LogInfo;


public class AdminAction extends BaseAction {
	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(AdminAction.class);
	private static final long serialVersionUID = 7598058935180381702L;

	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private IAdminService customeruserService;
	//private LoginService loginService;
	private IUserManageService userService;
	//private PermissionServiceImpl perService;


	private Integer companyId;
	private Integer userId;
	private Integer vmResId;
	private Integer groupId;
	private String newPwd;
	private String oldPwd;
	private String userCreateStartTime;
	private String userCreateEndTime;
	private List<TCompUgroup> groups;

	private String msg;

	private String username;
	private String password;
	private String state;
	private String email;
	private String activeCode;

	private String blnStartTime;
	private String blnEndTime;

	private String zhanghao;

	private int loginType;

	// 验证码
	private String yzm;

	private TCompanyInfo tcompanyinfo;
	private TCompanyInfo company;


	private TUserBO user;
	private TUserBO admin;

	private IMailSender smsSend;

	//公有云普通用户组
	private final static int DEPT_PUBLIC = 1;
	//私有云普通用户组
	private final static int DEPT_PRIVATE = 2;
	//用户注册时是否需要激活
	private final static String activeFlag= ConfigManager.getInstance().getString("company.reg.portal.active.flag");


	/***********getter and setter start******************/
	

	public TCompanyInfo getCompany() {
		return company;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	public TUserBO getAdmin() {
		return admin;
	}
	public void setAdmin(TUserBO admin) {
		this.admin = admin;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public void setCompany(TCompanyInfo company) {
		this.company = company;
	}

	public String getZhanghao() {
		return zhanghao;
	}

	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	public IUserManageService getUserService() {
		return userService;
	}
	
	public void setUserService(IUserManageService userService) {
		this.userService = userService;
	}
	public IAdminService getCustomeruserService() {
		return customeruserService;
	}
	public void setCustomeruserService(IAdminService customeruserService) {
		this.customeruserService = customeruserService;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public IMailSender getSmsSend() {
		return smsSend;
	}

	public void setSmsSend(IMailSender smsSend) {
		this.smsSend = smsSend;
	}
	public String getUserCreateStartTime() {
		return userCreateStartTime;
	}
	public void setUserCreateStartTime(String userCreateStartTime) {
		this.userCreateStartTime = userCreateStartTime;
	}
	public String getUserCreateEndTime() {
		return userCreateEndTime;
	}
	public void setUserCreateEndTime(String userCreateEndTime) {
		this.userCreateEndTime = userCreateEndTime;
	}

	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}


	public Integer getVmResId() {
		return vmResId;
	}

	public void setVmResId(Integer vmResId) {
		this.vmResId = vmResId;
	}




	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}



	public String getBlnStartTime() {
		return blnStartTime;
	}

	public void setBlnStartTime(String blnStartTime) {
		this.blnStartTime = blnStartTime;
	}

	public String getBlnEndTime() {
		return blnEndTime;
	}

	public void setBlnEndTime(String blnEndTime) {
		this.blnEndTime = blnEndTime;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}


	public TCompanyInfo getTcompanyinfo() {
		return tcompanyinfo;
	}
	public void setTcompanyinfo(TCompanyInfo tcompanyinfo) {
		this.tcompanyinfo = tcompanyinfo;
	}
	public TUserBO getUser() {
		return user;
	}
	public void setUser(TUserBO user) {
		this.user = user;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	/**
	 * 登录验证Action<br>
	 * 包括企业管理员登录和企业子账号登录(优先验证)<br>
	 * 
	 * @return state 登录状态标识<br>
	 */
	@LogInfo(desc="用户登录",moduleName="自服务平台用户管理",functionName="用户登录",operateType=4,parameters="username")
	public String login()  {

		//		boolean flag1 = false;
		//		boolean flag2 = false;
		//		boolean flag3 = false;
		TUserBO user = null;
		password = DegistUtil.md5(password);
		if (loginType == 1) {// 静态密码登陆
			//共有云
			if(1==ConstDef.getCloudId()){
				user = customeruserService.checkUser(username, password,DEPT_PUBLIC);
			}
			//to fix 1970
			else if(2==ConstDef.getCloudId()){
				user = customeruserService.checkUser(username, password);
			}
		} else if (loginType == 2) {// 随机密码登陆
			user = customeruserService.findAdminByAccount(username);
		}
		try {
			if(null!=user){
				if(user.getState()==CompanyCheckStateEnum.WAITING_FOR_2ND_CHECK){
					this.setState("inactive");
				}
				//fix bug 6734
				else if(user.getState()==CompanyCheckStateEnum.PAUSE){
					this.setState("pause");
				}else if(null == user.getCheckCode() || "".equals(user.getCheckCode())){
					//				List<TMenuBO> menus = loginService.rebuildMenu(perService.queryMenusByDeptId(user.getDeptId()));
					if (loginType == 1) {
						//私有云，仅普通用户登录portal fix bug 7380 7379 7375
						if(2==ConstDef.getCloudId()&&user.getRoleApproveLevel()>1){
							this.setState("authorize");
						}
						else {
							this.getSession().removeAttribute(ConstDef.SESSION_KEY_USER);
							this.getSession().setAttribute(ConstDef.SESSION_KEY_USER, user);
							this.setState("true");
						}
					} else if (loginType == 2) {
						String dypw = user.getDynPwd();
						if (dypw.toLowerCase().equalsIgnoreCase(password)) {
							this.getSession().removeAttribute(ConstDef.SESSION_KEY_USER);
							this.getSession().setAttribute(ConstDef.SESSION_KEY_USER, user);
							this.setState("true");
							user.setDynPwd("");
							// 登陆成功后清除随机密码
							customeruserService.saveDynPwd(user);
						} else {
							this.setState("false");
						}
					}
				}else{
					this.setState("notactive");//fix buf 3040 用户激活后才允许登录
				}
			} else {
				this.setState("false");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SUCCESS;
	}

	/**
	 * 添加子账户
	 * 
	 */
	@LogInfo(desc="用户注册",moduleName="自服务平台用户管理",functionName="用户注册",operateType=1)
	public String register() throws Exception {
		//未编码的密码，创建elaster用户使用
		user.setDecPwd(user.getPwd());
		user.setPwd(DegistUtil.md5(user.getPwd()));
		try {
			if(user.getAccount()!=null){
				user.setAccount(user.getAccount().trim());
				user.setRoleId(1);
				user.setDeptId(1);
			}
			if(userService.getUserByAccout(user.getAccount())==null){
				tcompanyinfo.setCheckState(CompanyCheckStateEnum.SUCCESS);
				blnStartTime += " 00:00:00";
				blnEndTime += " 00:00:00";
				tcompanyinfo.setBlnStartTime(Timestamp.valueOf(blnStartTime));
				tcompanyinfo.setBlnEndTime(Timestamp.valueOf(blnEndTime));
				tcompanyinfo.setCompRegTime(new Timestamp(System
				                                          .currentTimeMillis()));// 当前时间
				tcompanyinfo.setCompCreateTime(new Timestamp(System
				                                             .currentTimeMillis()));// 当前时间

				user.setState(CompanyCheckStateEnum.SUCCESS);
				user.setCreatorUserId(1);
				user.setCreateDt(new Date(System.currentTimeMillis()));
				user.setLastupdateDt(new Date(System.currentTimeMillis()));
				int ret = customeruserService.insertUserAndCompany(user, tcompanyinfo);
				if(ret > 0){
					this.state="true";
				}
				else if(ret == -1){
					this.state="elaster";
				}
				else if(ret == 0){
					this.state="false";
				}

			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			this.state="false";
			return ERROR;
		}
	}


	/**
	 * 添加selfcare个人信息
	 * 
	 */
	@LogInfo(desc="用户注册",moduleName="自服务平台用户管理",functionName="用户注册",operateType=1)
	public String insertUserInfo() throws Exception {
		//未编码的密码，创建elaster用户使用
		//		user.setDecPwd(user.getPwd());
		//		user.setPwd(DegistUtil.md5(user.getPwd()));
		TUserBO user = (TUserBO)this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if(null==user){
			return ERROR;
		}
		try {
			tcompanyinfo.setCheckState(CompanyCheckStateEnum.SUCCESS);
			if(blnStartTime != null){
				blnStartTime += " 00:00:00";
				tcompanyinfo.setBlnStartTime(Timestamp.valueOf(blnStartTime));
			}
			if(blnEndTime != null){
				blnEndTime += " 00:00:00";
				tcompanyinfo.setBlnEndTime(Timestamp.valueOf(blnEndTime));
			}
			int ret = 0;
			if(null!=tcompanyinfo&&tcompanyinfo.getCompId()>0){
				ret =  this.updateCompInfo();
			}
			else {
				ret = customeruserService.insertUserAndCompanyInfo(user, tcompanyinfo);
				//对于新增企业信息，更新session中的值 fix bug 3301
				this.getSession().setAttribute(ConstDef.SESSION_KEY_USER,user);
			}
			//修改用户邮箱
			if(StringUtils.isNotEmpty(tcompanyinfo.getRelaEmail())){
				user.setEmail(tcompanyinfo.getRelaEmail());
				userService.updateUserEmail(user);
			}
			if(ret > 0){
				this.state="true";
			}
			else if(ret == 0){
				this.state="false";
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			this.state="false";
			return ERROR;
		}
	}
	/**
	 * 判断验证码是否正确<br>
	 * 
	 * @author wangcf
	 * @return state
	 * @throws Exception
	 */
	@LogInfo(desc="验证码校验",moduleName="自服务平台用户管理",functionName="验证码校验",operateType=4)
	public String verifyCode() throws Exception {
		try {
			String validateC = (String) this.getSession().getAttribute(
			"validateCode");
			String veryCode = yzm;
			//没有输入验证码的情况
			if (veryCode == null || "".equals(veryCode)) {
				this.state = "false";
				return SUCCESS;
			}
			// session超时的情况
			if (validateC == null) {
				this.state = "timeout";
				return SUCCESS;
			}
			//正常验证对错
			if (validateC.equalsIgnoreCase(veryCode)) {
				this.state = "true";
			} else {
				this.state = "false";
			}

		} catch (Exception e) {
			this.state = "false";
		}
		return SUCCESS;
	}

	/**
	 * 发送随机密码<br>
	 * 
	 * @author hetao
	 * @return state
	 * @throws Exception
	 */
	@LogInfo(desc = "发送随机密码", moduleName = "自服务平台用户登陆", functionName = "发送随机密码", operateType = 1)
	public String getDynCode() throws Exception {
		SendMailContent content = new SendMailContent();
		try {
			// 创建一个随机数生成器类
			Random random = new Random();
			String dypw = "";
			for (int i = 0; i < 6; i++) {
				// 得到随机产生的数字。
				String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
				dypw += strRand;
			}
			TUserBO userBO = customeruserService.findAdminByAccount(zhanghao);
			String dynPwd = userBO.getDynPwd();
			if (StringUtils.isEmpty(dynPwd)) {
				userBO.setDynPwd(DegistUtil.md5(dypw));
				// 随机密码暂存
				customeruserService.saveDynPwd(userBO);
				content.setToMobile(userBO.getMobile());
				content.setSendSmsText(dypw);
				// 发送短信
				smsSend.sendMail(content);
			}
			this.state = "true";
			return SUCCESS;
		}
		catch (Exception ex) {
			this.state = "false";
		}
		return SUCCESS;
	}

	/**
	 * 随机密码失效<br>
	 * 
	 * @author hetao
	 * @return state
	 * @throws Exception
	 */
	@LogInfo(desc = "随机密码失效", moduleName = "自服务平台用户登陆", functionName = "随机密码失效", operateType = 1)
	public String dynCodeTimout() throws Exception {
		TUserBO userBO = customeruserService.findAdminByAccount(zhanghao);
		try {
			userBO.setDynPwd("");
			customeruserService.saveDynPwd(userBO);
			this.state = "true";
		}
		catch (Exception ex) {
			this.state = "false";
		}
		return SUCCESS;
	}


	public String findCompInfoById(){
		try {

			company = customeruserService.findCompInfoById(companyId);
			// System.out.println("action:"+company.getCompCnName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getSessioninfo(){
		try {
			user = (TUserBO)this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 更改admin密码
	 */
	@LogInfo(desc="用户密码修改",moduleName="自服务平台用户管理",functionName="用户密码修改",operateType=2)
	public String updateAdminPwd(){
		try {
			TUserBO session = (TUserBO)this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
			//		TUserBO compUser = customeruserService.findAdminById(session.getId());
			if(session.getPwd().equals(DegistUtil.md5(oldPwd))){
				session.setPwd(DegistUtil.md5(newPwd));
				//fix bug 6750 传未加密的明码
				session.setDecPwd(newPwd);
				userService.updateUserPwd(session);
				this.state="true";
				return SUCCESS;
			}else{
				this.state="invalide";
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.state="false";
			return ERROR;
		}
	}
	
	public int updateCompInfo(){
		int ret = 0;
		try {

			if(null!=tcompanyinfo&&tcompanyinfo.getCompId()>0){
				//				blnStartTime += " 00:00:00";
				//				blnEndTime += " 00:00:00";
				if(blnStartTime != null) {
					tcompanyinfo.setBlnStartTime(Timestamp.valueOf(blnStartTime));
				}
				if(blnEndTime != null) {
					tcompanyinfo.setBlnEndTime(Timestamp.valueOf(blnEndTime));
				}
				tcompanyinfo.setCompRegTime(new Timestamp(System
				                                          .currentTimeMillis()));// 当前时间
				tcompanyinfo.setCompCreateTime(new Timestamp(System
				                                             .currentTimeMillis()));// 当前时间
			}
			ret = customeruserService.updateCompany(user,tcompanyinfo);

		} catch (Exception e) {
			e.printStackTrace();
			ret = 0;
		}
		return ret;
	}
	
	@LogInfo(desc="用户查询",moduleName="自服务平台用户管理",functionName="用户查询",operateType=4,parameters="userId")
	public String findAdminByAccount(){
		try {
			admin = customeruserService.findAdminByAccount(username);
			if(null != admin){
				this.state="true";

			}else{
				this.state="false";
			}
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.state="error";
			return ERROR;
		}
	}
	/** 获得所有企业组信息 */
	public String findAllCompanyGroup() {

		try {
			TUserBO session = (TUserBO)this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
			//			groups = customeruserService.findCurentAllCompUgroup(session.getCompId());
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	@LogInfo(desc="用户登录",moduleName="自服务平台用户管理",functionName="用户退出",operateType=4)
	public String frontLogout() throws SQLException {
		try {
			Object user = this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
			if (user != null) {
				this.getSession().removeAttribute(ConstDef.SESSION_KEY_USER);
			}
			this.getSession().invalidate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		return SUCCESS;
	}
	public String checkEmailIsExist(){
		try{
			boolean flg = userService.checkEmailIsExist(email);
			if(flg){
				state="true";
			}
			else {
				state = "false";
			}
			return SUCCESS;
		}
		catch (Exception e) {
			state = "false";
			return ERROR;
		}
	}
	
}
