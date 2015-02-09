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
public class FieldCustFieldPK implements Serializable {
	private static final long serialVersionUID = -7522823594395752183L;

	@ManyToOne
	private Field field;
	
	@ManyToOne
	private CustField custField;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        FieldCustFieldPK that = (FieldCustFieldPK) o;
 
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (custField != null ? !custField.equals(that.custField) : that.custField != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (field != null ? field.hashCode() : 0);
        result = 100 * result + (custField != null ? custField.hashCode() : 0);
        return result;
    }

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public CustField getCustField() {
		return custField;
	}

	public void setCustField(CustField custField) {
		this.custField = custField;
	}	
}