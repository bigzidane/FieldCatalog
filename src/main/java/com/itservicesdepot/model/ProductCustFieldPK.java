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
public class ProductCustFieldPK implements Serializable {
	private static final long serialVersionUID = 5459689374683347551L;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private CustField custField;
	
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ProductCustFieldPK that = (ProductCustFieldPK) o;
 
        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (custField != null ? !custField.equals(that.custField) : that.custField != null) return false;
 
        return true;
    }
 
    public int hashCode() {
        int result;
        result = (product != null ? product.hashCode() : 0);
        result = 31 * result + (custField != null ? custField.hashCode() : 0);
        return result;
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public CustField getCustField() {
		return custField;
	}

	public void setCustField(CustField custField) {
		this.custField = custField;
	}	
}