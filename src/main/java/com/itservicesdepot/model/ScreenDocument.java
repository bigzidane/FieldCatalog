/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="SCREEN_DOCUMENT")
public class ScreenDocument extends BaseDocument {
	private static final long serialVersionUID = -134526124216419180L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "SCREEN_ID", nullable = false)
	private Screen target = new Screen();
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		ScreenDocument that = (ScreenDocument) o;
 
		if (that.getName().equalsIgnoreCase(this.getName()) &&
			that.target.getId() == this.target.getId())
			return true;
 
		if (that.getId() == this.getId()) return true;
		return false;
	}
 
	public int hashCode() {
		return new HashCodeBuilder(103, 105).   
			      append(name).toHashCode();
	}

	@Override
	public Screen getTarget() {
		return target;
	}

	@Override
	public void setTarget(Object target) {
		this.target = (Screen)target;
	}

	
}