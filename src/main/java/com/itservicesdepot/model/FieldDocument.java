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
@Table(name="FIELD_DOCUMENT")
public class FieldDocument extends BaseDocument {
	private static final long serialVersionUID = -134526124216419180L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "FIELD_ID", nullable = false)
	private Field target = new Field();
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		FieldDocument that = (FieldDocument) o;
 
		if (that.getName().equalsIgnoreCase(this.getName()) &&
			that.target.getId() == this.target.getId())
			return true;
 
		if (that.getId() == this.getId()) return true;
		return false;
	}
 
	public int hashCode() {
		return new HashCodeBuilder(107, 109).   
			      append(name).toHashCode();
	}

	@Override
	public Field getTarget() {
		return target;
	}

	@Override
	public void setTarget(Object field) {
		this.target = (Field)field;
	}
}