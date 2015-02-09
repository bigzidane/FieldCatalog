/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */
	 
package com.itservicesdepot.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.FieldMessage;
import com.itservicesdepot.model.Message;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.ScreenMessage;
import com.itservicesdepot.model.User;
import com.itservicesdepot.service.FieldService;
import com.itservicesdepot.service.MessageService;
import com.itservicesdepot.service.ScreenService;
import com.itservicesdepot.service.UserService;
import com.itservicesdepot.utils.DateTimeUtils;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class to handle 2 pages
 * 	1.	Manage Messages
 * 	2.	Add a new Message
 * 
 * Only Admin/Designer can perform Add/Edit/Delete messages
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="messageMgntBean")
@ViewScoped
public class MessageMgntBean extends BaseBean {
	private static final long serialVersionUID = 8054862683831021637L;

	static Logger logger = Logger.getLogger(MessageMgntBean.class);
	
	@ManagedProperty("#{messageService}")
    private MessageService messageService;
	
	@ManagedProperty("#{userService}")
    private UserService userService;
	
	@ManagedProperty("#{screenService}")
    private ScreenService screenService;
	
	@ManagedProperty("#{fieldService}")
    private FieldService fieldService;
	
	// for Manage Grop page
	private List<Message> messages = new ArrayList<Message>();
	
	// for Add Cust Field page
	private List<User> owners;
	private Message message = new Message();
	
	private DualListModel<Screen> screens;
	private DualListModel<Field> fields;
	
	private List<Screen> existingScreens;
	private List<Field> existingFields;
	
	@PostConstruct
	public void init() {
		this.owners = this.userService.getUsers();
		this.messages  = this.messageService.getMessages();
		this.existingScreens = this.screenService.getScreens();
		this.existingFields = this.fieldService.getFields();
		
		this.initAssignScreen(null);
		this.initAssignField(null);
	}
	
	
	private void initAssignScreen(Message message) {
		List<Screen> screenSource = new ArrayList<Screen>();
		screenSource.addAll(this.existingScreens);
		
        List<Screen> screenTarget = new ArrayList<Screen>();
    
        this.screens = new DualListModel<Screen>(screenSource, screenTarget);
        
        if (ValidateUtils.isObjectNotEmpty(message)) {
        	for (ScreenMessage screenMessage : message.getScreenMessages()) {
    			this.screens.getTarget().add(screenMessage.getScreen());
    			this.screens.getSource().remove(screenMessage.getScreen());
    		}
        }
	}
	
	private void initAssignField(Message message) {
		List<Field> fieldSource = new ArrayList<Field>();
		fieldSource.addAll(this.existingFields);
		
        List<Field> fieldTarget = new ArrayList<Field>();
    
        this.fields = new DualListModel<Field>(fieldSource, fieldTarget);
        
        if (ValidateUtils.isObjectNotEmpty(message)) {
        	for (FieldMessage fieldMessage : message.getFieldMessages()) {
    			this.fields.getTarget().add(fieldMessage.getField());
    			this.fields.getSource().remove(fieldMessage.getField());
    		}
        }
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addMessage() {
		try {
			Authentication auth=SecurityContextHolder.getContext().getAuthentication();
			int currentUserId = userService.getUser(auth.getName()).getId();
			
			this.buildMessageModel(this.message, currentUserId);
			this.buildScreenMessageModel(this.message);
			this.buildFieldMessageModel(this.message);
			
        	this.getMessageService().addMessage(this.message);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' message is added successfully.", message.getCode())));
        	
        	this.reset();
        	
        	return ApplicationConstant.SUCCESS;
        	
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' message is NOT added successfully.. Please contact System Administrator.", message.getCode())));
        	
        	logger.error(e.getMessage(), e);
        
        	return ApplicationConstant.ERROR;
        }    
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateMessage() {
		return updateMessage(this.message);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateMessage(Message message) {
        try {
        	this.buildScreenMessageModel(message);
        	this.buildFieldMessageModel(message);
        	
        	this.getMessageService().updateMessage(message);
        	
        	this.messages = this.getMessageService().getMessages();
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' message is update successfully.", message.getCode())));

        	this.reset();
        	
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' message is NOT updated successfully.. Please contact System Administrator.", message.getCode())));
        	
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;
        }    
        
    }  
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String deleteMessage(Message message) {
		try {
			Result result = this.getMessageService().deleteMessage(message);

			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				this.messages = this.getMessageService().getMessages();
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' message is deleted successfully.", message.getCode())));
			}
			else if (result.getCode() == ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' message is NOT deleted successfully. The message has been used already across the system. Please contact System Administrator.", message.getCode())));
			}
                       
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' message is NOT deleted successfully. Please contact System Administrator.", message.getCode()));
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;       
        }    
	}
	
	private Message buildMessageModel(Message message, int currentUserId) {
		Date now = DateTimeUtils.Now();
		
		message.setCreatedDate(now);
		message.setLastUpdatedDate(now);
		message.setLastUpdatedUserId(currentUserId);
	
		return message;
	}
	
	private void buildScreenMessageModel(Message message) {
		List<ScreenMessage> screenMessages = new ArrayList<ScreenMessage>();

		for (Screen screen : this.screens.getTarget()) {
		    ScreenMessage screenMessage = new ScreenMessage();
		    
		    screenMessage.setScreen(screen);
		    screenMessage.setMessage(message);
		    
		    screenMessages.add(screenMessage);
		}
		
		message.setScreenMessages(screenMessages);
	}

	private void buildFieldMessageModel(Message message) {
		List<FieldMessage> fieldMessages = new ArrayList<FieldMessage>();

		for (Field field : this.fields.getTarget()) {
		    FieldMessage fieldMessage = new FieldMessage();
		    
		    fieldMessage.setField(field);
		    fieldMessage.setMessage(message);
		    
		    fieldMessages.add(fieldMessage);
		}
		
		message.setFieldMessages(fieldMessages);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public void updateMessage(RowEditEvent event) {
        updateMessage((Message)event.getObject());
    }

	public void showMessage(Message message) {
		this.message = this.messageService.getMessage(Integer.valueOf(message.getId()).intValue());
		
		this.initAssignScreen(this.message);
		this.initAssignField(this.message);
	}
	
	private void reset() {
		if (ValidateUtils.isObjectNotEmpty(this.message)) {
			this.message = new Message();
			this.screens.getTarget().clear();
		}
	}
	
	public MessageService getMessageService() {
		return messageService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public List<User> getOwners() {
		return owners;
	}

	public void setOwners(List<User> owners) {
		this.owners = owners;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public ScreenService getScreenService() {
		return screenService;
	}

	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}

	public DualListModel<Screen> getScreens() {
		return screens;
	}

	public void setScreens(DualListModel<Screen> screens) {
		this.screens = screens;
	}

	public List<Screen> getExistingScreens() {
		return existingScreens;
	}

	public void setExistingScreens(List<Screen> existingScreens) {
		this.existingScreens = existingScreens;
	}


	public FieldService getFieldService() {
		return fieldService;
	}


	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}


	public DualListModel<Field> getFields() {
		return fields;
	}


	public void setFields(DualListModel<Field> fields) {
		this.fields = fields;
	}


	public List<Field> getExistingFields() {
		return existingFields;
	}


	public void setExistingFields(List<Field> existingFields) {
		this.existingFields = existingFields;
	}
}
