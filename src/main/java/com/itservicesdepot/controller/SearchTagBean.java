/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

import com.itservicesdepot.model.Tag;
import com.itservicesdepot.service.TagService;

/**
 * This class represents for Search Tag page
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="searchTagBean")
@ViewScoped
public class SearchTagBean extends BaseBean {
	private static final long serialVersionUID = 7927475043853437480L;

	private TagCloudModel model;

	@ManagedProperty("#{tagService}")
    private TagService tagService;
	
	private String paramId;
	
	@PostConstruct
	public void init() {
		HashMap<Tag, Integer> tags = this.tagService.getCloudTags();
		
		model = new DefaultTagCloudModel();
        
		for (Map.Entry<Tag, Integer> entry : tags.entrySet()) {
		    Tag tag = entry.getKey();
		    Integer value = entry.getValue();

		    DefaultTagCloudItem item = new DefaultTagCloudItem(tag.getName(), "/pages/account/main.xhtml?id=" + tag.getName(), value.intValue()); 
		    model.addTag(item);
		}
	}
	
	public TagCloudModel getModel() {
		return model;
	}

	public void setModel(TagCloudModel model) {
		this.model = model;
	}

	public TagService getTagService() {
		return tagService;
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
}
