/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.Screen;

public interface ScreenDAO {

	public List<Screen> getScreens();
	
	public List<Screen> getScreensByProductVersion(String productVersionId);
	
	public List<Screen> getDirectScreensByProductVersion(String productVersionId);
	
	public List<Screen> getChildScreens(int parentId);

	public int addScreen(Screen product);

	public int updateScreen(Screen product);

	public int deleteScreen(Screen product);

	public Screen getScreen(int id);

	public Screen getScreen(String id);
	
	public Screen getScreenOnly(int id);
	
	public List<Screen> searchByKeyword(String keyword)  throws Exception;
	
	public List<Screen> getScreenNameByIds(String ids);
}
