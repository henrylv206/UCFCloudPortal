package com.skycloud.management.portal.front.command.impl;

import com.skycloud.management.portal.front.command.IAsyncCommand;

public abstract class AsyncCommand extends Command implements IAsyncCommand {

	public AsyncCommand(String name) {
		super(name);
	}
}
