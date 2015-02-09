/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.dao;

import java.util.List;

import com.itservicesdepot.model.Event;

public interface EventDAO {

	public Event getEvent(int id);

	public List<Event> getEventsByScreen();

	public List<Event> getEventBysField();

	public List<Event> getEventsByTarget(String target);

	public void addEvent(Event event);

	public void deleteEvent(Event event);

	public void updateEvent(Event event);
	
	public int getNewDisplayOrder(String target);
	
	public int getEventUsedCount(int id);
}
