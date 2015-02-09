/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.HashMap;
import java.util.List;

import com.itservicesdepot.model.Tag;

public interface TagDAO {

	public Tag getTag(int id);

	public Tag getTag(String name);

	public List<Tag> getSystemTags();
	
	public HashMap<Tag, Integer> getCloudTags();
}