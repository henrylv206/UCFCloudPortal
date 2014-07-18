package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class UpdateRecordStateUninstall extends BaseCommandPo {

	private static final String COMMAND = "updateIriRecordStateIsUninstall";
	
	public UpdateRecordStateUninstall (){
		super(COMMAND);
	}
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return "updateIriRecordStateIsUninstall";
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		return "update iri state to Uninstall when resume vmvolume ";
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		// TODO Auto-generated method stub
		return null;
	}

}
