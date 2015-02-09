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
public class FieldMessagePK implements Serializable {
	private static final long serialVersionUID = -3590075364120847480L;

	@ManyToOne
	private Field field;
	
	@ManyToOne
	private Message message;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        FieldMessagePK that = (FieldMessagePK) o;
 
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (field != null ? field.hashCode() : 0);
        result = 102 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}	
}