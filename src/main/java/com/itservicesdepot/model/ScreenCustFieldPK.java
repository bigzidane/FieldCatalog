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
public class ScreenCustFieldPK implements Serializable {
	private static final long serialVersionUID = 6583467006965143897L;

	@ManyToOne
	private Screen screen;
	
	@ManyToOne
	private CustField custField;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ScreenCustFieldPK that = (ScreenCustFieldPK) o;
 
        if (screen != null ? !screen.equals(that.screen) : that.screen != null) return false;
        if (custField != null ? !custField.equals(that.custField) : that.custField != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (screen != null ? screen.hashCode() : 0);
        result = 99 * result + (custField != null ? custField.hashCode() : 0);
        return result;
    }

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public CustField getCustField() {
		return custField;
	}

	public void setCustField(CustField custField) {
		this.custField = custField;
	}	
}