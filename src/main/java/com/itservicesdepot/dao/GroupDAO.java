/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.Group;

public interface GroupDAO {

	public List<Group> getGroups();
	
	public Group getGroup(int id);
	
	public Group getGroup(String name);
	
	public int addGroup(Group Group);

	public void deleteGroup(Group Group);

	public void updateGroup(Group Group);
	
	public int getGroupUsedCount(int id);
}
