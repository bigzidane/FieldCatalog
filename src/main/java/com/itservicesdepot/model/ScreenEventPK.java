/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ScreenEventPK implements Serializable {
	private static final long serialVersionUID = 1926020507651030719L;

	@ManyToOne
	private Screen screen;
	
	@ManyToOne
	private Event event;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ScreenEventPK that = (ScreenEventPK) o;
 
        if (screen != null ? !screen.equals(that.screen) : that.screen != null) return false;
        if (event != null ? !event.equals(that.event) : that.event != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (screen != null ? screen.hashCode() : 0);
        result = 13 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event custField) {
		this.event = custField;
	}	
}