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
public class ProductGroupPK implements Serializable {
	private static final long serialVersionUID = -385962737026633665L;

	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Group group;
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ProductGroupPK that = (ProductGroupPK) o;
 
        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (product != null ? product.hashCode() : 0);
        result = 32 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}