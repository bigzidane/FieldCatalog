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
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.springframework.security.access.prepost.PreAuthorize;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.model.Event;
import com.itservicesdepot.model.Result;
import com.itservicesdepot.service.EventService;
import com.itservicesdepot.utils.ValidateUtils;

/**
 * This class to handle 2 pages
 * 	1.	Manage Events
 * 	2.	Add a new Cust Field
 * 
 * Only Admin/Designer can perform Add/Edit/Delete custom fields
 * 
 * @author 	Nghia Do
 * @version 1.0
*/

@ManagedBean(name="eventMgntBean")
@ViewScoped
public class EventMgntBean extends BaseBean {
	private static final long serialVersionUID = 8198461069069899469L;

	static Logger logger = Logger.getLogger(EventMgntBean.class);
	
	@ManagedProperty("#{eventService}")
    private EventService eventService;
	
	// for Manage Events page
	private String target;
	private List<SelectItem> targets;
	private List<Event> events = new ArrayList<Event>();
	private List<String> eventDisplayOrders = new ArrayList<String>();
	
	// for Add Event page
	private Event event = new Event();
	
	@PostConstruct
	public void init() {
		targets = new ArrayList<SelectItem>();
		targets.add(new SelectItem(ApplicationConstant.SCREEN_TARGET_ID, ApplicationConstant.SCREEN_TARGET_ID));
		targets.add(new SelectItem(ApplicationConstant.FIELD_TARGET_ID, ApplicationConstant.FIELD_TARGET_ID));
	}
	
	public String onFlowProcess(FlowEvent event) {
		if ("Target".equalsIgnoreCase(event.getOldStep())) {
			this.events = this.getEventsByTarget(this.target);
			buildEventDisplayOrder(this.events);
		}
		return event.getNewStep();
    }
	
	private List<Event> getEventsByTarget(String selectedTarget) {
		return eventService.getEventsByTarget(selectedTarget);
	}
	
	private void buildEventDisplayOrder(List<Event> events) {
		for (int i=1; i<= events.size(); i++) {
			this.eventDisplayOrders.add(String.valueOf(i));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String addEvent() {
		try {
			
			this.buildEventModel(this.event);
			
        	this.getEventService().addEvent(this.event);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' Event is added successfully", event.getName())));
        	
        	this.reset();
        	
        	return ApplicationConstant.SUCCESS;
        	
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Event is NOT added successfully. Please contact System Administrator.", event.getName())));
        	
        	logger.error(e.getMessage(), e);
        
        	return ApplicationConstant.ERROR;
        }    
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String updateEvent(Event event) {
        try {
        	this.getEventService().updateEvent(event);
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' Event is update successfully", event.getName())));
        	
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Event is NOT added successfully. Please contact System Administrator.", event.getName())));
        	
        	logger.error(e.getMessage(), e);
        	
        	return ApplicationConstant.ERROR;
        }    
        
    }  
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_DESIGNER')")
	public String deleteEvent(Event event) {
		try {
			Result result = this.getEventService().deleteEvent(event);
            
			if (result.getCode() == ErrorCodeConstant.SUCCESS) {
				this.events = this.getEventsByTarget(this.target);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", String.format("The '%s' Event is deleted successfully", event.getName())));
			}
            else if (result.getCode() == ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Event is NOT deleted successfully. The event has been used already across the system. Please contact System Administrator.", event.getName())));
			}
            return ApplicationConstant.SUCCESS;
            
        } catch (Exception e) {
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", String.format("The '%s' Event is NOT deleted successfully. Please contact System Administrator.", event.getName())));
        	logger.error(e.getMessage(), e);
        	return ApplicationConstant.ERROR;       
        }    
	}
	
	private Event buildEventModel(Event event) {
		event.setOrder(String.valueOf(this.getEventService().getNewDisplayOrder(event.getTarget())+1));
		
		return event;
	}

	private void reset() {
		if (ValidateUtils.isObjectNotEmpty(this.event)) {
			this.event = new Event();
		}
	}
	
	public void updateEvent(RowEditEvent event) {
        updateEvent((Event)event.getObject());
    }
     
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<SelectItem> getTargets() {
		return targets;
	}

	public void setTargets(List<SelectItem> targets) {
		this.targets = targets;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<String> getEventDisplayOrders() {
		return eventDisplayOrders;
	}

	public void setEventDisplayOrders(List<String> eventDisplayOrders) {
		this.eventDisplayOrders = eventDisplayOrders;
	}
}
