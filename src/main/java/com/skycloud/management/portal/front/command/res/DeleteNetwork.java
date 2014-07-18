package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.IResult;
import com.skycloud.management.portal.front.command.impl.AsyncCommand;

public class DeleteNetwork extends AsyncCommand {
	
	public static final String COMMAND_NAME = "deleteNetwork";
	public static final String ID = "id";
	private long id;

	public DeleteNetwork(long id) {
		super(COMMAND_NAME);
		this.setId(id);
	}

	@Override
	public <E> E callback(IResult result) {
		return null;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public long getId() {
		return id;
	}

}
