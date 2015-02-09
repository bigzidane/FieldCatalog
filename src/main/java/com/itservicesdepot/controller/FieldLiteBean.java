/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.service.VersionService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class handles Add/Edit/Update field pages.
 *  
 * Only Admin/Designer can use those pages
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="fieldLiteBean")
@ViewScoped
public class FieldLiteBean extends FieldBean {
	private static final long serialVersionUID = -9082087186234348973L;

	static Logger logger = Logger.getLogger(FieldBean.class);
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{versionService}")
    private VersionService versionService;
	
	private List<User> owners;
	
	private Field field = new Field();
	
	private List<Field> fields;
	private Field selectedField = new Field();
	
	@PostConstruct
    public void init() {
		this.owners = this.userService.getUsers();
		
		String screenId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (ValidateUtils.isObjectNotEmpty(screenId)) {
			Screen screen = this.screenService.getScreenOnly(Integer.valueOf(screenId));
			this.fields = this.fieldService.getFieldsByScreenId(screen.getId());
		}
		else {
			this.fields = new ArrayList<Field>();
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addField(int screenId) {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			buildFieldModel(this.field, currentUserId, this.screenService.getScreenOnly(screenId));
			
            this.fieldService.addField(this.field);
			
			this.buildCallbackData(this.field);
			
			FacesContext.getCurrentInstance().addMessage("msgAddField", new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' field has been added to the system successfully.", field.getName())));
		
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			return ApplicationConstant.SUCCESS;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("msgAddField", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' field has NOT been added to the system successfully. Please contact System Administrator.",field.getName())));
			logger.error(e.getMessage(),e);
			
			RequestContext.getCurrentInstance().addCallbackParam("saved", false);
			return ApplicationConstant.ERROR;
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String assignField() {
		try {
			
			Field existingField = this.fieldService.getFieldOnly(this.selectedField.getId());
			existingField.setTagInfo(selectedField.getTagInfo());
			
			this.fieldService.updateField(existingField);
			
			this.buildCallbackData(existingField);
			this.reset();
			
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);
			return ApplicationConstant.SUCCESS;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("msgAssignField", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' field has NOT been assigned successfully. Please contact System Administrator.",field.getName())));
			logger.error(e.getMessage(),e);
			
			RequestContext.getCurrentInstance().addCallbackParam("saved", false);
			return ApplicationConstant.ERROR;
		}
	}
	
	private void buildCallbackData(Field field) {
		String data = String.format("%d,%.0f,%.0f,%.0f,%.0f,%s", field.getId(), 
				field.getTagInfo().getLeftTagPosition(), 
				field.getTagInfo().getTopTagPosition(),
				field.getTagInfo().getTagWidth(), 
				field.getTagInfo().getTagHeight(),
				field.getName());

		RequestContext.getCurrentInstance().addCallbackParam("data", data);
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public VersionService getVersionService() {
		return versionService;
	}

	public void setVersionService(VersionService versionService) {
		this.versionService = versionService;
	}

	public List<User> getOwners() {
		return owners;
	}

	public void setOwners(List<User> owners) {
		this.owners = owners;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Field getSelectedField() {
		return selectedField;
	}

	public void setSelectedField(Field selectedField) {
		this.selectedField = selectedField;
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	@Override
	public void reset() {
		this.selectedField = null;
		this.field = new Field();
	}
}