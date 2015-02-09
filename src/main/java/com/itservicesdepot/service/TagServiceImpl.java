/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.dao.TagDAO;
import com.itservicesdepot.model.Tag;

@Service("tagService")
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {
	
	@Autowired
    TagDAO tagDAO;
	
	public Tag getTag(int id) {
		Tag tag = new Tag();
		tag.setId(id);
		tag.setName("Item"+String.valueOf(id));
		
		return tag;
	}
	
	public List<Tag> getSystemTags() {
		List<Tag> listTag = tagDAO.getSystemTags();
		return listTag;
	}

	@Override
	public Tag getTag(String name) {
		return tagDAO.getTag(name);
	}

	public TagDAO getTagDAO() {
		return tagDAO;
	}

	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	@Override
	public Tag getNewTag(String name) {
		Tag newTag = new Tag();
		newTag.setName(name);
		return newTag;
	}
	
	public HashMap<Tag, Integer> getCloudTags() {
		return tagDAO.getCloudTags();
	}
}