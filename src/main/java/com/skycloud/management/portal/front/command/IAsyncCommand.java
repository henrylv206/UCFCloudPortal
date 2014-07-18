package com.skycloud.management.portal.front.command;

public interface IAsyncCommand extends ICommand {
	<E> E callback(IResult result);
}
