package com.skycloud.management.portal.admin.menu.dao;

import java.util.List;

import com.skycloud.management.portal.admin.menu.entity.MenuBO;

public interface IMenuDAO {
	List<MenuBO> listMenu() throws Exception;
}
