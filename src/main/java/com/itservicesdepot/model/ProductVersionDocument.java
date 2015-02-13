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
@Table(name="PRODUCT_VERSION_DOCUMENT")
public class ProductVersionDocument extends BaseDocument {
	private static final long serialVersionUID = -134526124216419180L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PRODUCT_VERSION_ID", nullable = false)
	private ProductVersion target = new ProductVersion();
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		ProductVersionDocument that = (ProductVersionDocument) o;
 
		if (that.getName().equalsIgnoreCase(this.getName()) &&
			that.target.getId() == this.target.getId())
			return true;
 
		if (that.getId() == this.getId()) return true;
		return false;
	}
 
	public int hashCode() {
		return new HashCodeBuilder(105, 107).   
			      append(name).toHashCode();
	}

	@Override
	public ProductVersion getTarget() {
		return target;
	}

	@Override
	public void setTarget(Object productVersion) {
		this.target = (ProductVersion)productVersion;
	}
}