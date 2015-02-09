/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.FileStorageService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.utils.ApplicationUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Add/Edit/Update screen pages.
 *  
 * Only Admin/Designer can use those pages
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="screenImageBean")
@ViewScoped
public class ScreenImageBean extends BaseBean {
	private static final long serialVersionUID = 9113029125883497311L;

	static Logger logger = Logger.getLogger(ScreenBean.class);
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{fileStorageService}")
    private FileStorageService fileStorageService;
	
	private Screen screen;
	private List<Field> fields;
    
	@PostConstruct
    public void init() {
		String screenId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(screenId)) {
			this.screen = this.screenService.getScreenOnly(Integer.valueOf(screenId));
			this.fields = this.fieldService.getFieldsByScreenId(Integer.valueOf(screenId));
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The screen has NOT been retrieved successfully from system. Please contact System Administrator.")));
		}
	}

	public String getScreenImagePath() {
		if (ValidateUtils.isObjectNotEmpty(this.screen) && ValidateUtils.isObjectNotEmpty(this.screen.getAttachedFile())) {
			return ApplicationUtils.getApplicationURL() + ApplicationConstant.SCREEN_IMAGE_PATTERN + this.screen.getAttachedFile();
		}
		else {
			return "";
		}
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
}