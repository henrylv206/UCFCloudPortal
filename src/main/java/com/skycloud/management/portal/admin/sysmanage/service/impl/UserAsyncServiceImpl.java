package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.dao.IUserManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TRoleBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IRoleManageService;
import com.skycloud.management.portal.admin.sysmanage.service.IUserAsyncService;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.front.command.res.CreateAccount;
import com.skycloud.management.portal.front.command.res.CreateDomain;
import com.skycloud.management.portal.front.command.res.ListAccounts;
import com.skycloud.management.portal.front.command.res.ListDomains;
import com.skycloud.management.portal.front.command.res.ListUsers;
import com.skycloud.management.portal.front.instance.service.ICommandService;

/**
 * 跟elaster同步用户实现
 * <dl>
 * <dt>类名：UserAsyncServiceImpl</dt>
 * <dd>描述: 扫描在elater上不存在的用户,并把该用户同步到elater系统中</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2012-5-17 下午16:00:00</dd>
 * <dd>创建人： 张慧征</dd>
 * </dl>
 * to fix bug:1809
 */
public class UserAsyncServiceImpl implements IUserAsyncService {
	private IUserManageDao userDao;
	private ICommandService commandService;
	private IRoleManageService roleService;
	private final String CLOUD = "cloud";
	private IResourcePoolsDao resourcePoolsDao;

	private static Log log = LogFactory.getLog(UserAsyncServiceImpl.class);

	public IRoleManageService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleManageService roleService) {
		this.roleService = roleService;
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

	public IResourcePoolsDao getResourcePoolsDao() {
		return resourcePoolsDao;
	}

	public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
		this.resourcePoolsDao = resourcePoolsDao;
	}

	/**
	 * 查询出所有的资源池，在所有的资源池上同步用户
	 * 
	 * @return
	 */
	public int addNotAsyncUserForAllResourcePools() {
		try {
			// 查询出所有的资源池
			List<TResourcePoolsBO> poollist = resourcePoolsDao.searchAllPools();
			List<TUserBO> allUsers = null;
			List<TUserBO> existlist = null;
			// 循环所有的资源池
			if (null != poollist && poollist.size() > 0) {
				for (TResourcePoolsBO pool : poollist) {
					this.addNotAsyncUser(pool.getId(), allUsers, existlist);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int addNotAsyncUser(Integer resourcePoolsId, List<TUserBO> allUsers,
			List<TUserBO> existlist) throws SQLException {
		try {
			// 如果非VDC,跟elaster同步用户
			if (ConstDef.curProjectId != 2) {
				List<TUserBO> notExistUsers = this.getNotExistAccounts(
						resourcePoolsId, allUsers, existlist);
				if (null != notExistUsers && notExistUsers.size() > 0) {
					for (TUserBO user : notExistUsers) {
						this.createUser(user, resourcePoolsId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<TUserBO> getNotExistAccounts(Integer resourcePoolsId,
			List<TUserBO> allUsers, List<TUserBO> existlist)
			throws SQLException {
		allUsers = userDao.queryUserForAsync();
		if (null != allUsers && allUsers.size() > 0) {
			List<TUserBO> existUsers = this.getExistAccounts(resourcePoolsId,
					allUsers, existlist);
			if (null != existUsers && existUsers.size() > 0) {				
				for (TUserBO existUser : existUsers) {
					allUsers.remove(existUser);
				}
			}
		}
		return allUsers;
	}

	private void createUser(TUserBO user, Integer resourcePoolsId)
			throws Exception {
		JSONObject domain = getDomain(user.getAccount(), resourcePoolsId);
		int domainId = 0;
		if (null == domain) {
			// 为帐户创建一个domain
			domainId = createDomain(user, resourcePoolsId);
			// 创建account
			this.createAccount(user, domainId, resourcePoolsId);
		} else {
			if (!isExistUser(user.getAccount(), resourcePoolsId)) {
				String _id = String.valueOf(domain.get("id"));
				if (null != _id && !_id.equals("")) {
					domainId = Integer.parseInt(_id);
					// 创建account
					this.createAccount(user, domainId, resourcePoolsId);
				}
			}
		}
		//fix bug 5006		
	}

	/**
	 * 从云平台的用户表中取出所有的用户，从elaster中取出所有的用户，进行对比，把云平台中存在而elaster中不存在的用户放入list中，
	 * 以便进行同步
	 * 
	 * @return
	 */
	private List<TUserBO> getExistAccounts(Integer resourcePoolsId,
			List<TUserBO> allUsers, List<TUserBO> existlist) {

		try {
			// 从elaster中取出所有的用户
			ListAccounts cmd_listaccounts = new ListAccounts();
			JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd_listaccounts,
					resourcePoolsId));
			//System.out.println(jo);
			if(null != jo ){
				JSONArray arrays = null;
				if (jo.containsKey("listaccountsresponse")) {
					jo = jo.getJSONObject("listaccountsresponse");
					if (jo.containsKey("account")) {
						arrays = jo.getJSONArray("account");
					}
				}
				if (null != arrays && arrays.size() > 0) {
					existlist = new ArrayList<TUserBO>();
					for (TUserBO user : allUsers) {
						for (int i = 0; i < arrays.size(); i++) {
							JSONObject jAccount = JSONObject.fromObject(arrays
									.get(i));
							String _name = String.valueOf(jAccount.get("name"));
							if (user.getAccount().equals(_name)) {
								existlist.add(user);
								break;
							}
						}
					}
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return existlist;

	}

	/**
	 * 根据account查询查询user是否存在
	 * 
	 * @param account
	 * @return
	 */
	private boolean isExistUser(String account, Integer resourcePoolsId) {
		ListUsers cmd_listusers = new ListUsers();
		cmd_listusers.setUsername(account);
		Object _obj = commandService.executeAndJsonReturn(cmd_listusers,
				resourcePoolsId);
		boolean hasOne = false;
		
		if (null != _obj) {
			JSONObject jo = JSONObject.fromObject(_obj);
			JSONArray array = null;
			if(jo.containsKey("listusersresponse")){
				jo = jo.getJSONObject("listusersresponse");
				if(jo.containsKey("user")){
					array = jo.getJSONArray("user");
					if(null != array && array.size()>0){
						for(int i=0;i<array.size();i++){
							JSONObject juser = JSONObject.fromObject(array.get(i));
							String _name = juser.getString("username");
							if(_name.equals(account)){
								hasOne = true;
								break;
							}
						}						
					}
				}
			}
		}

		return hasOne;
	}

	/**
	 * 根据account查询查询domain是否存在,并返回domain
	 */
	private JSONObject getDomain(String account, Integer resourcePoolsId) {
		// 根据account字段查询,api?command=listDomains&name=
		ListDomains cmd_listdomains = new ListDomains();
		cmd_listdomains.setDomainName(account);
		Object obj = commandService.executeAndJsonReturn(cmd_listdomains,
				resourcePoolsId);
		JSONObject result = null;
		if(null != obj && !(obj.toString().isEmpty()) && obj.toString().contains("{")){
			JSONObject jo = JSONObject.fromObject(obj);
			if(jo.containsKey("listdomainsresponse")){
				jo = jo.getJSONObject("listdomainsresponse");
				if(jo.containsKey("domain")){
					JSONArray array = jo.getJSONArray("domain");
					if(null != array && !array.isEmpty()){
						for(int i=0;i<array.size();i++){
							JSONObject jdomain = JSONObject.fromObject(array.get(i));
							String _name = jdomain.getString("name");
							if(_name.equals(account)){
								// hasOne = true;
								result = jdomain;
								break;
							}
						}
					}
				}
			}			
		}
		return result;
	}

	/**
	 * 在elaster上面创建account
	 */
	private void createAccount(TUserBO user, int domainId,
			Integer resourcePoolsId) throws Exception {
		List<TRoleBO> roleList = roleService.findAllRole();
		user = this.getApproveLevel(roleList, user);
		CreateAccount cmd = new CreateAccount();
		cmd.setAccounttype("2");
		cmd.setUsername(user.getAccount());
		cmd.setAccount(user.getAccount());
		cmd.setEmail(user.getEmail());
		cmd.setFirstname(user.getAccount());
		cmd.setLastname(user.getAccount());
		cmd.setPassword(this.createEPassword(user));
		if (domainId > 0) {
			cmd.setDomainid("" + domainId);
			JSONObject jo = JSONObject.fromObject(commandService
					.executeAndJsonReturn(cmd, resourcePoolsId));
			jo = JSONObject.fromObject(jo.getString("createaccountresponse"));
			log.debug("createAccount:" + jo);
		}
	}

	/**
	 * 定制elaster密码，仅对自服务用户、审核级别为普通用户、高级用户
	 * */
	private String createEPassword(TUserBO user) {
		StringBuilder ePass = new StringBuilder(50);
		try {
			ePass.append(user.getAccount());
			if (1 == user.getDeptId() || 2 == user.getDeptId()
					|| user.getRoleApproveLevel() < 3) {
				ePass.append(this.CLOUD);
			}
			//System.out.println("1:"+ePass.toString());
			ePass = new StringBuilder(DigestUtils.md5Hex((ePass.toString())
					.getBytes("UTF-8")));
			//System.out.println("2:"+ePass.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ePass.toString();
	}

	/**
	 * 获取用户的审核权限等级
	 */
	private TUserBO getApproveLevel(List<TRoleBO> roleList, TUserBO user) {
		if (null != roleList && !roleList.isEmpty()) {
			for (TRoleBO role : roleList) {
				if (user.getRoleId() == role.getRoleId()) {
					user.setRoleApproveLevel(role.getRoleApproveLevel());
				}
			}
		}
		return user;
	}

	private int createDomain(TUserBO user, Integer resourcePoolsId)
			throws Exception {
		int domainId = 0;
		CreateDomain cmd = new CreateDomain();
		cmd.setName(user.getAccount());
		JSONObject jo = JSONObject.fromObject(commandService
				.executeAndJsonReturn(cmd, resourcePoolsId));
		jo = JSONObject.fromObject(jo.getString("createdomainresponse"));
		if (jo.containsKey("domain")) {
			jo = JSONObject.fromObject(jo.getString("domain"));
			domainId = jo.getInt("id");
		}
		log.debug("createDomain:" + domainId);
		return domainId;
	}

}
