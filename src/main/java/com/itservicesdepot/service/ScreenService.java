/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenDocument;

public interface ScreenService {

	public List<Screen> getScreens();
	
	public List<Screen> getScreensByProductVersion(String productVersionId);
	
	public List<Screen> getDirectScreensByProductVersion(String productVersionId);
	
	public List<Screen> getParentIdScreens(String parentIds);
	
	public List<Screen> getParentScreens(String parentIds);
	
	public List<Screen> getChildScreens(int parentId);

	public Result addScreen(Screen screen);

	public int saveUpdateScreen(Screen screen);
	
	public int updateScreen(Screen screen);

	public int deleteScreen(Screen screen);

	public Screen getScreen(int id);

	public Screen getScreen(String name);
	
	public Screen getScreenOnly(int id);
	
	public List<Screen> searchByKeyword(String keyword) throws Exception;
	
	public int addDocument(ScreenDocument document);
	
	public List<Screen> getUniqueScreens();
	
	public List<Screen> getUniqueScreens(String productVersionId);
}
