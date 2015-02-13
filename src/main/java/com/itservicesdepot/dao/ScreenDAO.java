/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.ClonedObject;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenDocument;

public interface ScreenDAO {

	public List<Screen> getScreens();
	
	public List<Screen> getScreensByProductVersion(String productVersionId);
	
	public List<Screen> getDirectScreensByProductVersion(String productVersionId);
	
	public List<Screen> getChildScreens(int parentId);

	public int addScreen(Screen product);

	public int saveUpdateScreen(Screen screen);
	
	public int updateScreen(Screen screen);

	public int deleteScreen(Screen product);

	public Screen getScreen(int id);

	public Screen getScreen(String id);
	
	public Screen getScreenOnly(int id);
	
	public List<Screen> searchByKeyword(String keyword)  throws Exception;
	
	public List<Screen> getScreenNameByIds(String ids);
	
	public List<ClonedObject> getClonedScreenMapping(int productVersionId);
	
	public void updateParentIds(int id, String parentIds);
	
	public ScreenDocument getDocument(String name, int screenId);

	public int addDocument(ScreenDocument document);
	
	public List<Screen> getUniqueScreens();
	
	public List<Screen> getUniqueScreens(String productVersionId);
}
