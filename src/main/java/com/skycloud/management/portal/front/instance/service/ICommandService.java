package com.skycloud.management.portal.front.instance.service;

import com.skycloud.management.portal.front.command.ICommand;
import com.skycloud.management.portal.front.command.IResult;

public interface ICommandService {
  public Object executeAndJsonReturn(ICommand cmd,Integer resourcePoolID);

  public Object executeAndXMLReturn(ICommand cmd,Integer resourcePoolID);
	
	
	public IResult executeAsync(ICommand cmd,Integer resourcePoolID);
	
	/**
	 * 取得URL
	 * @param cmd
	 * @return String url
	 */
	public String getUrl(ICommand cmd,Integer resourcePoolID);
	
}
