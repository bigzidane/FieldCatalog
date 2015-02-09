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
public class FieldGroupPK implements Serializable {
	private static final long serialVersionUID = -475444422522985091L;

	@ManyToOne
	private Field field;
	
	@ManyToOne
	private Group group;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        FieldGroupPK that = (FieldGroupPK) o;
 
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (field != null ? field.hashCode() : 0);
        result = 101 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}	
}