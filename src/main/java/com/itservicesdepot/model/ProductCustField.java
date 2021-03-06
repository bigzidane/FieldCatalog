/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="PRODUCT_CUST_FIELD")
@AssociationOverrides({
	@AssociationOverride(name = "pk.product", 
		joinColumns = @JoinColumn(name = "PRODUCT_ID")),
	@AssociationOverride(name = "pk.custField", 
		joinColumns = @JoinColumn(name = "CUST_FIELD_ID")) })
public class ProductCustField extends BaseModel {
	
	private static final long serialVersionUID = 1115372028583897790L;

	@EmbeddedId
	private ProductCustFieldPK pk = new ProductCustFieldPK();
	
	@Column(name="Value")
	private String value;
	
	@Transient
	public Product getProduct() {
		return getPk().getProduct();
	}
 
	public void setProduct(Product product) {
		getPk().setProduct(product);
	}
 
	@Transient
	public CustField getCustField() {
		return getPk().getCustField();
	}
 
	public void setCustField(CustField category) {
		getPk().setCustField(category);
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
 
		ProductCustField that = (ProductCustField) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null)
			return false;
 
		return true;
	}
 
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ProductCustFieldPK getPk() {
		return pk;
	}
	public void setPk(ProductCustFieldPK pk) {
		this.pk = pk;
	}
}