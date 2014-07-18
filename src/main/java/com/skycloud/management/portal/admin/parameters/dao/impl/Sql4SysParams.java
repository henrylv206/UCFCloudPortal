package com.skycloud.management.portal.admin.parameters.dao.impl;

public class Sql4SysParams {

	public static final String queryElasterApiCallLogSql = "select t.*, err.id log_id, err.description, err.create_dt, u.account user_name "
	        + " from   T_SCS_ASYNCJOB_ERROR err, T_SCS_USER u, ("
	        + "	select o.order_id, ins.id ins_id, ins.template_id, ins.template_type, ins.instance_name ins_name, a.apply_id,"
	        + "		o.creator_user_id user_id, o.type order_type, a.id asy_id, a.operation  "
	        + "		from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_ASYNCJOB a"
	        + "		   where o.STATE=4 and a.apply_id=0 and o.type=1 and ins.order_id = o.order_id 	   and a.instance_info_id=ins.id " + " "
	        + "		union all " + " " + "	select o.order_id, ins.id ins_id, ins.template_id, ins.template_type, ins.instance_name ins_name, a.apply_id,"
	        + "		o.creator_user_id user_id, o.type order_type, a.id asy_id, a.operation  "
	        + "		from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_ASYNCJOB a"
	        + "		   where o.STATE=4 and a.apply_id=0 and o.type<>1 and ins.id = o.instance_info_id 	   and a.instance_info_id=ins.id " + " "
	        + "		union all " + " " + "	select o.order_id, ins.id ins_id, ins.template_id, ins.template_type, ins.instance_name ins_name, a.apply_id,"
	        + "		o.creator_user_id user_id, o.type order_type, a.id asy_id, a.operation  "
	        + "		from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_ASYNCJOB a"
	        + "		   where o.STATE=4 and a.apply_id=1 and o.type=1 and ins.order_id = o.order_id 	   and a.instance_info_id=ins.id" + ") t 	   "
	        + "	   where" + "	   t.operation not in ('createVolume', 'deleteVolume', 'detachVolume', 'attachVolume')"
	        + "		and  t.asy_id=err.asy_id and u.id=t.user_id" + " " + " union all " + " "
	        + "	select o.order_id, ins.id ins_id, ins.template_id, ins.template_type, ins.instance_name ins_name, a.apply_id, "
	        + "		o.creator_user_id user_id, o.type order_type, a.id asy_id, a.operation, "
	        + "		err.id log_id, err.description, err.create_dt, u.account user_name   "
	        + "		from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_ASYNCJOB a, T_SCS_IRI r,		T_SCS_ASYNCJOB_ERROR err, T_SCS_USER u"
	        + "		   where o.STATE=4 and a.apply_id=0 and o.type = 1 and ins.order_id = o.order_id  "
	        + "		           and a.operation = 'createVolume'  " + "		   		   and a.instance_info_id=ins.id"
	        + "				   and a.id=err.asy_id and u.id=o.creator_user_id " + " " + " union all " + " "
	        + "	select o.order_id, ins.id ins_id, ins.template_id, ins.template_type, ins.instance_name ins_name, a.apply_id, "
	        + "		o.creator_user_id user_id, o.type order_type, a.id asy_id, a.operation, "
	        + "		err.id log_id, err.description, err.create_dt, u.account user_name   "
	        + "		from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_ASYNCJOB a, T_SCS_IRI r,		T_SCS_ASYNCJOB_ERROR err, T_SCS_USER u"
	        + "		   where o.STATE=4 and a.apply_id=0 and o.type=3 and ins.id = o.instance_info_id  "
	        + "		           and a.operation = 'deleteVolume' " + "		   		   and a.instance_info_id=ins.id"
	        + "				   and a.id=err.asy_id and u.id=o.creator_user_id " + " " + " union all " + " "
	        + "	select o.order_id, ins.id ins_id, ins.template_id, ins.template_type, ins.instance_name ins_name, a.apply_id, "
	        + "		o.creator_user_id user_id, o.type order_type, a.id asy_id, a.operation, "
	        + "		err.id log_id, err.description, err.create_dt, u.account user_name   "
	        + "		from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_ASYNCJOB a, T_SCS_IRI r,		T_SCS_ASYNCJOB_ERROR err, T_SCS_USER u"
	        + "		   where o.STATE=4 and a.apply_id=1 and o.type=1 and ins.order_id = o.order_id "
	        + "		           and a.operation in ('detachVolume', 'attachVolume') "
	        + "		   		   and a.instance_info_id=r.id and r.disk_instance_info_id=ins.id	   and a.id=err.asy_id and u.id=o.creator_user_id " + "";

	public static final String queryH3cApiCallLogSql = "select  err.id log_id, err.message description, err.createDate create_dt, u.id user_id, u.account user_name, ins.id ins_id, ins.instance_name ins_name"
	        + " from T_SCS_H3C_API_LOG err, T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_USER u"
	        + " where err.userid=o.creator_user_id and o.order_id=ins.order_id and o.type=1 and u.id=o.creator_user_id "
	        + " union "
	        + " select  err.id log_id, err.message description, err.createDate create_dt, u.id user_id, u.account user_name, ins.id ins_id, ins.instance_name ins_name"
	        + " from T_SCS_H3C_API_LOG err, T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_USER u"
	        + " where err.userid=o.creator_user_id and o.instance_info_id=ins.id and o.type<>1 and u.id=o.creator_user_id ";

	public static final String queryS3ApiCallLogSql = "select err.id log_id, err.description, err.create_dt, u.id user_id, u.account user_name, ins.id ins_id, ins.instance_name ins_name"
	        + " from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_USER u, T_SCS_ASYNCJOB_ERROR err"
	        + " where o.order_id=ins.order_id and o.type=1 and ins.id=err.asy_id and u.ID = o.CREATOR_USER_ID"
	        + " union "
	        + " select err.id log_id, err.description, err.create_dt, u.id user_id, u.account user_name, ins.id ins_id, ins.instance_name ins_name"
	        + " from T_SCS_ORDER o, T_SCS_INSTANCE_INFO ins, T_SCS_USER u, T_SCS_ASYNCJOB_ERROR err"
	        + " where o.instance_info_id=ins.id and o.type<>1 and ins.id=err.asy_id and u.ID = o.CREATOR_USER_ID ";

}
