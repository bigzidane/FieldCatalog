/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itservicesdepot.constant.ErrorCodeConstant;
import com.itservicesdepot.dao.EventDAO;
import com.itservicesdepot.model.Event;
import com.itservicesdepot.model.Result;

@Service("eventService")
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDAO eventDAO;

	@Override
	public List<Event> getEventByScreen() {
		return eventDAO.getEventsByScreen();
	}

	@Override
	public List<Event> getEventByField() {
		return eventDAO.getEventBysField();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result addEvent(Event event) {
		Result result = new Result();
		
		eventDAO.addEvent(event);
		
		return result;

	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result deleteEvent(Event event) {
		Result result = new Result();

		int usedCount = this.eventDAO.getEventUsedCount(event.getId());
		if (usedCount > 0) {
			result.setCode(ErrorCodeConstant.ERROR_DELETE_ENTRY_FOREIGN_KEY_VIOLATION);
		}
		else {
			eventDAO.deleteEvent(event);
		}
		
		return result;

	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public Result updateEvent(Event event) {
		Result result = new Result();

		eventDAO.updateEvent(event);
		
		return result;
	}

	@Override
	public List<Event> getEventsByTarget(String target) {
		return eventDAO.getEventsByTarget(target);
	}

	@Override
	public Event getEvent(int id) {
		return eventDAO.getEvent(id);
	}
	
	@Override
	public int getNewDisplayOrder(String target) {
		return eventDAO.getNewDisplayOrder(target);
	}
}
