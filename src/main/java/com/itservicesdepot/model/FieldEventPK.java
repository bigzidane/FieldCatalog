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
public class FieldEventPK implements Serializable {
	private static final long serialVersionUID = 8800529954643871563L;

	@ManyToOne
	private Field field;
	
	@ManyToOne
	private Event event;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        FieldEventPK that = (FieldEventPK) o;
 
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (event != null ? !event.equals(that.event) : that.event != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (field != null ? field.hashCode() : 0);
        result = 15 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event custField) {
		this.event = custField;
	}	
}