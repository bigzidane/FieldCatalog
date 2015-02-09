/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Group;
import com.itservicesdepot.model.Result;

public interface GroupService {

	public List<Group> getGroups();

	public Group getGroup(int i);

	public Group getGroup(String name);

	public Result addGroup(Group group);

	public Result deleteGroup(Group group);

	public Result updateGroup(Group group);

}
