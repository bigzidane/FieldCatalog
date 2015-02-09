/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.service;

import java.util.List;

import com.itservicesdepot.model.Event;
import com.itservicesdepot.model.Result;

public interface EventService {
	public Event getEvent(int id);

	public List<Event> getEventsByTarget(String target);

	public List<Event> getEventByScreen();

	public List<Event> getEventByField();

	public Result addEvent(Event event);

	public Result deleteEvent(Event event);

	public Result updateEvent(Event event);
	
	public int getNewDisplayOrder(String target);
}